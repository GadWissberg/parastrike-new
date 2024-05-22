package com.gadarts.parashoot.player.sidekick;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.GroundUnit;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 13/10/2015.
 */
public class HeatTurret extends com.gadarts.parashoot.player.SideKick {
    private Array<GroundUnit> targets = new Array<GroundUnit>();
    private TextureRegion beamImage;
    private long myBeamSoundId;

    public HeatTurret() {
        atlas =
            Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.HEAT_TURRET_DATA_FILE);
    }

    @Override
    public void init(float x, float hp, WarScreen warScreen) {
        super.init(x, hp, warScreen);
        beamImage = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BULLET);
    }

    @Override
    public void shoot() {
        //Do nothing.
    }

    @Override
    public boolean setTarget(Enemy target) {
        if (targets.size < Rules.Player.SideKicks.HeatTurret.MAX_TARGETS && !targets.contains((GroundUnit) target, true)) {
            if (targets.size == 0) {
                myBeamSoundId = Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.HEAT_BEAM, true);
            }
            targets.add((GroundUnit) target);
            getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.DAMAGE_SMOKE, target, 0, -target.getHeight() / 2);
            Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.HEAT_BEAM_ON, false);
            return true;
        }
        return false;
    }

    /**
     * In Heat Turret it actually means hasNoRoomForMoreTargets().
     */
    @Override
    public boolean hasTarget() {
        if (targets.size < Rules.Player.SideKicks.HeatTurret.MAX_TARGETS) {
            return false;
        }
        for (int i = 0; i < targets.size; i++) {
            Enemy currentTarget = targets.get(i);
            if (currentTarget.isDead()) {
                targets.removeIndex(i);
                return true;
            }
        }
        return false;
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (isDead()) {
            return;
        }
        if (targets.size > 0) {
            setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT, Rules.Player.SideKicks.HeatTurret.SHOOT_FRAME_DURATION, Animation.PlayMode.LOOP_PINGPONG);
            for (int i = 0; i < targets.size; i++) {
                GroundUnit currentTarget = targets.get(i);
                if (currentTarget.isDead() || currentTarget.isDestroyed() || !currentTarget.hasLanded()) {
                    targets.removeIndex(i);
                    break;
                }
                currentTarget.changeHealth(-attackStrength, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.FLAME, Rules.System.GFX.ShadesIds.RED_SHADE);
            }
        } else {
            setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
            stopBeamSound();
        }
    }

    private void stopBeamSound() {
        if (myBeamSoundId != -1) {
            Parastrike.getSoundPlayer().stopSound(SFX.Player.SideKicks.HEAT_BEAM, myBeamSoundId);
            myBeamSoundId = 0;
        }
    }

    @Override
    protected void onDeath() {
        stopBeamSound();
        super.onDeath();
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (!isDead()) {
            for (int i = 0; i < targets.size; i++) {
                Enemy currentTarget = targets.get(i);
                float headY = y + getHeight() - 15;
                float targetY = currentTarget.getY() - currentTarget.getHeight() / 2;
                float distance = GameUtils.getDistanceBetweenTwoPoints(x, headY, currentTarget.getX(), targetY);
                float directionToTarget = GameUtils.getDirectionToPoint(x, headY, currentTarget.getX(), targetY);
                batch.draw(beamImage, x, headY, beamImage.getRegionWidth() / 2, beamImage.getRegionHeight() / 2, distance, beamImage.getRegionHeight(), 1, 1, directionToTarget);
            }
        }
        super.onDraw(batch);
    }
}
