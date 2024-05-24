package com.gadarts.parashoot.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.tutorial.Guide;
import com.gadarts.parashoot.screens.Menus.ButtonClickImproved;
import com.gadarts.parashoot.screens.Menus.MonitorImproved;
import com.gadarts.parashoot.utils.Rules;

import java.util.HashMap;

import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK_PRESSED;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.INFO_BUTTON;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.SCROLL_KNOB;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.SCROLL_KNOB_BACKGROUND;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.STAR_LEVEL_FILLED;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.MEDIUM;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.SMALL;


/**
 * Created by Gad on 05/08/2016.
 */
public abstract class MenuScreenImproved extends BasicMenuScreen {
    /**
     * Hack to fix the unknown bug of the looping sounds when returning to game.
     */
    private static final Timer.Task TASK_AUTO_PAUSE_SOUND_POOL = new Timer.Task() {
        @Override
        public void run() {
            Parastrike.getInstance().getActionResolver().autoPauseSoundPool();
        }
    };

    private final Skin skin = new Skin(Parastrike.getAssetsManager().get(Assets.GFX.Sheets.Menus.GENERAL_MENU_DATA_FILE, TextureAtlas.class));
    protected final Vector3 auxVector = new Vector3();
    private final boolean ANIMATED_BACKGROUND;
    protected ParticleEffect hoorayEffect;
    private ParticleEffect paratroopersBackground;
    private Texture backgroundTexture;
    private final HashMap<String, MonitorImproved> monitors = new HashMap<>();
    private final Guide guide = new Guide();
    private ScrollPane.ScrollPaneStyle scrollPaneStyle;

    public MenuScreenImproved(boolean animatedBackground, String... imageSheets) {
        super();
        menuStage.addActor(guide);
        this.ANIMATED_BACKGROUND = animatedBackground;
        for (String imageSheet : imageSheets)
            skin.addRegions(Parastrike.getAssetsManager().get(imageSheet, TextureAtlas.class));
        getSkin().add(MEDIUM, new Label.LabelStyle(Parastrike.getAssetsManager().get(MEDIUM, BitmapFont.class), Color.GOLD));
    }

    public MonitorImproved createMonitor(float monitorWidth, float monitorHeight, float targetY, boolean isBottom, boolean activate, boolean outsideTouchClose, String header, String name) {
        return createMonitor(null, monitorWidth, monitorHeight, targetY, isBottom, activate, outsideTouchClose, header, name);
    }

    @Override
    public void resume() {
        super.resume();
        if (!TASK_AUTO_PAUSE_SOUND_POOL.isScheduled()) {
            timer.scheduleTask(TASK_AUTO_PAUSE_SOUND_POOL, 1);
        }
    }

    public MonitorImproved createMonitor(WidgetGroup table, float monitorWidth, float monitorHeight, float targetY, boolean isBottom, boolean activate, boolean outsideTouchClose) {
        return createMonitor(table, monitorWidth, monitorHeight, targetY, isBottom, activate, outsideTouchClose, null, Rules.Menu.DEFAULT_MONITOR_NAME);
    }

    public MonitorImproved createMonitor(WidgetGroup content, float monitorWidth, float monitorHeight, float targetY, boolean isBottom, boolean activate, boolean outsideTouchClose, String header) {
        return createMonitor(content, monitorWidth, monitorHeight, targetY, isBottom, activate, outsideTouchClose, header, Rules.Menu.DEFAULT_MONITOR_NAME);
    }


    public MonitorImproved createMonitor(WidgetGroup content, float monitorWidth, float monitorHeight, float targetY, boolean isBottom, boolean activate, boolean outsideTouchClose, String header, String name) {
        if (monitors.containsKey(name)) return returnExistingMonitor(name);
        MonitorImproved monitor = new MonitorImproved(getSkin(), content, monitorWidth, monitorHeight, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2 - monitorWidth / 2, targetY, isBottom, activate, outsideTouchClose, Rules.Menu.Shop.MONITOR_ACCELERATION, this, header);
        getStage().addActor(monitor);
        monitors.put(name, monitor);
        monitor.setName(name);
        return monitor;
    }

    private MonitorImproved returnExistingMonitor(String name) {
        MonitorImproved monitor = monitors.get(name);
        monitor.setActivation(true);
        return monitor;
    }

    @Override
    public void show() {
        super.show();
        if (ANIMATED_BACKGROUND) {
            setAnimatedBackground();
        }
    }


    protected ScrollPane.ScrollPaneStyle getScrollPaneStyle() {
        if (scrollPaneStyle == null) {
            NinePatch knobPatch = new NinePatch(getSkin().getRegion(SCROLL_KNOB), 0, 0, 11, 13);
            NinePatch scrollPatch = new NinePatch(getSkin().getRegion(SCROLL_KNOB_BACKGROUND), 0, 0, 11, 13);
            NinePatchDrawable knobDrawable = new NinePatchDrawable(knobPatch);
            NinePatchDrawable scrollDrawable = new NinePatchDrawable(scrollPatch);
            scrollPaneStyle = new ScrollPane.ScrollPaneStyle(null, null, null, scrollDrawable, knobDrawable);
        }
        return scrollPaneStyle;
    }

    protected ScrollPane createLayoutScrollPane(WidgetGroup table) {
        ScrollPane scrollPane = new ScrollPane(table, getScrollPaneStyle());
        scrollPane.setScrollbarsOnTop(true);
        scrollPane.setOverscroll(false, false);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        return scrollPane;
    }

    @Override
    protected void onBackPressed() {
        Parastrike.getInstance().goToMenuScreen(getBackScreen());
    }

