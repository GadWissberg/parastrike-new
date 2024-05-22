package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.hud.ButtonClick;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

import java.util.HashMap;

import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Player.PREF_PLAYER;
import static com.gadarts.parashoot.assets.Assets.Configs.Preferences.Player.VISITED_FACEBOOK;
import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.*;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.WelcomeScreen.*;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.*;
import static com.gadarts.parashoot.utils.Rules.Menu.WelcomeScreen.BigButtons.MARGIN_HORIZONTAL;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.MEDIUM;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.SMALL;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.WIDTH_TARGET_RESOLUTION;

/**
 * Created by Gad on 21/10/2016.
 */

public class WelcomeScreen extends MenuScreenImproved {
    private final MonitorImproved monitor;
    private final MonitorImproved rateMonitor;
    private Button muteButton;
    private Button rateButton;
    private Button facebookButton;
    private Button highscoresButton;
    private MonitorImproved visitFacebookMonitor;

    public WelcomeScreen() {
        super(true, Assets.GFX.Sheets.Menus.WELCOME_BUTTONS_DATA_FILE, Assets.GFX.Sheets.Menus.MAIN_MENU_DATA_FILE);
        createLogo();
        setNextScreen(Parastrike.MenuType.SCENE_SELECTION);
        playMusic();
        monitor = createMonitor(createParastrikeContent(), MainMonitor.MONITOR_WIDTH, MainMonitor.MONITOR_HEIGHT, MainMonitor.MONITOR_Y, true, true, false);
        rateMonitor = createMonitor(createRateContent(), RateMonitor.RATE_MONITOR_WIDTH, RateMonitor.RATE_MONITOR_HEIGHT, RateMonitor.TARGET_Y, true, false, true, RATE_HEADER, RateMonitor.MONITOR_NAME);
        rateMonitor.setBackButtonVisibility(false);
        rateMonitor.setCloseButton(true);
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
        timer.scheduleTask(new Timer.Task() {
            @Override
            public void run() {
                visitFacebookButton();
            }
        }, Rules.Menu.WelcomeScreen.VisitFacebookMonitor.VISIT_FACEBOOK_MONITOR_DELAY);
    }

    private void visitFacebookButton() {
        Preferences playerPrefs = Gdx.app.getPreferences(PREF_PLAYER);
        if (!playerPrefs.getBoolean(VISITED_FACEBOOK, false)) {
            showVisitFacebookMessage();
        }
    }

    private void showVisitFacebookMessage() {
        visitFacebookMonitor = createMonitor(createVisitFacebookContent(),
            VisitFacebookMonitor.MONITOR_WIDTH, VisitFacebookMonitor.MONITOR_HEIGHT,
            VisitFacebookMonitor.TARGET_Y, true, true,
            true, VISIT_FACEBOOK_HEADER, VisitFacebookMonitor.MONITOR_NAME);
        visitFacebookMonitor.setBackButtonVisibility(false);
        visitFacebookMonitor.setCloseButton(true);
    }

    private Table createVisitFacebookContent() {
        Table table = new Table(getSkin());
        Label likeUsLabel = createVisitFacebookLabel();
        likeUsLabel.setWrap(true);
        likeUsLabel.setAlignment(Align.center);
        likeUsLabel.setVisible(false);
        table.add(likeUsLabel).row();
        return table;
    }

