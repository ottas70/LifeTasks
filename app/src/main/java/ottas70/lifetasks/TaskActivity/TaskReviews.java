package ottas70.lifetasks.TaskActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.AppCompatButton;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.R;
import ottas70.lifetasks.Review;
import ottas70.lifetasks.Task;
import ottas70.lifetasks.WriteReviewActivity;

import static ottas70.lifetasks.R.color.primary_darker;

/**
 * Created by Ottas on 27.10.2015.
 */
public class TaskReviews extends LinearLayout {

    Context context;
    boolean rozbaleno = false;
    Task task;

    public TaskReviews(Context context,Task task) {
        super(context);
        this.context = context;
        this.task = task;
        setOrientation(VERTICAL);
        setBackgroundColor(getResources().getColor(R.color.layout_grey1));
        addTitle();
        addReviewButton();
        if(!task.getReviews().isEmpty()){
            for( Review r : task.getReviews()){
                pridejRecenzi(r);
                addDivider();
            }
        }
    }

    private void addReviewButton(){
        AppCompatButton btn = new AppCompatButton(context);
        btn.setText("Write A Review");
        btn.setTextColor(Color.WHITE);
        btn.setBackgroundColor(getResources().getColor(R.color.primary_darker));
        LifeTasks.setMargininDpforLinearLayout(68, 10, 48, 20, btn);
        LifeTasks.setPaddinginDp(60, 0, 60, 0, btn);
        btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, WriteReviewActivity.class);
                Bundle b = new Bundle();
                b.putInt("id",task.getId());
                i.putExtras(b);
                context.startActivity(i);
            }
        });

        addView(btn);

    }

    private void addTitle(){
        TextView nadpis = new TextView(context);
        nadpis.setText("Reviews (" + task.getReviews().size()+")");
        nadpis.setTypeface(Typeface.SANS_SERIF);
        nadpis.setTextSize(14);
        nadpis.setGravity(Gravity.CENTER);
        nadpis.setPadding(0, 15, 0, 0);

        addView(nadpis);
    }

    private void pridejRecenzi(Review r){
        RelativeLayout nadpisHvezdy = new RelativeLayout(context);
        LifeTasks.setPaddinginDp(10, 5, 10, 0, nadpisHvezdy);
        addView(nadpisHvezdy);

        TextView nadpis = new TextView(context);
        nadpis.setText(r.getNadpis());
        nadpis.setTextSize(20);
        nadpis.setTextColor(Color.BLACK);
        nadpis.setPadding(0, 0, 50, 0);
        nadpisHvezdy.addView(nadpis);

        LinearLayout hvezdy = new LinearLayout(context);
        hvezdy.setOrientation(HORIZONTAL);

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        nadpisHvezdy.addView(hvezdy,lp);

        for (int i = 1; i <= 5 ; i++) {
            ImageView star = new ImageView(context);
            if(i <= r.getStars()){
                star.setImageResource(R.drawable.rating_star);
            }else{
                star.setImageResource(R.drawable.rating_empty_star);
            }
            star.setPadding(0,10,0,0);
            hvezdy.addView(star);
        }

        TextView jmeno = new TextView(context);
        jmeno.setText(r.getUser().getUsername());
        jmeno.setTextSize(10);
        jmeno.setTextColor(Color.GRAY);
        LifeTasks.setPaddinginDp(20, 0, 0, 0, jmeno);
        addView(jmeno);

        final TextView recenze = new TextView(context);
        recenze.setText(r.getText());
        recenze.setTextColor(Color.BLACK);
        LifeTasks.setPaddinginDp(20, 0, 0, 10, recenze);
        recenze.setTextSize(12);
        recenze.setEllipsize(TextUtils.TruncateAt.END);
        recenze.setMaxLines(5);

        recenze.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rozbaleno) {
                    recenze.setMaxLines(5);
                    rozbaleno = false;
                } else {
                    recenze.setMaxLines(Integer.MAX_VALUE);
                    rozbaleno = true;
                }
            }
        });
        addView(recenze);

    }

    private void addDivider(){
        View divider = new View(context);
        divider.setBackgroundResource(R.drawable.horizontal_divider);
        LifeTasks.setMargininDpforLinearLayout(20,0,20,0,divider);
        addView(divider);
    }

}


