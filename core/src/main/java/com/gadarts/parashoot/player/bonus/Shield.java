package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

/**
 * Created by Gad on 01/08/2015.
 */
public class Shield extends Bonus {
    public Shield() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.Bonuses.DATA_FILE);
        message = Assets.Strings.InGameMessages.Bonus.SHIELD_ON;
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler) {
        super.init(Assets.GFX.Sheets.ImagesNames.SHIELD, x, y, mechanics, playerHandler);
    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!gameObject.isPlayerBullet()) return;
        super.onCollision(gameObject);
        if (obtained && obtainAble) {
            directionToTarget = GameUtils.getDirectionToPoint(this, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y);
            distanceToTarget = GameUtils.getDistanceBetweenObjectToPoint(this, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y);
            getPlayerHandler().getBunker().activateShield();
            obtainAble = false;
        }
    }
}
