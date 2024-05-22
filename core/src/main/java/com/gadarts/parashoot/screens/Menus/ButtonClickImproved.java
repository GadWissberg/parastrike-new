package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.SFX;

public class ButtonClickImproved extends ClickListener {

    @Override
    public void clicked(InputEvent event, float x, float y) {
        super.clicked(event, x, y);
        Parastrike.getSoundPlayer().playSound(SFX.HUD.BUTTON_CLICK, false, false);
    }

}