    private void setAnimatedBackground() {
        backgroundTexture = Parastrike.getAssetsManager().get(Assets.GFX.Images.General.NIGHT);
        paratroopersBackground = Parastrike.getAssetsManager().get(Assets.Configs.CONFIGS_FOLDER_NAME + "/" + Assets.Configs.PARTICLE_CONFIGS_FOLDER_NAME + "/" + Assets.Configs.ParticleEffects.MENU_PARATROOPERS + "." + Assets.Configs.PARTICLE_CONFIGS_DATA_EXTENSION, ParticleEffect.class);
        paratroopersBackground.setPosition(-200, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
        paratroopersBackground.start();
    }

    public Skin getSkin() {
        return skin;
    }

    public Stage getStage() {
        return menuStage;
    }

    public Vector3 getAuxVector() {
        return auxVector;
    }

    public OrthographicCamera getMainCamera() {
        return mainCamera;
    }

    protected void createStarsDisplay(MonitorImproved monitor) {
        Skin skin = getSkin();
        Table starsTable = createStarsDisplayTable();
        starsTable.add(new Image(skin.getDrawable(STAR_LEVEL_FILLED)));
        starsTable.add(new Label(Integer.toString(Parastrike.getPlayerStatsHandler().getStars()), new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, BitmapFont.class), Color.GOLD))).padRight(Rules.Menu.SceneSelection.Monitor.StarsDisplay.STARS_VALUE_PADDING_RIGHT).padLeft(Rules.Menu.SceneSelection.Monitor.StarsDisplay.STARS_VALUE_PADDING_LEFT);
        starsTable.add(new Image(skin.getDrawable(INFO_BUTTON)));
        monitor.setTopRightWidget(starsTable);

    }

    private Table createStarsDisplayTable() {
        Table starsTable = new Table(getSkin());
        starsTable.addListener(defineStarsDisplayClick());
        return starsTable;
    }

    private ButtonClickImproved defineStarsDisplayClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                MonitorImproved starsMonitor = createMonitor(null, Rules.Menu.SceneSelection.Monitor.StarsDisplay.MONITOR_WIDTH, Rules.Menu.SceneSelection.Monitor.StarsDisplay.MONITOR_HEIGHT, Rules.Menu.SceneSelection.Monitor.StarsDisplay.MONITOR_Y, true, true, true, Assets.Strings.Menu.SceneSelection.StarsInfoMonitor.STARS_INFO_MONITOR_HEADER, Rules.Menu.SceneSelection.Monitor.StarsDisplay.MONITOR_NAME);
                if (starsMonitor.getContent() == null) {
                    starsMonitor.setContent(createStarsContent(starsMonitor));
                }
                starsMonitor.setCloseButton(true).setBackButtonVisibility(false);
            }

            private WidgetGroup createStarsContent(final MonitorImproved starsMonitor) {
                Skin skin = getSkin();
                Table table = new Table(skin);
                createLabel(table);
                Button ok = new Button(skin.getDrawable(BUTTON_CHECK), skin.getDrawable(BUTTON_CHECK_PRESSED));
                ok.addListener(defineOkClick(starsMonitor));
                table.add(ok);
                return table;
            }

            private ButtonClickImproved defineOkClick(final MonitorImproved starsMonitor) {
                return new ButtonClickImproved() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        starsMonitor.setActivation(false);
                    }
                };
            }

            private void createLabel(Table table) {
                Label label = new Label(Assets.Strings.Menu.SceneSelection.StarsInfoMonitor.TEXT, new Label.LabelStyle(Parastrike.getAssetsManager().get(SMALL, BitmapFont.class), Color.GOLD));
                label.setAlignment(Align.center);
                label.setWrap(true);
                table.add(label).width(Rules.Menu.SceneSelection.Monitor.StarsDisplay.MONITOR_WIDTH).row();
            }
        };
    }

    protected void createHoorayEffect(SFX.Menu sound) {
        hoorayEffect = Parastrike.getAssetsManager().get(Assets.Configs.CONFIGS_FOLDER_NAME + "/" + Assets.Configs.PARTICLE_CONFIGS_FOLDER_NAME + "/" + Assets.Configs.ParticleEffects.SCENE_UNLOCKED_STARS + "." + Assets.Configs.PARTICLE_CONFIGS_DATA_EXTENSION, ParticleEffect.class);
        hoorayEffect.setPosition(0, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
        hoorayEffect.start();
        Parastrike.getSoundPlayer().playSound(sound);
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        guide.toFront();
        menuStage.getBatch().begin();
        if (ANIMATED_BACKGROUND) {
            paratroopersBackground.update(delta);
        }
        menuStage.getBatch().draw(backgroundTexture, 0, 0);
        if (ANIMATED_BACKGROUND) {
            paratroopersBackground.draw(menuStage.getBatch(), delta);
        }
        menuStage.getBatch().end();
        menuStage.draw();
        renderSceneUnlockedEffect(delta);
    }

    public HashMap<String, MonitorImproved> getMonitors() {
        return monitors;
    }

    protected void updateAndDrawSceneUnlockedEffect(float delta) {
        menuStage.getBatch().begin();
        hoorayEffect.update(delta);
        hoorayEffect.draw(menuStage.getBatch(), delta);
        menuStage.getBatch().end();
    }

    protected void renderSceneUnlockedEffect(float delta) {
        if (hoorayEffect != null) {
            if (hoorayEffect.isComplete()) {
                hoorayEffect = null;
            } else {
                updateAndDrawSceneUnlockedEffect(delta);
            }
        }
    }
}
