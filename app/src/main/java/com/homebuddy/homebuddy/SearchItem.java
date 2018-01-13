package com.homebuddy.homebuddy;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
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


public class SearchItem extends Fragment {
    View mView;
    RecyclerView recyclerView ;
    private List<ItemListModel> activityList = new ArrayList<>();
    private ItemListAdapter mAdapter;
    ItemListModel activityItems;
    private static final String TAG = "ItemSearch";
    ProgressBar progressBar , loadMoreProgressBar;
    LinearLayoutManager layoutManager ;
    TextView textView ;
    EditText search ;
    int page_count = 1 ;
    String imageUrl ;

    public SearchItem() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_search_item, container, false);
        this.mView = view;

        ((Home) getActivity())
                .setActionBarTitle("Express search");

        progressBar = (ProgressBar)mView.findViewById(R.id.itemSearch_loading);
        loadMoreProgressBar = (ProgressBar)mView.findViewById(R.id.MoreItem_loading);
        recyclerView = (RecyclerView) mView.findViewById(R.id.searchItemRecycler);
        textView = (TextView)mView.findViewById(R.id.search_textView);
        mAdapter = new ItemListAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

        search = (EditText)mView.findViewById(R.id.search_item);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                activityList.clear();
                mAdapter= new ItemListAdapter(activityList,getActivity());
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                SearchItemListApiCall(s.toString());
                page_count = 1 ;
            }
        });

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    LoadMoreItemListApiCall(search.getText().toString(),++page_count);
                }
            }
        });

        return view ;
    }

    void SearchItemListApiCall(final String text){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/search/";
        Map<String, Object> data = new HashMap<>();
        data.put( "text", text );
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

                        // Toast.makeText(getActivity(),imageUrl,Toast.LENGTH_LONG).show();


                        activityItems = new ItemListModel(itemName , "Rs. "+itemPrice , "Brand : "+itemBrand , "https://homebuddy2018.herokuapp.com/"+ imageUrl);
                        activityList.add(activityItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter = new ItemListAdapter(activityList, getActivity());
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

    void LoadMoreItemListApiCall(final String text , int page_no){
        showLoadMoreProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "https://homebuddy2018.herokuapp.com/search/";
        Map<String, Object> data = new HashMap<>();
        data.put( "text", text );
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
        textView.setVisibility(View.GONE);
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

                        HomeFragment fragment = new HomeFragment();
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
