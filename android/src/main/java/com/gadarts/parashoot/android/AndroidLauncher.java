package com.gadarts.parashoot.android;

import android.content.Intent;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.android.aidl.com.example.android.trivialdrivesample.util.IabHelper;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.utils.GameSettings;

/**
 * Launches the Android application.
 */
public class AndroidLauncher extends AndroidApplication {

    /**
     * Handler for the IAB.
     */
    private IabHelper iabHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = createAppConfig();
        ActionResolverAndroid actionResolver = new ActionResolverAndroid(this);
        final Parastrike main = new Parastrike(actionResolver);
        initializeIab();
        initialize(main, config);
        InitializeAdMob(config, main);
    }

    private void InitializeAdMob(AndroidApplicationConfiguration config, Parastrike main) {
        RelativeLayout layout = new RelativeLayout(this);
        View gameView = initializeForView(main, config);
        layout.addView(gameView);
        setContentView(layout);
    }


    /**
     * Initializes In-App Billing.
     */
    private void initializeIab() {
        iabHandler = new IabHelper(this, Assets.Strings.PUBLIC_KEY);
        iabHandler.startSetup(null);
        iabHandler.enableDebugLogging(GameSettings.ALLOW_IAP_DEBUG);
    }

    /**
     * Sets the application android config.
     *
     * @return The config object.
     */
    @NonNull
    private AndroidApplicationConfiguration createAppConfig() {
        AndroidApplicationConfiguration config = new AndroidApplicationConfiguration();
        setAppUseConfig(config);
        return config;
    }

    /**
     * Sets the app to use device's features.
     *
     */
    private void setAppUseConfig(AndroidApplicationConfiguration config) {
        config.useAccelerometer = false;
        config.useCompass = false;
        config.useWakelock = true;
        config.useImmersiveMode = true;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        try {
            iabHandler.dispose();
        } catch (IabHelper.IabAsyncInProgressException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        iabHandler.handleActivityResult(requestCode, resultCode, data);
    }

    /**
     * Creates a toast. This is suitable for all kinds of threads.
     *
     * @param text The message.
     */
    public void toast(final String text) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show();
        } else {
            toastForWorkerThread(text);
        }
    }

    /**
     * Creates a toast on the UI thread.
     *
     * @param text The message to show.
     */
    private void toastForWorkerThread(final String text) {
        runOnUiThread(() -> Toast.makeText(getBaseContext(), text, Toast.LENGTH_LONG).show());
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

}
