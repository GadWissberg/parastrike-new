package com.gadarts.parashoot.misc.stuff;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.Rules;

public class AllyFallingBomb extends Misc {
    private long myFallingSoundId;
    private boolean atomic;
    private Factories factories;

    public AllyFallingBomb() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.AirCrafts.ZEPPELIN_DATA_FILE);
    }

    public void init(WarScreenElements mechanics, float x, float y, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.BOMB, x, y, 0, 0, mechanics);
        this.factories = factories;
        fitSpriteToDirection = false;
        gravityAcceleration = Rules.Enemies.AirCrafts.Zeppelin.BOMB_GRAVITY_ACCELERATION;
        gravityStatus = true;
        atomic = false;
        setOrigin(width / 2, height / 2);
        myFallingSoundId = Parastrike.getSoundPlayer().playSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC);
    }

    @Override
    protected void onGroundCollision() {
        super.onGroundCollision();
        MiscFactory miscFactory = factories.getMiscFactory();
        if (atomic) {
            getElements().getEffectsManager().activateAtomicFlash();
            getElements().destroyAllEnemies();
            miscFactory.createIndependentEffect(MiscFactory.IndependentEffectType.ATOMIC_EXPLOSION, 3 * Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 4, Rules.Level.GROUND_Y);
            miscFactory.createIndependentEffect(MiscFactory.IndependentEffectType.ATOMIC_EXPLOSION, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 4, Rules.Level.GROUND_Y);
        } else {
            getElements().getEffectsManager().activateBioHazardSmoke();
        }
        miscFactory.createExplosion(MiscFactory.ExplosionType.BIG_EXPLOSION_UP, x, y);
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.FALLING_BALLISTIC, myFallingSoundId);
        Parastrike.getSoundPlayer().playSound(SFX.Misc.HUGE_EXPLOSION);
        onDestroy();
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        setSpriteDirectionToMovement();
    }

    public void setAtomic(boolean atomic) {
        this.atomic = atomic;
    }
}
