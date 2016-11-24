package ottas70.lifetasks.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.params.BasicHttpParams;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;

import java.util.ArrayList;

import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.GetUserCallback;
import ottas70.lifetasks.Task;
import ottas70.lifetasks.User;

/**
 * Created by Ottas on 18.4.2016.
 */
public class UploadTaskAsyncTask extends AsyncTask<Void,Void,Integer> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/";
    Task task;
    GetCallback userCallback;
    ProgressDialog progressDialog;

    public UploadTaskAsyncTask(Task task, GetCallback userCallback, ProgressDialog progressDialog){
        this.task = task;
        this.userCallback = userCallback;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("popularity","0"));
        dataToSend.add(new BasicNameValuePair("name",task.getNazev()));
        dataToSend.add(new BasicNameValuePair("difficulty",Integer.toString(task.getDifficulty())));
        dataToSend.add(new BasicNameValuePair("informations",task.getInformations()));
        dataToSend.add(new BasicNameValuePair("upperTask_id",Integer.toString(task.getUpperTask_id())));
        dataToSend.add(new BasicNameValuePair("mainPhoto_name",task.getMainPhotoUrl()));
        dataToSend.add(new BasicNameValuePair("state",task.getAdress().getState()));
        dataToSend.add(new BasicNameValuePair("city",task.getAdress().getCity()));
        dataToSend.add(new BasicNameValuePair("street",task.getAdress().getStreet()));
        dataToSend.add(new BasicNameValuePair("points",Integer.toString(task.getPoints())));
        dataToSend.add(new BasicNameValuePair("owner",task.getOwnersName()));
        dataToSend.add(new BasicNameValuePair("latitude",Double.toString(task.getLatitude())));
        dataToSend.add(new BasicNameValuePair("longtitude",Double.toString(task.getLongtitude())));


        HttpParams httpRequestParams = getHttpRequestParams();
        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS+"UploadTask.php");

        Integer id = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jObject = new JSONObject(result);

            if (jObject.length() != 0){
                id= new Integer(jObject.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    private HttpParams getHttpRequestParams(){
        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams, CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams, CONNECTION_TIMEOUT);
        return httpRequestParams;
    }
    @Override
    protected void onPostExecute(Integer result){
        super.onPostExecute(result);
        progressDialog.dismiss();
        userCallback.done(result);
    }
}
