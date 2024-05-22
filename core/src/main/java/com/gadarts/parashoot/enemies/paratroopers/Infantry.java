package com.gadarts.parashoot.enemies.paratroopers;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * A paratrooper with a rifle.
 */
public class Infantry extends Paratrooper {

    public Infantry() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Enemies.GroundUnits.Paratroopers.INFANTRY_DATA_FILE);
        setAttackDamage(getDamageBySkill(0));
    }

    /**
     * Poolable initialization method.
     *
     * @param x             Starting X.
     * @param y             Starting Y.
     * @param elements      The war screen's elements.
     * @param playerHandler The war screen's player handler.
     * @param factories     The war screen's factories.
     */
    public void init(float x, float y, WarScreenElements elements, PlayerHandler playerHandler, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.SINK, x, y, getHealthBySkill(), Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.GIB_REACH, elements, playerHandler, factories);
    }

    @Override
    public float getSpeedBySkill() {
        return 0;
    }

    @Override
    public float getHealthBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Easy.HP;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Medium.HP;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Hard.HP;
        }
    }


    @Override
    public int getCoinsBySkill() {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Easy.COINS;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Medium.COINS;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Hard.COINS;
        }
    }

    @Override
    public float getDamageBySkill(int weapon) {
        if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 0) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Easy.ATTACK_DAMAGE;
        } else if (Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().ordinal() == 1) {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Medium.ATTACK_DAMAGE;
        } else {
            return Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.Hard.ATTACK_DAMAGE;
        }
    }

    @Override
    public float getSpecificBySkill() {
        return 0;
    }
}
