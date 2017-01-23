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

public class FetchUserDataAsyncTask extends AsyncTask<Void, Void, User> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";
    User user;
    GetUserCallback userCallBack;
    ProgressDialog progressDialog;

    public FetchUserDataAsyncTask(User user, GetUserCallback userCallBack, ProgressDialog progressDialog) {
        this.user = user;
        this.userCallBack = userCallBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected User doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("email", user.getEmail()));
        dataToSend.add(new BasicNameValuePair("password", user.getPassword()));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "FetchUserData.php");

        User returnedUser = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONObject jObject = new JSONObject(result);

            if (jObject.length() != 0){
                Log.v("happened", "2");
                int id = jObject.getInt("id");
                String username = jObject.getString("username");
                int points = jObject.getInt("points");
                int level = jObject.getInt("level");
                String realname = jObject.getString("realName");
                String adress = jObject.getString("adress");
                String work = jObject.getString("work");
                String profile = jObject.getString("profilePhoto_name");
                if(profile.equals("")){
                    profile = "basic_profile.jpg";
                }
                String account = jObject.getString("accountPhoto_name");
                if(account.equals("")){
                    account = "basic_background.jpg";
                }
                returnedUser = new User(id,username, user.getEmail(),user.getPassword(),points, level,realname,adress,work,profile,account);
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