package com.gadarts.parashoot.player.sidekick;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.player.HeadedSideKick;

/**
 * Created by gadw1_000 on 06-Jul-15.
 */
public class Flamer extends HeadedSideKick {
    public Flamer() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.FLAMER_DATA_FILE);
    }
}
