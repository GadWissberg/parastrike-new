package com.gadarts.parashoot.screens.Menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Scaling;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.hud.ButtonClick;
import com.gadarts.parashoot.screens.MenuScreenImproved;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 05/05/2017.
 */
public class MonitorImproved extends Table {
    private Cell<WidgetGroup> mainContentWidgetCell;
    private float targetY;
    private MenuScreenImproved parentScreen;
    private Table headerTable;
    private float targetRange;
    private float verticalSpeed;
    private boolean isBottom;
    private boolean closeOnOutsideTouch;
    private float monitorAcceleration;
    private TextureAtlas.AtlasRegion topLeftRegion, topRightRegion, bottomLeftRegion, bottomRightRegion, horTopRegion, horBottomRegion, verLeftRegion, verRightRegion, redLightRegion;
    private Texture scanLineTexture = Parastrike.getAssetsManager().get(Assets.GFX.Images.Menus.SCAN_LINES);
    private TextureAtlas.AtlasRegion tubeRegion;
    private int defaultBlendSrc = -1, defaultBlendDst = -1;
    private boolean activated;
    private Label headerLabel;
    private Cell headerRightCell;
    private Cell<Button> headerLeftCell;
    private boolean closeButton;


    public MonitorImproved(Skin skin, final WidgetGroup mainContentWidgetCell, float width, float height, float x, float y, boolean isBottom, boolean activate, boolean closeOnOutsideTouch, float monitorAcceleration, MenuScreenImproved parentScreen, String header) {
        setDebug(GameSettings.SHOW_TABLES_LINES);
        initializeFields(y, isBottom, closeOnOutsideTouch, monitorAcceleration, parentScreen, activate, skin);
        initializeScreenRegions(isBottom, parentScreen);
        initializePositionAndSize(width, height, x, y, isBottom);
        initializeHeaderTable(header);
        setContent(mainContentWidgetCell);
    }

    private void initializeHeaderTable(String header) {
        if (header != null) {
            createHeader(header);
            add(headerTable).width(getWidth()).top();
            row();
        }
    }

