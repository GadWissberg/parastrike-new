package com.gadarts.parashoot.misc;

import com.gadarts.parashoot.model.PoolableGameObject;
import com.gadarts.parashoot.model.WarScreenElements;

public abstract class Misc extends PoolableGameObject {

    protected void init(String spriteName, float x, float y, float direction, float speed, boolean animationEndDestroy, boolean fitSpriteToDirection, WarScreenElements mechanics) {
        super.init(spriteName, x, y, direction, speed, mechanics);
        setSpeed(speed);
        this.animationEndDestroy = animationEndDestroy;
        this.fitSpriteToDirection = fitSpriteToDirection;
        setOrigin(width / 2, height / 2);
        setFrameDuration(0.1f);
    }

}
