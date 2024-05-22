package com.gadarts.parashoot.level_model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.JsonValue;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.AssetManagerWrapper;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.model.object_factories.BonusFactory;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;
import com.gadarts.parashoot.model.tutorial.Tutor;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

import java.util.HashMap;

import static com.gadarts.parashoot.level_model.Level.States.GAME_OVER;

public class Level {

    private final boolean forceCannonBall;
    private final Array<SFX.Misc> ambSounds;
    private Queue<Event> events;
    private String openingMessage;
    private boolean playGoodLuckTaunt;
    private String levelCompleteMessage;
    private boolean featsAllowed;
    private Scene scene;
    private Array<OrnamentAppearance> ornaments;
    private int landscapeIndex;
    private boolean allowGain;
    private boolean showOptionsButton;
    private boolean showBombsButton;
    private String showOptionsButtonKey;
    private HUD hud;
    private final boolean showStatistics;
    private String fileName;
    private String name;
    private Array<BonusFactory.BonusType> allowedBonuses = new Array<BonusFactory.BonusType>();
    private Array<EnemyFactory.EnemyType> allowedParatroopers = new Array<EnemyFactory.EnemyType>();
    private Array<EnemyAppearance> enemyAppearances;
    private int totalEnemies;
    private Rules.Level.GlobalEffects.WeatherTypes weather;
    private Rules.Level.GlobalEffects.SkyType sky;
    private States state = States.PREPARING;
    private Timer levelTimer = new Timer();
    private SFX.Music music;
    private WarScreenElements elements;
    private Factories factories;
    private HashMap<Feat, Boolean> feats = new HashMap<Feat, Boolean>();

    public Level(String fileName, String name, Array<EnemyAppearance> array, Object allowedBonuses, Rules.Level.GlobalEffects.SkyType sky, Rules.Level.GlobalEffects.WeatherTypes weather, Object allowedParatroopers, SFX.Music music, boolean showStatistics, Queue<Event> events, String openingMessage, boolean playGoodLuckTaunt, String levelCompleteMessage, boolean allowFeats, Scene scene, Array<OrnamentAppearance> ornaments, Array<SFX.Misc> ambSounds, int landscapeIndex, boolean allowGain, JsonValue showOptionsButton, boolean showBombsButton, boolean forceCannonBall) {
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        featsAllowed = (playerStatsHandler.getLevelState(scene, playerStatsHandler.getSelectedLevel()) == LevelState.values()[LevelState.values().length - 1]) ? false : allowFeats;
        this.landscapeIndex = landscapeIndex;
        this.levelCompleteMessage = levelCompleteMessage;
        this.allowGain = allowGain;
        this.playGoodLuckTaunt = playGoodLuckTaunt;
        this.openingMessage = openingMessage;
        this.events = events;
        this.enemyAppearances = array;
        this.totalEnemies = array.size;
        this.fileName = fileName;
        this.name = name;
        this.forceCannonBall = forceCannonBall;
        this.showStatistics = showStatistics;
        if (showOptionsButton.isBoolean()) {
            this.showOptionsButton = showOptionsButton.asBoolean();
        } else if (showOptionsButton.isString()) {
            String s = showOptionsButton.asString();
            this.showOptionsButton = Gdx.app.getPreferences(Assets.Configs.Preferences.InGameGuides.PREF_GUIDES).getBoolean(s, false);
            this.showOptionsButtonKey = s;
        }
        this.showBombsButton = showBombsButton;
        this.scene = scene;
        this.ornaments = ornaments;
        this.ambSounds = ambSounds;
        removeEnemiesBySkill();
        //noinspection ConstantConditions
        this.sky = sky;
        //noinspection ConstantConditions
        this.weather = (GameSettings.FORCE_WEATHER == null) ? weather : GameSettings.FORCE_WEATHER;
        this.music = music;
        if (allowedBonuses instanceof String[]) {
            String[] bonuses = (String[]) allowedBonuses;
            for (String bonus : bonuses) {
                this.allowedBonuses.add(BonusFactory.BonusType.valueOf(bonus));
            }
        } else if (allowedBonuses instanceof Boolean) {
            Boolean bonuses = (Boolean) allowedBonuses;
            if (bonuses) {
                this.allowedBonuses.addAll(BonusFactory.BonusType.values());
            }
        }
        if (GameSettings.ENEMY_TESTING) {
            this.allowedParatroopers.add(EnemyFactory.EnemyType.INFANTRY);
            this.allowedParatroopers.add(EnemyFactory.EnemyType.BAZOOKA_GUY);
            this.allowedParatroopers.add(EnemyFactory.EnemyType.CHAINGUNNER);
        } else if (allowedParatroopers instanceof Boolean) {
            Boolean paratroopersAllowed = (Boolean) allowedParatroopers;
            if (!paratroopersAllowed || GameSettings.ENEMY_TESTING) {
                this.allowedParatroopers.addAll(EnemyFactory.EnemyType.values());
            }
        } else if (allowedParatroopers instanceof String[]) {
            String[] paratroopersAllowed = (String[]) allowedParatroopers;
            for (String paratrooperType : paratroopersAllowed) {
                this.allowedParatroopers.add(EnemyFactory.EnemyType.valueOf(paratrooperType));
            }
        }

        if (featsAllowed) {
            for (Feat feat : Feat.values()) {
                feats.put(feat, true);
            }
        }
    }

