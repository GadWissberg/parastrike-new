package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Group;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.*;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

import java.util.HashMap;

import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.Menus.ShopMenu.*;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.MentorsMessages;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.ShopImproved;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.*;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.SWIPE_DURATION;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;

/**
 * Created by Gad on 20/10/2017.
 */

public class ShopScreenImproved extends MenuScreenImproved {
    public static final String TARGET_CATEGORY = "target_category";
    public static final String TARGET_ITEM = "target_item";
    private MonitorImproved monitor;
    private Category selectedCategory = Category.CANNONS;
    private WeaponType selectedItem = BulletType.CANNON_BALL;
    private CategoryButton cannonsButton, armoryButton, sideKicksButton;
    private ParastrikeContent mainContent;
    private Label currentCoinsLabel;
    private ErrorContent errorContent;
    private Stack itemsStack;


    enum Category {
        CANNONS(Rules.Cannons.CANNONS_ORDER), ARMORY(Rules.Player.ArmoryItem.values()), SIDE_KICKS(SideKickFactory.SideKickType.values());


        private final WeaponType[] items;

        Category(WeaponType[] items) {
            this.items = items;
        }

        public WeaponType[] getItems() {
            return items;
        }


    }

    public ShopScreenImproved() {
        super(true,
                CANNONS_THUMBS_DATA_FILE,
                ARMORY_THUMBS_DATA_FILE,
                SIDEKICKS_THUMBS_DATA_FILE,
                SHOP_MENU_DATA_FILE,
                SHOP_MENU_ILLUSTRATIONS_DATA_FILE);
        initializeShopScreen();
    }

    @Override
    public void show() {
        super.show();
        scheduleMentor(UPGRADE_GENERATOR, TASK_SHOW_UPGRADE_GENERATOR_MENTOR);
        scheduleMentor(UPGRADE_ARMOR, TASK_SHOW_UPGRADE_ARMOR_MENTOR);
        Parastrike.getMentorsManager().readyMentorIfDidntRun(SCROLLABLE);
        scheduleMentor(SCROLLABLE, TASK_SHOW_SCROLLABLE_MENTOR);
    }

    private void initializeShopScreen() {
        setBackScreen(Parastrike.MenuType.LOBBY);
        addFontsToSkin();
        createContents();
        setDefaultSelection();
    }

    private void createContents() {
        monitor = createMonitor(createMonitorContent(), Rules.Menu.ShopScreenImproved.Monitor.WIDTH, Rules.Menu.ShopScreenImproved.Monitor.HEIGHT, Rules.Menu.ShopScreenImproved.Monitor.Y, false, true, false, ShopImproved.Monitor.HEADER);
        createTopRightTable();
    }

    @Override
    public void invokeAdditionalInfo(HashMap<String, Integer> additionalInfo) {
        super.invokeAdditionalInfo(additionalInfo);
        if (additionalInfo != null) if (additionalInfo.containsKey(TARGET_CATEGORY)) {
            selectedCategory = Category.values()[additionalInfo.get(TARGET_CATEGORY)];
            Integer itemIndex = additionalInfo.get(TARGET_ITEM);
            setSelectedItemAccordingToRequest(itemIndex);
            mainContent.refresh();
        }
    }

    private void setSelectedItemAccordingToRequest(Integer index) {
        if (selectedCategory == Category.CANNONS) {
            simulatePushDownUpAndSetSelectedItem(cannonsButton, index, Rules.Cannons.CANNONS_ORDER);
        } else if (selectedCategory == Category.SIDE_KICKS) {
            simulatePushDownUpAndSetSelectedItem(sideKicksButton, index, SideKickFactory.SideKickType.values());
        } else
            simulatePushDownUpAndSetSelectedItem(armoryButton, index, Rules.Player.ArmoryItem.values());
    }


    private void simulatePushDownUpAndSetSelectedItem(CategoryButton button, int index, WeaponType[] array) {
        selectedItem = array[index];
        button.setChecked(true);
        manageItemsListsVisibility(selectedCategory);
    }

    private void setDefaultSelection() {
        selectedCategory = Category.CANNONS;
        selectedItem = getStrogestEnabledCannon();
        timer.scheduleTask(TASK_DEFAULT_ENTRANCE, 1.5f);
        mainContent.refresh();
    }

