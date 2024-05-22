package com.gadarts.parashoot.model;

import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Queue;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.GroundUnit;
import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.misc.DistanceInteractor;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.player.PlayerCharacter;
import com.gadarts.parashoot.player.SideKick;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

import java.util.*;

import static com.gadarts.parashoot.utils.Rules.System.GameObjectTypes.*;

/**
 * Created by Gad on 26/07/2015.
 */
public class InteractionsManager {
    private static HashMap<Integer, Array<CollideableGameObject>> spatial = new HashMap<Integer, Array<CollideableGameObject>>(Rules.System.SPATIAL_WIDTH_DIVISION * Rules.System.SPATIAL_HEIGHT_DIVISION);
    private static HashMap<Integer, Array<CollideableGameObject>> DistanceInteractorsSpatial = new HashMap<Integer, Array<CollideableGameObject>>(Rules.System.SPATIAL_WIDTH_DIVISION * Rules.System.SPATIAL_HEIGHT_DIVISION);
    private final WarScreenElements elements;
    private final PlayerHandler playerHandler;
    private Set<Integer> spatialAux = new HashSet<Integer>();
    private Queue<Array<CollideableGameObject>> arraysPool = new Queue<Array<CollideableGameObject>>();

    public InteractionsManager(WarScreenElements elements, PlayerHandler playerHandler) {
        this.elements = elements;
        this.playerHandler = playerHandler;
    }

    public Array<CollideableGameObject> obtainGameObjectsAtPosition(int id) {
        Array<CollideableGameObject> gameObjectsAtPosition = spatial.get(id);
        if (gameObjectsAtPosition == null) {
            gameObjectsAtPosition = obtainArray();
            spatial.put(id, gameObjectsAtPosition);
        }
        return gameObjectsAtPosition;
    }

    private Array<CollideableGameObject> obtainArray() {
        if (arraysPool.size > 0) {
            return arraysPool.removeFirst();
        } else {
            return new Array<CollideableGameObject>();
        }
    }

    public HashMap<Integer, Array<CollideableGameObject>> getSpatial() {
        return spatial;
    }

    public Queue<Array<CollideableGameObject>> getArraysPool() {
        return arraysPool;
    }

    public void setPlayerTargets() {
        SideKick leftSideKick = playerHandler.getLeftSideKick();
        if (leftSideKick == null) {
            if (!playerHandler.getBunker().isDead()) {
                setGroundTarget(playerHandler.getBunker());
            }
            return;
        }
        SideKick rightSideKick = playerHandler.getRightSideKick();
        if (leftSideKick.canShootAirTargets()) {
            setNearestEnemyAsTarget(leftSideKick);
            setNearestEnemyAsTarget(rightSideKick);
        } else {
            setGroundTarget(leftSideKick);
            setGroundTarget(rightSideKick);
        }
    }

    @SuppressWarnings("unchecked")
    private void setNearestEnemyAsTarget(SideKick sideKick) {
        if (sideKick.isDead()) {
            if (!playerHandler.getBunker().isDead()) {
                setGroundTarget(playerHandler.getBunker());
            }
        } else {
            if (!sideKick.hasTarget()) {
                float shortestDistance = 0, distance;
                Enemy closestEnemy = null;
                Array<Array<Enemy>> enemies = elements.getAllEnemies();
                for (int j = 0; j < enemies.size; j++) {
                    Array<Enemy> type = enemies.get(j);
                    for (int i = 0; i < type.size; i++) {
                        Enemy enemy = type.get(i);
                        if (closestEnemy == null) {
                            closestEnemy = enemy;
                        }
                        if (!enemy.isDead()) {
                            distance = GameUtils.getDistanceBetweenTwoObjects(sideKick, enemy);
                            if (distance < shortestDistance || shortestDistance == 0) {
                                shortestDistance = distance;
                                closestEnemy = enemy;
                            }
                        }
                    }
                }
                sideKick.setTarget(closestEnemy);
            }
        }
    }

    public void setGroundTarget(PlayerCharacter playerCharacter) {
        if (!playerCharacter.isDead()) {
            if (playerCharacter.hasTarget()) {
                return;
            }
        } else {
            if (!playerHandler.getBunker().isDead()) {
                setGroundTarget(playerHandler.getBunker());
            }
            return;
        }
        Array<Paratrooper> paratroopers = (Array<Paratrooper>) elements.getObjectsMap().get(PARATROOPERS);
        if (paratroopers != null) {
            for (int i = 0; i < paratroopers.size; i++) {
                Paratrooper currentParatrooper = paratroopers.get(i);
                if (currentParatrooper.hasLanded()) {
                    if (handleSetGroundTarget(playerCharacter, currentParatrooper)) {
                        return;
                    }
                }
            }
        }
        Array<GroundUnit> groundUnits = (Array<GroundUnit>) elements.getObjectsMap().get(GROUND_CRAFTS);
        if (groundUnits != null) {
            for (int i = 0; i < groundUnits.size; i++) {
                GroundUnit currentGroundUnit = groundUnits.get(i);
                if (handleSetGroundTarget(playerCharacter, currentGroundUnit)) {
                    return;
                }
            }
        }
    }

