package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.HUD;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 01/08/2015.
 */
public class AtomBombBonus extends BombBonus {
    public AtomBombBonus() {
        message = Assets.Strings.InGameMessages.Bonus.ATOM_BOMB;
        bombType = Rules.Player.ArmoryItem.ATOM;
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, HUD hud) {
        super.init(Assets.GFX.Sheets.ImagesNames.ATOM, x, y, mechanics, playerHandler, hud);
    }

}
