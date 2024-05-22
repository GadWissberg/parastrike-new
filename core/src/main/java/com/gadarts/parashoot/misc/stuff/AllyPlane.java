package com.gadarts.parashoot.misc.stuff;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

public class AllyPlane extends Misc {
    private boolean shot;
    private long motorSound;
    private Rules.Player.ArmoryItem type;
    private Factories factories;

    public AllyPlane() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.BONUS_APPENDIX_DATA_FILE);
    }

    public void init(WarScreenElements mechanics, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.AIR_STRIKE_PLANE, -300, Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP, 0, Rules.Misc.AllyPlane.ALLY_PLANE_SPEED, false, false, mechanics);
        this.factories = factories;
        setBeenInside(false);
        startingOutside = true;
        shot = false;
        setOrigin(width / 2, height / 2);
        motorSound = Parastrike.getSoundPlayer().playSound(SFX.Enemies.AirCrafts.PLANE_FLY, true);
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (type == Rules.Player.ArmoryItem.BIO_HAZARD || type == Rules.Player.ArmoryItem.ATOM) {
            if (x > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2) {
                if (!shot) {
                    Misc bomb;
                    if (type == Rules.Player.ArmoryItem.BIO_HAZARD) {
                        bomb = factories.getMiscFactory().createMisc(MiscFactory.MiscType.ALLY_BIOHAZARD_BOMB, x, y - height / 2);
                    } else {
                        bomb = factories.getMiscFactory().createMisc(MiscFactory.MiscType.ALLY_ATOMIC_BOMB, x, y - height / 2);
                    }
                    bomb.setSpeed(speed / 2);
                    shot = true;
                }
            }
        } else if (type == Rules.Player.ArmoryItem.AIR_STRIKE) {
            if (x > 0 && x < Rules.System.Resolution.WIDTH_TARGET_RESOLUTION) {
                if (!shot) {
                    factories.getBulletFactory().createBullet(BulletType.ALLY_BOMB, x, y - height / 2, 0).setSpeed(speed / 2);
                    shot = true;
                    scheduleTask(TASK_RELOAD, Rules.Misc.AllyPlane.ALLY_PLANE_RELOAD);
                }
            }
        }
    }

    @Override
    protected void onDestroy(boolean doDestroySequence, boolean clearScheduler) {
        super.onDestroy(doDestroySequence, clearScheduler);
        Parastrike.getSoundPlayer().stopSound(SFX.Enemies.AirCrafts.PLANE_FLY, motorSound);
    }

    public void setType(Rules.Player.ArmoryItem type) {
        this.type = type;
    }

    private final Timer.Task TASK_RELOAD = new Timer.Task() {
        @Override
        public void run() {
            shot = false;
        }
    };
}
