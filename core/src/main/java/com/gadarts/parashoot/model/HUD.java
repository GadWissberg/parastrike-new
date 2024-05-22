package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Button;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TransformDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.hud.ButtonClick;
import com.gadarts.parashoot.level_model.Event;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.misc.stuff.AllyPlane;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.model.tutorial.Tutor;
import com.gadarts.parashoot.player.bunker.Turret;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;
// All menus code has to be re-written. It was made in a non-generic way.

/**
 * Created by Gad on 31/05/2016.
 */
public class HUD extends Stage {

    private final Array<BombButton> bombsButtons = new Array<BombButton>();
    private final Skin windowSkin = new Skin();
    private final Skin buttonsSkin = new Skin();
    private final Skin hudSkin = new Skin();
    private final MiniHud miniHud;
    private final HUDwidget needle;
    private final HUDwidget gauge;
    private final PauseWindow pauseWindow;
    private final StatisticsWindow statisticsWindow;
    private final WarScreen warScreen;
    private HUDbutton bombsButton;
    private HUDbutton optionsButton;
    private ShapeRenderer shapeRenderer = new ShapeRenderer();
    private GlyphLayout fontLayout = new GlyphLayout();
    private BitmapFont armyFont, regularFont;
    private com.gadarts.parashoot.model.tutorial.Guide guide = new com.gadarts.parashoot.model.tutorial.Guide();

