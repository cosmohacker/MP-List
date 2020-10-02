package com.asocialfingers.mp_list.Frontend.Tabs.Add;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

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
import com.asocialfingers.mp_list.Backend.Actions.PieceActions;
import com.asocialfingers.mp_list.Backend.Actions.VersionActions;
import com.asocialfingers.mp_list.Backend.Adapters.Spinner.GetIdFromSpinner;
import com.asocialfingers.mp_list.Backend.Connection.AppController;
import com.asocialfingers.mp_list.Backend.Connection.ConnectionLinks;
import com.asocialfingers.mp_list.Backend.Models.Model.ModelDetails;
import com.asocialfingers.mp_list.Backend.Models.Module.ModuleDetails;
import com.asocialfingers.mp_list.Backend.Models.Version.VersionDetails;
import com.asocialfingers.mp_list.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_CANCELED;
import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {

    private Button mainBrand, mainCategory, mainModel, mainVersion, mainModule, mainPiece;
    private static String TAG = AddFragment.class.getSimpleName();
    private String cookieHolder = ConnectionLinks.CookieHolder;
    private String userIdHolder = "1";
    private TextView _title;

    //region Category
    private CategoryActions categoryActions = new CategoryActions();
    private RelativeLayout relativeCategory;
    private ArrayList<String> _category_;
    private ImageButton _insertCategory;
    private SeekBar _prgCategory;
    private EditText _category;
    //endregion

    //region Version
    private String versionBrandNameHolder, versionModuleNameHolder, versionModelNameHolder, versionCategoryNameHolder;
    private String versionBrandIdHolder, versionModuleIdHolder, versionModelIdHolder, versionCategoryIdHolder;
    private Spinner spinnerVersionBrand, spinnerVersionModel, spinnerVersionModule, spinnerVersionCategory;
    private VersionActions versionActions = new VersionActions();
    private RelativeLayout relativeVersion;
    private ArrayList<String> _versions_;
    private ImageButton btnInsertVersion;
    private EditText txtVersionName;
    private SeekBar _prgVersion;
    //endregion

    //region Module
    private ModuleActions moduleActions = new ModuleActions();
    private RelativeLayout relativeModule;
    private ArrayList<String> _modules_;
    private ImageButton _insertModule;
    private SeekBar _prgModule;
    private EditText _module;
    //endregion

    //region Model
    private String modelVersionNameHolder, modelBrandNameHolder, modelCategoryNameHolder;
    private String modelBrandIdHolder, modelCategoryIdHolder, modelVersionIdHolder;
    private Spinner spinnerModelVersion, spinnerModelBrand, spinnerModelCategory;
    private ModelActions modelActions = new ModelActions();
    private ArrayList<String> _models_, _version_id_;
    private RelativeLayout relativeModel;
    private ImageButton _insertModel;
    private String sendNameForId;
    private SeekBar _prgModel;
    private EditText _model;
    //endregion

    //region Brand
    private String brandModelHolder, brandCategoryHolder, brandVersionHolder;
    private BrandActions brandActions = new BrandActions();
    private RelativeLayout relativeBrand;
    private ArrayList<String> _brands_;
    private ImageButton _insertBrand;
    private SeekBar _prgBrand;
    private EditText _brand;
    //endregion

    //region Piece
    private String pieceCountHolder, pieceNameHolder, pieceJunkCountHolder, pieceDescriptionHolder, piecePriceHolder, pieceNoteHolder, pieceBrandHolder, pieceModelHolder, pieceVersionHolder,
            pieceModuleHolder, pieceCategoryHolder, piecePriceTypeHolder, pieceBrandIdHolder, pieceModelIdHolder, pieceVersionIdHolder, pieceModuleIdHolder, pieceCategoryIdHolder, piecePriceTypeIdHolder;
    private Spinner spinnerPieceBrand, spinnerPieceModel, spinnerPieceVersion, spinnerPieceModule, spinnerPieceCategory, spinnerPiecePriceType;
    private EditText pieceCount, pieceName, pieceJunkCount, pieceDescription, piecePrice, pieceNote;
    private int GALLERY_REQUEST_CODE = 1, CAMERA_REQUEST_CODE = 2;
    private PieceActions pieceActions = new PieceActions();
    String[] _currency = {"$", "€", "£", "TL",};
    private Button _addPiece, _gallery, _camera;
    private RelativeLayout relativePiece;
    private ImageButton pieceImage;
    private Bitmap bmp;
    private File file;
    private Uri uri;

    //endregion

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add, container, false);

        methodCleaner(root);

        return root;
    }

    private void methodCleaner(View root) {

        utils(root);

        designMethods();

        allProgressBarEvents();

        mainButtonEvents();

        brandEvents();
        categoryEvents();
        moduleEvents();
        versionEvents();
        modelEvents();
        pieceEvents();

        imageViewClickEvents(root);

    }

    private void utils(View root) {

        //region Main Utilities
        mainCategory = (Button) root.findViewById(R.id.btnMainCategory);
        mainVersion = (Button) root.findViewById(R.id.btnMainVersion);
        mainModule = (Button) root.findViewById(R.id.btnMainModule);
        mainBrand = (Button) root.findViewById(R.id.btnMainBrand);
        mainModel = (Button) root.findViewById(R.id.btnMainModel);
        mainPiece = (Button) root.findViewById(R.id.btnMainPiece);

        _title = (TextView) root.findViewById(R.id.txtEventText);
        //endregion

        //region Add Module Utilities
        relativeModule = (RelativeLayout) root.findViewById(R.id.relativeModuleSection);

        _insertModule = (ImageButton) root.findViewById(R.id.btnInsertModule);

        _module = (EditText) root.findViewById(R.id.txtModuleName);

        _prgModule = (SeekBar) root.findViewById(R.id.prgModule);
        //endregion

        //region Add Brand Utilities
        relativeBrand = (RelativeLayout) root.findViewById(R.id.relativeBrandSection);

        _insertBrand = (ImageButton) root.findViewById(R.id.btnInsertBrand);

        _brand = (EditText) root.findViewById(R.id.txtBrandName);

        _prgBrand = (SeekBar) root.findViewById(R.id.prgBrand);
        //endregion

        //region Add Category Utilities
        relativeCategory = (RelativeLayout) root.findViewById(R.id.relativeCategorySection);

        _insertCategory = (ImageButton) root.findViewById(R.id.btnInsertCategory);

        _category = (EditText) root.findViewById(R.id.txtCategoryName);

        _prgCategory = (SeekBar) root.findViewById(R.id.prgCategory);
        //endregion

        //region Add Model Utilities
        relativeModel = (RelativeLayout) root.findViewById(R.id.relativeModelSection);

        spinnerModelCategory = (Spinner) root.findViewById(R.id.spinnerModelCategory);
        spinnerModelVersion = (Spinner) root.findViewById(R.id.spinnerModelVersion);
        spinnerModelBrand = (Spinner) root.findViewById(R.id.spinnerModelBrand);

        _insertModel = (ImageButton) root.findViewById(R.id.btnInsertModel);

        _model = (EditText) root.findViewById(R.id.txtModelName);

        _prgModel = (SeekBar) root.findViewById(R.id.prgModel);

        //endregion

        //region Add Piece Utilities

        relativePiece = (RelativeLayout) root.findViewById(R.id.relativePieceSection);

        pieceImage = (ImageButton) root.findViewById(R.id.btnPieceImage);

        spinnerPiecePriceType = (Spinner) root.findViewById(R.id.spinnerPiecePriceType);
        spinnerPieceCategory = (Spinner) root.findViewById(R.id.spinnerPieceCategory);
        spinnerPieceVersion = (Spinner) root.findViewById(R.id.spinnerPieceVersion);
        spinnerPieceModule = (Spinner) root.findViewById(R.id.spinnerPieceModule);
        spinnerPieceBrand = (Spinner) root.findViewById(R.id.spinnerPieceBrand);
        spinnerPieceModel = (Spinner) root.findViewById(R.id.spinnerPieceModel);

        _addPiece = (Button) root.findViewById(R.id.btnInsertPiece);
        _gallery = (Button) root.findViewById(R.id.btnOpenGallery);
        _camera = (Button) root.findViewById(R.id.btnOpenCamera);

        pieceJunkCount = (EditText) root.findViewById(R.id.txtPieceJunkCount);
        pieceDescription = (EditText) root.findViewById(R.id.txtDescription);
        piecePrice = (EditText) root.findViewById(R.id.txtPiecePrice);
        pieceCount = (EditText) root.findViewById(R.id.txtPieceCount);
        pieceNote = (EditText) root.findViewById(R.id.txtPieceNote);
        pieceName = (EditText) root.findViewById(R.id.txtPieceName);

        //region Price Type Adapter
        ArrayAdapter priceAdapter = new ArrayAdapter(getActivity(), android.R.layout.simple_spinner_item);
        priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        priceAdapter.addAll(_currency);
        spinnerPiecePriceType.setAdapter(priceAdapter);
        //endregion

        //endregion

        //region Add Version Utilities
        relativeVersion = (RelativeLayout) root.findViewById(R.id.relativeVersionSection);

        spinnerVersionCategory = (Spinner) root.findViewById(R.id.spinnerVersionCategory);
        spinnerVersionModule = (Spinner) root.findViewById(R.id.spinnerVersionModule);
        spinnerVersionBrand = (Spinner) root.findViewById(R.id.spinnerVersionBrand);
        spinnerVersionModel = (Spinner) root.findViewById(R.id.spinnerVersionModel);

        btnInsertVersion = (ImageButton) root.findViewById(R.id.btnInsertVersion);

        txtVersionName = (EditText) root.findViewById(R.id.txtVersionName);

        _prgVersion = (SeekBar) root.findViewById(R.id.prgVersion);

        //endregion

    }

    //region Fragment Events

    private void brandEvents() {

        _brands_ = new ArrayList<String>();

        brandActions.getAllBrands(TAG, getActivity(), _brands_, spinnerModelBrand, spinnerPieceBrand, spinnerVersionBrand);

        _insertBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_brand.getText().length() < 1) {
                    _brand.setError("Lütfen marka adı belirtin");
                } else
                    brandActions.addBrand(getActivity(), TAG, _brand, userIdHolder);
            }
        });
    }

    private void categoryEvents() {

        _category_ = new ArrayList<String>();
        categoryActions.getAllCategories(getActivity(), TAG, _category_, spinnerModelCategory, spinnerPieceCategory, spinnerVersionCategory);

        _insertCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_category.getText().length() < 1) {
                    _category.setError("Lütfen kategori adı belirtin");
                } else
                    categoryActions.addCategory(_category, TAG, getActivity(), userIdHolder);
            }
        });

    }

    private void moduleEvents() {

        _modules_ = new ArrayList<String>();

        moduleActions.getAllModules(TAG, getActivity(), _modules_, spinnerPieceModule, spinnerVersionModule);

        _insertModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_module.getText().length() < 1) {
                    _module.setError("Lütfen modül adı belirtin");
                } else
                    moduleActions.addModule(getActivity(), _module, TAG, userIdHolder);
            }
        });

    }

    //region Model

    private void getVersionIdFromModel() {

        String endPoint = ConnectionLinks.getVersionId.replace("_VERSION_ID_", modelVersionNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Versions: " + response);

                try {

                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("versions");

                    modelVersionIdHolder = userDetails.getString("Version_Id");

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

    private void getBrandIdFromModel() {

        String endPoint = ConnectionLinks.getBrandId.replace("_BRAND_ID_", modelBrandNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Brands: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("brands");

                    modelBrandIdHolder = userDetails.getString("Brand_Id");
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

    private void getCategoryIdFromModel() {

        String endPoint = ConnectionLinks.getCategoryId.replace("_CATEGORY_ID_", modelCategoryNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Brands: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("categories");

                    modelCategoryIdHolder = userDetails.getString("Category_Id");
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

    private void modelEvents() {

        _models_ = new ArrayList<String>();

        modelActions.getAllModels(getActivity(), TAG, _models_, spinnerPieceModel, spinnerVersionModel);

        _insertModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_model.getText().length() < 1) {
                    _model.setError("Lütfen model adı belirtin");
                } else
                    modelActions.addModel(TAG, _model, getActivity(), userIdHolder, modelVersionIdHolder, modelCategoryIdHolder, modelBrandIdHolder);
            }
        });

        spinnerModelVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelVersionNameHolder = spinnerModelVersion.getSelectedItem().toString();
                getVersionIdFromModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerModelBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelBrandNameHolder = spinnerModelBrand.getSelectedItem().toString();
                getBrandIdFromModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerModelCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                modelCategoryNameHolder = spinnerModelCategory.getSelectedItem().toString();
                getCategoryIdFromModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    //endregion

    //region Version
    private void getBrandIdFromVersion() {

        String endPoint = ConnectionLinks.getBrandId.replace("_BRAND_ID_", versionBrandNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Brands: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("brands");

                    versionBrandIdHolder = userDetails.getString("Brand_Id");
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

    private void getCategoryIdFromVersion() {

        String endPoint = ConnectionLinks.getCategoryId.replace("_CATEGORY_ID_", versionCategoryNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Categories: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("categories");

                    versionCategoryIdHolder = userDetails.getString("Category_Id");
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

    private void getModelIdFromVersion() {

        String endPoint = ConnectionLinks.getModelId.replace("_MODEL_ID_", versionModelNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Versions: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("models");

                    versionModelIdHolder = userDetails.getString("Model_Id");
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

    private void getModuleIdFromVersion() {

        String endPoint = ConnectionLinks.getModuleId.replace("_MODULE_ID_", versionModuleNameHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Modules: " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("modules");

                    versionModuleIdHolder = userDetails.getString("Module_Id");
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

    private void versionEvents() {

        _versions_ = new ArrayList<String>();
        versionActions.getAllVersions(TAG, getActivity(), _versions_, spinnerModelVersion, spinnerPieceVersion);

        btnInsertVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (txtVersionName.getText().length() < 1) {
                    txtVersionName.setError("Lütfen versiyon adı belirtin");
                } else
                    versionActions.addVersion(txtVersionName, TAG, getActivity(), userIdHolder, versionBrandIdHolder, versionCategoryIdHolder, versionModelIdHolder, versionModuleIdHolder);
            }
        });

        spinnerVersionBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                versionBrandNameHolder = spinnerVersionBrand.getSelectedItem().toString();
                getBrandIdFromVersion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVersionCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                versionCategoryNameHolder = spinnerVersionCategory.getSelectedItem().toString();
                getCategoryIdFromVersion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVersionModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                versionModelNameHolder = spinnerVersionModel.getSelectedItem().toString();
                getModelIdFromVersion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerVersionModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                versionModuleNameHolder = spinnerVersionModule.getSelectedItem().toString();
                getModuleIdFromVersion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }
    //endregion

    //region Piece
    private void getIdFromPieceVersion() {

        String endPoint = ConnectionLinks.getVersionId.replace("_VERSION_ID_", pieceVersionHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Versions : " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("versions");

                    pieceVersionIdHolder = userDetails.getString("Version_Id");
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

    private void getIdFromPieceModule() {

        String endPoint = ConnectionLinks.getModuleId.replace("_MODULE_ID_", pieceModuleHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Modules : " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("modules");

                    pieceModuleIdHolder = userDetails.getString("Module_Id");
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

    private void getIdFromPieceModel() {

        String endPoint = ConnectionLinks.getModelId.replace("_MODEL_ID_", pieceModelHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Models : " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("models");

                    pieceModelIdHolder = userDetails.getString("Model_Id");
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

    private void getIdFromPieceCategory() {

        String endPoint = ConnectionLinks.getCategoryId.replace("_CATEGORY_ID_", pieceCategoryHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Categories : " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("categories");

                    pieceCategoryIdHolder = userDetails.getString("Category_Id");
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

    private void getIdFromPieceBrand() {

        String endPoint = ConnectionLinks.getBrandId.replace("_BRAND_ID_", pieceBrandHolder);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Brands : " + response);

                try {
                    JSONObject parentObject = new JSONObject(response);
                    JSONObject userDetails = parentObject.getJSONObject("brands");

                    pieceBrandIdHolder = userDetails.getString("Brand_Id");
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

    private void pieceEvents() {

        _addPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPiece();
                Toast.makeText(getActivity(),
                        "user ıd: " + userIdHolder +
                                " brand ıd: " + pieceBrandIdHolder +
                                " category ıd :" + pieceCategoryIdHolder +
                                " model ıd: " + pieceModelIdHolder +
                                " version ıd: " + pieceVersionIdHolder +
                                " module ıd: " + pieceModuleIdHolder +
                                " price: " + piecePriceHolder +
                                " currency: " + piecePriceTypeHolder +
                                " count: " + pieceCountHolder +
                                " name: " + pieceNameHolder +
                                "junk count: " + pieceJunkCountHolder +
                                " description: " + pieceDescriptionHolder +
                                " note: " + pieceNoteHolder, Toast.LENGTH_SHORT).show();
            }
        });

        spinnerPieceVersion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceVersionHolder = spinnerPieceVersion.getSelectedItem().toString();
                getIdFromPieceVersion();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPieceBrand.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceBrandHolder = spinnerPieceBrand.getSelectedItem().toString();
                getIdFromPieceBrand();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPieceCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceCategoryHolder = spinnerPieceCategory.getSelectedItem().toString();
                getIdFromPieceCategory();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPieceModel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceModelHolder = spinnerPieceModel.getSelectedItem().toString();
                getIdFromPieceModel();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPieceModule.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pieceModuleHolder = spinnerPieceModule.getSelectedItem().toString();
                getIdFromPieceModule();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinnerPiecePriceType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                piecePriceTypeHolder = spinnerPiecePriceType.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void getPieceUtilsValues() {

        pieceDescriptionHolder = pieceDescription.getText().toString();
        pieceJunkCountHolder = pieceJunkCount.getText().toString();
        piecePriceHolder = piecePrice.getText().toString();
        pieceCountHolder = pieceCount.getText().toString();
        pieceNameHolder = pieceName.getText().toString();
        pieceNoteHolder = pieceNote.getText().toString();

    }

    private void addPiece() {

        getPieceUtilsValues();

        StringRequest strReq = new StringRequest(Request.Method.POST, ConnectionLinks.insertPiece, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.d(TAG, "Response: " + response.toString());
                try {
                    JSONObject jObj = new JSONObject(response);
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                    } else {
                        Toast.makeText(getActivity(), "Hata oluştu! Daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getActivity(), "Server'da problem oluştu. Daha sonra tekrar deneyin.", Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                Bitmap bitmap = ((BitmapDrawable) pieceImage.getDrawable()).getBitmap();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] imageBytes = baos.toByteArray();
                String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);

                params.put("User_Id", userIdHolder);
                params.put("Brand_Id", pieceBrandIdHolder);
                params.put("Category_Id", pieceCategoryIdHolder);
                params.put("Model_Id", pieceModelIdHolder);
                params.put("Version_Id", pieceVersionIdHolder);
                params.put("Module_Id", pieceModuleIdHolder);
                params.put("Price", piecePriceHolder);
                params.put("Price_Type", piecePriceTypeHolder);
                params.put("Count", pieceCountHolder);
                params.put("Piece_Name", pieceNameHolder);
                params.put("Junk_Count", pieceJunkCountHolder);
                params.put("Description", pieceDescriptionHolder);
                params.put("Note", pieceNoteHolder);
                params.put("Image", imageString);

                return params;
            }

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

    //endregion

    //region Main Events

    private void mainButtonEvents() {

        mainVersion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainVersionSwitch();
            }
        });

        mainPiece.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainPieceSwitch();
            }
        });

        mainModule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainModuleSwitch();
            }
        });

        mainCategory.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainCategorySwitch();
            }
        });

        mainBrand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainBrandSwitch();
            }
        });

        mainModel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mainModelSwitch();
            }
        });

    }

    private void mainBrandSwitch() {
        _title.setText("MARKA");

        relativeBrand.setVisibility(View.VISIBLE);
        relativePiece.setVisibility(View.GONE);
        relativeVersion.setVisibility(View.GONE);
        relativeModule.setVisibility(View.GONE);
        relativeModel.setVisibility(View.GONE);
        relativeCategory.setVisibility(View.GONE);
    }

    private void mainCategorySwitch() {
        _title.setText("KATEGORİ");

        relativeCategory.setVisibility(View.VISIBLE);
        relativeBrand.setVisibility(View.GONE);
        relativePiece.setVisibility(View.GONE);
        relativeVersion.setVisibility(View.GONE);
        relativeModule.setVisibility(View.GONE);
        relativeModel.setVisibility(View.GONE);
    }

    private void mainModelSwitch() {
        _title.setText("MODEL");

        relativeModel.setVisibility(View.VISIBLE);
        relativeBrand.setVisibility(View.GONE);
        relativePiece.setVisibility(View.GONE);
        relativeVersion.setVisibility(View.GONE);
        relativeModule.setVisibility(View.GONE);
        relativeCategory.setVisibility(View.GONE);
    }

    private void mainVersionSwitch() {
        _title.setText("VERSİYON");

        relativeVersion.setVisibility(View.VISIBLE);
        relativeModel.setVisibility(View.GONE);
        relativeBrand.setVisibility(View.GONE);
        relativePiece.setVisibility(View.GONE);
        relativeModule.setVisibility(View.GONE);
        relativeCategory.setVisibility(View.GONE);
    }

    private void mainModuleSwitch() {
        _title.setText("MODÜL");

        relativeModule.setVisibility(View.VISIBLE);
        relativeModel.setVisibility(View.GONE);
        relativeBrand.setVisibility(View.GONE);
        relativePiece.setVisibility(View.GONE);
        relativeVersion.setVisibility(View.GONE);
        relativeCategory.setVisibility(View.GONE);
    }

    private void mainPieceSwitch() {
        _title.setText("PARÇA");

        relativePiece.setVisibility(View.VISIBLE);
        relativeModule.setVisibility(View.GONE);
        relativeModel.setVisibility(View.GONE);
        relativeBrand.setVisibility(View.GONE);
        relativeVersion.setVisibility(View.GONE);
        relativeCategory.setVisibility(View.GONE);

    }

    //endregion

    //region Design

    private void designMethods() {

        radiusEvents();

    }

    private void radiusEvents() {

        GradientDrawable pieceButtonGD = new GradientDrawable();
        pieceButtonGD.setColor(getResources().getColor(R.color.login_page_button_text_color_dark));
        pieceButtonGD.setCornerRadius(10);

        pieceImage.setBackground(pieceButtonGD);
    }

    private void allProgressBarEvents() {

        _prgBrand.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    _insertBrand.setVisibility(View.VISIBLE);
                } else {
                    _insertBrand.setVisibility(View.GONE);
                }

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        _prgCategory.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    _insertCategory.setVisibility(View.VISIBLE);
                } else {
                    _insertCategory.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        _prgModule.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    _insertModule.setVisibility(View.VISIBLE);
                } else {
                    _insertModule.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        _prgModel.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    _insertModel.setVisibility(View.VISIBLE);
                } else {
                    _insertModel.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        _prgVersion.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (progress == 100) {
                    btnInsertVersion.setVisibility(View.VISIBLE);
                } else {
                    btnInsertVersion.setVisibility(View.GONE);
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

    }

    //endregion

    //region Image

    private void imageViewClickEvents(View rootView) {
        pieceImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                imageDialog();
            }
        });
    }

    private void imageDialog() {
        @SuppressLint("ResourceType") AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Please Select Action");
        String[] _actions = {
                "Camera",
                "Gallery"
        };
        builder.setItems(_actions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which) {
                    case 0:
                        pickFromCamera();
                        break;
                    case 1:
                        pickFromGallery();
                        break;
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_CANCELED) {
            if (requestCode == 0 && resultCode == RESULT_OK)
                cropImage();
            if (requestCode == GALLERY_REQUEST_CODE) {
                if (data != null) {
                    uri = data.getData();
                    cropImage();
                }
            } else if (requestCode == CAMERA_REQUEST_CODE) {
                if (data != null) {
                    Bundle bundle = data.getExtras();
                    bmp = (Bitmap) bundle.getParcelable("data");
                    pieceImage.setImageBitmap(bmp);
                }
            }
        }
    }

    private void cropImage() {
        try {
            Intent i = new Intent("com.android.camera.action.CROP");
            i.setDataAndType(uri, "image/*");

            i.putExtra("crop", "true");
            i.putExtra("outputX", 400);
            i.putExtra("outputY", 400);
            i.putExtra("aspectX", 3);
            i.putExtra("aspectY", 3);
            i.putExtra("scaleUpIfNeeded", true);
            i.putExtra("return-data", true);

            startActivityForResult(i, CAMERA_REQUEST_CODE);

        } catch (ActivityNotFoundException ex) {
        }
    }

    private void pickFromGallery() {
        Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(i, GALLERY_REQUEST_CODE);
    }

    private void pickFromCamera() {
        Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(), "file" + String.valueOf(System.currentTimeMillis()) + ".jpg");
        uri = Uri.fromFile(file);
        i.putExtra(MediaStore.EXTRA_OUTPUT, uri);
        i.putExtra("return-data", true);
        startActivityForResult(i, 0);
    }

    private String getPath(Uri uri) {
        Cursor cursor = getActivity().getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        String document_id = cursor.getString(0);
        document_id = document_id.substring(document_id.lastIndexOf(":") + 1);
        cursor.close();

        cursor = getActivity().getContentResolver().query(
                android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, MediaStore.Images.Media._ID + " = ? ", new String[]{document_id}, null);
        cursor.moveToFirst();
        String path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
        cursor.close();

        return path;
    }

    //endregion

}