    private void addFontsToSkin() {
        Skin skin = getSkin();
        skin.add(Rules.System.FontsParameters.DigitalFontNames.SMALL, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.DigitalFontNames.SMALL, BitmapFont.class), Color.GOLD));
        skin.add(Rules.System.FontsParameters.DigitalFontNames.TINY, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.DigitalFontNames.TINY, BitmapFont.class), Color.GOLD));
        addArmyFontsToSkin();
        skin.add(Rules.System.FontsParameters.RegularFontNames.TINY, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.TINY, BitmapFont.class), Color.GOLD));
        addRegularSmallFontsToSkin();
    }

    private void addArmyFontsToSkin() {
        Skin skin = getSkin();
        skin.add(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, BitmapFont.class), Color.GOLD));
        skin.add(Rules.System.FontsParameters.ArmyFontNames.HUGE, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.HUGE, BitmapFont.class), Color.GOLD));
    }

    private void addRegularSmallFontsToSkin() {
        Skin skin = getSkin();
        skin.add(Rules.System.FontsParameters.RegularFontNames.SMALL, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GOLD));
        skin.add(Rules.System.FontsParameters.RegularFontNames.SMALL + Color.GREEN, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GREEN));
        skin.add(Rules.System.FontsParameters.RegularFontNames.SMALL + Color.RED, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.RED));
        skin.add(Rules.System.FontsParameters.RegularFontNames.MEDIUM + Color.RED, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.RED));
    }

    private void createTopRightTable() {
        Table topRightTable = new Table(getSkin());
        createTopRightCoinImage(topRightTable);
        createTopRightLabelAndCoinsButton(topRightTable);
        monitor.setTopRightWidget(topRightTable);
    }

    private void createTopRightCoinImage(Table topRightTable) {
        Image coinImage = new Image(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.SMALL_COIN_ICON));
        coinImage.setScaling(Scaling.none);
        topRightTable.add(coinImage);
    }

    private void createTopRightLabelAndCoinsButton(Table topRightTable) {
        Skin skin = getSkin();
        currentCoinsLabel = new Label(Integer.toString(Parastrike.getPlayerStatsHandler().getCoins()), skin.get(Rules.System.FontsParameters.DigitalFontNames.SMALL, Label.LabelStyle.class));
        currentCoinsLabel.setAlignment(Align.center);
        topRightTable.add(currentCoinsLabel).width(200).row();
    }

    private WidgetGroup createMonitorContent() {
        Table table = new Table(getSkin());
        createCategoryButtons(table);
        createParastrikeContents(table);
        createItemsStack(table);
        return table;
    }

    private void createItemsStack(Table table) {
        itemsStack = new Stack(createLayoutScrollPane(createCategoryItems(Category.CANNONS)), createLayoutScrollPane(createCategoryItems(Category.ARMORY)), createLayoutScrollPane(createCategoryItems(Category.SIDE_KICKS)));
        manageItemsListsVisibility(Category.CANNONS);
        itemsStack.setDebug(GameSettings.SHOW_TABLES_LINES);
        table.add(itemsStack).center().colspan(1).width(Rules.Menu.ShopScreenImproved.Monitor.ITEMS_STACK_WIDTH);
    }

    private void createParastrikeContents(Table table) {
        mainContent = new ParastrikeContent(getSkin());
        errorContent = new ErrorContent(getSkin());
        Stack mainContentStack = new Stack(mainContent, errorContent);
        mainContent.refresh();
        table.add(mainContentStack).expandX().colspan(2);
    }

    private Table createCategoryItems(Category category) {
        return createCategoryItemsTable(category);
    }

    private CategoryItems createCategoryItemsTable(Category category) {
        CategoryItems categoryItems = new CategoryItems(getSkin(), category);
        categoryItems.setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
        return categoryItems;
    }


    private ButtonGroup<CategoryItemButton> createItemsGroup() {
        ButtonGroup<CategoryItemButton> buttonGroup = new ButtonGroup<>();
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setMaxCheckCount(1);
        return buttonGroup;
    }


    private void createCategoryButtons(Table table) {
        createCannonsButton(table);
        createArmoryButton(table);
        createSideKicksButton(table);
        table.row();
        createCategoryButtonsGroup();
        cannonsButton.setChecked(true);
    }

    private void createCategoryButtonsGroup() {
        ButtonGroup group = new ButtonGroup(cannonsButton, armoryButton, sideKicksButton);
        group.setMinCheckCount(1);
        group.setMaxCheckCount(1);
    }

    public SideKickFactory.SideKickType getStrogestEnabledSideKick() {
        SideKickFactory.SideKickType[] sideKicks = SideKickFactory.SideKickType.values();
        SideKickFactory.SideKickType sideKick = null;
        for (int i = sideKicks.length - 1; i >= 0; i--) {
            sideKick = sideKicks[i];
            if (Parastrike.getPlayerStatsHandler().getSideKickAttribute(sideKick, Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED) == 1)
                break;
        }
        return sideKick;
    }


    public BulletType getStrogestEnabledCannon() {
        BulletType[] cannons = Rules.Cannons.CANNONS_ORDER;
        BulletType cannon = null;
        for (int i = cannons.length - 1; i >= 0; i--) {
            cannon = cannons[i];
            if (Parastrike.getPlayerStatsHandler().getCannonAttribute(cannon, Rules.Player.UpgradeableAttribute.WeaponAttribute.ENABLED) == 1)
                break;
        }
        return cannon;
    }

    private void createSideKicksButton(Table table) {
        Skin skin = getSkin();
        sideKicksButton = new CategoryButton(Category.SIDE_KICKS, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.SIDEKICK_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.SIDEKICK_BUTTON_PRESSED), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.SIDEKICK_BUTTON_CHECK));
        table.add(sideKicksButton).top().right().padBottom(Rules.Menu.ShopScreenImproved.Monitor.CATEGORY_BUTTONS_PADDING_VERTICAL).padTop(Rules.Menu.ShopScreenImproved.Monitor.CATEGORY_BUTTONS_PADDING_VERTICAL);
        sideKicksButton.addListener(defineCategoryClick(Category.SIDE_KICKS, getStrogestEnabledSideKick()));
    }

    private void createArmoryButton(Table table) {
        Skin skin = getSkin();
        armoryButton = new CategoryButton(Category.ARMORY, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.ARMORY_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.ARMORY_BUTTON_PRESSED), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.ARMORY_BUTTON_CHECK));
        table.add(armoryButton).top().expandX().center().padBottom(Rules.Menu.ShopScreenImproved.Monitor.CATEGORY_BUTTONS_PADDING_VERTICAL).padTop(Rules.Menu.ShopScreenImproved.Monitor.CATEGORY_BUTTONS_PADDING_VERTICAL);
        armoryButton.addListener(defineCategoryClick(Category.ARMORY, Rules.Player.ArmoryItem.BUNKER));
    }

    private void createCannonsButton(Table table) {
        Skin skin = getSkin();
        cannonsButton = new CategoryButton(Category.CANNONS, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.CANNONS_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.CANNONS_BUTTON_PRESSED), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.CANNONS_BUTTON_CHECK));
        table.add(cannonsButton).top().left().pad(Rules.Menu.ShopScreenImproved.Monitor.CATEGORY_BUTTONS_PADDING_VERTICAL, Rules.Menu.ShopScreenImproved.Monitor.CANNONS_CATEGORY_BUTTON_PADDING_LEFT, Rules.Menu.ShopScreenImproved.Monitor.CATEGORY_BUTTONS_PADDING_VERTICAL, 0);
        cannonsButton.addListener(defineCategoryClick(Category.CANNONS, getStrogestEnabledCannon()));
    }

    private ButtonClickImproved defineCategoryClick(final Category category, final WeaponType item) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedCategory = category;
                selectedItem = item;
                errorContent.hide();
                mainContent.refresh();
                manageItemsListsVisibility(category);
            }


        };
    }

    private void manageItemsListsVisibility(Category category) {
        for (int i = 0; i < itemsStack.getChildren().size; i++) {
            boolean visible = category.ordinal() == i;
            ScrollPane currentListScrollPane = (ScrollPane) itemsStack.getChildren().get(i);
            currentListScrollPane.setVisible(visible);
            if (visible) ((CategoryItems) currentListScrollPane.getActor()).refresh();
        }
    }


    private final Timer.Task TASK_DEFAULT_ENTRANCE = new Timer.Task() {
        @Override
        public void run() {
            manageItemsListsVisibility(selectedCategory);
        }
    };

    private class CategoryButton extends Button {


        private final Category category;

        public CategoryButton(Category category, Drawable regular, Drawable pressed, Drawable checked) {
            super(regular, pressed, checked);
            this.category = category;
        }

        @Override
        public void setChecked(boolean isChecked) {
            super.setChecked(isChecked);
            if (isChecked) {
                selectedCategory = category;
            }
        }


    }

    private final Timer.Task TASK_SHOW_MAIN_CONTENT = new Timer.Task() {
        @Override
        public void run() {
            mainContent.addAction(Actions.fadeIn(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
            errorContent.addAction(Actions.sequence(Actions.fadeOut(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth), Actions.visible(false)));
        }
    };

    private final MentorTask TASK_SHOW_UPGRADE_GENERATOR_MENTOR = new MentorTask() {
        @Override
        public void run() {
            super.run();
            showMentor(UPGRADE_GENERATOR, MentorsMessages.UPGRADE_GENERATOR, armoryButton,
                    getSkin(), ShopScreenImproved.this);
        }
    };

    private final MentorTask TASK_SHOW_UPGRADE_ARMOR_MENTOR = new MentorTask() {
        @Override
        public void run() {
            super.run();
            showMentor(UPGRADE_ARMOR, MentorsMessages.UPGRADE_ARMOR, armoryButton, getSkin(),
                    ShopScreenImproved.this);
        }
    };

    private final MentorTask TASK_SHOW_SCROLLABLE_MENTOR = new MentorTask() {
        @Override
        public void run() {
            super.run();
            SequenceAction actions = createMentorAction();
            showMentor(SCROLLABLE, MentorsMessages.SCROLLABLE, actions, getSkin());
        }

        private SequenceAction createMentorAction() {
            float mainContentX = mainContent.getX();
            float mainContentY = mainContent.getY();
            MoveToAction initialPosition = Actions.moveTo(mainContentX, mainContentY);
            RepeatAction swipe = createMentorSwipeAction();
            MoveToAction leave = createMentorLeaveAction();
            return Actions.sequence(initialPosition, swipe, leave);
        }

        private MoveToAction createMentorLeaveAction() {
            return Actions.moveTo(0,
                    HEIGHT_TARGET_RESOLUTION,
                    SWIPE_DURATION,
                    Interpolation.smooth);
        }

        private RepeatAction createMentorSwipeAction() {
            float contentX = mainContent.getX();
            MoveToAction moveUp = Actions.moveTo(contentX,
                    mainContent.getY() + mainContent.getHeight(),
                    SWIPE_DURATION, Interpolation.exp5);
            MoveToAction moveDown = Actions.moveTo(contentX,
                    mainContent.getY(),
                    SWIPE_DURATION, Interpolation.exp5);
            return Actions.repeat(3, Actions.sequence(moveUp, moveDown));
        }
    };

    private class CategoryItemButton extends ImageButton {


        private final WeaponType item;
        private final Category category;

        public CategoryItemButton(final WeaponType item, Label.LabelStyle style, Category category) {
            super(ShopScreenImproved.this.getSkin().getDrawable(item.getThumb()));
            this.item = item;
            this.category = category;
            initializeButton(style);
        }

        private void initializeButton(Label.LabelStyle style) {
            addAction(fadeOut(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
            row();
            addListener(defineClick());
            createBottomPart(style);
        }

        private void createBottomPart(Label.LabelStyle style) {
            add(new Label(item.getName(), style));
        }


        private ButtonClickImproved defineClick() {
            return new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    addAction(fadeIn(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
                    errorContent.hide();
                    updateSelectedItemAndRefresh();
                }
            };
        }

        private void updateSelectedItemAndRefresh() {
            if (selectedItem != item) {
                selectedCategory = category;
                selectedItem = item;
                mainContent.refresh();
                mainContent.setScrollY(0);
            }
        }

        @Override
        public void setChecked(boolean isChecked) {
            if (!isChecked() && isChecked) {
                addAction(fadeIn(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
                updateSelectedItemAndRefresh();
            } else if (isChecked() && !isChecked)
                addAction(fadeOut(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
            super.setChecked(isChecked);
        }

        private AlphaAction fadeIn(float duration, Interpolation interpolation) {
            AlphaAction action = Actions.action(AlphaAction.class);
            action.setAlpha(1);
            action.setDuration(duration);
            action.setInterpolation(interpolation);
            return action;
        }

        private AlphaAction fadeOut(float duration, Interpolation interpolation) {
            AlphaAction action = Actions.action(AlphaAction.class);
            action.setAlpha(0.3f);
            action.setDuration(duration);
            action.setInterpolation(interpolation);
            return action;
        }

        public WeaponType getItem() {
            return item;
        }
    }

    private class ParastrikeContent extends ScrollPane {
        private Label header;
        private Table table;
        private Label description;
        private Table upgradersTable;
        private Table purchaseTable;
        private Upgrader upgraderTop, upgraderCenter, upgraderBottom;
        private MonitorImproved infoMonitor;
        private Label infoTextLabel;
        private Cell<Stack> middleStackCell;
        private Label purchasePrice;
        private Button purchaseButton;
        private Label purchaseAdditionalLabel;
        private Cell<Image> IllustrationCell;

        public ParastrikeContent(Skin skin) {
            super(new Table(skin), ShopScreenImproved.this.getScrollPaneStyle());
            defineTable();
            setScrollPaneAttributes();
            createContent();
        }

        private void createContent() {
            createHeader();
            createMiddleStack();
            createIllustration();
            createDescription();
        }

        private void createPurchaseTable() {
            purchaseTable = new Table();
            createPurchasePriceLabel();
            createPurchaseButton();
            createPurchaseAdditionalLabel();
        }

        private void createPurchaseButton() {
            Skin skin = getSkin();
            purchaseButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.PURCHASE_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.PURCHASE_BUTTON_PRESSED));
            purchaseButton.addListener(definePurchaseButtonClick());
            purchaseTable.add(purchaseButton).row();
        }

        private void createPurchaseAdditionalLabel() {
            purchaseAdditionalLabel = new Label("", getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL + Color.RED, Label.LabelStyle.class));
            purchaseAdditionalLabel.setWrap(true);
            purchaseAdditionalLabel.setAlignment(Align.center);
            purchaseTable.add(purchaseAdditionalLabel).padTop(Rules.Menu.ShopScreenImproved.Monitor.PURCHASE_ADDITIONAL_LABEL_PADDING_TOP).row();
        }

        private void createPurchasePriceLabel() {
            purchasePrice = new Label("price", getSkin().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, Label.LabelStyle.class));
            purchaseTable.add(purchasePrice).padBottom(Rules.Menu.ShopScreenImproved.Monitor.PURCHASE_COST_PADDING_BOTTOM).row();
        }


        private ButtonClickImproved definePurchaseButtonClick() {
            return new ButtonClickImproved() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    super.clicked(event, x, y);
                    if (Parastrike.getPlayerStatsHandler().getCoins() >= selectedItem.getCost()) {
                        performPurchase();
                    } else showError(ShopImproved.Monitor.NOT_ENOUGH);
                }

                private void performPurchase() {
                    if (selectedCategory == Category.CANNONS) {
                        purchaseCannon();
                    } else if (selectedCategory == Category.SIDE_KICKS) {
                        purchaseSideKick();
                    } else purchaseArmory();
                }

                private void purchaseCannon() {
                    int minGeneratorReq = ((BulletType) ShopScreenImproved.this.selectedItem).getMinGeneratorReq();
                    if ((Parastrike.getPlayerStatsHandler().getBunkerGeneratorLevel() / 10) + 1 >= minGeneratorReq) {
                        updateCannonStats();
                        Parastrike.getMentorsManager().readyMentorIfDidntRun(CHANGE_CANNON);
                    } else
                        showError(String.format(ShopImproved.Monitor.UPGRADE_GEN, minGeneratorReq));
                }

                private void purchaseSuccess(String event) {
                    refresh();
                    Parastrike.getSoundPlayer().playSound(SFX.Menu.SYSTEM_CONFIRM, false, false);
                    currentCoinsLabel.setText(Integer.toString(Parastrike.getPlayerStatsHandler().getCoins()));
                    createHoorayEffect(SFX.Menu.CANNON_PURCHASED);
                }

                private void purchaseSideKick() {
                    updateSideKickStats();
                }

                private void purchaseArmory() {
                    updateArmoryStats();
                }

                private void updateCannonStats() {
                    BulletType selectedCannon = (BulletType) ShopScreenImproved.this.selectedItem;
                    PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                    playerStatsHandler.enableCannon(selectedCannon);
                    playerStatsHandler.setCannonAmmo(selectedCannon, Rules.Cannons.INITIAL_AMMO);
                    playerStatsHandler.setSelectedCannon(selectedCannon);
                    playerStatsHandler.setCoins(playerStatsHandler.getCoins() - selectedItem.getCost());
                }

                private void updateSideKickStats() {
                    SideKickFactory.SideKickType selectedSideKick = (SideKickFactory.SideKickType) ShopScreenImproved.this.selectedItem;
                    PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                    playerStatsHandler.enableSideKick(selectedSideKick);
                    playerStatsHandler.setSelectedSideKick(selectedSideKick);
                    playerStatsHandler.setCoins(playerStatsHandler.getCoins() - selectedItem.getCost());
                    Parastrike.getMentorsManager().readyMentorIfDidntRun(Names.CHANGE_SIDE_KICK);
                }

                private void updateArmoryStats() {
                    Rules.Player.ArmoryItem selectedItem = (Rules.Player.ArmoryItem) ShopScreenImproved.this.selectedItem;
                    PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                    playerStatsHandler.addBomb(selectedItem);
                    playerStatsHandler.setCoins(playerStatsHandler.getCoins() - ShopScreenImproved.this.selectedItem.getCost());
                }
            };
        }

        private void createMiddleStack() {
            createUpgradersTable();
            createPurchaseTable();
            Stack middleStack = new Stack(upgradersTable, purchaseTable);
            middleStackCell = table.add(middleStack);
            table.row();
        }

        private void createIllustration() {
            if (selectedCategory == Category.CANNONS) {
                Image illustration = new Image(getSkin().getDrawable(((BulletType) selectedItem).getIllustration()));
                illustration.setScaling(Scaling.none);
                IllustrationCell = table.add(illustration).padBottom(Rules.Menu.ShopScreenImproved.Monitor.ILLUSTRATION_PADDING_BOTTOM);
                table.row();
            }
        }

        private void createUpgradersTable() {
            Skin skin = getSkin();
            upgradersTable = new Table(skin);
            upgradersTable.setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
            upgradersTable.padTop(Rules.Menu.ShopScreenImproved.Monitor.UPGRADERS_TABLE_PADDING_TOP);
            createUpgraders();
        }

        private void createUpgraders() {
            upgraderTop = new Upgrader();
            upgraderCenter = new Upgrader();
            upgraderBottom = new Upgrader();
            addUpgraders();
        }

        private void addUpgraders() {
            upgradersTable.addActor(upgraderTop);
            upgradersTable.addActor(upgraderCenter);
            upgradersTable.addActor(upgraderBottom);
        }

        private void defineTable() {
            table = (Table) getChildren().get(0);
            table.center();
        }

        private void createDescription() {
            description = new Label("", getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL, Label.LabelStyle.class));
            description.setWrap(true);
            description.setAlignment(Align.center);
            table.add(description).width(Rules.Menu.ShopScreenImproved.Monitor.MAIN_CONTENT_TEXT_WIDTH);
        }

        private void createHeader() {
            header = new Label("", getSkin().get(Rules.System.FontsParameters.ArmyFontNames.HUGE, Label.LabelStyle.class));
            header.setAlignment(Align.center);
            table.add(header).top().row();
        }

        public Table getUpgradersTable() {
            return upgradersTable;
        }

        private void setScrollPaneAttributes() {
            setDebug(GameSettings.SHOW_TABLES_LINES, GameSettings.SHOW_TABLES_LINES);
            setScrollbarsOnTop(true);
            setOverscroll(false, true);
            setScrollingDisabled(true, false);
        }

        public void setHeader(String header) {
            this.header.setText(header);
        }

        private void refresh() {
            setHeader(selectedItem.getName());
            boolean enabled = isSelectedItemEnabled();
            refreshParastrikeContent(enabled);
            middleStackCell.height(enabled ? Rules.Menu.ShopScreenImproved.Monitor.MIDDLE_STACK_SIZE_UPGRADERS : Rules.Menu.ShopScreenImproved.Monitor.MIDDLE_STACK_SIZE_PURCHASE);
            setIllustration();
        }

        private boolean isSelectedItemEnabled() {
            if (selectedCategory == Category.CANNONS) {
                return Parastrike.getPlayerStatsHandler().isCannonEnabled(selectedItem);
            } else if (selectedCategory == Category.SIDE_KICKS) {
                return Parastrike.getPlayerStatsHandler().isSideKickEnabled(selectedItem);
            }
            return (selectedItem.isUpgradeable());
        }

        private void setIllustration() {
            boolean isCannons = selectedCategory == Category.CANNONS;
            Image image = IllustrationCell.getActor();
            image.setVisible(isCannons);
            if (isCannons) {
                initializeIllustration();
            } else IllustrationCell.height(0);
        }

        private void initializeIllustration() {
            Drawable drawable = getSkin().getDrawable(((BulletType) selectedItem).getIllustration());
            IllustrationCell.getActor().setDrawable(drawable);
            IllustrationCell.height(drawable.getMinHeight());
        }

        private void refreshParastrikeContent(boolean enabled) {
            description.setText(selectedItem.getDescription());
            if (enabled) {
                refreshParastrikeContentForEnabled(selectedItem.getAttributes());
            } else refreshParastrikeContentForNotEnabled();
            setMiddleStackTablesVisibilities(enabled, !enabled);
        }

        private void setMiddleStackTablesVisibilities(boolean upgraders, boolean purchase) {
            mainContent.getUpgradersTable().setVisible(upgraders);
            mainContent.getPurchaseTable().setVisible(purchase);
        }

        private void refreshParastrikeContentForEnabled(Rules.Player.UpgradeableAttribute[] attributes) {
            if (!upgradersTable.isDescendantOf(upgradersTable)) {
                table.add(upgradersTable);
            }
            refreshUpgraders(attributes);
        }

        private void refreshParastrikeContentForNotEnabled() {
            purchasePrice.setText(String.format(ShopImproved.Monitor.PRICE, selectedItem.getCost()));
            if (selectedCategory == Category.CANNONS) {
                refreshParastrikeContentForNotEnabledCannon();
            } else if (selectedCategory == Category.ARMORY) {
                refreshParastrikeContentForBombs();
            } else purchaseAdditionalLabel.setVisible(false);
        }

        private void refreshParastrikeContentForBombs() {
            Rules.Player.ArmoryItem selectedBomb = (Rules.Player.ArmoryItem) ShopScreenImproved.this.selectedItem;
            initializePurchaseAdditionalLabel(true, String.format(ShopImproved.Monitor.YOU_HAVE, Parastrike.getPlayerStatsHandler().getBombAmount(selectedBomb), Rules.Player.Bonus.MAX_BOMBS_TO_HAVE), Rules.System.FontsParameters.RegularFontNames.SMALL);
        }

        private void refreshParastrikeContentForNotEnabledCannon() {
            int minGeneratorReq = ((BulletType) selectedItem).getMinGeneratorReq();
            initializePurchaseAdditionalLabel(minGeneratorReq > 0, String.format(ShopImproved.Monitor.UPGRADE_GEN, minGeneratorReq), Rules.System.FontsParameters.RegularFontNames.SMALL + ((minGeneratorReq > (Parastrike.getPlayerStatsHandler().getBunkerGeneratorLevel() / 10) + 1) ? Color.RED.toString() : ""));
        }

        private void initializePurchaseAdditionalLabel(boolean visible, String text, String font) {
            purchaseAdditionalLabel.setVisible(visible);
            purchaseAdditionalLabel.setStyle(getSkin().get(font, Label.LabelStyle.class));
            purchaseAdditionalLabel.setText(text);
        }

        private void refreshUpgraders(Rules.Player.UpgradeableAttribute[] attributes) {
            refreshUpgrader(attributes, 0, upgraderTop);
            refreshUpgrader(attributes, 1, upgraderCenter);
            refreshUpgrader(attributes, 2, upgraderBottom);
        }

        private void refreshUpgrader(Rules.Player.UpgradeableAttribute[] attributes, int i, Upgrader upgrader) {
            if (attributes.length - 1 < i) upgrader.setVisible(false);
            else {
                upgrader.setVisible(true);
                upgrader.setAttribute(attributes[i]);
            }
        }

        public Table getPurchaseTable() {
            return purchaseTable;
        }

        private class Upgrader extends WidgetGroup {

            private static final int UPGRADE_MAXED_VALUE = -2;
            private Cell<Image> iconCell;
            private Label nameLabel;
            private Label priceLabel;
            private int priceValue;
            private Label confirmationLabel;
            private Cell<ImageButton> infoButtonCell;
            private Cell<Table> labelsTableCell;
            private Cell<Stack> upgradeButtonCell;

            public Upgrader() {
                createWidgets();
                addWidgets();
            }

            private void createWidgets() {
                createInfoButton();
                createIcon();
                createLabels();
                createUpgradeButton();
            }

            private void createIcon() {
                Image icon = new Image(null, Scaling.none);
                iconCell = upgradersTable.add(icon);
            }

            private void createLabels() {
                Table labelsTable = new Table(getSkin());
                nameLabel = new Label(null, getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL, Label.LabelStyle.class));
                priceLabel = new Label(null, getSkin().get(Rules.System.FontsParameters.RegularFontNames.TINY, Label.LabelStyle.class));
                labelsTable.add(nameLabel).row();
                labelsTable.add(priceLabel);
                labelsTableCell = upgradersTable.add(labelsTable).pad(0, Rules.Menu.ShopScreenImproved.Monitor.UPGRADER_LABEL_PADDING_HORIZONTAL, 0, Rules.Menu.ShopScreenImproved.Monitor.UPGRADER_LABEL_PADDING_HORIZONTAL);
            }

            private void createUpgradeButton() {
                UpgradeButton upgradeButton = new LevelUpgradeButton(getSkin(), this);
                UpgradeButton secondaryUpgradeButton = new NonLevelUpgradeButton(getSkin(), this);
                secondaryUpgradeButton.setVisible(false);
                Stack stack = new Stack(upgradeButton, secondaryUpgradeButton);
                upgradeButtonCell = upgradersTable.add(stack).width(Rules.Menu.ShopScreenImproved.Monitor.UPGRADE_STACK_WIDTH);
                upgradersTable.row();
            }


            private void createInfoButton() {
                Drawable infoButtonDrawable = getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.INFO_BUTTON);
                ImageButton infoButton = new ImageButton(infoButtonDrawable, infoButtonDrawable);
                infoButton.addListener(defineInfoButtonClick());
                infoButtonCell = upgradersTable.add(infoButton).padRight(Rules.Menu.ShopScreenImproved.Monitor.INFO_BUTTON_PADDING_RIGHT);
            }

            private ButtonClickImproved defineInfoButtonClick() {
                return new ButtonClickImproved() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        createInfoMonitor();
                        updateInfoMonitor();
                    }

                    private void updateInfoMonitor() {
                        Rules.Player.UpgradeableAttribute attribute = ((UpgradeButton) upgradeButtonCell.getActor().getChildren().get(0)).getAttribute();
                        infoTextLabel.setText(attribute.getDescription());
                        infoMonitor.setHeader(attribute.getAttributeName());
                    }

                    private void createInfoMonitor() {
                        infoMonitor = createMonitor(Rules.Menu.ShopScreenImproved.InfoMonitor.WIDTH, Rules.Menu.ShopScreenImproved.InfoMonitor.HEIGHT, Rules.Menu.ShopScreenImproved.InfoMonitor.TARGET_Y, true, true, true, "", Rules.Menu.ShopScreenImproved.InfoMonitor.NAME);
                        infoMonitor.setBackButtonVisibility(false);
                        infoMonitor.setCloseButton(true);
                        if (infoMonitor.getContent() == null)
                            infoMonitor.setContent(createInfoMonitorContent());
                    }

                    private WidgetGroup createInfoMonitorContent() {
                        Skin skin = getSkin();
                        Table table = new Table(skin);
                        createInfoTextLabel();
                        table.add(infoTextLabel).width(Rules.Menu.ShopScreenImproved.InfoMonitor.WIDTH);
                        return table;
                    }

                    private void createInfoTextLabel() {
                        infoTextLabel = new Label("", getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL, Label.LabelStyle.class));
                        infoTextLabel.setWrap(true);
                        infoTextLabel.setAlignment(Align.center);
                    }
                };
            }

            private void addWidgets() {
                addConfirmation();
                upgradersTable.row();
            }

            private void addConfirmation() {
                confirmationLabel = new Label(null, getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL + Color.GREEN, Label.LabelStyle.class));
                confirmationLabel.setVisible(false);
                confirmationLabel.setWrap(true);
                confirmationLabel.setAlignment(Align.center);
                upgradersTable.add(confirmationLabel).colspan(4).width(upgradersTable.getWidth()).height(0).padBottom(Rules.Menu.ShopScreenImproved.Monitor.UPGRADER_PADDING_BOTTOM).padTop(Rules.Menu.ShopScreenImproved.Monitor.UPGRADER_CONFIRM_PADDING_TOP);
            }

            private final Timer.Task TASK_HIDE_CONFIRMATION = new Timer.Task() {
                @Override
                public void run() {
                    confirmationLabel.setVisible(false);
                }
            };


            @Override
            public void setVisible(boolean visible) {
                super.setVisible(visible);
                UpgradeButton upgradeButton = (UpgradeButton) upgradeButtonCell.getActor().getChildren().get(0);
                float height = visible ? upgradeButton.getMinHeight() : 0;
                setWidgetsVisibility(visible);
                setWidgetsHeight(height);
            }

            private void setWidgetsHeight(float height) {
                upgradeButtonCell.height(height);
                iconCell.height(height);
                labelsTableCell.height(height);
                infoButtonCell.height(height);
            }

            private void setWidgetsVisibility(boolean visible) {
                upgradeButtonCell.getActor().setVisible(visible);
                iconCell.getActor().setVisible(visible);
                labelsTableCell.getActor().setVisible(visible);
                infoButtonCell.getActor().setVisible(visible);
            }

            public void setAttribute(Rules.Player.UpgradeableAttribute attribute) {
                refreshLevelUpgrader(attribute);
                refreshNonLevelUpgrader(attribute);
                updatePriceLabel();
                upgradeButtonCell.getActor().getChildren().get(1).setVisible((attribute == Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO) && (priceValue != -1));
                iconCell.getActor().setDrawable(getSkin().getDrawable(attribute.getIcon()));
                nameLabel.setText(attribute.getAttributeName());
            }

            private void refreshNonLevelUpgrader(Rules.Player.UpgradeableAttribute attribute) {
                Actor nonLevelUpgrader = upgradeButtonCell.getActor().getChildren().get(1);
                ((NonLevelUpgradeButton) nonLevelUpgrader).setAttribute(attribute);
            }

            private void refreshLevelUpgrader(Rules.Player.UpgradeableAttribute attribute) {
                Actor levelUpgrader = upgradeButtonCell.getActor().getChildren().get(0);
                levelUpgrader.setVisible((!(attribute == Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO)) && (priceValue != -1));
                ((LevelUpgradeButton) levelUpgrader).setAttribute(attribute);
            }

            private void updatePriceLabel() {
                UpgradeButton upgradeButton = (UpgradeButton) upgradeButtonCell.getActor().getChildren().get(0);
                Integer attributeValue = upgradeButton.getAttributeValue();
                if (attributeValue < upgradeButton.getAttribute().getMaxValue()) {
                    priceValue = (upgradeButton.getAttribute().isCostConstant()) ? ((BulletType) selectedItem).getAmmoCost() : selectedItem.getUpgradeBasicCost() * attributeValue;
                    priceLabel.setText((priceValue == -1) ? ShopImproved.Monitor.UNLIMITED : String.format(ShopImproved.Monitor.PRICE, priceValue));
                } else setPriceForMaxed();
            }

            private void setPriceForMaxed() {
                priceLabel.setText(ShopImproved.Monitor.MAXED);
                priceValue = UPGRADE_MAXED_VALUE;
            }

            public int getPrice() {
                return priceValue;
            }

            private class NonLevelUpgradeButton extends UpgradeButton {

                private final Label label;

                public NonLevelUpgradeButton(Skin skin, Upgrader upgrader) {
                    super(skin, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.PLUS_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.PLUS_BUTTON_PRESSED), upgrader);
                    label = new Label(String.format(ShopImproved.Monitor.YOU_HAVE, attributeValue, 0), skin.get(Rules.System.FontsParameters.RegularFontNames.SMALL, Label.LabelStyle.class));
                    add(label).padLeft(Rules.Menu.ShopScreenImproved.Monitor.YOU_HAVE_PADDING_LEFT);
                }

                @Override
                protected String getUpgradedSuccesfullMessage() {
                    return ShopImproved.Monitor.AMMO_PURCHASED;
                }

                @Override
                public void setAttribute(Rules.Player.UpgradeableAttribute attribute) {
                    super.setAttribute(attribute);
                    label.setText(String.format(ShopImproved.Monitor.YOU_HAVE, attributeValue, getAttribute().getMaxValue()));
                    label.setStyle(attributeValue == 0 ? getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL + Color.RED, Label.LabelStyle.class) : getSkin().get(Rules.System.FontsParameters.RegularFontNames.SMALL, Label.LabelStyle.class));
                }
            }

            private class LevelUpgradeButton extends UpgradeButton {
                private Label levelLabel;

                public LevelUpgradeButton(Skin skin, Upgrader upgrader) {
                    super(skin, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.UPGRADE_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.UPGRADE_BUTTON_PRESSED), upgrader);
                    createLevelLabel();
                }

                @Override
                public void draw(Batch batch, float parentAlpha) {
                    super.draw(batch, parentAlpha);
                    if (!(getAttribute() == Rules.Player.UpgradeableAttribute.WeaponAttribute.AMMO)) {
                        TextureRegion fullRegion = getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.CELL_FULL);
                        TextureRegion emptyRegion = getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.CELL_EMPTY);
                        drawCells(batch, fullRegion, emptyRegion);
                    }
                }

                private void drawCells(Batch batch, TextureRegion fullRegion, TextureRegion emptyRegion) {
                    TextureRegion region;
                    for (int i = 0; i < 10; i++) {
                        region = (i <= (attributeValue - 1) % 10) ? fullRegion : emptyRegion;
                        float offsetY = (region == fullRegion) ? Rules.Menu.ShopScreenImproved.Monitor.UPGRADE_BUTTON_FULL_CELL_RELATIVE_Y : Rules.Menu.ShopScreenImproved.Monitor.UPGRADE_BUTTON_EMPTY_CELL_RELATIVE_Y;
                        batch.draw(region, getX() + Rules.Menu.ShopScreenImproved.Monitor.UPGRADE_BUTTON_CELL_RELATIVE_X + i * (fullRegion.getRegionWidth() - 3), getY() + offsetY);
                    }
                }

                @Override
                protected void setCannonAttribute() {
                    super.setCannonAttribute();
                    levelLabel.setText(Integer.toString((attributeValue - 1) / 10 + 1));
                }

                @Override
                protected void setSideKickAttribute() {
                    super.setSideKickAttribute();
                    levelLabel.setText(Integer.toString((attributeValue - 1) / 10 + 1));
                }

                @Override
                protected void setArmoryAttribute() {
                    super.setArmoryAttribute();
                    levelLabel.setText(Integer.toString((attributeValue - 1) / 10 + 1));
                }

                private void createLevelLabel() {
                    levelLabel = new Label("", getSkin(), Rules.System.FontsParameters.DigitalFontNames.TINY);
                    levelLabel.setAlignment(Align.center);
                    addActor(levelLabel);
                    levelLabel.setPosition(Rules.Menu.ShopScreenImproved.Monitor.UPGRADE_BUTTON_LABEL_RELATIVE_X, Rules.Menu.ShopScreenImproved.Monitor.UPGRADE_BUTTON_LABEL_RELATIVE_Y);
                }

            }

            private abstract class UpgradeButton extends ImageButton {
                protected final Upgrader upgrader;
                private Rules.Player.UpgradeableAttribute attributeType;
                protected int attributeValue;

                public UpgradeButton(Skin skin, Drawable upgradeDrawable, Drawable downDrawable, Upgrader upgrader) {
                    super(upgradeDrawable, downDrawable);
                    Image image = getImage();
                    image.setScaling(Scaling.none);
                    image.addListener(defineUpgradeClick());
                    setSkin(skin);
                    this.upgrader = upgrader;
                }

                private ButtonClickImproved defineUpgradeClick() {
                    return new ButtonClickImproved() {
                        @Override
                        public void clicked(InputEvent event, float x, float y) {
                            super.clicked(event, x, y);
                            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                            if (playerStatsHandler.getCoins() >= getPrice()) {
                                purchase();
                            } else showError(ShopImproved.Monitor.NOT_ENOUGH);
                        }

                        private void purchase() {
                            if (updateSelectedItem()) {
                                purchasedSuccess();
                            } else {
                                showError(ShopImproved.Monitor.MAX_REACHED);
                            }
                        }

                        private void purchasedSuccess() {
                            Parastrike.getSoundPlayer().playSound(SFX.Menu.SYSTEM_CONFIRM, false, false);
                            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                            playerStatsHandler.setCoins(playerStatsHandler.getCoins() - priceValue, true);
                            refreshUi();
                        }

                        private void refreshUi() {
                            currentCoinsLabel.setText(Integer.toString(Parastrike.getPlayerStatsHandler().getCoins()));
                            Upgrader.this.setAttribute(attributeType);
                            confirmationLabel.setText(String.format(getUpgradedSuccesfullMessage(), attributeType.getAttributeName()));
                            confirmationLabel.setVisible(true);
                            if (!TASK_HIDE_CONFIRMATION.isScheduled())
                                timer.scheduleTask(TASK_HIDE_CONFIRMATION, Rules.Menu.ShopScreenImproved.Monitor.UPGRADER_CONFIRM_DURATION);
                        }

                        private boolean updateSelectedItem() {
                            if (attributeValue >= attributeType.getMaxValue()) return false;
                            switch (selectedCategory) {
                                case CANNONS:
                                    upgradeCannon();
                                    break;
                                case SIDE_KICKS:
                                    upgradeSideKick();
                                    break;
                                case ARMORY:
                                    upgradeArmory();
                                    break;
                            }
                            return true;
                        }

                        private void upgradeCannon() {
                            attributeValue = attributeValue + 1;
                            BulletType selectedCannon = (BulletType) ShopScreenImproved.this.selectedItem;
                            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                            playerStatsHandler.setCannonAttribute(selectedCannon, attributeType, attributeValue, true);
                        }

                        private void upgradeSideKick() {
                            attributeValue = attributeValue + 1;
                            SideKickFactory.SideKickType selectedSideKick = (SideKickFactory.SideKickType) ShopScreenImproved.this.selectedItem;
                            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                            playerStatsHandler.setSideKickAttribute(selectedSideKick, attributeType, attributeValue, true);
                        }

                        private void upgradeArmory() {
                            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                            if (selectedItem == Rules.Player.ArmoryItem.BUNKER) {
                                attributeValue = attributeValue + 1;
                                playerStatsHandler.setBunkerAttribute(attributeType, attributeValue, true);
                            } else {
                                playerStatsHandler.addBomb((Rules.Player.ArmoryItem) attributeType, true);
                            }
                        }
                    };
                }

                protected String getUpgradedSuccesfullMessage() {
                    return ShopImproved.Monitor.UPGRADED_SUCCESSFUL;
                }


                public void setAttribute(Rules.Player.UpgradeableAttribute attribute) {
                    this.attributeType = attribute;
                    if (selectedCategory == Category.CANNONS) {
                        setCannonAttribute();
                    } else if (selectedCategory == Category.SIDE_KICKS) {
                        setSideKickAttribute();
                    } else setArmoryAttribute();
                }

                protected void setCannonAttribute() {
                    upgrader.setVisible(true);
                    attributeValue = Parastrike.getPlayerStatsHandler().getCannonAttribute((BulletType) selectedItem, (Rules.Player.UpgradeableAttribute.WeaponAttribute) attributeType);
                }

                protected void setSideKickAttribute() {
                    SideKickFactory.SideKickType selectedItem = (SideKickFactory.SideKickType) ShopScreenImproved.this.selectedItem;
                    upgrader.setVisible(selectedItem.getDisabledAttribute() != attributeType);
                    setVisible(selectedItem.getDisabledAttribute() != attributeType);
                    attributeValue = Parastrike.getPlayerStatsHandler().getSideKickAttribute(selectedItem, attributeType);
                }

                protected void setArmoryAttribute() {
                    Rules.Player.ArmoryItem selectedItem = (Rules.Player.ArmoryItem) ShopScreenImproved.this.selectedItem;
                    upgrader.setVisible(true);
                    if (selectedItem == Rules.Player.ArmoryItem.BUNKER) {
                        attributeValue = Parastrike.getPlayerStatsHandler().getBunkerAttribute(attributeType);
                    } else
                        attributeValue = Parastrike.getPlayerStatsHandler().getBombAmount(selectedItem);
                }

                public Rules.Player.UpgradeableAttribute getAttribute() {
                    return attributeType;
                }

                public Integer getAttributeValue() {
                    return attributeValue;
                }
            }
        }
    }

    private void showError(String message) {
        Parastrike.getSoundPlayer().playSound(SFX.Menu.SYSTEM_ERROR, false, false);
        mainContent.addAction(Actions.fadeOut(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
        initializeError(message);
        if (!TASK_SHOW_MAIN_CONTENT.isScheduled())
            timer.scheduleTask(TASK_SHOW_MAIN_CONTENT, Rules.Menu.ShopScreenImproved.Monitor.ERROR_DURATION);
    }

    private void initializeError(String message) {
        errorContent.addAction(Actions.fadeIn(Rules.Menu.ShopScreenImproved.Monitor.ITEM_FADE_DURATION, Interpolation.smooth));
        errorContent.setLock(true);
        errorContent.setVisible(true);
        errorContent.setMessage(message);
        if (!errorContent.TASK_UNLOCK.isScheduled())
            timer.scheduleTask(errorContent.TASK_UNLOCK, Rules.Menu.ShopScreenImproved.Monitor.ERROR_LOCK_DURATION);
    }


    private class ErrorContent extends Table {

        private Label label;
        private boolean locked;

        public ErrorContent(Skin skin) {
            super(skin);
            createErrorIcon();
            createLabel();
            setVisible(false);
        }

        private void createLabel() {
            label = new Label("", getSkin().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM + Color.RED, Label.LabelStyle.class));
            label.setWrap(true);
            label.setAlignment(Align.center);
            add(label).width(Rules.Menu.ShopScreenImproved.Monitor.MAIN_CONTENT_TEXT_WIDTH).padTop(Rules.Menu.ShopScreenImproved.Monitor.ERROR_MESSAGE_PADDING_TOP);
        }

        @Override
        protected void setParent(Group parent) {
            super.setParent(parent);
            parent.addListener(defineTouch());
        }

        private ClickListener defineTouch() {
            return new ClickListener() {
                @Override
                public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                    if (locked) return false;
                    if (TASK_SHOW_MAIN_CONTENT.isScheduled()) {
                        TASK_SHOW_MAIN_CONTENT.cancel();
                        TASK_SHOW_MAIN_CONTENT.run();
                    }
                    return super.touchDown(event, x, y, pointer, button);
                }
            };
        }

        public final Timer.Task TASK_UNLOCK = new Timer.Task() {
            @Override
            public void run() {
                setLock(false);
            }
        };

        private void createErrorIcon() {
            Image errorIcon = new Image(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.ICON_ERROR));
            errorIcon.setScaling(Scaling.none);
            add(errorIcon).row();
        }

        public void setMessage(String message) {
            label.setText(message);
        }

        public void hide() {
            if (isVisible()) {
                TASK_SHOW_MAIN_CONTENT.cancel();
                TASK_SHOW_MAIN_CONTENT.run();
            }
        }

        public void setLock(boolean b) {
            locked = b;
        }
    }

    private class CategoryItems extends Table {
        private final Category category;
        private final ButtonGroup<CategoryItemButton> buttonGroup;

        public CategoryItems(Skin skin, Category category) {
            super(skin);
            this.category = category;
            buttonGroup = createItemsGroup();
            Label.LabelStyle style = getSkin().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, Label.LabelStyle.class);
            for (WeaponType item : category.getItems())
                createCategoryItem(buttonGroup, item, style);
            initializeCategoryItems(buttonGroup);
            right().top();
        }

        private void initializeCategoryItems(ButtonGroup<CategoryItemButton> buttonGroup) {
            buttonGroup.getButtons().get(0).setChecked(true);
        }

        private void createCategoryItem(ButtonGroup<CategoryItemButton> buttonGroup, WeaponType item, Label.LabelStyle style) {
            CategoryItemButton categoryItem = new CategoryItemButton(item, style, category);
            add(categoryItem).padBottom(Rules.Menu.ShopScreenImproved.Monitor.ITEM_PADDING_BOTTOM).center().row();
            buttonGroup.add(categoryItem);
        }


        public void refresh() {
            for (CategoryItemButton button : buttonGroup.getButtons()) {
                if (button.getItem() == selectedItem) {
                    button.setChecked(true);
                    ((ScrollPane) getParent()).scrollTo(button.getX(), button.getY(), button.getWidth(), button.getHeight(), false, true);
                }
            }
        }
    }
}
