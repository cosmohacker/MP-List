package com.asocialfingers.mp_list.Frontend.Tabs.List;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.Image;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asocialfingers.mp_list.Backend.Actions.BrandActions;
import com.asocialfingers.mp_list.Backend.Actions.CategoryActions;
import com.asocialfingers.mp_list.Backend.Actions.ModelActions;
import com.asocialfingers.mp_list.Backend.Actions.ModuleActions;
import com.asocialfingers.mp_list.Backend.Actions.VersionActions;
import com.asocialfingers.mp_list.Backend.Adapters.Piece.PieceListViewAdapter;
import com.asocialfingers.mp_list.Backend.Connection.AppController;
import com.asocialfingers.mp_list.Backend.Connection.ConnectionLinks;
import com.asocialfingers.mp_list.Backend.Models.Category.CategoryDetails;
import com.asocialfingers.mp_list.Backend.Models.Piece.PieceDetails;
import com.asocialfingers.mp_list.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ListFragment extends Fragment {

    private String cookieHolder = ConnectionLinks.CookieHolder, spinnerChoiceHolder, imageHolder, categoryIdHolder, categoryNameHolder;
    private int number_of_items_in_page = 12, number_of_buttons, piecesArrayCount, baseCountList = 12, startCountList = 0;
    private String[] spinnerTitles = {"Kategoriler", "Markalar", "Versiyonlar", "Modüller", "Modeller"};
    private ArrayList<String> _spinner_, _versions, _models, _modules, _brands, _categories;
    private CategoryActions categoryActions = new CategoryActions();
    private static String TAG = ListFragment.class.getSimpleName();
    private VersionActions versionActions = new VersionActions();
    private ModuleActions moduleActions = new ModuleActions();
    private ModelActions modelActions = new ModelActions();
    private BrandActions brandActions = new BrandActions();
    private Spinner _spinner, _allSpinners, delSpinner;
    private ImageButton _slideDown, closeSpinnerEdit;
    private ArrayList<PieceDetails> pieceDetails;
    private PieceListViewAdapter adapter;
    private Dialog _spinnerList;
    private GridView gridView;
    private EditText _search;
    private Button btns;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_list, container, false);

        _spinnerList = new Dialog(getActivity());

        utils(root);
        gridViewEvents(root);
        getAllCategories();
        search(root);
        slides();
        btnFooter(root);

        return root;
    }

    private void utils(View root) {
        _spinner_ = new ArrayList<String>();

        _spinner = (Spinner) root.findViewById(R.id.spinnerListPieceCategory);
        gridView = (GridView) root.findViewById(R.id.recycler_view_posts);
        _search = (EditText) root.findViewById(R.id.txtSearchPieces);
        _slideDown = (ImageButton) root.findViewById(R.id.btnEditPieceDetails);
    }

    private void slides() {

        _slideDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spinnerDataPopUp();
            }
        });

    }

    //region Volley

    private void listAllPieces(final View root, final int baseCount, final int startCount) {

        StringRequest strReq = new StringRequest(Request.Method.GET, ConnectionLinks.getPieces, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "response: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        JSONArray piecesArray = obj.getJSONArray("pieces");
                        piecesArrayCount = piecesArray.length();
                        //for (int i = start; i < (start) + number_of_items_in_page; i++) {
                        for (int i = startCount; i < baseCount; i++) {

                            JSONObject piecesOBJ = (JSONObject) piecesArray.get(i);
                            PieceDetails pd = new PieceDetails();

                            pd.setPiece_id(piecesOBJ.getString("Piece_Id"));
                            pd.setUser_id(piecesOBJ.getString("User_Id"));
                            pd.setBrand_id(piecesOBJ.getString("Brand_Id"));
                            pd.setCategory_id(piecesOBJ.getString("Category_Id"));
                            pd.setModel_id(piecesOBJ.getString("Model_Id"));
                            pd.setVersion_id(piecesOBJ.getString("Version_Id"));
                            pd.setModule_id(piecesOBJ.getString("Module_Id"));
                            pd.setPrice(piecesOBJ.getString("Price"));
                            pd.setCurrency(piecesOBJ.getString("Price_Type"));
                            pd.setCount(piecesOBJ.getString("Count"));
                            pd.setPiece_name(piecesOBJ.getString("Piece_Name"));
                            pd.setJunk_count(piecesOBJ.getString("Junk_Count"));
                            pd.setDescription(piecesOBJ.getString("Description"));
                            pd.setNote(piecesOBJ.getString("Note"));
                            pd.setImage(piecesOBJ.getString("Image"));
                            pd.setCreated_at(piecesOBJ.getString("Created_At"));

                            imageHolder = piecesOBJ.getString("Image");

                            pieceDetails.add(pd);

                        }

                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
                adapter.notifyDataSetChanged();

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

                params.put("Cookie", cookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void getCategoryId() {

        String endPoint = ConnectionLinks.getCategoryId.replace("_CATEGORY_ID_", categoryNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Pieces: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("categories");

                    categoryIdHolder = userDetails.getString("Category_Id");

                    getPiecesFromCategoryId();

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

                params.put("Cookie", cookieHolder);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void getAllCategories() {

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

                            ArrayAdapter categorySpinnerAdapter = new ArrayAdapter(getActivity(), R.layout.custom_spinner_item);
                            categorySpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            categorySpinnerAdapter.clear();
                            categorySpinnerAdapter.add("Kategori");
                            _spinner_.add(categoriesOBJ.getString("Category_Name"));
                            categorySpinnerAdapter.addAll(_spinner_);
                            _spinner.setAdapter(categorySpinnerAdapter);
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

                params.put("Cookie", cookieHolder);

                return params;
            }
        };

        //Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void getPiecesFromCategoryId() {

        String endPoint = ConnectionLinks.getPiecesFromCategory.replace("_CATEGORY_ID_", categoryIdHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Pieces: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        JSONArray piecesArray = obj.getJSONArray("pieces");

                        adapter.clear();

                        for (int i = 0; i < 40; i++) {

                            JSONObject piecesOBJ = (JSONObject) piecesArray.get(i);
                            PieceDetails pd = new PieceDetails();

                            pd.setPiece_id(piecesOBJ.getString("Piece_Id"));
                            pd.setUser_id(piecesOBJ.getString("User_Id"));
                            pd.setBrand_id(piecesOBJ.getString("Brand_Id"));
                            pd.setCategory_id(piecesOBJ.getString("Category_Id"));
                            pd.setModel_id(piecesOBJ.getString("Model_Id"));
                            pd.setVersion_id(piecesOBJ.getString("Version_Id"));
                            pd.setModule_id(piecesOBJ.getString("Module_Id"));
                            pd.setPrice(piecesOBJ.getString("Price"));
                            pd.setCurrency(piecesOBJ.getString("Price_Type"));
                            pd.setCount(piecesOBJ.getString("Count"));
                            pd.setPiece_name(piecesOBJ.getString("Piece_Name"));
                            pd.setJunk_count(piecesOBJ.getString("Junk_Count"));
                            pd.setDescription(piecesOBJ.getString("Description"));
                            pd.setNote(piecesOBJ.getString("Note"));
                            pd.setImage(piecesOBJ.getString("Image"));
                            pd.setCreated_at(piecesOBJ.getString("Created_At"));

                            imageHolder = piecesOBJ.getString("Image");

                            pieceDetails.add(pd);

                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
                adapter.notifyDataSetChanged();
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

                params.put("Cookie", cookieHolder);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);

    }

    private void searchPieceByName(String search) {

        String endPoint = ConnectionLinks.getPiecesFromName.replace("_PIECE_NAME_", search);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Pieces: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        JSONArray piecesArray = obj.getJSONArray("pieces");
                        piecesArrayCount = piecesArray.length();
                        for (int i = 0; i < piecesArrayCount; i++) {

                            JSONObject piecesOBJ = (JSONObject) piecesArray.get(i);
                            PieceDetails pd = new PieceDetails();

                            pd.setPiece_id(piecesOBJ.getString("Piece_Id"));
                            pd.setUser_id(piecesOBJ.getString("User_Id"));
                            pd.setBrand_id(piecesOBJ.getString("Brand_Id"));
                            pd.setCategory_id(piecesOBJ.getString("Category_Id"));
                            pd.setModel_id(piecesOBJ.getString("Model_Id"));
                            pd.setVersion_id(piecesOBJ.getString("Version_Id"));
                            pd.setModule_id(piecesOBJ.getString("Module_Id"));
                            pd.setPrice(piecesOBJ.getString("Price"));
                            pd.setCurrency(piecesOBJ.getString("Price_Type"));
                            pd.setCount(piecesOBJ.getString("Count"));
                            pd.setPiece_name(piecesOBJ.getString("Piece_Name"));
                            pd.setJunk_count(piecesOBJ.getString("Junk_Count"));
                            pd.setDescription(piecesOBJ.getString("Description"));
                            pd.setNote(piecesOBJ.getString("Note"));
                            pd.setImage(piecesOBJ.getString("Image"));
                            pd.setCreated_at(piecesOBJ.getString("Created_At"));

                            imageHolder = piecesOBJ.getString("Image");

                            pieceDetails.add(pd);

                        }
                    }

                } catch (JSONException e) {
                    Log.e(TAG, "json parsing error: " + e.getMessage());
                }
                adapter.notifyDataSetChanged();
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

                params.put("Cookie", cookieHolder);

                return params;
            }
        };
        AppController.getInstance().addToRequestQueue(strReq);

    }

    //endregion

    private void spinnerEvent(final View root) {

        _spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                categoryNameHolder = _spinner.getSelectedItem().toString();
                getCategoryId();

                if (position == 0) {
                    baseCountList = 12;
                    startCountList = 0;
                    listAllPieces(root, 12, 0);
                } else
                    btns.setVisibility(View.GONE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void search(final View root) {

        _search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String instantKey = _search.getText().toString();

                adapter.clear();
                searchPieceByName(instantKey);

                if (_search.getText().toString().isEmpty()) {
                    baseCountList = 12;
                    startCountList = 0;
                    adapter.clear();
                    listAllPieces(root, 12, 0);
                } else {
                    btns.setVisibility(View.GONE);
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void checkBtnBackground() {

        btns.setBackgroundDrawable(getResources().getDrawable(R.color.login_page_button_text_color_dark));
        btns.setTextColor(getResources().getColor(R.color.login_page_background_dark));

    }

    private void gridViewEvents(final View root) {

        pieceDetails = new ArrayList<>();
        adapter = new PieceListViewAdapter(getContext(), pieceDetails);
        gridView.setAdapter(adapter);

        spinnerEvent(root);

    }

    private void btnFooter(View root) {

        btns = new Button(getActivity());
        checkBtnBackground();
        btns.setText("Yükle");

        LinearLayout ll = (LinearLayout) root.findViewById(R.id.linearGridView);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT);
        ll.addView(btns, lp);

        btns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startCountList += 12;
                baseCountList += 12;

                if (baseCountList < piecesArrayCount) {
                    listAllPieces(v, baseCountList, startCountList);
                    //Toast.makeText(getActivity(), "" + baseCountList + " " + startCountList, Toast.LENGTH_LONG).show();
                } else {
                    listAllPieces(v, baseCountList, startCountList);
                    //Toast.makeText(getActivity(), "" + baseCountList + " " + startCountList, Toast.LENGTH_LONG).show();
                    btns.setVisibility(View.GONE);
                }

            }
        });

    }

    private void spinnerDataPopUp() {

        _spinnerList.setContentView(R.layout.custom_edit_spinners_via_list);
        _spinnerList.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        _spinnerList.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        _spinnerList.show();

        closeSpinnerEdit = (ImageButton) _spinnerList.findViewById(R.id.btnCloseSpinnerEdit);
        closeSpinnerEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _spinnerList.dismiss();
            }
        });

        _allSpinners = (Spinner) _spinnerList.findViewById(R.id.spinnerChooseList);
        ArrayAdapter allSpinner = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        allSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        allSpinner.addAll(spinnerTitles);
        _allSpinners.setAdapter(allSpinner);

        delSpinner = (Spinner) _spinnerList.findViewById(R.id.spinnerList);
        final ArrayAdapter selectedSpinner = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        selectedSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        _allSpinners.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spinnerChoiceHolder = _allSpinners.getSelectedItem().toString();

                switch (spinnerChoiceHolder) {
                    case "Kategoriler":
                        selectedSpinner.clear();
                        _categories = new ArrayList<String>();
                        categoryActions.getAllCategoriesList(getActivity(), TAG, _categories, delSpinner, selectedSpinner);
                        delSpinner.setAdapter(selectedSpinner);

                        break;
                    case "Markalar":
                        selectedSpinner.clear();
                        _brands = new ArrayList<String>();
                        brandActions.getAllBrandsList(TAG, getActivity(), _brands, delSpinner, selectedSpinner);
                        delSpinner.setAdapter(selectedSpinner);

                        break;
                    case "Modeller":
                        selectedSpinner.clear();
                        _models = new ArrayList<String>();
                        modelActions.getAllModelsList(getActivity(), TAG, _models, delSpinner, selectedSpinner);
                        delSpinner.setAdapter(selectedSpinner);

                        break;
                    case "Versiyonlar":
                        selectedSpinner.clear();
                        _versions = new ArrayList<String>();
                        versionActions.getAllVersionsList(TAG, getActivity(), _versions, delSpinner, selectedSpinner);
                        delSpinner.setAdapter(selectedSpinner);

                        break;
                    case "Modüller":
                        selectedSpinner.clear();
                        _modules = new ArrayList<String>();
                        moduleActions.getAllModulesList(TAG, getActivity(), _modules, delSpinner, selectedSpinner);
                        delSpinner.setAdapter(selectedSpinner);
                        break;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

}
