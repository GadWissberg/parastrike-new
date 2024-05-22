package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.Rules;

import java.util.HashMap;

/**
 * Created by Gad on 05/08/2016.
 */
public class SceneSelectionScreen extends MenuScreenImproved {

    private ScrollPane scrollPane;
    public static final String EVENT_SCENE_UNLOCKED = "event_scene_unlocked";
    private MonitorImproved monitor;

    public SceneSelectionScreen() {
        super(true, Assets.GFX.Sheets.Menus.SCENE_SELECTION_DATA_FILE, Assets.GFX.Sheets.Menus.LOBBY_DATA_FILE);
        setNextScreen(Parastrike.MenuType.LOBBY);
        setBackScreen(Parastrike.MenuType.WELCOME);
        createParastrikeMonitor();
        createStarsDisplay(monitor);
    }

    @Override
    public void show() {
        super.show();
        Scene[] scenes = Scene.values();
        int selectedSceneIndex;
        for (selectedSceneIndex = scenes.length - 1; selectedSceneIndex >= 0; selectedSceneIndex--)
            if (Parastrike.getPlayerStatsHandler().isSceneEnabled(scenes[selectedSceneIndex])) break;
        scrollToSpecificScene(selectedSceneIndex);
    }

    private void createParastrikeMonitor() {
        scrollPane = createParastrikeMonitorContent();
        monitor = createMonitor(scrollPane, Rules.Menu.SceneSelection.Monitor.MONITOR_WIDTH, Rules.Menu.SceneSelection.Monitor.MONITOR_HEIGHT, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - Rules.Menu.SceneSelection.Monitor.MONITOR_HEIGHT / 2, false, true, false, Assets.Strings.Menu.SceneSelection.HEADER);
        monitor.setBackButtonVisibility(true);
    }

    private ScrollPane createParastrikeMonitorContent() {
        Table table = createParastrikeMonitorTable();
        ScrollPane scrollPane = new ScrollPane(table, getScrollPaneStyle());
        scrollPane.setFadeScrollBars(false);
        return scrollPane;
    }

    private Table createParastrikeMonitorTable() {
        Table table = new Table();
        table.padLeft(Rules.Menu.SceneSelection.Monitor.TABLE_PADDING_HORIZONTAL).padRight(Rules.Menu.SceneSelection.Monitor.TABLE_PADDING_HORIZONTAL);
        createParastrikeMonitorTableContent(table);
        table.top();
        return table;
    }

