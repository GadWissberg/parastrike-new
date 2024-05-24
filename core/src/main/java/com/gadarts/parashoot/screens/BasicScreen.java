package com.gadarts.parashoot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.AssetManagerWrapper;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.utils.Rules;
// All menus code has to be re-written. It was made in a non-generic way.

/**
 * Created by Gad on 01/02/2017.
 */
public abstract class BasicScreen implements Screen {
    protected final OrthographicCamera mainCamera = new OrthographicCamera();
    protected final Stage menuStage = new Stage();
    protected BitmapFont armyFont, digitalFont, regularFont;
    protected final GlyphLayout fontLayout = new GlyphLayout();
    private final LoadingsDoors loadingDoors;

    public BasicScreen() {
        Skin skin = new Skin();
        skin.addRegions(Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.TOP_LOADING_SCREEN_DATA_FILE, TextureAtlas.class));
        skin.addRegions(Parastrike.getAssetsManager().get(Assets.GFX.Sheets.General.BOTTOM_LOADING_SCREEN_DATA_FILE, TextureAtlas.class));
        loadingDoors = new LoadingsDoors(skin);
        armyFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.HUGE, BitmapFont.class);
        digitalFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.DigitalFontNames.HUGE, BitmapFont.class);
        regularFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.HUGE, BitmapFont.class);
        loadingDoors.forceLoadingDoorsState(Parastrike.getAssetsManager().isLoadingInProcess());
        Parastrike.getAssetsManager().setLoadingProcess(false);
    }


    @Override
    public void show() {
        mainCamera.setToOrtho(false, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
        menuStage.getViewport().setCamera(mainCamera);
    }

    @Override
    public void render(float delta) {
        checkBackButtonPress();
        mainCamera.update();
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        menuStage.getBatch().setProjectionMatrix(mainCamera.combined);
        menuStage.act(delta);
    }

    private void checkBackButtonPress() {
        if (Gdx.input.isKeyJustPressed(Input.Keys.BACK)) {
            onBackPressed();
        }
    }

    protected abstract void onBackPressed();

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
        menuStage.dispose();
    }

    public LoadingsDoors getLoadingDoors() {
        return loadingDoors;
    }

    protected class LoadingDoor extends Image {
        private final boolean BOTTOM;
        private final float OUTSIDE_Y, INSIDE_Y;
        private final TextureRegion rightRegion;
        private TextureRegion loadingRegion;
        private float verticalSpeed;
        private Timer.Task loadingTask, onCompletionTask;
        private ScrollPane.ScrollPaneStyle scrollPaneStyle;

        public LoadingDoor(Skin skin, String leftDrawableName, String rightDrawableName, boolean isBottom) {
            super(skin, leftDrawableName);
            this.rightRegion = skin.getRegion(rightDrawableName);
            this.BOTTOM = isBottom;
            if (isBottom) {
                this.loadingRegion = skin.getRegion(Assets.GFX.Sheets.ImagesNames.LOADING_SCREEN);
                OUTSIDE_Y = -(getHeight() + loadingRegion.getRegionHeight() / 2);
                INSIDE_Y = 0;
            } else {
                OUTSIDE_Y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
                INSIDE_Y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2;
            }
            setPosition(0, OUTSIDE_Y);
            menuStage.addActor(this);
            toFront();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            batch.draw(rightRegion, getWidth(), getY());
            if (loadingRegion != null) {
                batch.draw(loadingRegion, getWidth() - loadingRegion.getRegionWidth() / 2, getY() + getHeight() - loadingRegion.getRegionHeight() / 2);
                AssetManagerWrapper assetsManager = Parastrike.getAssetsManager();
                if (getY() == INSIDE_Y || !assetsManager.isLoadingInProcess()) {
                    int percent;
                    if (assetsManager.isLoadingInProcess()) {
                        percent = (int) (assetsManager.getProgress() * 100);
                    } else {
                        percent = 100;
                    }
                    drawText(
                        batch,
                        Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, BitmapFont.class),
                        Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2F,
                        getY() + getHeight(),
                        percent + "...", true, Color.RED);
                }
            }
        }


        @Override
        public void act(float delta) {
            super.act(delta);
            handleMovement();
            if (onCompletionTask != null && loadingTask == null) {
                if (Parastrike.getAssetsManager().update() && getY() == INSIDE_Y) {
                    if (loadingRegion != null) {
                        Parastrike.getSoundPlayer().playSound(SFX.HUD.ENGINE_MOVE);
                    }
                    onCompletionTask.run();
                    Parastrike.getAssetsManager().setLoadingProcess(false);
                    onCompletionTask = null;
                }
            }
        }

        private void handleMovement() {
            if (Parastrike.getAssetsManager().isLoadingInProcess()) {
                if (!BOTTOM) {
                    if (getY() >= OUTSIDE_Y) {
                        verticalSpeed = -Rules.Menu.Shop.LOADING_DOORS_SPEED;
                    } else if (verticalSpeed != 0 && getY() <= INSIDE_Y) {
                        onActiveAndInside();
                    }
                } else {
                    if (getY() <= OUTSIDE_Y) {
                        verticalSpeed = Rules.Menu.Shop.LOADING_DOORS_SPEED;
                    } else if (verticalSpeed != 0 && getY() >= INSIDE_Y) {
                        onActiveAndInside();
                    }
                }
            } else {
                if (!BOTTOM) {
                    if (getY() < OUTSIDE_Y) {
                        verticalSpeed = Rules.Menu.Shop.LOADING_DOORS_SPEED;
                    } else if (isVisible()) {
                        onDisabledAndOutside();
                    }
                } else {
                    if (getY() > OUTSIDE_Y) {
                        verticalSpeed = -Rules.Menu.Shop.LOADING_DOORS_SPEED;
                    } else if (isVisible()) {
                        onDisabledAndOutside();
                    }
                }
            }
            if (verticalSpeed != 0) {
                setY(getY() + verticalSpeed);
            }
        }

        private void onDisabledAndOutside() {
            verticalSpeed = 0;
            setY(OUTSIDE_Y);
            setVisible(false);
        }

        private void onActiveAndInside() {
            verticalSpeed = 0;
            setY(INSIDE_Y);
            if (loadingTask != null) {
                loadingTask.run();
                loadingTask = null;
            }
        }

        public void activate() {
            activate(null, null);
        }

        public void activate(Timer.Task loadingTask, Timer.Task onCompletionTask) {
            if (loadingRegion != null) {
                Parastrike.getSoundPlayer().playSound(SFX.HUD.ENGINE_MOVE);
            }
            setVisible(true);
            Parastrike.getAssetsManager().setLoadingProcess(true);
            if (loadingTask != null) {
                this.loadingTask = loadingTask;
            }
            if (onCompletionTask != null) {
                this.onCompletionTask = onCompletionTask;
            }
        }

        public void forceState(boolean state) {
            Parastrike.getAssetsManager().setLoadingProcess(state);
            setY(state ? INSIDE_Y : OUTSIDE_Y);
        }

        public boolean isClosed() {
            return verticalSpeed == 0 && getY() == INSIDE_Y;
        }

        public boolean isOpen() {
            return verticalSpeed == 0 && getY() == OUTSIDE_Y;
        }

    }

    protected void drawText(Batch batch, BitmapFont font, float x, float y, String text, Color color) {
        drawText(batch, font, x, y, text, true, color);
    }

    protected void drawText(Batch batch, BitmapFont font, float x, float y, String text, boolean center) {
        drawText(batch, font, x, y, text, center, Color.GOLD);
    }

    protected void drawText(Batch batch, BitmapFont font, float x, float y, String text, boolean center, Color color) {
        drawText(batch, font, 0, x, y, text, center, color);
    }

    protected void drawText(Batch batch, BitmapFont font, float targetWidth, float x, float y, String text, boolean center) {
        drawText(batch, font, targetWidth, x, y, text, center, Color.GOLD);
    }


    protected void drawText(Batch batch, BitmapFont font, float targetWidth, float x, float y, String text, boolean center, Color color) {
        drawText(batch, font, targetWidth, x, y, text, (center) ? Align.center : Align.left, color);
    }

    protected void drawText(Batch batch, BitmapFont font, float targetWidth, float x, float y, String text, int alignment, Color color) {
        if (text == null || text.isEmpty()) {
            return;
        }
        fontLayout.setText(font, text, color, targetWidth, alignment, false);
        font.draw(batch, fontLayout, x, y);
    }


    protected void closeDoorsAndGoToScreen(Timer.Task loadingTask, Timer.Task onCompletionTask) {
        loadingDoors.closeDoorsAndGoToScreen(loadingTask, onCompletionTask);
    }

    protected class LoadingsDoors {
        private final LoadingDoor topLoadingDoor;
        private final LoadingDoor bottomLoadingDoor;

        public LoadingsDoors(Skin skin) {
            topLoadingDoor = new LoadingDoor(skin, Assets.GFX.Sheets.ImagesNames.TOP_LEFT_LOADING, Assets.GFX.Sheets.ImagesNames.TOP_RIGHT_LOADING, false);
            bottomLoadingDoor = new LoadingDoor(skin, Assets.GFX.Sheets.ImagesNames.BOTTOM_LEFT_LOADING, Assets.GFX.Sheets.ImagesNames.BOTTOM_RIGHT_LOADING, true);
        }

        public LoadingDoor getTopLoadingDoor() {
            return topLoadingDoor;
        }

        public LoadingDoor getBottomLoadingDoor() {
            return bottomLoadingDoor;
        }

        public void toFront() {
            topLoadingDoor.toFront();
            bottomLoadingDoor.toFront();
        }

        public void forceLoadingDoorsState(boolean state) {
            topLoadingDoor.forceState(state);
            bottomLoadingDoor.forceState(state);
        }

        public void closeDoorsAndGoToScreen(Timer.Task loadingTask, Timer.Task onCompletionTask) {
            topLoadingDoor.activate();
            bottomLoadingDoor.activate(loadingTask, onCompletionTask);
        }

        public boolean areOpen() {
            return bottomLoadingDoor.isOpen() && topLoadingDoor.isOpen();
        }
    }
}
