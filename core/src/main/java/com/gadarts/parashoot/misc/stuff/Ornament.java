package com.gadarts.parashoot.misc.stuff;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.utils.TiledDrawable;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 14/08/2017.
 */

public class Ornament extends Misc {
    private TiledDrawable tile;
    private SFX sound;

    public void init(TextureAtlas atlas, String region, int x, int y, WarScreenElements elements, float speed, float animationSpeed, SFX sound, boolean facingDirection) {
        initializeFields(atlas, sound);
        x = handleFillingOrnament(x);
        if (facingDirection) handleDirection(x, speed);
        super.init(region, x, y, getDirection(), speed, false, false, elements);
        handleDirectionOriginAnimationSpeed(animationSpeed);
        beginSoundChainingForStandingOrnament(sound);
    }

    private void beginSoundChainingForStandingOrnament(SFX sound) {
        if (getSpeed() == 0 && sound != null) {
            scheduleTask(TASK_PLAY_SOUND, (float) (Rules.Level.Scenes.PLAY_SOUND_MINIMUM_INTERVAL + Rules.Level.Scenes.PLAY_SOUND_MINIMUM_INTERVAL * Math.random()));
        }
    }

    private void handleDirectionOriginAnimationSpeed(float animationSpeed) {
        faceRegion();
        setOrigin(0, 0);
        setFrameDuration(animationSpeed);
    }

    private final Timer.Task TASK_PLAY_SOUND = new Timer.Task() {
        @Override
        public void run() {
            if (getSpeed() == 0 && sound != null) {
                Parastrike.getSoundPlayer().playSound(sound);
                scheduleTask(TASK_PLAY_SOUND, (float) (Rules.Level.Scenes.PLAY_SOUND_MINIMUM_INTERVAL + Rules.Level.Scenes.PLAY_SOUND_MINIMUM_INTERVAL * Math.random()));
            }
        }
    };

    private void initializeFields(TextureAtlas atlas, SFX sound) {
        this.atlas = atlas;
        this.sound = sound;
    }

    @Override
    public void setBeenInside(boolean beenInside) {
        super.setBeenInside(beenInside);
        if (beenInside && getSpeed() != 0) {
            Parastrike.getSoundPlayer().playSound(sound);
        }
    }

    private void handleDirection(int x, float speed) {
        if (speed != 0) {
            setDirection(x >= 0 ? 180 : 0);
        }

    }


    private int handleFillingOrnament(int x) {
        if (x == -1) {
            tile = new TiledDrawable();
            x = 0;
        }
        return x;
    }

    @Override
    public void onOutside(Level currentLevel) {
        super.onOutside(currentLevel);
    }

    @Override
    protected boolean setRegion(String name, float frameDuration, Animation.PlayMode playMode) {
        boolean result = super.setRegion(name, frameDuration, playMode);
        if (result && tile != null) {
            tile.setRegion(currentFrame);
        }
        return result;
    }

    @Override
    public void reset() {
        super.reset();
        tile = null;
        startingOutside = true;
        setDirection(0);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (tile == null) {
            super.onDraw(batch);
        } else {
            tile.draw(batch, x, y, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, height);
        }
    }

}
