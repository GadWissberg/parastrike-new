package com.gadarts.parashoot.weapons;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.MiscFactory;

/**
 * Created by Gad on 27/01/2016.
 */
public class Flame extends Bullet {
    @Override
    public void init(String spriteName, float x, float y, float direction, float speed, float damageValue, WarScreenElements mechanics, Factories factories) {
        super.init(spriteName, x, y, direction, speed, damageValue, mechanics, factories);
        setExplosion(null, SFX.Misc.FLAME_HIT);
        emitSmoke(Assets.Configs.ParticleEffects.SMALL_SMOKE);
    }

    @Override
    protected void createMyExplosion() {
        getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.FIRE, x, y);
        if (explosionSound != null) {
            Parastrike.getSoundPlayer().playSound(explosionSound);
        }
    }
}
