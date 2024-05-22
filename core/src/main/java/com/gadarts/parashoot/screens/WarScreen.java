package com.gadarts.parashoot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.FPSLogger;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.GroundUnit;
import com.gadarts.parashoot.enemies.Paratrooper;
import com.gadarts.parashoot.enemies.aircrafts.*;
import com.gadarts.parashoot.enemies.ground_crafts.APC;
import com.gadarts.parashoot.enemies.ground_crafts.Tank;
import com.gadarts.parashoot.enemies.paratroopers.BazookaGuy;
import com.gadarts.parashoot.enemies.paratroopers.Chaingunner;
import com.gadarts.parashoot.enemies.paratroopers.Infantry;
import com.gadarts.parashoot.enemies.paratroopers.Parachute;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.level_model.OrnamentAppearance;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.misc.DistanceInteractor;
import com.gadarts.parashoot.misc.IndependentEffect;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.misc.effects.CoinGainEffect;
import com.gadarts.parashoot.misc.effects.Light;
import com.gadarts.parashoot.misc.stuff.*;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.interfaces.Targeting;
import com.gadarts.parashoot.model.object_factories.BonusFactory;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.player.Bonus;
import com.gadarts.parashoot.player.SideKick;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.player.bunker.Turret;
import com.gadarts.parashoot.player.sidekick.*;
import com.gadarts.parashoot.screens.Menus.LevelSelectionScreen;
import com.gadarts.parashoot.screens.Menus.SceneSelectionScreen;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.*;

import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by Gad on 29/09/2015.
 */
public class WarScreen extends BasicScreen {

    private final PlayerStats oldStats;
    private WarScreenElements elements;
    private Factories factories;
    private HUD hud;
    private Map<Integer, Array<? extends GameObject>> objectsMap;
    private OrthographicCamera hudCamera;
    private FPSLogger fpsLogger;
    private PlayerHandler playerHandler;
    private Painter dali;
    private Array<Array<Enemy>> allEnemies;
    private boolean screenPaused;
    private InputMultiplexer multiplexer;
    private Map<Bunker.PlayerAttributes, Object> playerAttributesForTesting;
    private HashMap<String, String> analyticsReportAttributes = new HashMap<String, String>();
    private long timerDelay;
    private Timer scheduler = new Timer();
    private HashMap<SideKick.SideKickAttributes, Object> sidekicksAttributesForTesting;
    private int sceneUnlockIndex;
    private CombatTouchHandler combatTouchHandler;
    private boolean pauseMessageEnabled = true;
    private double pauseLockDuration;
    private long lastScreenPausedTimeStamp;
    private Scene sceneCompleted;

    public WarScreen() {
        this(null);
    }

    public WarScreen(PlayerStats oldStats) {
        super();
        this.oldStats = oldStats;
        screenPaused = false;
    }

    @Override
    public void show() {
        super.show();
        Timer.instance().start();
        initializeHudCamera();
        initializeGameObjectsMap();
        playerHandler = new PlayerHandler();
        hud = new HUD(this);
        factories = new Factories(this);
        dali = new Painter(mainCamera, (SpriteBatch) menuStage.getBatch(), this);
        elements = new WarScreenElements(this);
        elements.initializeLevel();
        dali.initializeSky();
        dali.initializeGroundAndLandscape();
        factories.setElements(elements);
        Level currentLevel = elements.getCurrentLevel();
        Array<OrnamentAppearance> ornaments = currentLevel.getOrnaments();
        if (ornaments != null) {
            for (OrnamentAppearance ornamentAppearance : ornaments) {
                if (ornamentAppearance.isMoving()) {
                    currentLevel.startMovingOrnament(ornamentAppearance, ornamentAppearance.getY());
                } else {
                    factories.getMiscFactory().createOrnament(ornamentAppearance);
                }
            }
        }
        elements.getEffectsManager().initializeEffects();
        fpsLogger = new FPSLogger();
        createBunker();
        createSideKicks();
        initializeHud();
        initializeTouch();
        getLoadingDoors().toFront();
    }

