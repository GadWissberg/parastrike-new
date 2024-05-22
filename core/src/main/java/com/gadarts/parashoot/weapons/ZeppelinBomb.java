package com.gadarts.parashoot.weapons;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 27/01/2016.
 */
public class ZeppelinBomb extends Bullet {

    private long myFallingSoundId;

    public ZeppelinBomb() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.ZEPPELIN_DATA_FILE);
    }

    public void init(float x, float y, float direction, WarScreenElements mechanics, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.BOMB, x, y, direction, 0, mechanics, factories);
        initializeFields();
        setExplosion(MiscFactory.ExplosionType.HUGE_EXPLOSION, SFX.Misc.BIG_EXPLOSION);
    }

    private void initializeFields() {
        gravityStatus = true;
        gravityAcceleration = Rules.Enemies.AirCrafts.Zeppelin.BOMB_GRAVITY_ACCELERATION;
        maxFallingSpeed = Rules.Enemies.AirCrafts.Zeppelin.BOMB_MAX_FALLING_SPEED;
        myFallingSoundId = Parastrike.getSoundPlayer().playSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC);
        fitSpriteToDirection = true;
    }

    @Override
    protected void createLight() {
        //Do nothing.
    }

    @Override
    public void onDestroy(boolean doDestroySequence) {
        super.onDestroy(doDestroySequence);
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC, myFallingSoundId);
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (speed > 0) {
            setSpriteDirectionToMovement();
        }
    }
}
