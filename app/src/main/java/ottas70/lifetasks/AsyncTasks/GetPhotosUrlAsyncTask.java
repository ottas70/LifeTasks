package ottas70.lifetasks.AsyncTasks;

import android.app.ProgressDialog;
import android.os.AsyncTask;

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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.Task;

/**
 * Created by Ottas on 30.12.2015.
 */
public class GetPhotosUrlAsyncTask extends AsyncTask<Void,Void,ArrayList<String>> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";
    public static final String IMAGE_URL_ADRESS = "http://ottas70.com/LifeTasks/images/";
    ProgressDialog progressDialog;
    GetCallback callBack;
    Task task;

    public GetPhotosUrlAsyncTask(Task task, GetCallback callBack, ProgressDialog progressDialog) {
        this.callBack = callBack;
        this.task = task;
        this.progressDialog = progressDialog;
    }

    @Override
    protected ArrayList<String> doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("id",Integer.toString(task.getId())));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "GetPhotosUrl.php");

        ArrayList<String> returnedUrls = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONArray jArray = new JSONArray(result);

            returnedUrls = new ArrayList<>();
            if (jArray.length() != 0){
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    String url = IMAGE_URL_ADRESS + object.getString("url");
                    returnedUrls.add(url);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedUrls;
    }

    @Override
    protected void onPostExecute(ArrayList<String> returnedUrls) {
        super.onPostExecute(returnedUrls);
        progressDialog.dismiss();
        callBack.done(returnedUrls);
    }

}