    private void removeEnemiesBySkill() {
        for (int i = 0; i < enemyAppearances.size; i++) {
            EnemyAppearance enemyAppearance = enemyAppearances.get(i);
            if (enemyAppearance.getMinimumSkill().compareTo(Parastrike.getPlayerStatsHandler().getSelectedLevelSkill()) > 0) {
                enemyAppearances.removeValue(enemyAppearance, true);
                i--;
            }
        }
    }

    public SFX.Music getMusicName() {
        return music;
    }

    private EnemyAppearance extractNextAppearance() {
        EnemyAppearance first = enemyAppearances.first();
        enemyAppearances.removeIndex(0);
        return first;
    }


    public boolean hasAppearances() {
        return enemyAppearances.size != 0;
    }

    public void start(WarScreenElements elements, Factories factories, HUD hud) {
        this.factories = factories;
        this.hud = hud;
        this.elements = elements;
        Parastrike.getSoundPlayer().playMusic(music);
        levelTimer.start();
        levelTimer.scheduleTask(TASK_BEGIN, Rules.Level.LevelStructure.BEGIN_DELAY);
    }

    public void pause() {
        levelTimer.stop();
    }

    public Array<EnemyFactory.EnemyType> getAllowedParatroopers() {
        return allowedParatroopers;
    }

    @SuppressWarnings("unused")
    public boolean isBonusAllowed(BonusFactory.BonusType bonus) {
        return allowedBonuses.contains(bonus, true);
    }

    public States getState() {
        return state;
    }

