package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.HUD;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 01/08/2015.
 */
public class GasBombBonus extends BombBonus {
    public GasBombBonus() {
        message = Assets.Strings.InGameMessages.Bonus.BIOHAZARD_BOMB;
        bombType = Rules.Player.ArmoryItem.BIO_HAZARD;
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, HUD hud) {
        super.init(Assets.GFX.Sheets.ImagesNames.GAS, x, y, mechanics, playerHandler, hud);
    }

}
