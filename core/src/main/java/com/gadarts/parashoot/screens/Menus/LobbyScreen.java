package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SizeByAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Stack;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.GGSActionResolver;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.util.HashMap;

import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.AUTO_CLOUD_SAVING;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Settings.PREF_SETTINGS;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.Lobby;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.MentorsMessages;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.Shop;
import static com.gadarts.parashoot.assets.SFX.Menu.SYSTEM_WARNING;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.AttackButton.EXPAND_DURATION;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.AttackButton.INTERVAL;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.AttackButton.SHRINK_DURATION;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.AttackButton.SIZE_EFFECT_DELTA;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.CLOUD_OFFSET_X;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.CLOUD_OFFSET_Y;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.ATTRIBUTE_LABEL_PADDING_LEFT;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.ATTRIBUTE_PADDING_BOTTOM;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.BOMBS_X;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.BOMB_Y;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.BUNKER_X;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.BUNKER_Y;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.CANNONS_FLICKER_INTERVAL;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.CANNONS_FLICKER_REPEAT;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.CANNONS_X;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.HEADER_PADDING_BOTTOM;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.SIDEKICKS_X;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.TABLE_PADDING_HORIZONTAL;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.TABLE_PADDING_TOP;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.TAP_TO_CHANGE_PADDING_BOTTOM;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.InfoBorders.WEAPON_PADDING_BOTTOM;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.MONITOR_HEIGHT;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.MONITOR_WIDTH;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.MONITOR_Y;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.STACK_PADDING_LEFT;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.UPGRADE_BUTTON_X;
import static com.gadarts.parashoot.utils.Rules.Menu.Lobby.MainMonitor.UPGRADE_BUTTON_Y;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.AMMUNITION_DEPLETED;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.CHANGE_CANNON;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.CHANGE_SIDE_KICK;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.GO_TO_BATTLE;

/**
 * Created by Gad on 21/10/2016.
 */

public class LobbyScreen extends MenuScreenImproved {

    private MonitorImproved monitor;
    private MonitorImproved cannonSelectionMonitor;
    private MonitorImproved sideKicksSelectionMonitor;
    private Label.LabelStyle textStyle;
    private BunkerScheme bunkerScheme;
    private ButtonGroup<WeaponSelectionButton> cannonsSelectionButtonGroup;
    private Image gameSavedImage;
    private ImageButton attack;

    public LobbyScreen() {
        super(true, Assets.GFX.Sheets.Menus.LOBBY_DATA_FILE, Assets.GFX.Sheets.Menus.OPTIONS_BUTTONS_DATA_FILE, Assets.GFX.Sheets.Menus.WELCOME_BUTTONS_DATA_FILE);
        setScreens();
        createRegularTextStyle();
        createParastrikeMonitor();
        handleAmmoCorrection();
    }

    private final MentorTask TASK_MENTOR_GO_TO_BATTLE = new MentorTask() {
        @Override
        public void run() {
            super.run();
            showMentor(GO_TO_BATTLE, MentorsMessages.GO_TO_BATTLE, attack, getSkin(),
                    LobbyScreen.this);
        }
    };

    private void addCloudSavedImage() {
        gameSavedImage = new Image(getSkin(), Assets.GFX.Sheets.ImagesNames.GAME_SAVED);
        gameSavedImage.setVisible(false);
        gameSavedImage.setPosition(CLOUD_OFFSET_X, CLOUD_OFFSET_Y);
        monitor.addActor(gameSavedImage);
    }

    private void handleAmmoCorrection() {
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        if (playerStatsHandler.correctCannonSelection()) {
            correctCannonSelection(playerStatsHandler);
        }
    }


    private final Timer.Task TASK_FLICKER_CANNON = new Timer.Task() {
        @Override
        public void run() {
            boolean contentVisible = bunkerScheme.cannonInfoBorder.isContentVisible();
            bunkerScheme.cannonInfoBorder.setContentVisibility(!contentVisible);
            if (bunkerScheme.cannonInfoBorder.isVisible()) {
                Parastrike.getSoundPlayer().playSound(SYSTEM_WARNING, false, false);
            }
        }
    };

