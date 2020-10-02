package com.asocialfingers.mp_list.Backend.Actions;

import android.content.Context;
import android.util.Log;
import android.view.View;
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
import com.asocialfingers.mp_list.Backend.Models.Category.CategoryDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class CategoryActions {

    public void addCategory(final EditText _category, final String TAG, final Context context, final String userIdHolder) {

        final String categoryHolder = _category.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.insertCategory, new Response.Listener<String>() {

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
                params.put("Category_Name", categoryHolder);

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

    public void getAllCategories(final Context context, final String TAG, final ArrayList<String> _category_, final Spinner spinnerModelCategory, final Spinner spinnerPieceCategory, final Spinner spinnerVersionCategory) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getCategories, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray categoriesArray = obj.getJSONArray("categories");
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject categoriesOBJ = (JSONObject) categoriesArray.get(i);
                            CategoryDetails cd = new CategoryDetails();
                            cd.setCategory_name(categoriesOBJ.getString("Category_Name"));

                            ArrayAdapter categorySpinnerAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
                            categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinnerAdapter.clear();
                            categorySpinnerAdapter.add("Kategori");
                            _category_.add(categoriesOBJ.getString("Category_Name"));
                            categorySpinnerAdapter.addAll(_category_);
                            spinnerModelCategory.setAdapter(categorySpinnerAdapter);
                            spinnerPieceCategory.setAdapter(categorySpinnerAdapter);
                            spinnerVersionCategory.setAdapter(categorySpinnerAdapter);
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

    public void getAllCategoriesList(final Context context, final String TAG, final ArrayList<String> _category_, final Spinner categorySpinner,final ArrayAdapter arrayAdapter) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getCategories, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray categoriesArray = obj.getJSONArray("categories");
                        for (int i = 0; i < categoriesArray.length(); i++) {
                            JSONObject categoriesOBJ = (JSONObject) categoriesArray.get(i);
                            arrayAdapter.clear();
                            _category_.add(categoriesOBJ.getString("Category_Name"));
                            arrayAdapter.addAll(_category_);
                            categorySpinner.setAdapter(arrayAdapter);
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
