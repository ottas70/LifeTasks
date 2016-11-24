package ottas70.lifetasks;

import android.location.Address;
import android.location.Geocoder;
import android.media.Image;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.List;

public class MapActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.
    ImageButton back;
    Task task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);

        back = (ImageButton)findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Bundle b = getIntent().getExtras();
        task = LifeTasks.instance.tasks.getTaskByID(b.getInt("id"));

        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();
            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {
        String location = "";
        if(task.getAdress().getStreet() != null){
            location += (task.getAdress().getState()+","+
                         task.getAdress().getCity()+","+
                         task.getAdress().getStreet());
        }else{
            if(task.getAdress().getCity() != null){
                location += (task.getAdress().getState()+","+
                        task.getAdress().getCity());
            }else{
                if(task.getAdress().getState() != null){
                    location += (task.getAdress().getState());
                }
            }
        }

        Geocoder gc = new Geocoder(this);
        try {
            List<Address> list = gc.getFromLocationName(location, 1);
            if(!list.isEmpty()) {
                Address add = list.get(0);
                String locality = add.getLocality();

                double lat = add.getLatitude();
                double lng = add.getLongitude();

                mMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)));
                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng), 15));
            }else{
                Toast.makeText(this,"Location does not exist",Toast.LENGTH_LONG).show();
            }

        } catch (IOException e) {
            Toast.makeText(this,"Location does not exist",Toast.LENGTH_LONG).show();
        }

    }
}
