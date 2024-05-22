package com.gadarts.parashoot.model.object_factories;

import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.ObjectFactory;
import com.gadarts.parashoot.player.SideKick;
import com.gadarts.parashoot.player.sidekick.*;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.Rules;

import static com.gadarts.parashoot.utils.Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.SIDE_KICK_ARMOR;
import static com.gadarts.parashoot.utils.Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE;
import static com.gadarts.parashoot.utils.Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH;

public class SideKickFactory extends ObjectFactory {

    private WarScreen warScreen;

    public SideKickFactory(Factories factories, WarScreen warScreen) {
        super(factories, warScreen.getPlayerHandler());
        this.warScreen = warScreen;
        setElements(warScreen.getElements());
    }

    /**
     * This acts as the order too.
     */
    public enum SideKickType implements WeaponType {
        INFANTRY_TOWER(Assets.Strings.Menu.Shop.SideKickNames.INFANTRY, Assets.Strings.Menu.Shop.SideKickDescriptions.INFANTRY, Rules.Player.SideKicks.InfantryTower.COST, Rules.Player.SideKicks.InfantryTower.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_WATCH, Assets.GFX.Sheets.ImagesNames.ICON_WATCH_TOWER),
        TESLA(Assets.Strings.Menu.Shop.SideKickNames.TESLA, Assets.Strings.Menu.Shop.SideKickDescriptions.TESLA, Rules.Player.SideKicks.Tesla.COST, Rules.Player.SideKicks.Tesla.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_TESLA, Assets.GFX.Sheets.ImagesNames.ICON_TESLA),
        FLAMER(Assets.Strings.Menu.Shop.SideKickNames.FLAMER, Assets.Strings.Menu.Shop.SideKickDescriptions.FLAMER, Rules.Player.SideKicks.Flamer.COST, Rules.Player.SideKicks.Flamer.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_FLAMER, Assets.GFX.Sheets.ImagesNames.ICON_FLAMER),
        HEAT_TURRET(Assets.Strings.Menu.Shop.SideKickNames.HEAT_TURRET, Assets.Strings.Menu.Shop.SideKickDescriptions.HEAT_TURRET, Rules.Player.SideKicks.HeatTurret.COST, Rules.Player.SideKicks.HeatTurret.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_HEAT, Assets.GFX.Sheets.ImagesNames.ICON_HEAT, RATE),
        AUTO_TURRET(Assets.Strings.Menu.Shop.SideKickNames.AUTO, Assets.Strings.Menu.Shop.SideKickDescriptions.AUTO, Rules.Player.SideKicks.Minigunner.COST, Rules.Player.SideKicks.Minigunner.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_AUTO, Assets.GFX.Sheets.ImagesNames.ICON_AUTO),
        HEMISPHERE(Assets.Strings.Menu.Shop.SideKickNames.HEMISPHERE, Assets.Strings.Menu.Shop.SideKickDescriptions.HEMISPHERE, Rules.Player.SideKicks.Dome.COST, Rules.Player.SideKicks.Dome.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_DOME, Assets.GFX.Sheets.ImagesNames.ICON_HEMISPHERE),
        PHANTOM(Assets.Strings.Menu.Shop.SideKickNames.PHANTOM, Assets.Strings.Menu.Shop.SideKickDescriptions.PHANTOM, Rules.Player.SideKicks.Phantom.COST, Rules.Player.SideKicks.Phantom.UPGRADE_BASIC_COST, Assets.GFX.Sheets.ImagesNames.THUMB_PHANTOM, Assets.GFX.Sheets.ImagesNames.ICON_PHANTOM);

        private final String displayName, description, icon, thumb;
        private final int COST, UPGRADE_BASIC_COST;
        private final Rules.Player.UpgradeableAttribute disabledAttribute;


        SideKickType(String displayName, String description, int cost, int upgradeBasicCost, String thumb, String icon) {
            this(displayName, description, cost, upgradeBasicCost, thumb, icon, null);
        }

        SideKickType(String displayName, String description, int cost, int upgradeBasicCost, String thumb, String icon, Rules.Player.UpgradeableAttribute disabledAttribute) {
            this.displayName = displayName;
            this.icon = icon;
            this.COST = cost;
            this.UPGRADE_BASIC_COST = upgradeBasicCost;
            this.description = description;
            this.thumb = thumb;
            this.disabledAttribute = disabledAttribute;
        }

        @Override
        public Rules.Player.UpgradeableAttribute[] getAttributes() {
            return new Rules.Player.UpgradeableAttribute[]{STRENGTH, SIDE_KICK_ARMOR, RATE};
        }

