package com.erd.reblood.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.erd.reblood.BaseActivity;
import com.erd.reblood.R;
import com.erd.reblood.app.AppController;
import com.erd.reblood.utils.Constants;
import com.erd.reblood.utils.SessionManager;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by E.R.D on 4/2/2016.
 */
public class LoginActivity extends BaseActivity {
    private ProgressDialog pDialog;
    private static long back_pressed;
    private SessionManager session;
    private static String URL_LOGIN = Constants.BASE_URL + "/users/login";
    private static final String TAG = LoginActivity.class.getName();

    @BindView(R.id.toolbar) Toolbar toolbar;
    @BindView(R.id.uid) EditText editUserId;
    @BindView(R.id.password) EditText editPass;
    @BindView(R.id.buttonlogin) Button btnLogin;
    @BindView(R.id.textViewRegister) Button tvRegister;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);

        editUserId.setTypeface(Typeface.DEFAULT);
        editPass.setTypeface(Typeface.DEFAULT);
        editPass.setTransformationMethod(new PasswordTransformationMethod());

        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);

        session = new SessionManager(getApplicationContext());
        if (session.isLoggedIn()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }

        btnLogin.setOnClickListener(new View.OnClickListener() {

            public void onClick(View view) {
                String username = editUserId.getText().toString();
                String password = editPass.getText().toString();
                if (username.trim().length() > 0 && password.trim().length() > 0) {

                    ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                        checkLogin(username, password);
                    }
                    else{ Toast.makeText(getApplicationContext(),
                            "No Internet Access Available!", Toast.LENGTH_LONG)
                            .show();}
                } else {
                    Toast.makeText(getApplicationContext(),
                            "Please enter Phone Number and Password", Toast.LENGTH_LONG)
                            .show();
                }
            }
        });

        tvRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), RegisterActivity.class);
                startActivity(intent);
            }
        });

    }
    private void checkLogin(final String username, final String password) {
        String tag_string_req = "req_login";

        pDialog.setMessage("Logging in ...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST, URL_LOGIN, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    String cid = jObj.getString("customer_id");
                    String firstname = jObj.getString("first_name");
                    String lastname = jObj.getString("last_name");
                    String pic = jObj.getString("pic");
                    String status = jObj.getString("status");

                    session.setLogin(true);
                    session.createLoginSession(username, password, cid, firstname, lastname);

                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    intent.putExtra("cid", cid);
                    intent.putExtra("fname", firstname);
                    intent.putExtra("lname", lastname);
                    intent.putExtra("pic", pic);
                    intent.putExtra("status", status);

                    startActivity(intent);
                    finish();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                if (error != null) { //NULL DATA GIVEN
                    if (error.toString().equals("com.android.volley.ServerError")){
                        //Log.d("salahserver", "salahserver" + error.toString());
                        Toast.makeText(getApplicationContext(), "Phone Number and Password did not match", Toast.LENGTH_LONG).show();
                    } else{
                        //Log.d("salah", "salah" + error.toString());
                        Toast.makeText(getApplicationContext(), "Please try again later", Toast.LENGTH_LONG).show();
                    }
                    hideDialog();
                }else{ //DATA GIVEN
                    Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
                    hideDialog();}}
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("cid", username);
                params.put("pin", password);
                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        LoginActivity.super.onBackPressed();
        finish();
        back_pressed = System.currentTimeMillis();
    }
}
