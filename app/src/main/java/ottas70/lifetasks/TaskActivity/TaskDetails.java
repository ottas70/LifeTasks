package ottas70.lifetasks.TaskActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.R;
import ottas70.lifetasks.ServerRequest;
import ottas70.lifetasks.Task;


/**
 * Created by Ottas on 1.1.2016.
 */
public class TaskDetails extends RelativeLayout {

    Context context;
    Task task;
    FragmentManager fm;
    TextView name;
    ImageView image;
    Button add;
    Button remove;
    LinearLayout informaceView;
    boolean inProgress;


    public TaskDetails(final Context context, final Task task, FragmentManager fm) {
        super(context);
        this.context = context;
        this.task = task;
        this.fm = fm;
        setPadding(35, 0, 35, 20);

        if (LifeTasks.instance.getUser().getTasksInProgress().contains(task)) {
            inProgress = true;
        } else {
            inProgress = false;
        }

        add = (Button) (((Activity) context).findViewById(R.id.addButton));
        add.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) add.getParent();
                layout.removeView(add);
                layout.addView(remove);

                LifeTasks.instance.getUser().getTasksInProgress().add(task);
                task.setInProgress(true);

                ServerRequest request = new ServerRequest(context);
                request.storeTaskInProgress(task, false, new GetCallback() {
                    @Override
                    public void done(Object o) {

                    }
                });
            }
        });

        remove = (Button) (((Activity) context).findViewById(R.id.remove));
        remove.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout layout = (LinearLayout) remove.getParent();
                layout.removeView(remove);
                layout.addView(add);

                LifeTasks.instance.getUser().getTasksInProgress().remove(task);
                task.setInProgress(false);

                ServerRequest request = new ServerRequest(context);
                request.deleteTaskInProgress(task, false, new GetCallback() {
                    @Override
                    public void done(Object o) {

                    }
                });
            }
        });

        informaceView = (LinearLayout) (((Activity) context).findViewById(R.id.informaceView));
        informaceView.removeView(add);
        informaceView.removeView(remove);

        addNameAndDifficulty();
        addCheckBox();
    }

    private void addNameAndDifficulty() {
        name = new TextView(context);
        name.setText(task.getNazev());
        name.setTextSize(34);
        name.setTypeface(Typeface.SANS_SERIF);
        name.setTextColor(Color.BLACK);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        name.setLayoutParams(lp);

        LinearLayout stars = new LinearLayout(context);
        stars.setOrientation(LinearLayout.HORIZONTAL);

        for (int i = 1; i <= 5; i++) {
            ImageView star = new ImageView(context);
            if (i <= task.getDifficulty()) {
                star.setImageResource(R.drawable.star);
            } else {
                star.setImageResource(R.drawable.empty_star);
            }
            star.setPadding(5, 10, 0, 0);
            stars.addView(star);
        }


        LinearLayout layout = new LinearLayout(context);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.addView(name);
        layout.addView(stars);
        if (!task.isDone()  && task != LifeTasks.instance.dailyTask) {
            if (inProgress) {
                layout.addView(remove);
            } else {
                layout.addView(add);
            }
        }

        addView(layout);

    }

    private void addCheckBox() {
        image = new ImageView(context);
        if (task.isDone()) {
            image.setImageResource(R.drawable.checkbox2);
        } else {
            image.setImageResource(R.drawable.checkbox_empty);
        }
        image.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (task.isDone()) {
                    /*image.setImageResource(R.drawable.checkbox_empty);
                    task.setDone(false);
                    unmark();*/
                } else {
                    final AlertDialog.Builder builder = new AlertDialog.Builder(add.getContext());
                    builder.setMessage("Are you sure?")
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    image.setImageResource(R.drawable.checkbox2);
                                    task.setDone(true);
                                    mark();
                                }
                            })
                            .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                }
                            });
                    builder.show();

                }
            }
        });
        RelativeLayout.LayoutParams lp = new RelativeLayout.LayoutParams
                (LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);

        addView(image, lp);
    }

    private void mark() {
        ServerRequest request = new ServerRequest(context);
        request.storeFinishedTaskAsyncTask(task, false, new GetCallback() {
            @Override
            public void done(Object o) {
                LifeTasks.instance.getUser().getFinishedTasks().add(task);
                task.setViews(task.getViews() + 1);
                LifeTasks.setPopularity();

                add.setVisibility(GONE);
                remove.setVisibility(GONE);

                TaskDialog dialog = new TaskDialog();

                Bundle b = new Bundle();
                b.putInt("id", task.getId());

                dialog.setArguments(b);
                dialog.show(fm, "dialog");
            }
        });
    }

    private void unmark() {
        LifeTasks.instance.getUser().getFinishedTasks().remove(task);
        ServerRequest request = new ServerRequest(context);
        request.deleteFinishedAsyncTask(task, false, new GetCallback() {
            @Override
            public void done(Object o) {

            }
        });
    }


    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus == true) {
            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity) getContext()).getWindowManager()
                    .getDefaultDisplay()
                    .getMetrics(displayMetrics);
            float x = (float) (displayMetrics.widthPixels / 10.8);
            name.setMaxWidth((int) (getWidth() - image.getWidth() - x));
        }
    }


}
