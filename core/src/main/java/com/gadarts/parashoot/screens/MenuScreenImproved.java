package com.gadarts.parashoot.screens;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.model.PurchaseItem;
import com.gadarts.parashoot.model.tutorial.Guide;
import com.gadarts.parashoot.screens.Menus.ButtonClickImproved;
import com.gadarts.parashoot.screens.Menus.MonitorImproved;
import com.gadarts.parashoot.utils.Rules;

import java.util.ArrayList;
import java.util.HashMap;

import static com.gadarts.parashoot.Parastrike.MenuType.LEVEL_SELECTION;
import static com.gadarts.parashoot.Parastrike.MenuType.LOBBY;
import static com.gadarts.parashoot.Parastrike.MenuType.SHOP;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.*;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.GetMoreCoinsMonitor.*;
import static com.gadarts.parashoot.utils.Rules.Menu.GetMoreCoinsMonitor.*;
import static com.gadarts.parashoot.utils.Rules.Menu.Shop.MainScreen.Coins.CoinsPacks.PurchasePack;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.*;


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
    private HashMap<String, MonitorImproved> monitors = new HashMap<String, MonitorImproved>();
    private Guide guide = new Guide();
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
        } else {
//            backgroundTexture = Main.getAssetsManager().get(Assets.GFX.Images.Menus.MAIN_THEME);
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

    public Guide getGuide() {
        return guide;
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

    public void removeMonitor(MonitorImproved monitor) {
        monitor.remove();
        monitors.remove(monitor.getName());
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

    public class CoinsButton extends ImageButton {
        private MonitorImproved monitor;

        public CoinsButton(Skin skin) {
            super(skin.getDrawable(COINS_BUTTON), skin.getDrawable(COINS_BUTTON_PRESSED));
            addAction(Actions.forever(Actions.sequence(Actions.fadeIn(COINS_BUTTON_FADE_DURATION, Interpolation.smooth), Actions.fadeOut(COINS_BUTTON_FADE_DURATION, Interpolation.smooth))));
            getImage().setScaling(Scaling.none);
            setSkin(skin);
            addListener(defineCoinsButtonClick());
        }

        private ButtonClickImproved defineCoinsButtonClick() {
            return new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    ArrayList<String> names = new ArrayList<String>();
                    names.add(PurchasePack.coins_1.name());
                    names.add(PurchasePack.coins_2.name());
                    names.add(PurchasePack.coins_3.name());
                    names.add(PurchasePack.coins_4.name());
                    createCoinsMonitor();
                    Parastrike.getGGS().queryProducts(names, CoinsButton.this);
                }

            };
        }

        public MonitorImproved createCoinsMonitor() {
            monitor = createMonitor(WIDTH, HEIGHT, Y, true, true, true, HEADER, NAME);
            if (monitor.getContent() == null) monitor.setContent(createLoadingContent());
            this.monitor.setCloseButton(true);
            this.monitor.setBackButtonVisibility(false);
            return monitor;
        }

        private WidgetGroup createLoadingContent() {
            Table table = new Table(getSkin());
            Label label = new Label(PLEASE_WAIT, getSkin().get(MEDIUM, Label.LabelStyle.class));
            label.setAlignment(Align.center);
            table.add(label);
            return table;
        }

        public void createAndSetCoinsMonitorContent(HashMap<String, PurchaseItem> purchaseItems) {
            Table table = new Table();
            table.top();
            BitmapFont tinyFont = Parastrike.getAssetsManager().get(TINY, BitmapFont.class);
            BitmapFont smallFont = Parastrike.getAssetsManager().get(SMALL, BitmapFont.class);
            createAdsRemoveLabel(table, smallFont);
            createBuyButtons(purchaseItems, table);
            createTaxLabel(table, tinyFont);
            monitor.setContent(table);
        }

        private void createTaxLabel(Table table, BitmapFont tinyFont) {
            Label tax = new Label(TAX, new Label.LabelStyle(tinyFont, Color.GOLD));
            table.add(tax).colspan(2).top().padTop(TAX_TEXT_PADDING_TOP).row();
        }

        private void createAdsRemoveLabel(Table table, BitmapFont smallFont) {
            Label adsRemoveLabel = new Label(ADS_REMOVE, new Label.LabelStyle(smallFont, Color.GOLD));
            table.add(adsRemoveLabel).colspan(2).top().padBottom(ADS_TEXT_PADDING_BOTTOM).row();
        }

        private void createBuyButtons(HashMap<String, PurchaseItem> purchaseItems, Table table) {
            Skin skin = getSkin();
            Drawable buttonDrawable = skin.getDrawable(INFO_BORDER);
            Drawable buttonPressedDrawable = skin.getDrawable(INFO_BORDER_PRESSED);
            table.add(createBuyButton(buttonDrawable, buttonPressedDrawable,
                    purchaseItems.get(PurchasePack.coins_1.name())))
                    .padBottom(BUY_PRODUCT_PADDING_BOTTOM);
            table.add(createBuyButton(buttonDrawable, buttonPressedDrawable,
                    purchaseItems.get(PurchasePack.coins_2.name())))
                    .padBottom(BUY_PRODUCT_PADDING_BOTTOM).row();
            table.add(createBuyButton(buttonDrawable, buttonPressedDrawable,
                    purchaseItems.get(PurchasePack.coins_3.name())));
            table.add(createBuyButton(buttonDrawable, buttonPressedDrawable,
                    purchaseItems.get(PurchasePack.coins_4.name()))).row();
        }

        private Stack createBuyButton(Drawable buttonDrawable, Drawable buttonPressedDrawable,
                                      final PurchaseItem purchaseItem) {
            ImageButton button = new ImageButton(buttonDrawable, buttonPressedDrawable);
            Table labelsTable = createContent(purchaseItem);
            Stack stack = new Stack(button, labelsTable);
            button.addListener(new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    Parastrike.getGGS().buy(purchaseItem.getId());
                }
            });
            return stack;
        }

        private Table createContent(PurchaseItem item) {
            Table labelsTable = new Table(getSkin());
            String purchaseValue = item.getTitle();
            Label.LabelStyle style = createStyle();
            labelsTable.add(new Label(purchaseValue, style)).row();
            labelsTable.add(createIcon(item, getSkin())).row();
            createPriceLabel(item, labelsTable, style);
            labelsTable.setTouchable(Touchable.disabled);
            return labelsTable;
        }

        private void createPriceLabel(PurchaseItem purchaseItem, Table labelsTable, Label.LabelStyle style) {
            String priceText = purchaseItem.getPrice() + " (" + purchaseItem.getCurrencyCode() + ")";
            labelsTable.add(new Label(priceText, style)).padTop(COINS_PRICE_PADDING_TOP);
        }

        private Label.LabelStyle createStyle() {
            BitmapFont font = Parastrike.getAssetsManager().get(SMALL, BitmapFont.class);
            return new Label.LabelStyle(font, Color.GOLD);
        }

        private Image createIcon(PurchaseItem purchaseItem, Skin skin) {
            Image icon = new Image(skin.getDrawable(purchaseItem.getId()));
            icon.setScaling(Scaling.none);
            return icon;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.setColor(Color.WHITE);
        }

        public void purchaseSuccess(String id) {
            createHoorayEffect(SFX.Menu.SCENE_UNLOCKED);
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            int newCoins = playerStatsHandler.getCoins() + PurchasePack.valueOf(id).getValue();
            playerStatsHandler.setCoins(newCoins, true);
            timer.scheduleTask(new Timer.Task() {
                @Override
                public void run() {
                    Parastrike.MenuType navigation = getNextScreen() == LEVEL_SELECTION ? SHOP : LOBBY;
                    Parastrike.getInstance().goToMenuScreen(navigation);
                }
            }, Rules.Menu.GetMoreCoinsMonitor.DELAY_AFTER_PURCHASE);
        }
    }
}
