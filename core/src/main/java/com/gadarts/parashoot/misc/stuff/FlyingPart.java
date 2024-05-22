package com.gadarts.parashoot.misc.stuff;

import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;

public class FlyingPart extends Misc {
    public FlyingPart() {
        atlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Misc.FLYING_PARTS_DATA_FILE);
    }

    public void init(float x, float y, float direction, float speed, WarScreenElements mechanics) {
        init(null, x, y, direction, speed, mechanics);
    }

    public void init(String imageName, float x, float y, float direction, float speed, WarScreenElements mechanics) {
        super.init(imageName, x, y, direction, speed, false, false, mechanics);
        gravityStatus = true;
        setPosition(x, y);
        setDirection(direction);
        setDestroyMeFlag(false);
        fallingSpeed = 0;
        setOrigin(width / 2, height / 2);
    }

    public void emitSmoke(String smoke) {
        getElements().getEffectsManager().createEffect(smoke, this);
    }

    @Override
    public void onUpdate(float delta) {
        super.onUpdate(delta);
        if (spriteDirection < 360) {
            setSpriteDirection(spriteDirection + Rules.Misc.FlyingPart.FLYING_PART_ROTATION_SPEED);
        } else {
            setSpriteDirection(0);
        }
    }

    @Override
    protected void onGroundCollision() {
        super.onGroundCollision();
        onDestroy();
    }

    public void setRandomFrames(String... images) {
        int randomFrameIndex = randomizer.nextInt(images.length);
        setRegion(atlas.findRegion(images[randomFrameIndex]).name);
    }
}
