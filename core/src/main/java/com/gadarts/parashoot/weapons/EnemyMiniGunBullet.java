package com.gadarts.parashoot.weapons;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 25/04/2017.
 */

public class EnemyMiniGunBullet extends Bullet {
    @Override
    public void init(String spriteName, float x, float y, float direction, float speed, WarScreenElements mechanics, Factories factories) {
        super.init(Assets.GFX.Sheets.ImagesNames.AUTO_TURRET_BULLET, x, y, direction, Rules.Player.SideKicks.Minigunner.BULLET_SPEED, mechanics, factories);
    }
}
