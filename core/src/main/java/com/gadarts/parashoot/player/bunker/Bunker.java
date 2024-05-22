package com.gadarts.parashoot.player.bunker;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.model.CollideableGameObject;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

import static com.gadarts.parashoot.utils.Rules.System.Resolution.WIDTH_TARGET_RESOLUTION;

/**
 * Created by Gad on 05/02/2015.
 */
public class Bunker extends PlayerCharacter {

    private float startArmor;

    public Bunker() {
        super();
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.Bunker.DATA_FILE);
        setShieldRegion(atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BUNKER_SHIELD));
        customMaskWidth = Rules.Player.Bunker.MASK_WIDTH;
        setArmorUpgrade(Rules.Player.Bunker.ARMOR_UPGRADE_UNIT);
    }

    @Override
    protected void drawShield(SpriteBatch batch) {
        float shieldX = WIDTH_TARGET_RESOLUTION / 2 - getShieldRegion().getRegionWidth() / 2;
        float shieldY = Rules.Level.GROUND_Y;
        TextureRegion shieldRegion = getShieldRegion();
        int shieldWidth = shieldRegion.getRegionWidth();
        int shieldHeight = shieldRegion.getRegionHeight();
        batch.draw(shieldRegion, shieldX, shieldY, 0, 0, shieldWidth, shieldHeight,
                1, 1, 0);
    }

    public void init(WarScreen warScreen) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, 0, 0, 0, 0, Rules.Player.Bunker.ARMOR, warScreen);
        setPosition(WIDTH_TARGET_RESOLUTION / 2 - width / 2, Rules.Level.GROUND_Y);
        setShootingSoundFileName(SFX.Player.SideKicks.INFANTRY_TOWER_SHOOT);
        setAttackStrength(Rules.Player.Bunker.ATTACK_STRENGTH);
        setShootingRate(Rules.Player.Bunker.SHOOTING_RATE);
        setShieldActive(GameSettings.GOD_MODE);
    }

    @Override
    public void collisionInteraction(CollideableGameObject otherObject) {
        otherObject.onCollision(this);
    }

    @Override
    public void onCollision(Bullet bullet) {
        super.onCollision(bullet);
        failFeat();
    }


    private void failFeat() {
        Level currentLevel = getElements().getCurrentLevel();
        Level.Feat pure = Level.Feat.FLAWLESS;
        if (currentLevel.getFeat(pure)) {
            currentLevel.failFeat(pure);
        }
    }

    @Override
    public void setArmorLevel(float armorLevel) {
        super.setArmorLevel(armorLevel);
        startArmor = health;
    }

    @Override
    protected void createDamageSmoke() {
        if (myDamageSmoke == null) {
            myDamageSmoke = getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.BIG_DAMAGE_SMOKE, this, width / 2, 0);
        }
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        Parastrike.getSoundPlayer().playSound(SFX.Misc.HUGE_EXPLOSION);
        MiscFactory miscFactory = getWarScreen().getFactories().getMiscFactory();
        miscFactory.createIndependentEffect(MiscFactory.IndependentEffectType.ATOMIC_EXPLOSION, x + width / 2, y);
        miscFactory.createExplosion(MiscFactory.ExplosionType.HUGE_EXPLOSION_UP, x + width / 2, y);
        miscFactory.createFlyingParts(MiscFactory.MiscType.FLYING_PART, x + width / 2, y + height, 4);
        getWarScreen().gameOver();
    }

    @Override
    public void changeHealth(float delta, int damageType, int shade) {
        if (!isShieldActive() || delta > 0) {
            super.changeHealth(delta, damageType, shade);
            if (delta < 0) {
                manageHit();
            }
        }
    }

    private void manageHit() {
        getElements().getEffectsManager().vibrate(Rules.System.Vibration.PAIN_VIBRATION_DURATION);
        failFeat();
    }

    public float getStartArmor() {
        return startArmor;
    }

    public void activateShield() {
        setShieldVisibility(true);
        setShieldActive(true);
        TASK_SHIELD_WARNING.cancel();
        TASK_DEACTIVATE_SHIELD.cancel();
        scheduleTask(TASK_SHIELD_WARNING, Rules.Player.Bonus.Shield.DURATION - Rules.Player.Bonus.Shield.SECONDS_BEFORE_DEACTIVATE, Rules.Player.Bonus.Shield.WARNING_INTERVAL, Rules.Player.Bonus.Shield.WARNING_REPEAT);
        scheduleTask(TASK_DEACTIVATE_SHIELD, Rules.Player.Bonus.Shield.DURATION);
        Parastrike.getSoundPlayer().playSound(SFX.Player.Bonus.SHIELD_ACTIVATED);
//        Parastrike.soundPlayer.playSound(Assets.Sfx.Sounds.Taunts.SHIELD_ON, false, false);
    }

    private final Timer.Task TASK_DEACTIVATE_SHIELD = new Timer.Task() {
        @Override
        public void run() {
            setShieldActive(false);
            getElements().getMessageDisplay().add(Assets.Strings.InGameMessages.Bonus.SHIELD_OFF);
            Parastrike.getSoundPlayer().playSound(SFX.Player.Bonus.SHIELD_OFF);
//            Parastrike.soundPlayer.playSound(Assets.Sfx.Sounds.Taunts.SHIELD_OFF, false, false);
        }
    };

    private final Timer.Task TASK_SHIELD_WARNING = new Timer.Task() {
        @Override
        public void run() {
            if (getShieldVisibility()) {
                Parastrike.getSoundPlayer().playSound(SFX.Player.Bonus.SHIELD_DEPLETED);
                setShieldVisibility(false);
            } else {
                setShieldVisibility(true);
            }
        }
    };


    public enum PlayerAttributes {
        GENERATOR_LEVEL, SELECTED_WEAPON, SHOOTING_RATE_LEVEL, STRENGTH_LEVEL, ARMOR_LEVEL
    }
}
