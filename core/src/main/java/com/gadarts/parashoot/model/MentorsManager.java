package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.gadarts.parashoot.assets.Assets;

public class MentorsManager {
    private final Preferences mentors;


    public enum MentorState {DID_NOT_RUN, READY, FINISHED}

    public MentorsManager() {
        mentors = Gdx.app.getPreferences(Assets.Configs.Preferences.Mentors.PREF_MENTORS);
    }


    public MentorState getMentorState(String mentorName) {
        int stateIndex = mentors.getInteger(mentorName, MentorState.DID_NOT_RUN.ordinal());
        return MentorState.values()[stateIndex];
    }

    public void setMentorState(String mentorName, MentorState state) {
        int current = mentors.getInteger(mentorName, MentorState.DID_NOT_RUN.ordinal());
        if (current != state.ordinal()) {
            mentors.putInteger(mentorName, state.ordinal());
            mentors.flush();
        }
    }

    public void readyMentorIfDidntRun(String mentorName) {
        int current = mentors.getInteger(mentorName, MentorState.DID_NOT_RUN.ordinal());
        if (current == MentorState.DID_NOT_RUN.ordinal()) {
            setMentorState(mentorName, MentorState.READY);
        }
    }
}
