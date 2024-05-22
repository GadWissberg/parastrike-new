package com.gadarts.parashoot.misc;

import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.model.WarScreenElements;

public abstract class DistanceInteractor extends Misc {
    private float blastRadius;

    public void setBlastRadius(float distanceInteraction) {
        this.blastRadius = distanceInteraction;
    }

    public float getBlastRadius() {
        return blastRadius;
    }

    public void radiusEffect(Paratrooper paratrooper) {
        paratrooper.flyAwayFrom(this);
    }

    public void init(String spriteName, float x, float y, float direction, float speed, boolean animationEndDestroy, boolean fitSpriteToDirection, WarScreenElements mechanics) {
        super.init(spriteName, x, y, direction, speed, animationEndDestroy, fitSpriteToDirection, mechanics);
        blastRadius = 0;
    }
}
