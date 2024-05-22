package com.gadarts.parashoot.model;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.io.*;
import java.util.HashMap;

/**
 * Created by Gad on 03/08/2017.
 */

public class SavedGame implements Serializable {

    private byte[] data;

    public SavedGame() {

    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public byte[] getData() {
        return data;
    }

//    public void resolveConflictedData(byte[] latestSnapshotData, byte[] olderSnapshotData) {
//        try {
//            PlayerStats latestStats = deserialize(latestSnapshotData);
//            PlayerStats olderStats = deserialize(olderSnapshotData);
//            resolveStats(latestStats, olderStats);
//        } catch (IOException e) {
//            e.printStackTrace();
//        } catch (ClassNotFoundException e) {
//            e.printStackTrace();
//        }
//    }

//    private void resolveStats(PlayerStats latestStats, PlayerStats olderStats) {
//        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
//        playerStatsHandler.setCoins(latestStats.getCoins());
//        resolveBunkerAttributes(latestStats, olderStats);
//        resolveCannonsAttributes(latestStats, olderStats);
//        resolveSideKicksAttributes(latestStats, olderStats);
//        resolveBombs(latestStats, olderStats);
//        for (Scene scene : Scene.values()) {
//            if (playerStatsHandler.getLevelsStates().containsKey(scene.name())) {
//                for (Map.Entry<String, Hashtable<String, Integer>> level : playerStatsHandler.getLevelsStates().get(scene.name()).entrySet()) {
//                    int state = level.getValue().get(0);
//                    if (state != LevelState.LOCKED.ordinal()) {
//                        int levelNumber = Integer.valueOf((level.getKey()));
//                        playerStatsHandler.setLevelState(scene, levelNumber, LevelState.values()[state]);
//                    }
//                }
//            }
//        }
//    }

    private void resolveBombs(PlayerStats latestStats, PlayerStats olderStats) {
        HashMap<String, Integer> latestBombs = latestStats.getBombs();
        HashMap<String, Integer> olderBombs = olderStats.getBombs();
        HashMap<String, Integer> mergedBombs = new HashMap<String, Integer>();
        for (int i = 1; i < Rules.Player.ArmoryItem.values().length; i++) {
            resolveBomb(latestBombs, olderBombs, mergedBombs, Rules.Player.ArmoryItem.values()[i]);
        }
    }

    private void resolveBomb(HashMap<String, Integer> latestBombs, HashMap<String, Integer> olderBombs, HashMap<String, Integer> mergedBombs, Rules.Player.ArmoryItem bomb) {
        String name = bomb.name();
        Integer latestBombAmount = latestBombs.get(name);
        Integer olderBombAmount = olderBombs.get(name);
        mergedBombs.put(name, (Math.max(latestBombAmount == null ? 0 : latestBombAmount, olderBombAmount == null ? 0 : olderBombAmount)));
    }

    private void resolveCannonsAttributes(PlayerStats latestStats, PlayerStats olderStats) {
        for (Rules.Player.UpgradeableAttribute.WeaponAttribute attribute : Rules.Player.UpgradeableAttribute.WeaponAttribute.values()) {
            resolveCannonsByAttribute(latestStats, olderStats, attribute);
        }
    }

    private void resolveSideKicksAttributes(PlayerStats latestStats, PlayerStats olderStats) {
        resolveSideKicksAttributes(latestStats, olderStats, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE);
        resolveSideKicksAttributes(latestStats, olderStats, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH);
        resolveSideKicksAttributes(latestStats, olderStats, Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED);
        resolveSideKicksAttributes(latestStats, olderStats, Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR);
    }

    private void resolveCannonsByAttribute(PlayerStats latestStats, PlayerStats olderStats, Rules.Player.UpgradeableAttribute.WeaponAttribute attribute) {
        for (BulletType cannon : Rules.Cannons.CANNONS_ORDER) {
            Integer olderCannonAttribute = olderStats.getCannonAttribute(cannon, attribute);
            Integer latestCannonAttribute = latestStats.getCannonAttribute(cannon, attribute);
            Parastrike.getPlayerStatsHandler().setCannonAttribute(cannon, attribute, (latestCannonAttribute > olderCannonAttribute) ? latestCannonAttribute : olderCannonAttribute);
        }
    }

    private void resolveSideKicksAttributes(PlayerStats latestStats, PlayerStats olderStats, Rules.Player.UpgradeableAttribute attribute) {
        for (SideKickFactory.SideKickType sideKickType : SideKickFactory.SideKickType.values()) {
            Integer olderAttribute = olderStats.getSideKickAttribute(sideKickType, attribute);
            Integer latestAttribute = latestStats.getSideKickAttribute(sideKickType, attribute);
            Parastrike.getPlayerStatsHandler().setSideKickAttribute(sideKickType, attribute, (latestAttribute > olderAttribute) ? latestAttribute : olderAttribute);
        }
    }

    private void resolveBunkerAttributes(PlayerStats latestStats, PlayerStats olderStats) {
        Parastrike.getPlayerStatsHandler().setBunkerAttribute(Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.GENERATOR, latestStats.getBunkerGeneratorLevel() > olderStats.getBunkerGeneratorLevel() ? latestStats.getBunkerGeneratorLevel() : olderStats.getBunkerGeneratorLevel());
        Parastrike.getPlayerStatsHandler().setBunkerAttribute(Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR, latestStats.getBunkerArmorLevel() > olderStats.getBunkerArmorLevel() ? latestStats.getBunkerArmorLevel() : olderStats.getBunkerArmorLevel());
    }

    public static PlayerStats deserialize(byte[] mSaveGameData) throws IOException, ClassNotFoundException {
        ObjectInput in = null;
        try {
            in = new ObjectInputStream(new ByteArrayInputStream(mSaveGameData));
            return (PlayerStats) in.readObject();
        } finally {
            if (in != null) {
                in.close();
            }
        }
    }


    public static byte[] serialize(PlayerStats playerStats) throws IOException {
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ObjectOutput out;
        try {
            out = new ObjectOutputStream(bos);
            out.writeObject(playerStats);
            out.flush();
            return bos.toByteArray();
        } finally {
            try {
                bos.close();
            } catch (IOException ignore) {
            }
        }
    }

}
