package com.gadarts.parashoot.misc.stuff;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.misc.DistanceInteractor;
import com.gadarts.parashoot.model.EffectsManager;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

public class Explosion extends DistanceInteractor {


    public Explosion() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Misc.EXPLOSIONS_DATA_FILE);
    }

    public void init(String spriteName, float x, float y, WarScreenElements mechanics) {
        init(spriteName, x, y, null, mechanics);
    }

    public void init(String spriteName, float x, float y, String effectId, WarScreenElements mechanics) {
        super.init(spriteName, x, y, 0, 0, true, false, mechanics);
        setAlpha(alpha);
        EffectsManager effectsManager = mechanics.getEffectsManager();
        if (effectId != null) {
            effectsManager.createEffect(effectId, this);
        }
        if (getElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT) {
            effectsManager.createLight(this);
        }
    }

}
