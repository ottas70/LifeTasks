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
import ottas70.lifetasks.GetUserCallback;
import ottas70.lifetasks.Review;
import ottas70.lifetasks.ServerRequest;
import ottas70.lifetasks.Task;
import ottas70.lifetasks.User;

/**
 * Created by Ottas on 29.12.2015.
 */
public class GetReviewsAsyncTask extends AsyncTask<Void,Void,ArrayList<Review>>{

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/";
    ProgressDialog progressDialog;
    GetCallback callBack;
    Task task;

    public GetReviewsAsyncTask(Task task, GetCallback callBack, ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
        this.callBack = callBack;
        this.task = task;
    }

    @Override
    protected ArrayList<Review> doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("id",Integer.toString(task.getId())));

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "GetReviews.php");

        ArrayList<Review> returnedReviews = null;

        try {
            post.setEntity(new UrlEncodedFormEntity(dataToSend));
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONArray jArray = new JSONArray(result);

            returnedReviews = new ArrayList<>();
            if (jArray.length() != 0){
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    String title = object.getString("title");
                    int stars = object.getInt("stars");
                    String reviewText = object.getString("reviewText");
                    String username = object.getString("username");
                    String email = object.getString("email");
                    String password = object.getString("password");
                    Review r = new Review(title,stars,reviewText,new User(username,email,password));
                    returnedReviews.add(r);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedReviews;
    }

    @Override
    protected void onPostExecute(ArrayList<Review> returnedReviews) {
        super.onPostExecute(returnedReviews);
        progressDialog.dismiss();
        callBack.done(returnedReviews);
    }

}
