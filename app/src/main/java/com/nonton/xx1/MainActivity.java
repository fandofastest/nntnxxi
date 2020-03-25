package com.nonton.xx1;

import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import androidx.annotation.NonNull;

import com.facebook.ads.AdView;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;

import androidx.fragment.app.Fragment;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuItemCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.analytics.FirebaseAnalytics;

import com.nonton.xx1.adap.NavigationAdapter;
import com.nonton.xx1.frag.MoviesFragment;
import com.nonton.xx1.mdl.NavigationModel;
import com.nonton.xx1.navfrag.CountryFragment;
import com.nonton.xx1.navfrag.FavoriteFragment;
import com.nonton.xx1.navfrag.GenreFragment;
import com.nonton.xx1.navfrag.MainHomeFragment;
import com.nonton.xx1.utl.ApiResources;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.stepstone.apprating.AppRatingDialog;
import com.stepstone.apprating.listener.RatingDialogListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, RatingDialogListener {


    private DrawerLayout mDrawerLayout;
    private Toolbar toolbar;

    private RecyclerView recyclerView;
    private NavigationAdapter mAdapter;
    private List<NavigationModel> list =new ArrayList<>();
    private NavigationView navigationView;
    private String[] navItemImage;

    private String[] navItemName2;
    private String[] navItemImage2;
    private boolean status=false;

    private SharedPreferences preferences;
    private FirebaseAnalytics mFirebaseAnalytics;

    private AdView fanadView;


    public static String fanadStatus,fanBannerid="0",fanInterid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        AudienceNetworkAds.initialize(this);

        //---analytics-----------
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);
        Bundle bundle = new Bundle();
        bundle.putString(FirebaseAnalytics.Param.ITEM_ID, "id");
        bundle.putString(FirebaseAnalytics.Param.ITEM_NAME, "main_activity");
        bundle.putString(FirebaseAnalytics.Param.CONTENT_TYPE, "activity");
        mFirebaseAnalytics.logEvent(FirebaseAnalytics.Event.SELECT_CONTENT, bundle);

        //----dark mode----------
        preferences=getSharedPreferences("push",MODE_PRIVATE);
//        if (preferences.getBoolean("dark",false)){
//            AppCompatDelegate
//                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
//        }else {
//            AppCompatDelegate
//                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
//        }

        if (ApiResources.startappstatus.equals("1")){

            StartAppAd.showSplash(this, savedInstanceState);

        }



        //----init---------------------------
        navigationView = findViewById(R.id.nav_view);
        mDrawerLayout = findViewById(R.id.drawer_layout);


        //----navDrawer------------------------
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionbar = getSupportActionBar();
        actionbar.setDisplayHomeAsUpEnabled(true);
        actionbar.setHomeAsUpIndicator(R.drawable.ic_menu);
        navigationView.setNavigationItemSelectedListener(this);


        //----fetch array------------
        String[] navItemName = getResources().getStringArray(R.array.nav_item_name);



        navItemName2=getResources().getStringArray(R.array.nav_item_name_2);




        //----navigation view items---------------------
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.addItemDecoration(new SpacingItemDecoration(2, Tools.dpToPx(this, 15), true));
        recyclerView.setHasFixedSize(true);


        SharedPreferences prefs = getSharedPreferences("user", MODE_PRIVATE);
        status = prefs.getBoolean("status",false);

        if (status){
            for (int i = 0; i< navItemName.length; i++){
                NavigationModel models =new NavigationModel( navItemName[i]);
                list.add(models);
            }
        }else {
            for (int i=0;i<navItemName2.length;i++){
                NavigationModel models =new NavigationModel(navItemName2[i]);
                list.add(models);
            }
        }







        //set data and list adapter
        mAdapter = new NavigationAdapter(this, list);
        recyclerView.setAdapter(mAdapter);


        final NavigationAdapter.OriginalViewHolder[] viewHolder = {null};

        mAdapter.setOnItemClickListener(new NavigationAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, NavigationModel obj, int position, NavigationAdapter.OriginalViewHolder holder) {

                Log.e("POSITION OF NAV:::", String.valueOf(position));

                //----action for click items nav---------------------

                if (position==0){
                    loadFragment(new MainHomeFragment());
                }
                else if (position==1){
                    loadFragment(new MoviesFragment());
                }

                else if (position==2){
                    loadFragment(new GenreFragment());
                }
                else if (position==3){
                    loadFragment(new CountryFragment());
                }
                else {


                    if (status){

                        if (position==4){
                            Intent intent=new Intent(MainActivity.this,ProfileActivity.class);
                            startActivity(intent);
                        }
                        else if (position==5){
                            loadFragment(new FavoriteFragment());
                        }
                        else if (position==6){
                            new AlertDialog.Builder(MainActivity.this).setMessage("Are you sure to logout ?")
                                    .setPositiveButton("YES", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                            editor.putBoolean("status",false);
                                            editor.apply();

                                            Intent intent=new Intent(MainActivity.this,MainActivity.class);
                                            startActivity(intent);
                                            finish();
                                        }
                                    })
                                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            dialog.cancel();
                                        }
                                    }).create().show();
                        }
                        else if (position==7){
                            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                            startActivity(intent);
                        }
                    }else {
                        if (position==4){
                            Intent intent=new Intent(MainActivity.this,LoginActivity.class);
                            startActivity(intent);
                        }
                        else if (position==5){
                            Intent intent=new Intent(MainActivity.this,SettingsActivity.class);
                            startActivity(intent);
                        }
                    }

                }




                //----behaviour of bg nav items-----------------
                if (!obj.getTitle().equals("Settings") && !obj.getTitle().equals("Login") && !obj.getTitle().equals("Sign Out")){

                    if (preferences.getBoolean("dark",false)){
                        mAdapter.chanColor(viewHolder[0],position,R.color.nav_bg);
                    }else {
                        mAdapter.chanColor(viewHolder[0],position,R.color.white);
                    }


                    holder.cardView.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));
                    holder.name.setTextColor(getResources().getColor(R.color.white));
                    viewHolder[0] =holder;
                }

                mDrawerLayout.closeDrawers();
            }
        });

        //----external method call--------------
        loadFragment(new MainHomeFragment());








    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_action, menu);
        return true;
    }


    private boolean loadFragment(Fragment fragment){

        if (fragment!=null){

            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.fragment_container,fragment)
                    .commit();

            return true;
        }
        return false;

    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.action_search:

                final SearchView searchView = (SearchView) MenuItemCompat.getActionView(item);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {

                        Intent intent=new Intent(MainActivity.this,SearchActivity.class);
                        intent.putExtra("q",s);
                        startActivity(intent);

                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        return false;
                    }
                });

                return true;

        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

        if (mDrawerLayout.isDrawerOpen(GravityCompat.START)){
            mDrawerLayout.closeDrawers();
        }else {

            showDialog();

        }
    }

    //----nav menu item click---------------
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        // set item as selected to persist highlight
        menuItem.setChecked(true);
        mDrawerLayout.closeDrawers();


        return true;
    }

