package com.gadarts.parashoot.player.bonus;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.object_factories.BonusFactory;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 01/08/2015.
 */
public class Upgrade extends Bonus {
    private BonusFactory.BonusType type;
    private Timer.Task upgradeTask;
    private SFX.Taunts taunt;

    public Upgrade() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.Bonuses.DATA_FILE);
    }

    public void init(float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler, BonusFactory.BonusType upgradeType) {
        type = upgradeType;
        String region = initializeUpgrade();
        super.init(region, x, y, mechanics, playerHandler);
    }

    private String initializeUpgrade() {
        String region;
        switch (type) {
            case CANNON_UPGRADE:
                region = setCannonUpgrade();
                break;
            case BUNKER_UPGRADE:
                region = setBunkerUpgrade();
                break;
            case SIDEKICK_UPGRADE:
                region = setSideKickUpgrade();
                break;
            default:
                upgradeTask = null;
                region = Assets.GFX.Sheets.ImagesNames.CANNON_UPGRADE;
                break;
        }
        return region;
    }

    private String setSideKickUpgrade() {
        String region;
        message = Assets.Strings.InGameMessages.Bonus.SIDEKICK_UPGRADED;
        region = Assets.GFX.Sheets.ImagesNames.SIDEKICK_UPGRADE;
        taunt = SFX.Taunts.SIDEKICK_UPGRADE;
        upgradeTask = defineSideKickUpgradeTask();
        return region;
    }

    private Timer.Task defineSideKickUpgradeTask() {
        return new Timer.Task() {
            @Override
            public void run() {
                PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                SideKickFactory.SideKickType selectedSideKick = (SideKickFactory.SideKickType) playerStatsHandler.getSelectedSideKick();
                playerStatsHandler.setSideKickAttribute(selectedSideKick, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE, playerStatsHandler.getSideKickAttribute(selectedSideKick, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE) + 1, false);
                playerStatsHandler.setSideKickAttribute(selectedSideKick, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH, playerStatsHandler.getSideKickAttribute(selectedSideKick, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH) + 1, false);
                playerStatsHandler.setSideKickAttribute(selectedSideKick, Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR, playerStatsHandler.getSideKickAttribute(selectedSideKick, Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR) + 1, false);
            }
        };
    }

    private String setBunkerUpgrade() {
        String region;
        message = Assets.Strings.InGameMessages.Bonus.BUNKER_UPGRADED;
        region = Assets.GFX.Sheets.ImagesNames.BUNKER_UPGRADE;
        taunt = SFX.Taunts.BUNKER_UPGRADE;
        upgradeTask = defineBunkerUpgradeTask();
        return region;
    }

    private Timer.Task defineBunkerUpgradeTask() {
        return new Timer.Task() {
            @Override
            public void run() {
                PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                playerStatsHandler.setBunkerAttribute(Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR, playerStatsHandler.getBunkerArmorLevel() + 1, false);
                playerStatsHandler.setBunkerAttribute(Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.GENERATOR, playerStatsHandler.getBunkerGeneratorLevel() + 1, false);
            }
        };
    }

    private String setCannonUpgrade() {
        String region;
        message = Assets.Strings.InGameMessages.Bonus.CANNON_UPGRADED;
        region = Assets.GFX.Sheets.ImagesNames.CANNON_UPGRADE;
        taunt = SFX.Taunts.CANNON_UPGRADE;
        upgradeTask = defineCannonUpgrade();
        return region;
    }

    private Timer.Task defineCannonUpgrade() {
        return new Timer.Task() {
            @Override
            public void run() {
                PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                WeaponType selectedCannon = playerStatsHandler.getSelectedCannon();
                playerStatsHandler.setCannonAttribute((BulletType) selectedCannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH, playerStatsHandler.getCannonAttribute((BulletType) selectedCannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH) + 1);
                playerStatsHandler.setCannonAttribute((BulletType) selectedCannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE, playerStatsHandler.getCannonAttribute((BulletType) selectedCannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE) + 1);
            }
        };
    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!gameObject.isPlayerBullet()) return;
        super.onCollision(gameObject);
        if (obtained && obtainAble) {
            directionToTarget = GameUtils.getDirectionToPoint(this, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y);
            distanceToTarget = GameUtils.getDistanceBetweenObjectToPoint(this, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.Level.GROUND_Y);
            upgradeTask.run();
            obtainAble = false;
            getWarScreenMechanics().getMessageDisplay().taunt(taunt);
        }
    }
}
