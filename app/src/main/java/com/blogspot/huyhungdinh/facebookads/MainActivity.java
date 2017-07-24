package com.blogspot.huyhungdinh.facebookads;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.ads.Ad;
import com.facebook.ads.AdChoicesView;
import com.facebook.ads.AdError;
import com.facebook.ads.AdSize;
import com.facebook.ads.AdView;
import com.facebook.ads.InterstitialAd;
import com.facebook.ads.InterstitialAdListener;
import com.facebook.ads.MediaView;
import com.facebook.ads.NativeAd;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private LinearLayout llNative;
    private NativeAd fbNative;

    private Button btnInter;
    private InterstitialAd fbInterstitialAd;

    private LinearLayout llBanner;
    private AdView fbAdView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnInter = (Button) findViewById(R.id.btnInter);
        btnInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (fbInterstitialAd != null && fbInterstitialAd.isAdLoaded()) {
                    fbInterstitialAd.show();
                } else {
                    showToast("onError!");
                }
            }
        });

        initInterFb();
        initBannerFb();
        initNativeFb();
    }

    private void initInterFb() {
        fbInterstitialAd = new com.facebook.ads.InterstitialAd(this, "YOUR_PLACEMENT_ID");
        // Set listeners for the Interstitial Ad
        fbInterstitialAd.setAdListener(new InterstitialAdListener() {
            @Override
            public void onInterstitialDisplayed(Ad ad) {
            }

            @Override
            public void onInterstitialDismissed(Ad ad) {
                fbInterstitialAd.loadAd();
            }

            @Override
            public void onError(Ad ad, AdError adError) {
            }

            @Override
            public void onAdLoaded(Ad ad) {

            }

            @Override
            public void onAdClicked(Ad ad) {

            }

            @Override
            public void onLoggingImpression(Ad ad) {

            }
        });
        fbInterstitialAd.loadAd();
    }

    private void initBannerFb() {
        llBanner = (LinearLayout) findViewById(R.id.llBanner);
        // Instantiate an AdView view
        fbAdView = new com.facebook.ads.AdView(this, "YOUR_PLACEMENT_ID", AdSize.BANNER_HEIGHT_50);
        fbAdView.setAdListener(new com.facebook.ads.AdListener() {
            @Override
            public void onError(Ad ad, AdError adError) {
                showToast("Banner: onError");
            }

            @Override
            public void onAdLoaded(Ad ad) {
                llBanner.removeAllViews();
                llBanner.addView(fbAdView);
                showToast("Banner: onAdLoaded");
            }

            @Override
            public void onAdClicked(Ad ad) {
                showToast("Banner: onAdClicked");
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                showToast("Banner: onLoggingImpression");
            }
        });
        // Request an ad
        fbAdView.loadAd();
    }

    private void initNativeFb() {
        llNative = (LinearLayout) findViewById(R.id.llNative);
        fbNative = new NativeAd(this, "YOUR_PLACEMENT_ID");
        fbNative.setAdListener(new com.facebook.ads.AdListener() {

            @Override
            public void onError(Ad ad, AdError error) {
                // Ad error callback
            }

            @Override
            public void onAdLoaded(Ad ad) {
                // Add the Ad view into the ad container.
                LayoutInflater inflater = LayoutInflater.from(MainActivity.this);
                LinearLayout adViewFb = (LinearLayout) inflater.inflate(R.layout.native_ad_layout, llNative, false);
                llNative.removeAllViews();
                llNative.addView(adViewFb);

                // Create native UI using the ad metadata.
                ImageView nativeAdIcon = (ImageView) adViewFb.findViewById(R.id.native_ad_icon);
                TextView nativeAdTitle = (TextView) adViewFb.findViewById(R.id.native_ad_title);
                MediaView nativeAdMedia = (MediaView) adViewFb.findViewById(R.id.native_ad_media);
                TextView nativeAdSocialContext = (TextView) adViewFb.findViewById(R.id.native_ad_social_context);
                TextView nativeAdBody = (TextView) adViewFb.findViewById(R.id.native_ad_body);
                Button nativeAdCallToAction = (Button) adViewFb.findViewById(R.id.native_ad_call_to_action);

                // Set the Text.
                nativeAdTitle.setText(fbNative.getAdTitle());
                nativeAdSocialContext.setText(fbNative.getAdSocialContext());
                nativeAdBody.setText(fbNative.getAdBody());
                nativeAdCallToAction.setText(fbNative.getAdCallToAction());

                // Download and display the ad icon.
                NativeAd.Image adIcon = fbNative.getAdIcon();
                NativeAd.downloadAndDisplayImage(adIcon, nativeAdIcon);

                // Download and display the cover image.
                nativeAdMedia.setNativeAd(fbNative);

                // Add the AdChoices icon
                LinearLayout adChoicesContainer = (LinearLayout) adViewFb.findViewById(R.id.ad_choices_container);
                AdChoicesView adChoicesView = new AdChoicesView(MainActivity.this, fbNative, true);
                adChoicesContainer.addView(adChoicesView);

                // Register the Title and CTA button to listen for clicks.
                List<View> clickableViews = new ArrayList<>();
                clickableViews.add(nativeAdTitle);
                clickableViews.add(nativeAdCallToAction);
                fbNative.registerViewForInteraction(llNative, clickableViews);
            }

            @Override
            public void onAdClicked(Ad ad) {
                // Ad clicked callback
            }

            @Override
            public void onLoggingImpression(Ad ad) {
                // On logging impression callback
            }
        });

        // Request an ad
        fbNative.loadAd();
    }

    private void showToast(String message) {
        Log.d("myLog", message);
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
}
