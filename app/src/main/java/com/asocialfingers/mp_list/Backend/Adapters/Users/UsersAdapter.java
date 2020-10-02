package com.asocialfingers.mp_list.Backend.Adapters.Users;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.asocialfingers.mp_list.Backend.Models.Users.UsersDetails;
import com.asocialfingers.mp_list.R;

import java.util.ArrayList;

public class UsersAdapter extends RecyclerView.Adapter<UsersAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<UsersDetails> usersDetailsArrayList;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView username, permission;

        public ViewHolder(View view) {
            super(view);
            username = (TextView) view.findViewById(R.id.txtUsername);
            permission = (TextView) view.findViewById(R.id.txtPermission);
        }
    }

    public UsersAdapter(Context mContext, ArrayList<UsersDetails> usersDetailsArrayList) {
        this.mContext = mContext;
        this.usersDetailsArrayList = usersDetailsArrayList;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.custom_users_list_view, parent, false);

        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        UsersDetails commentDetails = usersDetailsArrayList.get(position);
        holder.username.setText(commentDetails.getUsername());
        holder.permission.setText(commentDetails.getPermission());
    }

    @Override
    public int getItemCount() {
        return usersDetailsArrayList.size();
    }

}