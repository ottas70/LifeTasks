package ottas70.lifetasks;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import java.util.UUID;

import cz.msebera.android.httpclient.Header;


public class ProfileActivity extends DrawerActivity {

    ImageView userPhoto;
    ImageView menu;
    ImageView profilePhoto;
    TextView username;
    ImageView editProfile;
    TextView infoText;

    TextView name;
    EditText nameEditText;
    TextView adress;
    EditText adressEditText;
    TextView work;
    EditText workEditText;

    ProgressBar progressBar;
    TextView level;
    TextView points;

    TextView completed;
    TextView progress;
    TextView usersTasks;
    ListView taskList;

    ColorStateList oldColors;
    boolean edit = false;
    private static final int SELECT_PICTURE_PROFILE = 1;
    private static final int SELECT_PICTURE_PHOTO = 2;
    boolean profileImageChange = false;
    boolean accountImageChange = false;
    Uri selectedImageURIProfile;
    Uri selectedImageURIAccount;
    String encodedStringProfile;
    String encodedStringAccount;
    ProgressDialog progressDialog;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        userPhoto = (ImageView) findViewById(R.id.userPhoto);
        menu = (ImageView) findViewById(R.id.menu);
        profilePhoto = (ImageView) findViewById(R.id.profilePhoto);
        username = (TextView) findViewById(R.id.username);
        editProfile = (ImageView) findViewById(R.id.editProfile);
        infoText = (TextView) findViewById(R.id.infoText);

        name = (TextView) findViewById(R.id.name);
        nameEditText = (EditText) findViewById(R.id.nameEdit);
        adress = (TextView) findViewById(R.id.adress);
        adressEditText = (EditText) findViewById(R.id.adressEdit);
        work = (TextView) findViewById(R.id.workText);
        workEditText = (EditText) findViewById(R.id.workEdit);

        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        level = (TextView) findViewById(R.id.level);
        points = (TextView) findViewById(R.id.points);

        completed = (TextView) findViewById(R.id.completed);
        progress = (TextView) findViewById(R.id.progress);
        usersTasks = (TextView) findViewById(R.id.usersTasks);
        taskList = (ListView) findViewById(R.id.taskList);

        progressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);
        progressBar.setMax(LifeTasks.instance.levels[LifeTasks.instance.getUser().getLevel()]);
        progressBar.setProgress(LifeTasks.instance.getUser().getPoints());

        level.setText("level " + String.valueOf(LifeTasks.instance.getUser().getLevel()));
        points.setText(String.valueOf(LifeTasks.instance.getUser().getPoints()) + "/" + String.valueOf(LifeTasks.instance.levels[LifeTasks.instance.getUser().getLevel()]));

        oldColors = completed.getTextColors();

        Picasso.with(this).load("http://ottas70.com/LifeTasks/images/" + LifeTasks.instance.getUser().getAccountPhotoName()).fit().centerCrop().into(userPhoto);
        Picasso.with(this).load("http://ottas70.com/LifeTasks/images/" + LifeTasks.instance.getUser().getProfilePhotoName()).transform(new CircleTransform()).into(profilePhoto);

        username.setText(LifeTasks.instance.getUser().getUsername());
        if (!LifeTasks.instance.getUser().getRealName().equals("")) {
            name.setText(LifeTasks.instance.getUser().getRealName());
        } else {
            name.setText("Real name");
        }
        if (!LifeTasks.instance.getUser().getAdress().equals("")) {
            adress.setText(LifeTasks.instance.getUser().getAdress());
        } else {
            adress.setText("Home adress");
        }
        if (!LifeTasks.instance.getUser().getWork().equals("")) {
            work.setText(LifeTasks.instance.getUser().getWork());
        } else {
            work.setText("School/Work");
        }

        taskList.setAdapter(new TaskAdapter(this, LifeTasks.instance.tasks.getTasks()));

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        completed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                completed.setTextColor(getResources().getColor(R.color.primary_darker));
                progress.setTextColor(oldColors);
                usersTasks.setTextColor(oldColors);
                taskList.setAdapter(new TaskAdapter(username.getContext(), LifeTasks.instance.getUser().getFinishedTasks()));
            }
        });

        progress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progress.setTextColor(getResources().getColor(R.color.primary_darker));
                completed.setTextColor(oldColors);
                usersTasks.setTextColor(oldColors);
                taskList.setAdapter(new TaskAdapter(username.getContext(), LifeTasks.instance.getUser().getTasksInProgress()));
            }
        });

        usersTasks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usersTasks.setTextColor(getResources().getColor(R.color.primary_darker));
                completed.setTextColor(oldColors);
                progress.setTextColor(oldColors);
                taskList.setAdapter(new TaskAdapter(username.getContext(), LifeTasks.instance.getUser().getUsersTasks()));
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!edit) {
                    edit = true;
                    editProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_24dp));
                    infoText.setVisibility(View.VISIBLE);

                    nameEditText.setVisibility(View.VISIBLE);
                    nameEditText.setText(name.getText());
                    name.setVisibility(View.GONE);

                    adressEditText.setVisibility(View.VISIBLE);
                    adressEditText.setText(adress.getText());
                    adress.setVisibility(View.GONE);

                    workEditText.setVisibility(View.VISIBLE);
                    workEditText.setText(work.getText());
                    work.setVisibility(View.GONE);
                } else {
                    LifeTasks.instance.getUser().setRealName(nameEditText.getText().toString());
                    LifeTasks.instance.getUser().setAdress(adressEditText.getText().toString());
                    LifeTasks.instance.getUser().setWork(workEditText.getText().toString());

                    ServerRequest request = new ServerRequest(name.getContext());
                    request.updateUserDataAsyncTask(LifeTasks.instance.getUser(), false, new GetCallback() {
                        @Override
                        public void done(Object o) {

                        }
                    });

                    edit = false;
                    editProfile.setImageDrawable(getResources().getDrawable(R.drawable.ic_settings_black_24dp));
                    infoText.setVisibility(View.GONE);

                    nameEditText.setVisibility(View.GONE);
                    name.setText(nameEditText.getText());
                    name.setVisibility(View.VISIBLE);

                    adressEditText.setVisibility(View.GONE);
                    adress.setText(adressEditText.getText());
                    adress.setVisibility(View.VISIBLE);

                    workEditText.setVisibility(View.GONE);
                    work.setText(workEditText.getText());
                    work.setVisibility(View.VISIBLE);

                    if (profileImageChange) {
                        ServerRequest serverRequest = new ServerRequest(name.getContext());
                        Bitmap bitmap = ((BitmapDrawable) profilePhoto.getDrawable()).getBitmap();
                        String path = LifeTasks.getPath(name.getContext(), selectedImageURIProfile);
                        serverRequest.encodeImage(bitmap, path, true, new GetCallback() {
                            @Override
                            public void done(Object o) {
                                encodedStringProfile = (String) o;
                                makeHTTPCall(encodedStringProfile,true);
                            }
                        });
                    }

                    if (accountImageChange) {
                        ServerRequest serverRequest = new ServerRequest(name.getContext());
                        Bitmap bitmap = ((BitmapDrawable) userPhoto.getDrawable()).getBitmap();
                        String path = LifeTasks.getPath(name.getContext(), selectedImageURIAccount);
                        serverRequest.encodeImage(bitmap, path, true, new GetCallback() {
                            @Override
                            public void done(Object o) {
                                encodedStringAccount = (String) o;
                                makeHTTPCall(encodedStringAccount,false);
                            }
                        });
                    }

                    profileImageChange = false;
                    accountImageChange = false;
                }
            }
        });

        profilePhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_PROFILE);
                }
            }
        });

        userPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (edit) {
                    Intent i = new Intent();
                    i.setType("image/*");
                    i.setAction(Intent.ACTION_GET_CONTENT);
                    startActivityForResult(Intent.createChooser(i, "Select Picture"), SELECT_PICTURE_PHOTO);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SELECT_PICTURE_PROFILE) {
            if (resultCode == RESULT_OK) {
                profileImageChange = true;
                selectedImageURIProfile = data.getData();

                Picasso.with(this).load(selectedImageURIProfile).transform(new CircleTransform()).into(profilePhoto);
            }
        }
        if (requestCode == SELECT_PICTURE_PHOTO) {
            if (resultCode == RESULT_OK) {
                accountImageChange = true;
                selectedImageURIAccount = data.getData();
                Picasso.with(this).load(selectedImageURIAccount).fit().centerCrop().into(userPhoto);

            }
        }
    }


    public void makeHTTPCall(String encodedString, final boolean profile) {
        RequestParams params = new RequestParams();
        params.put("image", encodedString);
        final String filename = getUniqueFilename(profile);
        params.put("filename", filename);
        storeFilename(profile,filename);
        AsyncHttpClient client = new AsyncHttpClient();
        client.post("http://ottas70.com/LifeTasks/UploadImage.php",
                params, new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        Toast.makeText(getApplicationContext(), "Changes were succesfully saved", Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                        Toast.makeText(getApplicationContext(), "Image Upload failed", Toast.LENGTH_LONG).show();
                    }

                });
    }

    private String getUniqueFilename(boolean profile){
        ContentResolver cR = this.getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        Uri uri;
        String firstPart = LifeTasks.instance.getUser().getUsername();
        if(profile){
            uri = selectedImageURIProfile;
            firstPart += "_profile_";
        }else{
            uri = selectedImageURIAccount;
            firstPart += "_account_";
        }
        String fileType = mime.getExtensionFromMimeType(cR.getType(uri));

        String uniqueID = UUID.randomUUID().toString();

        return firstPart+uniqueID+"."+fileType;
    }

    private void storeFilename(boolean profile, final String filename){
        ServerRequest request = new ServerRequest(name.getContext());
        if(profile){
            request.uploadImageName(LifeTasks.instance.getUser(), filename, 1, false, new GetCallback() {
                @Override
                public void done(Object o) {
                    LifeTasks.instance.getUser().setProfilePhotoName(filename);
                }
            });
        }else{
            request.uploadImageName(LifeTasks.instance.getUser(), filename, 2, false, new GetCallback() {
                @Override
                public void done(Object o) {
                    LifeTasks.instance.getUser().setAccountPhotoName(filename);
                }
            });
        }
    }


}
