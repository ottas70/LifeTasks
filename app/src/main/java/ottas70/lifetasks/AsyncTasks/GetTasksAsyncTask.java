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

import ottas70.lifetasks.Adress;
import ottas70.lifetasks.GetCallback;
import ottas70.lifetasks.Task;

/**
 * Created by Ottas on 28.12.2015.
 */
public class GetTasksAsyncTask extends AsyncTask<Void, Void, ArrayList<Task>> {

    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/";
    public static final String IMAGE_URL_ADRESS = "http://ottas70.com/images/";
    ProgressDialog progressDialog;
    GetCallback callBack;

    public GetTasksAsyncTask(GetCallback callBack, ProgressDialog progressDialog) {
        this.progressDialog = progressDialog;
        this.callBack = callBack;
    }

    @Override
    protected ArrayList<Task> doInBackground(Void... params) {

        HttpParams httpRequestParams = new BasicHttpParams();
        HttpConnectionParams.setConnectionTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);
        HttpConnectionParams.setSoTimeout(httpRequestParams,
                CONNECTION_TIMEOUT);

        HttpClient client = new DefaultHttpClient(httpRequestParams);
        HttpPost post = new HttpPost(SERVER_ADRESS
                + "GetTasks.php");

        ArrayList<Task> returnedTasks = null;

        try {
            HttpResponse httpResponse = client.execute(post);

            HttpEntity entity = httpResponse.getEntity();
            String result = EntityUtils.toString(entity);
            JSONArray jArray = new JSONArray(result);

            returnedTasks = new ArrayList<>();
            if (jArray.length() != 0){
                for (int i = 0; i < jArray.length(); i++) {
                    JSONObject object = jArray.getJSONObject(i);
                    int task_id = object.getInt("task_id");
                    String name = object.getString("name");
                    String information = object.getString("informations");
                    int difficulty = object.getInt("difficulty");
                    int upperTask_id = object.getInt("upperTask_id");
                    String url = IMAGE_URL_ADRESS + object.getString("mainPhoto_name");
                    Adress adress = returnAdress(object.getString("state"),object.getString("city"),object.getString("street"));
                    int points = object.getInt("task_points");
                    int views = object.getInt("popularity");
                    String username = object.getString("username");
                    double latitude = object.getDouble("latitude");
                    double longtitude = object.getDouble("longtitude");
                    Task t = new Task(task_id,name,information,difficulty,url,adress,upperTask_id,points,views,username,latitude,longtitude);
                    returnedTasks.add(t);
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return returnedTasks;
    }

    @Override
    protected void onPostExecute(ArrayList<Task> returnedTasks) {
        super.onPostExecute(returnedTasks);
        progressDialog.dismiss();
        callBack.done(returnedTasks);
    }

    private Adress returnAdress(String state,String city,String street){
        if(street != null){
            return new Adress(street,city,state);
        }else{
            if(city != null){
                return new Adress(city, state);
            }else{
                if(state != null){
                    return new Adress(state);
                }else{
                    return null;
                }
            }
        }
    }

}
