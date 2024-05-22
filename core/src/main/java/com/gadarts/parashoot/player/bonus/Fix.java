package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

/**
 * Created by Gad on 01/08/2015.
 */
public class Fix extends Bonus {
    private int fixValuePercent;

    public Fix() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.Bonuses.DATA_FILE_2);
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, boolean isSuper) {
        message = (isSuper) ? Assets.Strings.InGameMessages.Bonus.SUPER_FIX : Assets.Strings.InGameMessages.Bonus.FIX;
        String region = (isSuper) ? Assets.GFX.Sheets.ImagesNames.SUPER_FIX : Assets.GFX.Sheets.ImagesNames.FIX;
        fixValuePercent = (isSuper) ? Rules.Player.Bonus.Fix.SUPER_FIX_VALUE_PERCENT : Rules.Player.Bonus.Fix.FIX_VALUE_PERCENT;
        super.init(region, x, y, mechanics, playerHandler);
    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!gameObject.isPlayerBullet()) return;
        super.onCollision(gameObject);
        if (obtained && obtainAble) {
            directionToTarget = GameUtils.getDirectionToPoint(this, Rules.Hud.Stats.Health.X, Rules.Hud.Stats.Health.Y);
            distanceToTarget = GameUtils.getDistanceBetweenObjectToPoint(this, Rules.Hud.Stats.Health.X, Rules.Hud.Stats.Health.Y);
            Bunker bunker = getPlayerHandler().getBunker();
            float armorToAdd = fixValuePercent * bunker.getStartArmor() / 100;
            if (bunker.getHealth() + armorToAdd <= bunker.getStartArmor()) {
                bunker.changeHealth(armorToAdd);
            } else {
                bunker.setHealth(bunker.getStartArmor());
            }
            obtainAble = false;
            getWarScreenMechanics().getMessageDisplay().taunt(SFX.Taunts.DAMAGE_FIXED);
        }
    }
}
