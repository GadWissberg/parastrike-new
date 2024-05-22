package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.object_factories.BonusFactory;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 21/10/2016.
 */

public class HelpScreen extends MenuScreenImproved {
    private final MonitorImproved monitor;
    private WidgetGroup[] layouts = new WidgetGroup[Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS];
    private int selectedIndex;
    private Button leftArrow, rightArrow;
    private Cell<WidgetGroup> middleCell;

    public HelpScreen() {
        super(true, Assets.GFX.Sheets.Menus.HELP_DATA_FILE, Assets.GFX.Sheets.Menus.HELP_2_DATA_FILE, Assets.GFX.Sheets.General.BOTTOM_LOADING_SCREEN_DATA_FILE, Assets.GFX.Sheets.General.Bonuses.DATA_FILE, Assets.GFX.Sheets.General.Bonuses.DATA_FILE_2);
        setBackScreen(Parastrike.MenuType.WELCOME);
        monitor = createMonitor(createParastrikeContent(), Rules.Menu.Help.Monitor.MONITOR_WIDTH, Rules.Menu.Help.Monitor.MONITOR_HEIGHT, Rules.Menu.Help.Monitor.MONITOR_Y, false, true, false, Assets.Strings.Menu.Help.HEADER_HOW_TO_PLAY);
        createTutorialButton();
        refresh();
    }

