package com.gadarts.parashoot.assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.AssetDescriptor;
import com.badlogic.gdx.assets.AssetLoaderParameters;
import com.badlogic.gdx.assets.AssetManager;
import com.badlogic.gdx.assets.loaders.AsynchronousAssetLoader;
import com.badlogic.gdx.assets.loaders.FileHandleResolver;
import com.badlogic.gdx.assets.loaders.ParticleEffectLoader;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.graphics.g2d.freetype.FreetypeFontLoader;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ArrayMap;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.level_model.*;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.JsonUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * The assets manager.
 */
public final class AssetManagerWrapper extends AssetManager {
    private ArrayMap<String, FileHandle> configs = new ArrayMap<String, FileHandle>(); // Holds all config files (such as the particle effects definitions).
    private boolean menuLoaded, inGameLoaded; // Indicates whether the menu and in-game data is loaded or not.
    private boolean loadingInProcess;// Indicates whether the asset manager is currently loading or not.

    public AssetManagerWrapper() {
        setLoader(Level.class, new LevelLoader(getFileHandleResolver()));
    }

    /**
     * Unloads menus data and loads all battle related assets for the given scene and level number.
     *
     * @param episode     Selected episode.
     * @param levelNumber Selected level number to load.
     */
    public void loadDataBattle(int episode, int levelNumber) {
        loadDataBattle(Assets.Levels.SCENE_FOLDER_NAME + "_" + episode + "/" + Assets.Levels.LEVEL_FOLDER_NAME + "_" + levelNumber);
    }

    /**
     * Unloads menus data and loads all battle related assets for the given level.
     *
     * @param fileName Selected level.
     */
    public void loadDataBattle(String fileName) {
        if (menuLoaded) unloadData(Assets.GFX.Sheets.Menus.FOLDER_NAME, SFX.Music.MENU);
        String level = finishLoadingLevelForBattle(fileName);
        Level currentLevel = Parastrike.getAssetsManager().get(level, Level.class);
        loadData(Assets.GFX.Sheets.InGame.FOLDER_NAME, currentLevel.getMusicName());
        inGameLoaded = true;
        menuLoaded = false;
    }

    /**
     * Loading and waits 'till it's finished the given level for battle.
     *
     * @param fileName Selected level file.
     * @return Returns the level filename.
     */
    private String finishLoadingLevelForBattle(String fileName) {
        String level = Parastrike.getAssetsManager().loadLevel(fileName);
        finishLoadingAsset(level);
        loadingSleep();
        return level;
    }

    /**
     * For some reason finishLoadingAsset finishes it's process but level is not fully loaded yet! But if you wait a second, it works!
     */
    private void loadingSleep() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /**
     * Unloads battle data and loads all menu related assets.
     */
    public void loadDataMenus() {
        if (inGameLoaded) {
            unloadedDataBattle();
        }
        loadData(Assets.GFX.Images.Menus.FOLDER_NAME, SFX.Music.MENU);
        inGameLoaded = false;
        menuLoaded = true;
    }

    /**
     * Unloads the battle data and levels.
     */
    private void unloadedDataBattle() {
        unloadData(Assets.GFX.Sheets.InGame.FOLDER_NAME);
        unloadLevels();
    }

    /**
     * Unloads all loaded levels.
     */
    private void unloadLevels() {
        Array<Level> levels = new Array<Level>();
        getAll(Level.class, levels);
        for (int i = 0; i < levels.size; i++) {
            unload(levels.get(i).getFileName());
        }
    }

    /**
     * Loads sheets, images and sounds from the given folder. Also loads the given music.
     *
     * @param folderName The folder to load from.
     * @param music      The music to load.
     */
    private void loadData(String folderName, SFX.Music music) {
        loadSheets(folderName);
        loadImages(folderName);
        loadSounds(folderName);
        loadMusic(music);
    }

    /**
     * Unloads sheets, images and sounds from the given folder. Also unloads all musics.
     *
     * @param folderName The folder to unload.
     */
    private void unloadData(String folderName) {
        unloadData(folderName, null);
    }

