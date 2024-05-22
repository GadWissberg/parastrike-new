package com.gadarts.parashoot.model;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.screens.WarScreen;

/**
 * Created by Gad on 11/07/2015.
 */
public class PoolableGameObject extends GameObject implements Pool.Poolable {
    private boolean free;

    public PoolableGameObject() {
        super();
    }

    protected void init(String spriteName, float x, float y, float direction, float speed, WarScreenElements elements) {
        this.speed = speed;
        this.free = false;
        setWarScreenElements(elements);
        setPosition(x, y);
        if (spriteName != null) {
            setRegion(spriteName);
        } else {
            animation = null;
        }
        setDirection(direction);
        animationStateTime = 0;
        scaleX = 1;
        scaleY = 1;
        visible = true;
        animating = true;
        fitSpriteToDirection = true;
        fallingSpeed = 0;
        setDestroyMeFlag(false);
        gravityStatus = false;
        setSpriteDirection(0);
    }

    @Override
    public void onTerminate(WarScreen warScreen) {
        super.onTerminate(warScreen);
        free();
    }

    public void free() {
        if (!free) {
            Pools.free(this);
            free = true;
        }
    }

    @Override
    public void reset() {
        setOrigin(0, 0);
        setBeenInside(false);
    }

}
