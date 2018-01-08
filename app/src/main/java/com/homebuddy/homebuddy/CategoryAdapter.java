package com.homebuddy.homebuddy;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.List;

class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder>{
    private List<CategoryModel> activityList;
    private Context mContext;

    CategoryAdapter(List<CategoryModel> activityList, Context context) {
        this.activityList = activityList;
        mContext = context;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView categoryName ;
        ImageView itemImage ;

        public ViewHolder(View view) {
            super(view);
            categoryName = (TextView) view.findViewById(R.id.category_name);
            itemImage = (ImageView) view.findViewById(R.id.category_icon);
            view.setOnClickListener(new View.OnClickListener() {
                @Override public void onClick(View v) {
//                    Toast.makeText(mContext,categoryName.getText().toString(),Toast.LENGTH_SHORT).show();
                    SubCategoryList fragment = new SubCategoryList();
                    Bundle bundle=new Bundle();
                    bundle.putString("category",categoryName.getText().toString());
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
    public CategoryAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.category_model, parent, false);
        return new CategoryAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(CategoryAdapter.ViewHolder holder, int position) {
        CategoryModel activityListItems = activityList.get(position);
        holder.categoryName.setText(activityListItems.getCategoryName());
        Picasso.with(mContext)
                .load(activityListItems.getImageURL())
                .error(R.mipmap.error_image)
                .resize(30,30)
                .into(holder.itemImage);
    }

    @Override
    public int getItemCount() {
        return activityList.size();
    }
}
