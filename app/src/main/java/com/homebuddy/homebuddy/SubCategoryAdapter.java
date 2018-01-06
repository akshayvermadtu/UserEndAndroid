package com.homebuddy.homebuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

class SubCategoryAdapter extends RecyclerView.Adapter<SubCategoryAdapter.ViewHolder>{
    private List<SubCategoryModel> activityList;
    private Context mContext;
    private SubCategoryList subCategoryList;

    SubCategoryAdapter(List<SubCategoryModel> activityList, Context context , SubCategoryList subCategoryList) {
        this.activityList = activityList;
        mContext = context;
        this.subCategoryList = subCategoryList;
    }

class ViewHolder extends RecyclerView.ViewHolder {
    TextView subCategoryName ;
    ImageView itemImage ;

    ViewHolder(View view) {
        super(view);
        subCategoryName = (TextView) view.findViewById(R.id.sub_category_name);
        itemImage = (ImageView) view.findViewById(R.id.sub_category_icon);
        view.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
//                    Toast.makeText(mContext,categoryName.getText().toString(),Toast.LENGTH_SHORT).show();
                ItemlistDisplay fragment = new ItemlistDisplay();
                Bundle bundle=new Bundle();
                bundle.putString("sub_category",subCategoryName.getText().toString());
                bundle.putString("category",subCategoryList.category);
                fragment.setArguments(bundle);
                ((Home) mContext).getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
    }
}

    @Override
    public SubCategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.sub_category_model, parent, false);
        return new SubCategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(SubCategoryAdapter.ViewHolder holder, int position) {
        SubCategoryModel activityListItems = activityList.get(position);
        holder.subCategoryName.setText(activityListItems.getSubCategoryName());
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
