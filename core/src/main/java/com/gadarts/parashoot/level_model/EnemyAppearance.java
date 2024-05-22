package com.gadarts.parashoot.level_model;

import com.badlogic.gdx.utils.JsonValue;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;

public class EnemyAppearance {

    private final EnemyFactory.EnemyType type;

    private final int timing;
    private final Alignment alignment;
    private final LevelSkill minimumSkill;
    private JsonValue focusParameters;

    public EnemyAppearance(EnemyFactory.EnemyType type, int timing, Alignment alignment, LevelSkill minimumSkill) {
        this.type = type;
        this.timing = timing;
        this.alignment = alignment;
        this.minimumSkill = minimumSkill;
    }

    public Alignment getAlignment() {
        return alignment;
    }

    LevelSkill getMinimumSkill() {
        return minimumSkill;
    }

     JsonValue getFocusParameters() {
        return focusParameters;
    }

    public void setFocusParameters(JsonValue focusParameters) {
        this.focusParameters = focusParameters;
    }

    public enum Alignment {LEFT, RIGHT, RANDOM}

    public EnemyFactory.EnemyType getType() {
        return type;
    }

    public int getTiming() {
        return timing;
    }

}
