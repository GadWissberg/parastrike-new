package com.gadarts.parashoot.enemies.paratroopers;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.misc.effects.ParticleWrapper;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.Rules;

/**
 * A paratrooper with a chaingun.
 */
public class Chaingunner extends Paratrooper {
    /**
     * The ID of it's shooting sound.
     */
    private long shootingSoundId;

    /**
     * The animation of the flying bullets cartridges.
     */
    private ParticleWrapper flyingCartridges;

    public Chaingunner() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.GroundUnits.Paratroopers.CHAINGUNNER_DATA_FILE);
        setReloadTime(Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.RELOAD_TIME);
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
        shootingSoundId = -1;
        flyingCartridges = null;
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
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Medium.COINS;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Hard.COINS;
        }
    }

    private final Timer.Task TASK_SHOOT = new Timer.Task() {
        @Override
        public void run() {
            if (!dead && hasLanded() && target != null && !target.isDead()) {
                setFacingSprite(Assets.GFX.Sheets.ImagesNames.SHOOT_LEFT, Assets.GFX.Sheets.ImagesNames.SHOOT_RIGHT, Rules.Enemies.GeneralAttributes.SHOOT_FRAME_DURATION);
                scheduleTask(TASK_SHOOT, reloadTime);
                target.changeHealth(-attackDamage);
                float randomX = target.getX() + randomizer.nextInt((int) target.getWidth()) - target.getOriginX();
                float randomY = target.getY() + randomizer.nextInt((int) target.getHeight()) - target.getOriginY();
                getFactories().getMiscFactory().createExplosion(MiscFactory.ExplosionType.BULLET_IMPACT, randomX, randomY);
                if (flyingCartridges == null) {
                    flyingCartridges = getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.SMALL_FLYING_CARTRIDGES, Chaingunner.this);
                }
                if (shootingSoundId == -1) {
                    shootingSoundId = Parastrike.getSoundPlayer().playSound(SFX.Enemies.GroundUnits.Paratroopers.CHAINGUNNER_SHOOT, true);
                }
            }
        }
    };

    @Override
    protected void onGib() {
        super.onGib();
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.GroundUnits.Paratroopers.CHAINGUNNER_SHOOT, shootingSoundId);
    }

    @Override
    protected void onDestroy(boolean doDestroySequence, boolean clearScheduler) {
        super.onDestroy(doDestroySequence, clearScheduler);
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.GroundUnits.Paratroopers.CHAINGUNNER_SHOOT, shootingSoundId);
    }

    @Override
    public void onDeath() {
        super.onDeath();
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.GroundUnits.Paratroopers.CHAINGUNNER_SHOOT, shootingSoundId);
        if (flyingCartridges != null) {
            flyingCartridges.getEffect().allowCompletion();
        }
    }

    @Override
    public float getSpeedBySkill() {
        return 0;
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Medium.HP;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Hard.HP;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Easy.ATTACK_DAMAGE;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Medium.ATTACK_DAMAGE;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Chaingunner.Hard.ATTACK_DAMAGE;
        }
    }


    @Override
    public void flyAwayFrom(GameObject gameObject) {
        super.flyAwayFrom(gameObject);
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.GroundUnits.Paratroopers.CHAINGUNNER_SHOOT, shootingSoundId);
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }
}
