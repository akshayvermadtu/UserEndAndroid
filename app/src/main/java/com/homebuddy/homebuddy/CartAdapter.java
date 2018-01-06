package com.homebuddy.homebuddy;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private List<CartModel> activityList;
    private Context mContext;

    CartAdapter(List<CartModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemName , itemQuantity ;

        ViewHolder(View view) {
            super(view);
            itemName = (TextView) view.findViewById(R.id.item_name);
            itemQuantity = (TextView) view.findViewById(R.id.item_amount);
        }
    }

    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_model, parent, false);
        return new CartAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder holder, int position) {
        CartModel activityListItems = activityList.get(position);
        holder.itemName.setText(activityListItems.getItemName());
        holder.itemQuantity.setText(activityListItems.getItemQuantity());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}

