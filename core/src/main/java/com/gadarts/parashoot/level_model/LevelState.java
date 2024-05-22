package com.gadarts.parashoot.level_model;

public enum LevelState {
    LOCKED, NO_STARS, ONE_STAR, TWO_STARS, COMPLETED;

    public static LevelSkill toLevelSkill(LevelState levelState) {
        LevelSkill result;
        switch (levelState) {
            case NO_STARS:
                result = LevelSkill.EASY;
                break;

            case ONE_STAR:
                result = LevelSkill.MEDIUM;
                break;

            case TWO_STARS:
            default:
                result = LevelSkill.HARD;
                break;
        }
        return result;
    }
}
