package com.gadarts.parashoot.model.object_factories;

import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.player.bonus.*;
import com.gadarts.parashoot.utils.Rules;

public class BonusFactory extends ObjectFactory {

    private static BonusFactory instance;
    private final PlayerHandler playerHandler;
    private HUD hud;

    public enum BonusType {
        FIX(Fix.class, Assets.Strings.Menu.Help.Bonuses.FIX, Assets.GFX.Sheets.ImagesNames.FIX),
        SUPER_FIX(Fix.class, Assets.Strings.Menu.Help.Bonuses.SUPER_FIX, Assets.GFX.Sheets.ImagesNames.SUPER_FIX),
        MONEY(MoneyBonus.class, Assets.Strings.Menu.Help.Bonuses.MONEY, Assets.GFX.Sheets.ImagesNames.DOLLAR),
        SUPER_MONEY(MoneyBonus.class, Assets.Strings.Menu.Help.Bonuses.SUPER_MONEY, Assets.GFX.Sheets.ImagesNames.DOLLAR_BAG),
        AMMO(Ammo.class, Assets.Strings.Menu.Help.Bonuses.AMMO, Assets.GFX.Sheets.ImagesNames.AMMO),
        CANNON_UPGRADE(Upgrade.class, Assets.Strings.Menu.Help.Bonuses.CANNON_UPGRADE, Assets.GFX.Sheets.ImagesNames.CANNON_UPGRADE),
        BUNKER_UPGRADE(Upgrade.class, Assets.Strings.Menu.Help.Bonuses.BUNKER_UPGRADE, Assets.GFX.Sheets.ImagesNames.BUNKER_UPGRADE),
        SIDEKICK_UPGRADE(Upgrade.class, Assets.Strings.Menu.Help.Bonuses.SIDEKICK_UPGRADE, Assets.GFX.Sheets.ImagesNames.SIDEKICK_UPGRADE),
        GAS_BOMB(GasBombBonus.class, Assets.Strings.Menu.Help.Bonuses.GAS_BOMB, Assets.GFX.Sheets.ImagesNames.GAS),
        AIR_STRIKE(AirStrikeCallBonus.class, Assets.Strings.Menu.Help.Bonuses.AIR_STRIKE, Assets.GFX.Sheets.ImagesNames.AIRSTRIKE),
        ATOM_BOMB(AtomBombBonus.class, Assets.Strings.Menu.Help.Bonuses.ATOM, Assets.GFX.Sheets.ImagesNames.ATOM),
        SHIELD(Shield.class, Assets.Strings.Menu.Help.Bonuses.SHIELD, Assets.GFX.Sheets.ImagesNames.SHIELD);

        private final Class<? extends Bonus> classObject;
        private final String description;

        private final String regionName;

        BonusType(Class<? extends Bonus> classObject, String description, String regionName) {
            this.classObject = classObject;
            this.description = description;
            this.regionName = regionName;
        }

        public String getDescription() {
            return description;
        }

        public String getRegionName() {
            return regionName;
        }

        public Class<? extends Bonus> getClassObject() {
            return classObject;
        }
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public BonusFactory(Factories factories, PlayerHandler playerHandler, HUD hud) {
        super(factories, playerHandler);
        this.instance = this;
        this.playerHandler = playerHandler;
        this.hud = hud;
    }

    public static BonusFactory getInstance() {
        return instance;
    }

    public Bonus createBonus(BonusType type, float x, float y) {
        Bonus gameObject = null;
        WarScreenElements mechanics = getWarScreenElements();
        switch (type) {
            case FIX:
                Fix fix = Pools.obtain(Fix.class);
                fix.init(x, y, mechanics, playerHandler, false);
                gameObject = fix;
                break;
            case SUPER_FIX:
                Fix superFix = Pools.obtain(Fix.class);
                superFix.init(x, y, mechanics, playerHandler, true);
                gameObject = superFix;
                break;
            case BUNKER_UPGRADE:
                Upgrade bunkerUpgrade = Pools.obtain(Upgrade.class);
                bunkerUpgrade.init(x, y, mechanics, playerHandler, BonusType.BUNKER_UPGRADE);
                gameObject = bunkerUpgrade;
                break;
            case CANNON_UPGRADE:
                Upgrade cannonUpgrade = Pools.obtain(Upgrade.class);
                cannonUpgrade.init(x, y, mechanics, playerHandler, BonusType.CANNON_UPGRADE);
                gameObject = cannonUpgrade;
                break;
            case SIDEKICK_UPGRADE:
                Upgrade sideKickUpgrade = Pools.obtain(Upgrade.class);
                sideKickUpgrade.init(x, y, mechanics, playerHandler, BonusType.SIDEKICK_UPGRADE);
                gameObject = sideKickUpgrade;
                break;
            case AMMO:
                Ammo ammo = Pools.obtain(Ammo.class);
                ammo.init(x, y, mechanics, playerHandler);
                gameObject = ammo;
                break;

            case MONEY:
                MoneyBonus moneyBonus = Pools.obtain(MoneyBonus.class);
                moneyBonus.init(x, y, mechanics, playerHandler, false);
                gameObject = moneyBonus;
                break;

            case SUPER_MONEY:
                MoneyBonus superMoneyBonus = Pools.obtain(MoneyBonus.class);
                superMoneyBonus.init(x, y, mechanics, playerHandler, true);
                gameObject = superMoneyBonus;
                break;

            case SHIELD:
                Shield shield = Pools.obtain(Shield.class);
                shield.init(x, y, mechanics, playerHandler);
                gameObject = shield;
                break;

            case GAS_BOMB:
                GasBombBonus gas = Pools.obtain(GasBombBonus.class);
                gas.init(x, y, mechanics, playerHandler, hud);
                gameObject = gas;
                break;

            case AIR_STRIKE:
                AirStrikeCallBonus airStrikeCallBonus = Pools.obtain(AirStrikeCallBonus.class);
                airStrikeCallBonus.init(x, y, mechanics, playerHandler, hud);
                gameObject = airStrikeCallBonus;
                break;

            case ATOM_BOMB:
                AtomBombBonus atomBombBonus = Pools.obtain(AtomBombBonus.class);
                atomBombBonus.init(x, y, mechanics, playerHandler, hud);
                gameObject = atomBombBonus;
                break;
        }
        mechanics.addObjectToMap(gameObject, Rules.System.GameObjectTypes.BONUSES);
        return gameObject;
    }

}