    private boolean handleSetGroundTarget(PlayerCharacter playerCharacter, GroundUnit currentGroundUnit) {
        if (currentGroundUnit.isDead()) {
            return false;
        }
        if (playerCharacter != playerHandler.getBunker()) {
            if ((playerCharacter.isOnLeftSide() && currentGroundUnit.isOnLeftSide()) || (!playerCharacter.isOnLeftSide() && !currentGroundUnit.isOnLeftSide())) {
                if (playerCharacter.setTarget(currentGroundUnit)) {
                    return true;
                }
            }
        } else {
            if ((currentGroundUnit.isOnLeftSide() && WarScreen.isSideKickDead(playerHandler.getLeftSideKick())) || !currentGroundUnit.isOnLeftSide() && WarScreen.isSideKickDead(playerHandler.getRightSideKick())) {
                if (playerHandler.getBunker().setTarget(currentGroundUnit)) {
                    return true;
                }
            }
        }
        return false;
    }

    public void checkCollisions(CollideableGameObject gameObject, Array<CollideableGameObject> list) {
        if (list == null) return;
        for (int j = 0; j < list.size; j++) {
            handleCollision(gameObject, list.get(j));
        }
    }

    private void handleCollision(CollideableGameObject gameObject, CollideableGameObject otherObject) {
        if (gameObject != otherObject && gameObject.overlapsWithOrigin(otherObject)) {
            gameObject.collisionInteraction(otherObject);
            otherObject.collisionInteraction(gameObject);
        }
    }

    public AirCraft findHomeableAirCraftInRange() {
        AirCraft answer = null;
        Array<AirCraft> airCrafts = (Array<AirCraft>) elements.getObjectsMap().get(AIR_CRAFTS);
        if (airCrafts.size == 0) return answer;
        for (int i = 0; i < airCrafts.size && answer == null; i++) {
            AirCraft enemy = airCrafts.get(i);
            if (checkIfEnemyCanBeHomed(playerHandler.getBunker(), enemy) != null) answer = enemy;
        }
        return answer;
    }

    private AirCraft checkIfEnemyCanBeHomed(Bunker bunker, AirCraft airCraft) {
        if (!airCraft.isHomeable()) return null;
        float directionToAirCraft = GameUtils.getDirectionToPoint(bunker, airCraft);
        float turretRange = Rules.Cannons.IronDome.TURRET_RANGE;
        float turretDirection = playerHandler.getTurret().getDirection();
        boolean inRangeFromLeft = directionToAirCraft <= turretDirection + turretRange;
        boolean inRangeFromRight = directionToAirCraft >= turretDirection - turretRange;
        return (inRangeFromLeft && inRangeFromRight) ? airCraft : null;
    }

    public void setEnemyTarget(Targeting enemy) {
        if (enemy.isOnLeftSide()) {
            SideKick leftSideKick = playerHandler.getLeftSideKick();
            if (WarScreen.isSideKickDead(leftSideKick)) {
                enemy.setTarget(playerHandler.getBunker());
            } else {
                enemy.setTarget(leftSideKick);
            }
        } else {
            SideKick rightSideKick = playerHandler.getRightSideKick();
            if (WarScreen.isSideKickDead(rightSideKick)) {
                enemy.setTarget(playerHandler.getBunker());
            } else {
                enemy.setTarget(rightSideKick);
            }
        }
    }

    private void addCorner(int corner) {
        if (corner != -1) {
            spatialAux.add(corner);
        }
    }

    public Set<Integer> getAuxSpatial() {
        return spatialAux;
    }

    private int getCenterId(GameObject gameObject) {
        int centerX = (int) (gameObject.getCenterX() * Rules.System.SPATIAL_WIDTH_DIVISION / Rules.System.Resolution.WIDTH_TARGET_RESOLUTION);
        int centerY = (int) ((gameObject.getCenterY() - Rules.Level.GROUND_Y) * Rules.System.SPATIAL_HEIGHT_DIVISION / (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - Rules.Level.GROUND_Y));
        if (centerX < 0 || centerX > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || centerY < 0 || centerY > Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION) {
            return -1;
        }
        return (centerY * Rules.System.SPATIAL_WIDTH_DIVISION) + centerX;
    }

    private int getTopRightId(GameObject gameObject) {
        int topRightX = (int) ((gameObject.getCenterX() + gameObject.getWidth() / 2) * Rules.System.SPATIAL_WIDTH_DIVISION / Rules.System.Resolution.WIDTH_TARGET_RESOLUTION);
        int topRightY = (int) ((gameObject.getCenterY() - Rules.Level.GROUND_Y + gameObject.getHeight() / 2) * Rules.System.SPATIAL_HEIGHT_DIVISION / (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - Rules.Level.GROUND_Y));
        if (topRightX < 0 || topRightX > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || topRightY < 0 || topRightY > Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION) {
            return -1;
        }
        return (topRightY * Rules.System.SPATIAL_WIDTH_DIVISION) + topRightX;
    }

