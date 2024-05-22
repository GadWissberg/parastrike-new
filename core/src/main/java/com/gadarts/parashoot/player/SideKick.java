package com.gadarts.parashoot.player;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.MessageDisplay;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by gadw1_000 on 06-Jul-15.
 */
public abstract class SideKick extends PlayerCharacter {

    private boolean allowShootingAirTargets;

    public enum SideKickAttributes {
        ARMOR_LEVEL, SHOOTING_RATE_LEVEL, STRENGTH_LEVEL, SELECTED_SIDEKICK
    }

    public void init(float x, float hp, WarScreen warScreen) {
        super.init(Assets.GFX.Sheets.ImagesNames.IDLE, x, Rules.Player.SideKicks.POSITION_Y, 0, 0, hp, warScreen);
        setOrigin(getWidth() / 2, 0);
    }

    @Override
    protected void onDeath() {
        super.onDeath();
        Parastrike.getSoundPlayer().playSound(SFX.Misc.BIG_EXPLOSION);
        postTask(TASK_CREATE_DEATH_BIG_EXPLOSION);
        scheduleTask(TASK_CREATE_DEATH_EXPLOSION, randomizer.nextFloat());
        scheduleTask(TASK_CREATE_DEATH_EXPLOSION, randomizer.nextFloat() + 3);
        getWarScreen().getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.DEAD_SMOKE, x, y);
        MessageDisplay messageDisplay = getWarScreen().getElements().getMessageDisplay();
        messageDisplay.taunt(SFX.Taunts.SIDE_KICK_LOST);
        messageDisplay.add(Assets.Strings.InGameMessages.SIDE_KICK_LOST);
    }

    private final Timer.Task TASK_CREATE_DEATH_EXPLOSION = new Timer.Task() {
        @Override
        public void run() {
            float randomX = x + SideKick.this.randomizer.nextInt((int) width) - originX;
            float randomY = y + SideKick.this.randomizer.nextInt((int) height) - originY;
            MiscFactory miscFactory = getWarScreen().getFactories().getMiscFactory();
            miscFactory.createExplosion(MiscFactory.ExplosionType.EXPLOSION, randomX, randomY);
            miscFactory.createMisc(MiscFactory.MiscType.FLYING_PART, randomX, randomY, 0);
            Parastrike.getSoundPlayer().playSound(SFX.Misc.EXPLOSION);
        }
    };
    private final Timer.Task TASK_CREATE_DEATH_BIG_EXPLOSION = new Timer.Task() {
        @Override
        public void run() {
            MiscFactory miscFactory = getWarScreen().getFactories().getMiscFactory();
            miscFactory.createExplosion(MiscFactory.ExplosionType.BIG_EXPLOSION_UP, x, y);
            miscFactory.createMisc(MiscFactory.MiscType.FLYING_PART, x, y, 0);
            miscFactory.createMisc(MiscFactory.MiscType.FLYING_PART, x, y, 0);
            miscFactory.createMisc(MiscFactory.MiscType.FLYING_PART, x, y, 0);
            miscFactory.createMisc(MiscFactory.MiscType.FLYING_PART, x, y, 0);
            Parastrike.getSoundPlayer().playSound(SFX.Misc.BIG_EXPLOSION);
        }
    };

    public void allowShootingAirTargets() {
        this.allowShootingAirTargets = true;
    }

    public boolean canShootAirTargets() {
        return allowShootingAirTargets;
    }

}
