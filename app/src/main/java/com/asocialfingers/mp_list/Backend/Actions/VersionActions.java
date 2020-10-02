package com.asocialfingers.mp_list.Backend.Actions;

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asocialfingers.mp_list.Backend.Connection.AppController;
import com.asocialfingers.mp_list.Backend.Connection.ConnectionLinks;
import com.asocialfingers.mp_list.Backend.Models.Version.VersionDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class VersionActions {

    public void getAllVersions(final String TAG, final Context context, final ArrayList<String> _versions_, final Spinner spinnerModelVersion, final Spinner spinnerPieceVersion) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getVersions, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray versionsArray = obj.getJSONArray("versions");
                        for (int i = 0; i < versionsArray.length(); i++) {
                            JSONObject versionsOBJ = (JSONObject) versionsArray.get(i);
                            VersionDetails vd = new VersionDetails();
                            vd.setVersion_name(versionsOBJ.getString("Version_Name"));

                            ArrayAdapter versionSpinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
                            versionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            versionSpinnerAdapter.add("Versiyon");
                            _versions_.add(versionsOBJ.getString("Version_Name"));
                            versionSpinnerAdapter.addAll(_versions_);
                            spinnerModelVersion.setAdapter(versionSpinnerAdapter);
                            spinnerPieceVersion.setAdapter(versionSpinnerAdapter);
                        }

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", ConnectionLinks.CookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    public void getAllVersionsList(final String TAG, final Context context, final ArrayList<String> _versions_,final Spinner spinnerValues
    ,final ArrayAdapter versionSpinnerAdapter) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getVersions, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray versionsArray = obj.getJSONArray("versions");
                        for (int i = 0; i < versionsArray.length(); i++) {
                            JSONObject versionsOBJ = (JSONObject) versionsArray.get(i);
                            versionSpinnerAdapter.clear();
                            _versions_.add(versionsOBJ.getString("Version_Name"));
                            versionSpinnerAdapter.addAll(_versions_);
                            spinnerValues.setAdapter(versionSpinnerAdapter);
                        }

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", ConnectionLinks.CookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    public void addVersion(final EditText _version, final String TAG, final Context context, final String userIdHolder, final String versionBrandIdHolder, final String versionCategoryIdHolder, final String versionModelIdHolder, final String versionModuleIdHolder) {

        final String versionHolder = _version.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.insertVersion, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");

                    if (!error) {
                    } else {
                        Toast.makeText(context, "Hata oluştu! Daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(context, "Server'da problem oluştu. Daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("User_Id", userIdHolder);
                params.put("Brand_Id", versionBrandIdHolder);
                params.put("Category_Id", versionCategoryIdHolder);
                params.put("Model_Id", versionModelIdHolder);
                params.put("Module_Id", versionModuleIdHolder);
                params.put("Version_Name", versionHolder);

                return params;
            }

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", ConnectionLinks.CookieHolder);

                return params;
            }

        };
        AppController.getInstance().addToRequestQueue(strReq);
    }

}
