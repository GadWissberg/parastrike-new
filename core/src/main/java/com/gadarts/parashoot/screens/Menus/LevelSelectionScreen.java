package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.AssetManagerWrapper;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.level_model.LevelState;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.model.PlayerStats;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.util.HashMap;

/**
 * Created by Gad on 05/08/2016.
 */
public class LevelSelectionScreen extends MenuScreenImproved {

    public final int NUM_OF_LEVELS_IN_SCENE;
    private boolean gameLaunched;
    public static final String EVENT_SCENE_COMPLETED = "event_scene_completed";

    public LevelSelectionScreen() {
        super(true, Assets.GFX.Sheets.Menus.SCENE_SELECTION_DATA_FILE);
        Scene selectedScene = Parastrike.getPlayerStatsHandler().getSelectedScene();
        AssetManagerWrapper assetsManager = Parastrike.getAssetsManager();
        NUM_OF_LEVELS_IN_SCENE = assetsManager.calculateNumberOfLevelsInScene(selectedScene);
        setBackScreen(Parastrike.MenuType.LOBBY);
        MonitorImproved monitor = createMonitor(createParastrikeMonitorContent(), Rules.Menu.SceneSelection.Monitor.MONITOR_WIDTH, Rules.Menu.SceneSelection.Monitor.MONITOR_HEIGHT, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - Rules.Menu.SceneSelection.Monitor.MONITOR_HEIGHT / 2, true, true, false, Assets.Strings.Menu.LevelSelection.HEADER);
        monitor.setBackButtonVisibility(true);
        createStarsDisplay(monitor);
    }

    @Override
    public void invokeAdditionalInfo(HashMap<String, Integer> additionalInfo) {
        super.invokeAdditionalInfo(additionalInfo);
        if (additionalInfo.containsKey(EVENT_SCENE_COMPLETED)) {
            if (additionalInfo.get(EVENT_SCENE_COMPLETED) == 1) {
                createSceneCompletedMonitor();
                createHoorayEffect(SFX.Menu.SCENE_UNLOCKED);
            }
        }
    }

    private void createSceneCompletedMonitor() {
        MonitorImproved sceneCompletedMonitor = createMonitor(null, Rules.Menu.LevelSelection.SceneCompletedMonitor.WIDTH, Rules.Menu.LevelSelection.SceneCompletedMonitor.HEIGHT, Rules.Menu.LevelSelection.SceneCompletedMonitor.Y, true, true, true, Assets.Strings.Menu.LevelSelection.SceneCompletedMonitor.HOORAY, Rules.Menu.LevelSelection.SceneCompletedMonitor.NAME);
        sceneCompletedMonitor.setContent(defineSceneCompletedContent(sceneCompletedMonitor));
        sceneCompletedMonitor.setBackButtonVisibility(false);
        sceneCompletedMonitor.setCloseButton(true);
    }

    private WidgetGroup defineSceneCompletedContent(final MonitorImproved sceneCompletedMonitor) {
        Skin skin = getSkin();
        Table table = new Table(skin);
        createSceneCompletedMessage(table);
        createCheckButton(sceneCompletedMonitor, table);
        return table;
    }

