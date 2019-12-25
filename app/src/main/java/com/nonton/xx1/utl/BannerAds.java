package com.nonton.xx1.utl;

import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;

import com.google.ads.mediation.admob.AdMobAdapter;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdSize;
import com.google.android.gms.ads.AdView;
import com.ixidev.gdpr.GDPRChecker;

public class BannerAds {
    public static void ShowBannerAds(Context context, RelativeLayout mAdViewLayout) {



        AdView mAdView = new AdView(context);
        mAdView.setAdSize(AdSize.BANNER);
        mAdView.setAdUnitId(ApiResources.adMobBannerId);
        AdRequest.Builder builder = new AdRequest.Builder();
        GDPRChecker.Request request = GDPRChecker.getRequest();

        if (request == GDPRChecker.Request.NON_PERSONALIZED) {
            // load non Personalized ads
            Bundle extras = new Bundle();
            extras.putString("npa", "1");
            builder.addNetworkExtrasBundle(AdMobAdapter.class, extras);
        } // else do nothing , it will load PERSONALIZED ads
        mAdView.loadAd(builder.build());
        mAdViewLayout.addView(mAdView);
        }


    public static void ShowFanBannerAds(Context context, RelativeLayout FanAdViewLayout) {


        com.facebook.ads.AdView fanadView = new com.facebook.ads.AdView(context,  ApiResources.fanBannerid , com.facebook.ads.AdSize.BANNER_HEIGHT_50);

        // Add the ad view to your activity layout
        FanAdViewLayout.addView(fanadView);

        // Request an ad
        fanadView.loadAd();



    }
}
