package com.gadarts.parashoot.enemies.ground_crafts;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.GroundCraft;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;
import com.gadarts.parashoot.utils.Rules;

/**
 * The APC enemy.
 */
public class APC extends GroundCraft {

    public APC() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.GroundUnits.GroundCrafts.APC_DATA_FILE);
    }

    /**
     * Poolable initialization method. Orders the APC to move towards the player.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param direction     The APC's direction.
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, float direction, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(x, y, direction, getHealthBySkill(), mechanics, playerHandler, factories);
        if (direction == 0) {
            this.order = new Order(Rules.Enemies.GroundUnits.GroundCrafts.LEFT_SIDE_DESTINATION_X_FAREST + (randomizer.nextFloat() * Rules.Enemies.GroundUnits.GroundCrafts.LEFT_SIDE_DESTINATION_X_CLOSEST), TASK_RELEASE_SOLDIERS, Rules.Enemies.GroundUnits.GroundCrafts.APC.TIME_RELEASE);
        } else {
            this.order = new Order(Rules.Enemies.GroundUnits.GroundCrafts.RIGHT_SIDE_DESTINATION_X_FAREST - (randomizer.nextFloat() * Rules.Enemies.GroundUnits.GroundCrafts.RIGHT_SIDE_DESTINATION_X_CLOSEST), TASK_RELEASE_SOLDIERS, Rules.Enemies.GroundUnits.GroundCrafts.APC.TIME_RELEASE);
        }
    }

    /**
     * Creates the soldiers and moves away.
     */
    private final Timer.Task TASK_RELEASE_SOLDIERS = new Timer.Task() {
        @Override
        public void run() {
            if (dead) {
                return;
            }
            for (int i = -1; i < Rules.Enemies.GroundUnits.GroundCrafts.APC.NUMBER_OF_PARATROOPERS_TO_RELEASE - 1; i++) {
                Paratrooper infantry = (Paratrooper) getFactories().getEnemyFactory().createEnemy(EnemyFactory.EnemyType.INFANTRY, x - (width / 4) * i, y);
                if (infantry == null) {
                    break;
                }
                infantry.destroyParachute();
                infantry.land();
            }
            setDirection(direction + 180);
            order = new Order(beginX);
            setFrameDuration(Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
        }
    };

    @Override
    public float getSpeedBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.APC.Easy.SPEED;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.APC.Medium.SPEED;
        }
        return Rules.Enemies.GroundUnits.GroundCrafts.APC.Hard.SPEED;
    }


    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.APC.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.APC.Medium.COINS;
        }
        return Rules.Enemies.GroundUnits.GroundCrafts.APC.Hard.COINS;
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.GroundCrafts.APC.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.GroundCrafts.APC.Medium.HP;
        }
        return Rules.Enemies.GroundUnits.GroundCrafts.APC.Hard.HP;
    }

    @Override
    public float getDamageBySkill(int weapon) {
        return 0;
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }
}
