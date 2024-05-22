package com.gadarts.parashoot.model;

import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

/**
 * Created by Gad on 03/08/2017.
 */

public class PlayerStats implements Serializable {
    private HashMap<String, Integer> bombs = new HashMap<String, Integer>();
    private HashMap<String, Hashtable<String, Integer>> cannons;
    private HashMap<String, Hashtable<String, Integer>> sideKicks;
    private HashMap<String, Hashtable<String, Integer>> levelsStates;
    private HashMap<String, Boolean> scenesStates;
    private int coins;
    private int score;

    private int stars;
    private int bunkerArmorLevel, bunkerGeneratorLevel;
    private BulletType selectedCannon;
    private WeaponType selectedSideKick;

    public PlayerStats() {
        super();
    }

    public PlayerStats(PlayerStats playerStats) {
        copyAllMaps(playerStats);
        copyFields(playerStats);
    }

    private void copyFields(PlayerStats playerStats) {
        setCoins(playerStats.coins);
        setScore(playerStats.score);
        setStars(playerStats.stars);
        copyBunkerFields(playerStats);
        setSelectedCannon(playerStats.selectedCannon);
        setSelectedSideKicks(playerStats.selectedSideKick);
    }

    private void copyBunkerFields(PlayerStats playerStats) {
        setBunkerArmorLevel(playerStats.bunkerArmorLevel);
        setBunkerGeneratorLevel(playerStats.bunkerGeneratorLevel);
    }

    private void copyAllMaps(PlayerStats playerStats) {
        bombs.putAll(playerStats.bombs);
        cannons = duplicateMapWithHashTables(playerStats.cannons);
        sideKicks = duplicateMapWithHashTables(playerStats.sideKicks);
        levelsStates = duplicateMapWithHashTables(playerStats.levelsStates);
        scenesStates = duplicateMapWithBoolean(playerStats.scenesStates);
    }

    private HashMap<String, Boolean> duplicateMapWithBoolean(HashMap<String, Boolean> map) {
        HashMap<String, Boolean> newMap = new HashMap<String, Boolean>();
        for (Map.Entry<String, Boolean> entry : map.entrySet()) {
            newMap.put(entry.getKey(), new Boolean(entry.getValue()));
        }
        return newMap;
    }

    private HashMap<String, Hashtable<String, Integer>> duplicateMapWithHashTables(HashMap<String, Hashtable<String, Integer>> map) {
        HashMap<String, Hashtable<String, Integer>> newMap = new HashMap<String, Hashtable<String, Integer>>();
        for (Map.Entry<String, Hashtable<String, Integer>> entry : map.entrySet()) {
            createSubMap(newMap, entry);
        }
        return newMap;
    }

    private void createSubMap(HashMap<String, Hashtable<String, Integer>> newMap, Map.Entry<String, Hashtable<String, Integer>> entry) {
        Hashtable<String, Integer> hashTable = new Hashtable<String, Integer>();
        newMap.put(entry.getKey(), hashTable);
        for (Map.Entry<String, Integer> insideEntry : entry.getValue().entrySet())
            hashTable.put(insideEntry.getKey(), new Integer(insideEntry.getValue()));
    }

    HashMap<String, Hashtable<String, Integer>> getLevelsStates() {
        return levelsStates;
    }

    HashMap<String, Boolean> getScenesStates() {
        return scenesStates;
    }

    void setLevelsStates(HashMap<String, Hashtable<String, Integer>> levelsStates) {
        this.levelsStates = levelsStates;
    }

    void setScenesStates(HashMap<String, Boolean> scenesStates) {
        this.scenesStates = scenesStates;
    }

    HashMap<String, Integer> getBombs() {
        return bombs;
    }

    void setBombs(HashMap bombs) {
        this.bombs = bombs;
    }

    void setCannons(HashMap<String, Hashtable<String, Integer>> cannons) {
        this.cannons = cannons;
    }

    HashMap<String, Hashtable<String, Integer>> getCannons() {
        return cannons;
    }

    void setSelectedCannon(BulletType selectedCannon) {
        this.selectedCannon = selectedCannon;
    }

    HashMap<String, Hashtable<String, Integer>> getSideKicks() {
        return sideKicks;
    }

    int getCoins() {
        return coins;
    }

    void setCoins(int coins) {
        this.coins = coins;
    }

    int getBunkerArmorLevel() {
        return bunkerArmorLevel;
    }

    int getBunkerGeneratorLevel() {
        return bunkerGeneratorLevel;
    }

    void setBunkerArmorLevel(int bunkerArmorLevel) {
        this.bunkerArmorLevel = bunkerArmorLevel;
    }

    void setBunkerGeneratorLevel(int bunkerGeneratorLevel) {
        this.bunkerGeneratorLevel = bunkerGeneratorLevel;
    }

    public int getStars() {
        return stars;
    }

    BulletType getSelectedCannon() {
        return selectedCannon;
    }

    WeaponType getSelectedSideKick() {
        return selectedSideKick;
    }

    void setSelectedSideKicks(WeaponType selectedSideKick) {
        this.selectedSideKick = selectedSideKick;
    }

    void setSideKicks(HashMap<String, Hashtable<String, Integer>> sideKicks) {
        this.sideKicks = sideKicks;
    }

    Integer getCannonAttribute(BulletType weapon, Rules.Player.UpgradeableAttribute.WeaponAttribute attribute) {
        return (getCannons().get(weapon.getName()).get(attribute.getAttributeName())).intValue();
    }

    Integer getSideKickAttribute(SideKickFactory.SideKickType sideKick, Rules.Player.UpgradeableAttribute attribute) {
        if (sideKick.getDisabledAttribute() == attribute) return 0;
        return getSideKicks().get(sideKick.getName()).get(attribute.getAttributeName()).intValue();
    }

    public boolean isSceneEnabled(Scene scene) {
        Boolean state = scenesStates.get(scene.name());
        return state == null ? false : state;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void enableScene(Scene scene) {
        scenesStates.put(scene.name(), true);
    }

    public void setStars(int stars) {
        this.stars = stars;
    }

}