    /**
     * Unloads sheets, images and sounds from the given folder. Also unloads the given music and disposes the sound player.
     *
     * @param folderName The folder to unload.
     * @param music      The music to unload.
     */
    private void unloadData(String folderName, SFX.Music music) {
        Parastrike.getSoundPlayer().dispose();
        unloadGFX(folderName);
        unloadSounds(folderName);
        if (music != null) unloadMusic(music);
        else unloadMusic();
    }

    /**
     * Unloads the sheets and images in the given folder.
     *
     * @param folderName Selected folder's name.
     */
    private void unloadGFX(String folderName) {
        unloadSheets(folderName);
        unloadImages(folderName);
    }

    /**
     * Loads all assets for testing session.
     */
    public void loadDataTest() {
        loadSheets(Assets.GFX.Sheets.InGame.FOLDER_NAME);
        loadImages(Assets.GFX.Images.InGame.FOLDER_NAME);
        loadFonts();
        loadSounds(SFX.fileAttribute.IN_GAME_FOLDER_NAME.getValue());
        loadMusic(SFX.Music.BATTLE_1);
        loadParticlesConfigs();
        setLoader(Level.class, new LevelLoader(getFileHandleResolver()));
        finishLoading();
    }

    /**
     * Load specified level and return it's path.
     *
     * @param fileName The level's name (Looks in the 'levels' folder).
     */
    private String loadLevel(String fileName) {
        String fullPath = Gdx.files.internal(Assets.Levels.LEVELS_FOLDER_NAME).path() + "/" + fileName + "." + Assets.Levels.DATA_EXTENSION;
        load(fullPath, Level.class);
        return fullPath;
    }

    /**
     * Load specified music file and return it's file name.
     *
     * @param musicFile The music to load.
     */
    private String loadMusic(SFX.Music musicFile) {
        FileHandle dirHandle = Gdx.files.internal(SFX.fileAttribute.SFX_FOLDER_NAME.getValue()).child(musicFile.getParentDir().getValue()).child(musicFile.getValue() + "." + musicFile.getFormat().getValue());
        String path = dirHandle.path();
        load(path, Music.class);
        return path;
    }

    /**
     * Unload all music files.
     */
    private void unloadMusic() {
        FileHandle dirHandle = Gdx.files.internal(SFX.fileAttribute.SFX_FOLDER_NAME.getValue()).child(SFX.fileAttribute.MUSICS_FOLDER_NAME.getValue());
        for (FileHandle file : dirHandle.list()) {
            String path = file.path();
            if (file.extension().equalsIgnoreCase(SFX.fileAttribute.MUSIC_DATA_EXTENSION.getValue()) && isLoaded(path))
                unload(path);
        }
    }

    /**
     * Unload specified music file and return it's file name.
     *
     * @param musicFile The music to load.
     */
    private String unloadMusic(SFX.Music musicFile) {
        FileHandle dirHandle = Gdx.files.internal(SFX.fileAttribute.SFX_FOLDER_NAME.getValue()).child(musicFile.getParentDir().getValue()).child(musicFile.getValue() + "." + musicFile.getFormat().getValue());
        String path = dirHandle.path();
        unload(path);
        return path;
    }

    /**
     * Loads specified folder in the sheets folder.
     *
     * @param dirInSheets The name of the dir inside the sheets directory to load.
     */
    private void loadSheets(String dirInSheets) {
        FileHandle dirHandle = Gdx.files.internal(Assets.GFX.GFX_FOLDER_NAME).child(Assets.GFX.Sheets.SHEETS_FOLDER_NAME).child(dirInSheets);
        for (FileHandle file : dirHandle.list()) {
            if (file.extension().equalsIgnoreCase(Assets.GFX.Sheets.SHEETS_DATA_EXTENSION)) {
                load(file.path(), TextureAtlas.class);
            }
        }
    }

