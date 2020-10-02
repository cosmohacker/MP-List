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
import com.asocialfingers.mp_list.Backend.Models.Brand.BrandDetails;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class BrandActions {

    public void addBrand(final Context context, final String TAG, final EditText _brand, final String userIdHolder) {

        final String brandHolder = _brand.getText().toString();

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.insertBrand, new Response.Listener<String>() {

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
                params.put("Brand_Name", brandHolder);

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

    public void getAllBrands(final String TAG, final Context context, final ArrayList<String> _brands_, final Spinner spinnerModelBrand, final Spinner spinnerPieceBrand, final Spinner spinnerVersionBrand) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getBrands, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray brandsArray = obj.getJSONArray("brands");
                        for (int i = 0; i < brandsArray.length(); i++) {
                            JSONObject brandsOBJ = (JSONObject) brandsArray.get(i);
                            BrandDetails bd = new BrandDetails();
                            bd.setBrand_name(brandsOBJ.getString("Brand_Name"));
                            bd.setBrand_id(brandsOBJ.getString("Brand_Id"));

                            ArrayAdapter brandModelAdapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item);
                            brandModelAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            brandModelAdapter.clear();
                            brandModelAdapter.add("Marka");
                            _brands_.add(brandsOBJ.getString("Brand_Name"));
                            brandModelAdapter.addAll(_brands_);
                            spinnerModelBrand.setAdapter(brandModelAdapter);
                            spinnerPieceBrand.setAdapter(brandModelAdapter);
                            spinnerVersionBrand.setAdapter(brandModelAdapter);
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
                //brandAdapter.notifyDataSetChanged();

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

    public void getAllBrandsList(final String TAG, final Context context, final ArrayList<String> _brands_, final Spinner brandSpinner, final ArrayAdapter arrayAdapter) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getBrands, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray brandsArray = obj.getJSONArray("brands");
                        for (int i = 0; i < brandsArray.length(); i++) {
                            JSONObject brandsOBJ = (JSONObject) brandsArray.get(i);
                            arrayAdapter.clear();
                            _brands_.add(brandsOBJ.getString("Brand_Name"));
                            arrayAdapter.addAll(_brands_);
                            brandSpinner.setAdapter(arrayAdapter);
                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
                //brandAdapter.notifyDataSetChanged();

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
