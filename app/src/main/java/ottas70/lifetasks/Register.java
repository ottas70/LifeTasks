package ottas70.lifetasks;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.mindrot.jbcrypt.BCrypt;


public class Register extends Activity implements View.OnClickListener{

    EditText etUsername,etEmail,etPassword;
    Button bRegister;
    TextView loginLink;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etUsername = (EditText)findViewById(R.id.input_name);
        etEmail = (EditText)findViewById(R.id.input_email);
        etPassword = (EditText)findViewById(R.id.input_password);
        bRegister = (Button)findViewById(R.id.btn_signup);
        loginLink = (TextView)findViewById(R.id.link_login);

        bRegister.setOnClickListener(this);
        loginLink.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){
            case R.id.btn_signup:
                if(validate()) {
                    final String username = etUsername.getText().toString();
                    final String email = etEmail.getText().toString();
                    final String password = etPassword.getText().toString();


                    final ServerRequest request = new ServerRequest(this);
                    request.checkEmail(email, true, new GetCallback() {
                        @Override
                        public void done(Object o) {
                            Boolean emailExists = (Boolean) o;
                            if (emailExists.booleanValue()) {
                                showErrorMessage("Email is already used");
                            } else {
                                request.checkUsername(username, true, new GetCallback() {
                                    @Override
                                    public void done(Object o) {
                                        Boolean usernameExists = (Boolean) o;
                                        if (usernameExists.booleanValue()) {
                                            showErrorMessage("Username is already used");
                                        } else {
                                            User user = new User(username,email,password);
                                            registerUser(user);
                                        }
                                    }
                                });

                            }
                        }
                    });
                }
                break;
            case R.id.link_login:
                Intent loginIntent = new Intent(Register.this,Login.class);
                startActivity(loginIntent);
                break;
        }
    }

    private void registerUser(User user){
        ServerRequest serverRequest = new ServerRequest(this);
        serverRequest.storeUserDataInBackground(user, new GetUserCallback() {
            @Override
            public void done(User returnedUser) {
                Intent loginIntent = new Intent(Register.this, Login.class);
                startActivity(loginIntent);
            }
        });
    }

    public boolean validate() {
        boolean valid = true;

        String name = etUsername.getText().toString();
        String email = etEmail.getText().toString();
        String password = etPassword.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            etUsername.setError("at least 3 characters");
            valid = false;
        } else {
            etUsername.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            etEmail.setError("enter a valid email address");
            valid = false;
        } else {
            etEmail.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            etPassword.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            etPassword.setError(null);
        }

        return valid;
    }

    private void showErrorMessage(String message) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(Register.this);
        dialogBuilder.setMessage(message);
        dialogBuilder.setPositiveButton("Ok", null);
        dialogBuilder.show();
    }


}
