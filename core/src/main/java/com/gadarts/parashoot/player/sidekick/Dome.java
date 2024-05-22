package com.gadarts.parashoot.player.sidekick;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 13/10/2015.
 */
public class Dome extends com.gadarts.parashoot.player.SideKick {

    public Dome() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.DOME_DATA_FILE);
    }

    public void init(float x, int hp, WarScreen warScreen) {
        super.init(x, hp, warScreen);
        allowShootingAirTargets();
    }

    @Override
    public void shoot() {
        if (isGunLoaded() && hasTarget()) {
            setGunState(false);
            Parastrike.getSoundPlayer().playSound(SFX.Player.SideKicks.DOME_LOADING_PLASMA);
            getWarScreen().getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.LOADING_PLASMA, x, y);
            scheduleTask(CREATE_BULLET, Rules.Player.SideKicks.Dome.LOAD_BULLET);
        }
    }

    private final Timer.Task TASK_CHANGE_TO_IDLE = new Timer.Task() {
        @Override
        public void run() {
            if (!dead) {
                setRegion(Assets.GFX.Sheets.ImagesNames.IDLE);
            }
        }
    };

    private final Timer.Task CREATE_BULLET = new Timer.Task() {
        @Override
        public void run() {
            if (target != null && !dead) {
                Bullet energyBall = getWarScreen().getFactories().getBulletFactory().createBullet(BulletType.HOMING_PLASMA, x, y + height, 0);
                energyBall.setDamageValue(attackStrength);
                getWarScreen().getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.DOME_BLAST, x, y + height);
                energyBall.setTarget(target);
                energyBall.setDirection(GameUtils.getDirectionToPoint(x, y + height, target));
                Parastrike.getSoundPlayer().playSound(SFX.Weapons.HOMING_LASER);
                setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT);
                scheduleTask(TASK_RELOAD, shootingRate);
                scheduleTask(TASK_CHANGE_TO_IDLE, Rules.Player.SideKicks.Dome.SHOOTING_IMAGE_SHOWN_DURATION);
            } else {
                TASK_RELOAD.run();
            }
        }
    };
}
