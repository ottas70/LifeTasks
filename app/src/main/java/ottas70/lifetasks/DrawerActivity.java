package ottas70.lifetasks;

import android.app.Activity;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;


public class DrawerActivity extends Activity {

    public DrawerLayout drawerLayout;
    public FrameLayout frameLayout;
    public ListView drawerList;
    ImageView profile;
    TextView username;
    TextView email;
    ImageView userImage;

    String[] array = { "Home", "Activity","Search Tasks", "Profile","Settings","Feedback" };

    int[] icons = {R.drawable.ic_home_black_24dp,R.drawable.ic_notifications_black_24dp,R.drawable.ic_search_black_24dp,
                   R.drawable.ic_account_circle_black_24dp,R.drawable.ic_settings_black_24dp,
                   R.drawable.ic_feedback_black_24dp};

    Class[] classes = {MainMenu.class,MainMenu.class,TaskMenuActivity.class,ProfileActivity.class,MainMenu.class,MainMenu.class};

    @Override
    public void setContentView(int layoutResID) {

        drawerLayout = (DrawerLayout) getLayoutInflater().inflate(R.layout.drawer_activity, null);
        frameLayout = (FrameLayout) drawerLayout.findViewById(R.id.content_frame);
        drawerList = (ListView) drawerLayout.findViewById(R.id.drawerList);
        profile = (ImageView) drawerLayout.findViewById(R.id.profileImage);
        username = (TextView) drawerLayout.findViewById(R.id.username);
        email = (TextView) drawerLayout.findViewById(R.id.email);
        userImage = (ImageView)drawerLayout.findViewById(R.id.userImage);

        getLayoutInflater().inflate(layoutResID, frameLayout, true);

        super.setContentView(drawerLayout);

        Picasso.with(this).load("http://ottas70.com/LifeTasks/images/" + LifeTasks.instance.getUser().getAccountPhotoName()).fit().centerCrop().into(userImage);
        Picasso.with(this).load("http://ottas70.com/LifeTasks/images/" + LifeTasks.instance.getUser().getProfilePhotoName()).transform(new CircleTransform()).into(profile);

        username.setText(LifeTasks.instance.getUser().getUsername());
        email.setText(LifeTasks.instance.getUser().getEmail());


        drawerList.setAdapter(new DrawerAdapter(this,array,icons,classes));

        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        Picasso.with(this).load("http://ottas70.com/LifeTasks/images/" + LifeTasks.instance.getUser().getAccountPhotoName()).fit().centerCrop().into(userImage);
        Picasso.with(this).load("http://ottas70.com/LifeTasks/images/" + LifeTasks.instance.getUser().getProfilePhotoName()).transform(new CircleTransform()).into(profile);
    }
}
