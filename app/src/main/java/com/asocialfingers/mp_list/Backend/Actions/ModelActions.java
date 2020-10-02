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
import com.asocialfingers.mp_list.Backend.Models.Model.ModelDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ModelActions {

    public void addModel(final String TAG, final EditText _model, final Context context, final String userIdHolder, final String modelVersionIdHolder,final String modelCategoryIdHolder,
                         final String modelBrandIdHolder) {

        final String modelHolder = _model.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.insertModel, new Response.Listener<String>() {

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
                params.put("Brand_Id", modelBrandIdHolder);
                params.put("Category_Id", modelCategoryIdHolder);
                params.put("Version_Id", modelVersionIdHolder);
                params.put("Model_Name", modelHolder);

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

    public void getAllModels(final Context context, final String TAG, final ArrayList<String> _models_, final Spinner spinnerPieceModel, final Spinner spinnerVersionModel) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getModels, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray modelsArray = obj.getJSONArray("models");
                        for (int i = 0; i < modelsArray.length(); i++) {
                            JSONObject modelsOBJ = (JSONObject) modelsArray.get(i);
                            ModelDetails md = new ModelDetails();
                            md.setModel_name(modelsOBJ.getString("Model_Name"));

                            ArrayAdapter modelSpinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
                            modelSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            modelSpinnerAdapter.clear();
                            modelSpinnerAdapter.add("Model");
                            _models_.add(modelsOBJ.getString("Model_Name"));
                            modelSpinnerAdapter.addAll(_models_);
                            spinnerPieceModel.setAdapter(modelSpinnerAdapter);
                            spinnerVersionModel.setAdapter(modelSpinnerAdapter);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(context, "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(context, "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //brandAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
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

    public void getAllModelsList(final Context context, final String TAG, final ArrayList<String> _models_, final Spinner modelSpinner,final  ArrayAdapter arrayAdapter) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getModels, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray modelsArray = obj.getJSONArray("models");
                        for (int i = 0; i < modelsArray.length(); i++) {
                            JSONObject modelsOBJ = (JSONObject) modelsArray.get(i);
                            arrayAdapter.clear();
                            _models_.add(modelsOBJ.getString("Model_Name"));
                            arrayAdapter.addAll(_models_);
                            modelSpinner.setAdapter(arrayAdapter);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(context, "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(context, "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                //brandAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(context, "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
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
