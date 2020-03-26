package com.nonton.xx1.frag;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.ixidev.gdpr.GDPRChecker;
import com.nonton.xx1.DetailsActivity;
import com.nonton.xx1.ItemMovieActivity;
import com.nonton.xx1.MainActivity;
import com.nonton.xx1.R;
import com.nonton.xx1.UpdateActivity;
import com.nonton.xx1.adap.GenreHomeAdapter;
import com.nonton.xx1.adap.GenreListhomeAdapter;
import com.nonton.xx1.adap.HomePageAdapter;
import com.nonton.xx1.adap.LiveTvHomeAdapter;
import com.nonton.xx1.mdl.CommonModels;
import com.nonton.xx1.mdl.GenreModel;
import com.nonton.xx1.utl.ApiResources;
import com.nonton.xx1.utl.BannerAds;
import com.nonton.xx1.utl.NetworkInst;
import com.nonton.xx1.utl.ToastMsg;
import com.nonton.xx1.utl.VolleySingleton;
import com.squareup.picasso.Picasso;
import com.startapp.android.publish.adsCommon.Ad;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;
import com.viewpagerindicator.CirclePageIndicator;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;
import com.nonton.xx1.Config;

import com.facebook.ads.*;

import static com.android.volley.VolleyLog.TAG;
import static com.android.volley.VolleyLog.v;
import static com.nonton.xx1.utl.MyAppClass.getContext;


public class HomeFragment extends Fragment {

    ViewPager viewPager;
    CirclePageIndicator indicator;



    private List<CommonModels> listSlider=new ArrayList<>();

    private Timer timer;

    private ShimmerFrameLayout shimmerFrameLayout;
    private RecyclerView recyclerViewMovie,recyclerViewTv,recyclerViewTvSeries,recyclerViewGenre,recyclerViewgenrehome,recyclerViewfeatured,rvmostview;
    private HomePageAdapter adapterMovie,adapterSeries,adapterfeatured,adaptermostview;
    private LiveTvHomeAdapter adapterTv;
    private List<CommonModels> listMovie =new ArrayList<>();
    private List<CommonModels> listmostviewMovie =new ArrayList<>();

    private List<CommonModels> listfeatured =new ArrayList<>();
    private List<CommonModels> listTv =new ArrayList<>();
    private List<CommonModels> listSeries =new ArrayList<>();
    private ApiResources apiResources;
    private Button btnMoreMovie,btncomingsoon,btnMoreSeries,btnmoremostview,btnmorefeatured;

    private int checkPass =0;
    private GenreListhomeAdapter genrelistHomeAdapter;


    private SliderAdapter sliderAdapter;

    private VolleySingleton singleton;
    private TextView tvNoItem;
    private CoordinatorLayout coordinatorLayout;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ScrollView scrollView;

    private RelativeLayout adView,fanadView;


    private List<GenreModel> listGenre = new ArrayList<>();
    private List <CommonModels> listgenrehome = new ArrayList<>();
    private GenreHomeAdapter genreHomeAdapter;
    private View sliderLayout;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_home,null);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        getActivity().setTitle(getResources().getString(R.string.app_title));
        apiResources=new ApiResources();

        singleton=new VolleySingleton(getActivity());

        adView=view.findViewById(R.id.adView);
        fanadView=view.findViewById(R.id.adViewfan);
//        btnMoreSeries=view.findViewById(R.id.btn_more_series);
//        btnMoreTv=view.findViewById(R.id.btn_more_tv);
        btnMoreMovie=view.findViewById(R.id.btn_more_movie);
        btnmorefeatured=view.findViewById(R.id.btn_more_featured);
        btnmoremostview=view.findViewById(R.id.btn_more_mostv);
        shimmerFrameLayout=view.findViewById(R.id.shimmer_view_container);
        viewPager=view.findViewById(R.id.viewPager);
        indicator=view.findViewById(R.id.indicator);
        tvNoItem=view.findViewById(R.id.tv_noitem);
        coordinatorLayout=view.findViewById(R.id.coordinator_lyt);
        swipeRefreshLayout=view.findViewById(R.id.swipe_layout);
        scrollView=view.findViewById(R.id.scrollView);
        sliderLayout=view.findViewById(R.id.slider_layout);


        sliderAdapter=new SliderAdapter(getActivity(),listSlider);
        viewPager.setAdapter(sliderAdapter);
        indicator.setViewPager(viewPager);

        //----init timer slider--------------------
        timer = new Timer();


        //----btn click-------------
        btnClick();



        recyclerViewgenrehome = view.findViewById(R.id.recyclerViewgenrehome);


        recyclerViewgenrehome.setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.HORIZONTAL,false));
