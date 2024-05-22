package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.TimeUtils;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 01/08/2015.
 */
public class MessageDisplay {
    private final boolean allowed;
    private boolean fadeAway;
    private Queue<String> messages = new Queue<String>();
    private String currentMessage;
    private float messageAlpha = 1;
    private Timer scheduler = new Timer();
    private double fadeAwayPace = Rules.Hud.Monitor.MESSAGE_FADE_AWAY_PACE;
    private Queue<SFX.Taunts> tauntsQueue = new Queue<SFX.Taunts>();
    private long lastPlayedTaunt;

    public MessageDisplay() {
        allowed = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.MESSAGES, true);
    }

    public void add(String message) {
        if (!allowed) return;
        if (messages.size == 0) {
            setCurrentMessage(message);
        }
        messages.addLast(message);
    }

    private void setCurrentMessage(String message) {
        if (!allowed) return;
        currentMessage = message;
        scheduleTask(TASK_FADE_AWAY, Rules.Hud.Monitor.MESSAGE_TIME);
    }

    public String getCurrentMessage() {
        return currentMessage;
    }

    protected void scheduleTask(Timer.Task task, float timing) {
        if (!allowed) return;
        if (!task.isScheduled()) {
            scheduler.scheduleTask(task, timing);
        }
    }

    private void setNextMessage() {
        if (!allowed) return;
        messages.removeValue(currentMessage, false);
        currentMessage = null;
        messageAlpha = 1;
        fadeAway = false;
        if (messages.size > 0) {
            setCurrentMessage(messages.first());
        }
    }

    private final Timer.Task TASK_FADE_AWAY = new Timer.Task() {
        @Override
        public void run() {
            fadeAway = true;
            if (messages.size > 1) {
                fadeAwayPace = Rules.Hud.Monitor.MESSAGE_FADE_AWAY_PACE_FAST;
            } else {
                fadeAwayPace = Rules.Hud.Monitor.MESSAGE_FADE_AWAY_PACE;
            }
        }
    };

    public void update() {
        if (!allowed) return;
        if (fadeAway) {
            if (messageAlpha > 0) {
                if (messageAlpha - fadeAwayPace > 0) {
                    messageAlpha -= fadeAwayPace;
                } else {
                    messageAlpha = 0;
                }
            } else {
                setNextMessage();
            }
        }
        if (tauntsQueue.size > 0) {
            if (TimeUtils.millis() - lastPlayedTaunt > Rules.Hud.Monitor.TAUNT_INTERVAL) {
                Parastrike.getSoundPlayer().playSound(tauntsQueue.removeFirst(), false, false);
                lastPlayedTaunt = TimeUtils.millis();
            }
        }
    }

    public float getMessageAlpha() {
        return messageAlpha;
    }

    public void taunt(SFX.Taunts taunt) {
        if (!allowed) return;
        tauntsQueue.addFirst(taunt);
    }

}
