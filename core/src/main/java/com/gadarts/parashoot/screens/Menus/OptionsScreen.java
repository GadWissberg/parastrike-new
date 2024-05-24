package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.ButtonGroup;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 21/10/2016.
 */

public class OptionsScreen extends MenuScreenImproved {


    public OptionsScreen() {
        super(true, Assets.GFX.Sheets.Menus.OPTIONS_BUTTONS_DATA_FILE);
        setBackScreen(Parastrike.MenuType.WELCOME);
        createMonitor(createParastrikeContent(), Rules.Menu.Options.Monitor.MONITOR_WIDTH, Rules.Menu.Options.Monitor.MONITOR_HEIGHT, Rules.Menu.Help.Monitor.MONITOR_Y, true, true, false, Assets.Strings.Menu.Options.HEADER_OPTIONS);
    }

    private ScrollPane createParastrikeContent() {
        Label.LabelStyle style = new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GOLD);
        Table optionsTable = createOptionsTable();
        ScrollPane scrollPane = createScrollPane(optionsTable);
        createOptions(optionsTable, style);
        return scrollPane;
    }

    private ScrollPane createScrollPane(Table optionsTable) {
        ScrollPane scroller = new ScrollPane(optionsTable, getScrollPaneStyle());
        scroller.setDebug(GameSettings.SHOW_TABLES_LINES);
        scroller.setFadeScrollBars(false);
        return scroller;
    }

    private Table createOptionsTable() {
        Table optionsTable = new Table(getSkin());
        optionsTable.setDebug(GameSettings.SHOW_TABLES_LINES);
        optionsTable.pad(Rules.Menu.Options.Monitor.PADDING);
        return optionsTable;
    }

    private void createHeader(Label.LabelStyle style, Table optionsTable, String text) {
        Label header = new Label(text, style);
        header.setAlignment(Align.center);
        optionsTable.add(header).colspan(2).padTop(Rules.Menu.Options.Monitor.HEADER_TOP_PADDING);
        style.font.getData().setScale(1);
        optionsTable.row();
    }

    private void createOptions(Table optionsTable, Label.LabelStyle style) {
        createInGameOptions(optionsTable, style);
        createGeneralOptions(optionsTable, style);
    }

    private void createGeneralOptions(Table optionsTable, Label.LabelStyle style) {
        createHeader(new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.GOLD), optionsTable, Assets.Strings.Menu.Options.GENERAL);
        createOptionForRestart(optionsTable, style);
    }

    private void createInGameOptions(Table optionsTable, Label.LabelStyle style) {
        createHeader(new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.GOLD), optionsTable, Assets.Strings.Menu.Options.IN_GAME);
        createOptionForMessages(optionsTable, style);
        createOptionForVibration(optionsTable, style);
        createControllerOption(style, optionsTable);
        createOptionForEpilepsy(optionsTable, style);
    }

    private void createOptionForEpilepsy(Table optionsTable, Label.LabelStyle style) {
        Button button = new Button(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_EPILEPSY), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_EPILEPSY_PRESSED), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_EPILEPSY_CHECKED));
        button.addListener(defineToggleOptionClick(Assets.Configs.Preferences.Settings.EPILEPSY, true));
        button.setChecked(Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.EPILEPSY, true));
        createOption(style, button, Assets.Strings.Menu.Options.EPILEPSY, optionsTable);
    }

    private void createOptionForVibration(Table optionsTable, Label.LabelStyle style) {
        final Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS);
        final Button vibrationButton = new Button(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_VIBRATION), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_VIBRATION_PRESSED), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_VIBRATION_CHECKED));
        vibrationButton.addListener(defineToggleOptionClick(Assets.Configs.Preferences.Settings.VIBRATION, true));
        vibrationButton.setChecked(preferences.getBoolean(Assets.Configs.Preferences.Settings.VIBRATION, true));
        createOption(style, vibrationButton, Assets.Strings.Menu.Options.VIBRATION, optionsTable);
    }

    private void createOptionForRestart(Table optionsTable, Label.LabelStyle style) {
        final Button restartButton = new Button(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_RESTART), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_RESTART_PRESSED));
        restartButton.addListener(defineRestartClick());
        createOption(style, restartButton, Assets.Strings.Menu.Options.RESTART, optionsTable);
    }

    private ButtonClickImproved defineRestartClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                String monitorName = Rules.Menu.Options.Monitor.DELETE_CONFIRMATION_MONITOR_NAME;
                WidgetGroup table = createConfirmationTable(Assets.Strings.Menu.Options.RESTART_CONFIRMATION, OptionsScreen.this.createConfirmationNoClick(monitorName), createDeleteConfirmationClick(monitorName));
                createMonitor(table, Rules.Menu.Options.Monitor.CONFIRMATION_MONITOR_WIDTH, Rules.Menu.Options.Monitor.CONFIRMATION_MONITOR_HEIGHT, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2, true, true, true, null, monitorName);
            }

            private ButtonClickImproved createDeleteConfirmationClick(final String name) {
                return new ButtonClickImproved() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        super.clicked(event, x, y);
                        Parastrike.getPlayerStatsHandler().reset();
                        getMonitors().get(name).setActivation(false);
                    }
                };
            }
        };
    }

    private WidgetGroup createConfirmationTable(String label, ButtonClickImproved clickNo, ButtonClickImproved clickYes) {
        final Table table = new Table();
        createConfirmationLabel(table, label);
        Skin skin = getSkin();
        createConfirmationButtonNo(table, skin, clickNo);
        createConfirmationButtonYes(table, skin, clickYes);
        return table;
    }

    private void createConfirmationButtonYes(final Table table, Skin skin, ButtonClickImproved yesClick) {
        Button yesButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK_PRESSED));
        yesButton.addListener(yesClick);
        table.add(yesButton).space(Rules.Menu.Options.Monitor.BUTTONS_SPACING);
    }


    private void createConfirmationButtonNo(Table table, Skin skin, ButtonClickImproved click) {
        Button noButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BUTTON_CANCEL), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BUTTON_CANCEL_PRESSED));
        noButton.addListener(click);
        table.add(noButton).space(Rules.Menu.Options.Monitor.BUTTONS_SPACING);
    }

    private ButtonClickImproved createConfirmationNoClick(final String monitorName) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                getMonitors().get(monitorName).setActivation(false);
            }
        };
    }

    private void createConfirmationLabel(Table table, String restartConfirmation) {
        Label areYouSure = new Label(restartConfirmation, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class), Color.GOLD));
        areYouSure.setAlignment(Align.center);
        areYouSure.setWrap(true);
        table.add(areYouSure).colspan(2).width(Rules.Menu.Options.Monitor.CONFIRMATION_MONITOR_WIDTH);
        table.row();
    }

    private void createOptionForMessages(Table optionsTable, Label.LabelStyle style) {
        final Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS);
        final Button messagesButton = new Button(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_MESSAGES), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_MESSAGES_PRESSED), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_MESSAGES_CHECKED));
        messagesButton.addListener(defineToggleOptionClick(Assets.Configs.Preferences.Settings.MESSAGES, true));
        messagesButton.setChecked(preferences.getBoolean(Assets.Configs.Preferences.Settings.MESSAGES, true));
        createOption(style, messagesButton, Assets.Strings.Menu.Options.MESSAGES, optionsTable);
    }

    private ButtonClickImproved defineToggleOptionClick(final String key, final boolean defValue) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS);
                boolean newValue = !preferences.getBoolean(key, defValue);
                preferences.putBoolean(key, newValue);
                preferences.flush();
            }

        };
    }

    private void createControllerOption(Label.LabelStyle style, Table optionsTable) {
        createControllerLabel(style, optionsTable);
        Skin skin = getSkin();
        Table smallTable = new Table(skin).center();
        createButtonsForControllerOption(skin, smallTable);
        optionsTable.add(smallTable).colspan(2);
        optionsTable.row();
    }

    private void createButtonsForControllerOption(Skin skin, Table smallTable) {
        ButtonGroup buttonGroup = new ButtonGroup();
        Button swipeControllerButton = createSwipeControllerButton(skin, smallTable);
        Button touchControllerButton = createTouchControllerButton(skin, smallTable);
        swipeControllerButton.setChecked(Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.SWIPE, true));
        touchControllerButton.setChecked(Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.TOUCH, true));
        buttonGroup.setMaxCheckCount(-1);
        buttonGroup.setMinCheckCount(0);
        buttonGroup.add(swipeControllerButton);
        buttonGroup.add(touchControllerButton);
        buttonGroup.setMinCheckCount(1);
        buttonGroup.setUncheckLast(true);
    }

    private Button createTouchControllerButton(Skin skin, Table smallTable) {
        Button touchButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_TOUCH), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_TOUCH_PRESSED), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_TOUCH_CHECKED));
        touchButton.addListener(new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS);
                String key = Assets.Configs.Preferences.Settings.TOUCH;
                boolean newValue = !preferences.getBoolean(key, false);
                if (preferences.getBoolean(Assets.Configs.Preferences.Settings.SWIPE, false)) {
                    preferences.putBoolean(key, newValue);
                    preferences.flush();
                }
            }
        });
        smallTable.add(touchButton).center().padLeft(Rules.Menu.Options.Monitor.CONTROL_BUTTONS_SPACING / 2);
        return touchButton;
    }

    private Button createSwipeControllerButton(Skin skin, Table smallTable) {
        Button swipeButton = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_SWIPE), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_SWIPE_PRESSED), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.OPTIONS_SWIPE_CHECKED));
        swipeButton.addListener(new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS);
                String key = Assets.Configs.Preferences.Settings.SWIPE;
                boolean newValue = !preferences.getBoolean(key, false);
                if (preferences.getBoolean(Assets.Configs.Preferences.Settings.TOUCH, false)) {
                    preferences.putBoolean(key, newValue);
                    preferences.flush();
                }
            }
        });
        smallTable.add(swipeButton).center().padRight(Rules.Menu.Options.Monitor.CONTROL_BUTTONS_SPACING / 2);
        return swipeButton;
    }

    private void createControllerLabel(Label.LabelStyle style, Table optionsTable) {
        Label label = new Label(Assets.Strings.Menu.Options.CONTROL, style);
        label.setAlignment(Align.center);
        label.setWrap(true);
        optionsTable.add(label).center().width(optionsTable.getMinWidth()).center().colspan(2);
        optionsTable.row();
    }

    private void createOption(Label.LabelStyle style, Button button, String label, Table optionsTable) {
        createOptionButton(button, optionsTable);
        createOptionLabel(style, label, optionsTable);
        optionsTable.add();
        optionsTable.row();
    }

    private void createOptionButton(Button button, Table optionsTable) {
        optionsTable.add(button).center().top().padRight(Rules.Menu.Options.Monitor.BUTTON_RIGHT_PADDING);
    }

    private void createOptionLabel(Label.LabelStyle style, String label, Table optionsTable) {
        Label labelObject = new Label(label, style);
        labelObject.setWrap(true);
        labelObject.setAlignment(Align.center);
        optionsTable.add(labelObject).width(Rules.Menu.Options.Monitor.LABEL_WIDTH).center();
    }


}
