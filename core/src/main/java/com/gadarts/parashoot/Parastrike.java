package com.gadarts.parashoot;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.ApplicationListener;
import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.profiling.GL20Interceptor;
import com.badlogic.gdx.graphics.profiling.GLProfiler;
import com.badlogic.gdx.utils.TimeUtils;
import com.gadarts.parashoot.assets.AssetManagerWrapper;
import com.gadarts.parashoot.level_model.LevelSkill;
import com.gadarts.parashoot.model.ActionResolver;
import com.gadarts.parashoot.model.GGSActionResolver;
import com.gadarts.parashoot.model.MentorsFactory;
import com.gadarts.parashoot.model.MentorsManager;
import com.gadarts.parashoot.model.PlayerStats;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.model.SoundPlayer;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.player.SideKick;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.screens.BasicMenuScreen;
import com.gadarts.parashoot.screens.Menus.HelpScreen;
import com.gadarts.parashoot.screens.Menus.IntroScreen;
import com.gadarts.parashoot.screens.Menus.LevelSelectionScreen;
import com.gadarts.parashoot.screens.Menus.LobbyScreen;
import com.gadarts.parashoot.screens.Menus.OptionsScreen;
import com.gadarts.parashoot.screens.Menus.SceneSelectionScreen;
import com.gadarts.parashoot.screens.Menus.ShopScreenImproved;
import com.gadarts.parashoot.screens.Menus.WelcomeScreen;
import com.gadarts.parashoot.screens.TestMenuScreen;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.util.HashMap;

import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.DAILY_GIFT_APPEAR;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.DAILY_GIFT_LAST_ENABLED;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.DONT_ASK_TO_LOGIN;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.PREF_SETTINGS;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.SOUND;

/** {@link com.badlogic.gdx.ApplicationListener} implementation shared by all platforms. */
@SuppressWarnings("unchecked")
public class Parastrike extends Game implements ApplicationListener {
    private static final int GL_LOG_INTERVAL = 1000;
    private static PlayerStatsHandler playerStatsHandler;
    private static AssetManagerWrapper assetsManager;
    private static SoundPlayer soundPlayer;
    private static Parastrike instance;

    public final ActionResolver actionResolver;
    private static MentorsManager mentorsManager;
    private final GGSActionResolver ggsActionResolver;
    private long lastGLlog;
    private GLProfiler glProfiler;
    private MentorsFactory mentorsFactory;

    public Parastrike(ActionResolver actionResolver, GGSActionResolver ggsActionResolver) {
        this.actionResolver = actionResolver;
        this.ggsActionResolver = ggsActionResolver;
        instance = this;
    }

    public static MentorsManager getMentorsManager() {
        return mentorsManager;
    }

    @Override
    public void create() {
        if (!Gdx.app.getPreferences(PREF_SETTINGS).getBoolean(DONT_ASK_TO_LOGIN, false)) {
            ggsActionResolver.login();
        }
        glProfiler = new GLProfiler(Gdx.graphics);
        playerStatsHandler = new PlayerStatsHandler();
        assetsManager = new AssetManagerWrapper();
        Gdx.app.setLogLevel(GameSettings.GDX_DEBUG_LEVEL);
        assetsManager.getLogger().setLevel(GameSettings.ASSET_MANAGER_DEBUG_LEVEL);
        soundPlayer = new SoundPlayer();
        GameSettings.SOUND_TOGGLE = GameSettings.SOUND_TOGGLE ? Gdx.app.getPreferences(PREF_SETTINGS).getBoolean(SOUND, true) : false;
        Gdx.input.setCatchBackKey(true);
        if (GameSettings.TEST_MENU_ACTIVE) {
            goToTestMenu();
        } else {
            Parastrike.getAssetsManager().firstLoad();
            goToMenuScreen(GameSettings.BEGIN_MENU_SCREEN);
        }
        actionResolver.StartSession();
        reportEntry();
        if (GameSettings.GL_PROFILE_ENABLED) {
            glProfiler.enable();
            lastGLlog = System.currentTimeMillis();
        }
        Preferences preferences = Gdx.app.getPreferences(PREF_SETTINGS);
        long millis = TimeUtils.millis();
        long lastEnabledTimeStamp = preferences.getLong(DAILY_GIFT_LAST_ENABLED, 0);
        if (preferences.getBoolean(DAILY_GIFT_APPEAR, true) || millis > lastEnabledTimeStamp + Rules.Menu.Lobby.Gift.NEW_PASS_TIME_INTERVAL) {
            preferences.putBoolean(DAILY_GIFT_APPEAR, true);
            preferences.putLong(DAILY_GIFT_LAST_ENABLED, millis);
            preferences.flush();
        }
        mentorsManager = new MentorsManager();
        mentorsFactory = new MentorsFactory();
    }

    private void reportEntry() {
        PlayerStatsHandler.reportEvent(Rules.System.Analytics.Events.ENTERED_GAME);
    }

    private void logGLvalues() {
        Gdx.app.log("GL Report - Calls", String.valueOf(glProfiler.getCalls()));
        Gdx.app.log("GL Report - Draw Calls", String.valueOf(glProfiler.getDrawCalls()));
        Gdx.app.log("GL Report - Shader Switches", String.valueOf(glProfiler.getShaderSwitches()));
        Gdx.app.log("GL Report - Bindings", String.valueOf(glProfiler.getTextureBindings()));
        Gdx.app.log("GL Report - Vertex Count", String.valueOf(glProfiler.getVertexCount().average));
        Gdx.app.log("GL Report - FPS", String.valueOf(Gdx.graphics.getFramesPerSecond()));
    }

    @Override
    public void render() {
        if (GameSettings.GL_PROFILE_ENABLED) {
            glProfiler.reset();
        }
        super.render();
        logGL();
    }

