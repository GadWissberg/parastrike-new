package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;

import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_MAIN_HELP;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_MAIN_HELP_PRESSED;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_MAIN_OPTIONS;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_MAIN_OPTIONS_PRESSED;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_PLAY;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.BUTTON_PLAY_PRESSED;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.PRESSED;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.WELCOME_SOUND_OFF;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.WELCOME_SOUND_ON;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.BigButtons;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.BigButtons.MARGIN_HORIZONTAL;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.LogoEffect;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.MainMonitor;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.Version;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.MEDIUM;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.WIDTH_TARGET_RESOLUTION;

/**
 * Created by Gad on 21/10/2016.
 */

public class WelcomeScreen extends MenuScreenImproved {
    private Button muteButton;

    public WelcomeScreen() {
        super(true, Assets.GFX.Sheets.Menus.WELCOME_BUTTONS_DATA_FILE, Assets.GFX.Sheets.Menus.MAIN_MENU_DATA_FILE);
        createLogo();
        setNextScreen(Parastrike.MenuType.SCENE_SELECTION);
        playMusic();
        createMonitor(createParastrikeContent(), MainMonitor.MONITOR_WIDTH, MainMonitor.MONITOR_HEIGHT, MainMonitor.MONITOR_Y, true, true, false);
        createButtons();
        Label.LabelStyle style = new Label.LabelStyle(Parastrike.getAssetsManager().get(MEDIUM, BitmapFont.class), Color.GOLD);
        try {
            Label label = new Label(Parastrike.getInstance().actionResolver.getAppVersion(), style);
            label.setVisible(false);
            label.setPosition(Version.X, Version.Y);
            label.addAction(Actions.delay(Version.DELAY, Actions.show()));
            menuStage.addActor(label);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void createLogo() {
        Texture texture = Parastrike.getAssetsManager().get(Assets.GFX.Images.Menus.LOGO);
        Image logo = new Image(texture);
        getStage().addActor(logo);
        createLogoEffects(texture, logo);
    }

    private void createLogoEffects(Texture texture, Image logo) {
        int textureWidth = texture.getWidth();
        int middleScreen = WIDTH_TARGET_RESOLUTION / 2 - textureWidth / 2;
        logo.setPosition(middleScreen, HEIGHT_TARGET_RESOLUTION);
        int destY = HEIGHT_TARGET_RESOLUTION - texture.getHeight();
        MoveToAction moveTo = Actions.moveTo(middleScreen, destY, LogoEffect.MOVE_DURATION, Interpolation.bounce);
        logo.addAction(moveTo);
    }

    private Table createParastrikeContent() {
        Skin skin = getSkin();
        Table table = new Table(skin);
        createParastrikeMonitorButtons(table);
        table.setDebug(GameSettings.SHOW_TABLES_LINES);
        return table;
    }

    private void createParastrikeMonitorButtons(Table table) {
        createPlayButton(table);
        createHelpButton(table);
        createOptionsButton(table);
    }

    private void createOptionsButton(Table table) {
        Skin skin = getSkin();
        Button button = new Button(skin.getDrawable(BUTTON_MAIN_OPTIONS), skin.getDrawable(BUTTON_MAIN_OPTIONS_PRESSED));
        button.addListener(defineParastrikeMonitorButton(Parastrike.MenuType.OPTIONS));
        table.add(button);
        table.row();
    }

    private void createHelpButton(Table table) {
        Skin skin = getSkin();
        Button button = new Button(skin.getDrawable(BUTTON_MAIN_HELP), skin.getDrawable(BUTTON_MAIN_HELP_PRESSED));
        button.addListener(defineParastrikeMonitorButton(Parastrike.MenuType.HELP));
        table.add(button);
    }

    private void createPlayButton(Table table) {
        Skin skin = getSkin();
        Button playButton = new Button(skin.getDrawable(BUTTON_PLAY), skin.getDrawable(BUTTON_PLAY_PRESSED));
        playButton.addListener(defineParastrikeMonitorButton(getNextScreen()));
        table.add(playButton).colspan(2).padBottom(MainMonitor.PLAY_PADDING_BOTTOM);
        table.row();
    }

    private ButtonClickImproved defineParastrikeMonitorButton(final Parastrike.MenuType screen) {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Parastrike.getInstance().goToMenuScreen(screen);
            }
        };
    }

