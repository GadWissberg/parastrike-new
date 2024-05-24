package com.gadarts.parashoot.android;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.media.SoundPool;
import android.net.Uri;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.android.AndroidAudio;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.ActionResolver;
import com.gadarts.parashoot.utils.GameSettings;

import java.lang.reflect.Field;
import java.util.HashMap;

/**
 * Implementation of the action resolver.
 */
class ActionResolverAndroid implements ActionResolver {
    /**
     * The context to the Android activity.
     */
    private final AndroidLauncher androidLauncher;

    ActionResolverAndroid(AndroidLauncher androidLauncher) {
        this.androidLauncher = androidLauncher;

    }

    @Override
    public void StartSession() {
    }

    @Override
    public void EndSession() {
    }

    @Override
    public void mail() {
        final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);
        emailIntent.setType("plain/text");
        emailIntent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{Assets.Strings.Menu.Help.MAIL_ADDRESS});
        emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, Assets.Strings.Menu.Help.MAIL_SUBJECT);
        androidLauncher.startActivity(Intent.createChooser(emailIntent, "Contact Me"));
    }

    @Override
    public void toast(String message) {
        androidLauncher.toast(message);
    }

    @Override
    public String getAppVersion() throws Exception {
        PackageInfo pInfo = androidLauncher.getPackageManager().getPackageInfo(androidLauncher.getPackageName(), 0);
        return pInfo.versionName;
    }

    @Override
    public void autoPauseSoundPool() {
        try {
            AndroidAudio audio = (AndroidAudio) Gdx.app.getAudio();
            Field field = audio.getClass().getDeclaredField("soundPool");
            field.setAccessible(true);
            SoundPool soundPool = (SoundPool) field.get(audio);
            soundPool.autoPause();
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }



    @Override
    public void create() {

    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void render() {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void dispose() {

    }
}
