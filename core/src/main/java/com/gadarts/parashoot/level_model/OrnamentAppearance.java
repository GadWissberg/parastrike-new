package com.gadarts.parashoot.level_model;

/**
 * Created by Gad on 14/08/2017.
 */

public class OrnamentAppearance {
    private final OrnamentType ornamentType;
    private final int x;
    private final int y;
    private final boolean moving;

    public OrnamentAppearance(OrnamentType ornamentType, int x, boolean moving,int y) {
        this.ornamentType = ornamentType;
        this.x = x;
        this.moving = moving;
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public OrnamentType getOrnamentType() {
        return ornamentType;
    }

    public boolean isMoving() {
        return moving;
    }

    public int getY() {
        return y;
    }
}