    /**
     * Unloads specified folder in the sheets folder.
     *
     * @param dirInSheets The name of the dir inside the sheets directory to unload.
     */
    private void unloadSheets(String dirInSheets) {
        FileHandle dirHandle = Gdx.files.internal(Assets.GFX.GFX_FOLDER_NAME).child(Assets.GFX.Sheets.SHEETS_FOLDER_NAME).child(dirInSheets);
        for (FileHandle file : dirHandle.list()) {
            if (file.extension().equalsIgnoreCase(Assets.GFX.Sheets.SHEETS_DATA_EXTENSION)) {
                unload(file.path());
            }
        }
    }

    /**
     * Loads specified folder in the images folder.
     *
     * @param specificDir The name of the dir inside the sheets directory to load.
     */
    private void loadImages(String specificDir) {
        FileHandle dirHandle = Gdx.files.internal(Assets.GFX.GFX_FOLDER_NAME).child(Assets.GFX.Images.FOLDER_NAME).child(specificDir);
        for (FileHandle file : dirHandle.list()) {
            if (file.isDirectory()) loadImages(specificDir + "/" + file.name());
            else if (file.extension().equalsIgnoreCase(Assets.GFX.Images.DATA_EXTENSION))
                load(file.path(), Texture.class);
        }
    }

    /**
     * Unloads specified folder in the images folder.
     *
     * @param specificDir The name of the dir inside the sheets directory to unload.
     */
    private void unloadImages(String specificDir) {
        FileHandle dirHandle = Gdx.files.internal(Assets.GFX.GFX_FOLDER_NAME).child(Assets.GFX.Images.FOLDER_NAME).child(specificDir);
        for (FileHandle file : dirHandle.list()) {
            if (file.extension().equalsIgnoreCase(Assets.GFX.Images.DATA_EXTENSION)) {
                unload(file.path());
            }
        }
    }

    /**
     * Loads specified sound folder.
     *
     * @param dirToLoad The folder in the sounds folder to load.
     */
    private void loadSounds(String dirToLoad) {
        FileHandle dirHandle = Gdx.files.internal(SFX.fileAttribute.SFX_FOLDER_NAME.getValue()).child(SFX.fileAttribute.SOUNDS_FOLDER_NAME.getValue()).child(dirToLoad);
        for (FileHandle file : dirHandle.list()) {
            if (file.extension().equalsIgnoreCase(SFX.fileAttribute.SOUNDS_DATA_EXTENSION.getValue())) {
                load(file.path(), Sound.class);
            }
        }
    }

    /**
     * unloads specified sound folder.
     *
     * @param dirToLoad The folder in the sounds folder to unload.
     */
    private void unloadSounds(String dirToLoad) {
        FileHandle dirHandle = Gdx.files.internal(SFX.fileAttribute.SFX_FOLDER_NAME.getValue()).child(SFX.fileAttribute.SOUNDS_FOLDER_NAME.getValue()).child(dirToLoad);
        for (FileHandle file : dirHandle.list()) {
            if (file.extension().equalsIgnoreCase(SFX.fileAttribute.SOUNDS_DATA_EXTENSION.getValue())) {
                unload(file.path());
            }
        }
    }

    /**
     * Loads all font files (not generic).
     */
    private void loadFonts() {
        FileHandleResolver fileHandleResolver = getFileHandleResolver();
        setFontLoaders(fileHandleResolver);
        loadArmyFont();
        loadDigitalFont();
        loadRegularFont();
        finishLoading();
    }

