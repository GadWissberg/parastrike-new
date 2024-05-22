package com.gadarts.parashoot.weapons;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 13/10/2015.
 */
public class Electricity extends Misc {
    public Electricity() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.SideKicks.TESLA_DATA_FILE);
    }

    public void init(float x, float y, float direction, WarScreenElements mechanics) {
        super.init(Assets.GFX.Sheets.ImagesNames.ELECTRICITY, x, y, direction, 0, false, true, mechanics);
        scheduleTask(TASK_DESTROY, Rules.Player.SideKicks.Tesla.ELECTRICITY_LIFE_DURATION);
        setOrigin(0, height / 2);
    }

    public void stretchToTarget(Enemy target) {
        float distanceToObject = GameUtils.getDistanceBetweenTwoPoints(x, y + height / 2, target.getX(), target.getY() - target.getHeight());
        setWidth(distanceToObject);
    }

    @Override
    public void onDraw(SpriteBatch batch) {
        if (currentFrame != null || !isVisible()) {
            batch.draw(currentFrame, x - getOriginX(), y - getOriginY(), getOriginX(), getOriginY(), width, height, scaleX, scaleY, getSpriteDirection());
        }
    }
}