    private void correctCannonSelection(PlayerStatsHandler playerStatsHandler) {
        WeaponType selectedCannon = playerStatsHandler.getSelectedCannon();
        bunkerScheme.cannonInfoBorder.refresh(selectedCannon);
        bunkerScheme.setAmmoText(playerStatsHandler.getCannonAmmo((BulletType) selectedCannon));
        if (cannonsSelectionButtonGroup != null) checkSelectedCannon(selectedCannon);
        timer.scheduleTask(TASK_FLICKER_CANNON, 0, CANNONS_FLICKER_INTERVAL, CANNONS_FLICKER_REPEAT);
        Parastrike.getMentorsManager().readyMentorIfDidntRun(AMMUNITION_DEPLETED);
    }

    private void checkSelectedCannon(WeaponType selectedCannon) {
        for (WeaponSelectionButton button : cannonsSelectionButtonGroup.getButtons()) {
            if (button.getWeapon() == selectedCannon) {
                button.setChecked(true);
                button.weaponSelectionAction(selectedCannon);
            }
        }
    }

    private void createParastrikeMonitor() {
        monitor = createMonitor(createParastrikeContent(), MONITOR_WIDTH, MONITOR_HEIGHT, MONITOR_Y, false, true, false, Lobby.HEADER);
        addCloudSavedImage();
        addAttackButton(monitor);
    }

    private void setScreens() {
        setNextScreen(Parastrike.MenuType.LEVEL_SELECTION);
        setBackScreen(Parastrike.MenuType.SCENE_SELECTION);
    }

