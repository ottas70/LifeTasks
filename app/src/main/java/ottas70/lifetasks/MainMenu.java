package ottas70.lifetasks;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.Typeface;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import ottas70.lifetasks.TaskActivity.TaskActivity;


public class MainMenu extends DrawerActivity implements GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener{

    LinearLayout dailyTask;
    LinearLayout popularTask;
    LinearLayout nearbyTask;
    ImageView mainPhoto;
    ImageButton searchButton;
    ImageView sideMenu;
    ImageButton addTask;

    GoogleApiClient googleApiClient;
    Location location;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        dailyTask = (LinearLayout) findViewById(R.id.dailyTaskLayout);
        popularTask = (LinearLayout) findViewById(R.id.popularTaskLayout);
        nearbyTask = (LinearLayout) findViewById(R.id.nearbyTaskLayout);
        mainPhoto = (ImageView) findViewById(R.id.MainPhoto);
        searchButton = (ImageButton) findViewById(R.id.searchButton);
        sideMenu = (ImageView) findViewById(R.id.menuIcon);
        addTask = (ImageButton) findViewById(R.id.createTaskButton);

        if (googleApiClient == null) {
            googleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        //LifeTasks.sortByLocation(this);
        LifeTasks.sortByPopularity();

        Picasso.with(this).load("http://ottas70.com/images/mainPhoto.jpg").fit().centerCrop().into(mainPhoto);


        addDailyTask();
        addPopularTasks();
        //addNearbyTasks();

        sideMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainPhoto.getContext(), TaskMenuActivity.class);
                Bundle b = new Bundle();
                b.putInt("option", 1);
                i.putExtras(b);
                startActivity(i);
            }
        });

        addTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(addTask.getContext(), AddTask.class));
            }
        });

    }

    protected void onStart() {
        googleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        googleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onConnected(Bundle connectionHint) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        location = LocationServices.FusedLocationApi.getLastLocation(googleApiClient);
        LifeTasks.instance.getUser().setLastLocation(location);
        if(location != null){
        LifeTasks.sortByLocation(this);
        addNearbyTasks();
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }



    private void addDailyTask(){
        addDivider(dailyTask);
        if(LifeTasks.instance.dailyTask != null){
            addTask(LifeTasks.instance.dailyTask, dailyTask);
        }else{
            TextView text = new TextView(this);
            text.setText("No daily task today");
            text.setGravity(Gravity.CENTER);
            dailyTask.addView(text);
        }

        addDivider(dailyTask);
    }

    private void addPopularTasks(){
        addDivider(popularTask);
        for (int i = 0; i < 3; i++) {
            addTask(LifeTasks.instance.popularTasks.getTasks().get(i), popularTask);
            addDivider(popularTask);
        }
        TextView text = new TextView(this);
        text.setText("View more");
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(15);
        text.setTextColor(this.getResources().getColor(R.color.primary_darker));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainPhoto.getContext(), TaskMenuActivity.class);
                Bundle b = new Bundle();
                b.putInt("option",2);
                i.putExtras(b);
                startActivity(i);
            }
        });

        popularTask.addView(text);
    }

    private void addNearbyTasks(){
        addDivider(nearbyTask);
        for (int i = 0; i < 3; i++) {
            addTask(LifeTasks.instance.nearTasks.getTasks().get(i),nearbyTask);
            addDivider(nearbyTask);
        }
        TextView text = new TextView(this);
        text.setText("View more");
        text.setGravity(Gravity.CENTER_HORIZONTAL);
        text.setTextSize(15);
        text.setTextColor(this.getResources().getColor(R.color.primary_darker));
        text.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(mainPhoto.getContext(), TaskMenuActivity.class);
                Bundle b = new Bundle();
                b.putInt("option",3);
                i.putExtras(b);
                startActivity(i);
            }
        });

        nearbyTask.addView(text);
    }


    private void addTask(final Task task,LinearLayout layout){
        LayoutInflater vi = (LayoutInflater) getApplicationContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = vi.inflate(R.layout.list_row,null);

        TextView text = (TextView)view.findViewById(R.id.text);
        text.setText(task.getNazev());
        text.setTypeface(Typeface.SANS_SERIF);
        text.setTextColor(Color.BLACK);
        LifeTasks.setPaddinginDp(15, 10, 0, 0, text);
        text.setTextSize(16);

        ImageView image = (ImageView)view.findViewById(R.id.image);
        if(task.isDone()) {
            image.setBackgroundColor(this.getResources().getColor(R.color.primary_darker));
            Picasso.with(this).load(R.drawable.ic_action_accept).into(image);
        }else{
            image.setBackgroundColor(Color.RED);
            Picasso.with(this).load(R.drawable.ic_action_cancel).into(image);
        }
        LifeTasks.setPaddinginDp(5, 5, 5, 5, image);

        TextView popular = (TextView)view.findViewById(R.id.popularityText);
        popular.setText(task.getPopularity() + "/5.0");
        popular.setTypeface(Typeface.SANS_SERIF);
        popular.setTextColor(Color.BLACK);

        TextView difficulty = (TextView)view.findViewById(R.id.difficultyText);
        difficulty.setText(task.getDifficulty()+"");
        difficulty.setTypeface(Typeface.SANS_SERIF);
        difficulty.setTextColor(Color.BLACK);

        TextView points = (TextView)view.findViewById(R.id.pointsText);
        points.setText(task.getPoints()+"");
        points.setTypeface(Typeface.SANS_SERIF);
        points.setTextColor(Color.BLACK);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(popularTask.getContext(), TaskActivity.class);
                Bundle b = new Bundle();
                b.putInt("id", task.getId());
                i.putExtras(b);
                startActivity(i);
            }
        });


        layout.addView(view);
    }

    private void addDivider(LinearLayout layout){
        View divider = new View(this);
        divider.setBackgroundResource(R.drawable.horizontal_divider);
        layout.addView(divider);
    }

    public void onResume() {
        super.onResume();
        LifeTasks.sortByPopularity();
        popularTask.removeAllViews();
        addPopularTasks();
        if(location != null) {
            LifeTasks.sortByLocation(this);
            nearbyTask.removeAllViews();
            addNearbyTasks();
        }
    }



}
