package com.nonton.xx1;

import android.Manifest;
import android.content.Intent;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.ixidev.gdpr.GDPRChecker;
import com.nonton.xx1.R;
import com.nonton.xx1.utl.ApiResources;
import com.startapp.android.publish.adsCommon.AutoInterstitialPreferences;
import com.startapp.android.publish.adsCommon.StartAppAd;
import com.startapp.android.publish.adsCommon.StartAppSDK;
import com.startapp.android.publish.adsCommon.adListeners.AdDisplayListener;

import org.json.JSONException;
import org.json.JSONObject;

import static com.nonton.xx1.utl.MyAppClass.getContext;


public class SplashscreenActivity extends AppCompatActivity {

    private InterstitialAd finterstitialAd;
    private com.google.android.gms.ads.InterstitialAd mInterstitialAd;

    private final String TAG = MainActivity.class.getSimpleName();

    private int SPLASH_TIME = 2000;
    private SharedPreferences preferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 2909);
            } else {
            }
        } else {
        }

        //----dark mode----------
        preferences=getSharedPreferences("push",MODE_PRIVATE);
        if (preferences.getBoolean("dark",false)){
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }else {
            AppCompatDelegate
                    .setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        }


        setContentView(R.layout.activity_splashscreen);

        getSupportActionBar().hide();



        getAdDetails(new ApiResources().getAdDetails());


//        Thread timer = new Thread() {
//            public void run() {
//                try {
//                    sleep(SPLASH_TIME);
//                } catch (Exception e) {
//                    e.printStackTrace();
//                } finally {
//                    startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
//                    finish();
//
//                }
//            }
//        };
//        timer.start();

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

                    loadinterads(ApiResources.faninterid);

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

                try {
                    JSONObject jsonObject=response.getJSONObject("startapp");
                    ApiResources.startappid = jsonObject.getString("startappid");
                    ApiResources.startappstatus = jsonObject.getString("startappstatus");

                    StartAppSDK.init(getContext(), ApiResources.startappid, true);
                    StartAppSDK.setUserConsent (getContext(),
                            "pas",
                            System.currentTimeMillis(),
                            true);
                    StartAppAd.setAutoInterstitialPreferences(
                            new AutoInterstitialPreferences()
                                    .setSecondsBetweenAds(300)
                    );

                } catch (JSONException e) {
                    Log.e("json", "ERROR");


                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });

        Volley.newRequestQueue(getContext()).add(jsonObjectRequest);


    }


    public void loadinterads(final String interad){

        finterstitialAd = new InterstitialAd(this, interad);
        // Set listeners for the Interstitial Ad
        finterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
                // Interstitial ad displayed callback
                Log.e(TAG, "Interstitial ad displayed.");
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
                    finish();
                // Interstitial dismissed callback
                Log.e(TAG, "Interstitial ad dismissed.");
            }

            @Override
            public void onError(Ad ad, AdError adError) {
                loadinteradmob(ApiResources.adMobInterstitialId);

                // Ad error callback
                Log.e(TAG, "Interstitial ad failed to load: " + adError.getErrorMessage());
            }

            @Override
            public void onAdLoaded(Ad ad) {
                ProgressBar progressBar =findViewById(R.id.progressbar1);
                progressBar.setVisibility(View.GONE);
                Button button =findViewById(R.id.startbut);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        finterstitialAd.show();
                    }
                });
                // Interstitial ad is loaded and ready to be displayed
                Log.d(TAG, "Interstitial ad is loaded and ready to be displayed!");
                // Show the ad
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
                Log.d(TAG, "Interstitial ad clicked!");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // Ad impression logged callback
                Log.d(TAG, "Interstitial ad impression logged!");
            }
        });

        // For auto play video ads, it's recommended to load the ad
        // at least 30 seconds before it is shown
        finterstitialAd.loadAd();


    }

    public void loadinteradmob(String inter){

        mInterstitialAd = new com.google.android.gms.ads.InterstitialAd(this);
        mInterstitialAd.setAdUnitId(inter);
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                ProgressBar progressBar =findViewById(R.id.progressbar1);
                progressBar.setVisibility(View.GONE);

                Button button =findViewById(R.id.startbut);
                button.setVisibility(View.VISIBLE);
                button.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mInterstitialAd.show();
                    }
                });
                // Code to be executed when an ad finishes loading.
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {

                StartAppAd startAppAd = new StartAppAd(getContext());
                startAppAd.showAd(new AdDisplayListener() {
                    @Override
                    public void adHidden(com.startapp.android.publish.adsCommon.Ad ad) {
                        startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
                        finish();
                    }

                    @Override
                    public void adDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                    }

                    @Override
                    public void adClicked(com.startapp.android.publish.adsCommon.Ad ad) {
                        startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
                        finish();

                    }

                    @Override
                    public void adNotDisplayed(com.startapp.android.publish.adsCommon.Ad ad) {

                        startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
                        finish();

                    }
                })   ;





                // Code to be executed when an ad request fails.
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
            }

            @Override
            public void onAdClicked() {

                // Code to be executed when the user clicks on an ad.
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                startActivity(new Intent(SplashscreenActivity.this,MainActivity.class));
                finish();
                // Code to be executed when the interstitial ad is closed.
            }
        });
    }


}
