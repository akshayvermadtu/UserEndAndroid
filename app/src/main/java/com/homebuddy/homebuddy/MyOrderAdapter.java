package com.homebuddy.homebuddy;


import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

class MyOrderAdapter extends RecyclerView.Adapter<MyOrderAdapter.ViewHolder>{
    private List<MyOrderModel> activityList;
    private Context mContext;

    MyOrderAdapter(List<MyOrderModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView itemList , bill , status , payment , date ;

        ViewHolder(View view) {
            super(view);
            itemList = (TextView) view.findViewById(R.id.item_list);
            bill = (TextView) view.findViewById(R.id.bill);
            status = (TextView) view.findViewById(R.id.status);
            payment = (TextView) view.findViewById(R.id.payment);
            date = (TextView) view.findViewById(R.id.order_date);
        }
    }

    @Override
    public MyOrderAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_order_model, parent, false);
        return new MyOrderAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyOrderAdapter.ViewHolder holder, int position) {
        MyOrderModel activityListItems = activityList.get(position);
        holder.itemList.setText(activityListItems.getItemList());
        holder.bill.setText(activityListItems.getBill());
        holder.status.setText(activityListItems.getStatus());
        holder.payment.setText(activityListItems.getPayment());
        holder.date.setText(activityListItems.getDate());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