    public void addObjectToMap(GameObject gameObject, int listName) {
        addObjectToMap(gameObject, listName, false);
    }
    public OrthographicCamera getMainCamera() {
        return mainCamera;
    }
    public void addObjectToMap(GameObject gameObject, int listName, boolean addFirst) {
        if (gameObject == null || listName == -1) {
            return;
        }
        Array<GameObject> list = (Array<GameObject>) objectsMap.get(listName);
        if (addFirst) list.insert(0, gameObject);
        else list.add(gameObject);
    }

    public HUD getHud() {
        return hud;
    }

    public Map<Integer, Array<? extends GameObject>> getObjectsMap() {
        return objectsMap;
    }

    public OrthographicCamera getHudCamera() {
        return hudCamera;
    }

    public FPSLogger getFpsLogger() {
        return fpsLogger;
    }

    public Painter getPainter() {
        return dali;
    }

    private void initializeGameObjectsMap() {
        allEnemies = new Array<Array<Enemy>>();
        objectsMap = new LinkedHashMap<Integer, Array<? extends GameObject>>();
        objectsMap.put(Rules.System.GameObjectTypes.MISC, new Array<Misc>());
        objectsMap.put(Rules.System.GameObjectTypes.PLAYER_BULLETS, new Array<Bullet>());
        objectsMap.put(Rules.System.GameObjectTypes.ENEMY_BULLETS, new Array<Bullet>());
        objectsMap.put(Rules.System.GameObjectTypes.PARATROOPERS, new Array<Paratrooper>());
        objectsMap.put(Rules.System.GameObjectTypes.AIR_CRAFTS, new Array<AirCraft>());
        objectsMap.put(Rules.System.GameObjectTypes.GROUND_CRAFTS, new Array<GroundUnit>());
        objectsMap.put(Rules.System.GameObjectTypes.EXPLOSIONS, new Array<Explosion>());
        objectsMap.put(Rules.System.GameObjectTypes.BONUSES, new Array<Bonus>());
        allEnemies.add((Array<Enemy>) objectsMap.get(Rules.System.GameObjectTypes.AIR_CRAFTS));
        allEnemies.add((Array<Enemy>) objectsMap.get(Rules.System.GameObjectTypes.GROUND_CRAFTS));
        allEnemies.add((Array<Enemy>) objectsMap.get(Rules.System.GameObjectTypes.PARATROOPERS));
    }

