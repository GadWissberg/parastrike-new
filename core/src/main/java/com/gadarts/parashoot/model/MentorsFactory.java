package com.gadarts.parashoot.model;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Touchable;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.DelayAction;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Align;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.AssetManagerWrapper;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.utils.Rules;

import static com.badlogic.gdx.graphics.Pixmap.Format.RGB888;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.PADDING_LEFT;
import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.PADDING_RIGHT;
import static com.gadarts.parashoot.utils.Rules.System.FontsParameters.RegularFontNames.MEDIUM;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.WIDTH_TARGET_RESOLUTION;

public class MentorsFactory {
    private Label.LabelStyle mentorsLabelStyle;
    private Pixmap currentLabelPix;
    private Texture currentLabelColorTexture;

    public MentorsFactory() {
        AssetManagerWrapper assetsManager = Parastrike.getAssetsManager();
        BitmapFont font = assetsManager.get(MEDIUM, BitmapFont.class);
        mentorsLabelStyle = new Label.LabelStyle(font, Color.GOLD);
    }

    public Label createMentor(String message) {
        Label label = new Label(message, mentorsLabelStyle);
        label.setTouchable(Touchable.disabled);
        setLabelAttributes(label);
        createLabelBackground(label);
        setMessageMentorActions(label);
        return label;
    }

    private void setMessageMentorActions(Label label) {
        float insideY = HEIGHT_TARGET_RESOLUTION - label.getHeight();
        MoveToAction enter = Actions.moveTo(0, insideY, 1, Interpolation.bounceIn);
        MoveToAction leave = Actions.moveTo(0,
                HEIGHT_TARGET_RESOLUTION,
                2,
                Interpolation.circleOut);
        DelayAction wait = Actions.delay(Rules.Menu.Mentors.WAIT_TIME, leave);
        label.addAction(Actions.sequence(enter, wait));
    }

    private void setLabelAttributes(Label label) {
        label.setWidth(WIDTH_TARGET_RESOLUTION - PADDING_RIGHT);
        label.setAlignment(Align.center);
        label.setWrap(true);
        float prefHeight = label.getPrefHeight();
        label.setHeight(prefHeight);
        label.setPosition(PADDING_LEFT, HEIGHT_TARGET_RESOLUTION);
    }

    private void createLabelBackground(Label label) {
        float height = label.getHeight();
        currentLabelPix = new Pixmap((int) label.getPrefWidth(), (int) height, RGB888);
        currentLabelPix.setColor(Color.BLACK);
        currentLabelPix.fill();
        currentLabelColorTexture = new Texture(currentLabelPix);
        label.getStyle().background = new Image(currentLabelColorTexture).getDrawable();
    }


    public void dispose() {
        if (currentLabelPix != null) {
            currentLabelPix.dispose();
        }
        if (currentLabelColorTexture != null) {
            currentLabelColorTexture.dispose();
        }
    }

    public Image createMentor(Actor actor, Skin skin) {
        Image image = initializeMentor(skin, Assets.GFX.Sheets.ImagesNames.MENTOR_POINTER);
        Vector2 actorStagePosition = actor.localToStageCoordinates(new Vector2());
        float x = actorStagePosition.x + actor.getWidth() / 2 - image.getPrefWidth() / 2;
        float imageHeight = image.getHeight();
        image.setPosition(x, -imageHeight);
        float yTarget = actorStagePosition.y - imageHeight / 3;
        setPointerMentorActions(image, x, yTarget);
        return image;
    }

    public Image createMentor(SequenceAction actions, Skin skin) {
        Image image = initializeMentor(skin, Assets.GFX.Sheets.ImagesNames.MENTOR_SWIPE);
        image.addAction(actions);
        return image;
    }

    private Image initializeMentor(Skin skin, String mentorSwipe) {
        Image image = new Image(skin.getDrawable(mentorSwipe));
        image.setTouchable(Touchable.disabled);
        return image;
    }

    private void setPointerMentorActions(Image image, float x, float y) {
        MoveToAction enter = Actions.moveTo(x, y, 1, Interpolation.circle);
        MoveToAction leave = Actions.moveTo(x, -image.getHeight(), 2, Interpolation.bounce);
        DelayAction wait = Actions.delay(Rules.Menu.Mentors.WAIT_TIME, leave);
        image.addAction(Actions.sequence(enter, wait));
    }
}
