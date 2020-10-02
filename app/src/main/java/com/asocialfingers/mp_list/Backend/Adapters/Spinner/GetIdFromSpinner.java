package com.asocialfingers.mp_list.Backend.Adapters.Spinner;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asocialfingers.mp_list.Backend.Connection.AppController;

import org.json.JSONException;
import org.json.JSONObject;

public class GetIdFromSpinner {

    public String idHolder;

    public void GetSpinnerValueId(final String TAG, final Context context, final String linkTitle, final String linkCredential, final String linkCredentialValue,
                                  final String valueTitle, final String objectTitle, final String childTitle,final String artificialIdHolder) {

        String endPoint = linkTitle.replace(linkCredential, linkCredentialValue);

        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, valueTitle + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject objectDetails = parentObject.getJSONObject(objectTitle);

                    // String artificialIdHolder = userDetails.getString(childTitle);
                    idHolder = objectDetails.getString(childTitle);
                    Toast.makeText(context, idHolder, Toast.LENGTH_SHORT).show();


                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(context, "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
            }
        });
        AppController.getInstance().addToRequestQueue(strReq);

    }

}
