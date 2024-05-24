package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.utils.Json;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.level_model.LevelSkill;
import com.gadarts.parashoot.level_model.LevelState;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Player.*;
import static com.gadarts.parashoot.weapons.BulletType.*;

/**
 * Created by Gad on 06/10/2016.
 */
public class PlayerStatsHandler {
    private PlayerStats stats = new PlayerStats();
    private final Preferences playerSavedStats;
    private Scene selectedScene = Scene.values()[0];
    private int selectedLevel = 1;
    private LevelSkill selectedSkill = LevelSkill.EASY;
    private Json json = new Json();
    private String playerName;

    public PlayerStatsHandler() {
        playerSavedStats = Gdx.app.getPreferences(PREF_PLAYER);
        initialize();
    }

    private void initialize() {
        initializeGameElements();
        commitStats();
    }

    private void initializeGameElements() {
        initializeBunker();
        initializeCannons();
        initializeBombs();
        initializeSideKicks();
        initializeLevels();
    }

    public void setBombs(HashMap bombs) {
        stats.setBombs(bombs);
    }

    private void initializeLevels() {
        if (GameSettings.LEVEL_MANIPULATION_MODE) {
            initializeLevelsStatesForDebug();
        } else {
            initializeLevelsStates();
        }
    }

    public void setSelectedScene(Scene selectedScene) {
        this.selectedScene = selectedScene;
    }

    public Scene getSelectedScene() {
        return selectedScene;
    }

    public int getSelectedLevel() {
        return selectedLevel;
    }

    public LevelSkill getSelectedLevelSkill() {
        return selectedSkill;
    }

    public void setSelectedLevel(int currentLevel) {
        this.selectedLevel = currentLevel;
    }

    public void setSelectedLevelSkill(LevelSkill currentSkill) {
        this.selectedSkill = currentSkill;
    }


    private void initializeBunker() {
        stats.setBunkerArmorLevel(playerSavedStats.getInteger(BUNKER_ARMOR, 1));
        stats.setBunkerGeneratorLevel(playerSavedStats.getInteger(BUNKER_GENERATOR, 1));
        stats.setCoins((GameSettings.FORCE_BEGIN_COINS >= 0) ? GameSettings.FORCE_BEGIN_COINS : playerSavedStats.getInteger(COINS, Rules.Player.BEGIN_COINS));
        stats.setScore(playerSavedStats.getInteger(SCORE, 0));
        int starsValue = (GameSettings.FORCE_BEGIN_STARS >= 0) ? GameSettings.FORCE_BEGIN_STARS : playerSavedStats.getInteger(STARS, 0);
        stats.setStars(starsValue);
    }

