package ottas70.lifetasks;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;

/**
 * Created by Ottas on 31.1.2016.
 */
public class WriteReviewActivity extends DrawerActivity {

    LinearLayout layout;
    LinearLayout stars;
    int starsCount = 5;
    EditText title;
    EditText review;
    Button btn;
    ImageButton menu;
    Context c = this;
    Task task;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_review);

        Bundle b = getIntent().getExtras();
        task = LifeTasks.instance.tasks.getTaskByID(b.getInt("id"));

        layout = (LinearLayout)findViewById(R.id.layout2);
        title = (EditText)findViewById(R.id.input_title);
        review = (EditText)findViewById(R.id.input_review);
        btn = (Button)findViewById(R.id.btn_submit);
        menu = (ImageButton)findViewById(R.id.menu);

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final Review r = new Review(title.getText().toString(),starsCount,review.getText().toString(),LifeTasks.instance.getUser());
                ServerRequest request = new ServerRequest(c);
                request.uploadReviewAsyncTask(task, r, true, new GetCallback() {
                    @Override
                    public void done(Object o) {
                       task.getReviews().add(r);
                       finish();
                    }
                });

            }
        });

        stars = new LinearLayout(this);
        stars.setGravity(Gravity.CENTER);
        LifeTasks.setPaddinginDp(0, 10, 0, 0, stars);
        layout.addView(stars);

        drawStars();

    }

    private void drawStars(){
        stars.removeAllViews();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(this);
            star.setTag(i+1);
            if(i < starsCount){
                star.setImageResource(R.drawable.review_fill_star);
            }else{
                star.setImageResource(R.drawable.review_empty_star);
            }
            star.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(v.getTag() instanceof Integer){
                        starsCount = (Integer)v.getTag();
                        drawStars();
                    }
                }
            });
            stars.addView(star);
        }
    }


}
