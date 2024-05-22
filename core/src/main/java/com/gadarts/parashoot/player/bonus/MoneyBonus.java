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
public class MoneyBonus extends Bonus {
    private int moneyValue;

    public MoneyBonus() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.Bonuses.DATA_FILE);
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, boolean isSuper) {
        moneyValue = (isSuper) ? Rules.Player.Bonus.Money.SUPER_MONEY_VALUE : Rules.Player.Bonus.Money.MONEY_VALUE;
        message = moneyValue + Assets.Strings.InGameMessages.Bonus.MONEY;
        String region = (isSuper) ? Assets.GFX.Sheets.ImagesNames.DOLLAR_BAG : Assets.GFX.Sheets.ImagesNames.DOLLAR;
        super.init(region, x, y, mechanics, playerHandler);
    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!gameObject.isPlayerBullet()) return;
        super.onCollision(gameObject);
        if (obtained && obtainAble) {
            obtainAble = false;
            getWarScreenMechanics().getScoresHandler().addCoins(moneyValue);
            directionToTarget = GameUtils.getDirectionToPoint(this, Rules.Hud.Stats.Score.X, Rules.Hud.Stats.Score.Y);
            distanceToTarget = GameUtils.getDistanceBetweenObjectToPoint(this, Rules.Hud.Stats.Score.X, Rules.Hud.Stats.Score.Y);
        }
    }

}
