package ottas70.lifetasks.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;

import java.util.ArrayList;

import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.LifeTasks;
import ottas70.lifetasks.User;

/**
 * Created by Ottas on 17.4.2016.
 */
public class UploadImageNameAsyncTask extends AsyncTask<Void,Void,Void> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";
    GetCallback callBack;
    ProgressDialog progressDialog;
    User user;
    String filename;
    int option;              // 1 - profilePhoto    2 - account photo

    public UploadImageNameAsyncTask(User user,String filename,int option, GetCallback callBack, ProgressDialog progressDialog) {
        this.user = user;
        this.filename = filename;
        this.option = option;
        this.callBack = callBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("email", LifeTasks.instance.getUser().getEmail()));
        dataToSend.add(new BasicNameValuePair("option",Integer.toString(option)));
        dataToSend.add(new BasicNameValuePair("filename",filename));

        HttpParams httpRequestParams = getHttpRequestParams();
        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS+"UploadImageName.php");
        try{
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            client.execute(post);
        }catch(Exception e){
            e.printStackTrace();
        }
        return null;
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
        return httpRequestParams;
    }

    @Override
    protected void onPostExecute(Void result){
        super.onPostExecute(result);
        progressDialog.dismiss();
        callBack.done(null);
    }
}
