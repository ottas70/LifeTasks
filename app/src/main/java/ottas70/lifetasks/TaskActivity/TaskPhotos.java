package ottas70.lifetasks.TaskActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.nfc.tech.NfcBarcode;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;

import java.util.ArrayList;

import ottas70.lifetasks.ImageAdapter;
import ottas70.lifetasks.ImageDetail;
import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.MyGridView;
import ottas70.lifetasks.R;
import ottas70.lifetasks.Task;

/**
 * Created by Ottas on 28.10.2015.
 */
public class TaskPhotos extends LinearLayout {

    Context context;
    Task task;
    ArrayList<String> photosURLs;
    ArrayList<String> mainPhotosURLs = new ArrayList<>();

    public TaskPhotos(Context context,ArrayList<String> photosURLs,Task task) {
        super(context);
        this.context = context;
        this.photosURLs = photosURLs;
        setBackgroundColor(getResources().getColor(R.color.layout_grey2));
        int size = 4;
        if(photosURLs.size() < 4){
            size = photosURLs.size();
        }
        for (int i = 0; i < size; i++) {
           mainPhotosURLs.add(photosURLs.get(i));
        }
        this.task = task;
        setOrientation(VERTICAL);
        pridejFotky();
        addDivider();
    }

    private void pridejFotky(){
        MyGridView photos = new MyGridView(context);
        photos.setNumColumns(4);
        LifeTasks.setPaddinginDp(2,8,2,8,photos);
        photos.setAdapter(new ImageAdapter(context,mainPhotosURLs));
        photos.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(context, ImageDetail.class);
                Bundle b = new Bundle();
                b.putInt("position", position);
                b.putStringArrayList("array", photosURLs);
                i.putExtras(b);
                context.startActivity(i);
            }
        });
        addView(photos);
    }

    private void addDivider(){
        View divider = new View(context);
        divider.setBackgroundResource(R.drawable.horizontal_divider);
        addView(divider);
    }

}
