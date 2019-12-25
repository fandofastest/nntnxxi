package com.nonton.xx1.frag;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.nonton.xx1.Config;
import com.nonton.xx1.R;
import com.nonton.xx1.adap.CommonGridAdapter;
import com.nonton.xx1.mdl.CommonModels;
import com.nonton.xx1.utl.ApiResources;
import com.nonton.xx1.utl.BannerAds;
import com.nonton.xx1.utl.NetworkInst;
import com.nonton.xx1.utl.SpacingItemDecoration;
import com.nonton.xx1.utl.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MoviesFragment extends Fragment {

    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerView;
    private CommonGridAdapter mAdapter;
    private List<CommonModels> list =new ArrayList<>();

    private ApiResources apiResources;

    private String URL=null;
    private boolean isLoading=false;
    private ProgressBar progressBar;
    private int pageCount=1,checkPass=0;
    private SwipeRefreshLayout swipeRefreshLayout;
    private CoordinatorLayout coordinatorLayout;
    private TextView tvNoItem;

    private RelativeLayout adView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_movies,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle(getResources().getString(R.string.movie));



        initComponent(view);

    }


    private void initComponent(View view) {

        apiResources=new ApiResources();

        adView=view.findViewById(R.id.adView);
        progressBar=view.findViewById(R.id.item_progress_bar);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        shimmerFrameLayout.startShimmer();
        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        coordinatorLayout=view.findViewById(R.id.coordinator_lyt);
        tvNoItem=view.findViewById(R.id.tv_noitem);

        //----movie's recycler view-----------------
        recyclerView = view.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 3));
        recyclerView.addItemDecoration(new SpacingItemDecoration(3, Tools.dpToPx(getActivity(), 12), true));
        recyclerView.setHasFixedSize(true);
        recyclerView.setNestedScrollingEnabled(false);
        mAdapter = new CommonGridAdapter(getContext(), list);
        recyclerView.setAdapter(mAdapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1) && !isLoading) {

                    coordinatorLayout.setVisibility(View.GONE);

                    pageCount=pageCount+1;
                    isLoading = true;

                    progressBar.setVisibility(View.VISIBLE);

                    getData(apiResources.getGet_movie(),pageCount);
                }
            }
        });


        if (new NetworkInst(getContext()).isNetworkAvailable()){
            getData(apiResources.getGet_movie(),pageCount);
        }else {
            tvNoItem.setText(getResources().getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                coordinatorLayout.setVisibility(View.GONE);
                pageCount=1;
                list.clear();
                recyclerView.removeAllViews();
                mAdapter.notifyDataSetChanged();
                if (new NetworkInst(getContext()).isNetworkAvailable()){
                    getData(apiResources.getGet_movie(),pageCount);
                }else {
                    tvNoItem.setText(getResources().getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        });

        //getAdDetails(new ApiResources().getAdDetails());
        loadAd();
    }

    private void loadAd(){
        if (ApiResources.adStatus.equals("1")){
            BannerAds.ShowBannerAds(getContext(), adView);
        }
    }

    private void getData(String url,int pageNum){

        String fullUrl = url+String.valueOf(pageNum);


        StringRequest stringRequest=new StringRequest(Request.Method.POST, fullUrl, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                isLoading=false;
                progressBar.setVisibility(View.GONE);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                try {
                JSONArray jsonArray =new JSONArray(response);

                for (int i=0;i<jsonArray.length();i++){


                    JSONObject jsonObject= jsonArray.getJSONObject(i);


                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setTitle(jsonObject.getString("title"));
                        models.setVideoType("movie");
                        models.setImdbrating(jsonObject.getString("imdb_rating"));
                        models.setId(jsonObject.getString("videos_id"));
                        list.add(models);


                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

                mAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);
                isLoading=false;
                progressBar.setVisibility(View.GONE);
                if (pageCount==1){
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }
            }
        })

        {




            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda", Config.API_KEY);


                return params;
            }
        };


        Volley.newRequestQueue(getContext()).add(stringRequest);

    }

}