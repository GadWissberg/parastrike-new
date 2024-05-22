package com.gadarts.parashoot.misc.effects;

import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.model.GameObject;

/**
 * I would have extended PooledEffect, but PooledEffect's constructor isn't public!
 */
public class ParticleWrapper {

    private GameObject parent;
    private ParticleEffectPool.PooledEffect effect;
    private float originX, originY;
    private String name;
    private boolean turnedOffContinuous;
    private boolean free;

    public void init(ParticleEffectPool.PooledEffect effect, String effectName, GameObject parent) {
        init(effect, effectName, parent, 0, 0);
    }

    public void init(ParticleEffectPool.PooledEffect effect, String effectName, GameObject parent, float originX, float originY) {
        effect.reset();
        this.originX = originX;
        this.originY = originY;
        this.parent = parent;
        this.effect = effect;
        this.effect.setPosition(parent.getX(), parent.getY());
        this.free = false;
        turnedOffContinuous = false;
        name = effectName;
    }

    public ParticleEffectPool.PooledEffect getEffect() {
        return effect;
    }

    public GameObject getParent() {
        return parent;
    }

    public void destroy() {
        if (turnedOffContinuous) {
            setContinuous(true);
        }
        effect.free();
        if (!free) {
            Pools.free(this);
            free = true;
        }
    }

    public float getOriginX() {
        return originX;
    }

    public float getOriginY() {
        return originY;
    }

    public boolean isComplete() {
        return effect.isComplete();
    }

    public void allowCompletion() {
        effect.allowCompletion();
    }

    public String getName() {
        return name;
    }

    public void setOrigin(float x, float y) {
        originX = x;
        originY = y;
    }

    public void setContinuous(boolean continuous) {
        Array<ParticleEmitter> emitters = effect.getEmitters();
        for (int i = 0; i < emitters.size; i++) {
            ParticleEmitter emitter = emitters.get(i);
            emitter.setContinuous(continuous);
        }
        if (!continuous) {
            turnedOffContinuous = true;
        }
    }
}
