package com.gadarts.parashoot.model.object_factories;

import com.gadarts.parashoot.utils.Rules;

public interface WeaponType {

    Rules.Player.UpgradeableAttribute[] getAttributes();

    boolean isUpgradeable();

    String getName();

    String getThumb();

    String getEnumName();

    int getCost();

    int getUpgradeBasicCost();

    String getDescription();

    String getIcon();
}