    private void createTutorialButton() {
        Skin skin = getSkin();
        ImageButton tutorialButton = new ImageButton(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.TUTORIAL_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.TUTORIAL_BUTTON_PRESSED));
        tutorialButton.getImage().setScaling(Scaling.none);
        Cell cell = monitor.setTopRightWidget(tutorialButton);
        cell.padRight(Rules.Menu.Help.Monitor.TUTORIAL_BUTTON_PADDING_RIGHT);
        tutorialButton.addListener(defineTutorialButton());
    }

    private ButtonClickImproved defineTutorialButton() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                closeDoorsAndGoToScreen(new Timer.Task() {
                    @Override
                    public void run() {
                        IntroScreen.initializeTutorial();
                    }
                }, new Timer.Task() {
                    @Override
                    public void run() {
                        Parastrike.getInstance().goToBattle();
                    }
                });
            }
        };
    }

    private void refresh() {
        updateArrows();
        updateMiddleCell();
        refreshHeader();
    }

    private void refreshHeader() {
        String header;
        if (selectedIndex == 0) header = Assets.Strings.Menu.Help.HEADER_CREDITS;
        else {
            if (selectedIndex == Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS - 1)
                header = Assets.Strings.Menu.Help.HEADER_BONUSES;
            else header = Assets.Strings.Menu.Help.HEADER_HOW_TO_PLAY;
        }
        monitor.setHeader(header);
    }

    private void updateMiddleCell() {
        middleCell.getActor().remove();
        WidgetGroup layout = layouts[selectedIndex];
        middleCell.setActor(layout);
    }

    private void updateArrows() {
        leftArrow.setVisible(!(selectedIndex == 0));
        rightArrow.setVisible(!(selectedIndex == Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS - 1));
    }

    private Table createParastrikeContent() {
        createLayouts();
        Table table = createParastrikeContentTable();
        createLeftArrow(table);
        middleCell = table.add(layouts[0]).expand();
        createRightArrow(table);
        table.row();
        return table;
    }

    private Table createParastrikeContentTable() {
        Table table = new Table();
        table.setDebug(GameSettings.SHOW_TABLES_LINES);
        return table;
    }

    private void createLayouts() {
        layouts[0] = createCreditsLayout();
        for (int i = 1; i < Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS - 1; i++) {
            createHowToPlayLayouts(i);
        }
        layouts[Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS - 1] = createBonusesLayout();
    }

    private void createHowToPlayLayouts(int i) {
        Skin skin = getSkin();
        Table table = new Table(skin);
        Image image = new Image(skin.getRegion(Assets.GFX.Sheets.ImagesNames.HELP + i));
        table.add(image).bottom().expand();
        layouts[i] = table;
    }

    private ScrollPane createBonusesLayout() {
        Table table = createLayoutTable();
        ScrollPane scrollPane = createLayoutScrollPane(table);
        createBonusesRows(table);
        return scrollPane;
    }

    private void createBonusesRows(Table table) {
        Label.LabelStyle style = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GOLD);
        for (BonusFactory.BonusType bonus : BonusFactory.BonusType.values()) {
            createBonusRow(table, style, bonus);
        }
    }

    private void createBonusRow(Table table, Label.LabelStyle style, BonusFactory.BonusType bonus) {
        Image image = new Image(getSkin(), bonus.getRegionName());
        table.add(image).width(image.getWidth() / 3).height(image.getHeight() / 3).padRight(Rules.Menu.Help.Monitor.Bonus.IMAGE_PADDING_RIGHT).padBottom(Rules.Menu.Help.Monitor.Bonus.ROW_PADDING_BOTTOM).padLeft(Rules.Menu.Help.Monitor.Bonus.ROW_PADDING_LEFT);
        Label description = new Label(bonus.getDescription(), style);
        description.setAlignment(Align.left);
        description.setWrap(true);
        description.setWidth(Rules.Menu.Help.Monitor.Bonus.DESCRIPTION_WIDTH);
        table.add(description).left().width(Rules.Menu.Help.Monitor.Bonus.DESCRIPTION_WIDTH).padBottom(Rules.Menu.Help.Monitor.Bonus.ROW_PADDING_BOTTOM);
        table.row();
    }

    private ScrollPane createCreditsLayout() {
        Table table = createLayoutTable();
        ScrollPane scrollPane = createLayoutScrollPane(table);
        createCreditsLayoutContent(table);
        return scrollPane;
    }

    private void createCreditsLayoutContent(Table table) {
        Label.LabelStyle style = setCreditsLayoutTextStyle();
        createCreditsLayoutText(table, style, Assets.Strings.Menu.Help.LEFT_SIDE_CREDITS, Rules.Menu.Help.Monitor.LEFT_SIDE_CREDITS_WIDTH, Rules.Menu.Help.Monitor.ADDITIONAL_CREDITS_PADDING_TOP);
        addCreditsProfileImage(table);
        createAdditionalCreditsText(table, style);
        createLibGDX(table, style);
        createAboutMe(table, style);
    }

    private void createLibGDX(Table table, Label.LabelStyle style) {
        Image image = new Image(getSkin(), Assets.GFX.Sheets.ImagesNames.LIBGDX);
        image.setScaling(Scaling.none);
        table.add(image).left().colspan(2).padTop(Rules.Menu.Help.Monitor.LIBGDX_IMAGE_PADDING_TOP);
        table.row();
        createCreditsLayoutText(table, style, Assets.Strings.Menu.Help.LIBGDX, Rules.Menu.Help.Monitor.ADDITIONAL_CREDITS_WIDTH, 0).colspan(2).padBottom(Rules.Menu.Help.Monitor.LIBGDX_TEXT_PADDING_BOTTOM);
        table.row();
    }

    private void createAboutMe(Table table, Label.LabelStyle style) {
        table.add(new Label(Assets.Strings.Menu.Help.ABOUT_ME_HEADER, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.GOLD))).colspan(2).padTop(Rules.Menu.Help.Monitor.ABOUT_ME_HEADER_PADDING_TOP).left();
        table.row();
        createCreditsLayoutText(table, style, Assets.Strings.Menu.Help.ABOUT_ME, Rules.Menu.Help.Monitor.ADDITIONAL_CREDITS_WIDTH, Rules.Menu.Help.Monitor.ADDITIONAL_CREDITS_PADDING_TOP).colspan(2);
        table.row();
        createLogoButton(getSkin(), table, style);
    }

    private void createAdditionalCreditsText(Table table, Label.LabelStyle style) {
        Cell<Label> cell = createCreditsLayoutText(table, style, Assets.Strings.Menu.Help.ADDITIONAL_CREDITS, Rules.Menu.Help.Monitor.ADDITIONAL_CREDITS_WIDTH, Rules.Menu.Help.Monitor.ADDITIONAL_CREDITS_PADDING_TOP);
        cell.colspan(2);
        table.row();
    }

    private void createLogoButton(Skin skin, Table table, Label.LabelStyle style) {
        Button logo = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.GADARTS));
        logo.addListener(defineLogoClick());
        table.add(logo).padTop(Rules.Menu.Help.Monitor.GADARTS_PADDING_TOP);
        table.row();
        Label mail = new Label(Assets.Strings.Menu.Help.TAP_TO_MAIL, style);
        table.add(mail);
    }

    private void addCreditsProfileImage(Table table) {
        Image image = new Image(getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.PROFILE));
        image.setScaling(Scaling.none);
        table.add(image).padRight(Rules.Menu.Help.Monitor.PROFILE_IMAGE_PADDING_RIGHT);
        table.row();
    }

    private Cell<Label> createCreditsLayoutText(Table table, Label.LabelStyle style, String text, float width, float paddingTop) {
        Label creditsText = new Label(text, style);
        creditsText.setWrap(true);
        Cell<Label> cell = table.add(creditsText).width(width).left().top().padTop(paddingTop);
        creditsText.setWidth(width);
        return cell;
    }

    private Label.LabelStyle setCreditsLayoutTextStyle() {
        BitmapFont font = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class);
        return new Label.LabelStyle(font, Color.GOLD);
    }

    private Table createLayoutTable() {
        Table table = new Table(getSkin());
        table.setDebug(GameSettings.SHOW_TABLES_LINES);
        return table;
    }

    private ButtonClickImproved defineLogoClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Parastrike.getInstance().actionResolver.mail();
            }
        };
    }

    private void createLeftArrow(Table table) {
        Skin skin = getSkin();
        leftArrow = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LEFT_ARROW), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.LEFT_ARROW_PRESSED));
        leftArrow.addListener(defineLeftArrowClick());
        table.add(leftArrow).left();
    }

    private void createRightArrow(Table table) {
        Skin skin = getSkin();
        rightArrow = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.RIGHT_ARROW), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.RIGHT_ARROW_PRESSED));
        rightArrow.addListener(defineRightArrowClick());
        table.add(rightArrow).right();
    }

    private ButtonClickImproved defineLeftArrowClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedIndex = (selectedIndex > 0) ? selectedIndex - 1 : Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS - 1;
                refresh();
            }
        };
    }

    private ButtonClickImproved defineRightArrowClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                selectedIndex = (selectedIndex < Rules.Menu.Help.Monitor.NUMBER_OF_LAYOUTS - 1) ? selectedIndex + 1 : 0;
                refresh();
            }
        };
    }

    @Override
    protected void onBackPressed() {
        Parastrike.getInstance().goToMenuScreen(getBackScreen());
    }

}
