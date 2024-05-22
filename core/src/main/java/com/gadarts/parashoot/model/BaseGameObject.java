package com.gadarts.parashoot.model;

import com.badlogic.gdx.math.Rectangle;

public class BaseGameObject extends Rectangle {
    protected float originX, originY;

    public void setOrigin(float x, float y) {
        originX = x;
        originY = y;
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

}
