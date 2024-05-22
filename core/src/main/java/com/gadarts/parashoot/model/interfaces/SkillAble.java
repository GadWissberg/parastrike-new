package com.gadarts.parashoot.model.interfaces;

public interface SkillAble {
    int getCoinsBySkill();

    float getSpeedBySkill();

    float getHealthBySkill();

    float getDamageBySkill(int weapon);

    float getSpecificBySkill();
}
