package ottas70.lifetasks;

import android.app.Activity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by Ottas on 1.11.2015.
 */
public class ImageDetail extends DrawerActivity {

    ImageView photo;
    int position;
    ArrayList<String> urls;
    ImageButton menu;

    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        photo = (ImageView)findViewById(R.id.photo);
        photo.setOnTouchListener(new OnSwipeTouchListener(this){
            public void onSwipeRight(){
                if(position != 0){
                    position--;
                    drawImage(position);
                }
            }
            public void onSwipeLeft(){
                if(position != urls.size()){
                    position++;
                    drawImage(position);
                }
            }
        });

        menu = (ImageButton)findViewById(R.id.imageButton);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        Bundle b = getIntent().getExtras();
        urls = b.getStringArrayList("array");
        position = b.getInt("position");

        drawImage(position);
    }

    private void drawImage(int id){
        Picasso.with(this).load(urls.get(id)).into(photo);
    }

}
