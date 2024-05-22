package com.gadarts.parashoot.weapons;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.object_factories.BulletFactory;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 21/02/2015.
 */
public class BlasterBullet extends Bullet {

    private float destroyedX;
    private float destroyedY;

    @Override
    public void onDestroy(boolean doDestroySequence) {
        super.onDestroy(doDestroySequence, false);
        if (doDestroySequence) {
            scheduleTask(CREATE_BULLETS, Rules.Cannons.Blaster.SMALL_BULLET_CREATION_DELAY);
            destroyedX = x;
            destroyedY = y;
        }
    }

    private final Timer.Task CREATE_BULLETS = new Timer.Task() {
        @Override
        public void run() {
            BulletFactory bulletFactory = getFactories().getBulletFactory();
            bulletFactory.createBullet(BulletType.SMALL_SPLIT_BULLET, destroyedX, destroyedY, 45);
            bulletFactory.createBullet(BulletType.SMALL_SPLIT_BULLET, destroyedX, destroyedY, 135);
            bulletFactory.createBullet(BulletType.SMALL_SPLIT_BULLET, destroyedX, destroyedY, 225);
            bulletFactory.createBullet(BulletType.SMALL_SPLIT_BULLET, destroyedX, destroyedY, 315);
            Parastrike.getSoundPlayer().playSound(SFX.Weapons.SMALL_SPLIT_LASER);
        }
    };

}
