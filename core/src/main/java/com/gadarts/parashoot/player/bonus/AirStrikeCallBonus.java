package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.HUD;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 01/08/2015.
 */
public class AirStrikeCallBonus extends BombBonus {
    public AirStrikeCallBonus() {
        message = Assets.Strings.InGameMessages.Bonus.AIR_STRIKE_CALL;
        bombType = Rules.Player.ArmoryItem.AIR_STRIKE;
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, HUD hud) {
        super.init(Assets.GFX.Sheets.ImagesNames.AIRSTRIKE, x, y, mechanics, playerHandler, hud);
    }

}
