package com.gadarts.parashoot.model;

import com.gadarts.parashoot.player.SideKick;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.player.bunker.Turret;

/**
 * Created by Gad on 29/06/2017.
 */

public class PlayerHandler {
    private Bunker bunker;
    private SideKick leftSideKick;
    private SideKick rightSideKick;
    private Turret turret;

    public PlayerHandler() {
    }

    public Bunker getBunker() {
        return bunker;
    }

    public SideKick getLeftSideKick() {
        return leftSideKick;
    }

    public SideKick getRightSideKick() {
        return rightSideKick;
    }

    public Turret getTurret() {
        return turret;
    }

    public void setLeftSideKick(SideKick leftSideKick) {
        this.leftSideKick = leftSideKick;
    }

    public void setRightSideKick(SideKick rightSideKick) {
        this.rightSideKick = rightSideKick;
    }

    public void setBunker(Bunker bunker) {
        this.bunker = bunker;
    }

    public void setTurret(Turret turret) {
        this.turret = turret;
    }
}