//        recyclerViewgenrehome.addItemDecoration(new SpacingItemDecoration(1, Tools.dpToPx(getActivity(), 10), true));
        recyclerViewgenrehome.setHasFixedSize(true);
        recyclerViewgenrehome.setNestedScrollingEnabled(false);
        genrelistHomeAdapter = new GenreListhomeAdapter(getContext(), listgenrehome,"genre");
        recyclerViewgenrehome.setAdapter(genrelistHomeAdapter);



        //----featured tv recycler view-----------------
        recyclerViewTv = view.findViewById(R.id.recyclerViewTv);
        recyclerViewTv.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewTv.setHasFixedSize(true);
        recyclerViewTv.setNestedScrollingEnabled(false);
        adapterTv = new LiveTvHomeAdapter(getContext(), listTv);
        recyclerViewTv.setAdapter(adapterTv);


             //----movie's recycler view-----------------
        recyclerViewfeatured = view.findViewById(R.id.recyclerfeatured);
        recyclerViewfeatured.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewfeatured.setHasFixedSize(true);
        recyclerViewfeatured.setNestedScrollingEnabled(false);
        adapterfeatured = new HomePageAdapter(getContext(), listfeatured);
        recyclerViewfeatured.setAdapter(adapterfeatured);

        //----movie's recycler view-----------------
        rvmostview = view.findViewById(R.id.recyclerViewmv);
        rvmostview.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        rvmostview.setHasFixedSize(true);
        rvmostview.setNestedScrollingEnabled(false);
        adaptermostview = new HomePageAdapter(getContext(), listmostviewMovie);
        rvmostview.setAdapter(adaptermostview);

        //----movie's recycler view-----------------
        recyclerViewMovie = view.findViewById(R.id.recyclerView);
        recyclerViewMovie.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewMovie.setHasFixedSize(true);
        recyclerViewMovie.setNestedScrollingEnabled(false);
        adapterMovie = new HomePageAdapter(getContext(), listMovie);
        recyclerViewMovie.setAdapter(adapterMovie);

        //----series's recycler view-----------------
        recyclerViewTvSeries = view.findViewById(R.id.recyclerViewTvSeries);
        recyclerViewTvSeries.setLayoutManager(new LinearLayoutManager(getActivity(),LinearLayoutManager.HORIZONTAL,false));
        recyclerViewTvSeries.setHasFixedSize(true);
        recyclerViewTvSeries.setNestedScrollingEnabled(false);
        adapterSeries = new HomePageAdapter(getContext(), listSeries);
        recyclerViewTvSeries.setAdapter(adapterSeries);

        //----genre's recycler view--------------------
        recyclerViewGenre=view.findViewById(R.id.recyclerView_by_genre);
        recyclerViewGenre.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerViewGenre.setHasFixedSize(true);
        recyclerViewGenre.setNestedScrollingEnabled(false);
        genreHomeAdapter = new GenreHomeAdapter(getContext(),listGenre);
        recyclerViewGenre.setAdapter(genreHomeAdapter);






        shimmerFrameLayout.startShimmer();


        if (new NetworkInst(getContext()).isNetworkAvailable()){

                getFeaturedTV();
                getSlider(apiResources.getSlider());
                getLatestSeries();
                getLatestMovie();
                getmostview();
                getDataByGenre();
                getFeatured();
                getAllGenre();


        }else {
            tvNoItem.setText(getString(R.string.no_internet));
            shimmerFrameLayout.stopShimmer();
            shimmerFrameLayout.setVisibility(View.GONE);
            coordinatorLayout.setVisibility(View.VISIBLE);
            scrollView.setVisibility(View.GONE);
        }

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                recyclerViewMovie.removeAllViews();
                recyclerViewTv.removeAllViews();
                recyclerViewTvSeries.removeAllViews();
                recyclerViewfeatured.removeAllViews();
                recyclerViewGenre.removeAllViews();
                rvmostview.removeAllViews();
                listmostviewMovie.clear();
                listMovie.clear();
                listSeries.clear();
                listfeatured.clear();
                listSlider.clear();
                listTv.clear();
                listGenre.clear();


                if (new NetworkInst(getContext()).isNetworkAvailable()){
                    getFeaturedTV();
                    getSlider(apiResources.getSlider());
                    getLatestSeries();
                    getmostview();
                    getFeatured();
                    getLatestMovie();
                    getDataByGenre();
                }else {
                    tvNoItem.setText(getString(R.string.no_internet));
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    swipeRefreshLayout.setRefreshing(false);
                    coordinatorLayout.setVisibility(View.VISIBLE);
                    scrollView.setVisibility(View.GONE);
                }
            }
        });

        getStatusapp(new ApiResources().getStatusApp());


        getAdDetails(new ApiResources().getAdDetails());









    }

    private void loadAd(){
        if (ApiResources.adStatus.equals("1")){
            BannerAds.ShowBannerAds(getContext(), adView);
        }
        if (ApiResources.fanadStatus.equals("1")){
            BannerAds.ShowFanBannerAds(getContext(), fanadView);
        }
    }

    private void btnClick(){

        btnMoreMovie.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ItemMovieActivity.class);
                intent.putExtra("url",apiResources.getGet_movie());
                intent.putExtra("title","Movies");
                getActivity().startActivity(intent);
            }
        });
        btnmoremostview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ItemMovieActivity.class);
                intent.putExtra("url",apiResources.getMostview());
                intent.putExtra("title","Movies");
                getActivity().startActivity(intent);
            }
        });

        btnmorefeatured.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(),ItemMovieActivity.class);
                intent.putExtra("url",apiResources.getFeatured());
                intent.putExtra("title","Movies");
                getActivity().startActivity(intent);
            }
        });