    private void createRegularTextStyle() {
        BitmapFont font = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.TINY, BitmapFont.class);
        textStyle = new Label.LabelStyle(font, Color.GOLD);
    }

    private ButtonClickImproved defineUpgradeButton(final ShopScreenImproved.Category target) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                goToShop(target);
            }
        };
    }

    private void createCannonSelectionMonitor() {
        cannonSelectionMonitor = createMonitor(null, Rules.Menu.Lobby.SelectionMonitors.WIDTH, Rules.Menu.Lobby.SelectionMonitors.HEIGHT, Rules.Menu.Lobby.SelectionMonitors.TARGET_Y, true, true, true, Lobby.HEADER_CANNON_SELECTION, "cannon_selection");
        if (cannonSelectionMonitor.getContent() == null)
            cannonSelectionMonitor.setContent(createSelectionLayout(Rules.Cannons.CANNONS_ORDER));
        cannonSelectionMonitor.setBackButtonVisibility(false);
        cannonSelectionMonitor.setCloseButton(true);
        createUnlockButtonForSelectionMonitor(cannonSelectionMonitor);
    }

    private void createSideKicksSelectionMonitor() {
        sideKicksSelectionMonitor = createMonitor(null, Rules.Menu.Lobby.SelectionMonitors.WIDTH, Rules.Menu.Lobby.SelectionMonitors.HEIGHT, Rules.Menu.Lobby.SelectionMonitors.TARGET_Y, true, true, true, Lobby.HEADER_SIDE_KICK_SELECTION, "sidekick_selection");
        if (sideKicksSelectionMonitor.getContent() == null)
            sideKicksSelectionMonitor.setContent(createSelectionLayout(SideKickFactory.SideKickType.values()));
        sideKicksSelectionMonitor.setBackButtonVisibility(false);
        sideKicksSelectionMonitor.setCloseButton(true);
        createUnlockButtonForSelectionMonitor(sideKicksSelectionMonitor);
    }

    private void createUnlockButtonForSelectionMonitor(MonitorImproved monitor) {
        ImageButton upgradeButton = new ImageButton(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.LOBBY_UNLOCK_BUTTON), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.LOBBY_UNLOCK_BUTTON_PRESSED));
        upgradeButton.getImage().setScaling(Scaling.none);
        upgradeButton.addListener(defineUpgradeButton((monitor == cannonSelectionMonitor) ? ShopScreenImproved.Category.CANNONS : ShopScreenImproved.Category.SIDE_KICKS));
        monitor.setTopLeftButton(upgradeButton);
    }

    private WidgetGroup createSelectionLayout(WeaponType[] weapons) {
        Table layout = new Table();
        layout.setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
        ButtonGroup buttonGroup = createWeaponSelectionButtons(layout, 4, weapons);
        if (weapons[0] instanceof BulletType) {
            cannonsSelectionButtonGroup = buttonGroup;
        }
        return layout;
    }

    private ButtonGroup createWeaponSelectionButtons(Table layout, int numberOfButtonsInLine, WeaponType[] weapons) {
        ButtonGroup buttonGroup = createButtonGroup((weapons[0] instanceof BulletType) ? 1 : 0);
        for (int i = 0; i < weapons.length; i++) {
            if (i != 0 && i % numberOfButtonsInLine == 0) layout.row();
            addWeaponButton(layout, buttonGroup, weapons[i]);
        }
        return buttonGroup;
    }

    private void addWeaponButton(Table layout, ButtonGroup buttonGroup, WeaponType weapon) {
        WeaponSelectionButton button = createWeaponButton(layout, buttonGroup, weapon);
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        WeaponType selectedWeapon = (weapon instanceof BulletType) ? playerStatsHandler.getSelectedCannon() : playerStatsHandler.getSelectedSideKick();
        if (selectedWeapon == weapon) {
            checkSelectedWeapon(button);
        }
    }

    private void checkSelectedWeapon(WeaponSelectionButton button) {
        button.setChecked(true);
        button.selectedSign.setVisible(true);
    }

    private WeaponSelectionButton createWeaponButton(Table layout, ButtonGroup buttonGroup, WeaponType weapon) {
        WeaponSelectionButton button = new WeaponSelectionButton(getSkin(), weapon);
        buttonGroup.add(button);
        layout.add(button).space(Rules.Menu.Lobby.SelectionMonitors.BUTTON_SPACING);
        return button;
    }

    private ButtonGroup createButtonGroup(int minCheckCount) {
        ButtonGroup buttonGroup = new ButtonGroup();
        buttonGroup.setMaxCheckCount(1);
        buttonGroup.setMinCheckCount(minCheckCount);
        buttonGroup.setUncheckLast(true);
        return buttonGroup;
    }


    private void addAttackButton(MonitorImproved monitor) {
        attack = new ImageButton(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.ATTACK_BUTTON), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.ATTACK_BUTTON_PRESSED));
        attack.setTransform(true);
        attack.setOrigin(Align.center);
        SizeByAction shrink = Actions.sizeBy(-SIZE_EFFECT_DELTA, -SIZE_EFFECT_DELTA, SHRINK_DURATION, Interpolation.bounce);
        SizeByAction expand = Actions.sizeBy(SIZE_EFFECT_DELTA, SIZE_EFFECT_DELTA, EXPAND_DURATION, Interpolation.bounce);
        attack.addAction(Actions.forever(Actions.sequence(shrink, expand, Actions.delay(INTERVAL))));
        attack.addListener(defineAttackButtonClick());
        monitor.setTopRightWidget(attack);
    }

    private ButtonClickImproved defineAttackButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Parastrike.getInstance().goToMenuScreen(getNextScreen());
            }
        };
    }

    private WidgetGroup createParastrikeContent() {
        Skin skin = getSkin();
        Table layout = new Table(skin);
        createBunkerScheme(skin, layout);
        bunkerScheme.setDebug(GameSettings.SHOW_TABLES_LINES);
        return layout;
    }

    @Override
    public void show() {
        super.show();
        Parastrike.getMentorsManager().readyMentorIfDidntRun(GO_TO_BATTLE);
        scheduleMentor(GO_TO_BATTLE, TASK_MENTOR_GO_TO_BATTLE);
    }

    private void createBunkerScheme(Skin skin, Table layout) {
        bunkerScheme = new BunkerScheme(skin, layout);
        layout.add(bunkerScheme).padLeft(STACK_PADDING_LEFT).row();
    }


    @Override
    protected void onBackPressed() {
        Parastrike.getInstance().goToMenuScreen(getBackScreen());
    }

    private void goToShop(ShopScreenImproved.Category category) {
        goToShop(category, -1);
    }

    private void goToShop(ShopScreenImproved.Category category, int selectedIndex) {
        HashMap<String, Integer> additionalInfo = null;
        if (selectedIndex > -1) {
            additionalInfo = createAdditionalInfo(category, selectedIndex);
        }
        Parastrike.getInstance().goToMenuScreen(Parastrike.MenuType.SHOP, additionalInfo);
    }

    private HashMap<String, Integer> createAdditionalInfo(ShopScreenImproved.Category category, int selectedIndex) {
        HashMap<String, Integer> additionalInfo;
        additionalInfo = new HashMap<String, Integer>();
        additionalInfo.put(ShopScreenImproved.TARGET_CATEGORY, category.ordinal());
        additionalInfo.put(ShopScreenImproved.TARGET_ITEM, selectedIndex);
        return additionalInfo;
    }

    private class InfoBorder extends Button {
        private Table table;
        private Image weaponIcon;

        public InfoBorder(Skin skin, String header) {
            super(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.INFO_BORDER), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.INFO_BORDER_PRESSED));
            setSkin(skin);
            createTable(header);
        }


        private void setContentVisibility(boolean visible) {
            weaponIcon.setVisible(visible);
        }

        private void createTable(String header) {
            table = new Table(getSkin());
            table.setTouchable(Touchable.disabled);
            table.setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
            setTablePadding();
            addHeader(header);
            setTableSizeAndPosition();
            addActor(table);
        }

        public void setTablePadding() {
            table.padTop(TABLE_PADDING_TOP);
            table.padLeft(TABLE_PADDING_HORIZONTAL);
            table.padRight(TABLE_PADDING_HORIZONTAL);
        }

        private void setTableSizeAndPosition() {
            table.setSize(getWidth(), getHeight());
            table.setBounds(getX(), getY(), getWidth(), getHeight());
            table.top();
            table.left();
        }

        private void addHeader(String header) {
            table.add(new Label(header, textStyle)).top().colspan(2).padBottom(HEADER_PADDING_BOTTOM).left();
            table.row();
        }

        public Label addAttribute(String label, String icon, String value) {
            addIcon(icon);
            addAttributeName(label);
            Label valueLabel = new Label(value, textStyle);
            valueLabel.setAlignment(Align.right);
            table.add(valueLabel).right().expandX().padBottom(ATTRIBUTE_PADDING_BOTTOM);
            table.row();
            return valueLabel;
        }

        private void addAttributeName(String label) {
            Label attributeName = new Label(label, textStyle);
            attributeName.setAlignment(Align.left);
            table.add(attributeName).padLeft(ATTRIBUTE_LABEL_PADDING_LEFT).padBottom(ATTRIBUTE_PADDING_BOTTOM).left();
        }

        private void addIcon(String icon) {
            Image iconImage = new Image(getSkin(), icon);
            iconImage.setScaling(Scaling.none);
            table.add(iconImage).padBottom(ATTRIBUTE_PADDING_BOTTOM);
        }

        public void putWeapon(WeaponType selectedWeapon) {
            weaponIcon = new Image(getSkin(), (selectedWeapon != null) ? selectedWeapon.getIcon() : Assets.GFX.Sheets.ImagesNames.ICON_NONE);
            table.add(weaponIcon).expand().padBottom(WEAPON_PADDING_BOTTOM);
            table.row();
            table.add(new Label(Lobby.InfoBorders.LABEL_CHANGE, textStyle)).padBottom(TAP_TO_CHANGE_PADDING_BOTTOM);
        }

        public void refresh(WeaponType weapon) {
            if (weaponIcon != null) {
                weaponIcon.setDrawable(getSkin(), weapon.getIcon());
            }
        }

        public boolean isContentVisible() {
            return weaponIcon.isVisible();
        }
    }

    private class BunkerScheme extends Image {

        private Label ammoLabel;
        private InfoBorder bunkerInfoBorder;
        private InfoBorder cannonInfoBorder;
        private InfoBorder bombsInfoBorder;
        private InfoBorder sideKicksInfoBorder;

        public BunkerScheme(Skin skin, Table layout) {
            super(skin, Assets.GFX.Sheets.ImagesNames.LOBBY_BUNKER);
            setTouchable(Touchable.childrenOnly);
            createInfoBorders(layout);
            createAmmoInfo(layout);
            createUpgradeButton(layout);
            scheduleMentors();
        }

        private void scheduleMentors() {
            scheduleMentor(CHANGE_CANNON, TASK_MENTOR_CHANGE_CANNON);
            scheduleMentor(CHANGE_SIDE_KICK, TASK_MENTOR_CHANGE_SIDE_KICK);
            scheduleMentor(AMMUNITION_DEPLETED, TASK_MENTOR_AMMUNITION_DEPLETED);
        }

        private final MentorTask TASK_MENTOR_CHANGE_CANNON = new MentorTask() {
            @Override
            public void run() {
                super.run();
                showMentor(CHANGE_CANNON, MentorsMessages.CHANGE_CANNON, cannonInfoBorder,
                        getSkin(), LobbyScreen.this);
            }
        };

        private final MentorTask TASK_MENTOR_CHANGE_SIDE_KICK = new MentorTask() {
            @Override
            public void run() {
                super.run();
                showMentor(CHANGE_SIDE_KICK,
                        MentorsMessages.CHANGE_SIDE_KICK,
                        sideKicksInfoBorder,
                        getSkin(), LobbyScreen.this);
            }
        };

        private final MentorTask TASK_MENTOR_AMMUNITION_DEPLETED = new MentorTask() {
            @Override
            public void run() {
                super.run();
                showMentor(AMMUNITION_DEPLETED,
                        MentorsMessages.AMMUNITION_DEPLETED,
                        cannonInfoBorder,
                        getSkin(), LobbyScreen.this);
            }
        };


        private void createUpgradeButton(Table layout) {
            Skin skin = getSkin();
            Button upgradeButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LOBBY_UPGRADE_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LOBBY_UPGRADE_BUTTON_PRESSED));
            layout.addActor(upgradeButton);
            upgradeButton.setPosition(UPGRADE_BUTTON_X, UPGRADE_BUTTON_Y);
            upgradeButton.addListener(defineUpgradeButton(ShopScreenImproved.Category.CANNONS));
        }


        private void createAmmoInfo(Table layout) {
            addAmmoHeader(layout);
            addAmmoValue(layout);
        }

        private void addAmmoValue(Table layout) {
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            ammoLabel = new Label(null, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GOLD));
            setAmmoText(playerStatsHandler.getCannonAmmo((BulletType) playerStatsHandler.getSelectedCannon()));
            ammoLabel.setAlignment(Align.center);
            layout.addActor(ammoLabel);
            ammoLabel.setPosition(Rules.Menu.Lobby.MainMonitor.Ammo.VALUE_LABEL_X, Rules.Menu.Lobby.MainMonitor.Ammo.VALUE_LABEL_Y);
        }

        private void setAmmoText(int cannonAmmo) {
            ammoLabel.setText((cannonAmmo == -1) ? "---" : Integer.toString(cannonAmmo));
            ammoLabel.setColor((cannonAmmo == 1) ? Color.RED : Color.GOLD);
        }

        private void addAmmoHeader(Table layout) {
            final Label ammoHeader = new Label(Lobby.AMMO_HEADER, textStyle);
            ammoHeader.setAlignment(Align.center);
            layout.addActor(ammoHeader);
            ammoHeader.setPosition(Rules.Menu.Lobby.MainMonitor.Ammo.HEADER_LABEL_X, Rules.Menu.Lobby.MainMonitor.Ammo.HEADER_LABEL_Y);
        }

        private void addInfoBorders(Table layout) {
            layout.addActor(bunkerInfoBorder);
            layout.addActor(cannonInfoBorder);
            layout.addActor(bombsInfoBorder);
            layout.addActor(sideKicksInfoBorder);
        }

        private void createInfoBorders(Table layout) {
            createBunkerInfoBorder();
            createCannonInfoBorder();
            createBombsInfoBorder();
            createSideKickInfoBorder();
            addInfoBorders(layout);
        }

        private void createSideKickInfoBorder() {
            sideKicksInfoBorder = new InfoBorder(getSkin(), Lobby.InfoBorders.SIDEKICK_HEADER);
            sideKicksInfoBorder.putWeapon(Parastrike.getPlayerStatsHandler().getSelectedSideKick());
            sideKicksInfoBorder.addListener(defineSideKicksInfoBorderClick());
        }

        private void createBombsInfoBorder() {
            bombsInfoBorder = new InfoBorder(getSkin(), Lobby.InfoBorders.BOMBS_HEADER);
            bombsInfoBorder.addAttribute(Shop.ArmoryNames.ShortArmoryNames.BIO, Assets.GFX.Sheets.ImagesNames.SMALL_BIO_ICON, String.valueOf(Parastrike.getPlayerStatsHandler().getBombAmount(Rules.Player.ArmoryItem.BIO_HAZARD)));
            bombsInfoBorder.addAttribute(Shop.ArmoryNames.ShortArmoryNames.AIR, Assets.GFX.Sheets.ImagesNames.SMALL_BOMB_ICON, String.valueOf(Parastrike.getPlayerStatsHandler().getBombAmount(Rules.Player.ArmoryItem.AIR_STRIKE)));
            bombsInfoBorder.addAttribute(Shop.ArmoryNames.ShortArmoryNames.ATOM, Assets.GFX.Sheets.ImagesNames.SMALL_ATOM_ICON, String.valueOf(Parastrike.getPlayerStatsHandler().getBombAmount(Rules.Player.ArmoryItem.ATOM)));
            bombsInfoBorder.addListener(new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    goToShop(ShopScreenImproved.Category.ARMORY, Rules.Player.ArmoryItem.BIO_HAZARD.ordinal());
                }
            });
        }

        private void createCannonInfoBorder() {
            cannonInfoBorder = new InfoBorder(getSkin(), Lobby.InfoBorders.CANNON_HEADER);
            cannonInfoBorder.putWeapon(Parastrike.getPlayerStatsHandler().getSelectedCannon());
            cannonInfoBorder.addListener(defineCannonInfoBorderClick());
        }

        private ButtonClickImproved defineCannonInfoBorderClick() {
            return new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    createCannonSelectionMonitor();
                }
            };
        }

        private ButtonClickImproved defineSideKicksInfoBorderClick() {
            return new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    createSideKicksSelectionMonitor();
                }
            };
        }

        private void createBunkerInfoBorder() {
            bunkerInfoBorder = new InfoBorder(getSkin(), Lobby.InfoBorders.BUNKER_HEADER);
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            bunkerInfoBorder.addAttribute(Shop.Stats.ARMOR, Assets.GFX.Sheets.ImagesNames.SMALL_ARMOR_ICON, Double.toString((playerStatsHandler.getBunkerArmorLevel() / 10.0) + 1.0));
            bunkerInfoBorder.addAttribute(Shop.Stats.GENERATOR, Assets.GFX.Sheets.ImagesNames.SMALL_GEAR_ICON, Double.toString((playerStatsHandler.getBunkerGeneratorLevel() / 10.0) + 1.0));
            bunkerInfoBorder.addListener(new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    goToShop(ShopScreenImproved.Category.ARMORY, 0);
                }
            });
        }


        @Override
        public void act(float delta) {
            super.act(delta);
            bunkerInfoBorder.setPosition(BUNKER_X, BUNKER_Y);
            cannonInfoBorder.setPosition(CANNONS_X, BUNKER_Y);
            bombsInfoBorder.setPosition(BOMBS_X, BOMB_Y);
            sideKicksInfoBorder.setPosition(SIDEKICKS_X, BOMB_Y);
        }
    }

    private class WeaponSelectionButton extends Button {
        private final WeaponType weapon;
        private Table selectedSign;
        private Table specialSign;

        public WeaponSelectionButton(Skin skin, final WeaponType weaponType) {
            super(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.INFO_BORDER), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.INFO_BORDER_PRESSED), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.INFO_BORDER_PRESSED));
            setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
            setSkin(skin);
            weapon = weaponType;
            addWidgets();
            addListener(defineClick(weaponType));
        }

        private void addWidgets() {
            Stack stack = new Stack();
            addIconImage(stack);
            createSelectedSign(stack);
            createLockSign(stack);
            add(stack).row();
            addLabel();
        }

        private void addLabel() {
            Table table = new Table();
            table.setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
            Label label = new Label(weapon.getName(), textStyle);
            label.setAlignment(Align.bottom);
            table.add(label).bottom().expand();
            table.bottom().padBottom(Rules.Menu.Lobby.SelectionMonitors.WEAPON_NAME_PADDING_BOTTOM);
            add(table);
        }

        private void createSelectedSign(Stack stack) {
            Image selectedSignImage = new Image(getSkin(), Assets.GFX.Sheets.ImagesNames.SELECTED_SIGN_BIG);
            selectedSignImage.setScaling(Scaling.none);
            selectedSign = new Table(getSkin());
            selectedSign.add(selectedSignImage).expand();
            stack.add(selectedSign);
        }

        private void createLockSign(Stack stack) {
            Image lockSignImage = new Image(getSkin(), Assets.GFX.Sheets.ImagesNames.LOCK);
            lockSignImage.setScaling(Scaling.none);
            specialSign = new Table(getSkin());
            specialSign.add(lockSignImage).expand();
            stack.add(specialSign);
            handleSpecialSigns();
        }

        private void handleSpecialSigns() {
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            boolean isCannon = weapon instanceof BulletType;
            if ((!(isCannon ? playerStatsHandler.isCannonEnabled(weapon) : playerStatsHandler.isSideKickEnabled(weapon))))
                setSpecialSignImage(Assets.GFX.Sheets.ImagesNames.LOCK);
            else if ((isCannon && playerStatsHandler.getCannonAmmo((BulletType) weapon) == 0)) {
                setSpecialSignImage(Assets.GFX.Sheets.ImagesNames.NO_AMMO_SIGN);
            } else {
                setDisabled(false);
                specialSign.setVisible(false);
            }
        }

        private void setSpecialSignImage(String lock) {
            setDisabled(true);
            specialSign.setVisible(true);
            Image image = (Image) specialSign.getCells().first().getActor();
            image.setDrawable(getSkin(), lock);
        }


        @Override
        public void setChecked(boolean isChecked) {
            super.setChecked(isChecked);
            if (!isChecked) {
                selectedSign.setVisible(false);
            }
        }

        private void addIconImage(Stack stack) {
            Image image = new Image(getSkin(), weapon.getIcon());
            image.setScaling(Scaling.none);
            stack.add(image);
        }

        private ClickListener defineClick(final WeaponType weaponType) {
            return new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (weaponType instanceof BulletType && Parastrike.getPlayerStatsHandler().getCannonAmmo((BulletType) weaponType) == 0) {
                        goToShop(ShopScreenImproved.Category.CANNONS, GameUtils.getIndexOfValueInArray(Rules.Cannons.CANNONS_ORDER, weaponType));
                    } else weaponSelectionAction(weaponType);
                }
            };
        }

        private void weaponSelectionAction(WeaponType weaponType) {
            if (isDisabled()) return;
            selectedSign.setVisible(true);
            if (weaponType instanceof BulletType) cannonClick(weaponType);
            else sideKickClick(weaponType);
        }

        private void sideKickClick(WeaponType weaponType) {
            Parastrike.getPlayerStatsHandler().setSelectedSideKick(weaponType);
            sideKicksSelectionMonitor.setActivation(false);
            bunkerScheme.sideKicksInfoBorder.refresh(weaponType);
        }

        private void cannonClick(WeaponType weaponType) {
            Parastrike.getPlayerStatsHandler().setSelectedCannon(weaponType);
            cannonSelectionMonitor.setActivation(false);
            bunkerScheme.cannonInfoBorder.refresh(weaponType);
            bunkerScheme.setAmmoText(Parastrike.getPlayerStatsHandler().getCannonAmmo((BulletType) weaponType));
        }

        public WeaponType getWeapon() {
            return weapon;
        }
    }
}
