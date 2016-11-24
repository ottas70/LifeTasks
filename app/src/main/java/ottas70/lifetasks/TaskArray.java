package ottas70.lifetasks;

import android.app.AlertDialog;
import android.content.Context;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Ottas on 28.12.2015.
 */
public class TaskArray {

    private ArrayList<Task> tasks;

    public TaskArray() {
        tasks = new ArrayList<>();
    }

    public void showErrorMessage(Context context){
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        dialogBuilder.setMessage("Cannot load data");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    public Task getTaskByID(int id){
        for (Task t : tasks){
            if(t.getId() ==  id){
                return t;
            }
        }
        return null;
    }

    public ArrayList<Task> getTasks() {
        return tasks;
    }

    public void removeTasks(ArrayList<Integer> ids){
        ArrayList<Task> array = new ArrayList<>();
        for(Task t : tasks){
            if(ids.contains(t.getId()) && t != LifeTasks.instance.dailyTask){
                array.add(t);
            }
        }
        tasks.removeAll(array);
    }
}
