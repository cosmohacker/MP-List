package com.asocialfingers.mp_list.Backend.Adapters.Piece;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Build;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.OnBackPressedCallback;
import androidx.activity.OnBackPressedDispatcher;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.ListFragment;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.asocialfingers.mp_list.Backend.Connection.AppController;
import com.asocialfingers.mp_list.Backend.Connection.ConnectionLinks;
import com.asocialfingers.mp_list.Backend.Database.DBHelper;
import com.asocialfingers.mp_list.Backend.Database.UserDao;
import com.asocialfingers.mp_list.Backend.Models.Piece.PieceDetails;
import com.asocialfingers.mp_list.Frontend.Main.MainActivity;
import com.asocialfingers.mp_list.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Random;

public class PieceListViewAdapter extends ArrayAdapter<PieceDetails> {

    private EditText _pieceName, _count, _junkCount, _description, _price, _note, _userId, _pieceId, _createdAt;
    private Spinner _brand, _model, _module, _category, _version, _currency;
    private static String TAG = PieceListViewAdapter.class.getSimpleName();
    private Button _gallery, _camera, _save, _delete, _close;
    String[] _currencies = {"$", "€", "£", "TL",};
    private ImageButton _image;
    private String imageHolder;
    private Dialog _details;

    public PieceListViewAdapter(Context context, ArrayList<PieceDetails> pieces) {
        super(context, 0, pieces);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        final PieceDetails pieceDetails = getItem(position);
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.custom_piece_list_grid_layout, parent, false);
        }

        _details = new Dialog(getContext());

        Random random = new Random();
        int color = Color.argb(255, random.nextInt(256), random.nextInt(256), random.nextInt(256));

        //region Utils

        final TextView _name, _price;
        ImageButton _image;
        LinearLayout _item;
        Button _detail;

        _item = (LinearLayout) convertView.findViewById(R.id.relativeCustomGridItem);

        _name = (TextView) convertView.findViewById(R.id.txtPieceName);
        _price = (TextView) convertView.findViewById(R.id.txtPiecePrice);

        _image = (ImageButton) convertView.findViewById(R.id.pieceImage);

        _detail = (Button) convertView.findViewById(R.id.btnDetails);

        //endregion

        //region Drawable
        GradientDrawable gd = new GradientDrawable();
        gd.setCornerRadius(40);
        gd.setColor(color);
        //endregion

        //_name.setText(pieceDetails.getPiece_name());
        _name.setText(pieceDetails.getPiece_name());

        _price.setText(pieceDetails.getPrice() + " " + pieceDetails.getCurrency());

        byte[] encodeByte = Base64.decode(pieceDetails.getImage(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
        _image.setImageBitmap(bitmap);

        _detail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(getContext(), _name.getText().toString(), Toast.LENGTH_SHORT).show();
                String pieceNameHolder = _name.getText().toString();
                getEditPieceDetailPopUp(pieceNameHolder);
            }
        });

        return convertView;
    }

    private void getEditPieceDetailPopUp(String pieceName) {

        _details.setContentView(R.layout.custom_edit_piece_via_list);
        _details.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        _details.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        _details.show();
        fetchPieceInformation(pieceName);

    }

    private void fetchPieceInformation(String pieceName) {

        popUpUtils();

        String endPoint = ConnectionLinks.getPiecesFromName.replace("_PIECE_NAME_", pieceName);
        Log.e(TAG, "endPoint: " + endPoint);

        StringRequest strReq = new StringRequest(Request.Method.GET, endPoint, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d(TAG, " Pieces: " + response);

                try {
                    JSONObject obj = new JSONObject(response);

                    if (obj.getBoolean("error") == false) {
                        JSONArray piecesArray = obj.getJSONArray("pieces");
                        for (int i = 0; i <= 1; i++) {

                            JSONObject piecesOBJ = (JSONObject) piecesArray.get(i);

                            _pieceId.setText(piecesOBJ.getString("Piece_Id"));
                            _userId.setText(piecesOBJ.getString("User_Id"));
                            //(piecesOBJ.getString("Brand_Id"));
                            //(piecesOBJ.getString("Category_Id"));
                            //(piecesOBJ.getString("Model_Id"));
                            //(piecesOBJ.getString("Version_Id"));
                            //(piecesOBJ.getString("Module_Id"));
                            _price.setText(piecesOBJ.getString("Price"));
                            ArrayAdapter priceAdapter = new ArrayAdapter(getContext(), android.R.layout.simple_spinner_item);
                            priceAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                            priceAdapter.add(piecesOBJ.getString("Price_Type"));
                            priceAdapter.addAll(_currencies);
                            _currency.setAdapter(priceAdapter);
                            _count.setText(piecesOBJ.getString("Count"));
                            _pieceName.setText(piecesOBJ.getString("Piece_Name"));
                            _junkCount.setText(piecesOBJ.getString("Junk_Count"));
                            _description.setText(piecesOBJ.getString("Description"));
                            _note.setText(piecesOBJ.getString("Note"));
                            _createdAt.setText(piecesOBJ.getString("Created_At"));

                            imageHolder = piecesOBJ.getString("Image");
                            byte[] encodeByte = Base64.decode(imageHolder, Base64.DEFAULT);
                            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
                            _image.setImageBitmap(bitmap);

                            _close.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    _details.dismiss();
                                }
                            });

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
        AppController.getInstance().addToRequestQueue(strReq);

    }

    @Nullable
    @Override
    public PieceDetails getItem(int position) {
        return super.getItem(position);
    }

    @Override
    public int getPosition(@Nullable PieceDetails item) {
        return super.getPosition(item);
    }

    private void popUpUtils() {

        _junkCount = (EditText) _details.findViewById(R.id.txtPieceJunkCount);
        _description = (EditText) _details.findViewById(R.id.txtDescription);
        _pieceName = (EditText) _details.findViewById(R.id.txtPieceName);
        _createdAt = (EditText) _details.findViewById(R.id.txtCreatedAt);
        _count = (EditText) _details.findViewById(R.id.txtPieceCount);
        _price = (EditText) _details.findViewById(R.id.txtPiecePrice);
        _note = (EditText) _details.findViewById(R.id.txtPieceNote);
        _pieceId = (EditText) _details.findViewById(R.id.txtPieceId);
        _userId = (EditText) _details.findViewById(R.id.txtUserId);

        _close = (Button) _details.findViewById(R.id.btnCloseDetailBar);
        _image = (ImageButton) _details.findViewById(R.id.btnPieceImage);

        _gallery = (Button) _details.findViewById(R.id.btnOpenGallery);
        _camera = (Button) _details.findViewById(R.id.btnOpenCamera);
        _save = (Button) _details.findViewById(R.id.btnSavePieceDetails);
        _delete = (Button) _details.findViewById(R.id.btnDeletePiece);

        _currency = (Spinner) _details.findViewById(R.id.spinnerPiecePriceType);
        _category = (Spinner) _details.findViewById(R.id.spinnerPieceCategory);
        _version = (Spinner) _details.findViewById(R.id.spinnerPieceVersion);
        _module = (Spinner) _details.findViewById(R.id.spinnerPieceModule);
        _brand = (Spinner) _details.findViewById(R.id.spinnerPieceBrand);
        _model = (Spinner) _details.findViewById(R.id.spinnerPieceModel);


    }

}
