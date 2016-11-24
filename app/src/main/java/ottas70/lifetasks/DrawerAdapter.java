package ottas70.lifetasks;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ottas on 9.4.2016.
 */
public class DrawerAdapter extends BaseAdapter {

    Context context;
    String[] items;
    int[] icons;
    Class[] classes;
    private static LayoutInflater inflater = null;

    public DrawerAdapter(Context context,String[] items,int[] icons,Class[] classes) {
        this.context = context;
        this.items = items;
        this.icons = icons;
        this.classes = classes;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return items.length;
    }

    @Override
    public Object getItem(int position) {
        return items[position];
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view;
        if(convertView == null){
            view = inflater.inflate(R.layout.drawer_list_row,null);
        }else{
            view = convertView;
        }

        ImageView icon = (ImageView)view.findViewById(R.id.icon);
        Picasso.with(context).load(icons[position]).into(icon);

        TextView text = (TextView)view.findViewById(R.id.itemText);
        text.setText(items[position]);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, classes[position]);
                if (position == 2) {
                    Bundle b = new Bundle();
                    b.putInt("option", 1);
                    i.putExtras(b);
                }
                context.startActivity(i);
            }
        });

        return view;
    }

}
