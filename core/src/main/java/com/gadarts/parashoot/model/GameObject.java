package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.model.interfaces.LifeCycle;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

import java.util.HashMap;
import java.util.Random;

/**
 * Life cycle: Creation (Constructor) > Update (onUpdate()) > Destroy (onDestroy(doDestroySequence)).
 */
public class GameObject extends BaseGameObject implements LifeCycle {
    protected float customMaskWidth;
    protected float customMaskHeight;
    protected boolean animating = true;
    protected float direction;
    protected float speed;
    protected float spriteDirection;
    protected boolean gravityStatus;
    protected boolean fitSpriteToDirection = true;
    protected boolean destroyMeFlag;
    protected Animation animation;
    protected boolean startingOutside;
    protected boolean animationEndDestroy;
    protected TextureAtlas atlas;
    protected boolean beenInside;
    protected float fallingSpeed;
    protected TextureAtlas.AtlasRegion currentFrame;
    protected float animationStateTime;
    protected Random randomizer = new Random();
    protected float alpha = 1;
    protected double maxFallingSpeed = Rules.Misc.MAX_FALLING_SPEED;
    protected float gravityAcceleration = Rules.Misc.Gravity.ACCELERATION;
    protected boolean visible = true;
    protected float scaleX = 1;
    protected float scaleY = 1;
    protected long shootingSoundId;
    private float previousX;
    private float previousY;
    private Timer scheduler = new Timer();
    private WarScreenElements elements;
    private HashMap<String, Animation> animationsObjectsMap;
    int shade;

    /**
     * Constructor for non-poolable object.
     *
     * @param atlas      The atlas which holds all images.
     * @param spriteName The name of the region this object starts with.
     * @param x          Starting X position of the object.
     * @param y          Starting Y position of the object.
     * @param elements   The war screen's elements.
     */
    public GameObject(TextureAtlas atlas, String spriteName, float x, float y, WarScreenElements elements) {
        this(atlas, spriteName, x, y, 0, elements);
    }

    /**
     * Constructor for non-poolable object.
     *
     * @param atlas      The atlas which holds all images.
     * @param spriteName The name of the region this object starts with.
     * @param x          Starting X position of the object.
     * @param y          Starting Y position of the object.
     * @param direction  Starting direction of the object.
     * @param elements   The war screen's elements.
     */
    public GameObject(TextureAtlas atlas, String spriteName, float x, float y, float direction, WarScreenElements elements) {
        this(atlas, spriteName, x, y, direction, true, elements);
    }

    /**
     * Constructor for non-poolable object.
     *
     * @param atlas                The atlas which holds all images.
     * @param spriteName           The name of the region this object starts with.
     * @param x                    Starting X position of the object.
     * @param y                    Starting Y position of the object.
     * @param direction            Starting direction of the object.
     * @param fitSpriteToDirection Whether to set current image to the direction.
     * @param elements             The war screen's elements.
     */
    public GameObject(TextureAtlas atlas, String spriteName, float x, float y, float direction, boolean fitSpriteToDirection, WarScreenElements elements) {
        this.fitSpriteToDirection = fitSpriteToDirection;
        this.elements = elements;
        if (fitSpriteToDirection) {
            setSpriteDirection(direction);
        } else {
            setSpriteDirection(0);
        }

        this.atlas = atlas;
        if (spriteName != null) {
            setRegion(spriteName);
        }
        setPosition(x, y);
        setDirection(direction);
        if (isOutside()) {
            startingOutside = true;
        }
    }

    /**
     * Constructor for poolable objects.
     * Initialization is made with init() methods.
     */
    public GameObject() {
        startingOutside = true;
    }

    public boolean isOutside() {
        return x < -width
                || x > Rules.System.Resolution.WIDTH_TARGET_RESOLUTION + width
                || y < -height
                || y > Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + height;
    }

    protected void postTask(Timer.Task task) {
        scheduler.postTask(task);
    }

