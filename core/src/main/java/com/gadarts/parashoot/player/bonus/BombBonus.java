package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

/**
 * Created by Gad on 01/08/2015.
 */
public abstract class BombBonus extends com.gadarts.parashoot.player.Bonus {

    protected Rules.Player.ArmoryItem bombType;
    private HUD hud;

    public BombBonus() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.Bonuses.DATA_FILE);
    }

    public void init(String imageName, float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, HUD hud) {
        super.init(imageName, x, y, mechanics, playerHandler);
        this.hud = hud;
    }

    @Override
    public void collisionInteraction(CollideableGameObject gameObject) {
        gameObject.onCollision(this);
    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!gameObject.isPlayerBullet()) return;
        if (obtainAble && !obtained) {
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            int bombAmount = playerStatsHandler.getBombAmount(bombType);
            if (bombAmount < Rules.Player.Bonus.MAX_BOMBS_TO_HAVE) {
                if (bombAmount == 0) {
                    hud.highlightBombsButton();
                }
                playerStatsHandler.addBomb(bombType, false);
                hud.updateBombsButtons();
                directionToTarget = GameUtils.getDirectionToPoint(this, Rules.Hud.Buttons.BOMBS_X, Rules.Hud.Buttons.BOMBS_Y);
                distanceToTarget = GameUtils.getDistanceBetweenObjectToPoint(this, Rules.Hud.Buttons.BOMBS_X, Rules.Hud.Buttons.BOMBS_Y);
                super.onCollision(gameObject);
            } else {
                String message = Assets.Strings.InGameMessages.Bonus.MAX_BOMBS;
                getWarScreenMechanics().getMessageDisplay().add(message + bombType.getSpecialMessage());
            }
            obtainAble = false;
        }
    }
}