    private void logGL() {
        if (GameSettings.GL_PROFILE_ENABLED && GL_LOG_INTERVAL <= System.currentTimeMillis() - lastGLlog) {
            lastGLlog = System.currentTimeMillis();
            logGLvalues();
            logGLerror();
        }
    }

    private void logGLerror() {
        int errorNumber = Gdx.gl.glGetError();
        if (errorNumber != 0) {
            Gdx.app.error("GL Report - Errors", GL20Interceptor.resolveErrorNumber(errorNumber));
        }
    }

    public static Parastrike getInstance() {
        return instance;
    }

    public static AssetManagerWrapper getAssetsManager() {
        return assetsManager;
    }

    public static SoundPlayer getSoundPlayer() {
        return soundPlayer;
    }

    public static GGSActionResolver getGGS() {
        return getInstance().ggsActionResolver;
    }

    public static PlayerStatsHandler getPlayerStatsHandler() {
        return playerStatsHandler;
    }

    @Override
    public void resize(int width, int height) {
        //Nothing.
    }

    @Override
    public void pause() {
        screen.pause();
    }

    @Override
    public void resume() {
//        if (!TASK_AUTO_PAUSE_SOUND_POOL.isScheduled()) {
//            Timer.schedule(TASK_AUTO_PAUSE_SOUND_POOL, 1);
//        }
        assetsManager.finishLoading();
        screen.resume();
    }

    @Override
    public void dispose() {
        Parastrike.getInstance().actionResolver.EndSession();
        soundPlayer.dispose();
        screen.dispose();
    }

    public void goToBattle() {
        goToBattle(null);
    }

    public void goToBattle(PlayerStats oldStats) {
        WarScreen warScreen = new WarScreen(oldStats);
        setScreen(warScreen);
    }

    public void goToBattleTest(BulletType weapon, SideKickFactory.SideKickType sidekicks, int sideKickArmor, int sideKickShootingRate, int sideKickStrength, int weaponShootingRate, int weaponStrength, int enemyLevel, int playerArmor, int generator) {
        WarScreen warScreen = new WarScreen();
        HashMap<Bunker.PlayerAttributes, Object> playerAttributes = new HashMap<Bunker.PlayerAttributes, Object>();
        playerAttributes.put(Bunker.PlayerAttributes.ARMOR_LEVEL, playerArmor);
        playerAttributes.put(Bunker.PlayerAttributes.GENERATOR_LEVEL, generator);
        playerAttributes.put(Bunker.PlayerAttributes.SELECTED_WEAPON, weapon);
        playerAttributes.put(Bunker.PlayerAttributes.SHOOTING_RATE_LEVEL, weaponShootingRate);
        playerAttributes.put(Bunker.PlayerAttributes.STRENGTH_LEVEL, weaponStrength);
        warScreen.setPlayerAttributesForTest(playerAttributes);
        HashMap<SideKick.SideKickAttributes, Object> sideKicksAttributes = new HashMap<SideKick.SideKickAttributes, Object>();
        sideKicksAttributes.put(SideKick.SideKickAttributes.SELECTED_SIDEKICK, sidekicks);
        sideKicksAttributes.put(SideKick.SideKickAttributes.ARMOR_LEVEL, sideKickArmor);
        sideKicksAttributes.put(SideKick.SideKickAttributes.SHOOTING_RATE_LEVEL, sideKickShootingRate);
        sideKicksAttributes.put(SideKick.SideKickAttributes.STRENGTH_LEVEL, sideKickStrength);
        warScreen.setSidekicksAttributesForTest(sideKicksAttributes);
        playerStatsHandler.setSelectedLevelSkill(LevelSkill.values()[enemyLevel]);
        setScreen(warScreen);
    }

    public void goToTestMenu() {
        assetsManager.loadDataTest();
        setScreen(new TestMenuScreen(this));
        soundPlayer.stopMusic();
    }

    public void goToMenuScreen(Parastrike.MenuType type) {
        goToMenuScreen(type, null);
    }

    public void goToMenuScreen(Parastrike.MenuType type, HashMap<String, Integer> additionalInfo) {
        if (type != Parastrike.MenuType.INTRO && !assetsManager.isMenuLoaded()) {
            assetsManager.loadDataMenus();
            assetsManager.finishLoading();
        }
        BasicMenuScreen currentMenuScreen;
        try {
            currentMenuScreen = (BasicMenuScreen) type.getValue().newInstance();
            if (additionalInfo != null) {
                currentMenuScreen.invokeAdditionalInfo(additionalInfo);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }
        setScreen(currentMenuScreen);
        currentMenuScreen.setMentorsFactory(mentorsFactory);
    }

    public ActionResolver getActionResolver() {
        return actionResolver;
    }

    public enum MenuType {

        //        SHOP(ShopScreen.class),
        SHOP(ShopScreenImproved.class),
        SCENE_SELECTION(SceneSelectionScreen.class),
        LEVEL_SELECTION(LevelSelectionScreen.class),
        LOBBY(LobbyScreen.class),
        WELCOME(WelcomeScreen.class),
        INTRO(IntroScreen.class),
        HELP(HelpScreen.class),
        OPTIONS(OptionsScreen.class);

        private final Class value;

        MenuType(Class value) {
            this.value = value;
        }

        public Class getValue() {
            return value;
        }

    }

//    /**
//     * This is for the strangest bug! Couldn't detect it's cause! The bug where you get back from war screen, pause the game, return and all the looping sounds from the war screen are played!
//     */
//    private final Timer.Task TASK_AUTO_PAUSE_SOUND_POOL = new Timer.Task() {
//        @Override
//        public void run() {
//            Main.getInstance().actionResolver.autoPauseSoundPool();
//        }
//    };
}
