package com.gadarts.parashoot.model.tutorial;

import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.MoveToAction;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.WidgetGroup;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.JsonValue;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.utils.Rules;

public class Guide extends WidgetGroup {


    public Tutor createTutor(Tutor.TutorType type, JsonValue parameters) {
        final Tutor tutor = new Tutor();
        initializeTutor(type, parameters, tutor);
        addActor(tutor);
        return tutor;
    }

    private void initializeTutor(Tutor.TutorType type, JsonValue parameters, Tutor tutor) {
        if (type == Tutor.TutorType.SWIPE_ANIMATION) {
            initializeSwipeAnimation(parameters, tutor);
        } else if (type == Tutor.TutorType.MESSAGE) {
            initializeMessage(parameters, tutor);
        } else if (type == Tutor.TutorType.IN_GAME_FOCUS) {
            initializeInGameFocus(parameters, tutor);
        }
    }

    private void initializeInGameFocus(JsonValue parameters, Tutor tutor) {
        Label label = tutor.getLabel();
        label.setText(parameters.getString(Assets.InGameGuides.Focus.MESSAGE, null));
        label.setPosition(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Assets.InGameGuides.Animation.MESSAGE_Y);
        boolean showFinger = parameters.getBoolean(Assets.InGameGuides.Focus.SHOW_FINGER, false);
        tutor.getFinger().setVisible(showFinger);
    }

    private void initializeMessage(JsonValue parameters, Tutor tutor) {
        Label label = tutor.getLabel();
        label.setText(parameters.getString(Assets.InGameGuides.Animation.MESSAGE, null));
        label.setPosition(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Assets.InGameGuides.Animation.MESSAGE_Y);
        tutor.timeToDisappear(Assets.InGameGuides.MESSAGE_DURATION);
    }

    private void initializeSwipeAnimation(JsonValue parameters, Tutor tutor) {
        createSwipeActions(parameters, tutor);
        String message = parameters.getString(Assets.InGameGuides.Animation.MESSAGE, null);
        setLabelForSwipeAnimation(tutor, message);
    }

    private void createSwipeActions(JsonValue parameters, Tutor tutor) {
        final Image finger = tutor.getFinger();
        MoveToAction actionBackward = createActionBackward(parameters);
        MoveToAction actionForward = createActionForward(parameters);
        finger.setPosition(actionBackward.getX(), actionBackward.getY());
        SequenceAction animation = createAnimationForSwipe(actionBackward, actionForward, tutor);
        finger.addAction(animation);
    }

    private void setLabelForSwipeAnimation(Tutor tutor, String message) {
        final Label label = tutor.getLabel();
        label.setText(message);
        label.setAlignment(Align.center);
        label.setPosition(Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Assets.InGameGuides.Animation.MESSAGE_Y);
    }

    private SequenceAction createAnimationForSwipe(MoveToAction actionBackward, MoveToAction actionForward, final Tutor tutor) {
        SequenceAction move = new SequenceAction(actionForward, actionBackward, actionForward, actionBackward);
        return new SequenceAction(Actions.repeat(2, move), new Action() {
            @Override
            public boolean act(float delta) {
                tutor.remove();
                return true;
            }
        });
    }

    private MoveToAction createActionBackward(JsonValue parameters) {
        int startX = parameters.getInt(Assets.InGameGuides.Animation.START_X, 0);
        int startY = parameters.getInt(Assets.InGameGuides.Animation.START_Y, 0);
        return Actions.moveTo(startX, startY, 3, Interpolation.smooth);
    }

    private MoveToAction createActionForward(JsonValue parameters) {
        int endX = parameters.getInt(Assets.InGameGuides.Animation.END_X, 0);
        int endY = parameters.getInt(Assets.InGameGuides.Animation.END_Y, 0);
        return Actions.moveTo(endX, endY, 3, Interpolation.smooth);
    }
}
