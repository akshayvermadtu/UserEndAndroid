package com.homebuddy.homebuddy;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ItemlistDisplay extends Fragment {
    View mView;
    RecyclerView recyclerView ;
    private List<ItemListModel> activityList = new ArrayList<>();
    private ItemListAdapter mAdapter;
    ItemListModel activityItems;
    private static final String TAG = "ItemList";
    ProgressBar progressBar,loadMoreProgressBar;
    LinearLayoutManager layoutManager ;
    String sub_category ;
    String category ;
    int page_count = 1 ;
    String imageUrl ;

    public ItemlistDisplay() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sub_category = getArguments().getString("sub_category");
        category = getArguments().getString("category");
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_itemlist_display, container, false);
        this.mView = view;

        ((Home) getActivity())
                .setActionBarTitle(sub_category);

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        progressBar = (ProgressBar)mView.findViewById(R.id.itemList_loading);
        loadMoreProgressBar = (ProgressBar)mView.findViewById(R.id.MoreItemList_loading);
        recyclerView = (RecyclerView) mView.findViewById(R.id.itemListRecycler);
        mAdapter = new ItemListAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        ItemListApiCall(sub_category);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    LoadMoreItemListApiCall(sub_category,++page_count);
                }
            }
        });

        return view;
    }

    void ItemListApiCall(String subCategory){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/items/";
        Map<String, Object> data = new HashMap<>();
        data.put( "sub_category", subCategory );
        data.put( "page_no", 1 );

        VolleyRequester request = new VolleyRequester(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);
                        String itemName = itemDetails.get("name").toString();
                        String itemPrice = itemDetails.get("price").toString();
                        String itemBrand = itemDetails.get("brand").toString();
                        String itemImage = itemDetails.get("image").toString();
                        if (itemImage.contains("_")){
                            String str[] = itemImage.split("_");
                            imageUrl = str[0]+".png";
                        }
                        else
                            imageUrl = itemImage ;

//                             Toast.makeText(getActivity(),imageUrl,Toast.LENGTH_LONG).show();


                        activityItems = new ItemListModel(itemName , "Rs. "+itemPrice , "Brand : "+itemBrand , "https://homebuddy2018.herokuapp.com/"+ imageUrl);
                        activityList.add(activityItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter.notifyDataSetChanged();
                hideProgress();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
            @Override
            public String getBodyContentType()
            {
                return "application/json";
            }
        };

        queue.add(request);
    }

    void LoadMoreItemListApiCall(String subCat , int page_no){
        showLoadMoreProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/items/";
        Map<String, Object> data = new HashMap<>();
        data.put( "sub_category", subCat );
        data.put( "page_no", page_no );

        VolleyRequester request = new VolleyRequester(Request.Method.POST,api,new JSONObject(data),new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);

                        if (!itemDetails.has("status")){
                            String itemName = itemDetails.get("name").toString();
                            String itemPrice = itemDetails.get("price").toString();
                            String itemBrand = itemDetails.get("brand").toString();
                            String itemImage = itemDetails.get("image").toString();
                            if (itemImage.contains("_")){
                                String str[] = itemImage.split("_");
                                imageUrl = str[0]+".png";
                            }
                            else
                                imageUrl = itemImage ;


                            activityItems = new ItemListModel(itemName , "Rs. "+itemPrice , "Brand : "+itemBrand , "https://homebuddy2018.herokuapp.com/"+ imageUrl);
                            activityList.add(activityItems);
                        }

                        else{
//                            Toast.makeText(getActivity(),"No more data",Toast.LENGTH_LONG).show();
                            break;
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter.notifyDataSetChanged();
                hideLoadMoreProgress();
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
            }
        })
        {
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

    private void showLoadMoreProgress() {

        loadMoreProgressBar.setIndeterminate(true);
        loadMoreProgressBar.setVisibility(View.VISIBLE);
    }
    private void hideLoadMoreProgress() {
        loadMoreProgressBar.setIndeterminate(false);
        loadMoreProgressBar.setVisibility(View.GONE);
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

                        SubCategoryList fragment = new SubCategoryList();
                        Bundle bundle=new Bundle();
                        bundle.putString("category",category);
                        fragment.setArguments(bundle);
                        getActivity().getSupportFragmentManager().beginTransaction()
                                .replace(R.id.fragment_container,fragment)
                                .addToBackStack(null)
                                .commit();

                    }

                    return true;
                }
                return false;
            }
        });

    }

}