        @Override
        public boolean isUpgradeable() {
            return true;
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
        public String getIcon() {
            return icon;
        }

        @Override
        public String getThumb() {
            return thumb;
        }

        @Override
        public int getCost() {
            return COST;
        }

        @Override
        public int getUpgradeBasicCost() {
            return UPGRADE_BASIC_COST;
        }

        @Override
        public String getDescription() {
            return description;
        }

        public Rules.Player.UpgradeableAttribute getDisabledAttribute() {
            return disabledAttribute;
        }
    }

    public SideKick createSideKick(SideKickType type, float x) {
        SideKick gameObject = null;
        int listName = -1;
        switch (type) {
            case INFANTRY_TOWER:
                InfantryTower infantryTower = Pools.obtain(InfantryTower.class);
                infantryTower.init(x, Rules.Player.SideKicks.InfantryTower.Armor.ARMOR, warScreen);
                infantryTower.setAttackStrength(Rules.Player.SideKicks.InfantryTower.AttackStrength.STRENGTH);
                infantryTower.setShootingSoundFileName(SFX.Player.SideKicks.INFANTRY_TOWER_SHOOT);
                infantryTower.setShootingRate(Rules.Player.SideKicks.InfantryTower.ShootingRate.SHOOTING_RATE);
                infantryTower.setArmorUpgrade(Rules.Player.SideKicks.InfantryTower.Armor.ARMOR_UPGRADE);
                infantryTower.setRateUpgrade(Rules.Player.SideKicks.InfantryTower.ShootingRate.SHOOTING_RATE_UPGRADE);
                infantryTower.setStrengthUpgrade(Rules.Player.SideKicks.InfantryTower.AttackStrength.STRENGTH_UPGRADE);
                gameObject = infantryTower;
                break;

            case TESLA:
                Tesla tesla = Pools.obtain(Tesla.class);
                tesla.init(x, Rules.Player.SideKicks.Tesla.Armor.ARMOR, warScreen);
                tesla.setAttackStrength(Rules.Player.SideKicks.Tesla.AttackStrength.STRENGTH);
                tesla.setShootingSoundFileName(SFX.Player.SideKicks.INFANTRY_TOWER_SHOOT);
                tesla.setShootingRate(Rules.Player.SideKicks.Tesla.ShootingRate.SHOOTING_RATE);
                tesla.setArmorUpgrade(Rules.Player.SideKicks.Tesla.Armor.ARMOR_UPGRADE);
                tesla.setRateUpgrade(Rules.Player.SideKicks.Tesla.ShootingRate.SHOOTING_RATE_UPGRADE);
                tesla.setStrengthUpgrade(Rules.Player.SideKicks.Tesla.AttackStrength.STRENGTH_UPGRADE);
                gameObject = tesla;
                break;

            case FLAMER:
                Flamer flamer = Pools.obtain(Flamer.class);
                flamer.init(x, Rules.Player.SideKicks.Flamer.Armor.ARMOR, warScreen);
                flamer.setAttackStrength(Rules.Player.SideKicks.Flamer.AttackStrength.STRENGTH);
                flamer.setShootingRate(Rules.Player.SideKicks.Flamer.ShootingRate.SHOOTING_RATE);
                flamer.setHead(createSideKickHead(SideKickHeadType.FLAMER_HEAD, flamer.getX(), flamer.getY() + flamer.getHeight() - 10));
                flamer.setHeadScaleToDirection();
                flamer.setArmorUpgrade(Rules.Player.SideKicks.Flamer.Armor.ARMOR_UPGRADE);
                flamer.setRateUpgrade(Rules.Player.SideKicks.Flamer.ShootingRate.SHOOTING_RATE_UPGRADE);
                flamer.setStrengthUpgrade(Rules.Player.SideKicks.Flamer.AttackStrength.STRENGTH_UPGRADE);
                flamer.setShootingSoundFileName(SFX.Player.SideKicks.FLAME_THROWER_SHOOT);
                gameObject = flamer;
                break;

            case HEAT_TURRET:
                HeatTurret heat = Pools.obtain(HeatTurret.class);
                heat.init(x, Rules.Player.SideKicks.HeatTurret.Armor.ARMOR, warScreen);
                heat.setAttackStrength(Rules.Player.SideKicks.HeatTurret.AttackStrength.STRENGTH);
                heat.setShootingSoundFileName(SFX.Player.SideKicks.INFANTRY_TOWER_SHOOT);
                heat.setArmorUpgrade(Rules.Player.SideKicks.HeatTurret.Armor.ARMOR_UPGRADE);
                heat.setStrengthUpgrade(Rules.Player.SideKicks.HeatTurret.AttackStrength.STRENGTH_UPGRADE);
                gameObject = heat;
                break;

            case AUTO_TURRET:
                Jaguar autoTurret = Pools.obtain(Jaguar.class);
                autoTurret.init(x, Rules.Player.SideKicks.Minigunner.Armor.ARMOR, warScreen);
                autoTurret.setAttackStrength(Rules.Player.SideKicks.Minigunner.AttackStrength.STRENGTH);
                autoTurret.setShootingSoundFileName(SFX.Player.SideKicks.AUTO_TURRET_SHOOT);
                autoTurret.setShootingRate(Rules.Player.SideKicks.Minigunner.ShootingRate.SHOOTING_RATE);
                autoTurret.setHead(createSideKickHead(SideKickHeadType.AUTO_TURRET_GUN, autoTurret.getX(), autoTurret.getY() + autoTurret.getHeight() / 2));
                autoTurret.setHeadScaleToDirection();
                autoTurret.allowShootingAirTargets();
                autoTurret.setArmorUpgrade(Rules.Player.SideKicks.Minigunner.Armor.ARMOR_UPGRADE);
                autoTurret.setRateUpgrade(Rules.Player.SideKicks.Minigunner.ShootingRate.SHOOTING_RATE_UPGRADE);
                autoTurret.setStrengthUpgrade(Rules.Player.SideKicks.Minigunner.AttackStrength.STRENGTH_UPGRADE);
                gameObject = autoTurret;
                break;

            case HEMISPHERE:
                Dome dome = Pools.obtain(Dome.class);
                dome.init(x, Rules.Player.SideKicks.Dome.Armor.ARMOR, warScreen);
                dome.setAttackStrength(Rules.Player.SideKicks.Dome.AttackStrength.STRENGTH);
                dome.setShootingRate(Rules.Player.SideKicks.Dome.ShootingRate.SHOOTING_RATE);
                dome.setShootingSoundFileName(SFX.Player.SideKicks.AUTO_TURRET_SHOOT);
                dome.setArmorUpgrade(Rules.Player.SideKicks.Dome.Armor.ARMOR_UPGRADE);
                dome.setStrengthUpgrade(Rules.Player.SideKicks.Dome.AttackStrength.STRENGTH_UPGRADE);
                dome.setRateUpgrade(Rules.Player.SideKicks.Dome.ShootingRate.SHOOTING_RATE_UPGRADE);
                gameObject = dome;
                break;

            case PHANTOM:
                Phantom phantom = Pools.obtain(Phantom.class);
                phantom.init(x, Rules.Player.SideKicks.Phantom.Armor.ARMOR, warScreen);
                phantom.setAttackStrength(Rules.Player.SideKicks.Phantom.AttackStrength.STRENGTH);
                phantom.setShootingSoundFileName(SFX.Player.SideKicks.AUTO_TURRET_SHOOT);
                phantom.setShootingRate(Rules.Player.SideKicks.Phantom.ShootingRate.SHOOTING_RATE);
                phantom.setHead(createSideKickHead(SideKickHeadType.BEAM_TURRET_GUN, phantom.getX(), phantom.getY() + phantom.getHeight()));
                phantom.setHeadScaleToDirection();
                phantom.allowShootingAirTargets();
                phantom.setArmorUpgrade(Rules.Player.SideKicks.Phantom.Armor.ARMOR_UPGRADE);
                phantom.setRateUpgrade(Rules.Player.SideKicks.Phantom.ShootingRate.SHOOTING_RATE_UPGRADE);
                phantom.setStrengthUpgrade(Rules.Player.SideKicks.Phantom.AttackStrength.STRENGTH_UPGRADE);
                gameObject = phantom;
                break;

        }
        //noinspection ConstantConditions
        if (listName != -1) {
            warScreen.addObjectToMap(gameObject, listName);
        }
        return gameObject;
    }

