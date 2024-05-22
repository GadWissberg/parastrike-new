package com.gadarts.parashoot.misc.stuff;

import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.misc.IndependentEffect;
import com.gadarts.parashoot.utils.Rules;


public class Fire extends IndependentEffect {
    @Override
    public void radiusEffect(Paratrooper paratrooper) {
        paratrooper.changeHealth(-paratrooper.getHealth(), Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.FLAME);
    }
}