    private void createSceneCompletedMessage(Table table) {
        Label message = new Label(String.format(Assets.Strings.Menu.LevelSelection.SceneCompletedMonitor.TEXT, Parastrike.getPlayerStatsHandler().getSelectedScene().getDisplayName(), Rules.Menu.LevelSelection.SceneCompletedMonitor.REWARD), new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.GOLD));
        message.setWrap(true);
        table.add(message).width(Rules.Menu.LevelSelection.SceneCompletedMonitor.WIDTH).top().row();
        message.setAlignment(Align.center);
    }

    private void createCheckButton(final MonitorImproved sceneCompletedMonitor, Table table) {
        Skin skin = getSkin();
        Button closeButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK_PRESSED));
        closeButton.addListener(defineCheckButton(sceneCompletedMonitor));
        table.add(closeButton).bottom();
    }

    private ButtonClickImproved defineCheckButton(final MonitorImproved sceneCompletedMonitor) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                sceneCompletedMonitor.setActivation(false);
            }
        };
    }

    private WidgetGroup createParastrikeMonitorContent() {
        Table table = new Table();
        table.add(new Label(Parastrike.getPlayerStatsHandler().getSelectedScene().getDisplayName(), new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, BitmapFont.class), Color.GOLD))).colspan(NUM_OF_LEVELS_IN_SCENE).row();
        initializeAndCreateLevelsButtons(table);
        return table;
    }

    private void initializeAndCreateLevelsButtons(Table table) {
        Skin skin = getSkin();
        Drawable filledDrawable = skin.getDrawable(Assets.GFX.Sheets.ImagesNames.STAR_LEVEL_FILLED);
        Drawable emptyDrawable = skin.getDrawable(Assets.GFX.Sheets.ImagesNames.STAR_LEVEL_EMPTY);
        Label.LabelStyle style = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.HUGE, BitmapFont.class), Color.GOLD);
        createLevelsButtons(table, filledDrawable, emptyDrawable, style);
    }

    private void createLevelsButtons(Table table, Drawable filledDrawable, Drawable emptyDrawable, Label.LabelStyle style) {
        int numberOfLevelsInRow = (NUM_OF_LEVELS_IN_SCENE / 2 < 1) ? 1 : NUM_OF_LEVELS_IN_SCENE / 2;
        int numberOfRows = NUM_OF_LEVELS_IN_SCENE / numberOfLevelsInRow;
        for (int rowIndex = 0; rowIndex < numberOfRows; rowIndex++) {
            createLevelsButtonsRow(table, filledDrawable, emptyDrawable, rowIndex, style);
            table.row();
        }
    }

    private void createLevelsButtonsRow(Table table, Drawable filledDrawable, Drawable emptyDrawable, int rowIndex, Label.LabelStyle style) {
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        Skin skin = getSkin();
        int numberOfLevelsInRow = (NUM_OF_LEVELS_IN_SCENE / 2 < 1) ? 1 : NUM_OF_LEVELS_IN_SCENE / 2;
        for (int columnIndex = 0; columnIndex < numberOfLevelsInRow; columnIndex++) {
            int levelIndex = (columnIndex + 1) + (rowIndex * NUM_OF_LEVELS_IN_SCENE / 2);
            createLevelButton(table, filledDrawable, emptyDrawable, playerStatsHandler.getLevelState(playerStatsHandler.getSelectedScene(), levelIndex), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LEVEL_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LEVEL_BUTTON_PRESSED), levelIndex, style);
        }
    }

    private void createLevelButton(Table table, Drawable filledDrawable, Drawable emptyDrawable, LevelState levelState, Drawable levelButtonDrawable, Drawable levelButtonPressedDrawable, final int levelIndex, Label.LabelStyle style) {
        Stack stack = new Stack();
        stack.add(new Button(levelButtonDrawable, levelButtonPressedDrawable));
        stack.add(createStarsImages(filledDrawable, emptyDrawable, levelState));
        table.add(stack).pad(Rules.Menu.LevelSelection.LevelsButtons.LEVEL_BUTTONS_PADDING);
        createLevelNumberLabel(levelIndex, style, stack);
        handleLockAndListener(levelState, levelIndex, stack);
    }

    private void handleLockAndListener(LevelState levelState, int levelIndex, Stack stack) {
        if (levelState != LevelState.LOCKED)
            stack.addListener(defineLevelButtonClick(levelIndex, levelState));
        else {
            Image lockImage = new Image(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.LOCK));
            lockImage.setScaling(Scaling.none);
            stack.add(lockImage);
        }
    }

    private ButtonClickImproved defineLevelButtonClick(final int levelIndex, final LevelState levelState) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (gameLaunched) return;
                final PlayerStats oldStats = new PlayerStats(Parastrike.getPlayerStatsHandler().getPlayerStats());
                initializeLevel();
                reduceAmmo();
                goToBattle(oldStats);
            }

            private void goToBattle(final PlayerStats oldStats) {
                gameLaunched = true;
                closeDoorsAndGoToScreen(defineLoadingCallBack(), defineLoadingFinishedCallBack(oldStats));
            }

            private Timer.Task defineLoadingFinishedCallBack(final PlayerStats oldStats) {
                return new Timer.Task() {
                    @Override
                    public void run() {
                        Parastrike.getInstance().goToBattle(oldStats);
                    }
                };
            }

            private Timer.Task defineLoadingCallBack() {
                return new Timer.Task() {
                    @Override
                    public void run() {
                        Parastrike.getSoundPlayer().stopMusic();
                        Parastrike.getAssetsManager().loadDataBattle(Parastrike.getPlayerStatsHandler().getSelectedScene().ordinal() + 1, levelIndex);
                    }
                };
            }

            private void initializeLevel() {
                PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                playerStatsHandler.setSelectedLevel(levelIndex);
                playerStatsHandler.setSelectedLevelSkill(LevelState.toLevelSkill(levelState));
            }

            private void reduceAmmo() {
                PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                BulletType selectedCannon = playerStatsHandler.getSelectedCannon();
                int currentCannonAmmo = playerStatsHandler.getCannonAmmo(selectedCannon);
                if (currentCannonAmmo != -1) {
                    playerStatsHandler.setCannonAmmo(selectedCannon, currentCannonAmmo - 1, false);
                }
            }

        };
    }

    private void createLevelNumberLabel(int levelIndex, Label.LabelStyle style, Stack stack) {
        Label label = new Label(Integer.toString(levelIndex), style);
        label.setAlignment(Align.top);
        stack.add(label);
    }

    private Table createStarsImages(Drawable filledDrawable, Drawable emptyDrawable, LevelState levelState) {
        int numberOfFilledStars = levelState.ordinal() - 1 < 0 ? 0 : levelState.ordinal() - 1;
        Table table = new Table();
        for (int k = 0; k < numberOfFilledStars; k++) table.add(new Image(filledDrawable));
        for (int k = 0; k < (LevelState.values().length - 2) - numberOfFilledStars; k++)
            table.add(new Image(emptyDrawable));
        return table.bottom().padBottom(Rules.Menu.LevelSelection.LevelsButtons.STARS_PADDING_BOTTOM);
    }

    @Override
    protected void onBackPressed() {
        Parastrike.getInstance().goToMenuScreen(getBackScreen());
    }
}