    public void accomplish() {
        state = States.ACCOMPLISHED;
        if (showOptionsButtonKey != null) {
            Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).putBoolean(showOptionsButtonKey, true).flush();
        }
        MessageDisplay messageDisplay = elements.getMessageDisplay();
        messageDisplay.add(levelCompleteMessage);
        messageDisplay.taunt(SFX.Taunts.MISSION_ACCOMPLISHED);
        if (!TASK_SHOW_STATISTICS.isScheduled()) {
            levelTimer.scheduleTask(TASK_SHOW_STATISTICS, Rules.Level.LevelStructure.END_DELAY);
        }
        if (allowGain) {
            handleFeats();
            advanceLevelsStates();
        }
    }

    private void handleFeats() {
        if (!featsAllowed) {
            return;
        }
        for (Feat feat : Feat.values()) {
            Boolean featAchieved = feats.get(feat);
            if (featAchieved != null && featAchieved) {
                ScoresHandler scoresHandler = elements.getScoresHandler();
                scoresHandler.addCoins((int) (scoresHandler.getSessionCoins() * 0.5));
            }
        }
    }

    private void advanceLevelsStates() {
        if (Parastrike.getPlayerStatsHandler().getSelectedScene() == null) {
            return;
        }
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        Scene selectedScene = playerStatsHandler.getSelectedScene();
        Scene currentScene = selectedScene;
        int currentLevel = playerStatsHandler.getSelectedLevel();
        LevelState state = playerStatsHandler.getLevelState(currentScene, currentLevel);
        LevelState[] states = LevelState.values();
        int currentStateOrdinal = state.ordinal();
        Scene[] scenes = Scene.values();
        int currentSceneIndex = currentScene.ordinal();
        if (currentStateOrdinal < states.length - 1) {
            int newLevelStarsStateIndex = currentStateOrdinal + 1;
            playerStatsHandler.setLevelState(currentScene, currentLevel, states[newLevelStarsStateIndex]);
            playerStatsHandler.setStars(playerStatsHandler.getStars() + 1);
            ScoresHandler scoresHandler = getElements().getScoresHandler();
            int starCoinsReward = (Rules.Level.BASIC_LEVEL_STAR_COINS_WORTH * (newLevelStarsStateIndex - 1)) * (currentSceneIndex + 1);
            scoresHandler.addCoins(starCoinsReward);
            int nextLevel = currentLevel + 1;
            AssetManagerWrapper assetsManager = Parastrike.getAssetsManager();
            int numOfLevelsInScene = assetsManager.calculateNumberOfLevelsInScene(selectedScene);
            if (currentLevel < numOfLevelsInScene && playerStatsHandler.getLevelState(currentScene, nextLevel) == LevelState.LOCKED) {
                playerStatsHandler.setLevelState(currentScene, nextLevel, LevelState.NO_STARS);
            } else {
                boolean sceneCompleted = true;
                for (int i = 1; i <= numOfLevelsInScene; i++) {
                    LevelState[] levelStates = LevelState.values();
                    if (playerStatsHandler.getLevelState(currentScene, i) != levelStates[levelStates.length - 1]) {
                        sceneCompleted = false;
                        break;
                    }
                }
                if (sceneCompleted) {
                    elements.sceneCompleted(currentScene);
                    playerStatsHandler.setCoins(playerStatsHandler.getCoins() + Rules.Menu.LevelSelection.SceneCompletedMonitor.REWARD);
                }
            }
        }
        if (scenes[scenes.length - 1] != currentScene) {
            for (Scene scene : Scene.values()) {
                if (!playerStatsHandler.isSceneEnabled(scene)) {
                    if (scene.getCoinsTarget() <= playerStatsHandler.getCoins() && scene.getStarsTarget() <= playerStatsHandler.getStars()) {
                        playerStatsHandler.setLevelState(scene, 1, LevelState.NO_STARS);
                        playerStatsHandler.enableScene(scene);
                        elements.setSceneUnlockIndex(scene.ordinal());
                    } else {
                        break;
                    }
                }
            }
        }
    }

    private void generateEnemy() {
        if (!GameSettings.ALLOW_ENEMIES || (!hasAppearances() && !GameSettings.ENEMY_TESTING)) {
            return;
        } else if (GameSettings.ENEMY_TESTING) {
            testEnemyGenerating();
            return;
        }
        final EnemyAppearance enemyAppearance = extractNextAppearance();
        int lastTiming = enemyAppearance.getTiming();
        EnemyAppearance.Alignment alignment = enemyAppearance.getAlignment();
        EnemyFactory.EnemyType type = enemyAppearance.getType();
        float y;
        if (type == EnemyFactory.EnemyType.YAK || type == EnemyFactory.EnemyType.SUPER_YAK || type == EnemyFactory.EnemyType.MEDIC_YAK) {
            y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + 100;
        } else if (type == EnemyFactory.EnemyType.BOSS) {
            y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
        } else if (type == EnemyFactory.EnemyType.TANK || type == EnemyFactory.EnemyType.APC) {
            y = Rules.Level.GROUND_Y;
        } else {
            y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
        }
        if (alignment == EnemyAppearance.Alignment.RANDOM) {
            alignment = (MathUtils.randomBoolean()) ? EnemyAppearance.Alignment.LEFT : EnemyAppearance.Alignment.RIGHT;
        }
        int x;
        EnemyFactory enemyFactory = factories.getEnemyFactory();
        int direction;
        if (alignment == EnemyAppearance.Alignment.LEFT) {
            x = Rules.Misc.OUTSIDE_ENEMY_DISTANCE_LEFT_SIDE;
            direction = 0;
        } else {
            x = Rules.Misc.OUTSIDE_ENEMY_DISTANCE_RIGHT_SIDE;
            direction = 180;
        }
        final Enemy enemy = enemyFactory.createEnemy(type, x, y, direction);
        final JsonValue focusMessage = enemyAppearance.getFocusParameters();
        if (focusMessage != null) {
            levelTimer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    Tutor tutor = hud.getGuide().createTutor(Tutor.TutorType.IN_GAME_FOCUS, focusMessage);
                    tutor.setInGameTarget(enemy);
                    getElements().pauseGame(false);
                }
            }, focusMessage.getInt(Rules.Level.LevelStructure.EnemyAppearance.Focus.DELAY, 1));
        }
        if (hasAppearances()) {
            levelTimer.scheduleTask(TASK_GENERATE_ENEMY, Math.max(enemyAppearances.first().getTiming() - lastTiming, 0));
        }
    }

    @SuppressWarnings("ConstantConditions")
    private void testEnemyGenerating() {
        boolean createOnLeftSide = MathUtils.randomBoolean();
        EnemyFactory.EnemyType type;
        float y = 0;
        if (GameSettings.CREATE_ONLY_SPECIFIC_ENEMY == null) {
            int chance = MathUtils.random(11);
            type = null;
            switch (chance) {
                case 0:
                    type = EnemyFactory.EnemyType.SCOUT;
                    y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
                    break;

                case 1:
                    type = EnemyFactory.EnemyType.OSPREY;
                    y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
                    break;

                case 2:
                    type = EnemyFactory.EnemyType.APC;
                    y = Rules.Level.GROUND_Y;
                    break;

                case 3:
                    type = EnemyFactory.EnemyType.YAK;
                    y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + 100;
                    break;

                case 4:
                    type = EnemyFactory.EnemyType.SUPER_YAK;
                    y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + 100;
                    break;

                case 5:
                    type = EnemyFactory.EnemyType.BONUS_SCOUT;
                    y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
                    break;

                case 6:
                    type = EnemyFactory.EnemyType.BALLISTIC;
                    y = Rules.Level.GROUND_Y;
                    break;

                case 7:
                    type = EnemyFactory.EnemyType.APACHE;
                    y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
                    break;

                case 8:
                    type = EnemyFactory.EnemyType.ZEPPELIN;
                    y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
                    break;

                case 9:
                    type = EnemyFactory.EnemyType.TANK;
                    y = Rules.Level.GROUND_Y;
                    break;

                case 10:
                    type = EnemyFactory.EnemyType.BASIC_SCOUT;
                    y = (float) (Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP - Math.random() * Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP_MIN);
                    break;
                case 11:
                    type = EnemyFactory.EnemyType.MEDIC_YAK;
                    y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + 100;
                    break;
            }
        } else {
            type = GameSettings.CREATE_ONLY_SPECIFIC_ENEMY;
            if (type == EnemyFactory.EnemyType.APC || type == EnemyFactory.EnemyType.BALLISTIC || type == EnemyFactory.EnemyType.TANK) {
                y = Rules.Level.GROUND_Y;
            } else if (type == EnemyFactory.EnemyType.YAK || type == EnemyFactory.EnemyType.SUPER_YAK) {
                y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + 100;
            } else {
                y = Rules.Enemies.GeneralAttributes.AIRCRAFTS_Y_FROM_TOP;
            }
        }
        int x;
        int direction;
        if (createOnLeftSide) {
            x = Rules.Misc.OUTSIDE_ENEMY_DISTANCE_LEFT_SIDE;
            direction = 0;
        } else {
            x = Rules.Misc.OUTSIDE_ENEMY_DISTANCE_RIGHT_SIDE;
            direction = 180;
        }
        factories.getEnemyFactory().createEnemy(type, x, y, direction);
        levelTimer.scheduleTask(TASK_GENERATE_ENEMY, MathUtils.random(1, 4));
    }


    private final Timer.Task TASK_GENERATE_ENEMY = new Timer.Task() {
        @Override
        public void run() {
            generateEnemy();
        }
    };

    public void resume(long delayMillis) {
        levelTimer.delay(delayMillis);
        levelTimer.start();
    }

    public void end() {
        reportLevelEnd(getState());
    }

    public Rules.Level.GlobalEffects.SkyType getSky() {
        return sky;
    }

    public Rules.Level.GlobalEffects.WeatherTypes getWeather() {
        return weather;
    }

    public void AddToTotalEnemies() {
        totalEnemies++;
    }

    public int getTotalEnemies() {
        return totalEnemies;
    }

    public String getName() {
        return name;
    }

    public void gameOver() {
        state = GAME_OVER;
        for (Feat feat : Feat.values()) {
            failFeat(feat);
        }
        levelTimer.scheduleTask(TASK_GAME_OVER, Rules.Level.LevelStructure.END_DELAY);
    }

    public String getFileName() {
        return fileName;
    }

    public void stopTimers() {
        levelTimer.clear();
    }

    public Array<BonusFactory.BonusType> getAllowedBonuses() {
        return allowedBonuses;
    }

    private final Timer.Task TASK_BEGIN = new Timer.Task() {
        @Override
        public void run() {
            if (events.size > 0 && events.first() != null)
                levelTimer.scheduleTask(TASK_PERFORM_EVENT, events.first().getTiming());
            tauntGoodLuck();
            state = States.RUNNING;
            levelTimer.scheduleTask(TASK_GENERATE_ENEMY, enemyAppearances.first().getTiming());
            reportLevelBegin();
        }

        private void tauntGoodLuck() {
            MessageDisplay messageDisplay = elements.getMessageDisplay();
            messageDisplay.add(openingMessage);
            if (playGoodLuckTaunt) {
                messageDisplay.taunt(SFX.Taunts.GOOD_LUCK);
            }
        }
    };

    private void reportLevelBegin() {
        Preferences prefs = Gdx.app.getPreferences(Assets.Configs.Preferences.Player.PREF_PLAYER);
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(Rules.System.Analytics.Attributes.MenuScreen.COINS, String.valueOf(prefs.getInteger(Assets.Configs.Preferences.Player.COINS)));
        attributes.put(Rules.System.Analytics.Attributes.InGame.SELECTED_CANNON, prefs.getString(Assets.Configs.Preferences.Player.SELECTED_CANNON));
        attributes.put(Rules.System.Analytics.Attributes.InGame.SELECTED_SIDE_KICK, prefs.getString(Assets.Configs.Preferences.Player.SELECTED_SIDE_KICK));
        attributes.put(Rules.System.Analytics.Attributes.InGame.LEVEL_NUMBER, String.valueOf(Parastrike.getPlayerStatsHandler().getSelectedLevel()));
        attributes.put(Rules.System.Analytics.Attributes.InGame.LEVEL_SKILL, Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().name());
        Parastrike.getInstance().actionResolver.analyticsEventReport(Rules.System.Analytics.Events.LEVEL_BEGIN, attributes);
    }

    private void reportLevelEnd(States state) {
        Preferences prefs = Gdx.app.getPreferences(Assets.Configs.Preferences.Player.PREF_PLAYER);
        HashMap<String, String> attributes = new HashMap<String, String>();
        attributes.put(Rules.System.Analytics.Attributes.MenuScreen.COINS, String.valueOf(prefs.getInteger(Assets.Configs.Preferences.Player.COINS)));
        attributes.put(Rules.System.Analytics.Attributes.InGame.SELECTED_CANNON, prefs.getString(Assets.Configs.Preferences.Player.SELECTED_CANNON));
        attributes.put(Rules.System.Analytics.Attributes.InGame.SELECTED_SIDE_KICK, prefs.getString(Assets.Configs.Preferences.Player.SELECTED_SIDE_KICK));
        attributes.put(Rules.System.Analytics.Attributes.InGame.LEVEL_NUMBER, String.valueOf(Parastrike.getPlayerStatsHandler().getSelectedLevel()));
        attributes.put(Rules.System.Analytics.Attributes.InGame.LEVEL_SKILL, Parastrike.getPlayerStatsHandler().getSelectedLevelSkill().name());
        attributes.put(Rules.System.Analytics.Attributes.InGame.SCORE, String.valueOf(elements.getScoresHandler().getSessionScore()));
        attributes.put(Rules.System.Analytics.Attributes.InGame.STATE, state.name());
        Parastrike.getInstance().actionResolver.analyticsEventReport(Rules.System.Analytics.Events.LEVEL_END, attributes);
    }

    public WarScreenElements getElements() {
        return elements;
    }

    private final Timer.Task TASK_SHOW_STATISTICS = new Timer.Task() {
        @Override
        public void run() {
            if (showStatistics) {
                hud.showStatistics();
            } else {
                elements.endBattle();
            }
            Parastrike.getPlayerStatsHandler().commitStats();
            Parastrike.getGGS().submitScore(Parastrike.getPlayerStatsHandler().getScore());
        }
    };

    private final Timer.Task TASK_GAME_OVER = new Timer.Task() {
        @Override
        public void run() {
            MessageDisplay messageDisplay = elements.getMessageDisplay();
            messageDisplay.add(Assets.Strings.InGameMessages.GAME_OVER);
            messageDisplay.taunt(SFX.Taunts.GAME_OVER);
            levelTimer.scheduleTask(TASK_SHOW_STATISTICS, Rules.Level.LevelStructure.END_DELAY);
        }
    };

    public void failFeat(Feat feat) {
        failFeat(feat, true);
    }

    public void failFeat(Feat feat, boolean taunt) {
        if (!featsAllowed) {
            return;
        }
        feats.put(feat, false);
        if (taunt) getElements().getMessageDisplay().taunt(SFX.Taunts.ACHIEVEMENT_LOST);
    }

    public boolean getFeat(Feat feat) {
        return featsAllowed && feats.get(feat);
    }

    public Scene getScene() {
        return scene;
    }

    public Array<OrnamentAppearance> getOrnaments() {
        return ornaments;
    }

    public void startMovingOrnament(final OrnamentAppearance ornamentAppearance, final int yPosition) {
        levelTimer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                int minimumY = Rules.Level.Scenes.MovingOrnaments.MINIMUM_Y;
                int y = yPosition >= 0 ? yPosition : (int) (minimumY + Math.random() * (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - minimumY));
                int x = Math.random() > 0.5 ? Rules.Level.Scenes.MovingOrnaments.START_LEFT_SIDE : Rules.Level.Scenes.MovingOrnaments.START_RIGHT_SIDE;
                factories.getMiscFactory().createOrnament(ornamentAppearance, x, y);
                startMovingOrnament(ornamentAppearance, ornamentAppearance.getY());
            }
        }, (float) (Rules.Level.Scenes.MovingOrnaments.MINIMUM_INTERVAL + Math.random() * Rules.Level.Scenes.MovingOrnaments.MINIMUM_INTERVAL));
    }

    public String getSelectedLandscape() {
        return (landscapeIndex >= 0) ? scene.getLandscape()[landscapeIndex] : null;
    }

    public boolean isGainAllowed() {
        return allowGain;
    }

    public boolean isOptionsButtonShown() {
        return showOptionsButton;
    }

    public boolean isBombsButtonShown() {
        return showBombsButton;
    }

    public boolean isSimpleCannonBallForced() {
        return forceCannonBall;
    }

    public Array<SFX.Misc> getAmbSounds() {
        return ambSounds;
    }

    public enum States {
        PREPARING, RUNNING, PAUSED, GAME_OVER, ACCOMPLISHED
    }

    private final Timer.Task TASK_PERFORM_EVENT = new Timer.Task() {
        @Override
        public void run() {
            Event event = events.removeFirst();
            event.performEventAction(elements, hud);
            if (events.size > 0) {
                levelTimer.scheduleTask(TASK_PERFORM_EVENT, events.first().getTiming() - event.getTiming());
            }
        }
    };


    public enum Feat {RAMPAGE, FLAWLESS, PURE}
}
