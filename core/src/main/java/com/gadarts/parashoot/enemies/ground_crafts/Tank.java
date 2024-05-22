package com.gadarts.parashoot.enemies.ground_crafts;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.GroundCraft;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.utils.Rules;

/**
 * The tank enemy.
 */
public class Tank extends GroundCraft implements Targeting<PlayerCharacter> {

    public Tank() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.GroundUnits.GroundCrafts.TANK_DATA_FILE);
        motorSoundFileName = SFX.Enemies.GroundUnits.GroundCrafts.VEHICLE_MOVE;
        attackDamage = getDamageBySkill(0);
        customMaskWidth = Rules.Enemies.GroundUnits.GroundCrafts.Tank.MASK_WIDTH;
    }

    /**
     * Poolable initialization method. Orders the tank to move towards the player.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     The tank's direction.
     * @param elements     The war screen's elements.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, float direction, WarScreenElements elements, PlayerHandler playerHandler, Factories factories) {
        super.init(x, y, direction, getHealthBySkill(), elements, playerHandler, factories);
        if (direction == 0) {
            this.order = new Order(Rules.Enemies.GroundUnits.GroundCrafts.LEFT_SIDE_DESTINATION_X_FAREST + (randomizer.nextFloat() * Rules.Enemies.GroundUnits.GroundCrafts.LEFT_SIDE_DESTINATION_X_CLOSEST), TASK_SHOOT, Rules.Enemies.GroundUnits.GroundCrafts.Tank.RELOAD_TIME);
        } else {
            this.order = new Order(Rules.Enemies.GroundUnits.GroundCrafts.RIGHT_SIDE_DESTINATION_X_FAREST - (randomizer.nextFloat() * Rules.Enemies.GroundUnits.GroundCrafts.RIGHT_SIDE_DESTINATION_X_CLOSEST), TASK_SHOOT, Rules.Enemies.GroundUnits.GroundCrafts.Tank.RELOAD_TIME);
        }
    }

    /**
     * Completely overridden method. Actually does exactly the same, just with some position fix for the smoke.
     */
    @Override
    protected void createDamageSmoke() {
        if (myDamageSmoke == null) {
            myDamageSmoke = getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.DAMAGE_SMOKE, this, width / 2 - Rules.Enemies.GroundUnits.GroundCrafts.Tank.SMOKE_X_OFFSET, 0);
        }
    }


    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Medium.COINS;
        }
        return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Hard.COINS;
    }

    private final Timer.Task TASK_SHOOT = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && target != null && !target.isDead()) {
                setRegion(Assets.GFX.Sheets.ImagesNames.SHOOT);
                scheduleTask(TASK_CHANGE_TO_IDLE, Rules.Enemies.GroundUnits.GroundCrafts.Tank.SHOOT_TIME);
                scheduleTask(TASK_SHOOT, Rules.Enemies.GroundUnits.GroundCrafts.Tank.RELOAD_TIME);
                target.changeHealth(-attackDamage);
                int numberOfParts = randomizer.nextInt(3) + 2;
                float randomX = target.getX() + randomizer.nextInt((int) target.getWidth()) - target.getOriginX();
                float randomY = target.getY() + randomizer.nextInt((int) target.getHeight()) - target.getOriginY();
                MiscFactory miscFactory = getFactories().getMiscFactory();
                miscFactory.createFlyingParts(MiscFactory.MiscType.FLYING_PART, randomX, randomY, numberOfParts);
                miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, randomX, randomY);
                Parastrike.getSoundPlayer().playSound(SFX.Enemies.GroundUnits.GroundCrafts.TANK_SHOOT);
            }
        }
    };

    @Override
    public float getSpeedBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Easy.SPEED;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Medium.SPEED;
        } else {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Hard.SPEED;
        }
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Medium.HP;
        } else {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Hard.HP;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Easy.ATTACK_DAMAGE;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Medium.ATTACK_DAMAGE;
        } else {
            return Rules.Enemies.GroundUnits.GroundCrafts.Tank.Hard.ATTACK_DAMAGE;
        }
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }
}
