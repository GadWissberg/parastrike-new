package com.gadarts.parashoot.model.tutorial;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.GameCharacter;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

public class Tutor extends WidgetGroup {
    private Image circle;
    private Image arrow;
    private Label label;
    private Image finger;
    private GameCharacter inGameTarget;

    Image getFinger() {
        if (finger == null) {
            TextureAtlas textureAtlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.HUD_DATA_FILE, TextureAtlas.class);
            finger = new Image(textureAtlas.findRegion(Assets.GFX.Sheets.ImagesNames.TUTORIAL_POINT));
            addActor(finger);
        }
        return finger;
    }

    public Label getLabel() {
        if (label == null) {
            createLabel();
        }
        return label;
    }

    private void createLabel() {
        BitmapFont font = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.SMALL);
        label = new Label(null, new Label.LabelStyle(font, Color.GOLD));
        label.setAlignment(Align.center);
        addActor(label);
    }

    public void setInGameTarget(GameCharacter inGameTarget) {
        this.inGameTarget = inGameTarget;
        initializeWidgetsForInGameTarget(inGameTarget);
    }

    private final Timer.Task TASK_DISAPPEAR = new Timer.Task() {
        @Override
        public void run() {
            remove();
        }
    };

    private void initializeWidgetsForInGameTarget(GameCharacter inGameTarget) {
        float targetX = inGameTarget.getCenterX();
        float targetY = inGameTarget.getCenterY();
        int directionToCenter = (int) GameUtils.getDirectionToPoint(targetX, targetY, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2);
        double cos = Math.cos(Math.toRadians(directionToCenter));
        double sin = Math.sin(Math.toRadians(directionToCenter));
        setPositionAndRotationForWidgetsInGameTarget(targetX, targetY, directionToCenter, cos, sin);
    }

    private void setPositionAndRotationForWidgetsInGameTarget(float targetX, float targetY, int directionToCenter, double cos, double sin) {
        initializeCircleForInGameTarget(targetX, targetY, cos, sin);
        initializeArrowForInGameTarget(targetX, targetY, directionToCenter, cos, sin);
        setLabelAlignment(directionToCenter);
        Image finger = getFinger();
        finger.setPosition(targetX - finger.getWidth() / 2, targetY - (finger.getHeight() / 2) - (circle.getHeight() / 2));
        finger.toFront();
    }

    private void initializeArrowForInGameTarget(float targetX, float targetY, int directionToCenter, double cos, double sin) {
        Image arrow = getArrow();
        Image circle = getCircle();
        arrow.setRotation(directionToCenter + 180);
        arrow.setPosition((float) (targetX + (cos * circle.getWidth() / 2) - arrow.getWidth() / 2), (float) (targetY + (sin * circle.getHeight() / 2) - arrow.getHeight() / 2));
    }

    private void initializeCircleForInGameTarget(float targetX, float targetY, double cos, double sin) {
        Image circle = getCircle();
        getLabel().setPosition((float) (targetX + (cos * circle.getWidth())), (float) (targetY + (sin * circle.getHeight())));
        circle.setPosition(targetX - circle.getWidth() / 2, targetY - circle.getHeight() / 2);
    }

    private void setLabelAlignment(int directionToCenter) {
        Label label = getLabel();
        if (directionToCenter >= 135 && directionToCenter <= 215) {
            label.setAlignment(Align.right);
        } else if (directionToCenter <= 45 || directionToCenter >= 315) {
            label.setAlignment(Align.left);
        } else label.setAlignment(Align.center);
    }

    private Image getCircle() {
        if (circle == null) {
            TextureAtlas textureAtlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.HUD_DATA_FILE, TextureAtlas.class);
            circle = new Image(textureAtlas.findRegion(Assets.GFX.Sheets.ImagesNames.TUTORIAL_CIRCLE));
            addActor(circle);
        }
        return circle;
    }

    private Image getArrow() {
        if (arrow == null) {
            TextureAtlas textureAtlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.HUD_DATA_FILE, TextureAtlas.class);
            arrow = new Image(textureAtlas.findRegion(Assets.GFX.Sheets.ImagesNames.TUTORIAL_ARROW));
            arrow.setOrigin(arrow.getWidth() / 2, arrow.getHeight() / 2);
            addActor(arrow);
        }
        return arrow;
    }


    void timeToDisappear(int duration) {
        Timer.instance().scheduleTask(TASK_DISAPPEAR, duration);
    }

    public enum TutorType {MESSAGE, IN_GAME_FOCUS, MENU_FOCUS, SWIPE_ANIMATION}

}
