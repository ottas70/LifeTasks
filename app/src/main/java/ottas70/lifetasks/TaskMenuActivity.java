package ottas70.lifetasks;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.SearchView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import ottas70.lifetasks.TaskActivity.TaskActivity;

/**
 * Created by Ottas on 28.12.2015.
 */
public class TaskMenuActivity extends DrawerActivity{

    ListView list;
    SearchView search;
    Button orderBy;
    ImageView menu;

    ArrayList<Task> results;
    int option;
    int SEARCHING_ACCURACY = 70;   //0-100%

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_taskmenu);

        list = (ListView)findViewById(R.id.list);
        orderBy = (Button)findViewById(R.id.orderBy);
        search = (SearchView)findViewById(R.id.searchView);
        menu = (ImageView)findViewById(R.id.menuButton);

        option = getIntent().getExtras().getInt("option");

        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() < 3) {
                    return false;
                }
                search(newText);
                return true;
            }
        });

        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(Gravity.LEFT);
            }
        });

        loadMenu();
        drawTasks();
    }

    @Override
    public void onResume() {
        super.onResume();
        drawTasks();
    }

    private void drawTasks(){
        results = new ArrayList<>();
        switch(option){
            case 1:
                results.addAll(LifeTasks.instance.tasks.getTasks());
                break;
            case 2:
                results.addAll(LifeTasks.instance.popularTasks.getTasks());
                break;
            case 3:
                results.addAll(LifeTasks.instance.nearTasks.getTasks());
                break;
        }
        list.setAdapter(new TaskAdapter(this, results));
    }

    private void loadMenu(){
        orderBy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(TaskMenuActivity.this, orderBy);
                popup.getMenuInflater().inflate(R.menu.popup_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        orderBy.setText(item.getTitle());
                        order(item);
                        return true;
                    }
                });
                popup.show();
            }
        });
    }

    private void search(String text){
        results.clear();
       for (Task t : LifeTasks.instance.tasks.getTasks()){
           if(t.getNazev().toLowerCase().contains(text.toLowerCase())){
               results.add(t);
               continue;
           }
           if(isAccurate(text, t.getAdress().getState(), SEARCHING_ACCURACY)){
               results.add(t);
               continue;
           }

           if(isAccurate(text,t.getAdress().getCity(),SEARCHING_ACCURACY)){
               results.add(t);
               continue;
           }

           if(isAccurate(text,t.getAdress().getStreet(),SEARCHING_ACCURACY)){
               results.add(t);
               continue;
           }

           if(isAccurate(text,t.getNazev(),SEARCHING_ACCURACY)){
               results.add(t);
               continue;
           }

       }
        list.setAdapter(new TaskAdapter(this, results));
    }

    private boolean isAccurate(String searchWord,String word,int accuracy){      //accuracy 0-100%
        searchWord = searchWord.toLowerCase();
        word = word.toLowerCase();
        char[] letters = searchWord.toCharArray();
        char[] letters2 = word.toCharArray();
        int correct = 0;
        int i = 0;
        for(char c : letters2){
            if(letters[i] == c){
                correct++;
                if(i < searchWord.length()-1){
                    i++;
                }else{
                    break;
                }
            }
        }
        double result = (double)correct/((double)word.length()/100.0);
        if((int)result < accuracy){
            return false;
        }
        return true;
    }

    private void order(MenuItem item){
        switch(item.getItemId()){
            case R.id.Popular:
                orderByPopularity();
                break;
            case R.id.A_Z:
                orderAlphabetically(true);  // a-z true  z-a false
                break;
            case R.id.Z_A:
                orderAlphabetically(false);
                break;
            case R.id.done:
                orderByCompletion(true);   //done - true   undone-false
                break;
            case R.id.undone:
                orderByCompletion(false);
        }
    }

    private  void orderByPopularity(){
        Collections.sort(results, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return (int) (rhs.getPopularity() * 100 - lhs.getPopularity() * 100);
            }
        });
        list.setAdapter(new TaskAdapter(this, results));
    }

    private void orderAlphabetically(final boolean a_z){
        Collections.sort(results, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return lhs.getNazev().compareToIgnoreCase(rhs.getNazev());
            }
        });
        if(!a_z){
            Collections.reverse(results);
        }
        list.setAdapter(new TaskAdapter(this,results));
    }

    private void orderByCompletion(boolean done){
        Collections.sort(results, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                    if(lhs.isDone() && rhs.isDone()){
                        return 0;
                    }else{
                        if(lhs.isDone()){
                            return 1;
                        }else{
                            return -1;
                        }
                    }
            }
        });
        if(done){
            Collections.reverse(results);
        }
        list.setAdapter(new TaskAdapter(this, results));
    }

}