    public HUD(WarScreen warScreen) {
        this.warScreen = warScreen;
        addActor(guide);
        armyFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.SMALL, BitmapFont.class);
        regularFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class);
        buttonsSkin.addRegions((TextureAtlas) Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.BUTTONS_DATA_FILE));
        hudSkin.addRegions((TextureAtlas) Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.HUD_DATA_FILE));
        windowSkin.addRegions((TextureAtlas) Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.HUD_MENU_DATA_FILE));
        createButtons();
        TextureRegion hudRegion = hudSkin.getRegion(Assets.GFX.Sheets.ImagesNames.MINI_HUD);
        miniHud = new MiniHud(hudRegion);
        miniHud.setPosition(Rules.Hud.Gauge.X, Rules.Hud.Gauge.Y);
        addActor(miniHud);
        TextureRegion gaugeRegion = hudSkin.getRegion(Assets.GFX.Sheets.ImagesNames.HEAT_GAUGE);
        gauge = new HUDwidget(gaugeRegion);
        gauge.setPosition(Rules.Hud.Gauge.X, Rules.Hud.Gauge.Y, true);
        addActor(gauge);
        TextureRegion needleRegion = hudSkin.getRegion(Assets.GFX.Sheets.ImagesNames.NEEDLE);
        needle = new HUDwidget(needleRegion);
        needle.setPosition(gauge.getX() + gaugeRegion.getRegionWidth() / 2, gauge.getY() + gaugeRegion.getRegionHeight() / 2 - Rules.Hud.Gauge.Needle.NEEDLE_Y_OFFSET);
        needle.setOrigin(Rules.Hud.Gauge.Needle.NEEDLE_ORIGIN_X, needleRegion.getRegionHeight() / 2);
        addActor(needle);
        pauseWindow = new PauseWindow(windowSkin);
        statisticsWindow = new StatisticsWindow(windowSkin);
    }

    public boolean isMenuOpen() {
        return pauseWindow.activated || statisticsWindow.activated;
    }

    private void createButtons() {
        bombsButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_BOMBS, Assets.GFX.Sheets.ImagesNames.BUTTON_BOMBS_PRESSED, Assets.GFX.Sheets.ImagesNames.BUTTON_BOMBS_CHECKED, Assets.GFX.Sheets.ImagesNames.BUTTON_BOMBS_DISABLED, Rules.Hud.Buttons.BOMBS_X, Rules.Hud.Buttons.BOMBS_Y, new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                Parastrike.getSoundPlayer().playSound(SFX.HUD.BUTTON_CLICK, false, false);
                switchBombsButtonsAppearance();
                bombsButton.setHighlight(false);
            }
        });
        BombButton button = createBombButton(Assets.GFX.Sheets.ImagesNames.BUTTON_BIO_HAZARD, Assets.GFX.Sheets.ImagesNames.BUTTON_BIO_HAZARD_PRESSED, null, Assets.GFX.Sheets.ImagesNames.BUTTON_BIO_HAZARD_DISABLED, Rules.Hud.Buttons.BIOHAZARD_X, Rules.Hud.Buttons.BIOHAZARD_Y, Rules.Player.ArmoryItem.BIO_HAZARD, new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((HUDbutton) actor).getVerticalSpeed() != 0) {
                    return;
                }
                super.changed(event, actor);
                MessageDisplay messageDisplay = warScreen.getElements().getMessageDisplay();
                messageDisplay.add(Assets.Strings.InGameMessages.Bonus.INCOMING_AIRSTRIKE);
                Parastrike.getPlayerStatsHandler().degradeBomb(Rules.Player.ArmoryItem.BIO_HAZARD);
                updateBombsButtons();
                bombsButton.setChecked(false);
                AllyPlane plane = (AllyPlane) warScreen.getFactories().getMiscFactory().createMisc(MiscFactory.MiscType.ALLY_PLANE);
                plane.setType(Rules.Player.ArmoryItem.BIO_HAZARD);
                messageDisplay.taunt(SFX.Taunts.INCOMING_AIR_STRIKE);
            }
        });
        bombsButtons.add(button);
        button.setDisabled(true);
        button = createBombButton(Assets.GFX.Sheets.ImagesNames.BUTTON_AIR_STRIKE, Assets.GFX.Sheets.ImagesNames.BUTTON_AIRSTRIKE_PRESSED, null, Assets.GFX.Sheets.ImagesNames.BUTTON_AIRSTRIKE_DISABLED, Rules.Hud.Buttons.AIR_STRIKE_X, Rules.Hud.Buttons.AIR_STRIKE_Y, Rules.Player.ArmoryItem.AIR_STRIKE, new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((HUDbutton) actor).getVerticalSpeed() != 0) {
                    return;
                }
                super.changed(event, actor);
                MessageDisplay messageDisplay = warScreen.getElements().getMessageDisplay();
                messageDisplay.add(Assets.Strings.InGameMessages.Bonus.INCOMING_AIRSTRIKE);
                Parastrike.getPlayerStatsHandler().degradeBomb(Rules.Player.ArmoryItem.AIR_STRIKE);
                updateBombsButtons();
                bombsButton.setChecked(false);
                AllyPlane plane = (AllyPlane) warScreen.getFactories().getMiscFactory().createMisc(MiscFactory.MiscType.ALLY_PLANE);
                plane.setType(Rules.Player.ArmoryItem.AIR_STRIKE);
                messageDisplay.taunt(SFX.Taunts.INCOMING_AIR_STRIKE);
            }
        });
        bombsButtons.add(button);
        button.setDisabled(true);

        button = createBombButton(Assets.GFX.Sheets.ImagesNames.BUTTON_ATOM, Assets.GFX.Sheets.ImagesNames.BUTTON_ATOM_PRESSED, null, Assets.GFX.Sheets.ImagesNames.BUTTON_ATOM_DISABLED, Rules.Hud.Buttons.ATOM_X, Rules.Hud.Buttons.ATOM_Y, Rules.Player.ArmoryItem.ATOM, new ButtonClick() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                if (((HUDbutton) actor).getVerticalSpeed() != 0) {
                    return;
                }
                super.changed(event, actor);
                MessageDisplay messageDisplay = warScreen.getElements().getMessageDisplay();
                messageDisplay.add(Assets.Strings.InGameMessages.Bonus.INCOMING_AIRSTRIKE);
                Parastrike.getPlayerStatsHandler().degradeBomb(Rules.Player.ArmoryItem.ATOM);
                updateBombsButtons();
                bombsButton.setChecked(false);
                AllyPlane plane = (AllyPlane) warScreen.getFactories().getMiscFactory().createMisc(MiscFactory.MiscType.ALLY_PLANE);
                plane.setType(Rules.Player.ArmoryItem.ATOM);
                messageDisplay.taunt(SFX.Taunts.INCOMING_AIR_STRIKE);
            }
        });
        bombsButtons.add(button);
        button.setDisabled(true);
        optionsButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_OPTIONS, Assets.GFX.Sheets.ImagesNames.BUTTON_OPTIONS_PRESSED, Assets.GFX.Sheets.ImagesNames.BUTTON_OPTIONS_CHECKED, null, Rules.Hud.Buttons.OPTIONS_X, Rules.Hud.Buttons.OPTIONS_Y, new ButtonClick() {

            @Override
            public void changed(ChangeEvent event, Actor actor) {
                super.changed(event, actor);
                engagePauseMenu();
            }
        });
        updateBombsButtons();
    }

    public void engagePauseMenu() {
        pauseWindow.setState(!pauseWindow.isActivated());
        Parastrike.getSoundPlayer().playSound(SFX.HUD.PAUSE_MENU_MOVES);
    }

    @Override
    public void draw() {
        Camera camera = getViewport().getCamera();
        camera.update();

        if (!getRoot().isVisible()) return;

        Batch batch = this.getBatch();
        if (batch != null) {
            batch.setProjectionMatrix(camera.combined);
            getRoot().draw(batch, 1);
        }
    }

    public void initializeBombsButtons() {
        bombsButton.setHighlight(false);
        for (int i = 0; i < bombsButtons.size; i++) {
            BombButton button = bombsButtons.get(i);
            button.setY(-button.getHeight());
            button.getColor().a = 0;
            button.setIndicatorValue(Parastrike.getPlayerStatsHandler().getBombAmount(button.getBombType()));
        }
    }

    public void updateBombsButtons() {
        boolean disabled = true;
        for (int i = 0; i < bombsButtons.size; i++) {
            BombButton currentButton = bombsButtons.get(i);
            currentButton.updateIndicator();
            if (Parastrike.getPlayerStatsHandler().getBombAmount(currentButton.getBombType()) > 0) {
                disabled = false;
                currentButton.setDisabled(false);
            } else {
                currentButton.setDisabled(true);
            }
        }
        if (disabled) {
            bombsButton.setDisabled(true);
        } else {
            bombsButton.setDisabled(false);
        }
    }

    private HUDbutton createButton(String up, String down, String checked, String disabled, float x, float y, ChangeListener listener) {
        return createButton(up, down, checked, disabled, x, y, listener, true);
    }

    private BombButton createBombButton(String up, String down, String checked, String disabled, float x, float y, Rules.Player.ArmoryItem bombType, ChangeListener listener) {
        Drawable checkedDrawable = null;
        if (checked != null) {
            checkedDrawable = buttonsSkin.getDrawable(checked);
        }
        Button.ButtonStyle style = new Button.ButtonStyle(buttonsSkin.getDrawable(up), buttonsSkin.getDrawable(down), checkedDrawable);
        Drawable disabledDrawable = null;
        if (disabled != null) {
            disabledDrawable = buttonsSkin.getDrawable(disabled);
        }
        style.disabled = disabledDrawable;
        BombButton button = new BombButton(style, bombType);
        button.setX(x);
        button.setY(y);
        addActor(button);
        if (listener != null) {
            button.addListener(listener);
        }
        button.setVisible(true);
        return button;
    }

    private HUDbutton createButton(String up, String down, String checked, String disabled, float x, float y, ChangeListener listener, boolean visible) {
        Drawable checkedDrawable = null;
        if (checked != null) {
            checkedDrawable = buttonsSkin.getDrawable(checked);
        }
        Button.ButtonStyle style = new Button.ButtonStyle(buttonsSkin.getDrawable(up), buttonsSkin.getDrawable(down), checkedDrawable);
        Drawable disabledDrawable = null;
        if (disabled != null) {
            disabledDrawable = buttonsSkin.getDrawable(disabled);
        }
        style.disabled = disabledDrawable;
        HUDbutton button = new HUDbutton(style);
        button.setX(x);
        button.setY(y);
        addActor(button);
        if (listener != null) {
            button.addListener(listener);
        }
        button.setVisible(visible);
        return button;
    }

    private void switchBombsButtonsAppearance() {
        switchBombsButtonsAppearance(bombsButton.isChecked());
    }

    private void switchBombsButtonsAppearance(boolean state) {
        if (state) {
            setBombsButtonsAppearance(Rules.Hud.Buttons.VERTICAL_SPEED);
        } else {
            setBombsButtonsAppearance(-Rules.Hud.Buttons.VERTICAL_SPEED);
        }
    }

    private void setBombsButtonsAppearance(float verticalSpeed) {
        for (int i = 0; i < bombsButtons.size; i++) {
            HUDbutton button = bombsButtons.get(i);
            button.setVerticalSpeed(verticalSpeed);
        }
    }

    @Override
    public void act(float delta) {
        super.act(delta);
        Turret turret = warScreen.getPlayerHandler().getTurret();
        float heatPoints = turret.getHeatPoints();
        float rotation = needle.getRotation();
        if (heatPoints * 180 / 100 > rotation + Rules.Hud.Gauge.Needle.NEEDLE_ROTATION_DELTA) {
            needle.setRotation(rotation + Rules.Hud.Gauge.Needle.NEEDLE_ROTATION_DELTA);
        } else if (heatPoints * 180 / 100 < rotation - Rules.Hud.Gauge.Needle.NEEDLE_ROTATION_DELTA) {
            needle.setRotation(rotation - Rules.Hud.Gauge.Needle.NEEDLE_ROTATION_DELTA);
        }
        if (!miniHud.hide) {
            if (turret.isOverHeat()) {
                if (Math.abs(gauge.getSavedX() - gauge.getX()) <= Rules.Hud.Gauge.SHAKE_INTERVAL) {
                    gauge.setX(gauge.getX() + (MathUtils.randomBoolean() ? Rules.Hud.Gauge.SHAKE_INTERVAL : -Rules.Hud.Gauge.SHAKE_INTERVAL));
                } else {
                    gauge.setPositionToSaved();
                }
                if (Math.abs(gauge.getSavedY() - gauge.getY()) <= Rules.Hud.Gauge.SHAKE_INTERVAL) {
                    gauge.setY(gauge.getY() + (MathUtils.randomBoolean() ? Rules.Hud.Gauge.SHAKE_INTERVAL : -Rules.Hud.Gauge.SHAKE_INTERVAL));
                } else {
                    gauge.setPositionToSaved();
                }
            } else {
                gauge.setPositionToSaved();
            }
        }
    }

    public void setBombsButtonState(boolean bombsButtonState) {
        bombsButton.setChecked(bombsButtonState);
    }

    public void highlightBombsButton() {
        bombsButton.setHighlight(true);
    }

    public void engageSmoke() {
        warScreen.getFactories().getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.WHITE_BIG_SMOKE_UP, gauge.getX() + gauge.getImageWidth() / 2, gauge.getY() + gauge.getImageHeight() / 2);
    }

    public ShapeRenderer getShapeRenderer() {
        return shapeRenderer;
    }

    public HUDbutton getOptionsButton() {
        return optionsButton;
    }

    public void showStatistics() {
        statisticsWindow.setState(true);
        bombsButton.setDisabled(true);
        switchBombsButtonsAppearance(false);
        miniHud.hide();
    }

    public MiniHud getMiniHud() {
        return miniHud;
    }

    public HUDbutton getBombsButton() {
        return bombsButton;
    }


    public com.gadarts.parashoot.model.tutorial.Guide getGuide() {
        return guide;
    }

    public void setTutorTarget(Tutor tutor, Event.FocusTarget target) {
        if (target == Event.FocusTarget.PLAYER)
            tutor.setInGameTarget(warScreen.getPlayerHandler().getBunker());
        else if (target == Event.FocusTarget.PARATROOPER) {
            Array<? extends GameCharacter> allParatroopers = warScreen.getAllParatroopers();
            if (allParatroopers.size > 0) tutor.setInGameTarget(allParatroopers.get(0));
        }
    }


    public class BombButton extends HUDbutton {

        private Rules.Player.ArmoryItem bombType;

        public BombButton(ButtonStyle style, Rules.Player.ArmoryItem bombType) {
            super(style);
            this.bombType = bombType;
            updateIndicator();
        }

        public void updateIndicator() {
            setIndicatorValue(Parastrike.getPlayerStatsHandler().getBombAmount(bombType));
        }

        public Rules.Player.ArmoryItem getBombType() {
            return bombType;
        }

    }

    public class HUDbutton extends Button {

        private final float targetY;
        private int indicatorValue;
        private TextureRegion indicatorRegion = buttonsSkin.getRegion(Assets.GFX.Sheets.ImagesNames.INGAME_BUTTON_INDICATOR);
        private TextureRegion highlightRegion = buttonsSkin.getRegion(Assets.GFX.Sheets.ImagesNames.INGAME_BUTTON_HIGHLIGHT);
        private final BitmapFont indicatorFont;
        private final GlyphLayout indicatorLayout = new GlyphLayout();
        private float verticalSpeed;
        private boolean showHighlight;

        public HUDbutton(ButtonStyle style) {
            super(style);
            indicatorFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL, BitmapFont.class);
            targetY = getY();
        }

        public void setIndicatorValue(int value) {
            this.indicatorValue = value;
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            if (indicatorValue > 1) {
                indicatorFont.setColor(Color.WHITE);
                indicatorLayout.setText(indicatorFont, "" + indicatorValue);
                float x = getX() + getWidth() - indicatorRegion.getRegionWidth();
                batch.draw(indicatorRegion, x, getY());
                indicatorFont.draw(batch, indicatorLayout, x + indicatorRegion.getRegionWidth() / 2 - indicatorLayout.width / 2, getY() + indicatorRegion.getRegionHeight() / 2 + indicatorLayout.height / 2);
            }
            if (showHighlight) {
                batch.draw(highlightRegion, getX(), getY());
            }
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (verticalSpeed != 0) {
                if (getY() <= targetY && getY() >= -bombsButton.getHeight()) {
                    setY(getY() + verticalSpeed);
                    handleAlphaChange();
                } else if (getY() >= targetY) {
                    setY(targetY);
                    verticalSpeed = 0;
                } else if (getY() <= -bombsButton.getHeight()) {
                    setY(-bombsButton.getHeight());
                    verticalSpeed = 0;
                }
            }
        }

        private void handleAlphaChange() {
            Color color = getColor();
            if (color.a > 1) {
                color.a = 1;
            } else if (color.a < 0) {
                color.a = 0;
            } else {
                float delta = Rules.Hud.Buttons.RISE_ALPHA_CHANGE;
                if (verticalSpeed > 0) {
                    if (color.a < 1 - delta) {
                        color.a += delta;
                    } else {
                        color.a = 1;
                    }
                } else if (verticalSpeed < 0) {
                    if (color.a > 0 + delta) {
                        color.a -= delta;
                    } else {
                        color.a = 0;
                    }
                }
            }
        }

        public void setVerticalSpeed(float verticalSpeed) {
            this.verticalSpeed = verticalSpeed;
        }

        public void setHighlight(boolean highlight) {
            if (highlight && !TASK_CHANGE_HIGHLIGHT.isScheduled() && !bombsButton.isChecked()) {
                Timer.schedule(TASK_CHANGE_HIGHLIGHT, Rules.Hud.Buttons.HIGHLIGHT_FREQ, Rules.Hud.Buttons.HIGHLIGHT_FREQ, Rules.Hud.Buttons.HIGHLIGHT_DURATION);
            } else {
                showHighlight = false;
                TASK_CHANGE_HIGHLIGHT.cancel();
            }
        }

        private final Timer.Task TASK_CHANGE_HIGHLIGHT = new Timer.Task() {
            @Override
            public void run() {
                showHighlight = !showHighlight;
            }
        };

        public float getVerticalSpeed() {
            return verticalSpeed;
        }

    }

    public class MiniHud extends HUDwidget {

        private boolean hide;

        public MiniHud(TextureRegion region) {
            super(region);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (hide && getY() < Rules.Hud.Gauge.MAX_Y) {
                moveBy(0, Rules.Hud.Buttons.VERTICAL_SPEED);
                gauge.moveBy(0, Rules.Hud.Buttons.VERTICAL_SPEED);
                needle.moveBy(0, Rules.Hud.Buttons.VERTICAL_SPEED);
            }
        }

        public void hide() {
            hide = true;
        }

        public boolean isHiding() {
            return hide;
        }

    }

    private class HUDwidget extends Image {

        private float savedX;
        private float savedY;

        public HUDwidget(TextureRegion region) {
            super(region);
        }

        public void setPosition(float x, float y, boolean savePosition) {
            super.setPosition(x, y);
            if (savePosition) {
                savedX = x;
                savedY = y;
            }
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            validate();

            Color color = getColor();
            batch.setColor(color.r, color.g, color.b, color.a * parentAlpha);

            float x = getX();
            float y = getY();
            float scaleX = getScaleX();
            float scaleY = getScaleY();

            if (getDrawable() instanceof TransformDrawable) {
                float rotation = getRotation();
                ((TransformDrawable) getDrawable()).draw(batch, x + getImageX() - getOriginX(), y + getImageY() - getOriginY(), getOriginX() - getImageX(), getOriginY() - getImageY(), getImageWidth(), getImageHeight(), scaleX, scaleY, rotation);
            }
        }

        public void setPositionToSaved() {
            setX(savedX);
            setY(savedY);
        }

        public float getSavedX() {
            return savedX;
        }

        public float getSavedY() {
            return savedY;
        }

    }

    public class SlidingWindow extends Window {

        protected float speed;
        protected boolean activated;

        public SlidingWindow(Skin skin) {
            super(skin);
            setY(3 * Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2);
            addActor(this);
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (!isVisible()) {
                return;
            }
            int tileWidth = INSIDE_REGION.getRegionWidth();
            float leftHolderX = getX() - getWidth() / 4 - tileWidth / 2;
            batch.draw(VERTICAL_REGION, leftHolderX, getY() + getHeight() / 2, tileWidth, Math.abs(Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - (getY() + getHeight() / 2)));
            float rightHolderX = getX() + getWidth() / 4 - tileWidth / 2;
            batch.draw(VERTICAL_REGION, rightHolderX, getY() + getHeight() / 2, tileWidth, Math.abs(Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - (getY() + getHeight() / 2)));
            batch.draw(HOLDER_REGION, rightHolderX, getY() + getHeight() / 2);
            batch.draw(HOLDER_REGION, leftHolderX, getY() + getHeight() / 2);
            super.draw(batch, parentAlpha);
            String name = warScreen.getElements().getCurrentLevel().getName();
            fontLayout.setText(armyFont, name);
            armyFont.draw(getBatch(), name, getX() - fontLayout.width / 2, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.HEADER_Y_MARGIN);
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (activated) {
                if (getY() > (2 * Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION) / 3) {
                    move(false);
                } else if (speed != 0) {
                    slowDown();
                }
            } else {
                if (getY() < (3 * Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2)) {
                    move(true);
                } else if (isVisible()) {
                    setVisible(false);
                }
            }
        }

        protected void move(boolean up) {
            moveBy(0, speed);
            if (up) {
                if (speed < Rules.Hud.PauseWindow.MAX_SLIDE_SPEED) {
                    speed += Rules.Hud.PauseWindow.SPEED_ACCELERATION;
                }
            } else {
                if (speed > -Rules.Hud.PauseWindow.MAX_SLIDE_SPEED) {
                    speed -= Rules.Hud.PauseWindow.SPEED_ACCELERATION;
                }
            }
        }

        private void slowDown() {
            if (speed > 0) {
                speed -= Rules.Hud.PauseWindow.SPEED_ACCELERATION;
            } else if (speed < 0) {
                speed += Rules.Hud.PauseWindow.SPEED_ACCELERATION;
            }
            if (speed != 0) {
                if (speed < Rules.Hud.PauseWindow.SPEED_ACCELERATION && speed > -Rules.Hud.PauseWindow.SPEED_ACCELERATION) {
                    speed = 0;
                }
                moveBy(0, speed);
            }
        }

        public boolean isActivated() {
            return activated;
        }

        public void setState(boolean state) {
            setState(state, true);
        }

        public void setState(boolean state, boolean changeScreen) {
            this.activated = state;
            if (state) {
                toFront();
                setVisible(true);
                if (changeScreen) {
                    warScreen.pause();
                    warScreen.getPainter().fadeBrightness(-0.01f);
                    Parastrike.getSoundPlayer().pauseAllSounds();
                }
            } else {
                if (changeScreen) {
                    warScreen.resume();
                    warScreen.getPainter().fadeBrightness(0.01f);
                    Parastrike.getSoundPlayer().resumeAllSounds();
                }
            }
        }

    }

    public class StatisticsWindow extends SlidingWindow {

        private final TextureRegion iconCoins;
        private final TextureRegion iconScore;
        private final TextureRegion iconCrosshair;

        public StatisticsWindow(Skin skin) {
            super(skin);
            setVisible(false);
            setWidth(Rules.Hud.StatisticsWindow.WIDTH);
            setHeight(Rules.Hud.StatisticsWindow.HEIGHT);
            iconScore = hudSkin.getRegion(Assets.GFX.Sheets.ImagesNames.ICON_SCORE);
            iconCoins = hudSkin.getRegion(Assets.GFX.Sheets.ImagesNames.ICON_COINS);
            iconCrosshair = hudSkin.getRegion(Assets.GFX.Sheets.ImagesNames.ICON_CROSSHAIR);
            enableVerifyButton(new ButtonClick() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    super.changed(event, actor);
                    pauseWindow.setVisible(false);
                    statisticsWindow.setVisible(false);
                    warScreen.endBattle();
                }

            });
            verifyButton.setVisible(false);
        }

        @Override
        public void setState(boolean state, boolean changeScreen) {
            super.setState(state, changeScreen);
            verifyButton.setVisible(state);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
        }

        @Override
        public void moveBy(float x, float y) {
            super.moveBy(x, y);
            verifyButton.setPosition(getX() + getWidth() / 2 - Rules.Hud.StatisticsWindow.VERIFY_BUTTON_X_MARGIN, getY() - getHeight() / 2 + Rules.Hud.StatisticsWindow.VERIFY_BUTTON_Y_MARGIN);
            verifyButton.toFront();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            super.draw(batch, parentAlpha);
            if (!isVisible()) {
                return;
            }
            batch.draw(iconCoins, getX() - Rules.Hud.StatisticsWindow.COINS_ICON_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.COINS_ICON_Y_MARGIN);
            BitmapFont digitalFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.DigitalFontNames.SMALL, BitmapFont.class);
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            fontLayout.setText(digitalFont, "" + playerStatsHandler.getCoins());
            digitalFont.draw(getBatch(), fontLayout, getX() - Rules.Hud.StatisticsWindow.COINS_VALUE_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.COINS_VALUE_Y_MARGIN);

            batch.draw(iconScore, getX() - Rules.Hud.StatisticsWindow.COINS_ICON_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.SCORE_ICON_Y_MARGIN);
            WarScreenElements mechanics = warScreen.getElements();
            ScoresHandler scoresHandler = mechanics.getScoresHandler();
            fontLayout.setText(digitalFont, "" + playerStatsHandler.getScore());
            digitalFont.draw(getBatch(), fontLayout, getX() - Rules.Hud.StatisticsWindow.COINS_VALUE_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.SCORE_VALUE_Y_MARGIN);

            batch.draw(iconCrosshair, getX() - Rules.Hud.StatisticsWindow.COINS_ICON_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.CROSSHAIR_ICON_Y_MARGIN);
            Level currentLevel = warScreen.getElements().getCurrentLevel();
            fontLayout.setText(digitalFont, "" + scoresHandler.getEnemiesDestroyed() + " / " + currentLevel.getTotalEnemies());
            digitalFont.draw(getBatch(), fontLayout, getX() - Rules.Hud.StatisticsWindow.COINS_VALUE_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.CROSSHAIR_VALUE_Y_MARGIN);

            if (currentLevel.getFeat(Level.Feat.RAMPAGE)) {
                fontLayout.setText(digitalFont, "RAMPAGE");
                digitalFont.draw(getBatch(), fontLayout, getX() - Rules.Hud.StatisticsWindow.COINS_VALUE_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.CROSSHAIR_VALUE_Y_MARGIN - 75);
            }
            if (currentLevel.getFeat(Level.Feat.FLAWLESS)) {
                fontLayout.setText(digitalFont, "FLAWLESS");
                digitalFont.draw(getBatch(), fontLayout, getX() - Rules.Hud.StatisticsWindow.COINS_VALUE_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.CROSSHAIR_VALUE_Y_MARGIN - 125);
            }
            if (currentLevel.getFeat(Level.Feat.PURE)) {
                fontLayout.setText(digitalFont, "PURE");
                digitalFont.draw(getBatch(), fontLayout, getX() - Rules.Hud.StatisticsWindow.COINS_VALUE_X_MARGIN, getY() + getHeight() / 2 - Rules.Hud.StatisticsWindow.CROSSHAIR_VALUE_Y_MARGIN - 175);
            }
        }

    }

    public class PauseWindow extends SlidingWindow {

        private HUDbutton soundButton;
        private HUDbutton exitButton;
        private HUDbutton helpButton;
        private Window popup;
        private HelpWindow helpPopup;
        private Vector3 auxVector = new Vector3();

        public PauseWindow(Skin skin) {
            super(skin);
            setWidth(Rules.Hud.PauseWindow.WIDTH);
            setHeight(Rules.Hud.PauseWindow.HEIGHT);
            createButtons();
            setVisible(false);
        }

        @Override
        public void toFront() {
            super.toFront();
            soundButton.toFront();
            exitButton.toFront();
            helpButton.toFront();
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            if (speed != 0) {
                return;
            }
            auxVector.set(Gdx.input.getX(), Gdx.input.getY(), 0);
            warScreen.getHudCamera().unproject(auxVector);
        }

        private void createButtons() {
            exitButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_EXIT, Assets.GFX.Sheets.ImagesNames.BUTTON_EXIT_PRESSED, null, null, getX(), getY(), new ButtonClick() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    super.changed(event, actor);
                    ask(Assets.Strings.InGameMessages.EXIT_MESSAGE, new ButtonClick() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            super.changed(event, actor);
                            pauseWindow.setVisible(false);
                            statisticsWindow.setVisible(false);
                            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
                            PlayerStats oldStats = warScreen.getOldStats();
                            if (oldStats != null) {
                                playerStatsHandler.setPlayerStats(oldStats);
                            }
                            warScreen.endBattle();
                        }
                    });
                }
            }, false);
            helpButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_HELP, Assets.GFX.Sheets.ImagesNames.BUTTON_HELP_PRESSED, null, null, getX(), getY(), new ButtonClick() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    super.changed(event, actor);
                    helpPopup = new HelpWindow(windowSkin);
                    helpPopup.setSize(Rules.Hud.PauseWindow.HelpPopup.WIDTH, Rules.Hud.PauseWindow.HelpPopup.HEIGHT);
                    helpPopup.enableVerifyButton(new ButtonClick() {
                        @Override
                        public void changed(ChangeEvent event, Actor actor) {
                            super.changed(event, actor);
                            helpPopup.setVisible(false);
                        }
                    });
                    addActor(helpPopup);
                }
            }, false);
            String speakerImage = (GameSettings.SOUND_TOGGLE) ? Assets.GFX.Sheets.ImagesNames.BUTTON_SPEAKER_ON : Assets.GFX.Sheets.ImagesNames.BUTTON_SPEAKER_OFF;
            soundButton = createButton(speakerImage, speakerImage + Assets.GFX.Sheets.ImagesNames.PRESSED, null, null, getX(), getY(), new ButtonClick() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    Parastrike.getSoundPlayer().toggleSound(!GameSettings.SOUND_TOGGLE);
                    String speakerImage = (GameSettings.SOUND_TOGGLE) ? Assets.GFX.Sheets.ImagesNames.BUTTON_SPEAKER_ON : Assets.GFX.Sheets.ImagesNames.BUTTON_SPEAKER_OFF;
                    soundButton.getStyle().up = buttonsSkin.getDrawable(speakerImage);
                    soundButton.getStyle().down = buttonsSkin.getDrawable(speakerImage + Assets.GFX.Sheets.ImagesNames.PRESSED);
                    super.changed(event, actor);
                }
            }, false);
            exitButton.setPosition(getX() - exitButton.getWidth() / 2 - getWidth() / 4, getY() - exitButton.getHeight() / 2);
            helpButton.setPosition(getX() - helpButton.getWidth() / 2, getY() - soundButton.getHeight() / 2);
            soundButton.setPosition(getX() - soundButton.getWidth() / 2 + getWidth() / 4, getY() - soundButton.getHeight() / 2);
        }

        private void ask(String message, ChangeListener action) {
            popup = new Window(windowSkin, message);
            popup.setSize(Rules.Hud.PauseWindow.ExitPopup.WIDTH, Rules.Hud.PauseWindow.ExitPopup.HEIGHT);
            addActor(popup);
            popup.enableVerifyButton(action);
            popup.enableCancelButton();
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            exitButton.setVisible(visible);
            helpButton.setVisible(visible);
            soundButton.setVisible(visible);
            if (popup != null) {
                popup.setVisible(visible);
            }
            if (helpPopup != null) {
                helpPopup.setVisible(visible);
            }
        }

        @Override
        public void moveBy(float x, float y) {
            super.moveBy(x, y);
            exitButton.moveBy(x, y);
            helpButton.moveBy(x, y);
            soundButton.moveBy(x, y);
        }

        @Override
        public void setState(boolean state) {
            com.gadarts.parashoot.level_model.Level currentLevel = warScreen.getElements().getCurrentLevel();
            boolean fade = !(currentLevel.getState() == com.gadarts.parashoot.level_model.Level.States.ACCOMPLISHED || currentLevel.getState() == com.gadarts.parashoot.level_model.Level.States.GAME_OVER);
            super.setState(state, fade);
            if (!state) {
                if (popup != null) {
                    popup.remove();
                }
                if (helpPopup != null) {
                    helpPopup.remove();
                }
            }
        }

        private class HelpWindow extends Window {

            private TextureRegion helpRegion;

            public HelpWindow(Skin skin) {
                super(skin);
                helpRegion = skin.getRegion(Assets.GFX.Sheets.ImagesNames.HELP_INGAME);
            }

            @Override
            public void draw(Batch batch, float parentAlpha) {
                super.draw(batch, parentAlpha);
                batch.draw(helpRegion, getX() - helpRegion.getRegionWidth() / 2, getY() + getHeight() / 2 - helpRegion.getRegionHeight() - Rules.Hud.PauseWindow.HelpPopup.REGION_MARGIN_TOP);
            }

        }
    }

    private class Window extends Actor {

        private final Skin SKIN;
        protected final TextureAtlas.AtlasRegion EDGE_REGION, HORIZONTAL_REGION, VERTICAL_REGION, INSIDE_REGION, HOLDER_REGION;
        private final String message;
        protected HUDbutton verifyButton;
        private HUDbutton cancelButton;

        public Window(Skin skin) {
            this.message = null;
            SKIN = skin;
            EDGE_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.EDGE);
            HORIZONTAL_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.HORIZONTAL);
            VERTICAL_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.VERTICAL);
            INSIDE_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.INSIDE);
            HOLDER_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.HOLDER);
            setX(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2);
            setY(Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2);
        }

        public Window(Skin skin, String message) {
            this.message = message;
            SKIN = skin;
            EDGE_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.EDGE);
            HORIZONTAL_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.HORIZONTAL);
            VERTICAL_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.VERTICAL);
            INSIDE_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.INSIDE);
            HOLDER_REGION = (TextureAtlas.AtlasRegion) getSkin().getRegion(Assets.GFX.Sheets.ImagesNames.HOLDER);
            setX(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2);
            setY(Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2);
        }

        @Override
        public void setVisible(boolean visible) {
            super.setVisible(visible);
            if (verifyButton != null) {
                verifyButton.setVisible(visible);
            }
            if (cancelButton != null) {
                cancelButton.setVisible(visible);
            }
        }

        @Override
        public boolean remove() {
            if (verifyButton != null) {
                verifyButton.remove();
            }
            if (cancelButton != null) {
                cancelButton.remove();
            }
            return super.remove();
        }

        @Override
        public void draw(Batch batch, float parentAlpha) {
            if (!isVisible()) {
                return;
            }
            super.draw(batch, parentAlpha);
            int tileHeight = INSIDE_REGION.getRegionHeight();
            int tileWidth = INSIDE_REGION.getRegionWidth();
            batch.draw(INSIDE_REGION, getX() - getWidth() / 2, getY() - getHeight() / 2, getWidth(), getHeight());
            batch.draw(EDGE_REGION, getX() - getWidth() / 2 - tileWidth / 2, getY() + getHeight() / 2 - tileHeight / 2);
            batch.draw(EDGE_REGION, getX() + getWidth() / 2 - tileWidth / 2, getY() + getHeight() / 2 - tileHeight / 2);
            batch.draw(EDGE_REGION, getX() + getWidth() / 2 - tileWidth / 2, getY() - getHeight() / 2 - tileHeight / 2);
            batch.draw(EDGE_REGION, getX() - getWidth() / 2 - tileWidth / 2, getY() - getHeight() / 2 - tileHeight / 2);
            batch.draw(HORIZONTAL_REGION, getX() - getWidth() / 2 + (tileWidth / 2), getY() + getHeight() / 2 - tileHeight / 2, getWidth() - tileWidth, tileHeight);
            batch.draw(HORIZONTAL_REGION, getX() - getWidth() / 2 + (tileWidth / 2), getY() - getHeight() / 2 - tileHeight / 2, getWidth() - tileWidth, tileHeight);
            batch.draw(VERTICAL_REGION, getX() - getWidth() / 2 - tileWidth / 2, getY() - getHeight() / 2 + tileHeight / 2, tileWidth, getHeight() - tileHeight);
            batch.draw(VERTICAL_REGION, getX() + getWidth() / 2 - tileWidth / 2, getY() - getHeight() / 2 + tileHeight / 2, tileWidth, getHeight() - tileHeight);
            if (message != null) {
                fontLayout.setText(regularFont, message);
                regularFont.setColor(Color.RED);
                regularFont.draw(getBatch(), message, getX() - Rules.Hud.PauseWindow.ExitPopup.WIDTH / 2 + Rules.Hud.PauseWindow.ExitPopup.PADDING, getY() + Rules.Hud.PauseWindow.ExitPopup.TEXT_Y_BOTTOM_MARGIN, Rules.Hud.PauseWindow.ExitPopup.WIDTH - 2 * Rules.Hud.PauseWindow.ExitPopup.PADDING, Align.center, true);
            }
        }

        public Skin getSkin() {
            return SKIN;
        }

        public String getMessage() {
            return message;
        }

        public void enableVerifyButton(ChangeListener action) {
            verifyButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK, Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK_PRESSED, null, null, 0, 0, null, false);
            verifyButton.toFront();
            verifyButton.setVisible(true);
            verifyButton.setPosition(getX() - verifyButton.getWidth() / 2 - getWidth() / 4, getY() - (3 * verifyButton.getHeight() / 4));
            verifyButton.addListener(action);
        }

        public void enableVerifyButton(ChangeListener action, float x, float y) {
            verifyButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK, Assets.GFX.Sheets.ImagesNames.BUTTON_CHECK_PRESSED, null, null, 0, 0, null, false);
            verifyButton.toFront();
            verifyButton.setVisible(true);
            verifyButton.setPosition(x, y);
            verifyButton.addListener(action);
        }

        public void enableCancelButton() {
            cancelButton = createButton(Assets.GFX.Sheets.ImagesNames.BUTTON_CANCEL, Assets.GFX.Sheets.ImagesNames.BUTTON_CANCEL_PRESSED, null, null, 0, 0, null, false);
            cancelButton.toFront();
            cancelButton.setVisible(true);
            cancelButton.setPosition(getX() - cancelButton.getWidth() / 2 + getWidth() / 4, getY() - (3 * cancelButton.getHeight() / 4));
            cancelButton.addListener(new ButtonClick() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    super.changed(event, actor);
                    cancelButton.setVisible(false);
                    verifyButton.setVisible(false);
                    Window.this.remove();
                }
            });
        }

    }

}
