package com.gadarts.parashoot.misc.effects;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Interpolation;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.scenes.scene2d.actions.Actions;
import com.badlogic.gdx.scenes.scene2d.actions.SequenceAction;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.utils.Pool;
import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.utils.Rules;

/**
 * Created by Gad on 25/10/2017.
 */

public class CoinGainEffect extends Label implements Pool.Poolable {

    private final GlyphLayout layout;
    private boolean free;

    public CoinGainEffect() {
        super(null, new Label.LabelStyle(Parastrike.getAssetsManager().get(Rules.System.FontsParameters.RegularFontNames.MEDIUM, BitmapFont.class), Color.GREEN));
        layout = new GlyphLayout();
    }

    @Override
    public void reset() {
        free = true;
        remove();
        setVisible(true);
        getColor().a = 1;
        clearActions();
    }

    public void init(float x, float y, int value) {
        free = false;
        String text = "+" + value;
        setText(text);
        layout.setText(getStyle().font, text);
        handleActions(value);
        setPosition(x - layout.width / 2, y);
    }

    private void handleActions(int value) {
        if (value > Rules.Hud.CoinsEffect.COLORFUL_VALUE) {
            colorfulEffect();
        } else if (value > Rules.Hud.CoinsEffect.GOLD_VALUE) {
            goldEffect();
        } else greenEffect();
    }

    private void greenEffect() {
        LabelStyle style = getStyle();
        style.font.getData().setScale(Rules.Hud.CoinsEffect.GREEN_SCALE);
        style.fontColor = Color.GREEN;
        SequenceAction moveAndFadeOut = Actions.sequence(Actions.moveBy(0, Rules.Hud.CoinsEffect.AMOUNT_Y, Rules.Hud.CoinsEffect.GREEN_DURATION, Interpolation.smooth), Actions.fadeOut(Rules.Hud.CoinsEffect.GREEN_DURATION, Interpolation.smooth), Actions.visible(false));
        addAction(moveAndFadeOut);
    }

    private void colorfulEffect() {
        LabelStyle style = getStyle();
        style.font.getData().setScale(Rules.Hud.CoinsEffect.COLORFUL_SCALE);
        SequenceAction moveAndFadeOut = Actions.sequence(Actions.moveBy(0, Rules.Hud.CoinsEffect.AMOUNT_Y, Rules.Hud.CoinsEffect.GOLD_DURATION, Interpolation.smooth), Actions.visible(false));
        Action colorize = Actions.sequence(Actions.color(Color.GOLD, Rules.Hud.CoinsEffect.COLOR_CHANGE_DURATION), Actions.color(Color.BLUE, Rules.Hud.CoinsEffect.COLOR_CHANGE_DURATION), Actions.color(Color.GREEN, Rules.Hud.CoinsEffect.COLOR_CHANGE_DURATION));
        addAction(Actions.parallel(moveAndFadeOut, Actions.forever(colorize)));
    }

    private void goldEffect() {
        LabelStyle style = getStyle();
        style.font.getData().setScale(Rules.Hud.CoinsEffect.GOLD_SCALE);
        style.fontColor = Color.GOLD;
        SequenceAction moveAndFadeOut = Actions.sequence(Actions.moveBy(0, Rules.Hud.CoinsEffect.AMOUNT_Y, Rules.Hud.CoinsEffect.GOLD_DURATION, Interpolation.smooth), Actions.visible(false));
        Action fade = Actions.delay(Rules.Hud.CoinsEffect.FADE_IN_OUT_DURATION, Actions.sequence(Actions.fadeOut(Rules.Hud.CoinsEffect.FADE_IN_OUT_DURATION, Interpolation.smooth), Actions.fadeIn(Rules.Hud.CoinsEffect.FADE_IN_OUT_DURATION, Interpolation.smooth)));
        addAction(Actions.parallel(moveAndFadeOut, Actions.forever(fade)));
    }

    @Override
    public void setVisible(boolean visible) {
        super.setVisible(visible);
        if (!free) {
            Pools.get(CoinGainEffect.class).free(this);
        }
    }
}
