package ottas70.lifetasks;

import android.annotation.TargetApi;
import android.content.ContentUris;
import android.content.Context;
import android.database.Cursor;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.view.View;
import android.widget.LinearLayout;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by Ottas on 28.12.2015.
 */
public class LifeTasks {

    public static LifeTasks instance;
    public TaskArray tasks;
    public TaskArray popularTasks;
    public TaskArray nearTasks;
    public Task dailyTask;
    private User user;
    public static int[] levels = {500, 700, 900, 1200, 1500, 2100, 2800, 3500, 4200, 5000};

    public LifeTasks() {
        LifeTasks.instance = this;
        tasks = new TaskArray();
        popularTasks = new TaskArray();
        nearTasks = new TaskArray();
        //nearTasks = tasks;
    }


    public static void setPaddinginDp(int left, int top, int right, int bottom, View view) {
        float scale = view.getContext().getResources().getDisplayMetrics().density;
        int leftpx = (int) (left * scale + 0.5f);
        int toppx = (int) (top * scale + 0.5f);
        int rightpx = (int) (right * scale + 0.5f);
        int bottompx = (int) (bottom * scale + 0.5f);
        view.setPadding(leftpx, toppx, rightpx, bottompx);
    }

    public static void setMargininDpforLinearLayout(int left, int top, int right, int bottom, View view) {
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        float scale = view.getContext().getResources().getDisplayMetrics().density;
        int leftpx = (int) (left * scale + 0.5f);
        int toppx = (int) (top * scale + 0.5f);
        int rightpx = (int) (right * scale + 0.5f);
        int bottompx = (int) (bottom * scale + 0.5f);
        params.setMargins(leftpx, toppx, rightpx, bottompx);
        view.setLayoutParams(params);
    }

    public static void setDistances(Context context) {
        for (Task t : LifeTasks.instance.tasks.getTasks()){
            Location location = new Location("Location");
            location.setLatitude(t.getLatitude());
            location.setLongitude(t.getLongtitude());

            float distance = location.distanceTo(LifeTasks.instance.getUser().getLastLocation());
            t.setDistance(distance);
        }
    }

    public static void sortByLocation(Context context) {
        setDistances(context);
        instance.nearTasks.getTasks().clear();
        ArrayList<Task> clone = instance.tasks.getTasks();
        Collections.sort(clone, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return (int) ((int) lhs.getDistance() - rhs.getDistance());
            }
        });
        instance.nearTasks.getTasks().addAll(clone);
    }

    public static void sortByPopularity() {
        setPopularity();
        ArrayList<Task> clone = new ArrayList<>(instance.tasks.getTasks());
        Collections.sort(clone, new Comparator<Task>() {
            @Override
            public int compare(Task lhs, Task rhs) {
                return (int) (rhs.getPopularity() * 100 - lhs.getPopularity() * 100);
            }
        });
        instance.popularTasks.getTasks().clear();
        instance.popularTasks.getTasks().addAll(clone);

    }

    public static void setPopularity() {
        int max = Integer.MIN_VALUE;
        for (int i = 0; i < instance.tasks.getTasks().size(); i++) {
            if (instance.tasks.getTasks().get(i).getViews() > max) {
                max = instance.tasks.getTasks().get(i).getViews();
            }
        }
        for (Task t : instance.tasks.getTasks()) {
            t.setPopularity((t.getViews() * 1.0) / (max * 1.0) * 5.0);
        }
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public static double round(double value, int places) {
        if (places < 0) throw new IllegalArgumentException();

        long factor = (long) Math.pow(10, places);
        value = value * factor;
        long tmp = Math.round(value);
        return (double) tmp / factor;
    }

    /**
     * Get a file path from a Uri. This will get the the path for Storage Access
     * Framework Documents, as well as the _data field for the MediaStore and
     * other file-based ContentProviders.
     *
     * @param context The context.
     * @param uri     The Uri to query.
     * @author paulburke
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    public static String getPath(final Context context, final Uri uri) {

        final boolean isKitKat = Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT;

        // DocumentProvider
        if (isKitKat && DocumentsContract.isDocumentUri(context, uri)) {
            // ExternalStorageProvider
            if (isExternalStorageDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                if ("primary".equalsIgnoreCase(type)) {
                    return Environment.getExternalStorageDirectory() + "/" + split[1];
                }

                // TODO handle non-primary volumes
            }
            // DownloadsProvider
            else if (isDownloadsDocument(uri)) {

                final String id = DocumentsContract.getDocumentId(uri);
                final Uri contentUri = ContentUris.withAppendedId(
                        Uri.parse("content://downloads/public_downloads"), Long.valueOf(id));

                return getDataColumn(context, contentUri, null, null);
            }
            // MediaProvider
            else if (isMediaDocument(uri)) {
                final String docId = DocumentsContract.getDocumentId(uri);
                final String[] split = docId.split(":");
                final String type = split[0];

                Uri contentUri = null;
                if ("image".equals(type)) {
                    contentUri = MediaStore.Images.Media.EXTERNAL_CONTENT_URI;
                } else if ("video".equals(type)) {
                    contentUri = MediaStore.Video.Media.EXTERNAL_CONTENT_URI;
                } else if ("audio".equals(type)) {
                    contentUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
                }

                final String selection = "_id=?";
                final String[] selectionArgs = new String[]{
                        split[1]
                };

                return getDataColumn(context, contentUri, selection, selectionArgs);
            }
        }
        // MediaStore (and general)
        else if ("content".equalsIgnoreCase(uri.getScheme())) {
            return getDataColumn(context, uri, null, null);
        }
        // File
        else if ("file".equalsIgnoreCase(uri.getScheme())) {
            return uri.getPath();
        }

        return null;
    }

    /**
     * Get the value of the data column for this Uri. This is useful for
     * MediaStore Uris, and other file-based ContentProviders.
     *
     * @param context       The context.
     * @param uri           The Uri to query.
     * @param selection     (Optional) Filter used in the query.
     * @param selectionArgs (Optional) Selection arguments used in the query.
     * @return The value of the _data column, which is typically a file path.
     */
    public static String getDataColumn(Context context, Uri uri, String selection,
                                       String[] selectionArgs) {

        Cursor cursor = null;
        final String column = "_data";
        final String[] projection = {
                column
        };

        try {
            cursor = context.getContentResolver().query(uri, projection, selection, selectionArgs,
                    null);
            if (cursor != null && cursor.moveToFirst()) {
                final int column_index = cursor.getColumnIndexOrThrow(column);
                return cursor.getString(column_index);
            }
        } finally {
            if (cursor != null)
                cursor.close();
        }
        return null;
    }


    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is ExternalStorageProvider.
     */
    public static boolean isExternalStorageDocument(Uri uri) {
        return "com.android.externalstorage.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is DownloadsProvider.
     */
    public static boolean isDownloadsDocument(Uri uri) {
        return "com.android.providers.downloads.documents".equals(uri.getAuthority());
    }

    /**
     * @param uri The Uri to check.
     * @return Whether the Uri authority is MediaProvider.
     */
    public static boolean isMediaDocument(Uri uri) {
        return "com.android.providers.media.documents".equals(uri.getAuthority());
    }

}
