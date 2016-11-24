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

import ottas70.lifetasks.GetUserCallback;
import ottas70.lifetasks.User;

public class fetchUserDataByIDAsyncTask extends AsyncTask<Void, Void, User> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/";
    int userID;
    GetUserCallback userCallBack;
    ProgressDialog progressDialog;

    public fetchUserDataByIDAsyncTask(int userID, GetUserCallback userCallBack, ProgressDialog progressDialog) {
        this.userID = userID;
        this.userCallBack = userCallBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected User doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("id",Integer.toString(userID) ));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "FetchUserDataByID.php");

        User returnedUser = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jObject = new JSONObject(result);

            if (jObject.length() != 0){
                String username = jObject.getString("username");
                String email = jObject.getString("email");
                String password = jObject.getString("password");
                returnedUser = new User(username, email,password);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedUser;
    }

    @Override
    protected void onPostExecute(User returnedUser) {
        super.onPostExecute(returnedUser);
        progressDialog.dismiss();
        userCallBack.done(returnedUser);
    }
}