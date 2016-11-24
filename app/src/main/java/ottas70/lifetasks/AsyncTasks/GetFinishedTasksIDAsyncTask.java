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
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.Task;
import ottas70.lifetasks.User;

/**
 * Created by Ottas on 1.1.2016.
 */
public class GetFinishedTasksIDAsyncTask extends AsyncTask<Void,Void,ArrayList<Integer>> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/";
    ProgressDialog progressDialog;
    GetCallback callBack;
    User user;

    public GetFinishedTasksIDAsyncTask(User user, GetCallback callBack, ProgressDialog progressDialog) {
        this.user = user;
        this.callBack = callBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected ArrayList<Integer> doInBackground(Void... params) {
        //Log.v("Tag", "running in background");
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("email",user.getEmail() ));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "GetFinishedTasksID.php");

         ArrayList<Integer> returnedIds = new ArrayList<>();

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONArray jArray = new JSONArray(result);

            if (jArray.length() != 0){
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    int id = object.getInt("task_id");
                    returnedIds.add(id);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedIds;
    }

    @Override
    protected void onPostExecute(ArrayList<Integer> returnedIds) {
        super.onPostExecute(returnedIds);
        progressDialog.dismiss();
        callBack.done(returnedIds);
    }

}
