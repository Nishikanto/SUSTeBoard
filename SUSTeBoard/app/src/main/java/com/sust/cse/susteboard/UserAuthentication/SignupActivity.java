package com.sust.cse.susteboard.UserAuthentication;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.sust.cse.susteboard.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class SignupActivity extends AppCompatActivity {
    private static final String TAG = "SignupActivity";
    private String name;
    private String email;
    private String pass;

    @InjectView(R.id.input_name)
    EditText _nameText;
    @InjectView(R.id.input_email)
    EditText _emailText;
    @InjectView(R.id.input_password)
    EditText _passwordText;
    @InjectView(R.id.btn_signup)
    Button _signupButton;
    @InjectView(R.id.link_login)
    TextView _loginLink;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        ButterKnife.inject(this);

        _signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signup();
            }
        });

        _loginLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Finish the registration screen and return to the Login activity
                finish();
            }
        });
    }

    public void signup() {
        Log.d(TAG, "Signup");

        if (!validate()) {
            onSignupFailed();
            return;
        }

        _signupButton.setEnabled(false);

        final ProgressDialog progressDialog = new ProgressDialog(SignupActivity.this,
                R.style.AppTheme_Dark_Dialog);
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Creating Account...");
        progressDialog.show();

        name = _nameText.getText().toString();
        email = _emailText.getText().toString();
        pass = _passwordText.getText().toString();

        /*JSONObject obj = new JSONObject();
        try {
            obj.put("name", name);
            obj.put("email", email);
            obj.put("password", pass);
        } catch (JSONException e) {
            e.printStackTrace();
        }*/


        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://192.168.43.147:8080/SUSTeBoardServer/SusteboardServlet";


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contains("0")){
                            Log.d("simul", response.toString());
                            progressDialog.dismiss();
                            alartDialog("You are already registered");
                            //Toast.makeText(getBaseContext(), "You are already registered", Toast.LENGTH_LONG).show();
                            _signupButton.setEnabled(true);
                        }

                        else if(response.contains("1")){
                            progressDialog.dismiss();
                            alartDialog("Registration failed. Please try again");
                            //Toast.makeText(getBaseContext(), "Registration failed. Please try again", Toast.LENGTH_LONG).show();
                            _signupButton.setEnabled(true);

                        }

                        else if(response.contains("2")){
                            progressDialog.dismiss();
                            alartDialog("Registration successful. Please Log in");
                            //Toast.makeText(getBaseContext(), "Registration successful. Please Log in", Toast.LENGTH_LONG).show();
                            onSignupSuccess();
                        }

                        else {
                            progressDialog.dismiss();
                            alartDialog("Registration failed. Please try again");
                            //Toast.makeText(getBaseContext(), "Registration failed. Please try again", Toast.LENGTH_LONG).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                onSignupFailed();

                progressDialog.dismiss();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("command", "registration");
                params.put("name", name);
                params.put("email", email);
                params.put("password", pass);
                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    public void onSignupSuccess() {
        _signupButton.setEnabled(true);
        //setResult(RESULT_OK, null);
        finish();
    }

    public void onSignupFailed() {
        Toast.makeText(getBaseContext(), "Sign up failed", Toast.LENGTH_LONG).show();
        _signupButton.setEnabled(true);
    }

    public boolean validate() {
        boolean valid = true;

        String name = _nameText.getText().toString();
        String email = _emailText.getText().toString();
        String password = _passwordText.getText().toString();

        if (name.isEmpty() || name.length() < 3) {
            _nameText.setError("at least 3 characters");
            valid = false;
        } else {
            _nameText.setError(null);
        }

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
        AlertDialog.Builder builder1 = new AlertDialog.Builder(SignupActivity.this);
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