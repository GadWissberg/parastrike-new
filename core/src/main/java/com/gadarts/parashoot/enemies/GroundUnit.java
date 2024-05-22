package com.gadarts.parashoot.enemies;

import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;

public abstract class GroundUnit extends Enemy {

    protected void init(String spriteName, float x, float y, float direction, float speed, float hp, WarScreenElements mechanics, PlayerHandler playerHandler, Factories factories) {
        super.init(spriteName, x, y, direction, speed, hp, mechanics, playerHandler, factories);
        landed = true;
    }

}