    private void createButtons() {
        createMuteButton();
    }

    private void playMusic() {
        if (!Parastrike.getSoundPlayer().isMusicPlaying()) {
            Parastrike.getSoundPlayer().playMusic(SFX.Music.MENU);
        }
    }

    private void createMuteButton() {
        String speakerImage = (GameSettings.SOUND_TOGGLE) ? WELCOME_SOUND_ON : WELCOME_SOUND_OFF;
        Skin skin = getSkin();
        Drawable drawable = skin.getDrawable(speakerImage);
        muteButton = new Button(drawable, skin.getDrawable(speakerImage + PRESSED));
        muteButton.setPosition(-MARGIN_HORIZONTAL, 0);
        createBigButtonEffect(MARGIN_HORIZONTAL, 0, muteButton);
        defineMuteButton();
        getStage().addActor(muteButton);
    }

    private void defineMuteButton() {
        muteButton.setPosition(MARGIN_HORIZONTAL, 0);
        muteButton.addListener(defineMuteButtonClick());
    }

    private void createBigButtonEffect(float x, float y, Button button) {
        MoveToAction effect = Actions.moveTo(x, y, BigButtons.MOVE_DURATION, Interpolation.circle);
        button.addAction(effect);
    }

    private ButtonClickImproved defineMuteButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Parastrike.getSoundPlayer().toggleSound(!GameSettings.SOUND_TOGGLE);
                updateMuteButtonImage();
                super.clicked(event, x, y);
            }
        };
    }

    private void updateMuteButtonImage() {
        String speakerImage = (GameSettings.SOUND_TOGGLE) ? WELCOME_SOUND_ON : WELCOME_SOUND_OFF;
        Skin skin = getSkin();
        muteButton.getStyle().up = skin.getDrawable(speakerImage);
        muteButton.getStyle().down = skin.getDrawable(speakerImage + PRESSED);
    }

    @Override
    protected void onBackPressed() {
        Gdx.app.exit();
    }


