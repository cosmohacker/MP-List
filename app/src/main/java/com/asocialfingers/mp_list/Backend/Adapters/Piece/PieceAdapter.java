package com.asocialfingers.mp_list.Backend.Adapters.Piece;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.asocialfingers.mp_list.Backend.Models.Piece.PieceDetails;
import com.asocialfingers.mp_list.Backend.Models.Users.UsersDetails;
import com.asocialfingers.mp_list.R;

import java.util.ArrayList;

public class PieceAdapter extends RecyclerView.Adapter<PieceAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<PieceDetails> pieceDetailsArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView name, price;

        public ViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.txtUsername);
            price = (TextView) view.findViewById(R.id.txtPermission);
        }
    }

    public PieceAdapter(Context mContext, ArrayList<PieceDetails> pieceDetailsArrayList) {
        this.mContext = mContext;
        this.pieceDetailsArrayList = pieceDetailsArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_users_list_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        PieceDetails commentDetails = pieceDetailsArrayList.get(position);
        holder.name.setText(commentDetails.getPiece_name());
        holder.price.setText(commentDetails.getPrice() + " " + commentDetails.getCurrency());
    }

    @Override
    public int getItemCount() {
        return pieceDetailsArrayList.size();
    }

}