    private enum SideKickHeadType {
        BEAM_TURRET_GUN, FLAMER_HEAD, AUTO_TURRET_GUN
    }

    private SideKickHead createSideKickHead(SideKickHeadType type, float x, float y) {
        SideKickHead gameObject = null;
        int listName = -1;
        switch (type) {
            case BEAM_TURRET_GUN:
                BeamTurretHead beamTurretHead = Pools.obtain(BeamTurretHead.class);
                beamTurretHead.init(x, y, getWarScreenElements(), getFactories());
                gameObject = beamTurretHead;
                break;

            case FLAMER_HEAD:
                FlamerHead flamerHead = Pools.obtain(FlamerHead.class);
                flamerHead.init(x, y, getWarScreenElements(), getFactories());
                gameObject = flamerHead;
                break;

            case AUTO_TURRET_GUN:
                JaguarTurret jaguarTurret = Pools.obtain(JaguarTurret.class);
                jaguarTurret.setShootingFrameDuration(Rules.Player.SideKicks.Minigunner.SHOOT_FRAME_DURATION);
                jaguarTurret.init(x, y, getWarScreenElements(), getFactories());
                gameObject = jaguarTurret;
                break;
        }
        //noinspection ConstantConditions
        if (listName != -1) {
            getWarScreenElements().addObjectToMap(gameObject, listName);
        }
        return gameObject;
    }
}

