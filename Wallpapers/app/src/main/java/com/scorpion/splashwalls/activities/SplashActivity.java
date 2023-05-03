package com.scorpion.splashwalls.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatActivity;

import com.google.ads.consent.ConsentForm;
import com.google.ads.consent.ConsentFormListener;
import com.google.ads.consent.ConsentInfoUpdateListener;
import com.google.ads.consent.ConsentInformation;
import com.google.ads.consent.ConsentStatus;
import com.scorpion.splashwalls.R;

import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    Context context;
    int failedToLoadCount = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        context = this;
        askUserConsent();
    }

    void askUserConsent() {

        final ConsentInformation consentInformation = ConsentInformation.getInstance(context);
        String[] publisherIds = {"pub-4938515760572795"};

        consentInformation.requestConsentInfoUpdate(publisherIds, new ConsentInfoUpdateListener() {
            @Override
            public void onConsentInfoUpdated(ConsentStatus consentStatus) {

                if ((consentStatus == ConsentStatus.UNKNOWN || consentStatus == ConsentStatus.NON_PERSONALIZED) && consentInformation.isRequestLocationInEeaOrUnknown()) {
                    displayConsentForm();
                } else {

                    openMainActivity();

                }
                // TODO: 26-06-2020 consent status is here


            }

            @Override
            public void onFailedToUpdateConsentInfo(String errorDescription) {
                if (failedToLoadCount < 5) {
                    failedToLoadCount += 1;
                    askUserConsent();
                } else {
                    consentInformation.setConsentStatus(ConsentStatus.NON_PERSONALIZED);
                    openMainActivity();

                }
            }
        });

    }

    private ConsentForm consentForm;

    private void displayConsentForm() {

        consentForm = new ConsentForm.Builder(context, getAppsPrivacyPolicy())
                .withListener(new ConsentFormListener() {
                    @Override
                    public void onConsentFormLoaded() {
                        consentForm.show();
                    }

                    @Override
                    public void onConsentFormOpened() {

                    }

                    @Override
                    public void onConsentFormClosed(
                            ConsentStatus consentStatus, Boolean userPrefersAdFree) {

                        if (consentStatus == ConsentStatus.UNKNOWN) {
                            displayConsentForm();
                        } else {
                            openMainActivity();
                        }

                        // TODO: 26-06-2020 if consent is still unkown then again ask for consent
                        //  form  else open main activity here

                        Log.d("Consent", "Status : " + consentStatus);
                    }

                    @Override
                    public void onConsentFormError(String errorDescription) {
                        // Consent form error.
                        Log.e("Error", errorDescription);

                    }
                })
                .withPersonalizedAdsOption()
                .withNonPersonalizedAdsOption()
                .build();
        consentForm.load();
    }

    private URL getAppsPrivacyPolicy() {
        URL mUrl = null;
        try {
            mUrl = new URL("https://sites.google.com/view/vikalp-similarappsuggestions/home");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return mUrl;
    }

    void openMainActivity() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
                finish();
            }
        },2300);
    }
}
