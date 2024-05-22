package com.gadarts.parashoot.level_model;

import com.badlogic.gdx.utils.JsonValue;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.HUD;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.model.tutorial.Guide;
import com.gadarts.parashoot.model.tutorial.Tutor;
public class Event {

    private final int timing;
    private final Action action;
    private final JsonValue actionParameters;

    public enum Action {TOUCH_LOCK, TOUCH_UNLOCK, FOCUS, ANIMATION, MESSAGE}

    public enum FocusTarget {PLAYER, PARATROOPER}

    public Event(int timing, Action action, JsonValue actionParameters) {
        this.timing = timing;
        this.action = action;
        this.actionParameters = actionParameters;
    }

    void performEventAction(WarScreenElements elements, HUD hud) {
        if (action == null) {
            return;
        }
        Guide guide = hud.getGuide();
        switch (action) {
            case TOUCH_LOCK:
                elements.setTouchLocked(true);
                break;
            case TOUCH_UNLOCK:
                elements.setTouchLocked(false);
                break;
            case FOCUS:
                Tutor tutor = guide.createTutor(Tutor.TutorType.IN_GAME_FOCUS, actionParameters);
                hud.setTutorTarget(tutor, Event.FocusTarget.valueOf(actionParameters.getString(Assets.InGameGuides.Focus.TARGET, Event.FocusTarget.PLAYER.name())));
                elements.pauseGame(false);
                break;
            case ANIMATION:
                guide.createTutor(Tutor.TutorType.SWIPE_ANIMATION, actionParameters);
                break;
            case MESSAGE:
                guide.createTutor(Tutor.TutorType.MESSAGE, actionParameters);
                break;
        }
    }

    public int getTiming() {
        return timing;
    }
}
