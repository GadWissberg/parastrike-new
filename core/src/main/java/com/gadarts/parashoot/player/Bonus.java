package com.gadarts.parashoot.player;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.model.CollideableGameObject;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Bullet;

/**
 * Created by Gad on 01/08/2015.
 */
public abstract class Bonus extends CollideableGameObject {

    protected String message;
    protected boolean obtainAble;
    protected boolean obtained;
    protected float distanceToTarget;
    private boolean goingAway;
    protected float directionToTarget;
    private float resizeStep;
    private WarScreenElements mechanics;
    private PlayerHandler playerHandler;

    public void init(String spriteName, float x, float y, WarScreenElements mechanics, PlayerHandler playerHandler) {
        super.init(spriteName, x, y, Rules.Misc.Gravity.DIRECTION, Rules.Player.Bonus.FALLING_SPEED, mechanics);
        this.mechanics = mechanics;
        this.playerHandler = playerHandler;
        width = Rules.Player.Bonus.SMALL_WIDTH;
        height = Rules.Player.Bonus.SMALL_HEIGHT;
        setOrigin(width / 2, height / 2);
        fitSpriteToDirection = false;
        obtainAble = false;
        obtained = false;
        goingAway = false;
        TASK_ALLOW_TO_OBTAIN.cancel();
        Timer.schedule(TASK_ALLOW_TO_OBTAIN, Rules.Player.Bonus.PROTECTION_TIME);
        createLight();
        directionToTarget = 0;
        distanceToTarget = 0;
    }

    @Override
    protected void onGroundCollision() {
        onDestroy();
    }

    public PlayerHandler getPlayerHandler() {
        return playerHandler;
    }

    public WarScreenElements getWarScreenMechanics() {
        return mechanics;
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (obtained) {
            if (!goingAway) {
                if (width <= currentFrame.getRotatedPackedWidth()) {
                    resizeStep = Rules.Player.Bonus.IMAGE_RESIZE_PACE;
                    resizeImage();
                } else {
                    goingAway = true;
                    speed = Rules.Player.Bonus.MOVE_AWAY_SPEED;
                    direction = directionToTarget;
                    resizeStep = -currentFrame.getRotatedPackedWidth() / (distanceToTarget / speed);
                    mechanics.getEffectsManager().createEffect(Assets.Configs.ParticleEffects.BONUS_STARS, this);
                }
            } else {
                if (width > Rules.Player.Bonus.IMAGE_RESIZE_PACE) {
                    resizeImage();
                } else {
                    onDestroy();
                }
            }
        }
    }

    private void resizeImage() {
        width += resizeStep;
        originX += resizeStep / 2;
        height += resizeStep;
        originY += resizeStep / 2;
    }

    @Override
    public void collisionInteraction(CollideableGameObject gameObject) {
        gameObject.onCollision(this);
    }

    @Override
    public void onCollision(Enemy other) {

    }

    @Override
    public void onCollision(PlayerCharacter other) {

    }

    @Override
    public void onCollision(Bonus other) {

    }

    @Override
    public void onCollision(Bullet gameObject) {
        if (!obtainAble || obtained || getPlayerHandler().getBunker().isDead() || !gameObject.isPlayerBullet()) {
            return;
        }
        mechanics.getMessageDisplay().add(message);
        mechanics.getEffectsManager().createEffect(Assets.Configs.ParticleEffects.BONUS_STARS, this);
        obtained = true;
        Parastrike.getSoundPlayer().playSound(SFX.Player.Bonus.BONUS);
        direction = 90;
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (currentFrame != null && isVisible()) {
            batch.draw(currentFrame, x - originX, y - originY, originX, originY, width, height, scaleX, scaleY, getSpriteDirection());
        }
    }

    private final Timer.Task TASK_ALLOW_TO_OBTAIN = new Timer.Task() {
        @Override
        public void run() {
            obtainAble = true;
        }
    };

    public boolean hasBeenObtained() {
        return obtained;
    }

    public boolean isObtainAble() {
        return obtainAble;
    }
}
