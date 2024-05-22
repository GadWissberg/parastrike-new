package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.level_model.LevelSkill;
import com.gadarts.parashoot.model.PlayerStatsHandler;
import com.gadarts.parashoot.screens.BasicMenuScreen;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 28/10/2016.
 */

public class IntroScreen extends BasicMenuScreen {

    public static final float FADING_PACE = 0.01f;
    private boolean fadeOut;
    private Sprite logoSprite;
    private float logoAlpha;
    private boolean introEnded;
    private boolean touchLocked;

    public IntroScreen() {
        super();
        setNextScreen(Parastrike.MenuType.WELCOME);
    }

    @Override
    public void show() {
        super.show();
        if (GameSettings.SKIP_INTRO) {
            goToNext();
        }
        logoSprite = new Sprite(Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.BOTTOM_LOADING_SCREEN_DATA_FILE, TextureAtlas.class).findRegion(Assets.GFX.Sheets.ImagesNames.GADARTS));
        logoSprite.setPosition(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2 - logoSprite.getWidth() / 2, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - logoSprite.getHeight() / 2);
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown(int screenX, int screenY, int pointer, int button) {
                if (!touchLocked && Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.INTRO_WAS_SHOWN, false)) {
                    goToNext();
                }
                return super.touchDown(screenX, screenY, pointer, button);
            }
        });
    }

    private void goToNext() {
        touchLocked = true;
        closeDoorsAndGoToScreen(new Timer.Task() {
            @Override
            public void run() {
                Parastrike.getAssetsManager().loadDataMenus();
            }
        }, new Timer.Task() {
            @Override
            public void run() {
                Parastrike.getInstance().goToMenuScreen(getNextScreen());
            }
        });
    }

    @Override
    public void render(float delta) {
        super.render(delta);
        menuStage.getBatch().begin();
        logoSprite.draw(menuStage.getBatch(), logoAlpha);
        menuStage.getBatch().end();
        menuStage.draw();
        handleLogoSpriteFading();
    }

    @Override
    protected void onBackPressed() {
        //Nothing.
    }

    private void handleLogoSpriteFading() {
        if (introEnded) {
            return;
        }
        if (!fadeOut) {
            logoAlpha = logoAlpha > 1 - FADING_PACE ? 1 : logoAlpha + FADING_PACE;
            if (logoAlpha == 1 && !TASK_CONTINUE_TO_NEXT.isScheduled()) {
                timer.scheduleTask(TASK_CONTINUE_TO_NEXT, Rules.Menu.INTRO_DURATION);
            }
        } else {
            if (logoAlpha <= 0 && !touchLocked) {
                final boolean tutorialComplete = Gdx.app.getPreferences(Assets.Configs.Preferences.InGameGuides.PREF_GUIDES).getBoolean(Assets.Configs.Preferences.InGameGuides.TUTORIAL_COMPLETE, false);
                touchLocked = true;
                closeDoorsAndGoToScreen(new Timer.Task() {
                    @Override
                    public void run() {
                        Preferences preferences = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS);
                        if (!preferences.getBoolean(Assets.Configs.Preferences.Settings.INTRO_WAS_SHOWN, false)) {
                            preferences.putBoolean(Assets.Configs.Preferences.Settings.INTRO_WAS_SHOWN, true).flush();
                        }
                        if (!tutorialComplete || GameSettings.ALWAYS_PLAY_TUTORIAL) {
                            initializeTutorial();
                        } else {
                            Parastrike.getAssetsManager().loadDataMenus();
                        }
                    }
                }, new Timer.Task() {
                    @Override
                    public void run() {
                        if (!tutorialComplete || GameSettings.ALWAYS_PLAY_TUTORIAL) {
                            Parastrike.getInstance().goToBattle();
                        } else {
                            Parastrike.getInstance().goToMenuScreen(getNextScreen());
                        }
                    }
                });
                introEnded = true;
            } else {
                logoAlpha = logoAlpha - FADING_PACE < 0 ? 0 : logoAlpha - FADING_PACE;
            }
        }
    }

    public static void initializeTutorial() {
        PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
        playerStatsHandler.setSelectedScene(null);
        playerStatsHandler.setSelectedLevel(0);
        playerStatsHandler.setSelectedLevelSkill(LevelSkill.EASY);
        Parastrike.getAssetsManager().loadDataBattle(Assets.Levels.TUTORIAL_LEVEL_FILE_NAME);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }

    private final Timer.Task TASK_CONTINUE_TO_NEXT = new Timer.Task() {
        @Override
        public void run() {
            fadeOut = true;
        }
    };

}
