package com.homebuddy.homebuddy;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.ViewFlipper;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment{
    protected View mView;
    ViewFlipper viewFlipper;
    RecyclerView recyclerView ;
    List<CategoryModel> activityList = new ArrayList<>();
    CategoryAdapter mAdapter;
    CategoryModel activityItems;
    private static final String TAG = "categoryList";
    ProgressBar progressBar;
    LinearLayoutManager layoutManager ;

    public HomeFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        View view = inflater.inflate(R.layout.fragment_home2, container, false);
        this.mView = view;
        setHasOptionsMenu(true);

        ((Home) getActivity())
                .setActionBarTitle("Home");

        EditText searchClick = (EditText)mView.findViewById(R.id.search_click);
        searchClick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SearchItem fragment = new SearchItem();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .setCustomAnimations(R.anim.slide_in_up, R.anim.slide_out_up)
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });


        progressBar = (ProgressBar)mView.findViewById(R.id.category_progress_bar);
        recyclerView = (RecyclerView) mView.findViewById(R.id.category_recycler);
        mAdapter = new CategoryAdapter(activityList, getActivity());
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setAdapter(mAdapter);

//        ViewCompat.setNestedScrollingEnabled(recyclerView, true);
        viewFlipper = (ViewFlipper)mView.findViewById(R.id.home_view_flipper);
        ImageView imageView = new ImageView(getActivity());
        Picasso.with(getActivity())
                .load("https://4.imimg.com/data4/LD/PB/MY-13969859/international-express-delivery-500x500.png")
                .error(R.mipmap.error_image)
                .resize(400,200)
                .into(imageView);
        viewFlipper.addView(imageView);
        ImageView imageView2 = new ImageView(getActivity());
        Picasso.with(getActivity())
                .load("http://www.molomart.com/uploads/images/1-composition-with-variety-of-grocery-products-t-monticello1.jpg")
                .error(R.mipmap.error_image)
                .resize(400,200)
                .into(imageView2);
        viewFlipper.addView(imageView2);
        ImageView imageView3 = new ImageView(getActivity());
        Picasso.with(getActivity())
                .load("https://beprepared-0qbhwd9wnnmy.stackpathdns.com/media/wysiwyg/custom/misc/Low_Price_Guarantee_Page_Icon.jpg")
                .error(R.mipmap.error_image)
                .resize(400,200)
                .into(imageView3);
        viewFlipper.addView(imageView3);
        viewFlipper.setAutoStart(true);
        viewFlipper.setFlipInterval(2000);
        viewFlipper.setInAnimation(getActivity(), R.anim.view_transition_in_left);
        viewFlipper.setOutAnimation(getActivity(), R.anim.view_transition_out_left);
        viewFlipper.showNext();
        viewFlipper.startFlipping();

        FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cart fragment = new Cart();
                getActivity().getSupportFragmentManager().beginTransaction()
                        .replace(R.id.fragment_container,fragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
        fab.setVisibility(View.VISIBLE);

        CategoryListApiCall();

        return view;
    }

    void CategoryListApiCall(){
        showProgress();
        RequestQueue queue = Volley.newRequestQueue(getActivity());
        String api = "http://192.168.1.5:8000/showCat";

        VolleyRequester request = new VolleyRequester(Request.Method.GET,api,null,new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray jsonArray) {

                for (int i = 0; i <=jsonArray.length(); i++) {
                    try {
                        JSONObject itemDetails = (JSONObject)jsonArray.get(i);
                        String categoryName = itemDetails.get("category_name").toString();
                        String itemImage = itemDetails.get("image").toString();

                        activityItems = new CategoryModel(categoryName , "http://192.168.1.5:8000/"+itemImage );
                        activityList.add(activityItems);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }

                mAdapter = new CategoryAdapter(activityList, getActivity());
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

}