    /**
     * Loads the digital font in all sizes.
     */
    private void loadDigitalFont() {
        loadFont(Rules.System.FontsParameters.DigitalFontNames.HUGE, Assets.FontsFileNames.DIGITAL, Rules.System.FontsParameters.FontSizes.HUGE, 0, true);
        loadFont(Rules.System.FontsParameters.DigitalFontNames.BIG, Assets.FontsFileNames.DIGITAL, Rules.System.FontsParameters.FontSizes.BIG, 0, true);
        loadFont(Rules.System.FontsParameters.DigitalFontNames.MEDIUM, Assets.FontsFileNames.DIGITAL, Rules.System.FontsParameters.FontSizes.MEDIUM, 0, true);
        loadFont(Rules.System.FontsParameters.DigitalFontNames.SMALL, Assets.FontsFileNames.DIGITAL, Rules.System.FontsParameters.FontSizes.SMALL, 0, true);
        loadFont(Rules.System.FontsParameters.DigitalFontNames.TINY, Assets.FontsFileNames.DIGITAL, Rules.System.FontsParameters.FontSizes.TINY, 0, true);
    }

    /**
     * Loads the regular font in all sizes.
     */
    private void loadRegularFont() {
        loadFont(Rules.System.FontsParameters.RegularFontNames.HUGE, Assets.FontsFileNames.REGULAR, Rules.System.FontsParameters.FontSizes.HUGE, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.RegularFontNames.BIG, Assets.FontsFileNames.REGULAR, Rules.System.FontsParameters.FontSizes.BIG, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.RegularFontNames.MEDIUM, Assets.FontsFileNames.REGULAR, Rules.System.FontsParameters.FontSizes.MEDIUM, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.RegularFontNames.SMALL, Assets.FontsFileNames.REGULAR, Rules.System.FontsParameters.FontSizes.SMALL, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.RegularFontNames.TINY, Assets.FontsFileNames.REGULAR, Rules.System.FontsParameters.FontSizes.TINY, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
    }

    /**
     * Loads the stencil font in all sizes.
     */
    private void loadArmyFont() {
        loadFont(Rules.System.FontsParameters.ArmyFontNames.HUGE, Assets.FontsFileNames.ARMY, Rules.System.FontsParameters.FontSizes.HUGE, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.ArmyFontNames.BIG, Assets.FontsFileNames.ARMY, Rules.System.FontsParameters.FontSizes.BIG, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, Assets.FontsFileNames.ARMY, Rules.System.FontsParameters.FontSizes.MEDIUM, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.ArmyFontNames.SMALL, Assets.FontsFileNames.ARMY, Rules.System.FontsParameters.FontSizes.SMALL, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
        loadFont(Rules.System.FontsParameters.ArmyFontNames.TINY, Assets.FontsFileNames.ARMY, Rules.System.FontsParameters.FontSizes.TINY, Rules.System.FontsParameters.ARMY_BORDER_WIDTH, false);
    }

    /**
     * Sets loaders for BitmapFont and FreeTypeFontGenerator classes.
     *
     * @param fileHandleResolver The fileHandleResolver to pass to the loaders.
     */
    private void setFontLoaders(FileHandleResolver fileHandleResolver) {
        setLoader(BitmapFont.class, ".ttf", new FreetypeFontLoader(fileHandleResolver));
        setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(fileHandleResolver));
    }

    /**
     * Loads a specified font file with given parameters.
     *
     * @param name           The unique name of the font to be used to load the font.
     * @param fileName       The file name of the font.
     * @param size           The size in pixels.
     * @param borderWidth    Border width in pixels, 0 to disable.
     * @param borderStraight True for straight (mitered), false for rounded borders.
     */
    private void loadFont(String name, String fileName, int size, float borderWidth, boolean borderStraight) {
        FreetypeFontLoader.FreeTypeFontLoaderParameter params;
        params = new FreetypeFontLoader.FreeTypeFontLoaderParameter();
        setFontParameters(fileName, size, borderWidth, borderStraight, params);
        load(name, BitmapFont.class, params);
    }

    /**
     * Sets the given parameters to the given params object.
     *
     * @param fileName       filename of the font.
     * @param size           Font size.
     * @param borderWidth    The width of the font's border.
     * @param borderStraight Whether to set font's border straight.
     * @param params         The params objects.
     */
    private void setFontParameters(String fileName, int size, float borderWidth, boolean borderStraight, FreetypeFontLoader.FreeTypeFontLoaderParameter params) {
        params.fontFileName = fileName;
        params.fontParameters.size = size;
        params.fontParameters.borderWidth = borderWidth;
        params.fontParameters.borderStraight = borderStraight;
    }


    /**
     * Loads all particle effects definitions.
     */
    private void loadParticlesConfigs() {
        FileHandle dirHandle = Gdx.files.internal(com.gadarts.parashoot.assets.Assets.Configs.CONFIGS_FOLDER_NAME + '/' + com.gadarts.parashoot.assets.Assets.Configs.PARTICLE_CONFIGS_FOLDER_NAME);
        ParticleEffectLoader.ParticleEffectParameter parameter = new ParticleEffectLoader.ParticleEffectParameter();
        parameter.atlasFile = Assets.GFX.GFX_FOLDER_NAME + '/' + Assets.GFX.Sheets.SHEETS_FOLDER_NAME + '/' + Assets.GFX.Sheets.InGame.FOLDER_NAME + '/' + "particles.txt";
        for (FileHandle file : dirHandle.list())
            if (file.extension().equalsIgnoreCase(Assets.Configs.PARTICLE_CONFIGS_DATA_EXTENSION))
                load(file.path(), ParticleEffect.class, parameter);
    }

    /**
     * Returns the config files (Such as the particle effects definitions).
     */
    public ArrayMap<String, FileHandle> getConfigs() {
        return configs;
    }

    /**
     * Loading basic stuff, before loading main game's data.
     */
    public void firstLoad() {
        loadSheets(Assets.GFX.Sheets.General.FOLDER_NAME);
        loadSounds(SFX.fileAttribute.GENERAL_FOLDER_NAME.getValue());
        loadImages(Assets.GFX.Images.General.FOLDER_NAME);
        loadFonts();
        loadParticlesConfigs();
        finishLoading();
    }

    /**
     * @return Whether the menus data is loaded or not.
     */
    public boolean isMenuLoaded() {
        return menuLoaded;
    }

    /**
     * @param loadingProcess Whether to set loading process true or false.
     */
    public void setLoadingProcess(boolean loadingProcess) {
        this.loadingInProcess = loadingProcess;
    }

    /**
     * @return Whether loading is currently in process.
     */
    public boolean isLoadingInProcess() {
        return loadingInProcess;
    }

    /**
     * @param selectedScene
     * @return the number of files in a scene folder.
     */
    public int calculateNumberOfLevelsInScene(Scene selectedScene) {
        int sceneIndex = GameUtils.getIndexOfValueInArray(Scene.values(), selectedScene) + 1;
        String sceneFolderName = Assets.Levels.SCENE_FOLDER_NAME + "_" + sceneIndex;
        FileHandle levelsFolder = Gdx.files.internal(Assets.Levels.LEVELS_FOLDER_NAME);
        return levelsFolder.child(sceneFolderName).list().length;
    }

    /**
     * Level loader (levels are JSONS).
     */
    private class LevelLoader extends AsynchronousAssetLoader<Level, AssetLoaderParameters<Level>> {
        LevelLoader(FileHandleResolver resolver) {
            super(resolver);
        }

        @Override
        public Array<AssetDescriptor> getDependencies(String fileName, FileHandle file, AssetLoaderParameters<Level> parameter) {
            return null;
        }

        @Override
        public void loadAsync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters<Level> parameter) {
            addAsset(fileName, Level.class, createLevel(fileName, file));
        }

        @Override
        public Level loadSync(AssetManager manager, String fileName, FileHandle file, AssetLoaderParameters<Level> parameter) {
            return createLevel(fileName, file);
        }

        /**
         * Creates a level object according to the given JSON file.
         *
         * @param fileName the resolved filename to load.
         * @param file     The resolved file to load.
         * @return The level object.
         */
        private Level createLevel(String fileName, FileHandle file) {
            JsonValue json = new JsonReader().parse(file);
            String levelCompleteMessage = json.getString(Rules.Level.LevelStructure.LevelAttributes.LEVEL_COMPLETE_MESSAGE, Assets.Strings.InGameMessages.MISSION_ACCOMPLISHED);
            JsonValue sceneJson = json.get(Rules.Level.LevelStructure.LevelAttributes.SCENE);
            Scene defaultScene = Scene.values()[0];
            Array<OrnamentAppearance> ornaments = new Array<OrnamentAppearance>();
            Array<SFX.Misc> ambSounds = new Array<SFX.Misc>();
            JsonValue ambSoundsArray = json.get(Rules.Level.LevelStructure.LevelAttributes.AMBIENCE_SOUNDS);
            if (ambSoundsArray != null) {
                for (JsonValue ambSoundJson : ambSoundsArray) {
                    try {
                        ambSounds.add(SFX.Misc.valueOf(ambSoundJson.asString()));
                    } catch (IllegalArgumentException exception) {
                        Gdx.app.error("Asset Manager", "Failed loading an amb sound.", exception);
                    }
                }
            }
            Scene scene;
            int landscape = 0;
            if (sceneJson == null) {
                scene = defaultScene;
            } else {
                scene = Scene.valueOf(sceneJson.getString(Rules.Level.LevelStructure.LevelAttributes.NAME, defaultScene.name()));
                landscape = (sceneJson.getInt(Rules.Level.LevelStructure.LevelAttributes.LANDSCAPE, 0));
                JsonValue ornamentsArray = sceneJson.get(Rules.Level.LevelStructure.LevelAttributes.ORNAMENTS);
                if (ornamentsArray != null) {
                    for (JsonValue ornamentJson : ornamentsArray) {
                        OrnamentType ornamentType = OrnamentType.valueOf(ornamentJson.getString(Rules.Level.LevelStructure.LevelAttributes.ORNAMENT, OrnamentType.values()[0].name()));
                        OrnamentAppearance ornamentAppearance = new OrnamentAppearance(ornamentType, ornamentJson.getInt(Rules.Level.LevelStructure.LevelAttributes.X, 0), ornamentJson.getBoolean(Rules.Level.LevelStructure.LevelAttributes.MOVING, false), ornamentType.getY() + ornamentJson.getInt(Rules.Level.LevelStructure.LevelAttributes.DELTA_Y, 0));
                        ornaments.add(ornamentAppearance);
                    }
                }
            }
            String openingMessage = json.getString(Rules.Level.LevelStructure.LevelAttributes.OPENING_MESSAGE, Assets.Strings.InGameMessages.GOOD_LUCK);
            String name = json.getString(Rules.Level.LevelStructure.LevelAttributes.NAME, fileName);
            boolean allowFeats = json.getBoolean(Rules.Level.LevelStructure.LevelAttributes.ALLOW_FEATS, true);
            boolean playGoodLuckTaunt = json.getBoolean(Rules.Level.LevelStructure.LevelAttributes.PLAY_GOOD_LUCK_TAUNT, true);
            boolean showStatistics = json.getBoolean(Rules.Level.LevelStructure.LevelAttributes.SHOW_STATISTICS, true);
            boolean allowGain = json.getBoolean(Rules.Level.LevelStructure.LevelAttributes.ALLOW_GAIN, true);
            boolean showBombsButton = json.getBoolean(Rules.Level.LevelStructure.LevelAttributes.SHOW_BOMBS_BUTTON, true);
            boolean forceCannonBall = json.getBoolean(Rules.Level.LevelStructure.LevelAttributes.FORCE_SIMPLE_CANNON_BALL, false);
            JsonValue showOptionsButton = json.get(Rules.Level.LevelStructure.LevelAttributes.SHOW_OPTIONS_BUTTON);
            if (showOptionsButton == null) {
                showOptionsButton = new JsonValue(true);
            }
            String music = json.getString(Rules.Level.LevelStructure.LevelAttributes.MUSIC, SFX.Music.BATTLE_1.name());
            Object disallowBonuses = JsonUtils.getBooleanOrStringArray(json, Rules.Level.LevelStructure.LevelAttributes.ALLOWED_BONUSES);
            Object allowedParatroopers = JsonUtils.getBooleanOrStringArray(json, Rules.Level.LevelStructure.LevelAttributes.ALLOWED_PARATROOPERS);
            Rules.Level.GlobalEffects.SkyType sky = Rules.Level.GlobalEffects.SkyType.valueOf(json.getString(Rules.Level.LevelStructure.LevelAttributes.SKY, Rules.Level.GlobalEffects.SkyType.DAY.name()));
            Rules.Level.GlobalEffects.WeatherTypes weather = Rules.Level.GlobalEffects.WeatherTypes.valueOf(json.getString(Rules.Level.LevelStructure.LevelAttributes.WEATHER, Rules.Level.GlobalEffects.WeatherTypes.REGULAR.name()));
            Array<EnemyAppearance> appearancesArray = new Array<EnemyAppearance>();
            SFX.Music musicObject = SFX.Music.BATTLE_1;
            JsonValue eventsJsonArray = json.get(Rules.Level.LevelStructure.LevelAttributes.EVENTS);
            com.badlogic.gdx.utils.Queue<Event> events = new com.badlogic.gdx.utils.Queue<Event>();
            if (eventsJsonArray != null) {
                for (JsonValue event : eventsJsonArray) {
                    int timing = event.getInt(Assets.InGameGuides.TIMING, 0);
                    Event.Action action;
                    JsonValue actionParameters = null;
                    try {
                        actionParameters = event.get(Assets.InGameGuides.ACTION_PARAMETERS);
                        action = Event.Action.valueOf(event.getString(Assets.InGameGuides.ACTION));
                    } catch (IllegalArgumentException e) {
                        action = null;
                    }
                    events.addLast(new Event(timing, action, actionParameters));
                }
            }
            try {
                JsonValue enemies = json.get(Rules.Level.LevelStructure.LevelAttributes.ENEMIES);
                for (int i = 0; i < enemies.size; i++) {
                    JsonValue appearance = enemies.get(i);
                    String typeString = appearance.getString(Rules.Level.LevelStructure.EnemyAppearance.TYPE, EnemyFactory.EnemyType.BONUS_SCOUT.name());
                    EnemyAppearance.Alignment alignment = EnemyAppearance.Alignment.RANDOM;
                    try {
                        alignment = EnemyAppearance.Alignment.valueOf(appearance.getString(Rules.Level.LevelStructure.EnemyAppearance.ALIGNMENT, EnemyAppearance.Alignment.RANDOM.name()));
                    } catch (Exception ignored) {
                    }
                    LevelSkill minimumSkill = LevelSkill.EASY;
                    try {
                        minimumSkill = LevelSkill.valueOf(appearance.getString(Rules.Level.LevelStructure.EnemyAppearance.MINIMUM_SKILL, LevelSkill.EASY.name()));
                    } catch (Exception ignored) {
                    }
                    EnemyAppearance enemyAppearance = new EnemyAppearance(EnemyFactory.EnemyType.valueOf(typeString), appearance.getInt(Rules.Level.LevelStructure.EnemyAppearance.TIMING, 0), alignment, minimumSkill);
                    JsonValue focus = appearance.get(Rules.Level.LevelStructure.EnemyAppearance.Focus.JSON_KEY);
                    if (focus != null) {
                        enemyAppearance.setFocusParameters(focus);
                    }
                    appearancesArray.add(enemyAppearance);
                    musicObject = SFX.Music.valueOf(music);
                }
            } catch (IllegalArgumentException e) {
                musicObject = SFX.Music.BATTLE_1;
            }
            return new Level(fileName, name, appearancesArray, disallowBonuses, sky, weather, allowedParatroopers, musicObject, showStatistics, events, openingMessage, playGoodLuckTaunt, levelCompleteMessage, allowFeats, scene, ornaments, ambSounds, landscape, allowGain, showOptionsButton, showBombsButton, forceCannonBall);
        }
    }
}