    private void createHeader(String header) {
        headerTable = createHeaderTable();
        headerLeftCell = createBack(headerTable);
        BitmapFont font = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.BIG, BitmapFont.class);
        headerLabel = new Label(header, new Label.LabelStyle(font, Color.GOLD));
        headerTable.add(headerLabel).expand();
        headerRightCell = headerTable.add().width(headerLeftCell.getMinWidth());
    }

    private Cell<Button> createBack(Table headerTable) {
        Skin skin = parentScreen.getSkin();
        Button button = new Button(skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BACK_BUTTON), skin.getDrawable(Assets.GFX.Sheets.ImagesNames.BACK_BUTTON_PRESSED));
        button.addListener(defineBackButtonClick());
        return headerTable.add(button).top();
    }

    private ButtonClickImproved defineBackButtonClick() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                Parastrike.getInstance().goToMenuScreen(parentScreen.getBackScreen());
            }
        };
    }

    private Table createHeaderTable() {
        Table headerTable = new Table(parentScreen.getSkin());
        headerTable.align(Align.center);
        headerTable.setDebug(GameSettings.SHOW_TABLES_LINES);
        headerTable.setSize(Rules.Menu.Options.Monitor.MONITOR_WIDTH, parentScreen.getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.BACK_BUTTON).getMinHeight());
        return headerTable;
    }

    private void initializePositionAndSize(float width, float height, float x, float y, boolean isBottom) {
        top();
        setSize(width, height);
        handleScreenDirection(y, isBottom);
        setPosition(x, (isBottom) ? -(getHeightWithBorder()) : Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
    }

    private void handleScreenDirection(float y, boolean isBottom) {
        if (isBottom) {
            targetRange = (y + getHeightWithBorder()) / 2;
        } else {
            targetRange = (Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - y) / 2;
        }
    }

    private void initializeScreenRegions(boolean isBottom, MenuScreenImproved parentScreen) {
        TextureAtlas atlas = parentScreen.getSkin().getAtlas();
        tubeRegion = (isBottom) ? atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BOTTOM_TUBE) : atlas.findRegion(Assets.GFX.Sheets.ImagesNames.TOP_TUBE);
        initializeEdgesRegions(atlas);
        initializeSidesRegions(atlas);
        redLightRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.SCREEN_RED_LIGHT);
        scanLineTexture.setWrap(Texture.TextureWrap.Repeat, Texture.TextureWrap.Repeat);
    }

    private void initializeSidesRegions(TextureAtlas atlas) {
        horTopRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.SCREEN_HOR_TOP);
        horBottomRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.SCREEN_HOR_BOTTOM);
        verLeftRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.SCREEN_VER_LEFT);
        verRightRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.SCREEN_VER_RIGHT);
    }

    private void initializeEdgesRegions(TextureAtlas atlas) {
        topLeftRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.TOP_LEFT_CORNER);
        topRightRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.TOP_RIGHT_CORNER);
        bottomLeftRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BOTTOM_LEFT_CORNER);
        bottomRightRegion = atlas.findRegion(Assets.GFX.Sheets.ImagesNames.BOTTOM_RIGHT_CORNER);
    }

    private void initializeFields(float y, boolean isBottom, boolean closeOnOutsideTouch, float screenAcceleration, MenuScreenImproved parentScreen, boolean activate, Skin skin) {
        this.closeOnOutsideTouch = closeOnOutsideTouch;
        this.isBottom = isBottom;
        this.monitorAcceleration = screenAcceleration;
        this.parentScreen = parentScreen;
        this.targetY = y;
        this.activated = activate;
        setSkin(skin);
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        handleMonitorMovement();
    }

    private void handleMonitorMovement() {
        if (activated) handleActivatedMonitorMovement();
        else disabledMovement();
        if (verticalSpeed != 0) setY(getY() + verticalSpeed);
    }

    private void handleActivatedMonitorMovement() {
        activatedMovement();
        if (closeOnOutsideTouch && verticalSpeed == 0) {
            handleOutsideScreenTouch();
        }
    }

    private void handleOutsideScreenTouch() {
        parentScreen.getAuxVector().set(Gdx.input.getX(), Gdx.input.getY(), 0);
        parentScreen.getMainCamera().unproject(parentScreen.getAuxVector());
        float x = parentScreen.getAuxVector().x;
        float y = parentScreen.getAuxVector().y;
        boolean b = x < getX() || x > getRight() || y < getY() || y > getTop();
        if (Gdx.input.isTouched() && b) {
            setActivation(false);
        }
    }


    private void activatedMovement() {
        if (isBottom) {
            if (getY() < targetY - targetRange) {
                verticalSpeed += monitorAcceleration;
            } else if (verticalSpeed != 0) {
                verticalSpeed -= monitorAcceleration;
                if (verticalSpeed < 0) {
                    verticalSpeed = 0;
                }
            }
        } else {
            if (getY() > targetY + targetRange) {
                verticalSpeed -= monitorAcceleration;
            } else if (verticalSpeed < 0) {
                verticalSpeed += monitorAcceleration;
                if (verticalSpeed > 0) {
                    verticalSpeed = 0;
                }
            }
        }
    }

    private void disabledMovement() {
        if (isBottom) {
            if (getY() > targetY - targetRange) {
                verticalSpeed -= monitorAcceleration;
            } else if (verticalSpeed != 0) {
                verticalSpeed += monitorAcceleration;
                if (verticalSpeed > 0) {
                    verticalSpeed = 0;
                }
            }
        } else {
            if (getY() < targetY + targetRange) {
                verticalSpeed += monitorAcceleration;
            } else if (verticalSpeed > 0) {
                verticalSpeed -= monitorAcceleration;
                if (verticalSpeed < 0) {
                    verticalSpeed = 0;
                }
            }
        }
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float offset = (isBottom) ? tubeRegion.getRegionHeight() + 50 : (-(getHeight() + 50));
        batch.draw(tubeRegion, getX() + getWidth() / 2 - tubeRegion.getRegionWidth() / 2, getY() - (offset));
        drawGenericScreen(batch);
        super.draw(batch, parentAlpha);
    }

    private void drawGenericScreen(Batch batch) {
        defaultBlendDst = (defaultBlendDst == -1) ? batch.getBlendDstFunc() : defaultBlendDst;
        defaultBlendSrc = (defaultBlendSrc == -1) ? batch.getBlendSrcFunc() : defaultBlendSrc;
        if (parentScreen.getStage().getRoot().getColor().a >= 1) {
            batch.setBlendFunction(GL20.GL_ONE, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
        int verSize = verLeftRegion.getRegionWidth();
        batch.draw(scanLineTexture, getX() - verSize, getY() - verSize, 0, 0, (int) getWidth() + 2 * verSize, (int) getHeight() + 2 * verSize);
        int cornerRegionWidth = topLeftRegion.getRegionWidth();
        int cornerRegionHeight = topLeftRegion.getRegionHeight();
        int horSize = horTopRegion.getRegionWidth();
        float numberOfHor = getWidth() / horSize;
        for (float i = 0; i < numberOfHor; i++) {
            batch.draw(horTopRegion, (getX() - 1) + i * (horSize), getY() + getHeight(), horSize + 1, horSize);
        }
        for (float i = 0; i < numberOfHor; i++) {
            batch.draw(horBottomRegion, (getX() - 1) + i * (horSize), getY() - cornerRegionHeight, horSize + 1, horSize);
        }
        float numberOfVer = getHeight() / verSize;
        for (float i = 0; i < numberOfVer; i++) {
            batch.draw(verLeftRegion, getX() - verSize, (getY() - 1) + i * (verSize), verSize, verSize + 1);
        }
        for (float i = 0; i < numberOfVer; i++) {
            batch.draw(verRightRegion, getX() + getWidth(), (getY() - 1) + i * (verSize), verSize, verSize + 1);
        }
        batch.draw(bottomLeftRegion, getX() - cornerRegionWidth, getY() - cornerRegionHeight);
        batch.draw(bottomRightRegion, getX() + getWidth(), getY() - cornerRegionHeight);
        batch.draw(topLeftRegion, getX() - cornerRegionWidth, getY() + getHeight());
        batch.draw(topRightRegion, getX() + getWidth(), getY() + getHeight());
        batch.draw(redLightRegion, getX() + getWidth() - Rules.Menu.RED_LIGHT_X_OFFSET, getY() - Rules.Menu.RED_LIGHT_Y_OFFSET);
        if (parentScreen.getStage().getRoot().getColor().a >= 1) {
            batch.setBlendFunction(defaultBlendSrc, defaultBlendDst);
        }
    }

    public void setActivation(boolean activation) {
        if (verticalSpeed != 0) {
            return;
        }
        this.activated = activation;
        Parastrike.getSoundPlayer().playSound(SFX.HUD.PAUSE_MENU_MOVES, false, false);
    }

    private ButtonClick getDefaultCloseButtonClick() {
        return new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                super.changed(event, actor);
                MonitorImproved.this.setActivation(false);
            }
        };
    }

    public float getHeightWithBorder() {
        return getHeight() + horTopRegion.getRegionHeight() + horBottomRegion.getRegionHeight();
    }

    public float getBorderWidth() {
        return verLeftRegion.getRegionWidth();
    }

    public float getWidthWithBorder() {
        return getWidth() + verLeftRegion.getRegionWidth() + verRightRegion.getRegionWidth();
    }

    public boolean isActivated() {
        return activated;
    }

    public void setHeader(String header) {
        headerLabel.setText(header);
        headerTable.invalidateHierarchy();
    }

    public Cell setTopRightWidget(Actor topRightButton) {
        if (headerRightCell != null) {
            headerRightCell.setActor(topRightButton);
            headerRightCell.align(Align.right);
        }
        return headerRightCell;
    }

    public MonitorImproved setBackButtonVisibility(boolean backButtonVisibility) {
        if (headerLeftCell != null) {
            headerLeftCell.getActor().setVisible(backButtonVisibility);
        }
        return this;
    }

    public MonitorImproved setCloseButton(boolean set) {
        if (headerRightCell != null) {
            if (headerRightCell.getActor() == null) {
                if (set) createCloseButton();
            } else headerRightCell.getActor().setVisible(set);
        }
        return this;
    }

    private void createCloseButton() {
        ImageButton closeButton = new ImageButton(getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.CLOSE_BUTTON), getSkin().getDrawable(Assets.GFX.Sheets.ImagesNames.CLOSE_BUTTON_PRESSED));
        Image image = closeButton.getImage();
        image.setScaling(Scaling.none);
        closeButton.align(Align.right);
        closeButton.addListener(defineCloseButton());
        headerRightCell.setActor(closeButton);
    }

    private ButtonClickImproved defineCloseButton() {
        return new ButtonClickImproved() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                super.clicked(event, x, y);
                MonitorImproved.this.setActivation(false);
            }
        };
    }

    public void setTopLeftButton(ImageButton topLeftButton) {
        if (headerLeftCell != null) {
            headerLeftCell.setActor(topLeftButton);
        }
    }

    public void setContent(WidgetGroup content) {
        if (mainContentWidgetCell != null) {
            if (content == mainContentWidgetCell.getActor()) return;
            mainContentWidgetCell.clearActor();
            this.mainContentWidgetCell.setActor(content);
        } else mainContentWidgetCell = add(content).width(getWidth()).expand();
    }

    public WidgetGroup getContent() {
        return mainContentWidgetCell.getActor();
    }

    public Cell getHeaderRightCell() {
        return headerRightCell;
    }
}
