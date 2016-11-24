package ottas70.lifetasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import ottas70.lifetasks.TaskActivity.TaskActivity;

/**
 * Created by Ottas on 17.1.2016.
 */
public class TaskAdapter extends BaseAdapter {

    Context context;
    ArrayList<Task> tasks;
    private static LayoutInflater inflater = null;

    public TaskAdapter(Context context, ArrayList<Task> tasks) {
        this.context = context;
        this.tasks = tasks;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return tasks.size();
    }

    @Override
    public Object getItem(int position) {
        return tasks.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = inflater.inflate(R.layout.list_row,null);
        }else{
            view = convertView;
        }
        TextView text = (TextView)view.findViewById(R.id.text);
        text.setText(tasks.get(position).getNazev());
        text.setTypeface(Typeface.SANS_SERIF);
        text.setTextColor(Color.BLACK);
        LifeTasks.setPaddinginDp(15,10,0,0,text);
        text.setTextSize(16);

        ImageView image = (ImageView)view.findViewById(R.id.image);
        if(tasks.get(position).isDone()) {
            image.setBackgroundColor(context.getResources().getColor(R.color.primary_darker));
            Picasso.with(context).load(R.drawable.ic_action_accept).into(image);
        }else{
            image.setBackgroundColor(Color.RED);
            Picasso.with(context).load(R.drawable.ic_action_cancel).into(image);
        }
        LifeTasks.setPaddinginDp(5, 5, 5, 5, image);

        TextView popular = (TextView)view.findViewById(R.id.popularityText);
        popular.setText(tasks.get(position).getPopularity()+"/5.0");
        popular.setTypeface(Typeface.SANS_SERIF);
        popular.setTextColor(Color.BLACK);

        TextView difficulty = (TextView)view.findViewById(R.id.difficultyText);
        difficulty.setText(tasks.get(position).getDifficulty()+"");
        difficulty.setTypeface(Typeface.SANS_SERIF);
        difficulty.setTextColor(Color.BLACK);

        TextView points = (TextView)view.findViewById(R.id.pointsText);
        points.setText(tasks.get(position).getPoints()+"");
        points.setTypeface(Typeface.SANS_SERIF);
        points.setTextColor(Color.BLACK);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, TaskActivity.class);
                Bundle b = new Bundle();
                b.putInt("id",tasks.get(position).getId());
                i.putExtras(b);
                context.startActivity(i);
            }
        });
        return view;
    }
}
