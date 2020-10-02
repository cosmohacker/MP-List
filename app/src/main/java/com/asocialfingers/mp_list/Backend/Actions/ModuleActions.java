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
import com.asocialfingers.mp_list.Backend.Models.Module.ModuleDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModuleActions {


    public void addModule(final Context context, final EditText _module, final String TAG, final String userIdHolder) {

        final String moduleHolder = _module.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.insertModule, new Response.Listener<String>() {

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
                params.put("Module_Name", moduleHolder);

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

    public void getAllModules(final String TAG, final Context context, final ArrayList<String> _modules_, final Spinner spinnerPieceModule, final Spinner spinnerVersionModule) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getModules, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray modulesArray = obj.getJSONArray("modules");
                        for (int i = 0; i < modulesArray.length(); i++) {
                            JSONObject modulesOBJ = (JSONObject) modulesArray.get(i);
                            ModuleDetails md = new ModuleDetails();
                            md.setModule_name(modulesOBJ.getString("Module_Name"));

                            ArrayAdapter moduleSpinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
                            moduleSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            moduleSpinnerAdapter.clear();
                            moduleSpinnerAdapter.add("Modül");
                            _modules_.add(modulesOBJ.getString("Module_Name"));
                            moduleSpinnerAdapter.addAll(_modules_);
                            spinnerPieceModule.setAdapter(moduleSpinnerAdapter);
                            spinnerVersionModule.setAdapter(moduleSpinnerAdapter);
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

    public void getAllModulesList(final String TAG, final Context context, final ArrayList<String> _modules_, final Spinner moduleSpinner,final ArrayAdapter arrayAdapter) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getModules, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray modulesArray = obj.getJSONArray("modules");
                        for (int i = 0; i < modulesArray.length(); i++) {
                            JSONObject modulesOBJ = (JSONObject) modulesArray.get(i);
                            arrayAdapter.clear();
                            _modules_.add(modulesOBJ.getString("Module_Name"));
                            arrayAdapter.addAll(_modules_);
                            moduleSpinner.setAdapter(arrayAdapter);
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

}
