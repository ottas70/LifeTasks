package ottas70.lifetasks.TaskActivity;

import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ottas70.lifetasks.DrawerActivity;
import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.R;
import ottas70.lifetasks.Review;
import ottas70.lifetasks.ServerRequest;
import ottas70.lifetasks.Task;


public class TaskActivity extends DrawerActivity {


    LinearLayout informaceView;
    ImageView mainPhoto;
    ImageView menu;

    ArrayList<String> photosURLs = new ArrayList<>();
    Task task;
    TaskReviews tr;
    boolean start = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        informaceView = (LinearLayout)findViewById(R.id.informaceView);
        mainPhoto = (ImageView)findViewById(R.id.mainPhoto);
        menu = (ImageView)findViewById(R.id.menu);

        Bundle b = getIntent().getExtras();
        task = LifeTasks.instance.tasks.getTaskByID(b.getInt("id"));
        Picasso.with(this).load(task.getMainPhotoUrl()).fit().centerCrop().into(mainPhoto);

        informaceView.setPadding(0, 25, 0, 0);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        nactiDetail();
        //addDivider();
        nactiInformace();
        addDivider();
        nactiFotky();

    }

    @Override
    public void onResume() {
        super.onResume();
        if(!start) {
            informaceView.removeView(tr);
            nactiRecenze();
        }
    }


    private void nactiInformace(){
        informaceView.addView(new TaskInformations(this, task));
    }

    private void nactiRecenze(){
        ServerRequest request = new ServerRequest(this);
        request.getReviewsAsyncTask(task, false, new GetCallback() {
            @Override
            public void done(Object o) {
                start = false;
                task.setReviews((ArrayList<Review>) o);
                tr = new TaskReviews(informaceView.getContext(), task);
                informaceView.addView(tr);
            }
        });

    }

    private void nactiFotky(){
        ServerRequest request2 = new ServerRequest(this);
        request2.getPhotosUrlAsyncTask(task, false, new GetCallback() {
            @Override
            public void done(Object o) {
                photosURLs = (ArrayList<String>) o;
                informaceView.addView(new TaskPhotos(informaceView.getContext(), photosURLs, task));
                nactiRecenze();
            }
        });

    }

    private void nactiDetail(){
        informaceView.addView(new TaskDetails(this, task, getFragmentManager()));
    }


    private void addDivider(){
        View divider = new View(this);
        divider.setBackgroundResource(R.drawable.horizontal_divider);
        informaceView.addView(divider);
    }

}
