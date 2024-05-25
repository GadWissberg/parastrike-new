package com.gadarts.parashoot.android;

import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.badlogic.gdx.backends.android.AndroidApplication;
import com.badlogic.gdx.backends.android.AndroidApplicationConfiguration;
import com.gadarts.parashoot.Parastrike;

/**
 * Launches the Android application.
 */
public class AndroidLauncher extends AndroidApplication {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AndroidApplicationConfiguration config = createAppConfig();
        ActionResolverAndroid actionResolver = new ActionResolverAndroid(this);
        final Parastrike main = new Parastrike(actionResolver);
        initialize(main, config);
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
