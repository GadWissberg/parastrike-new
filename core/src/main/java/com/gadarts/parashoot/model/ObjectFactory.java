package com.gadarts.parashoot.model;

import com.gadarts.parashoot.misc.effects.Light;
import com.gadarts.parashoot.misc.effects.ParticleWrapper;

/**
 * Created by gadw1_000 on 10-Jun-15.
 */
public abstract class ObjectFactory {
    private WarScreenElements elements;

    private final Factories factories;

    private final PlayerHandler playerHandler;

    public ObjectFactory(Factories factories, PlayerHandler playerHandler) {
        this.factories = factories;
        this.playerHandler = playerHandler;
    }

    public void setElements(WarScreenElements elements) {
        this.elements = elements;
    }

    public ParticleWrapper createEffect(String effectName, GameObject parent) {
        return createEffect(effectName, parent, 0, 0);
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public Factories getFactories() {
        return factories;
    }

    public ParticleWrapper createEffect(String effectName, GameObject parent, float originX, float originY) {
        return elements.getEffectsManager().createEffect(effectName, parent, originX, originY);
    }

    public WarScreenElements getWarScreenElements() {
        return elements;
    }

    public Light createLight(GameObject parent) {
        return elements.getEffectsManager().createLight(parent);
    }

    public Light createLight(GameObject parent, String region) {
        EffectsManager effectsManager = elements.getEffectsManager();
        return effectsManager.createLight(parent, region, 0, 0, 0, 0);
    }

}

