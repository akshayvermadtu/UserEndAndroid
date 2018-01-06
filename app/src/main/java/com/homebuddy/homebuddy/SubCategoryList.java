package com.homebuddy.homebuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SubCategoryList extends Fragment {
    View mView ;
    private static final String TAG = "SubcategoryList";
    ProgressBar progressBar;
    public String category ;
    private List<SubCategoryModel> activityList = new ArrayList<>();
    private SubCategoryAdapter mAdapter;
    SubCategoryModel activityItems;
    LinearLayoutManager layoutManager ;
    RecyclerView recyclerView ;

    public SubCategoryList() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        category = getArguments().getString("category");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_sub_category_list, container, false);
        this.mView = view;

        ((Home) getActivity())
                .setActionBarTitle(category);

        progressBar = (ProgressBar)mView.findViewById(R.id.sub_category_progress_bar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.sub_category_recycler);
        mAdapter = new SubCategoryAdapter(activityList, getActivity() , SubCategoryList.this);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);
        setHasOptionsMenu(true);
        SubCategoryListApiCall(category);

        return view ;
    }

    void SubCategoryListApiCall(String category){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "http://192.168.43.43:8000/showSubCat/";

        Map<String, Object> data = new HashMap<>();
        data.put( "category", category );

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.POST, api ,new JSONObject(data), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {
                    List subCategory;
                    String subCategoryString = response.getString("sub_categories");
                    String newSubCategoryString= subCategoryString.replaceAll("[\\[\\](){}]","");
                    String str[] = newSubCategoryString.split(",");
                    subCategory = Arrays.asList(str);
                    for(Object s: subCategory){
                        activityItems = new SubCategoryModel(s.toString());
                        activityList.add(activityItems);
                    }
                }

                catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter = new SubCategoryAdapter(activityList, getActivity() , SubCategoryList.this);
                recyclerView.setLayoutManager(layoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
                hideProgress();
            }

        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }

        }){
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(request);
    }

    private void showProgress() {
        recyclerView.setVisibility(View.GONE);
        progressBar.setIndeterminate(true);
        progressBar.setVisibility(View.VISIBLE);
    }

    private void hideProgress() {
        recyclerView.setVisibility(View.VISIBLE);
        progressBar.setIndeterminate(false);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(getView() == null){
            return;
        }

        getView().setFocusableInTouchMode(true);
        getView().requestFocus();
        getView().setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {

                if (event.getAction() == KeyEvent.ACTION_UP && keyCode == KeyEvent.KEYCODE_BACK){

                    DrawerLayout drawer = (DrawerLayout) getActivity().findViewById(R.id.drawer_layout);
                    if (drawer.isDrawerOpen(GravityCompat.START)) {
                        drawer.closeDrawer(GravityCompat.START);
                    } else {

                        HomeFragment fragment = new HomeFragment();
                        FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                        fragmentTransaction.replace(R.id.fragment_container,fragment);
                        fragmentTransaction.addToBackStack(null);
                        fragmentTransaction.commit();

                    }

                    return true;
                }
                return false;
            }
        });

    }

}
