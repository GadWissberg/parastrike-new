package com.gadarts.parashoot.model;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.misc.effects.ParticleWrapper;
import com.gadarts.parashoot.utils.Rules;

/**
 * Life cycle: Creation (Constructor) > Update (onUpdate()) > Critical Damage (onDamageCritical()) > Death (onDeath()/onGib()) > Destroy (onDestroy()).
 */
public abstract class GameCharacter extends CollideableGameObject {

    protected Float health;
    protected boolean dead;
    protected float gibReach;
    private boolean shieldVisibility;
    private TextureRegion shieldRegion;
    private float damageCriticalReach;
    protected int lastDamagedBy;
    protected ParticleWrapper myDamageSmoke;
    private boolean shieldActive;
    private float shieldRelativeX;
    private float shieldRelativeY;

    public GameCharacter() {
        super();
    }

    protected void init(String spriteName, float x, float y, float direction, float speed, float hp, WarScreenElements mechanics) {
        super.init(spriteName, x, y, direction, speed, mechanics);
        health = hp;
        setDeadFlag(false);
        lastDamagedBy = Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.REGULAR;
        damageCriticalReach = health / 2;
        myDamageSmoke = null;
    }

    public boolean getShieldVisibility() {
        return shieldVisibility;
    }

    public void setShieldVisibility(boolean shieldVisibility) {
        this.shieldVisibility = shieldVisibility;
    }

    public TextureRegion getShieldRegion() {
        return shieldRegion;
    }

    public void setShieldRegion(TextureAtlas.AtlasRegion shieldRegion) {
        this.shieldRegion = shieldRegion;
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        super.onDraw(batch);
        if (isShieldActive() && isShowingShield()) {
            drawShield(batch);
        }
    }

    protected void drawShield(SpriteBatch batch) {
        float shieldX = getX() + getShieldRelativeX();
        float shieldY = getY() + getShieldRelativeY();
        int shieldWidth = shieldRegion.getRegionWidth();
        int shieldHeight = shieldRegion.getRegionHeight();
        batch.draw(getShieldRegion(), shieldX, shieldY, 0, 0, shieldWidth, shieldHeight, 1, 1, 0);
    }

    protected void setShieldRelativePosition(float x, float y) {
        shieldRelativeX = x;
        shieldRelativeY = y;
    }

    protected void createDamageSmoke() {
        if (myDamageSmoke == null) {
            myDamageSmoke = getElements().getEffectsManager().createEffect(Assets.Configs.ParticleEffects.DAMAGE_SMOKE, this, width / 2, 0);
        }
    }

    /**
     * Do a sequence of actions before you call disappear(). You have to manually call disappear(). This method is called only once.
     */
    protected void onDeath() {
        setDeadFlag(true);
    }

    protected void setDeadFlag(boolean state) {
        dead = state;
    }

    private void onDamageCritical() {
        createDamageSmoke();
    }

    public void setHealth(float newValue) {
        health = newValue;
        checkHealth();
    }

    public boolean isDead() {
        return dead;
    }

    protected void onGib() {
        setDeadFlag(true);
        onDestroy(false);
    }

    public void changeHealth(float delta) {
        changeHealth(delta, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.DamageEffect.REGULAR);
    }

    public void changeHealth(float delta, int damageType) {
        changeHealth(delta, damageType, Rules.System.GFX.ShadesIds.WHITE_SHADE);
    }

    public void changeHealth(float delta, int damageType, int shade) {
        if (shade != Rules.System.GFX.ShadesIds.NO_SHADE) {
            shadePain(shade);
        }
        if (health != null) {
            health += delta;
            lastDamagedBy = damageType;
            checkHealth();
        }
    }

    private void checkHealth() {
        if (health <= 0) {
            if (health > gibReach) {
                if (!dead) {
                    onDeath();
                }
            } else {
                onGib();
            }
        } else {
            checkDamageCritical();
        }
    }

    protected void checkDamageCritical() {
        if (health <= damageCriticalReach) {
            onDamageCritical();
        } else {
            if (myDamageSmoke != null) {
                myDamageSmoke.destroy();
                myDamageSmoke = null;
            }
        }
    }

    private void shadePain(int shade) {
        this.shade = shade;
    }

    public Float getHealth() {
        return health;
    }

    public void setShieldActive(boolean shieldActive) {
        this.shieldActive = shieldActive;
    }

    public boolean isShieldActive() {
        return shieldActive;
    }

    public boolean isShowingShield() {
        return shieldVisibility;
    }

    public float getShieldRelativeX() {
        return shieldRelativeX;
    }

    public float getShieldRelativeY() {
        return shieldRelativeY;
    }
}