    protected boolean setRegion(String name) {
        return setRegion(name, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    protected boolean setRegion(String name, float frameDuration) {
        return setRegion(name, frameDuration, Animation.PlayMode.LOOP);
    }

    protected boolean setRegion(String name, float frameDuration, Animation.PlayMode playMode) {
        if ((currentFrame != null && currentFrame.name.equals(name)) || atlas == null) {
            return false;
        }
        currentFrame = atlas.findRegion(name, 0);
        if (currentFrame == null) {
            currentFrame = atlas.findRegion(name, -1);
        }
        width = currentFrame.getRegionWidth();
        height = currentFrame.getRegionHeight();
        animationStateTime = 0;
        animation = getAnimationObject(frameDuration, name, playMode);
        return true;
    }

    private Animation getAnimationObject(float frameDuration, String name, Animation.PlayMode playMode) {
        if (animationsObjectsMap == null) {
            animationsObjectsMap = new HashMap<String, Animation>();
        }
        Animation returnedAnimation;
        if (animationsObjectsMap.containsKey(name)) {
            returnedAnimation = animationsObjectsMap.get(name);
            returnedAnimation.setFrameDuration(frameDuration);
            returnedAnimation.setPlayMode(playMode);
            return returnedAnimation;
        }
        returnedAnimation = new Animation(frameDuration, atlas.findRegions(name), playMode);
        animationsObjectsMap.put(name, returnedAnimation);
        return returnedAnimation;
    }

    public float getCenterX() {
        return (getX() + (getWidth() / 2) - getOriginX());
    }

    public float getCenterY() {
        return (getY() + (getHeight() / 2) - getOriginY());
    }


    public WarScreenElements getElements() {
        return elements;
    }

    public void setDirection(float degrees) {
        direction = GameUtils.fixedDegrees(degrees);
    }

    protected void createLight() {
        if (elements.getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT) {
            elements.getEffectsManager().createLight(this);
        }
    }

    protected void setSpriteDirectionToMovement() {
        spriteDirection = GameUtils.getDirectionToPoint(previousX, previousY, x, y);
    }

    public void onUpdate(float delta) {
        previousX = x;
        previousY = y;
        handleMovement(delta);
        animate();
        if (fitSpriteToDirection) {
            setSpriteDirection(direction);
        }
        if (!hasBeenInside() && !isOutside()) {
            setBeenInside(true);
        }
        if (animationEndDestroy && animation != null) {
            if (animation.isAnimationFinished(animationStateTime)) {
                destroyMeFlag = true;
            }
        }
    }

    void setWarScreenElements(WarScreenElements elements) {
        this.elements = elements;
    }

    public void onDestroy() {
        onDestroy(false);
    }

    public void onDestroy(boolean doDestroySequence) {
        onDestroy(doDestroySequence, true);
    }

    protected void onDestroy(boolean doDestroySequence, boolean clearScheduler) {
        setDestroyMeFlag(true);
        if (clearScheduler) {
            scheduler.clear();
        }
    }

    private void animate() {
        if (animation == null || !animating) {
            return;
        }
        animationStateTime += Gdx.graphics.getDeltaTime();
        boolean isLooping = false;
        if (animation.getPlayMode() == Animation.PlayMode.LOOP) {
            isLooping = true;
        } else if (animation.getPlayMode() == Animation.PlayMode.NORMAL) {
            isLooping = false;
        }
        currentFrame = (TextureAtlas.AtlasRegion) animation.getKeyFrame(animationStateTime, isLooping);
    }

    private void handleMovement(float delta) {
        moveInDirection(delta);
        fall(delta);
    }

    private void moveInDirection(float delta) {
        if (speed > 0) {
            takeStep(speed, direction, delta);
        }
    }

    protected void onGroundCollision() {
        setY(Rules.Level.GROUND_Y);
    }

    protected void fall(float delta) {
        if (!gravityStatus) {
            return;
        }
        if (getCenterY() <= Rules.Level.GROUND_Y) {
            setY(Rules.Level.GROUND_Y);
            speed = 0;
            fallingSpeed = 0;
        } else {
            takeStep(fallingSpeed, Rules.Level.GRAVITY_DIRECTION, delta);
            if (fallingSpeed < maxFallingSpeed) {
                fallingSpeed += gravityAcceleration;
            }
        }
    }

    public boolean isStartingOutside() {
        return startingOutside;
    }

    public boolean hasBeenInside() {
        return beenInside;
    }

    public void setSpeed(float speed) {
        this.speed = speed;
    }

    public boolean isDestroyed() {
        return destroyMeFlag;
    }

    protected float getSpriteDirection() {
        return spriteDirection;
    }

    public boolean overlapsWithOrigin(GameObject r) {
        return getX() - getOriginX() + customMaskWidth < r.getX() - r.getOriginX() + r.getWidth() && getX() - getOriginX() + getWidth() - customMaskWidth > r.getX() - r.getOriginX() && getY() + customMaskHeight - getOriginY() < r.getY() - r.getOriginY() + r.getHeight() && getY() - customMaskHeight - getOriginY() + getHeight() > r.getY() - r.getOriginY();
    }

    @Override
    public boolean contains(float x, float y) {
        return this.getX() - getOriginX() <= x && this.getX() - getOriginX() + this.getWidth() >= x && this.getY() - getOriginY() <= y && this.getY() - getOriginY() + this.getHeight() >= y;
    }

    /**
     * Schedules a task by given seconds.
     */
    protected void scheduleTask(Timer.Task task, float timing) {
        scheduleTask(task, timing, 0, 0);
    }

    /**
     * Schedules a task by given seconds with a given interval for a given repeat.
     */
    protected void scheduleTask(Timer.Task task, float timing, float interval, int repeat) {
        if (!task.isScheduled()) {
            scheduler.scheduleTask(task, timing, interval, repeat);
        }
    }

    public float getDirection() {
        return direction;
    }

    public double getSpeed() {
        return speed;
    }

    protected void setAlpha(float alpha) {
        if (alpha > 0) {
            this.alpha = alpha;
        } else {
            this.alpha = 0;
        }
    }

    public void setDestroyMeFlag(boolean destroyMeFlag) {
        this.destroyMeFlag = destroyMeFlag;
    }

    public boolean isVisible() {
        return visible;
    }

    public void setSpriteDirection(float spriteDirection) {
        this.spriteDirection = GameUtils.fixedDegrees(spriteDirection);
    }

    public void setScaleX(float scaleX) {
        this.scaleX = scaleX;
    }

    public void setScaleY(float scaleY) {
        this.scaleY = scaleY;
    }

    public void pauseTimer() {
        scheduler.stop();
    }

    public void resumeTimer(long delayMillis) {
        scheduler.delay(delayMillis);
        scheduler.start();
    }

    public void onDraw(SpriteBatch batch) {
        if (currentFrame != null && isVisible()) {
            batch.draw(currentFrame, x - originX, y - originY, originX, originY, currentFrame.getRegionWidth(), currentFrame.getRegionHeight(), scaleX, scaleY, getSpriteDirection());
        }
    }

    protected final Timer.Task TASK_DESTROY = new Timer.Task() {
        @Override
        public void run() {
            onDestroy(false);
        }
    };

    public void setGravity(boolean gravity) {
        this.gravityStatus = gravity;
    }

    public boolean isOnLeftSide() {
        return x < Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2;
    }

    protected void takeStep(float step, float direction, float delta) {
        step = GameUtils.realSpeed(step, delta);
        setX(x + ((float) (Math.cos(Math.toRadians(direction)) * step)));
        float newY = y + ((float) (Math.sin(Math.toRadians(direction)) * step));
        if (newY > Rules.Level.GROUND_Y) {
            setY(newY);
        } else {
            onGroundCollision();
        }
    }

    public void setFrameDuration(float frameDuration) {
        if (animation != null) {
            animation.setFrameDuration(frameDuration);
        }
    }

    int getShade() {
        return shade;
    }

    void setShade(int shade) {
        this.shade = shade;
    }

    public void stopAllTasks() {
        scheduler.stop();
        scheduler.clear();
    }

    protected final Timer.Task TASK_DISAPPEAR = new Timer.Task() {
        @Override
        public void run() {
            onDestroy();
        }
    };

    @Override
    public void onTerminate(WarScreen warScreen) {

    }

    public void onOutside(Level currentLevel) {
        if (!isStartingOutside() || (isStartingOutside() && hasBeenInside())) {
            setDestroyMeFlag(true);
        }
    }

    public boolean isFacingLeft() {
        return getDirection() == 180;
    }

    protected void faceRegion() {
        if (isFacingLeft()) {
            scaleX = -1;
        }
    }

    public void setBeenInside(boolean beenInside) {
        this.beenInside = beenInside;
    }
}
