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
import android.view.ViewGroup;
import android.webkit.WebChromeClient;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.MapActivity;
import ottas70.lifetasks.R;
import ottas70.lifetasks.Task;

/**
 * Created by Ottas on 26.10.2015.
 */
public class TaskInformations extends LinearLayout {

    Context context;
    boolean rozbaleno = false;
    Task task;

    public TaskInformations(Context context,Task task) {
        super(context);
        this.context = context;
        this.task = task;
        setOrientation(VERTICAL);
        //setBackgroundColor(getResources().getColor(R.color.layout_grey2));
        pridejPopis();
        addDivider();
        if(task.getAdress() != null) {
            pridejAdresu();
        }
    }

    private void pridejPopis() {
        TextView nadpis = new TextView(context);
        nadpis.setText("Informations");
        nadpis.setTextSize(16);
        nadpis.setPadding(35, 30, 35, 40);
        nadpis.setTypeface(Typeface.SANS_SERIF);
        nadpis.setGravity(Gravity.CENTER);
        addView(nadpis);

        final TextView popis = new TextView(context);
        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");
        popis.setTypeface(tf);
        popis.setText(task.getInformations());
        popis.setTextColor(Color.BLACK);
        popis.setTextSize(12);
        popis.setPadding(35, 0, 35, 20);
        popis.setEllipsize(TextUtils.TruncateAt.END);
        popis.setMaxLines(5);
        popis.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rozbaleno) {
                    popis.setMaxLines(5);
                    rozbaleno = false;
                } else {
                    popis.setMaxLines(Integer.MAX_VALUE);
                    rozbaleno = true;
                }
            }
        });
        addView(popis);
    }

    private void pridejAdresu() {
        RelativeLayout adresa = new RelativeLayout(context);
        //adresa.setBackgroundColor(getResources().getColor(R.color.layout_grey2));
        addView(adresa);

        TextView nadpis = new TextView(context);
        nadpis.setText("Adress");
        nadpis.setTypeface(Typeface.SANS_SERIF);
        nadpis.setTextSize(16);
        nadpis.setPadding(35, 15, 35, 0);
        //noinspection ResourceType
        nadpis.setId(10);

        adresa.addView(nadpis);

        ImageButton map = new ImageButton(context);
        map.setBackgroundResource(R.color.primary_darker);
        map.setImageResource(R.drawable.icon_map);
        LifeTasks.setPaddinginDp(20, 5, 20, 5, map);
        map.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(context, MapActivity.class);
                Bundle b = new Bundle();
                b.putInt("id",task.getId());
                i.putExtras(b);
                context.startActivity(i);
            }
        });

        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
                (RelativeLayout.LayoutParams.WRAP_CONTENT, RelativeLayout.LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        adresa.addView(map, lp);

        Typeface tf = Typeface.createFromAsset(context.getAssets(),
                "fonts/Roboto-Light.ttf");

        LinearLayout adresaInfo = new LinearLayout(context);
        adresaInfo.setOrientation(VERTICAL);
        adresaInfo.setPadding(0,0,0,20);
        RelativeLayout.LayoutParams p = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
                ViewGroup.LayoutParams.WRAP_CONTENT);

        p.addRule(RelativeLayout.BELOW, nadpis.getId());
        adresa.addView(adresaInfo,p);

        if(task.getAdress().getStreet()!= null) {
            TextView ulice = new TextView(context);
            ulice.setText(task.getAdress().getStreet());
            ulice.setTextColor(Color.BLACK);
            ulice.setTextSize(12);
            ulice.setPadding(35, 15, 35, 0);
            ulice.setTypeface(tf);
            adresaInfo.addView(ulice);
        }

        if(task.getAdress().getCity() != null) {
            TextView mesto = new TextView(context);
            mesto.setText(task.getAdress().getCity());
            mesto.setTextColor(Color.BLACK);
            mesto.setTextSize(12);
            mesto.setPadding(35, 0, 35, 0);
            mesto.setTypeface(tf);
            adresaInfo.addView(mesto);
        }

        if(task.getAdress().getState() != null) {
            TextView zeme = new TextView(context);
            zeme.setText(task.getAdress().getState());
            zeme.setTextColor(Color.BLACK);
            zeme.setTextSize(12);
            zeme.setPadding(35, 0, 35, 0);
            zeme.setTypeface(tf);
            adresaInfo.addView(zeme);
        }



    }

    private void addDivider(){
        View divider = new View(context);
        divider.setBackgroundResource(R.drawable.horizontal_divider);
        addView(divider);
    }

}
