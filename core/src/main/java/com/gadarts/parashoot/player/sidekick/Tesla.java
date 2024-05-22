package com.gadarts.parashoot.player.sidekick;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.misc.IndependentEffect;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Electricity;

/**
 * Created by Gad on 13/10/2015.
 */
public class Tesla extends com.gadarts.parashoot.player.SideKick {
    private Electricity electricity;

    public Tesla() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.TESLA_DATA_FILE);
    }

    @Override
    public void shoot() {
        if (isGunLoaded() && !isDead()) {
            scheduleTask(TASK_ELECTRIFY, Rules.Player.SideKicks.Tesla.ATTACK_DELAY);
            Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.TESLA_LOAD);
            setGunState(false);
            setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT, Rules.Player.SideKicks.Tesla.SHOOT_FRAME_DURATION);
        }
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (electricity != null && !electricity.isDestroyed()) {
            electricity.onUpdate(delta);
        }
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        super.onDraw(batch);
        if (electricity != null && !electricity.isDestroyed()) {
            electricity.onDraw(batch);
        }
    }

    private final Timer.Task TASK_ELECTRIFY = new Timer.Task() {
        @Override
        public void run() {
            if (currentFrame.name.equals(Assets.GFX.Sheets.ImagesNames.SHOOT)) {
                setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
            }
            scheduleTask(TASK_RELOAD, shootingRate);
            if (dead || target == null || (target != null && target.isDead())) {
                return;
            }
            float directionToObject = GameUtils.getDirectionToPoint(x, y + height / 2, target.getX() - (target.getWidth() / 2) + (randomizer.nextFloat() * target.getWidth()), target.getY() - target.getHeight());
            electricity = (Electricity) getWarScreen().getFactories().getMiscFactory().createMisc(MiscFactory.MiscType.ELECTRICITY, x, y + height, directionToObject);
            electricity.stretchToTarget(target);
            target.changeHealth(-getAttackStrength(), Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.ELECTRICITY);
            Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.TESLA_SHOOT);
            IndependentEffect effect = getWarScreen().getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.INDEPENDENT_EFFECT, target.getX(), target.getY());
            effect.setEffect(Assets.Configs.ParticleEffects.ELECTRIC_HIT);
        }
    };
}
