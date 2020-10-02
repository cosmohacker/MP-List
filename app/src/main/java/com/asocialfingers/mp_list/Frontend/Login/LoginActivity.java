package com.asocialfingers.mp_list.Frontend.Login;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asocialfingers.mp_list.Backend.Connection.AppController;
import com.asocialfingers.mp_list.Backend.Connection.ConnectionLinks;
import com.asocialfingers.mp_list.Backend.Database.DBHelper;
import com.asocialfingers.mp_list.Backend.Database.UserDao;
import com.asocialfingers.mp_list.Backend.Database.UserDetails;
import com.asocialfingers.mp_list.Frontend.Main.MainActivity;
import com.asocialfingers.mp_list.R;
import com.google.android.material.snackbar.Snackbar;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity {

    private static String TAG = LoginActivity.class.getSimpleName();
    private String cookieHolder = ConnectionLinks.CookieHolder;
    private UserDetails userDetails = new UserDetails();
    private String usernameHolder, passwordHolder;
    private EditText _username, _password;
    private ConstraintLayout _relative;
    private CheckBox _remember;
    private DBHelper dbHelper;
    private UserDao userDao;
    private int checked = 0;
    private Button _login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userDao = new UserDao(this);
        dbHelper = new DBHelper(this);


        utils();
        checkBoxEvents();
        buttonEvents();

    }

    private void utils() {

        _relative = (ConstraintLayout) findViewById(R.id.relativeLoginPage);

        _username = (EditText) findViewById(R.id.txtUsername);
        _password = (EditText) findViewById(R.id.txtPassword);

        _remember = (CheckBox) findViewById(R.id.chcRemember);

        _login = (Button) findViewById(R.id.btnLogin);

    }

    private void buttonEvents() {

        _login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                fetchTexts();

                if (usernameHolder.isEmpty() || passwordHolder.isEmpty()) {
                    snackBarEvents(1, 0);
                } else {

                    loginQuery();

                }

            }
        });

    }

    private void snackBarEvents(int empty, int wrong) {

        if (empty == 1) {
            Snackbar snackbar = Snackbar
                    .make(_relative, "Lütfen alanları doldurunuz!", Snackbar.LENGTH_LONG)
                    .setAction("TAMAM", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sb = snackbar.getView();
            TextView textView = (TextView) sb.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        } else if (wrong == 1) {

            Snackbar snackbar = Snackbar
                    .make(_relative, "Kullanıcı adı veya şifre eşleşmiyor!", Snackbar.LENGTH_LONG)
                    .setAction("TAMAM", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                        }
                    });
            snackbar.setActionTextColor(Color.RED);
            View sb = snackbar.getView();
            TextView textView = (TextView) sb.findViewById(R.id.snackbar_text);
            textView.setTextColor(Color.YELLOW);
            snackbar.show();

        }

    }

    public void loginQuery() {
        String tag_string_req = "req_login";

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.login, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Login Response: " + response);
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        fetchTexts();

                        if (checked == 1) {

                            JSONObject uid = jObj.getJSONObject("user");
                            String fetchedUsername = uid.getString("Username");
                            String fetchedPassword = uid.getString("Password");
                            String fetchedPermission = uid.getString("Permission");
                            String fetchedCreatedAt = uid.getString("Created_At");

                            if (dbHelper.getUsernameData().isEmpty()) {
                                long user_id = userDao.insertUser(new UserDetails(-1, fetchedUsername, fetchedPassword, fetchedPermission, fetchedCreatedAt));
                            }

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        } else {

                            Intent i = new Intent(LoginActivity.this, MainActivity.class);
                            startActivity(i);
                            finish();

                        }

                    } else {
                        String errorMsg = jObj.getString("error_msg");
                        snackBarEvents(0, 1);

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
            }
        }) {

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("Username", usernameHolder);
                params.put("Password", passwordHolder);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", cookieHolder);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }

    private void checkBoxEvents() {

        _remember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    checked++;
                else
                    checked = 0;
            }
        });

    }

    private void fetchTexts() {

        usernameHolder = _username.getText().toString();
        passwordHolder = _password.getText().toString();

    }

}