//
//    public void loadfanbanner(String fanBannerid){
//        fanadView = new AdView(this,  fanBannerid, AdSize.BANNER_HEIGHT_50);
//
//
//
//        // Find the Ad Container
//        LinearLayout adContainer = (LinearLayout) findViewById(R.id.banner_container);
//
//        // Add the ad view to your activity layout
//        adContainer.addView(fanadView);
//
//        // Request an ad
//        fanadView.loadAd();
//    }




//    private void getAdDetails(String url){
//
//        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
//            @Override
//            public void onResponse(JSONObject response) {
//
//
//
//
//                try {
//                    JSONObject jsonObject=response.getJSONObject("fan");
//
//                    fanadStatus = jsonObject.getString("status");
//                      fanBannerid = jsonObject.getString("fan_banner_ads_id");
//                    fanInterid = jsonObject.getString("fan_interstitial_ads_id");
//
//                    if (fanadStatus.equals("1")){
//                        loadfanbanner(fanBannerid);
//
//                    }
//
//
//                    new GDPRChecker()
//                            .withContext(getContext())
//                            .withPrivacyUrl(Config.TERMS_URL) // your privacy url
//                            .withPublisherIds(ApiResources.adMobPublisherId) // your admob account Publisher id
//                            .withTestMode("9424DF76F06983D1392E609FC074596C") // remove this on real project
//                            .check();
//
//
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//            }
//        }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//
//            }
//        });
//
//        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);
//
//
//    }
private void showDialog() {
    new AppRatingDialog.Builder()
            .setPositiveButtonText("Rate")
            .setNegativeButtonText("Cancel")
            .setNeutralButtonText("Exit")
            .setNoteDescriptions(Arrays.asList("Very Bad", "Not good", "Quite ok", "Very Good", "Excellent !!!"))
            .setDefaultRating(2)
            .setTitle("Rate Our Apps")
            .setDescription("Give 5 Stars to Support This Apps")
            .setCommentInputEnabled(true)
            .setDefaultComment("Best Movie Apps")
            .setStarColor(R.color.colorAccent)

            .setNoteDescriptionTextColor(R.color.colorPrimary)
            .setTitleTextColor(R.color.colorPrimary)
            .setDescriptionTextColor(R.color.colorPrimary)
            .setHint("Please write your comment here ...")
            .setHintTextColor(R.color.grey_400)
            .setCommentTextColor(R.color.grey_400)
            .setCommentBackgroundColor(R.color.colorPrimaryDark)
            .setWindowAnimation(R.style.MyDialogFadeAnimation)
            .setCancelable(false)
            .setCanceledOnTouchOutside(false)
            .create(MainActivity.this)
            .show();
}


    @Override
    public void onPositiveButtonClicked(int rate, String comment) {
        launchMarket();
        // interpret results, send it to analytics etc...
    }

    @Override
    public void onNegativeButtonClicked() {


    }

    @Override
    public void onNeutralButtonClicked() {
        System.exit(0);
        finish();

    }

    private void launchMarket() {
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent myAppLinkToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(myAppLinkToMarket);
        } catch (ActivityNotFoundException e) {
            Toast.makeText(this, " unable to find market app", Toast.LENGTH_LONG).show();
        }
    }
}
