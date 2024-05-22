package com.gadarts.parashoot.player.bonus;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 01/08/2015.
 */
public class Ammo extends Bonus {
    public Ammo() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.Bonuses.DATA_FILE_2);
        message = Assets.Strings.InGameMessages.Bonus.AMMO;
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler) {
        super.init(Assets.GFX.Sheets.ImagesNames.AMMO, x, y, mechanics, playerHandler);
    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!gameObject.isPlayerBullet()) return;
        super.onCollision(gameObject);
        if (obtained && obtainAble) {
            obtainAmmo();
        }
    }

    private void obtainAmmo() {
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        int cannonAmmo = playerStatsHandler.getCannonAmmo(playerStatsHandler.getSelectedCannon());
        if (cannonAmmo < Rules.Cannons.MAX_AMMO) addAmmo();
        else
            getWarScreenMechanics().getMessageDisplay().add(Assets.Strings.InGameMessages.Bonus.AMMO_MAX);
        obtainAble = false;
    }

    private void addAmmo() {
        directionToTarget = GameUtils.getDirectionToPoint(this, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y);
        distanceToTarget = GameUtils.getDistanceBetweenObjectToPoint(this, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y);
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        BulletType selectedCannon =  playerStatsHandler.getSelectedCannon();
        playerStatsHandler.setCannonAmmo(selectedCannon, playerStatsHandler.getCannonAmmo(selectedCannon) + 1, false);
    }
}
