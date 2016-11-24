package ottas70.lifetasks;

import android.content.Context;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.io.File;
import java.net.URI;
import java.util.ArrayList;

/**
 * Created by Ottas on 29.10.2015.
 */
public class ImageAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<String> photosURLs;
    private ArrayList<Uri> uris;
    private  boolean uriUsage = false;


    public ImageAdapter(Context context, ArrayList<String> photosURLs) {
        this.context = context;
        this.photosURLs = photosURLs;
    }

    public ImageAdapter(Context context,ArrayList<Uri> uris,boolean uriUsage) {
        this.context = context;
        this.uris = uris;
        this.uriUsage = uriUsage;
    }

    @Override
    public int getCount() {
        if(uriUsage){
            return uris.size();
        }else{
            return photosURLs.size();
        }
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView im;
        if(convertView == null){
            im = new ImageView(context);
            im.setPadding(0,0,7,7);
        }else{
            im = (ImageView)convertView;
        }
        int width = (context.getResources().getDisplayMetrics().widthPixels/4);
        if(!uriUsage) {
            Picasso.with(context)
                    .load(photosURLs.get(position))
                    .noFade().resize(width, width)
                    .centerCrop()
                    .into(im);
        }else{
            Picasso.with(context)
                    .load(uris.get(position))
                    .noFade().resize(width, width)
                    .centerCrop()
                    .into(im);
        }
        return im;
    }

}
