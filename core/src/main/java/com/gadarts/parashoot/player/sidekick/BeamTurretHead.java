package com.gadarts.parashoot.player.sidekick;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 23/10/2015.
 */
public class BeamTurretHead extends SideKickHead {
    private boolean showBeam;
    private float distanceToTarget;
    private TextureAtlas.AtlasRegion beamImage;

    public BeamTurretHead() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.BEAM_TURRET_DATA_FILE);
        beamImage = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BULLET);
    }

    public void init(float x, float y, WarScreenElements mechanics, Factories factories) {
        super.init(x, y, mechanics, factories);
        stepsAhead = 0;
    }

    @Override
    protected void shoot() {
        showBeam = true;
        Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.BEAM_TURRET_SHOOT);
        body.setGunState(false);
        scheduleTask(TASK_HIDE_BEAM, Rules.Player.SideKicks.Phantom.SHOOTING_IMAGE_SHOWN_DURATION);
        setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT);
        scheduleTask(body.TASK_RELOAD, body.getShootingRate());
        distanceToTarget = GameUtils.getDistanceBetweenTwoObjects(this, target);
        MiscFactory miscFactory = getFactories().getMiscFactory();
        miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, target.getX(), target.getY());
        miscFactory.createIndependentEffect(MiscFactory.IndependentEffectType.FIRE, target.getX(), target.getY());
        target.changeHealth(-body.calculateAttack());
    }

    private final Timer.Task TASK_HIDE_BEAM = new Timer.Task() {
        @Override
        public void run() {
            showBeam = false;
            setRegion(Assets.GFX.Sheets.ImagesNames.HEAD, Rules.Player.SideKicks.Phantom.SHOOTING_IMAGE_SHOWN_DURATION);
        }
    };

    @Override
    public void onDraw(SpriteBatch batch) {
        if (showBeam) {
            batch.draw(beamImage, x, y - 5, 0, beamImage.getRegionHeight() / 2, distanceToTarget, beamImage.getRegionHeight(), 1, 1, direction);
        }
        super.onDraw(batch);
    }
}
