package com.sust.cse.susteboard.UserAuthentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sust.cse.susteboard.NoticeUtility.AdminPage;
import com.sust.cse.susteboard.NoticeUtility.IntroPage;
import com.sust.cse.susteboard.R;
import com.sust.cse.susteboard.UserAuthentication.SignupActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private static final int REQUEST_SIGNUP = 0;

    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_login)
    Button _loginButton;
    @InjectView(R.id.link_signup)
    TextView _signupLink;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);

        ArrayList<String> selectedItems;
        String PREFS_NAME = "SUSTeBoard_Settings";
        SharedPreferences settings;

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);


        String email = settings.getString("email", "");
        String password = settings.getString("password", "");
        String position = settings.getString("position", "");

        if(!email.equals("") && !password.equals("")){
            onLoginSuccess(position);
        }

        _loginButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                login();
            }
        });

        _signupLink.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // Start the Signup activity
                Intent intent = new Intent(getApplicationContext(), SignupActivity.class);
                startActivityForResult(intent, REQUEST_SIGNUP);
            }
        });
    }

    public void login() {
        Log.d(TAG, "Login");

        if (!validate()) {
            onLoginFailed();
            return;
        }

        _loginButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Authenticating...");
        progressDialog.show();

        final String email = _emailText.getText().toString();
        final String password = _passwordText.getText().toString();


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.43.147:8080/SUSTeBoardServer/SusteboardServlet?command=login&&email="+email+"&&pass="+password;


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            Log.d("simul", response.toString());
                            JSONObject jsonObject = new JSONObject(response);
                            String user = jsonObject.getString("user");
                            String position = null;
                            if(user.equals("valid")){
                                position = jsonObject.getString("position");
                                Log.d("simul", user + " " + position);
                                if(position.equals("student")){
                                    //Toast.makeText(getApplicationContext(), "Loged in as student", Toast.LENGTH_LONG).show();
                                }else if(position.equals("admin"))
                                    //Toast.makeText(getApplicationContext(), "Loged in as admin", Toast.LENGTH_LONG).show();
                                    progressDialog.dismiss();
                                onLoginSuccess(position);
                            }else if(user.equals("invalid")){
                                progressDialog.dismiss();
                                alartDialog("Invalid email or password");
                                //Toast.makeText(getApplicationContext(), "Email or Password Invalid", Toast.LENGTH_LONG).show();
                                onLoginFailed();
                            }

                            savaAuthentication(email, password, position);

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                //Log.d("simul", error.getMessage().toString());
                alartDialog("Login Faild");
                onLoginFailed();
                progressDialog.dismiss();
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void savaAuthentication(String email, String password, String position) {
        ArrayList<String> selectedItems;
        String PREFS_NAME = "SUSTeBoard_Settings";
        SharedPreferences settings;

        settings = getSharedPreferences(PREFS_NAME, MODE_PRIVATE);
        // Writing data to SharedPreferences
        SharedPreferences.Editor editor = settings.edit();
        editor.putString("email", email);
        editor.putString("password", password);
        editor.putString("position", position);
        editor.commit();


    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_SIGNUP) {
            if (resultCode == RESULT_OK) {
                this.finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        // Disable going back to the MainActivity
        moveTaskToBack(true);
    }

    public void onLoginSuccess(String position) {
        _loginButton.setEnabled(true);
        if(position.equals("student")){
            Intent i = new Intent(this, IntroPage.class);
            startActivity(i);
        }
        else if(position.equals("admin")){
            Intent i = new Intent(this, AdminPage.class);
            startActivity(i);
        }
    }

    public void onLoginFailed() {
         //Toast.makeText(getBaseContext(), "Login failed", Toast.LENGTH_LONG).show();
        _loginButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            _emailText.setError("enter a valid email address");
            valid = false;
        } else {
            _emailText.setError(null);
        }

        if (password.isEmpty() || password.length() < 4 || password.length() > 10) {
            _passwordText.setError("between 4 and 10 alphanumeric characters");
            valid = false;
        } else {
            _passwordText.setError(null);
        }

        return valid;
    }

    public void alartDialog(String msg){
        AlertDialog.Builder builder1 = new AlertDialog.Builder(LoginActivity.this);
        builder1.setMessage(msg);
        builder1.setCancelable(true);
        builder1.setPositiveButton("Ok",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert11 = builder1.create();
        alert11.show();
    }
}