    private Label createVisitFacebookLabel() {
        BitmapFont font = Parastrike.getAssetsManager().get(SMALL, BitmapFont.class);
        Label.LabelStyle style = new Label.LabelStyle(font, Color.GOLD);
        return new Label(VISIT_FACEBOOK, style);
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

    private Table createRateContent() {
        Table table = new Table(getSkin());
        RatingStarsGroup ratingStarsGroup = new RatingStarsGroup();
        Label label = createThanksLabel();
        createTopStack(table, ratingStarsGroup, label);
        createRateMonitorButtons(table, ratingStarsGroup, label);
        return table;
    }

    private void createTopStack(Table table, RatingStarsGroup ratingStarsGroup, Label label) {
        Stack topStack = new Stack(ratingStarsGroup, label);
        topStack.setWidth(RateMonitor.RATE_MONITOR_WIDTH);
        table.add(topStack).padBottom(RateMonitor.STARS_PADDING_BOTTOM).row();
    }

    private Label createThanksLabel() {
        Label thanksText = new Label(PLAY_STORE_TEXT, new Label.LabelStyle(Parastrike.getAssetsManager().get(MEDIUM, BitmapFont.class), Color.GOLD));
        thanksText.setWrap(true);
        thanksText.setAlignment(Align.center);
        thanksText.setVisible(false);
        return thanksText;
    }

    private void createRateMonitorButtons(Table table, RatingStarsGroup ratingStarsGroup, Label thanksLabel) {
        Button googleRateButton = createGoogleRateButton();
        Button submitButton = createSubmitButton(googleRateButton, ratingStarsGroup, thanksLabel);
        Stack bottomStack = new Stack(googleRateButton, submitButton);
        table.add(bottomStack).row();
    }

    private Button createSubmitButton(Button googleRateButton, RatingStarsGroup ratingStarsGroup, Label thanksLabel) {
        Skin skin = getSkin();
        Drawable submitDrawable = skin.getDrawable(WELCOME_RATE_SUBMIT);
        Button submitButton = new Button(submitDrawable, skin.getDrawable(WELCOME_RATE_SUBMIT_PRESSED));
        submitButton.addListener(defineSubmitButton(ratingStarsGroup, googleRateButton, submitButton, thanksLabel));
        return submitButton;
    }

    private Button createGoogleRateButton() {
        Skin skin = getSkin();
        Button googleRateButton = new Button(skin.getDrawable(WELCOME_RATE_GOOGLE), skin.getDrawable(WELCOME_RATE_GOOGLE_PRESSED));
        googleRateButton.addListener(defineGoogleRateButtonClick());
        googleRateButton.setVisible(false);
        return googleRateButton;
    }

    private ButtonClick defineGoogleRateButtonClick() {
        return new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                super.changed(event, actor);
                Parastrike.getGGS().rateGame();
            }
        };
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
        createHighScoresButton();
        createRateButton();
        createFacebookButton();
    }

    private void createHighScoresButton() {
        Skin skin = getSkin();
        Drawable drawable = skin.getDrawable(WELCOME_HIGHSCORES);
        highscoresButton = new Button(drawable, skin.getDrawable(WELCOME_HIGHSCORES_PRESSED));
        highscoresButton.setPosition(WIDTH_TARGET_RESOLUTION, 0);
        float destX = WIDTH_TARGET_RESOLUTION - MARGIN_HORIZONTAL - drawable.getMinWidth();
        createBigButtonEffect(destX, 0, highscoresButton);
        highscoresButton.addListener(defineHighScoresButtonClick());
        getStage().addActor(highscoresButton);
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

    private void createRateButton() {
        Skin skin = getSkin();
        Drawable drawable = skin.getDrawable(WELCOME_RATE);
        rateButton = new Button(drawable, skin.getDrawable(WELCOME_RATE_PRESSED));
        rateButton.setPosition(WIDTH_TARGET_RESOLUTION, drawable.getMinHeight());
        float destX = WIDTH_TARGET_RESOLUTION - drawable.getMinWidth() - MARGIN_HORIZONTAL;
        createBigButtonEffect(destX, drawable.getMinHeight(), rateButton);
        rateButton.addListener(defineRateButtonClick());
        getStage().addActor(rateButton);
    }

    private void createFacebookButton() {
        Skin skin = getSkin();
        Drawable drawable = skin.getDrawable(WELCOME_FACEBOOK);
        facebookButton = new Button(drawable, skin.getDrawable(WELCOME_FACEBOOK_PRESSED));
        float buttonHeight = drawable.getMinHeight();
        facebookButton.setPosition(-drawable.getMinWidth(), buttonHeight);
        getStage().addActor(facebookButton);
        createBigButtonEffect(MARGIN_HORIZONTAL, drawable.getMinHeight(), facebookButton);
        facebookButton.addListener(defineFacebookButtonClick());
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

    private ButtonClickImproved defineRateButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                rateMonitor.setActivation(true);
            }
        };
    }

    private ButtonClickImproved defineHighScoresButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Parastrike.getGGS().showLeaderBoard();
            }
        };
    }

    private ButtonClickImproved defineFacebookButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Parastrike.getInstance().getActionResolver().openUrl(Assets.Strings.FACEBOOK_PAGE);
            }
        };
    }

    private ButtonClickImproved defineWebsiteButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
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

    private ButtonClick defineSubmitButton(final RatingStarsGroup ratingStarsGroup, final Button googleRateButton, final Button submitButton, final Label thanksLabel) {
        return new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                super.changed(event, actor);
                ratingStarsGroup.reportRating();
                hideButtons();
                rateMonitor.setHeader(THANK_YOU_HEADER);
                thanksLabel.setVisible(true);
                doChangesAccordingToRate();
            }

            private void doChangesAccordingToRate() {
                if (ratingStarsGroup.getRate() >= 4)
                    googleRateButton.setVisible(true);
                else thanksLabel.setText(THANKS_TEXT);
            }

            private void hideButtons() {
                ratingStarsGroup.setVisible(false);
                submitButton.setVisible(false);
            }
        };
    }

    private class RatingStarsGroup extends Table {
        private RatingStar[] stars = new RatingStar[5];
        private int rate;

        public RatingStarsGroup() {
            super(WelcomeScreen.this.getSkin());
            createStars();
            setRate(5);
        }

        private void createStars() {
            for (int i = 0; i < 5; i++) {
                Skin skin = WelcomeScreen.this.getSkin();
                RatingStar star = new RatingStar(skin.getDrawable(WELCOME_RATE_FULL_STAR), skin.getDrawable(WELCOME_RATE_BLANK_STAR), i);
                add(star).pad(RateMonitor.STARS_INTERVAL);
                stars[i] = star;
            }
        }

        private void setStarStateAccordingToRate(RatingStar star) {
            if (star.getRateValue() <= rate) {
                star.setFill(true);
            } else {
                star.setFill(false);
            }
        }

        private void reportRating() {
            Preferences preferences = Gdx.app.getPreferences(PREF_PLAYER);
            HashMap<String, String> attributes = new HashMap<String, String>();
            attributes.put(Rules.System.Analytics.Attributes.MenuScreen.RATING, String.valueOf(rate));
            Parastrike.getInstance().actionResolver.analyticsEventReport(Rules.System.Analytics.Events.USER_RATED, attributes);
        }

        public int getRate() {
            return rate;
        }

        public void setRate(int rate) {
            this.rate = rate;
            for (RatingStar star : stars) {
                setStarStateAccordingToRate(star);
            }
        }

        private class RatingStar extends Button {
            private final Drawable fullStarDrawable, blankStarDrawable;
            private final int rateValue;

            public RatingStar(Drawable fullStar, Drawable blankStar, final int rateValue) {
                super(fullStar, fullStar);
                this.rateValue = rateValue;
                fullStarDrawable = fullStar;
                blankStarDrawable = blankStar;
                addListener(defineButtonClick(rateValue));
            }


            private ButtonClick defineButtonClick(final int rateValue) {
                return new ButtonClick() {
                    @Override
                    public void changed(ChangeEvent event, Actor actor) {
                        super.changed(event, actor);
                        setRate(rateValue);
                    }
                };
            }

            @Override
            public void act(float delta) {
                super.act(delta);
                toFront();
            }

            public void setFill(boolean fill) {
                Drawable drawable = fill ? fullStarDrawable : blankStarDrawable;
                getStyle().up = drawable;
                getStyle().down = drawable;
            }

            public int getRateValue() {
                return rateValue;
            }
        }
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