    private void createSideKicks() {
        if (sidekicksAttributesForTesting != null) {
            Object sidekick = sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.SELECTED_SIDEKICK);
            if (sidekick == null) {
                playerHandler.setLeftSideKick(null);
                playerHandler.setRightSideKick(null);
                return;
            }
            SideKickFactory sideKickFactory = factories.getSideKickFactory();
            playerHandler.setLeftSideKick(sideKickFactory.createSideKick((SideKickFactory.SideKickType) sidekick, Rules.Player.SideKicks.LEFT_POSITION_X));
            SideKick leftSideKick = playerHandler.getLeftSideKick();
            if (leftSideKick != null) {
                leftSideKick.setArmorLevel((Integer) sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.ARMOR_LEVEL));
                leftSideKick.setRateLevel((Integer) sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.SHOOTING_RATE_LEVEL));
                leftSideKick.setStrengthLevel((Integer) sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.STRENGTH_LEVEL));
            }
            playerHandler.setRightSideKick(sideKickFactory.createSideKick((SideKickFactory.SideKickType) sidekick, Rules.Player.SideKicks.RIGHT_POSITION_X));
            SideKick rightSideKick = playerHandler.getRightSideKick();
            if (rightSideKick != null) {
                rightSideKick.setArmorLevel((Integer) sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.ARMOR_LEVEL));
                rightSideKick.setRateLevel((Integer) sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.SHOOTING_RATE_LEVEL));
                rightSideKick.setStrengthLevel((Integer) sidekicksAttributesForTesting.get(SideKick.SideKickAttributes.STRENGTH_LEVEL));
            }
        } else {
            if (elements.getCurrentLevel().isSimpleCannonBallForced()) {
                return;
            }
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            WeaponType sidekick = playerStatsHandler.getSelectedSideKick();
            if (sidekick == null) {
                playerHandler.setLeftSideKick(null);
                playerHandler.setRightSideKick(null);
                return;
            }
            playerHandler.setLeftSideKick(createSideKick((SideKickFactory.SideKickType) sidekick, Rules.Player.SideKicks.LEFT_POSITION_X));
            playerHandler.setRightSideKick(createSideKick((SideKickFactory.SideKickType) sidekick, Rules.Player.SideKicks.RIGHT_POSITION_X));
        }
    }

    private SideKick createSideKick(SideKickFactory.SideKickType sidekickType, int position) {
        SideKick sideKick = factories.getSideKickFactory().createSideKick(sidekickType, position);
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        sideKick.setArmorLevel(playerStatsHandler.getSideKickAttribute(sidekickType, Rules.Player.UpgradeableAttribute.PlayerCharacterAttribute.ARMOR));
        sideKick.setRateLevel(playerStatsHandler.getSideKickAttribute(sidekickType, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE));
        sideKick.setStrengthLevel(playerStatsHandler.getSideKickAttribute(sidekickType, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH));
        return sideKick;
    }

    private void createBunker() {
        playerHandler.setBunker(new Bunker());
        Bunker bunker = playerHandler.getBunker();
        bunker.init(this);
        if (playerAttributesForTesting != null) {
            playerHandler.setTurret(new Turret(bunker.getCenterX(), (bunker.getY() + bunker.getHeight() / 2), (BulletType) playerAttributesForTesting.get(Bunker.PlayerAttributes.SELECTED_WEAPON), (Integer) playerAttributesForTesting.get(Bunker.PlayerAttributes.GENERATOR_LEVEL), this));
            Turret turret = playerHandler.getTurret();
            turret.setShootingRateLevel((Integer) playerAttributesForTesting.get(Bunker.PlayerAttributes.SHOOTING_RATE_LEVEL));
            turret.setStrengthLevel((Integer) playerAttributesForTesting.get(Bunker.PlayerAttributes.STRENGTH_LEVEL));
            bunker.setArmorLevel((Integer) playerAttributesForTesting.get(Bunker.PlayerAttributes.ARMOR_LEVEL));
        } else {
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            Turret turret;
            if (elements.getCurrentLevel().isSimpleCannonBallForced()) {
                WeaponType selectedCannon = BulletType.CANNON_BALL;
                turret = new Turret(bunker.getCenterX(), (bunker.getY() + bunker.getHeight() / 2), (BulletType) selectedCannon, playerStatsHandler.getBunkerGeneratorLevel(), this);
                turret.setShootingRateLevel(1);
                turret.setStrengthLevel(1);
            } else {
                WeaponType selectedCannon = playerStatsHandler.getSelectedCannon();
                turret = new Turret(bunker.getCenterX(), (bunker.getY() + bunker.getHeight() / 2), (BulletType) selectedCannon, playerStatsHandler.getBunkerGeneratorLevel(), this);
                turret.setShootingRateLevel(playerStatsHandler.getCannonAttribute((BulletType) selectedCannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.RATE));
                turret.setStrengthLevel(playerStatsHandler.getCannonAttribute((BulletType) selectedCannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.STRENGTH));
            }
            playerHandler.setTurret(turret);
            bunker.setArmorLevel(playerStatsHandler.getBunkerArmorLevel());
        }
    }

    private void initializeTouch() {
        combatTouchHandler = new CombatTouchHandler(this);
        multiplexer = new InputMultiplexer();
        multiplexer.addProcessor(hud);
        multiplexer.addProcessor(combatTouchHandler);
        Gdx.input.setInputProcessor(multiplexer);
    }

    private void initializeHud() {
        hud.getViewport().setCamera(hudCamera);
        hud.setBombsButtonState(false);
        hud.initializeBombsButtons();
    }

    private void initializeHudCamera() {
        hudCamera = new OrthographicCamera();
        hudCamera.setToOrtho(false, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
        menuStage.getViewport().setCamera(hudCamera);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        elements.getInteractionsManager().clearSpatial();
        hud.act(delta);
        if (screenPaused) {
            if (Gdx.input.isTouched() && !hud.isMenuOpen() && TimeUtils.timeSinceMillis(lastScreenPausedTimeStamp) > pauseLockDuration) {
                pauseLockDuration = 0;
                screenPaused = false;
                resume();
            }
        } else {
            updateObjects(delta);
            elements.getMessageDisplay().update();
        }
        dali.drawGame();
        if (!(getLoadingDoors().areOpen())) {
            menuStage.draw();
        }
    }

    @Override
    protected void onBackPressed() {
        HUD.HUDbutton optionsButton = hud.getOptionsButton();
        optionsButton.setChecked(!optionsButton.isChecked());
    }

    private boolean hasEnemies() {
        for (int i = 0; i < allEnemies.size; i++) {
            Array<Enemy> group = allEnemies.get(i);
            if (group.size != 0) {
                return true;
            }
        }
        return false;
    }

    private void updateObjects(float delta) {
        updateObject(playerHandler.getBunker(), delta);
        updateObject(playerHandler.getTurret(), delta);
        updateObject(playerHandler.getLeftSideKick(), delta);
        updateObject(playerHandler.getRightSideKick(), delta);
        Iterator it = objectsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Array list = (Array) pair.getValue();
            for (int i = 0; i < list.size; i++) {
                GameObject gameObject = (GameObject) list.get(i);
                updateObject(list, gameObject, delta);
            }
        }
        elements.getEffectsManager().onUpdate();
    }

    private void pauseObjectsTimers() {
        Timer.instance().stop();
        elements.getCurrentLevel().pause();
        playerHandler.getBunker().pauseTimer();
        playerHandler.getTurret().pauseTimer();
        if (playerHandler.getLeftSideKick() != null) {
            playerHandler.getLeftSideKick().pauseTimer();
            playerHandler.getRightSideKick().pauseTimer();
        }
        Iterator it = objectsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Array list = (Array) pair.getValue();
            pauseTimerListObject(list);
        }
    }

    private void resumeObjectsTimers(long delayMillis) {
        Timer.instance().delay(delayMillis);
        Timer.instance().start();
        elements.getCurrentLevel().resume(delayMillis);
        playerHandler.getBunker().resumeTimer(delayMillis);
        playerHandler.getTurret().resumeTimer(delayMillis);
        SideKick leftSideKick = playerHandler.getLeftSideKick();
        if (leftSideKick != null) {
            leftSideKick.resumeTimer(delayMillis);
            playerHandler.getRightSideKick().resumeTimer(delayMillis);
        }
        Iterator it = objectsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Array list = (Array) pair.getValue();
            resumeTimerListObject(list, delayMillis);
        }
    }

    private void stopTimers() {
        Timer.instance().clear();
        elements.getCurrentLevel().stopTimers();
        playerHandler.getBunker().stopAllTasks();
        playerHandler.getTurret().stopAllTasks();
        SideKick leftSideKick = playerHandler.getLeftSideKick();
        if (leftSideKick != null) {
            leftSideKick.stopAllTasks();
            playerHandler.getRightSideKick().stopAllTasks();
        }
        Iterator it = objectsMap.entrySet().iterator();
        while (it.hasNext()) {
            Map.Entry pair = (Map.Entry) it.next();
            Array list = (Array) pair.getValue();
            stopTimerListObject(list);
        }
    }

    private void updateObject(GameObject gameObject, float delta) {
        updateObject(null, gameObject, delta);
    }

    private void updateObject(Array list, GameObject gameObject, float delta) {
        if (gameObject == null) {
            return;
        }
        gameObject.onUpdate(delta);
        handleInterfaces(gameObject);
        if (gameObject.isOutside()) {
            gameObject.onOutside(elements.getCurrentLevel());
        }
        if (gameObject.isDestroyed()) {
            gameObject.onDestroy(false);
            removeObject(list, gameObject);
            gameObject.onTerminate(this);
        }
    }

    private void handleInterfaces(GameObject gameObject) {
        InteractionsManager interactionsManager = elements.getInteractionsManager();
        if (gameObject instanceof CollideableGameObject) {
            interactionsManager.handleCollisions((CollideableGameObject) gameObject);
        } else if (gameObject instanceof DistanceInteractor) {
            interactionsManager.checkDistanceInteractions((DistanceInteractor) gameObject);
        }
        if (gameObject instanceof Targeting) {
            Targeting targeting = (Targeting) gameObject;
            if (!targeting.hasTarget()) {
                if (targeting.isPlayerCharacter()) {
                    interactionsManager.setPlayerTargets();
                } else {
                    interactionsManager.setEnemyTarget(targeting);
                }
            }
        }
    }

    private void pauseTimerListObject(Array list) {
        for (int i = 0; i < list.size; i++) {
            GameObject gameObject = (GameObject) list.get(i);
            gameObject.pauseTimer();
        }
    }

    private void resumeTimerListObject(Array list, long delayMillis) {
        for (int i = 0; i < list.size; i++) {
            GameObject gameObject = (GameObject) list.get(i);
            gameObject.resumeTimer(delayMillis);
        }
    }

    private void stopTimerListObject(Array list) {
        for (int i = 0; i < list.size; i++) {
            GameObject gameObject = (GameObject) list.get(i);
            gameObject.stopAllTasks();
        }
    }

    private void removeObject(Array list, GameObject gameObject) {
        if (list == null) {
            return;
        }
        list.removeIndex(list.indexOf(gameObject, true));
    }

    @Override
    public void pause() {
        super.pause();
        lastScreenPausedTimeStamp = TimeUtils.millis();
        screenPaused = true;
        timerDelay = TimeUtils.nanosToMillis(TimeUtils.nanoTime());
        pauseObjectsTimers();
    }

    @Override
    public void resume() {
        super.resume();
        if (!screenPaused) {
            resumeObjectsTimers(TimeUtils.nanosToMillis(TimeUtils.nanoTime()) - timerDelay);
            hud.getGuide().clearChildren();
        }
    }

    @Override
    public void hide() {
        super.hide();
        objectsMap.clear();
        stopTimers();
        Parastrike.getSoundPlayer().stopMusic();
        clearPools();
        Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class).getData().setScale(1);
    }

    private void clearPools() {
        for (BonusFactory.BonusType bonus : BonusFactory.BonusType.values()) {
            Pools.get(bonus.getClassObject()).clear();
        }
        Pools.get(EnemyMiniGunBullet.class).clear();
        Pools.get(Bullet.class).clear();
        Pools.get(Fire.class).clear();
        Pools.get(ZeppelinBomb.class).clear();
        Pools.get(BlasterBullet.class).clear();
        Pools.get(Flame.class).clear();
        Pools.get(ShockWave.class).clear();
        Pools.get(Light.class).clear();
        clearEnemiesPools();
        Pools.get(Explosion.class).clear();
        Pools.get(IndependentEffect.class).clear();
        Pools.get(Electricity.class).clear();
        Pools.get(AllyPlane.class).clear();
        Pools.get(AllyFallingBomb.class).clear();
        Pools.get(Parachute.class).clear();
        Pools.get(FlyingPart.class).clear();
        Pools.get(Ornament.class).clear();
        clearSideKicksPools();
        Pools.get(CoinGainEffect.class).clear();
    }

    private void clearEnemiesPools() {
        Pools.get(Scout.class).clear();
        Pools.get(Zeppelin.class).clear();
        Pools.get(Ballistic.class).clear();
        Pools.get(Yak.class).clear();
        Pools.get(Apache.class).clear();
        Pools.get(BonusScout.class).clear();
        Pools.get(Osprey.class).clear();
        Pools.get(APC.class).clear();
        Pools.get(Tank.class).clear();
        Pools.get(Infantry.class).clear();
        Pools.get(Chaingunner.class).clear();
        Pools.get(BazookaGuy.class).clear();
    }

    private void clearSideKicksPools() {
        Pools.get(InfantryTower.class).clear();
        Pools.get(Tesla.class).clear();
        Pools.get(Flamer.class).clear();
        Pools.get(HeatTurret.class).clear();
        Pools.get(Jaguar.class).clear();
        Pools.get(Dome.class).clear();
        Pools.get(Phantom.class).clear();
        Pools.get(BeamTurretHead.class).clear();
        Pools.get(FlamerHead.class).clear();
        Pools.get(JaguarTurret.class).clear();
    }

    public void endBattle() {
        elements.getCurrentLevel().end();
        returnToMenu();
    }


    private void returnToMenu() {
        Parastrike.getGGS().setShouldSaveGame(true);
        closeDoorsAndGoToScreen(new Timer.Task() {
            @Override
            public void run() {
                Parastrike.getAssetsManager().loadDataMenus();
            }
        }, TASK_RETURN_TO_MENU);
    }

    public boolean isPaused() {
        return screenPaused;
    }

    public static boolean isSideKickDead(SideKick sideKick) {
        if (sideKick != null) {
            return sideKick.isDead();
        }
        return true;
    }

    public Array<Array<Enemy>> getAllEnemies() {
        return allEnemies;
    }

    public Array<? extends GameCharacter> getAllParatroopers() {
        return (Array<? extends GameCharacter>) getObjectsMap().get(Rules.System.GameObjectTypes.PARATROOPERS);
    }

    public void setPlayerAttributesForTest(Map<Bunker.PlayerAttributes, Object> playerAttributes) {
        this.playerAttributesForTesting = playerAttributes;
    }

    public void destroyAllEnemies() {
        for (int i = 0; i < allEnemies.size; i++) {
            Array<Enemy> list = allEnemies.get(i);
            for (int j = 0; j < list.size; j++) {
                list.get(j).setHealth(0);
            }
        }
    }

    public void setSidekicksAttributesForTest(HashMap<SideKick.SideKickAttributes, Object> sideKicksAttributes) {
        this.sidekicksAttributesForTesting = sideKicksAttributes;
    }

    public void gameOver() {
        Level currentLevel = elements.getCurrentLevel();
        if (currentLevel.getState() == Level.States.GAME_OVER) return;
        handleGameOverSound();
        multiplexer.removeProcessor(1);
        playerHandler.getTurret().onDestroy();
        currentLevel.gameOver();
    }

    private static void handleGameOverSound() {
        SoundPlayer soundPlayer = Parastrike.getSoundPlayer();
        soundPlayer.stopMusic();
    }

    public void setSceneUnlockIndex(int sceneIndex) {
        sceneUnlockIndex = sceneIndex;
    }


    public void onEnemyTermination(Enemy enemy) {
        if (!GameSettings.ENEMY_TESTING) {
            Level currentLevel = elements.getCurrentLevel();
            if (((!currentLevel.hasAppearances() && !hasEnemies())) && currentLevel.getState() != Level.States.ACCOMPLISHED) {
                currentLevel.accomplish();
            }
        }
    }

    public OrthographicCamera getParastrikeCamera() {
        return mainCamera;
    }

    public WarScreenElements getElements() {
        return elements;
    }

    public Factories getFactories() {
        return factories;
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    private Timer.Task TASK_RETURN_TO_MENU = new Timer.Task() {
        @Override
        public void run() {
            markTutorial();
            decideWhereToGo();
            Parastrike.getSoundPlayer().playMusic(SFX.Music.MENU);
        }

        private void decideWhereToGo() {
            if (sceneUnlockIndex > 0) {
                returnAndShowSomething(SceneSelectionScreen.EVENT_SCENE_UNLOCKED, sceneUnlockIndex, Parastrike.MenuType.SCENE_SELECTION);
            } else if (sceneCompleted != null) {
                returnAndShowSomething(LevelSelectionScreen.EVENT_SCENE_COMPLETED, 1, Parastrike.MenuType.LEVEL_SELECTION);
            } else Parastrike.getInstance().goToMenuScreen(Parastrike.MenuType.LOBBY);
        }

        private void returnAndShowSomething(String event, int data, Parastrike.MenuType screenDestination) {
            HashMap<String, Integer> info = new HashMap<String, Integer>();
            info.put(event, data);
            Parastrike.getInstance().goToMenuScreen(screenDestination, info);
        }

        private void markTutorial() {
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            if (playerStatsHandler.getSelectedScene() == null) {
                playerStatsHandler.setSelectedScene(Scene.values()[0]);
                Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.InGameGuides.PREF_GUIDES);
                preferences.putBoolean(Assets.Configs.Preferences.InGameGuides.TUTORIAL_COMPLETE, true);
                preferences.flush();
            }
        }
    };


    public CombatTouchHandler getCombatTouchHandler() {
        return combatTouchHandler;
    }

    public boolean isPauseMessageEnabled() {
        return pauseMessageEnabled;
    }

    public void setPauseMessage(boolean pauseMessage) {
        this.pauseMessageEnabled = pauseMessage;
    }

    public void setPauseLock(double pauseLockDuration) {
        this.pauseLockDuration = pauseLockDuration * 1000;
    }

    public void sceneCompleted(Scene currentScene) {
        sceneCompleted = currentScene;
    }

    public PlayerStats getOldStats() {
        return oldStats;
    }
}