    private int getTopLeftId(GameObject gameObject) {
        int topLeftX = (int) ((gameObject.getCenterX() - gameObject.getWidth() / 2) * Rules.System.SPATIAL_WIDTH_DIVISION / Rules.System.Resolution.WIDTH_TARGET_RESOLUTION);
        int topLeftY = (int) ((gameObject.getCenterY() - Rules.Level.GROUND_Y + gameObject.getHeight() / 2) * Rules.System.SPATIAL_HEIGHT_DIVISION / (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - Rules.Level.GROUND_Y));
        if (topLeftX < 0 || topLeftX > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || topLeftY < 0 || topLeftY > Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION) {
            return -1;
        }
        return (topLeftY * Rules.System.SPATIAL_WIDTH_DIVISION) + topLeftX;
    }

    private int getBottomLeftId(GameObject gameObject) {
        int bottomLeftX = (int) ((gameObject.getCenterX() - gameObject.getWidth() / 2) * Rules.System.SPATIAL_WIDTH_DIVISION / Rules.System.Resolution.WIDTH_TARGET_RESOLUTION);
        int bottomLeftY = (int) ((gameObject.getCenterY() - gameObject.getHeight() / 2 - Rules.Level.GROUND_Y) * Rules.System.SPATIAL_HEIGHT_DIVISION / (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - Rules.Level.GROUND_Y));
        if (bottomLeftX < 0 || bottomLeftX > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || bottomLeftY < 0 || bottomLeftY > Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION) {
            return -1;
        }
        return (bottomLeftY * Rules.System.SPATIAL_WIDTH_DIVISION) + bottomLeftX;
    }

    private int getBottomRightId(GameObject gameObject) {
        int bottomRightX = (int) ((gameObject.getCenterX() + gameObject.getWidth() / 2) * Rules.System.SPATIAL_WIDTH_DIVISION / Rules.System.Resolution.WIDTH_TARGET_RESOLUTION);
        int bottomRightY = (int) ((gameObject.getCenterY() - gameObject.getHeight() / 2 - Rules.Level.GROUND_Y) * Rules.System.SPATIAL_HEIGHT_DIVISION / (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - Rules.Level.GROUND_Y));
        if (bottomRightX < 0 || bottomRightX > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION || bottomRightY < 0 || bottomRightY > Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION) {
            return -1;
        }
        return (bottomRightY * Rules.System.SPATIAL_WIDTH_DIVISION) + bottomRightX;
    }

    public void addObjectToSpatial(CollideableGameObject gameObject) {
        if (gameObject.isSingleSpatialCell()) {
            Array<CollideableGameObject> gameObjectsAtPosition = obtainGameObjectsAtPosition(getCenterId(gameObject));
            gameObjectsAtPosition.add(gameObject);
        } else {
            getPositionIds(gameObject);
            Iterator<Integer> iterator = spatialAux.iterator();
            while (iterator.hasNext()) {
                Array<CollideableGameObject> gameObjectsAtPosition = obtainGameObjectsAtPosition(iterator.next().intValue());
                gameObjectsAtPosition.add(gameObject);
            }
        }
    }

    public void getPositionIds(GameObject gameObject) {
        spatialAux.clear();
        addCorner(getBottomLeftId(gameObject));
        addCorner(getBottomRightId(gameObject));
        addCorner(getTopLeftId(gameObject));
        addCorner(getTopRightId(gameObject));
        addCorner(getCenterId(gameObject));
    }

    public void handleCollisions(CollideableGameObject gameObject) {
        getPositionIds(gameObject);
        Iterator<Integer> iterator = spatialAux.iterator();
        while (iterator.hasNext()) {
            checkCollisions(gameObject, spatial.get(iterator.next()));
        }
    }

    public void checkDistanceInteractions(DistanceInteractor gameObject) {
        float blastRadius = gameObject.getBlastRadius();
        if (blastRadius > 0) {
            Array<Paratrooper> paratroopers = (Array<Paratrooper>) elements.getAllParatroopers();
            for (int i = 0; i < paratroopers.size; i++) {
                Paratrooper paratrooper = paratroopers.get(i);
                if (GameUtils.getDistanceBetweenTwoObjects(gameObject, paratrooper) <= blastRadius) {
                    gameObject.radiusEffect(paratrooper);
                }
            }
        }
    }

    public void clearSpatial() {
        Iterator<Map.Entry<Integer, Array<CollideableGameObject>>> it = spatial.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry<Integer, Array<CollideableGameObject>> pair = it.next();
            it.remove();
            freeArray(pair.getValue());
        }
    }

    private void freeArray(Array<CollideableGameObject> array) {
        array.clear();
        arraysPool.addLast(array);
    }
}
