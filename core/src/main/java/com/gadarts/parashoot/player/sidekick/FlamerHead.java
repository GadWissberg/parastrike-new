package com.gadarts.parashoot.player.sidekick;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 13/11/2015.
 */
public class FlamerHead extends SideKickHead {

    public FlamerHead() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.FLAMER_DATA_FILE);
        setBulletType(BulletType.FLAME);
    }

}