//
//    private class RateMonitor extends PopUpMonitor {
//        private ButtonMenu submitButton, rateButton;
//        private int rate;
//        private RatingStar[] stars;
//
//        public RateMonitor(float width, float height, boolean isBottom) {
//            super(width, height, isBottom);
//            createRatingSystem();
//        }
//
//        private void createRatingSystem() {
//            Drawable fullStar = skin.getDrawable(Assets.GFX.Sheets.ImagesNames.WELCOME_RATE_FULL_STAR);
//            Drawable blankStar = skin.getDrawable(Assets.GFX.Sheets.ImagesNames.WELCOME_RATE_BLANK_STAR);
//            createStars(fullStar, blankStar);
//            setRate(5);
//            createButtons();
//        }
//
//        private void createButtons() {
//            Drawable submitDrawable = skin.getDrawable(Assets.GFX.Sheets.ImagesNames.WELCOME_RATE_SUBMIT);
//            submitButton = new ButtonMenu(WelcomeScreen.this, submitDrawable, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.WELCOME_RATE_SUBMIT_PRESSED), this, getWidth() / 2 - submitDrawable.getMinWidth() / 2, Rules.Menu.WelcomeScreen.RateMonitor.SUBMIT_MARGIN_BOTTOM, defineSubmitButton());
//            Drawable rateDrawable = skin.getDrawable(Assets.GFX.Sheets.ImagesNames.WELCOME_RATE_GOOGLE);
//            rateButton = new ButtonMenu(WelcomeScreen.this, rateDrawable, skin.getDrawable(Assets.GFX.Sheets.ImagesNames.WELCOME_RATE_GOOGLE_PRESSED), this, getWidth() / 2 - rateDrawable.getMinWidth() / 2, Rules.Menu.WelcomeScreen.RateMonitor.SUBMIT_MARGIN_BOTTOM, defineRateButtonClick());
//            rateButton.setVisible(false);
//        }
//
//        private ButtonClick defineSubmitButton() {
//            return new ButtonClick() {
//                @Override
//                public void changed(ChangeEvent event, Actor actor) {
//                    super.changed(event, actor);
//                    reportRating();
//                    hideButtons();
//                    if (rate >= 4) {
//                        showPlayStoreLink();
//                    }
//                }
//
//                private void hideButtons() {
//                    setStarsVisibility(false);
//                    submitButton.setVisible(false);
//                }
//            };
//        }
//
//        private ButtonClick defineRateButtonClick() {
//            return new ButtonClick() {
//                @Override
//                public void changed(ChangeEvent event, Actor actor) {
//                    super.changed(event, actor);
//                    Parastrike.getGGS().rateGame();
//                }
//            };
//        }
//
//        private void showPlayStoreLink() {
//            rateButton.setVisible(true);
//        }
//
//        private void reportRating() {
//            Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Player.PREF_PLAYER);
//            HashMap<String, String> attributes = new HashMap<String, String>();
//            attributes.put(Rules.System.Analytics.Attributes.MenuScreen.NAME, preferences.getString(Assets.Configs.Preferences.Player.NAME));
//            attributes.put(Rules.System.Analytics.Attributes.MenuScreen.RATING, String.valueOf(rate));
//            Parastrike.getInstance().actionResolver.analyticsEventReport(Rules.System.Analytics.Events.USER_RATED, attributes);
//        }
//
//        @Override
//        public void act(float delta) {
//            super.act(delta);
//            submitButton.toFront();
//            rateButton.toFront();
//        }
//
//        @Override
//        public void draw(Batch batch, float parentAlpha) {
//            super.draw(batch, parentAlpha);
//            if (submitButton.isVisible()) {
//                drawText(batch, armyFont, getRight() - getWidth() / 2, getTop() - Rules.Menu.WelcomeScreen.RateMonitor.HEADER_MARGIN_TOP, Assets.Strings.Menu.WelcomeScreen.RATE_HEADER, true);
//            } else {
//                drawRatedText(batch);
//            }
//        }
//
//        private void drawRatedText(Batch batch) {
//            drawText(batch, armyFont, getRight() - getWidth() / 2, getTop() - Rules.Menu.WelcomeScreen.RateMonitor.HEADER_MARGIN_TOP, Assets.Strings.Menu.WelcomeScreen.THANK_YOU_HEADER, true);
//            if (rateButton.isVisible()) {
//                String playStoreText = Assets.Strings.Menu.WelcomeScreen.PLAY_STORE_TEXT;
//                float targetWidth = getWidth() - Rules.Menu.WelcomeScreen.RateMonitor.TEXT_MARGIN_HORIZONTAL;
//                fontLayout.setText(armyFont, playStoreText, Color.GOLD, targetWidth, Align.center, false);
//                drawText(batch, armyFont, targetWidth, getRight() - (getWidth() / 2) - fontLayout.width / 4, getTop() - Rules.Menu.WelcomeScreen.RateMonitor.TEXT_MARGIN_TOP, playStoreText, true);
//            }
//        }
//
//        private void createStars(Drawable fullStar, Drawable blankStar) {
//            stars = new RatingStar[5];
//            float starWithMarginWidth = fullStar.getMinWidth() + Rules.Menu.WelcomeScreen.RateMonitor.STARS_INTERVAL;
//            float ratingLayoutWidth = ((starWithMarginWidth) * stars.length) - Rules.Menu.WelcomeScreen.RateMonitor.STARS_INTERVAL;
//            for (int i = 0; i < stars.length; i++) {
//                stars[i] = createStar(fullStar, blankStar, starWithMarginWidth, ratingLayoutWidth, i);
//            }
//        }
//
//        private RatingStar createStar(Drawable fullStar, Drawable blankStar, float starWithMarginWidth, float ratingLayoutWidth, int i) {
//            float x = (i * starWithMarginWidth) + (getWidth() / 2 - (ratingLayoutWidth / 2));
//            RatingStar ratingStar = new RatingStar(fullStar, blankStar, x, i + 1);
//            return ratingStar;
//        }
//
//        public void setRate(int rate) {
//            this.rate = rate;
//            for (RatingStar star : stars) {
//                setStarStateAccordingToRate(star);
//            }
//        }
//
//        private void setStarStateAccordingToRate(RatingStar star) {
//            if (star.getRateValue() <= rate) {
//                star.setFill(true);
//            } else {
//                star.setFill(false);
//            }
//        }
//
//        public void setStarsVisibility(boolean starsVisibility) {
//            for (RatingStar star : stars) {
//                star.setVisible(starsVisibility);
//            }
//        }
//
//    }

}