    private void createParastrikeMonitorTableContent(Table table) {
        Skin skin = getSkin();
        createScenesButtons(table, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LOCK));
        createRequirementsTables(table);
    }

    private void createRequirementsTables(Table table) {
        Label.LabelStyle style = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, BitmapFont.class), Color.GOLD);
        for (final Scene scene : Scene.values()) {
            createRequirementsTable(table, style, scene);
        }
    }

    private void createRequirementsTable(Table table, Label.LabelStyle style, Scene scene) {
        Table requirementsTable = new Table(getSkin());
        requirementsTable.padTop(Rules.Menu.SceneSelection.Monitor.REQUIREMENTS_TABLE_PADDING_TOP);
        createRequirementsLabels(scene, requirementsTable, style);
        table.add(requirementsTable);
        if (Parastrike.getPlayerStatsHandler().isSceneEnabled(scene))
            requirementsTable.setVisible(false);
    }

    private void createRequirementsLabels(Scene scene, Table requirementsTable, Label.LabelStyle style) {
        Label requiresLabel = new Label(Assets.Strings.Menu.SceneSelection.REQUIRES, style);
        requirementsTable.add(requiresLabel).padRight(Rules.Menu.SceneSelection.Monitor.REQUIREMENTS_LABEL_PADDING_RIGHT);
        boolean showLabel = createStarsRequirement(style, scene, requirementsTable);
        showLabel |= createCoinsRequirement(style, scene, requirementsTable);
        requiresLabel.setVisible(showLabel);
    }

    private boolean createCoinsRequirement(Label.LabelStyle smallStyle, Scene scene, Table requirementsTable) {
        int coinsTarget = scene.getCoinsTarget();
        if (coinsTarget > 0) {
            requirementsTable.add(new Image(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.SMALL_COIN_ICON))).padRight(Rules.Menu.SceneSelection.Monitor.REQUIREMENTS_IMAGE_PADDING_RIGHT);
            requirementsTable.add(new Label(Integer.toString(coinsTarget), smallStyle));
        }
        return coinsTarget > 0;
    }

    private boolean createStarsRequirement(Label.LabelStyle smallStyle, Scene scene, Table requirementsTable) {
        int starsTarget = scene.getStarsTarget();
        if (starsTarget > 0) {
            requirementsTable.add(new Image(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.STAR_LEVEL_FILLED))).padRight(Rules.Menu.SceneSelection.Monitor.REQUIREMENTS_IMAGE_PADDING_RIGHT);
            requirementsTable.add(new Label(Integer.toString(starsTarget), smallStyle)).padRight(Rules.Menu.SceneSelection.Monitor.REQUIREMENTS_STAR_VALUE_PADDING_RIGHT);
        }
        return starsTarget > 0;
    }

    private void createScenesButtons(Table table, Drawable lockDrawable) {
        Label.LabelStyle labelStyle = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.BIG, BitmapFont.class), Color.GOLD);
        for (final Scene scene : Scene.values()) {
            createSceneButton(table, lockDrawable, labelStyle, scene);
        }
        table.row();
    }

    private void createSceneButton(Table table, Drawable lockDrawable, Label.LabelStyle labelStyle, Scene scene) {
        ImageButton sceneButton = new ImageButton(new TextureRegionDrawable(new TextureRegion(Parastrike.getAssetsManager().get(scene.getThumb(), Texture.class))));
        Stack stack = new Stack(sceneButton);
        setCorrectSceneButtonClickListener(lockDrawable, scene, stack);
        createNameLabel(labelStyle, scene, stack);
        table.add(stack).padLeft(Rules.Menu.SceneSelection.Monitor.STACK_PADDING_HORIZONTAL).padRight(Rules.Menu.SceneSelection.Monitor.STACK_PADDING_HORIZONTAL);
    }

    private void createNameLabel(Label.LabelStyle labelStyle, Scene scene, Stack stack) {
        Label nameLabel = new Label(scene.getDisplayName(), labelStyle);
        nameLabel.setAlignment(Align.bottom);
        stack.add(nameLabel);
        nameLabel.setFillParent(true);
    }

    private void setCorrectSceneButtonClickListener(Drawable lockDrawable, Scene scene, Stack stack) {
        boolean sceneEnabled = Parastrike.getPlayerStatsHandler().isSceneEnabled(scene);
        stack.addListener(defineSceneButtonClick(scene, sceneEnabled));
        if (!sceneEnabled || scene.isLocked()) {
            createLock(lockDrawable, stack);
        }
    }

    private void createLock(Drawable lockDrawable, Stack stack) {
        Image lock = new Image(lockDrawable);
        lock.setScaling(Scaling.none);
        stack.add(lock);
    }

    private ButtonClickImproved defineSceneButtonClick(final Scene scene, final boolean sceneEnabled) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                if (scene.isLocked()) return;
                if (sceneEnabled) {
                    Parastrike.getPlayerStatsHandler().setSelectedScene(scene);
                    Parastrike.getInstance().goToMenuScreen(getNextScreen());
                } else createLockedSceneMonitor();
            }

            private void createLockedSceneMonitor() {
                MonitorImproved lockedSceneMonitor = createMonitor(null, Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.WIDTH, Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.HEIGHT, Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.Y, true, true, true, Assets.Strings.Menu.SceneSelection.LockedSceneMonitor.HEADER, Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.NAME);
                lockedSceneMonitor.setContent(createLockedSceneContent(scene));
                lockedSceneMonitor.setBackButtonVisibility(false);
                lockedSceneMonitor.setCloseButton(true);
            }
        };
    }

    private Stack createLockedSceneContent(Scene scene) {
        Table table = createLockedSceneTable(scene);
        CoinsButton coinsButton = createCoinsButton();
        Stack stack = new Stack(table, coinsButton);
        return stack;
    }

    private Table createLockedSceneTable(Scene scene) {
        Table table = new Table();
        Label.LabelStyle style = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.GOLD);
        createLockedSceneMonitorText(table, style);
        createLockedSceneMonitorRequirementsLabels(scene, table, style);
        return table;
    }

    private CoinsButton createCoinsButton() {
        Skin skin = getSkin();
        CoinsButton coinsButton = new CoinsButton(skin);
        coinsButton.right().bottom();
        return coinsButton;
    }

    private void createLockedSceneMonitorRequirementsLabels(Scene scene, Table table, Label.LabelStyle style) {
        Label.LabelStyle smallerStyle = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GOLD);
        createLockedSceneMonitorRequirementLabel(scene.getStarsTarget(), table, style, Parastrike.getPlayerStatsHandler().getStars(), Assets.GFX.Sheets.ImagesNames.STAR_LEVEL_FILLED, Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.STARS_ICON_PADDING_RIGHT, smallerStyle);
        createLockedSceneMonitorRequirementLabel(scene.getCoinsTarget(), table, style, Parastrike.getPlayerStatsHandler().getCoins(), Assets.GFX.Sheets.ImagesNames.SMALL_COIN_ICON, Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.COINS_ICON_PADDING_RIGHT, smallerStyle);
    }

    private void createLockedSceneMonitorText(Table table, Label.LabelStyle style) {
        Label text = new Label(Assets.Strings.Menu.SceneSelection.LockedSceneMonitor.LEFT_TEXT, style);
        text.setAlignment(Align.center);
        text.setWrap(true);
        table.add(text).width(Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.WIDTH).top().colspan(2).row();
    }

    private void createLockedSceneMonitorRequirementLabel(int target, Table table, Label.LabelStyle style, int current, String icon, float requirementIconPaddingRight, Label.LabelStyle smallerStyle) {
        if (target > current) {
            table.add(new Image(getSkin().getDrawable(icon))).right().padTop(Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.REQUIREMENT_PADDING_TOP).padRight(requirementIconPaddingRight);
            table.add(new Label(Integer.toString(target), style)).padTop(Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.REQUIREMENT_PADDING_TOP).left().row();
            table.add(new Label(Assets.Strings.Menu.SceneSelection.LockedSceneMonitor.YOU_HAVE_TEXT + current, smallerStyle)).padBottom(Rules.Menu.SceneSelection.Monitor.LockedSceneMonitor.YOU_HAVE_PADDING_BOTTOM).colspan(2).row();
        }
    }

    @Override
    public void invokeAdditionalInfo(HashMap<String, Integer> additionalInfo) {
        super.invokeAdditionalInfo(additionalInfo);
        final Integer info = additionalInfo.get(EVENT_SCENE_UNLOCKED);
        timer.scheduleTask(createUnlockEffectTask(info), Rules.Menu.SceneSelection.Monitor.SCENE_UNLOCK_EFFECT_DELAY);
    }

    private Timer.Task createUnlockEffectTask(final Integer info) {
        return new Timer.Task() {
            @Override
            public void run() {
                createHoorayEffect(SFX.Menu.SCENE_UNLOCKED);
                if (info != null) scrollToSpecificScene(info);
            }
        };
    }

    private void scrollToSpecificScene(Integer info) {
        if (info < 0) {
            return;
        }
        scrollPane.layout();
        scrollPane.setScrollX(info * 800);
    }


    @Override
    protected void onBackPressed() {
        Parastrike.getInstance().goToMenuScreen(getBackScreen());
    }


}
