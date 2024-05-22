package com.gadarts.parashoot.enemies.aircrafts;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.Order;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.utils.Rules;

import java.util.ArrayList;

import static com.gadarts.parashoot.model.object_factories.MiscFactory.ExplosionType.SHOCK_WAVE_BLAST;

/**
 * The Boss.
 */
public class Boss extends AirCraft {

    private ArrayList<BossCannon> cannons = new ArrayList();

    public Boss() {
        super();
        setShieldActive(true);
        constructionFieldsInitialization();
    }

    /**
     * Initializes Boss's fields on it's object creation.
     */
    private void constructionFieldsInitialization() {
        String bossDataFile = Assets.GFX.Sheets.InGame.Enemies.AirCrafts.BOSS_DATA_FILE;
        atlas = Parastrike.getAssetsManager().get(bossDataFile);
    }

    /**
     * The boss's body cannot be tracked by homing weapons.
     */
    @Override
    public boolean isHomeable() {
        return false;
    }

    /**
     * Poolable initialization method.
     *
     * @param mechanics     The war screen's mechanics.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.BOSS_BODY, 0, 0, 270, getSpeedBySkill(), getHealthBySkill(), mechanics, playerHandler, factories);
        int destinationX = Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2;
        float destinationY = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - height / 2;
        Order moveDown = new Order(destinationX, destinationY);
        scheduleTask(TASK_SIGHT, Rules.Enemies.AirCrafts.Boss.SIGHT_SOUND_DELAY);
        setOrder(moveDown);
    }

    @Override
    protected void onOrderFulfilled() {
        super.onOrderFulfilled();
        Parastrike.getSoundPlayer().playSound(SFX.Player.Bonus.SHIELD_ACTIVATED);
        for (BossCannon cannon : cannons) {
            cannon.setShieldActive(true);
            cannon.setShieldVisibility(true);
            MiscFactory miscFactory = getFactories().getMiscFactory();
            miscFactory.createExplosion(SHOCK_WAVE_BLAST, cannon.getX(), cannon.getY());
            getElements().getEffectsManager().createBossHealthBar(this);
        }
    }

    @Override
    protected void takeStep(float step, float direction, float delta) {
        super.takeStep(step, direction, delta);
        for (BossCannon cannon : cannons) {
            float bodyRelativeX = cannon.getBodyRelativeX();
            float bodyRelativeY = cannon.getBodyRelativeY();
            cannon.setPosition(getX() + bodyRelativeX, getY() + bodyRelativeY);
        }
    }

    @Override
    protected void onWaitingForNewOrder(float aux) {
        slowDownSpeed(Rules.Enemies.AirCrafts.Boss.SPEED_SLOWDOWN_DELTA);
    }

    @Override
    public int getCoinsBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Apache.Easy.COINS;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Apache.Medium.COINS;
        }
        return Rules.Enemies.AirCrafts.Apache.Hard.COINS;
    }


    @Override
    public float getSpeedBySkill() {
        return Rules.Enemies.AirCrafts.Boss.SPEED;
    }

    @Override
    public float getHealthBySkill() {
        int ordinal = Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal();
        if (ordinal == 0) {
            return Rules.Enemies.AirCrafts.Boss.HP;
        } else if (ordinal == 1) {
            return Rules.Enemies.AirCrafts.Boss.HP;
        }
        return Rules.Enemies.AirCrafts.Boss.HP;
    }

    @Override
    public float getDamageBySkill(int weapon) {
        return 0;
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }

    @Override
    public void onCollision(Enemy other) {

    }

    @Override
    public void onCollision(PlayerCharacter other) {

    }

    public void addCannon(BossCannon cannon) {
        cannons.add(cannon);
    }

    private final Timer.Task TASK_SIGHT = new Timer.Task() {
        @Override
        public void run() {
            Parastrike.getSoundPlayer().playSound(SFX.Enemies.AirCrafts.BOSS_SIGHT);
        }
    };
}
