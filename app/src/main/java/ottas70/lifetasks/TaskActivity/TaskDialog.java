package ottas70.lifetasks.TaskActivity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.FacebookSdk;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.widget.ShareDialog;
import com.google.android.gms.plus.PlusShare;
import com.squareup.picasso.Picasso;

import ottas70.lifetasks.CircleTransform;
import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.ImageAdapter;
import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.ProfileActivity;
import ottas70.lifetasks.R;
import ottas70.lifetasks.ServerRequest;
import ottas70.lifetasks.Task;
import ottas70.lifetasks.User;


/**
 * Created by Ottas on 3.1.2016.
 */
public class TaskDialog extends DialogFragment {

    Task task;

    ImageView photo;
    TextView taskName;
    LinearLayout stars;
    TextView points;
    ProgressBar progressBar;
    ImageButton profile;
    ImageButton ok;
    ImageButton share;
    TextView level;
    TextView progress;
    TextView profileText;
    TextView okText;
    TextView shareText;

    boolean sharing = false;


    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        task = LifeTasks.instance.tasks.getTaskByID(getArguments().getInt("id"));

        View view = inflater.inflate(R.layout.dialog_taskinfo, null);
        photo = (ImageView) view.findViewById(R.id.photo);
        taskName = (TextView) view.findViewById(R.id.taskName);
        stars = (LinearLayout) view.findViewById(R.id.stars);
        points = (TextView) view.findViewById(R.id.points);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        profile = (ImageButton) view.findViewById(R.id.profileButton);
        ok = (ImageButton) view.findViewById(R.id.okButton);
        share = (ImageButton) view.findViewById(R.id.shareButton);
        level = (TextView) view.findViewById(R.id.level);
        progress = (TextView) view.findViewById((R.id.progress));
        profileText = (TextView) view.findViewById((R.id.profileText));
        okText = (TextView) view.findViewById((R.id.okText));
        shareText = (TextView) view.findViewById((R.id.shareText));

        drawStars(task.getDifficulty());
        Picasso.with(photo.getContext()).load(task.getMainPhotoUrl()).transform(new CircleTransform()).into(photo);
        progressBar.getProgressDrawable().setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN);

        taskName.setText(task.getNazev());
        setExp(task.getPoints());
        addPointsToUser(task.getPoints());


        progressBar.setMax(LifeTasks.instance.levels[LifeTasks.instance.getUser().getLevel()]);
        progressBar.setProgress(LifeTasks.instance.getUser().getPoints());

        setLevel(LifeTasks.instance.getUser().getLevel());
        setProgress(LifeTasks.instance.getUser().getPoints(), LifeTasks.instance.levels[LifeTasks.instance.getUser().getLevel()]);

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!sharing) {
                    startActivity(new Intent(getActivity(), ProfileActivity.class));
                    LifeTasks.instance.getUser().getTasksInProgress().remove(task);
                }else{
                    FacebookSdk.sdkInitialize(getActivity());
                ShareDialog shareDialog = new ShareDialog(getActivity());
                ShareLinkContent linkContent = new ShareLinkContent.Builder()
                        .setContentTitle("Life tasks")
                        .setContentDescription(
                                "Start to live the fullest")
                        .setContentUrl(Uri.parse("https://gyarab.cz"))
                        .build();
                shareDialog.show(getActivity(),linkContent);
                }
            }
        });

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!sharing){
                    dismiss();
                }else{
                    try {
                        Intent shareIntent = new PlusShare.Builder(getActivity())
                                .setType("text/plain")
                                .setText("Welcome to the Google+ platform.")
                                .setContentUrl(Uri.parse("https://gyarab.cz"))
                                .getIntent();

                        startActivityForResult(shareIntent, 0);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        share.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (sharing) {
                    sharing = false;

                    profile.setImageDrawable(getResources().getDrawable(R.drawable.ic_account_circle_black_48dp));
                    ok.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_48dp));   //google +
                    share.setImageDrawable(getResources().getDrawable(R.drawable.ic_share_black_48dp));

                    profileText.setText("Profile");
                    okText.setText("Ok");
                    shareText.setText("Share");

                } else {
                    sharing = true;

                    profile.setImageDrawable(getResources().getDrawable(R.drawable.facebook_icon));
                    ok.setImageDrawable(getResources().getDrawable(R.drawable.google_plus_icon));
                    share.setImageDrawable(getResources().getDrawable(R.drawable.ic_done_black_48dp));

                    profileText.setText("");
                    okText.setText("");
                    shareText.setText("Ok");
                }
            }
        });

        ServerRequest request = new ServerRequest(photo.getContext());
        request.addPointsToUserAsyncTask(false, new GetCallback() {
            @Override
            public void done(Object o) {
                //empty
            }
        });

        builder.setView(view);
        //startProgressBar(getArguments().getInt("points"));
        return builder.create();
    }

    private void drawStars(int starsCount) {
        stars.removeAllViews();
        for (int i = 0; i < 5; i++) {
            ImageView star = new ImageView(photo.getContext());
            if (i < starsCount) {
                star.setImageResource(R.drawable.review_fill_star);
            } else {
                star.setImageResource(R.drawable.review_empty_star);
            }
            stars.addView(star);
        }
    }

    private void setProgress(int actual, int max) {
        String s = String.valueOf(actual) + "/" + String.valueOf(max);
        progress.setText(s);
    }

    private void setLevel(int level) {
        String s = "level " + String.valueOf(level);
        this.level.setText(s);
    }

    private void setExp(int exp) {
        String s = "+" + String.valueOf(exp) + "exp";
        points.setText(s);
    }

    private void addPointsToUser(int points) {
        User user = LifeTasks.instance.getUser();
        user.addPoints(points);
        boolean pokracovat = false;
        while (!pokracovat) {
            if (user.getPoints() > LifeTasks.instance.levels[user.getLevel()]) {
                if (user.getLevel() != LifeTasks.instance.levels.length - 1) {
                    user.setPoints(user.getPoints() - LifeTasks.instance.levels[user.getLevel()]);
                    user.setLevel(user.getLevel() + 1);

                } else {
                    user.setPoints(LifeTasks.instance.levels[LifeTasks.instance.levels.length - 1]);
                    pokracovat = true;
                }
            } else {
                pokracovat = true;
            }
        }
    }


    /*private void startProgressBar(final int startPoints){
        new Handler().post(new Runnable() {
            @Override
            public void run() {
                int points = startPoints;
                int actualLevel = LifeTasks.instance.getUser().getLevel();
                int actualPoints = LifeTasks.instance.getUser().getPoints();
                while(points != 0){
                    progressBar.setMax(LifeTasks.instance.levels[actualLevel]);
                    progressBar.setProgress(actualPoints);

                    int target = 0;
                    if(points+actualPoints > LifeTasks.instance.levels[actualLevel]){
                        target = LifeTasks.instance.levels[actualLevel];
                        points = points - (LifeTasks.instance.levels[actualLevel]-actualPoints);
                        actualPoints = 0;
                        actualLevel++;
                    }else{
                        target = points+actualPoints;
                    }

                    ObjectAnimator animation = ObjectAnimator.ofInt(progressBar,"progress",target);
                    animation.setDuration(2000);
                    animation.start();
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    setLevel(actualLevel);

                }
            }
        });
    }*/


}
