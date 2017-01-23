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
import org.json.JSONObject;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import ottas70.lifetasks.GetCallback;

/**
 * Created by Ottas on 23.4.2016.
 */
public class GetDailyTaskIDAsyncTask extends AsyncTask<Void,Void,Integer> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";
    ProgressDialog progressDialog;
    GetCallback callBack;
    String date;

    public GetDailyTaskIDAsyncTask(Date date, GetCallback callBack, ProgressDialog progressDialog) {
        DateFormat df = new SimpleDateFormat("yyyy/MM/dd");
        this.date = df.format(date);
        this.callBack = callBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Integer doInBackground(Void... params) {
        //Log.v("Tag", "running in background");
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("date",date));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "GetDailyTaskID.php");

        Integer id = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jsonObject = new JSONObject(result);

            if (jsonObject.length() != 0){
                id = new Integer(jsonObject.getInt("id"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return id;
    }

    @Override
    protected void onPostExecute(Integer result) {
        super.onPostExecute(result);
        progressDialog.dismiss();
        callBack.done(result);
    }

}
