package com.gadarts.parashoot.misc.effects;

import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.model.BaseGameObject;
import com.gadarts.parashoot.model.GameObject;

public class Light extends BaseGameObject implements Pool.Poolable {
    private GameObject parent;
    private boolean free;

    private String region;

    public void init(GameObject parent, String region) {
        this.parent = parent;
        this.free = false;
        this.region = region;
    }

    @Override
    public void reset() {

    }

    public String getRegion() {
        return region;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public GameObject getParent() {
        return parent;
    }

    public void free() {
        if (!free) {
            Pools.free(this);
            free = true;
        }
    }

}
