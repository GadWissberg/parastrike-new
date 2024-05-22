package com.gadarts.parashoot.player.sidekick;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 13/11/2015.
 */
public class JaguarTurret extends SideKickHead {
    private boolean overHeated;
    private int heatPoints;

    public JaguarTurret() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.JAGUAR_DATA_FILE);
        setBulletType(BulletType.JAGUAR_BULLET);
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (heatPoints > 0) {
            heatPoints--;
        }
    }

    @Override
    public void aimToTargetAndShoot() {
        float stepX = (float) (Math.cos(Math.toRadians(target.getDirection())) * target.getSpeed());
        float stepY = (float) (Math.sin(Math.toRadians(target.getDirection())) * target.getSpeed());
        float directionToObject = GameUtils.getDirectionToPoint(x, y, target.getCenterX() + stepsAhead * stepX / 2, target.getCenterY() + stepsAhead * stepY / 2);
        if (directionToObject <= direction + 2 && directionToObject >= direction - 2) {
            if (body.isGunLoaded()) {
                shoot();
            }
        } else {
            if (direction < directionToObject) {
                if (Math.abs(direction - directionToObject) < 180) {
                    setDirection(direction + 1);
                } else {
                    setDirection(direction - 1);
                }
            } else {
                if (Math.abs(direction - directionToObject) < 180) {
                    setDirection(direction - 1);
                } else {
                    setDirection(direction + 1);
                }
            }
        }
    }

    protected boolean manageOverHeat() {
        if (overHeated) {
            return true;
        } else {
            heatPoints += Rules.Player.SideKicks.Minigunner.HEAT_POINTS_INCREASE;
            if (heatPoints >= Rules.Player.SideKicks.Minigunner.OVER_HEAT) {
                overHeated = true;
                scheduleTask(TASK_TURN_OFF_OVERHEAT, Rules.Player.SideKicks.Minigunner.OVER_HEAT_TIME);
                Parastrike.getSoundPlayer().playSound(SFX.Weapons.OVER_HEAT);
                getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.WHITE_SMALL_SMOKE_UP, x, y);
                return true;
            }
        }
        return false;
    }

    @Override
    protected void shoot() {
        if (manageOverHeat()) {
            return;
        }
        createBullet();
        Parastrike.getSoundPlayer().playSound(body.getShootingSoundFileName());
        body.setGunState(false);
        scheduleTask(body.TASK_RELOAD, body.getShootingRate());
    }

    private final Timer.Task TASK_TURN_OFF_OVERHEAT = new Timer.Task() {
        @Override
        public void run() {
            overHeated = false;
        }
    };


}
