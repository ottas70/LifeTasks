package ottas70.lifetasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


import org.mindrot.jbcrypt.BCrypt;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import ottas70.lifetasks.TaskActivity.TaskActivity;


public class Login extends Activity implements View.OnClickListener {

    Button bLogin;
    EditText etEmail, etPassword;
    TextView registerLink;

    ProgressDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        LifeTasks lt = new LifeTasks();

        bLogin = (Button) findViewById(R.id.btn_login);
        etEmail = (EditText) findViewById(R.id.input_email);
        etPassword = (EditText) findViewById(R.id.input_password);
        registerLink = (TextView) findViewById(R.id.link_signup);

        bLogin.setOnClickListener(this);
        registerLink.setOnClickListener(this);

        //authenticate(new User("yyy@y.y",generateHash("yyy")));

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_login:
                if(validate()) {
                    String email = etEmail.getText().toString();
                    String password = etPassword.getText().toString();
                    System.out.println(generateHash(password));
                    User user = new User(email, generateHash(password));
                    authenticate(user);
                }
                break;
            case R.id.link_signup:
                Intent registerintent = new Intent(Login.this, Register.class);
                startActivity(registerintent);
                break;
        }
    }

    private void authenticate(User user) {
        dialog = new ProgressDialog(this);
        dialog.setCancelable(false);
        dialog.setTitle("Processing...");
        dialog.setMessage("Please wait...");
        dialog.show();
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.fetchUserDataAsyncTask(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                if (returnedUser == null) {
                    showErrorMessage();
                    dialog.dismiss();
                } else {
                    logUserIn(returnedUser);
                }
            }
        });
    }

    private void showErrorMessage() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Login.this);
        dialogBuilder.setMessage("Incorrect user data");
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }

    private void logUserIn(final User user) {
        LifeTasks.instance.setUser(user);
        ServerRequest request = new ServerRequest(this);
        request.getTasksAsyncTask(new GetCallback() {
            @Override
            public void done(Object o) {
                if (o == null) {
                    LifeTasks.instance.tasks.showErrorMessage(bLogin.getContext());
                } else {
                    LifeTasks.instance.tasks.getTasks().clear();
                    LifeTasks.instance.tasks.getTasks().addAll((Collection<? extends Task>) o);
                    final ServerRequest request = new ServerRequest(bLogin.getContext());
                    request.getFinishedTasksIDAsyncTask(user, false, new GetCallback() {
                        @Override
                        public void done(Object o) {
                            user.setFinishedTasks((ArrayList<Integer>) o);
                            DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
                            Date date = new Date();
                            dateFormat.format(date);
                            request.getDailyTaskID(date, false, new GetCallback() {
                                @Override
                                public void done(Object o) {
                                    if(o == null){
                                        LifeTasks.instance.dailyTask = null;
                                    }else{
                                        LifeTasks.instance.dailyTask = LifeTasks.instance.tasks.getTaskByID((Integer)o);
                                    }
                                    request.getDailyTasksID(false, new GetCallback() {
                                        @Override
                                        public void done(Object o) {
                                            LifeTasks.instance.tasks.removeTasks((ArrayList<Integer>) o);
                                            LifeTasks.instance.getUser().removeFinishedTasks((ArrayList<Integer>) o);
                                            request.getTasksInProgressAsyncTask(user, false, new GetCallback() {
                                                @Override
                                                public void done(Object o) {
                                                    user.setTasksInProgress((ArrayList<Integer>) o);
                                                    LifeTasks.instance.getUser().setUsersTasks();
                                                    dialog.dismiss();
                                                    bLogin.getContext().startActivity(new Intent(bLogin.getContext(), MainMenu.class));
                                                }
                                            });
                                        }
                                    });
                                }
                            });
                        }
                    });
                }
            }
        });

    }

    public boolean validate() {
        boolean valid = true;

        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        return valid;
    }

    public String generateHash(String toHash) {
        MessageDigest md = null;
        byte[] hash = null;
        try {
            md = MessageDigest.getInstance("SHA-512");
            hash = md.digest(toHash.getBytes("UTF-8"));
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return convertToHex(hash);
    }

    private String convertToHex(byte[] raw) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < raw.length; i++) {
            sb.append(Integer.toString((raw[i] & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }


    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }
}
