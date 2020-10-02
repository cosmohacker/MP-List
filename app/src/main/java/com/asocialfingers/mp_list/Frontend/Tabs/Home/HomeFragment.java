package com.asocialfingers.mp_list.Frontend.Tabs.Home;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asocialfingers.mp_list.Backend.Adapters.Piece.PieceAdapter;
import com.asocialfingers.mp_list.Backend.Adapters.Users.UsersAdapter;
import com.asocialfingers.mp_list.Backend.Connection.AppController;
import com.asocialfingers.mp_list.Backend.Connection.ConnectionLinks;
import com.asocialfingers.mp_list.Backend.Models.Piece.PieceDetails;
import com.asocialfingers.mp_list.Backend.Models.Users.UsersDetails;
import com.asocialfingers.mp_list.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class HomeFragment extends Fragment {

    private LinearLayout layoutLastEntries, layoutUsers, layoutClock, layoutInfo, layoutNotes;
    private static String TAG = HomeFragment.class.getSimpleName();
    private String cookieHolder = ConnectionLinks.CookieHolder;
    private int pieceTotalJunkCount, pieceTotalCount;
    private TextView _junkCount, _count, _percentage;
    private ArrayList<UsersDetails> usersDetails;
    private ArrayList<PieceDetails> pieceDetails;
    private ProgressBar _countPiece, _junkPiece;
    private RecyclerView _users, _pieces;
    private RelativeLayout layoutGraph;
    private PieceAdapter pieceAdapter;
    private UsersAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        methodCleaner(root);

        return root;
    }

    private void methodCleaner(View root) {

        utils(root);
        colorEvents();
        usersRecyclerViewEvents();
        piecesRecyclerViewEvents();
        getTotalPieceCount();
        getTotalPieceJunkCount();
        getPercentageAndSum();

    }

    private void colorEvents() {

        Random random = new Random();
        int color1 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color2 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color3 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color4 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color5 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color6 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color7 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));
        int color8 = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        //region Drawable

        GradientDrawable g1 = new GradientDrawable();
        g1.setCornerRadius(10);
        g1.setColor(color1);

        GradientDrawable g2 = new GradientDrawable();
        g2.setCornerRadius(10);
        g2.setColor(color2);

        GradientDrawable g3 = new GradientDrawable();
        g3.setCornerRadius(10);
        g3.setColor(color3);

        GradientDrawable g4 = new GradientDrawable();
        g4.setCornerRadius(10);
        g4.setColor(color4);

        GradientDrawable g5 = new GradientDrawable();
        g5.setCornerRadius(10);
        g5.setColor(color5);

        GradientDrawable g6 = new GradientDrawable();
        g6.setCornerRadius(10);
        g6.setColor(color6);

        //endregion

        layoutClock.setBackground(g1);
        layoutGraph.setBackground(g2);
        layoutInfo.setBackground(g3);
        layoutLastEntries.setBackground(g4);
        layoutNotes.setBackground(g5);
        layoutUsers.setBackground(g6);

        _countPiece.getProgressDrawable().setColorFilter(color7, android.graphics.PorterDuff.Mode.SRC_IN);
        _junkPiece.getProgressDrawable().setColorFilter(color8, android.graphics.PorterDuff.Mode.SRC_IN);

    }

    private void utils(View root) {

        layoutLastEntries = (LinearLayout) root.findViewById(R.id.layoutLastEntries);
        layoutUsers = (LinearLayout) root.findViewById(R.id.layoutUsers);
        layoutNotes = (LinearLayout) root.findViewById(R.id.layoutNotes);
        layoutClock = (LinearLayout) root.findViewById(R.id.layoutClock);
        layoutInfo = (LinearLayout) root.findViewById(R.id.layoutInfo);

        layoutGraph = (RelativeLayout) root.findViewById(R.id.layoutGraph);

        _pieces = (RecyclerView) root.findViewById(R.id.recyclerViewLastPieces);
        _users = (RecyclerView) root.findViewById(R.id.recyclerViewUsers);

        _junkCount = (TextView) root.findViewById(R.id.txtTotalJunkCount);
        _count = (TextView) root.findViewById(R.id.txtTotalPieceCount);
        _percentage = (TextView) root.findViewById(R.id.txtPercentage);

        _countPiece = (ProgressBar) root.findViewById(R.id.prgCount);
        _junkPiece = (ProgressBar) root.findViewById(R.id.prgJunk);

    }

    private void usersRecyclerViewEvents() {

        usersDetails = new ArrayList<>();
        mAdapter = new UsersAdapter(getActivity(), usersDetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        _users.setHasFixedSize(true);
        _users.setLayoutManager(layoutManager);
        //_users.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        _users.setItemAnimator(new DefaultItemAnimator());
        _users.setAdapter(mAdapter);

        getAllUsers();
    }

    private void getAllUsers() {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getUsers, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("users");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject usersOBJ = (JSONObject) chatRoomsArray.get(i);
                            UsersDetails ud = new UsersDetails();
                            ud.setUsername(usersOBJ.getString("Username"));
                            ud.setPermission(usersOBJ.getString("Permission"));

                            usersDetails.add(ud);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                mAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                //Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }


        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", cookieHolder);

                return params;
            }
        };



        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void piecesRecyclerViewEvents() {

        pieceDetails = new ArrayList<>();
        pieceAdapter = new PieceAdapter(getActivity(), pieceDetails);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(RecyclerView.VERTICAL);
        _pieces.setHasFixedSize(true);
        _pieces.setLayoutManager(layoutManager);
        //_users.addItemDecoration(new SimpleDividerItemDecoration(getApplicationContext()));
        _pieces.setItemAnimator(new DefaultItemAnimator());
        _pieces.setAdapter(pieceAdapter);

        getLastTenPieces();

    }

    private void getLastTenPieces() {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getLastPieces, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("pieces");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject usersOBJ = (JSONObject) chatRoomsArray.get(i);
                            PieceDetails ud = new PieceDetails();
                            ud.setPiece_name(usersOBJ.getString("Piece_Name"));
                            ud.setPrice(usersOBJ.getString("Price"));
                            ud.setCurrency(usersOBJ.getString("Price_Type"));

                            pieceDetails.add(ud);
                        }

                    } else {
                        // error in fetching chat rooms
                        Toast.makeText(getActivity(), "" + obj.getJSONObject("error").getString("message"), Toast.LENGTH_LONG).show();
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                    Toast.makeText(getActivity(), "Json parse error: " + e.getMessage(), Toast.LENGTH_LONG).show();
                }
                pieceAdapter.notifyDataSetChanged();

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                NetworkResponse networkResponse = error.networkResponse;
                Log.e(TAG, "Volley error: " + error.getMessage() + ", code: " + networkResponse);
                Toast.makeText(getActivity(), "Volley error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", cookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void getTotalPieceCount() {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getTotalPieceCount, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("pieces");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject usersOBJ = (JSONObject) chatRoomsArray.get(i);
                            pieceTotalCount = Integer.parseInt(usersOBJ.getString("Count"));
                            _count.setText(usersOBJ.getString("Count"));
                            _countPiece.setProgress(Integer.parseInt(usersOBJ.getString("Count")));
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", cookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void getTotalPieceJunkCount() {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getTotalJunkCount, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("pieces");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject usersOBJ = (JSONObject) chatRoomsArray.get(i);
                            pieceTotalJunkCount = Integer.parseInt(usersOBJ.getString("Junk_Count"));
                            _junkCount.setText(usersOBJ.getString("Junk_Count"));
                            _junkPiece.setProgress(Integer.parseInt(usersOBJ.getString("Junk_Count")));
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
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();

                params.put("Cookie", cookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void getPercentageAndSum() {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getPercentageAndSum, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    // check for error flag
                    if (obj.getBoolean("error") == false) {
                        JSONArray chatRoomsArray = obj.getJSONArray("pieces");
                        for (int i = 0; i < chatRoomsArray.length(); i++) {
                            JSONObject usersOBJ = (JSONObject) chatRoomsArray.get(i);
                            _percentage.setText("% " + usersOBJ.getString("Percentage"));
                            _junkPiece.setMax(Integer.parseInt(usersOBJ.getString("Sum")));
                            _countPiece.setMax(Integer.parseInt(usersOBJ.getString("Sum")));
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
        }){
                @Override
                public Map<String, String> getHeaders() throws AuthFailureError {
                    Map<String, String> params = new HashMap<String, String>();

                    params.put("Cookie", cookieHolder);

                    return params;
                }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

}
