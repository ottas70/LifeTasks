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
import ottas70.lifetasks.Review;
import ottas70.lifetasks.Task;

/**
 * Created by Ottas on 31.1.2016.
 */
public class UploadReviewAsyncTask extends AsyncTask<Void,Void,Void> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";
    Task task;
    Review review;
    GetCallback callBack;
    ProgressDialog progressDialog;

    public UploadReviewAsyncTask(Task task, Review review, GetCallback callBack, ProgressDialog progressDialog) {
        this.task = task;
        this.review = review;
        this.callBack = callBack;
        this.progressDialog = progressDialog;
    }

    @Override
    protected Void doInBackground(Void... params) {
        ArrayList<NameValuePair> dataToSend = new ArrayList<>();
        dataToSend.add(new BasicNameValuePair("title", review.getNadpis()));
        dataToSend.add(new BasicNameValuePair("stars", Integer.toString(review.getStars())));
        dataToSend.add(new BasicNameValuePair("text", review.getText()));
        dataToSend.add(new BasicNameValuePair("email",review.getUser().getEmail()));
        dataToSend.add(new BasicNameValuePair("task_id",Integer.toString(task.getId())));

        HttpParams httpRequestParams = getHttpRequestParams();
        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS+"UploadReview.php");
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
