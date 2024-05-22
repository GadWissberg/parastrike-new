package com.gadarts.parashoot.misc;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.WarScreenElements;

public class IndependentEffect extends DistanceInteractor {

    private static final float MINIMAL_DURATION = 0.5f;
    private long mySoundId;
    private com.gadarts.parashoot.misc.effects.ParticleWrapper myEffect;
    private SFX mySoundFile;

    public void init(float x, float y, String effect, WarScreenElements mechanics) {
        init(x, y, effect, 0, mechanics);
    }

    public void init(float x, float y, String effect, float duration, WarScreenElements mechanics) {
        init(x, y, effect, duration, null, mechanics);
    }

    public void init(float x, float y, String effect, float duration, SFX sound, WarScreenElements mechanics) {
        super.init(null, x, y, 0, 0, false, false, mechanics);
        if (effect != null) {
            setEffect(effect);
        }
        if (duration > 0) {
            scheduleTask(TASK_DISAPPEAR, duration);
        } else if (duration == 0) {
            scheduleTask(TASK_DISAPPEAR, MINIMAL_DURATION);
        }
        mySoundId = -1;
        mySoundFile = sound;
        if (mySoundFile != null) {
            mySoundId = Parastrike.getSoundPlayer().playSound(mySoundFile, true);
        }
    }

    @Override
    public void setDestroyMeFlag(boolean destroyMeFlag) {
        super.setDestroyMeFlag(destroyMeFlag);
        if (destroyMeFlag) {
            if (mySoundId != -1) {
                Parastrike.getSoundPlayer().stopSound(mySoundFile, mySoundId);
            }
        }
    }

    public void setEffect(String effect) {
        myEffect = getElements().getEffectsManager().createEffect(effect, this);
    }

    @SuppressWarnings("unused")
    public com.gadarts.parashoot.misc.effects.ParticleWrapper getEffect() {
        return myEffect;
    }

}
