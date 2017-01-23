package ottas70.lifetasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import cz.msebera.android.httpclient.Header;

public class AddTask extends Activity {

    ImageView mainphoto;
    Button addMainImage;
    LinearLayout stars;
    EditText etName;
    EditText etInfo;
    EditText etStreet;
    EditText etCity;
    EditText etCountry;
    EditText etPoints;
    ImageButton addImages;
    ImageButton removeImage;
    MyGridView imageGrid;
    Button btn_createTask;

    Task task;
    ProgressDialog progressDialog;

    int starsCount = 1;
    int SELECT_PICTURE = 1;
    int SELECT_MULTIPLE_PICTURES = 2;
    Uri selectedImageURIPhoto;
    boolean taskPhotoChange = false;
    ArrayList<Uri> uris = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        mainphoto = (ImageView)findViewById(R.id.mainPhotoEdit);
        addMainImage = (Button)findViewById(R.id.addImage);
        stars = (LinearLayout)findViewById(R.id.stars);
        etName = (EditText) findViewById(R.id.etName);
        etInfo = (EditText) findViewById(R.id.etInfo);
        etStreet = (EditText) findViewById(R.id.etStreet);
        etCity = (EditText) findViewById(R.id.etCity);
        etCountry = (EditText) findViewById(R.id.etCountry);
        etPoints = (EditText) findViewById(R.id.etPoints);
        addImages = (ImageButton)findViewById(R.id.addPhotos);
        removeImage = (ImageButton)findViewById(R.id.removeImage);
        imageGrid = (MyGridView)findViewById(R.id.imageGridView);
        btn_createTask = (Button)findViewById(R.id.btn_createTask);

        drawStars();

        addMainImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addMainImage.setVisibility(View.GONE);
                mainphoto.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent i = new Intent();
                        i.setType("image/*");
                        i.setAction(Intent.ACTION_GET_CONTENT);
                        startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
                    }
                });
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE);
            }
        });

        addImages.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent();
                i.setType("image/*");
                i.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_MULTIPLE_PICTURES);
            }
        });

        removeImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!uris.isEmpty()) {
                    uris.remove(uris.size() - 1);
                    imageGrid.setNumColumns(4);
                    imageGrid.setAdapter(new ImageAdapter(removeImage.getContext(), uris,true));
                }
            }
        });

        btn_createTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadNewTask();
            }
        });

    }

    private void drawStars() {
        stars.removeAllViews();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(this);
            star.setTag(i + 1);
            if (i < starsCount) {
                star.setImageResource(R.drawable.review_fill_star);
            } else {
                star.setImageResource(R.drawable.review_empty_star);
            }
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (v.getTag() instanceof Integer) {
                        starsCount = (Integer) v.getTag();
                        drawStars();
                    }
                }
            });
            stars.addView(star);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE) {
            if (resultCode == RESULT_OK) {
                taskPhotoChange = true;
                selectedImageURIPhoto = data.getData();

                Picasso.with(this).load(selectedImageURIPhoto).fit().centerCrop().into(mainphoto);
            }
        }

        if(requestCode == SELECT_MULTIPLE_PICTURES){
            if(resultCode == RESULT_OK){
                uris.add(data.getData());
                imageGrid.setNumColumns(4);
                imageGrid.setAdapter(new ImageAdapter(this, uris,true));
            }
        }

    }

    private boolean validate(){
        boolean valid = true;

        String name = etName.getText().toString();
        String info = etInfo.getText().toString();
        String street = etStreet.getText().toString();
        String city = etCity.getText().toString();
        String country = etCountry.getText().toString();
        String points = etPoints.getText().toString();

        if(name.isEmpty()){
            etName.setError("Enter name");
            valid = false;
        }

        if(info.isEmpty()){
            etInfo.setError("Enter informations");
            valid = false;
        }

        if(street.isEmpty() && city.isEmpty() && country.isEmpty()){
            etStreet.setError("Enter adress");
            valid = false;
        }

        if(points.isEmpty()){
            etPoints.setError("Enter points");
            valid = false;
        }

        if(!taskPhotoChange){
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(AddTask.this);
            dialogBuilder.setMessage("Select task image");
            dialogBuilder.setPositiveButton("Ok", null);
            dialogBuilder.show();
            valid = false;
        }
        return valid;
    }

    private void uploadNewTask(){
        if(!validate()){
            return;
        }

        double lat = Double.MAX_VALUE;
        double lng = Double.MAX_VALUE;

        Geocoder gc = new Geocoder(this);
        String location = (etStreet.getText().toString() + "," + etCity.getText().toString() + "," + etCountry.getText().toString());
        List<Address> list = null;
        try {
            list = gc.getFromLocationName(location, 1);
            if (!list.isEmpty()) {
                Address add = list.get(0);

                lat = add.getLatitude();
                lng = add.getLongitude();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

         task = new Task(etName.getText().toString(),etInfo.getText().toString(),starsCount,getUniqueFilename(selectedImageURIPhoto),
                 new Adress(etStreet.getText().toString(),etCity.getText().toString(),etCountry.getText().toString()),
                 1,Integer.parseInt(etPoints.getText().toString()),0,LifeTasks.instance.getUser().getUsername(),lat,lng);
        final ServerRequest request = new ServerRequest(this);
        request.uploadTask(task, false, new GetCallback() {
            @Override
            public void done(Object o) {
                task.setId((Integer) o);
                LifeTasks.instance.tasks.getTasks().add(task);
                  request.encodeImage(((BitmapDrawable) mainphoto.getDrawable()).getBitmap(), LifeTasks.getPath(etCity.getContext(), selectedImageURIPhoto), false, new GetCallback() {
                      @Override
                      public void done(Object o) {
                          makeHTTPCall((String) o,task.getMainPhotoUrl(),true);
                          task.setMainPhotoUrl("http://ottas70.com/LifeTasks/images/" + task.getMainPhotoUrl());
                      }
                  });
                for (int i = 0; i < uris.size(); i++) {
                    ImageView iv = (ImageView) imageGrid.getAdapter().getView(i,null,null);
                    Bitmap bitmap = ((BitmapDrawable)iv.getDrawable()).getBitmap();
                    final String filename = getUniqueFilename(uris.get(i));
                    request.encodeImage(bitmap, LifeTasks.getPath(btn_createTask.getContext(), uris.get(i)), false, new GetCallback() {
                        @Override
                        public void done(Object o) {
                            makeHTTPCall((String) o,filename,false);
                        }
                    });
                }
                finish();
            }
        });
    }

    private String getUniqueFilename(Uri uri){
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        String firstPart = LifeTasks.instance.getUser().getUsername();
        firstPart += "_task_";
        String fileType = mime.getExtensionFromMimeType(cR.getType(uri));

        String uniqueID = UUID.randomUUID().toString();

        return firstPart+uniqueID+"."+fileType;
    }

    public void makeHTTPCall(String encodedString, final String filename, final boolean mainPhoto) {
        RequestParams params = new RequestParams();
        params.put("image", encodedString);
        params.put("filename", filename);
        storeFilename(filename);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://ottas70.com/LifeTasks/UploadImage.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        if(mainPhoto) {
                            Toast.makeText(getApplicationContext(), "Task was sucesfully uploaded", Toast.LENGTH_LONG).show();
                        }else{
                            ServerRequest request = new ServerRequest(etCity.getContext());
                            request.uploadPhotoToTask(task, filename, false, new GetCallback() {
                                @Override
                                public void done(Object o) {

                                }
                            });
                        }
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        if(mainPhoto) {
                            Toast.makeText(getApplicationContext(), "Task upload failed", Toast.LENGTH_LONG).show();
                        }
                    }

                });
    }

    private void storeFilename(final String filename){
        ServerRequest request = new ServerRequest(etName.getContext());

    }

}
