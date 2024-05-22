package com.gadarts.parashoot.player.sidekick;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.player.SideKick;

/**
 * Created by Gad on 13/10/2015.
 */
public class InfantryTower extends SideKick {

    public InfantryTower() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.INFANTRY_TOWER_DATA_FILE);
    }
}
