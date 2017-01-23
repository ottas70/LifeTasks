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

import java.util.ArrayList;

import ottas70.lifetasks.GetCallback;

/**
 * Created by Ottas on 17.4.2016.
 */
public class CheckEmailAsyncTask extends AsyncTask<Void, Void, Boolean> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";
    public static final String IMAGE_URL_ADRESS = "http://ottas70.com/LifeTasks/images/";
    ProgressDialog progressDialog;
    GetCallback callBack;
    String email;

    public CheckEmailAsyncTask(String email,GetCallback callBack, ProgressDialog progressDialog) {
        this.email = email;
        this.progressDialog = progressDialog;
        this.callBack = callBack;
    }

    @Override
    protected Boolean doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("email", email));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "CheckEmail.php");

        Boolean emailExist = new Boolean(false);

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONArray jArray = new JSONArray(result);
            if(jArray.length() > 0){
                emailExist = new Boolean(true);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        return emailExist;
    }

    @Override
    protected void onPostExecute(Boolean emailExist) {
        super.onPostExecute(emailExist);
        progressDialog.dismiss();
        callBack.done(emailExist);
    }
}
