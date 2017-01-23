package ottas70.lifetasks;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;

import java.util.Date;

import ottas70.lifetasks.AsyncTasks.CheckEmailAsyncTask;
import ottas70.lifetasks.AsyncTasks.CheckUsernameAsyncTask;
import ottas70.lifetasks.AsyncTasks.DeleteFinishedTaskAsyncTask;
import ottas70.lifetasks.AsyncTasks.DeleteTaskInProgressAsyncTask;
import ottas70.lifetasks.AsyncTasks.FetchUserDataAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetDailyTaskIDAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetDailyTasksIDAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetFinishedTasksIDAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetPhotosUrlAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetReviewsAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetTasksAsyncTask;
import ottas70.lifetasks.AsyncTasks.GetTasksInProgressIDAsyncTask;
import ottas70.lifetasks.AsyncTasks.ImageEncoder;
import ottas70.lifetasks.AsyncTasks.StoreFinishedTaskAsyncTask;
import ottas70.lifetasks.AsyncTasks.StoreTaskInProgressAsyncTask;
import ottas70.lifetasks.AsyncTasks.StoreUserDataAsyncTask;
import ottas70.lifetasks.AsyncTasks.UpdateUserDataAsyncTask;
import ottas70.lifetasks.AsyncTasks.UploadImageNameAsyncTask;
import ottas70.lifetasks.AsyncTasks.UploadPhotoToTaskAsyncTask;
import ottas70.lifetasks.AsyncTasks.UploadReviewAsyncTask;
import ottas70.lifetasks.AsyncTasks.UploadTaskAsyncTask;
import ottas70.lifetasks.AsyncTasks.addPointsToUserAsyncTask;

public class ServerRequest {

    private ProgressDialog progressDialog;
    public static final int CONNECTION_TIMEOUT = 1000*15;
    public static final String SERVER_ADRESS = "http://ottas70.com/LifeTasks";

    public ServerRequest(Context context){
        progressDialog = new ProgressDialog(context);
        progressDialog.setCancelable(false);
        progressDialog.setTitle("Processing...");
        progressDialog.setMessage("Please wait...");
    }

    public void  storeUserDataInBackground(User user,GetUserCallback userCallback){
        progressDialog.show();
        new StoreUserDataAsyncTask(user,userCallback,progressDialog).execute();
    }

    public void fetchUserDataAsyncTask(User user, GetUserCallback userCallBack) {
        //progressDialog.show();
        new FetchUserDataAsyncTask(user, userCallBack,progressDialog).execute();
    }


    public void getTasksAsyncTask(GetCallback callBack){
        //progressDialog.show();
        new GetTasksAsyncTask(callBack,progressDialog).execute();

    }

    public void getReviewsAsyncTask(Task task,boolean showDialog,GetCallback callBack){
        if(showDialog){
            progressDialog.show();
        }
        new GetReviewsAsyncTask(task,callBack,progressDialog).execute();
    }

    public void getPhotosUrlAsyncTask(Task task,boolean showDialog,GetCallback callBack){
        if(showDialog){
            progressDialog.show();
        }
        new GetPhotosUrlAsyncTask(task,callBack,progressDialog).execute();
    }

    public void getFinishedTasksIDAsyncTask(User user,boolean showDialog,GetCallback callBack){
        if(showDialog){
            progressDialog.show();
        }
        new GetFinishedTasksIDAsyncTask(user,callBack,progressDialog).execute();
    }

    public void storeFinishedTaskAsyncTask(Task task,boolean showDialog,GetCallback callBack){
        if(showDialog){
            progressDialog.show();
        }
        new StoreFinishedTaskAsyncTask(task,callBack,progressDialog).execute();
    }

    public void deleteFinishedAsyncTask(Task task,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new DeleteFinishedTaskAsyncTask(task,callback,progressDialog).execute();
    }

    public void uploadReviewAsyncTask(Task task,Review review,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new UploadReviewAsyncTask(task,review,callback,progressDialog).execute();
    }

    public void addPointsToUserAsyncTask(boolean showDialog,GetCallback callback) {
        if (showDialog) {
            progressDialog.show();
        }
        new addPointsToUserAsyncTask(callback,progressDialog).execute();
    }

    public void getTasksInProgressAsyncTask(User user, boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new GetTasksInProgressIDAsyncTask(user,callback,progressDialog).execute();
    }

    public void updateUserDataAsyncTask(User user,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new UpdateUserDataAsyncTask(user,callback,progressDialog).execute();
    }

    public void deleteTaskInProgress(Task task,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new DeleteTaskInProgressAsyncTask(task,callback,progressDialog).execute();
    }

    public void storeTaskInProgress(Task task,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new StoreTaskInProgressAsyncTask(task,callback,progressDialog).execute();
    }

    public void encodeImage(Bitmap bitmap, String imgPath, boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new ImageEncoder(bitmap,imgPath,callback,progressDialog).execute();
    }

    public void uploadImageName(User user,String filename,int option,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new UploadImageNameAsyncTask(user,filename,option,callback,progressDialog).execute();
    }

    public void checkEmail(String email,boolean showDialog,GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new CheckEmailAsyncTask(email,callback,progressDialog).execute();
    }

    public void checkUsername(String username, boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new CheckUsernameAsyncTask(username,callback,progressDialog).execute();
    }

    public void uploadTask(Task task, boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new UploadTaskAsyncTask(task,callback,progressDialog).execute();
    }

    public void uploadPhotoToTask(Task task,String filename,boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new UploadPhotoToTaskAsyncTask(task,filename,callback,progressDialog).execute();
    }

    public void getDailyTaskID(Date date, boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new GetDailyTaskIDAsyncTask(date,callback,progressDialog).execute();
    }

    public void getDailyTasksID(boolean showDialog, GetCallback callback){
        if(showDialog){
            progressDialog.show();
        }
        new GetDailyTasksIDAsyncTask(callback,progressDialog).execute();
    }

}
