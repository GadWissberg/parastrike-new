package com.gadarts.parashoot.weapons;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 07/05/2017.
 */
public enum BulletType implements WeaponType {
    CANNON_BALL(Assets.Strings.Menu.Shop.WeaponsNames.CANNON, Assets.Strings.Menu.Shop.WeaponsDescriptions.CANNON, 0, Rules.Cannons.CannonBall.UPGRADE_COST, Rules.Cannons.CannonBall.AMMO_COST, Assets.GFX.Sheets.ImagesNames.THUMB_CANNON_BALL, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_CANNON_BALL, Assets.GFX.Sheets.ImagesNames.ICON_CANNON),
    SPREAD_CANNON_BALL(Assets.Strings.Menu.Shop.WeaponsNames.SPREAD_CANNON, Assets.Strings.Menu.Shop.WeaponsDescriptions.SPREAD_CANNON, Rules.Cannons.SpreadCannonBall.COST, Rules.Cannons.SpreadCannonBall.UPGRADE_COST, Rules.Cannons.SpreadCannonBall.AMMO_COST, Rules.Cannons.SpreadCannonBall.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_SPREAD, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_SPREAD, Assets.GFX.Sheets.ImagesNames.ICON_SPREAD),
    CHAIN_GUN_BULLET(Assets.Strings.Menu.Shop.WeaponsNames.CHAIN_GUN, Assets.Strings.Menu.Shop.WeaponsDescriptions.CHAIN_GUN, Rules.Cannons.ChainGun.COST, Rules.Cannons.ChainGun.UPGRADE_COST, Rules.Cannons.ChainGun.AMMO_COST, Rules.Cannons.ChainGun.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_CHAINGUN, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_CHAIN, Assets.GFX.Sheets.ImagesNames.ICON_CHAIN),
    ROCKET(Assets.Strings.Menu.Shop.WeaponsNames.ROCKET_LAUNCHER, Assets.Strings.Menu.Shop.WeaponsDescriptions.ROCKET_LAUNCHER, Rules.Cannons.RocketLauncher.COST, Rules.Cannons.RocketLauncher.UPGRADE_COST, Rules.Cannons.RocketLauncher.AMMO_COST, Rules.Cannons.RocketLauncher.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_ROCKET_LAUNCHER, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_ROCKET, Assets.GFX.Sheets.ImagesNames.ICON_ROCKET),
    HOMING_MISSILE(Assets.Strings.Menu.Shop.WeaponsNames.MISSILE_LAUNCHER, Assets.Strings.Menu.Shop.WeaponsDescriptions.MISSILE_LAUNCHER, Rules.Cannons.IronDome.COST, Rules.Cannons.IronDome.UPGRADE_COST, Rules.Cannons.IronDome.AMMO_COST, Rules.Cannons.IronDome.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_MISSILE_LAUNCHER, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_HOMING, Assets.GFX.Sheets.ImagesNames.ICON_MISSILE),
    NON_HOMING_MISSILE,
    BAZOOKA_MISSILE, ZEPPELIN_BOMB,
    BLASTER(Assets.Strings.Menu.Shop.WeaponsNames.BLASTER, Assets.Strings.Menu.Shop.WeaponsDescriptions.BLASTER, Rules.Cannons.Blaster.COST, Rules.Cannons.Blaster.UPGRADE_COST, Rules.Cannons.Blaster.AMMO_COST, Rules.Cannons.Blaster.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_BLASTER, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_BLASTER, Assets.GFX.Sheets.ImagesNames.ICON_BLASTER),
    SMALL_SPLIT_BULLET, HOMING_PLASMA,
    LASER(Assets.Strings.Menu.Shop.WeaponsNames.LASER, Assets.Strings.Menu.Shop.WeaponsDescriptions.LASER, Rules.Cannons.TwinLaser.COST, Rules.Cannons.TwinLaser.UPGRADE_COST, Rules.Cannons.TwinLaser.AMMO_COST, Rules.Cannons.TwinLaser.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_TWIN_LASER, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_TWIN, Assets.GFX.Sheets.ImagesNames.ICON_TWIN),
    FLAME, JAGUAR_BULLET, ENEMY_MINIGUN_BULLET,
    SHOCK_WAVE(Assets.Strings.Menu.Shop.WeaponsNames.SHOCK, Assets.Strings.Menu.Shop.WeaponsDescriptions.SHOCK, Rules.Cannons.ShockWave.COST, Rules.Cannons.ShockWave.UPGRADE_COST, Rules.Cannons.ShockWave.AMMO_COST, Rules.Cannons.ShockWave.MIN_GENERATOR_REQ, Assets.GFX.Sheets.ImagesNames.THUMB_SHOCKWAVE, Assets.GFX.Sheets.ImagesNames.ILLUSTRATION_TESLA, Assets.GFX.Sheets.ImagesNames.ICON_SHOCK),
    ALLY_BOMB;

    private final String displayName, description, thumb, illustration, icon;
    private final int cost, upgradeBasicCost, ammoCost, minGeneratorReq;

    BulletType() {
        this("", "", 0);
    }

    BulletType(String name, String description, int cost) {
        this(name, description, cost, 0, 0, 0, null, null, null);
    }

    BulletType(String name, String description, int cost, int upgradeBasicCost, int ammoCost, String thumb, String illustration, String icon) {
        this(name, description, cost, upgradeBasicCost, ammoCost, 0, thumb, illustration, icon);
    }

    BulletType(String name, String description, int cost, int upgradeBasicCost, int ammoCost, int minGeneratorReq, String thumb, String illustration, String icon) {
        this.cost = cost;
        this.displayName = name;
        this.description = description;
        this.upgradeBasicCost = upgradeBasicCost;
        this.thumb = thumb;
        this.illustration = illustration;
        this.icon = icon;
        this.ammoCost = ammoCost;
        this.minGeneratorReq = minGeneratorReq;
    }

    @Override
    public boolean isUpgradeable() {
        return true;
    }

    @Override
    public Rules.Player.UpgradeableAttribute[] getAttributes() {
        return new Rules.Player.UpgradeableAttribute[]{Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE, Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO};
    }

    @Override
    public String getThumb() {
        return thumb;
    }

    @Override
    public String getName() {
        return displayName;
    }

    @Override
    public String getEnumName() {
        return name();
    }

    @Override
    public String getDescription() {
        return description;
    }

    public String getIllustration() {
        return illustration;
    }

    @Override
    public int getCost() {
        return cost;
    }

    public int getMinGeneratorReq() {
        return minGeneratorReq;
    }

    @Override
    public int getUpgradeBasicCost() {
        return upgradeBasicCost;
    }

    public int getAmmoCost() {
        return ammoCost;
    }

    @Override
    public String getIcon() {
        return icon;
    }
}
