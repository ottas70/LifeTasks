package ottas70.lifetasks.AsyncTasks;

import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Base64;

import java.io.ByteArrayOutputStream;

import ottas70.lifetasks.GetCallback;

/**
 * Created by Ottas on 16.4.2016.
 */
public class ImageEncoder extends AsyncTask<Void,Void,String> {

    Bitmap bitmap;
    GetCallback callBack;
    ProgressDialog progressDialog;
    String imgPath;

    public ImageEncoder(Bitmap bitmap,String imgPath,GetCallback callBack, ProgressDialog progressDialog) {
        this.bitmap = bitmap;
        this.imgPath = imgPath;
        this.callBack = callBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected String doInBackground(Void... params) {
        BitmapFactory.Options options = null;
        options = new BitmapFactory.Options();
        options.inSampleSize = 3;
        bitmap = BitmapFactory.decodeFile(imgPath,
                options);
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        // Must compress the Image to reduce image size to make upload easy
        bitmap.compress(Bitmap.CompressFormat.PNG, 50, stream);
        byte[] byte_arr = stream.toByteArray();
        // Encode Image to String
        return Base64.encodeToString(byte_arr, 0);
    }

    @Override
    protected void onPostExecute(String result){
        super.onPostExecute(result);
        progressDialog.dismiss();
        callBack.done(result);
    }
}