    private void initializeSideKicks() {
        if (playerSavedStats.getString(SIDE_KICKS).isEmpty() || json.fromJson(HashMap.class, playerSavedStats.getString(SIDE_KICKS)).size() == 0) {
            stats.setSideKicks(new HashMap<String, Hashtable<String, Integer>>());
            Hashtable<String, Integer> sideKickAttributes = new Hashtable<String, Integer>();
            sideKickAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH.getAttributeName(), 1);
            sideKickAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE.getAttributeName(), 1);
            sideKickAttributes.put(Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.SIDE_KICK_ARMOR.getAttributeName(), 1);
            sideKickAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName(), 0);
            HashMap<String, Hashtable<String, Integer>> sideKicks = stats.getSideKicks();
            sideKicks.put(SideKickFactory.SideKickType.INFANTRY_TOWER.getName(), cloneMap(sideKickAttributes));
            sideKicks.put(SideKickFactory.SideKickType.TESLA.getName(), cloneMap(sideKickAttributes));
            sideKicks.put(SideKickFactory.SideKickType.FLAMER.getName(), cloneMap(sideKickAttributes));
            sideKicks.put(SideKickFactory.SideKickType.HEAT_TURRET.getName(), cloneMap(sideKickAttributes));
            sideKicks.put(SideKickFactory.SideKickType.AUTO_TURRET.getName(), cloneMap(sideKickAttributes));
            sideKicks.put(SideKickFactory.SideKickType.HEMISPHERE.getName(), cloneMap(sideKickAttributes));
            sideKicks.put(SideKickFactory.SideKickType.PHANTOM.getName(), cloneMap(sideKickAttributes));
            stats.setSelectedSideKicks(null);
        } else {
            stats.setSideKicks(json.fromJson(HashMap.class, playerSavedStats.getString(SIDE_KICKS)));
            String selected = playerSavedStats.getString(SELECTED_SIDE_KICK);
            if (selected != null && !selected.isEmpty()) {
                stats.setSelectedSideKicks(SideKickFactory.SideKickType.valueOf(selected));
            } else {
                stats.setSelectedSideKicks(null);
            }
        }
    }

    private void initializeLevelsStatesForDebug() {
        HashMap<String, Hashtable<String, Integer>> levels = new HashMap<String, Hashtable<String, Integer>>();
        HashMap<String, Boolean> scenes = new HashMap<String, Boolean>();
        Scene levelManipulationUnlockScene = GameSettings.LEVEL_MANIPULATION_UNLOCK_SCENE;
        scenes.put(levelManipulationUnlockScene.name(), true);
        Hashtable<String, Integer> level = new Hashtable<String, Integer>();
        level.put(STATE, LevelState.NO_STARS.ordinal());
        levels.put(levelManipulationUnlockScene.name() + "_" + GameSettings.LEVEL_MANIPULATION_LEVEL_UNLOCK, level);
        stats.setLevelsStates(levels);
        stats.setScenesStates(scenes);
    }

    private void initializeLevelsStates() {
        if (playerSavedStats.getString(LEVELS_STATES).isEmpty() || json.fromJson(HashMap.class, playerSavedStats.getString(LEVELS_STATES)).size() == 0) {
            HashMap<String, Hashtable<String, Integer>> levels = new HashMap<String, Hashtable<String, Integer>>();
            HashMap<String, Boolean> scenes = new HashMap<String, Boolean>();
            scenes.put(Scene.values()[0].name(), true);
            Hashtable<String, Integer> level = new Hashtable<String, Integer>();
            level.put(STATE, LevelState.NO_STARS.ordinal());
            levels.put(Scene.values()[0].name() + "_1", level);
            stats.setLevelsStates(levels);
            stats.setScenesStates(scenes);
        } else {
            stats.setLevelsStates(json.fromJson(HashMap.class, playerSavedStats.getString(LEVELS_STATES)));
            stats.setScenesStates(json.fromJson(HashMap.class, playerSavedStats.getString(SCENES_STATES)));
        }
    }

    private void initializeBombs() {
        if (playerSavedStats.getString(BOMBS).isEmpty() || json.fromJson(HashMap.class, playerSavedStats.getString(BOMBS)).size() == 0) {
            HashMap<String, Integer> bombs = stats.getBombs();
            bombs.put(Rules.Player.ArmoryItem.BIO_HAZARD.getName(), 0);
            bombs.put(Rules.Player.ArmoryItem.AIR_STRIKE.getName(), 0);
            bombs.put(Rules.Player.ArmoryItem.ATOM.getName(), 0);
        } else {
            setBombs(json.fromJson(HashMap.class, playerSavedStats.getString(BOMBS)));
        }
    }

    private void initializeCannons() {
        if (playerSavedStats.getString(CANNONS).isEmpty() || json.fromJson(HashMap.class, playerSavedStats.getString(CANNONS)).size() == 0) {
            stats.setCannons(new HashMap<String, Hashtable<String, Integer>>());
            Hashtable<String, Integer> weaponAttributes = new Hashtable<String, Integer>();
            weaponAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH.getAttributeName(), 1);
            weaponAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE.getAttributeName(), 1);
            weaponAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName(), 0);
            weaponAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO.getAttributeName(), 0);
            HashMap<String, Hashtable<String, Integer>> cannons = stats.getCannons();
            cannons.put(BulletType.CANNON_BALL.getName(), cloneMap(weaponAttributes));
            cannons.get(BulletType.CANNON_BALL.getName()).put(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName(), 1);
            cannons.get(BulletType.CANNON_BALL.getName()).put(Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO.getAttributeName(), -1);
            cannons.put(SPREAD_CANNON_BALL.getName(), cloneMap(weaponAttributes));
            cannons.put(CHAIN_GUN_BULLET.getName(), cloneMap(weaponAttributes));
            cannons.put(BulletType.ROCKET.getName(), cloneMap(weaponAttributes));
            cannons.put(BulletType.HOMING_MISSILE.getName(), cloneMap(weaponAttributes));
            cannons.put(BulletType.LASER.getName(), cloneMap(weaponAttributes));
            cannons.put(BulletType.BLASTER.getName(), cloneMap(weaponAttributes));
            cannons.put(BulletType.SHOCK_WAVE.getName(), cloneMap(weaponAttributes));
            stats.setSelectedCannon(BulletType.CANNON_BALL);
            if (GameSettings.FORCE_BEGIN_CANNON != null) {
                cannons.get(GameSettings.FORCE_BEGIN_CANNON.getName()).put(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName(), 1);
                cannons.get(GameSettings.FORCE_BEGIN_CANNON.getName()).put(Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO.getAttributeName(), 5);
                stats.setSelectedCannon(GameSettings.FORCE_BEGIN_CANNON);
                stats.setBunkerGeneratorLevel(playerSavedStats.getInteger(BUNKER_GENERATOR, 20));
            }
        } else {
            stats.setCannons(json.fromJson(HashMap.class, playerSavedStats.getString(CANNONS)));
            stats.setSelectedCannon(BulletType.valueOf(playerSavedStats.getString(SELECTED_CANNON, CANNON_BALL.getName())));
        }
    }

    private Hashtable<String, Integer> cloneMap(Hashtable<String, Integer> toClone) {
        Hashtable<String, Integer> ltm = new Hashtable<String, Integer>();
        for (Map.Entry<String, Integer> entry : toClone.entrySet()) {
            String key = entry.getKey();
            Integer value = entry.getValue();
            ltm.put(key, value);
        }
        return ltm;
    }

    public Integer getCannonAttribute(BulletType weapon, Rules.Player.UpgradeableAttribute.WeaponAttribute attribute) {
        return (stats.getCannons().get(weapon.getName()).get(attribute.getAttributeName())).intValue();
    }

    public Integer getSideKickAttribute(SideKickFactory.SideKickType sideKick, Rules.Player.UpgradeableAttribute attribute) {
        if (sideKick.getDisabledAttribute() == attribute) return 0;
        return stats.getSideKicks().get(sideKick.getName()).get(attribute.getAttributeName()).intValue();
    }

    public boolean isSideKickEnabled(WeaponType sidekick) {
        HashMap<String, Hashtable<String, Integer>> sideKicks = stats.getSideKicks();
        String name = sidekick.getName();
        return (sideKicks.containsKey(name)) ? sideKicks.get(name).get(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName()) != 0 : false;
    }

    public boolean isCannonEnabled(WeaponType weapon) {
        return stats.getCannons().get(weapon.getName()).get(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName()).intValue() != 0;
    }

    public void enableSideKick(SideKickFactory.SideKickType sideKick) {
        stats.getSideKicks().get(sideKick.getName()).put(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName(), 1);
        commitStats();
    }

    public void enableCannon(BulletType weapon) {
        stats.getCannons().get(weapon.getName()).put(Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED.getAttributeName(), 1);
        commitStats();
    }

    public void setCannonAttribute(BulletType weapon, Rules.Player.UpgradeableAttribute attribute, int value) {
        setCannonAttribute(weapon, attribute, value, false);
    }

    public void setCannonAttribute(BulletType weapon, Rules.Player.UpgradeableAttribute attribute, int value, boolean commit) {
        if (value <= Rules.Cannons.MAXIMUM_LEVEL)
            stats.getCannons().get(weapon.getName()).put(attribute.getAttributeName(), value);
        if (commit) {
            HashMap<String, Hashtable<String, Integer>> cannons = stats.getCannons();
            playerSavedStats.putString(CANNONS, json.toJson(cannons, cannons.getClass()));
            playerSavedStats.flush();
        }
    }

    public void setSideKickAttribute(SideKickFactory.SideKickType sideKick, Rules.Player.UpgradeableAttribute attribute, int value) {
        setSideKickAttribute(sideKick, attribute, value, true);
    }

    public void setSideKickAttribute(SideKickFactory.SideKickType sideKick, Rules.Player.UpgradeableAttribute attribute, int value, boolean commit) {
        if (sideKick.getDisabledAttribute() == attribute || value > Rules.Cannons.MAXIMUM_LEVEL) {
            return;
        }
        stats.getSideKicks().get(sideKick.getName()).put(attribute.getAttributeName(), value);
        if (commit) commitStats();
    }

    public static int getCostGenericly(Object item) {
        if (item instanceof BulletType) {
            BulletType weapon = (BulletType) item;
            return weapon.getCost();
        } else if (item instanceof Rules.Player.ArmoryItem) {
            Rules.Player.ArmoryItem bomb = (Rules.Player.ArmoryItem) item;
            return bomb.getCost();
        } else if (item instanceof SideKickFactory.SideKickType) {
            SideKickFactory.SideKickType sideKick = (SideKickFactory.SideKickType) item;
            return sideKick.getCost();
        }
        return 0;
    }

    public int getCoins() {
        return stats.getCoins();
    }

    public void setCoins(int coins) {
        setCoins(coins, false);
    }

    public void setCoins(int coins, boolean commit) {
        stats.setCoins(coins);
        if (commit) {
            playerSavedStats.putInteger(COINS, stats.getCoins());
            playerSavedStats.flush();
        }
    }

    public int getBunkerArmorLevel() {
        return stats.getBunkerArmorLevel();
    }

    public int getBunkerGeneratorLevel() {
        return stats.getBunkerGeneratorLevel();
    }

    public void setBunkerAttribute(Rules.Player.UpgradeableAttribute statType, int value) {
        setBunkerAttribute(statType, value, true);
    }

    public void setBunkerAttribute(Rules.Player.UpgradeableAttribute statType, int value, boolean commit) {
        if (value > statType.getMaxValue()) return;
        if (statType == Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR) {
            stats.setBunkerArmorLevel(value);
        } else if (statType == Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.GENERATOR) {
            stats.setBunkerGeneratorLevel(value);
        }
        if (commit) {
            commitStats();
        }
    }

    public int getBunkerAttribute(Rules.Player.UpgradeableAttribute statType) {
        if (statType == Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR) {
            return stats.getBunkerArmorLevel();
        } else {
            return stats.getBunkerGeneratorLevel();
        }
    }

    public void addBomb(Rules.Player.ArmoryItem bomb) {
        addBomb(bomb, true);
    }

    public void addBomb(Rules.Player.ArmoryItem bomb, boolean commit) {
        HashMap<String, Integer> bombs = stats.getBombs();
        bombs.put(bomb.getName(), bombs.get(bomb.getName()) + 1);
        if (commit) {
            commitStats();
        }
    }

    public void degradeBomb(Rules.Player.ArmoryItem bomb) {
        HashMap<String, Integer> bombs = stats.getBombs();
        bombs.put(bomb.getName(), bombs.get(bomb.getName()) - 1);
        commitStats();
    }

    public int getBombAmount(Rules.Player.ArmoryItem bomb) {
        return stats.getBombs().get(bomb.getName()).intValue();
    }

    public void commitStats() {
        HashMap<String, Hashtable<String, Integer>> levelsStates = stats.getLevelsStates();
        playerSavedStats.putString(LEVELS_STATES, json.toJson(levelsStates, levelsStates.getClass()));
        HashMap<String, Boolean> scenesStates = stats.getScenesStates();
        playerSavedStats.putString(SCENES_STATES, json.toJson(scenesStates, scenesStates.getClass()));
        HashMap<String, Hashtable<String, Integer>> cannons = stats.getCannons();
        playerSavedStats.putString(CANNONS, json.toJson(cannons, cannons.getClass()));
        HashMap<String, Integer> bombs = stats.getBombs();
        playerSavedStats.putString(BOMBS, json.toJson(bombs, bombs.getClass()));
        HashMap<String, Hashtable<String, Integer>> sideKicks = stats.getSideKicks();
        playerSavedStats.putString(SIDE_KICKS, json.toJson(sideKicks, sideKicks.getClass()));
        playerSavedStats.putString(SELECTED_CANNON, stats.getSelectedCannon().getEnumName());
        WeaponType selectedSideKick = stats.getSelectedSideKick();
        if (selectedSideKick != null) {
            playerSavedStats.putString(SELECTED_SIDE_KICK, selectedSideKick.getEnumName());
        }
        playerSavedStats.putInteger(BUNKER_ARMOR, stats.getBunkerArmorLevel());
        playerSavedStats.putInteger(BUNKER_GENERATOR, stats.getBunkerGeneratorLevel());
        playerSavedStats.putInteger(COINS, stats.getCoins());
        playerSavedStats.putInteger(SCORE, stats.getScore());
        playerSavedStats.putInteger(STARS, stats.getStars());
        playerSavedStats.flush();
    }

    public BulletType getSelectedCannon() {
        return stats.getSelectedCannon();
    }

    public WeaponType getSelectedSideKick() {
        return stats.getSelectedSideKick();
    }

    public void setSelectedCannon(WeaponType selectedWeapon) {
        stats.setSelectedCannon((BulletType) selectedWeapon);
        commitStats();
    }

    public void setSelectedSideKick(WeaponType selectedWeapon) {
        stats.setSelectedSideKicks(selectedWeapon);
        commitStats();
    }

    public HashMap<String, Hashtable<String, Integer>> getLevelsStates() {
        return stats.getLevelsStates();
    }

    public com.gadarts.parashoot.level_model.LevelState getLevelState(Scene scene, int level) {
        Hashtable<String, Integer> selectedLevel = getLevelsStates().get(scene.name() + "_" + level);
        if (selectedLevel == null) {
            selectedLevel = new Hashtable<String, Integer>();
            selectedLevel.put(STATE, LevelState.LOCKED.ordinal());
            getLevelsStates().put(scene.name() + "_" + level, selectedLevel);
        }
        Integer selectedLevelState = selectedLevel.get(STATE);
        return com.gadarts.parashoot.level_model.LevelState.values()[selectedLevelState];
    }

    public boolean isSceneEnabled(Scene scene) {
        return stats.isSceneEnabled(scene);
    }

    public void setLevelState(Scene scene, Integer level, com.gadarts.parashoot.level_model.LevelState state) {
        HashMap<String, Hashtable<String, Integer>> levelsStates = stats.getLevelsStates();
        Hashtable<String, Integer> selectedLevel = levelsStates.get(scene.name() + "_" + level);
        Integer stateIndex = state.ordinal();
        if (selectedLevel != null) {
            selectedLevel.put(STATE, stateIndex);
        } else {
            Hashtable<String, Integer> levelObject = new Hashtable<String, Integer>();
            levelObject.put(STATE, stateIndex);
            levelsStates.put(scene.name() + "_" + level, levelObject);
        }
        if (state != LevelState.LOCKED) {
            enableScene(scene);
        }
    }

    public void enableScene(Scene scene) {
        stats.enableScene(scene);
    }

    public int getCannonAmmo(BulletType weaponType) {
        return stats.getCannons().get(weaponType.getName()).get(Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO.getAttributeName()).intValue();
    }

    public void setCannonAmmo(BulletType weaponType, int newValue) {
        setCannonAmmo(weaponType, newValue, true);
    }

    public void setCannonAmmo(BulletType weaponType, int newValue, boolean commit) {
        if (weaponType != BulletType.CANNON_BALL) {
            Hashtable<String, Integer> cannonAttributes = stats.getCannons().get(weaponType.getName());
            cannonAttributes.put(Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO.getAttributeName(), newValue);
            if (commit) commitStats();
        }
    }

    public boolean correctCannonSelection() {
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        BulletType selectedCannon = (BulletType) playerStatsHandler.getSelectedCannon();
        if (playerStatsHandler.getCannonAmmo(selectedCannon) == 0) {
            for (int i = Rules.Cannons.CANNONS_ORDER.length - 1; i > -1; i--) {
                BulletType selectedWeapon = Rules.Cannons.CANNONS_ORDER[i];
                if (playerStatsHandler.getCannonAmmo(selectedWeapon) != 0) {
                    playerStatsHandler.setSelectedCannon(selectedWeapon);
                    return true;
                }
            }
        }
        return false;
    }

    public int getBunkerGeneratorLevelDisplayVersion() {
        return (getBunkerGeneratorLevel() + 10) / 10;
    }

    public void reset() {
        Parastrike.getInstance().actionResolver.toast(Assets.Strings.Menu.Options.RESTARTED);
        playerSavedStats.clear();
        playerSavedStats.flush();
        stats.getLevelsStates().clear();
        stats.getScenesStates().clear();
        stats.getCannons().clear();
        stats.getSideKicks().clear();
        stats.getBombs().clear();
        initialize();
    }

    public void applySavedGame(SavedGame mSaveGameData) throws IOException, ClassNotFoundException {
        PlayerStats stats = SavedGame.deserialize(mSaveGameData.getData());
        this.stats = stats;
        commitStats();
    }


    public PlayerStats getPlayerStats() {
        return stats;
    }


    public int getScore() {
        return stats.getScore();
    }

    public void setScore(int score) {
        stats.setScore(score);
    }

    public int getStars() {
        return stats.getStars();
    }

    public void setStars(int stars) {
        stats.setStars(stars);
    }

    public void setPlayerStats(PlayerStats playerStats) {
        stats = playerStats;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }


}