//        btnMoreSeries.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(),ItemSeriesActivity.class);
//                intent.putExtra("url",apiResources.getTvSeries());
//                intent.putExtra("title","TV Series");
//                getActivity().startActivity(intent);
//            }
//        });

    }


    private void getAdDetails(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {



                try {
                    JSONObject jsonObject=response.getJSONObject("admob");

                    ApiResources.adStatus = jsonObject.getString("status");
                    ApiResources.adMobBannerId = jsonObject.getString("admob_banner_ads_id");
                    ApiResources.adMobInterstitialId = jsonObject.getString("admob_interstitial_ads_id");
                    ApiResources.adMobPublisherId = jsonObject.getString("admob_publisher_id");


                    new GDPRChecker()
                            .withContext(getContext())
                            .withPrivacyUrl(Config.TERMS_URL) // your privacy url
                            .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
                            .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
                            .check();




                } catch (JSONException e) {
                    e.printStackTrace();
                }


                try {
                    JSONObject jsonObject=response.getJSONObject("fan");

                    ApiResources.fanadStatus = jsonObject.getString("status");
                    ApiResources.fanBannerid = jsonObject.getString("fan_banner_ads_id");
                    ApiResources.faninterid = jsonObject.getString("fan_interstitial_ads_id");

//                    Toast.makeText(getActivity(),ApiResources.fanadStatus+ApiResources.fanBannerid+ApiResources.fanInterid , Toast.LENGTH_LONG).show();

                    new GDPRChecker()
                            .withContext(getContext())
                            .withPrivacyUrl(Config.TERMS_URL) // your privacy url
                            .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
                            .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
                            .check();




                } catch (JSONException e) {
                    e.printStackTrace();
                }
                loadAd();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }


    private void getDataByGenre(){

        StringRequest stringRequest =new StringRequest(Request.Method.POST, new ApiResources().getGenreMovieURL(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {


                try {
                    JSONArray jsonArray1 = new JSONArray(response);

                    for (int i =0;i<jsonArray1.length();i++){


                    JSONObject jsonObject=jsonArray1.getJSONObject(i);

                    GenreModel models=new GenreModel();

                    models.setName(jsonObject.getString("name"));
                    models.setId(jsonObject.getString("genre_id"));

                    JSONArray jsonArray = jsonObject.getJSONArray("videos");
                    //listGenreMovie.clear();
                    List<CommonModels> listGenreMovie = new ArrayList<>();
                    for (int j = 0;j<jsonArray.length();j++){

                        JSONObject movieObject = jsonArray.getJSONObject(j);

                        CommonModels commonModels = new CommonModels();
                        commonModels.setImdbrating(movieObject.getString("imdb_rating"));

                        commonModels.setId(movieObject.getString("videos_id"));
                        commonModels.setTitle(movieObject.getString("title"));
                        commonModels.setVideoType("movie");
                        commonModels.setImageUrl(movieObject.getString("poster_url"));

                        listGenreMovie.add(commonModels);

                    }


                    models.setList(listGenreMovie);

                    listGenre.add(models);
                    genreHomeAdapter.notifyDataSetChanged();
//                        Log.e("LIST 2 SIZE ::", String.valueOf(listGenreMovie.size()));




                }

                } catch (JSONException e) {
                    e.printStackTrace();
                }




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda",Config.API_KEY);



                return params;
            }
        };

        VolleySingleton.getInstance(getContext()).addToRequestQueue(stringRequest);


    }

    private void getSlider(String url){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                    swipeRefreshLayout.setRefreshing(false);
                    shimmerFrameLayout.stopShimmer();
                    shimmerFrameLayout.setVisibility(View.GONE);
                    scrollView.setVisibility(View.VISIBLE);
                    coordinatorLayout.setVisibility(View.GONE);

                    JSONObject jsonObject1 = new JSONObject(response);


                    if (jsonObject1.getString("slider_type").equals("disable")){
                        sliderLayout.setVisibility(View.GONE);
                    }

                    else if (jsonObject1.getString("slider_type").equals("movie")){

                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            CommonModels models =new CommonModels();
                            models.setImageUrl(jsonObject.getString("poster_url"));
                            models.setTitle(jsonObject.getString("title"));
                            models.setImdbrating(jsonObject.getString("imdb_rating"));
                            models.setVideoType("movie");
                            models.setId(jsonObject.getString("videos_id"));

                            listSlider.add(models);
                        }

                    }else {
                        JSONArray jsonArray=jsonObject1.getJSONArray("data");
                        for (int i=0;i<jsonArray.length();i++){
                            JSONObject jsonObject=jsonArray.getJSONObject(i);
                            CommonModels models =new CommonModels();
                            models.setImageUrl(jsonObject.getString("image_link"));
                            models.setTitle(jsonObject.getString("title"));
                            models.setVideoType("image");
                            listSlider.add(models);

                        }
                    }

                    sliderAdapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                coordinatorLayout.setVisibility(View.VISIBLE);
                scrollView.setVisibility(View.GONE);

            }
        })           {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda",Config.API_KEY);



                return params;
            }
        };

        Volley.newRequestQueue(getActivity()).add(stringRequest);


    }



    private void getLatestSeries(){

        StringRequest stringRequest=new StringRequest(Request.Method.POST, apiResources.getLatestTvSeries(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {

                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);



                try {
                    JSONArray jsonArray =new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("thumbnail_url"));
                        models.setTitle(jsonObject.getString("title"));
                        models.setVideoType("tvseries");
                        models.setId(jsonObject.getString("videos_id"));
                        listSeries.add(models);

                        adapterSeries.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda",Config.API_KEY);



                return params;
            }
        };
        singleton.addToRequestQueue(stringRequest);

    }

    private void getLatestMovie(){



        final StringRequest stringRequest=new StringRequest(Request.Method.POST, apiResources.getLatest_movie(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray =new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject1= jsonArray.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject1.getString("thumbnail_url"));
                        models.setTitle(jsonObject1.getString("title"));
                        models.setImdbrating(jsonObject1.getString("imdb_rating"));
                        models.setVideoType("movie");
                        models.setId(jsonObject1.getString("videos_id"));
                        listMovie.add(models);

                        adapterMovie.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda",Config.API_KEY);



                return params;
            }
        };




        singleton.addToRequestQueue(stringRequest);

    }
    private void getmostview(){



        final StringRequest stringRequest=new StringRequest(Request.Method.POST, apiResources.getMostview(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray =new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject1= jsonArray.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject1.getString("thumbnail_url"));
                        models.setTitle(jsonObject1.getString("title"));
                        models.setImdbrating(jsonObject1.getString("imdb_rating"));
                        models.setVideoType("movie");
                        models.setId(jsonObject1.getString("videos_id"));
                        listmostviewMovie.add(models);

                        adaptermostview.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda",Config.API_KEY);



                return params;
            }
        };




        singleton.addToRequestQueue(stringRequest);

    }


    private void getFeatured(){



        final StringRequest stringRequest=new StringRequest(Request.Method.POST, apiResources.getFeatured(), new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);

                try {
                    JSONArray jsonArray =new JSONArray(response);

                    for (int i=0;i<jsonArray.length();i++){

                        JSONObject jsonObject1= jsonArray.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject1.getString("thumbnail_url"));
                        models.setTitle(jsonObject1.getString("title"));
                        models.setImdbrating(jsonObject1.getString("imdb_rating"));
                        models.setVideoType("movie");
                        models.setId(jsonObject1.getString("videos_id"));
                        listfeatured.add(models);

                        adapterfeatured.notifyDataSetChanged();

                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }





            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());

            }
        })
        {
            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("kunciganda",Config.API_KEY);



                return params;
            }
        };




        singleton.addToRequestQueue(stringRequest);

    }

    private void getFeaturedTV(){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, apiResources.getGet_featured_tv(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                for (int i=0;i<response.length();i++){
                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setImageUrl(jsonObject.getString("poster_url"));
                        models.setTitle(jsonObject.getString("tv_name"));
                        models.setVideoType("tv");
                        models.setId(jsonObject.getString("live_tv_id"));
                        listTv.add(models);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                adapterTv.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        singleton.addToRequestQueue(jsonArrayRequest);

    }

    @Override
    public void onStart() {
        super.onStart();
        shimmerFrameLayout.startShimmer();
    }

    @Override
    public void onPause() {
        super.onPause();
        shimmerFrameLayout.stopShimmer();
        timer.cancel();
    }

    @Override
    public void onResume() {
        super.onResume();
        timer=new Timer();
        timer.scheduleAtFixedRate(new SliderTimer(), 5000, 5000);
    }

    //----timer for auto slide------------------
    private class SliderTimer extends TimerTask {

        @Override
        public void run() {

            if (getActivity()!=null){
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (viewPager.getCurrentItem() < listSlider.size() - 1) {
                            viewPager.setCurrentItem(viewPager.getCurrentItem() + 1);
                        } else {
                            viewPager.setCurrentItem(0);
                        }
                    }
                });
            }

        }
    }

    //----adapter for slider-------------
    public class SliderAdapter extends PagerAdapter {

        private Context context;
        private List<CommonModels> list=new ArrayList<>();

        public SliderAdapter(Context context, List<CommonModels> list) {
            this.context = context;
            this.list=list;
        }

        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public boolean isViewFromObject(View view, Object object) {
            return view == object;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.item_slider, null);
            AudienceNetworkAds.isInAdsProcess(getContext());
            View lyt_parent = view.findViewById(R.id.lyt_parent);

//            RelativeLayout adContainer = (RelativeLayout) view.findViewById(R.id.adViewfan);
//
//            fanadView = new AdView(getContext(), "330526207815682_330526497815653", AdSize.BANNER_HEIGHT_50);
//
//            // Find the Ad Container
//            fanadView.loadAd();
//            // Add the ad view to your activity layout
//            adContainer.addView(fanadView);
            // Request an ad



            final CommonModels models=list.get(position);

            TextView textView = view.findViewById(R.id.textView);

            TextView imdbrating= view.findViewById(R.id.tvimdb);
            imdbrating.setText(models.getImdbrating());

            textView.setText(models.getTitle());

            ImageView imageView=view.findViewById(R.id.imageview);

            Picasso.get().load(models.getImageUrl()).into(imageView);


            ViewPager viewPager = (ViewPager) container;
            viewPager.addView(view, 0);

            lyt_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (models.getVideoType().equals("movie")){

                        if (ApiResources.startappstatus.equals("1")){

                            StartAppAd startAppAd = new StartAppAd(context);
                            startAppAd.showAd(new AdDisplayListener() {
                                @Override
                                public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {




                                    Intent intent=new Intent(getContext(), DetailsActivity.class);
                                    intent.putExtra("vType",models.getVideoType());
                                    intent.putExtra("id",models.getId());
                                    startActivity(intent);



                                }

                                @Override
                                public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                                }

                                @Override
                                public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {

                                }

                                @Override
                                public void adNotDisplayed(Ad ad) {
                                    Intent intent=new Intent(getContext(), DetailsActivity.class);
                                    intent.putExtra("vType",models.getVideoType());
                                    intent.putExtra("id",models.getId());
                                    startActivity(intent);


                                }
                            });



                        }else {
                            Intent intent=new Intent(getContext(), DetailsActivity.class);
                            intent.putExtra("vType",models.getVideoType());
                            intent.putExtra("id",models.getId());
                            startActivity(intent);


                        }








                    }else {

                    }
                }
            });
            return view;
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            ViewPager viewPager = (ViewPager) container;
            View view = (View) object;
            viewPager.removeView(view);
        }
    }

    public void getAllGenre(){

        JsonArrayRequest jsonArrayRequest=new JsonArrayRequest(Request.Method.GET, apiResources.getAllGenre(), null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                swipeRefreshLayout.setRefreshing(false);

                if (String.valueOf(response).length()<10){
                    coordinatorLayout.setVisibility(View.VISIBLE);
                }else {
                    coordinatorLayout.setVisibility(View.GONE);
                }

                for (int i=0;i<response.length();i++){

                    try {
                        JSONObject jsonObject=response.getJSONObject(i);
                        CommonModels models =new CommonModels();
                        models.setId(jsonObject.getString("genre_id"));
                        models.setTitle(jsonObject.getString("name"));
                        listgenrehome.add(models);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                genrelistHomeAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                swipeRefreshLayout.setRefreshing(false);
                shimmerFrameLayout.stopShimmer();
                shimmerFrameLayout.setVisibility(View.GONE);
                new ToastMsg(getActivity()).toastIconError(getString(R.string.fetch_error));

                coordinatorLayout.setVisibility(View.VISIBLE);
            }
        });
        Volley.newRequestQueue(getContext()).add(jsonArrayRequest);



    }

    public void dialognew(){

        //before inflating the custom alert dialog layout, we will get the current activity viewgroup
        ViewGroup viewGroup = getView().findViewById(android.R.id.content);


        //then we will inflate the custom alert dialog xml that we created
        final View dialogView = LayoutInflater.from(getContext()).inflate(R.layout.popup_dialog, viewGroup, false);

        TextView judulnotif = dialogView.findViewById(R.id.judulpesan);
        TextView isinotif = dialogView.findViewById(R.id.isipesan);
        ImageView icon =dialogView.findViewById(R.id.icon);
        ImageView foto= dialogView.findViewById(R.id.foto);

        Glide.with(getContext())
                .load(ApiResources.icon)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(icon);

        Glide.with(getContext())
                .load(ApiResources.foto)
                .placeholder(R.drawable.ic_launcher_foreground)
                .into(foto);

        Button button = dialogView.findViewById(R.id.buttonOk);

        button.setText("Install Now");

        //Now we need an AlertDialog.Builder object
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        //setting the view of the builder to our custom view that we already inflated

        builder.setView(dialogView);

        //finally creating the alert dialog and displaying it
        final AlertDialog alertDialog = builder.create();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (ApiResources.apknew.equals("")){

                    alertDialog.dismiss();


                }
                else{
                    final String appPackageName = ApiResources.apknew; // getPackageName() from Context or Activity object
                    try {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    };
                }

            }
        });

        judulnotif.setText(ApiResources.judulstatus);
        isinotif.setText(ApiResources.pesan);






        alertDialog.show();
    }


    private void getStatusapp(String url){

        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {


                try {
                    JSONObject jsonObject=response.getJSONObject("statusapp");

                    ApiResources.statusapp = jsonObject.getString("statusapp");
                    String apk = jsonObject.getString("apk");

                    if (ApiResources.statusapp.equals("0")){

                        Intent intent=new Intent(getContext(), UpdateActivity.class);
                        intent.putExtra("apk",apk);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        startActivity(intent);


                        Toast.makeText(getContext(),"APPNOTFOUND" , Toast.LENGTH_LONG).show();



                    }

                    JSONObject jsonObject1=response.getJSONObject("notifapp");

                    ApiResources.statusnotif = jsonObject1.getString("statusnotif");
                    ApiResources.judulstatus = jsonObject1.getString("judulstatus");
                    ApiResources.pesan = jsonObject1.getString("pesan");
                    ApiResources.foto = jsonObject1.getString("foto");
                    ApiResources.icon = jsonObject1.getString("icon");
                    ApiResources.apknew = jsonObject1.getString("apk");
                    System.out.println("statusnotif:"+ApiResources.statusnotif);

                    if (ApiResources.statusnotif.equals("1"))
                    {
                        dialognew();

                    }







                } catch (JSONException e) {
                    System.out.println("Error json" +e);
                    e.printStackTrace();

                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("ggl"+error);

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }








}
