package com.gadarts.parashoot.misc.effects;

import com.badlogic.gdx.utils.Timer;
import com.gadarts.parashoot.utils.Rules;

public class HudCoinEffect {
    private int amount;
    private float x, y;
    private float alpha;
    private boolean fadingOut;

    public HudCoinEffect() {
    }

    public void initialize(int score, float x, float y) {
        this.amount += score;
        this.x = x;
        this.y = y;
        alpha = 1;
        fadingOut = false;
        TASK_DESTROY.cancel();
        TASK_FADEOUT.cancel();
        Timer.schedule(TASK_DESTROY, Rules.Misc.ScoreEffect.DURATION);
        Timer.schedule(TASK_FADEOUT, Rules.Misc.ScoreEffect.DURATION_TO_FADEOUT);
    }

    public int getAmount() {
        return amount;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

    public boolean isFadingOut() {
        return fadingOut;
    }

    public float getAlpha() {
        return alpha;
    }

    public void setAlpha(float alpha) {
        if (alpha < 0) {
            alpha = 0;
        } else if (alpha > 1) {
            alpha = 1;
        }
        this.alpha = alpha;
    }

    private final Timer.Task TASK_FADEOUT = new Timer.Task() {
        @Override
        public void run() {
            fadingOut = true;
        }
    };

    private final Timer.Task TASK_DESTROY = new Timer.Task() {
        @Override
        public void run() {
            reset();
        }
    };

    private void reset() {
        setAlpha(0);
        amount = 0;
    }
}
