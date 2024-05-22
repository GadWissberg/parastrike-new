package com.gadarts.parashoot.enemies.paratroopers;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * A paratrooper with bazooka.
 */
public class BazookaGuy extends Paratrooper {

    public BazookaGuy() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.GroundUnits.Paratroopers.BAZOOKA_DATA_FILE);
        setReloadTime(Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.RELOAD_TIME);
        setAttackDamage(getDamageBySkill(0));
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param elements      The war screen's elements.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, WarScreenElements elements, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.SINK, x, y, getHealthBySkill(), Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.GIB_REACH, elements, playerHandler, factories);
    }

    /**
     * Completely overridden method. Shoots if it has a targets.
     */
    @Override
    protected void shoot() {
        if (hasTarget()) {
            scheduleTask(TASK_SHOOT, reloadTime);
        }
    }

    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Medium.COINS;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Hard.COINS;
        }
    }


    /**
     * Launches a missile.
     */
    private final Timer.Task TASK_SHOOT = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && hasLanded() && target != null && !target.isDead()) {
                setFacingSprite(Assets.GFX.Sheets.ImagesNames.SHOOT_LEFT, Assets.GFX.Sheets.ImagesNames.SHOOT_RIGHT, Rules.Enemies.GeneralAttributes.SHOOT_FRAME_DURATION);
                scheduleTask(TASK_STOP_SHOOTING, Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.SHOOT_TIME);
                float missileDirection = 0;
                if (!isOnLeftSide()) {
                    missileDirection = 180;
                }
                Bullet bullet = getFactories().getBulletFactory().createBullet(BulletType.BAZOOKA_MISSILE, x, y + Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.MISSILE_Y_ORIGIN, missileDirection);
                bullet.setDamageValue(attackDamage);
                Parastrike.getSoundPlayer().playSound(SFX.Weapons.MISSILE_LAUNCH);
            }
        }
    };

    @Override
    public float getSpeedBySkill() {
        return 0;
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Medium.HP;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Hard.HP;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Easy.ATTACK_DAMAGE;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Medium.ATTACK_DAMAGE;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Bazooka.Hard.ATTACK_DAMAGE;
        }
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }
}
