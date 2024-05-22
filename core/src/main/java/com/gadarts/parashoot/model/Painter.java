package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.glutils.ShaderProgram;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Array;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.AssetManagerWrapper;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.aircrafts.Boss;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.misc.effects.HudCoinEffect;
import com.gadarts.parashoot.misc.effects.Light;
import com.gadarts.parashoot.misc.effects.ParticleWrapper;
import com.gadarts.parashoot.player.SideKick;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.screens.WarScreen;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

import java.util.Map;

import static com.gadarts.parashoot.utils.Rules.Enemies.AirCrafts.Boss.*;
import static com.gadarts.parashoot.utils.Rules.Hud;

/**
 * Created by Gad on 30/07/2015.
 */
public class Painter {
    private static final Color MINIMAL_BRIGHTNESS = new Color(0.2f, 0.2f, 0.2f, 1);
    public SpriteBatch batch;
    private Texture ground;
    private Texture landscape;
    private Texture background;
    private ShaderProgram DEFAULT_SHADER = SpriteBatch.createDefaultShader();
    private ShaderProgram WHITE_SHADER = new ShaderProgram(Gdx.files.internal(Assets.Configs.CONFIGS_FOLDER_NAME + '/' + Assets.Configs.GFX_FOLDER_NAME + '/' + Assets.Configs.GFX.VERTEX_SHADER).readString(), Gdx.files.internal(Assets.Configs.CONFIGS_FOLDER_NAME + '/' + Assets.Configs.GFX_FOLDER_NAME + '/' + Assets.Configs.GFX.WHITE_SHADER).readString());
    private ShaderProgram RED_SHADER = new ShaderProgram(Gdx.files.internal(Assets.Configs.CONFIGS_FOLDER_NAME + '/' + Assets.Configs.GFX_FOLDER_NAME + '/' + Assets.Configs.GFX.VERTEX_SHADER).readString(), Gdx.files.internal(Assets.Configs.CONFIGS_FOLDER_NAME + '/' + Assets.Configs.GFX_FOLDER_NAME + '/' + Assets.Configs.GFX.RED_SHADER).readString());
    private BitmapFont hudFont;
    private OrthographicCamera mainCamera;
    private WarScreen warScreen;
    private BitmapFont messagesFont;
    private GlyphLayout fontLayout = new GlyphLayout();
    private Array<GameObject> objectsToWhiteShade = new Array<GameObject>();
    private Array<GameObject> objectsToRedShade = new Array<GameObject>();
    private boolean shadeBackground;
    private boolean alarmState;
    private float alarmAlpha;
    private TextureAtlas.AtlasRegion flashRegion;
    private float fadeBrightnessPace;
    private Color currentColor = new Color(); //Holds current color.
    private Color colorBeforeFade = new Color();  //Used to hold color for the fade transition.
    private Boss boss;

    public Painter(OrthographicCamera mainCamera, SpriteBatch batch, WarScreen warScreen) {
        this.warScreen = warScreen;
        this.mainCamera = mainCamera;
        this.batch = batch;
        messagesFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.ArmyFontNames.MEDIUM, BitmapFont.class);
        messagesFont.setColor(Color.RED);
        ShaderProgram.pedantic = false;
        flashRegion = ((TextureAtlas) Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Player.BONUS_APPENDIX_DATA_FILE)).findRegion(Assets.GFX.Sheets.ImagesNames.WHITE_FLASH);
        hudFont = Parastrike.getAssetsManager().get(Rules.System.FontsParameters.DigitalFontNames.TINY, BitmapFont.class);
        hudFont.setColor(Color.RED);
    }

    public void initializeSky() {
        Level currentLevel = warScreen.getElements().getCurrentLevel();
        AssetManagerWrapper assetsManager = Parastrike.getAssetsManager();
        Rules.Level.GlobalEffects.SkyType sky = warScreen.getElements().getCurrentLevel().getSky();
        if (sky == Rules.Level.GlobalEffects.SkyType.DAY) {
            if (currentLevel.getWeather() == Rules.Level.GlobalEffects.WeatherTypes.RAIN) {
                background = assetsManager.get(Assets.GFX.Images.InGame.DAY_CLOUDY);
            } else {
                background = assetsManager.get(Assets.GFX.Images.InGame.DAY);
            }
        } else if (sky == Rules.Level.GlobalEffects.SkyType.AFTERNOON) {
            background = assetsManager.get(Assets.GFX.Images.InGame.NOON);
        } else {
            background = assetsManager.get(Assets.GFX.Images.General.NIGHT);
        }
    }

    public void drawGame() {
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        batch.setProjectionMatrix(mainCamera.combined);
        batch.begin();
        drawBackground();
        drawGameObjects();
        drawGameObject(warScreen.getPlayerHandler().getTurret());
        drawGameObject(warScreen.getPlayerHandler().getBunker());
        SideKick leftSideKick = warScreen.getPlayerHandler().getLeftSideKick();
        if (leftSideKick != null) {
            drawGameObject(leftSideKick);
            drawGameObject(warScreen.getPlayerHandler().getRightSideKick());
        }
        drawEffects();
        batch.draw(ground, 0, 0);
        drawFlash();
        if (GameSettings.SHOW_AND_LOG_FPS) warScreen.getFpsLogger().log();
        batch.end();
        shadeObjects(objectsToWhiteShade, WHITE_SHADER);
        shadeObjects(objectsToRedShade, RED_SHADER);
        drawHUD();
        handleBrightnessTransition();
    }

    private void handleBrightnessTransition() {
        if (fadeBrightnessPace != 0) {
            Color color = batch.getColor();
            if (fadeBrightnessPace < 0) {
                color.r = (color.r > MINIMAL_BRIGHTNESS.r + fadeBrightnessPace) ? color.r + fadeBrightnessPace : MINIMAL_BRIGHTNESS.r;
                color.g = (color.g > MINIMAL_BRIGHTNESS.g + fadeBrightnessPace) ? color.g + fadeBrightnessPace : MINIMAL_BRIGHTNESS.g;
                color.b = (color.b > MINIMAL_BRIGHTNESS.b + fadeBrightnessPace) ? color.b + fadeBrightnessPace : MINIMAL_BRIGHTNESS.b;
            } else {
                color.r = (color.r < colorBeforeFade.r - fadeBrightnessPace) ? color.r + fadeBrightnessPace : currentColor.r;
                color.g = (color.g < colorBeforeFade.g - fadeBrightnessPace) ? color.g + fadeBrightnessPace : currentColor.g;
                color.b = (color.b < colorBeforeFade.b - fadeBrightnessPace) ? color.b + fadeBrightnessPace : currentColor.b;
            }
            if (color.equals(colorBeforeFade) || color.equals(MINIMAL_BRIGHTNESS)) {
                fadeBrightnessPace = 0;
            }
            batch.setColor(color);
        }
    }

    private void drawGameObjects() {
        for (Object o : warScreen.getObjectsMap().entrySet()) {
            Map.Entry pair = (Map.Entry) o;
            Array list = (Array) pair.getValue();
            for (int i = 0; i < list.size; i++) {
                GameObject object = (GameObject) list.get(i);
                drawGameObject(object);
            }
        }
    }

    private void drawGameMonitorMessages(Batch hudBatch) {
        String currentMessage;
        MessageDisplay messageDisplay = warScreen.getElements().getMessageDisplay();
        if (warScreen.isPaused() && !warScreen.getHud().isMenuOpen() && warScreen.isPauseMessageEnabled()) {
            currentMessage = Assets.Strings.InGameMessages.PAUSE_MESSAGE;
        } else {
            currentMessage = messageDisplay.getCurrentMessage();
        }
        if (currentMessage != null) {
            fontLayout.setText(messagesFont, currentMessage);
            float alpha = messageDisplay.getMessageAlpha();
            if (alpha < 1) {
                setFontAlpha(alpha, messagesFont);
            }
            messagesFont.draw(hudBatch, currentMessage, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2 - fontLayout.width / 2, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 + fontLayout.height / 2);
            if (alpha < 1) {
                setFontAlpha(1, messagesFont);
            }
        }
    }

    private void drawFlash() {
        EffectsManager effectsManager = warScreen.getElements().getEffectsManager();
        if (effectsManager.getAtomicFlash() > 0) {
            Color color = batch.getColor();
            currentColor.set(color);
            color.a = effectsManager.getAtomicFlash();
            batch.setColor(color);
            batch.draw(flashRegion, 0, 0, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
            batch.setColor(currentColor);
        }
    }

    private void drawHUD() {
        Batch hudBatch = warScreen.getHud().getBatch();
        hudBatch.begin();
        drawGameMonitorMessages(hudBatch);
        drawAlarm(hudBatch);
        warScreen.getHud().draw();
        ScoresHandler scoresHandler = warScreen.getElements().getScoresHandler();
        if (!warScreen.getHud().getMiniHud().isHiding()) {
            PlayerStatsHandler playerStatsHandler = Parastrike.getPlayerStatsHandler();
            hudFont.draw(hudBatch, "" + playerStatsHandler.getCoins(), Hud.Stats.Coins.X, Hud.Stats.Coins.Y);
            hudFont.draw(hudBatch, "" + playerStatsHandler.getScore(), Hud.Stats.Score.X, Hud.Stats.Score.Y);
        }
        EffectsManager effectsManager = warScreen.getElements().getEffectsManager();
        drawCoinsEffects(hudBatch, effectsManager.getHudCoinEffect());
        drawCoinsEffects(hudBatch, effectsManager.getScoreEffect());
        if (GameSettings.SHOW_AND_LOG_FPS) {
            hudFont.draw(hudBatch, "FPS: " + Gdx.graphics.getFramesPerSecond(), 10, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 200);
//            messagesFont.draw(hudBatch, "Free: " + Pools.get(CoinGainEffect.class).getFree(), 10, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 300);
//            messagesFont.draw(hudBatch, "Free2 : " + effectsManager.getEffectsPools().get(Assets.Configs.ParticleEffects.SMALL_BLAST_RING).getFree(), 10, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 400);
//            hudFont.draw(hudBatch, "KILLS: " + scoresHandler.getEnemiesDestroyed(), 10, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 300);
//            hudFont.draw(hudBatch, "IN SPATIAL: " + warScreen.getElements().getInteractionsManager().getSpatial().entrySet().size(), 10, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 300);
//            hudFont.draw(hudBatch, "IN SPATIAL POOL: " + warScreen.getElements().getInteractionsManager().getArraysPool().size, 10, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 400);
        }
        hudBatch.end();
        ShapeRenderer shapeRenderer = warScreen.getHud().getShapeRenderer();
        showSpatialGrid(hudBatch, shapeRenderer);
        Bunker bunker = warScreen.getPlayerHandler().getBunker();
        if (!warScreen.getHud().getMiniHud().isHiding() && !bunker.isDead()) {
            shapeRenderer = warScreen.getHud().getShapeRenderer();
            shapeRenderer.setProjectionMatrix(warScreen.getHudCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            drawHealthBar(shapeRenderer, bunker.getHealth(), bunker.getStartArmor(), Hud.Stats.Health.X, Hud.Stats.Health.Y, Hud.Stats.Health.MAX_WIDTH, Hud.Stats.Health.HEIGHT);
            if (boss != null) {
                shapeRenderer.setColor(0, 0, 0, 0.5f);
                float x = Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2 - HEALTH_BAR_MAX_WIDTH / 2;
                shapeRenderer.rect(x, HEALTH_BAR_Y, HEALTH_BAR_MAX_WIDTH, HEALTH_BAR_HEIGHT);
                drawHealthBar(shapeRenderer, boss.getHealth(), HP, x, HEALTH_BAR_Y, HEALTH_BAR_MAX_WIDTH, HEALTH_BAR_HEIGHT);
            }
            shapeRenderer.end();
        }
    }

    private void drawHealthBar(ShapeRenderer shapeRenderer, Float health, float beginHealth, float x, float y, float maxWidth, float height) {
        float percentHealth = health * 100 / beginHealth;
        shapeRenderer.setColor((100 - percentHealth) / 100, percentHealth / 100, 0, 1);
        shapeRenderer.rect(x, y, health / beginHealth * maxWidth, height);
    }


    private void showSpatialGrid(Batch hudBatch, ShapeRenderer shapeRenderer) {
        if (GameSettings.SHOW_SPATIAL_GRID) {
            int cellWidth = Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / Rules.System.SPATIAL_WIDTH_DIVISION;
            int heightWithoutGround = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - Rules.Level.GROUND_Y;
            int cellHeight = heightWithoutGround / Rules.System.SPATIAL_HEIGHT_DIVISION;
            drawSpatialLines(shapeRenderer, cellWidth, cellHeight);
            drawSpatialLabels(hudBatch, cellWidth, cellHeight);
        }
    }

    private void drawSpatialLabels(Batch hudBatch, int cellWidth, int cellHeight) {
        hudBatch.begin();
        for (Integer id : warScreen.getElements().getInteractionsManager().getSpatial().keySet()) {
            hudFont.draw(hudBatch, id.toString(), (id % Rules.System.SPATIAL_WIDTH_DIVISION) * cellWidth + 10, Rules.Level.GROUND_Y + ((id / Rules.System.SPATIAL_WIDTH_DIVISION) * cellHeight + 30));
        }
        hudBatch.end();
    }

    private void drawSpatialLines(ShapeRenderer shapeRenderer, int cellWidth, int cellHeight) {
        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
        shapeRenderer.setColor(Color.BLACK);
        drawVerticalSpatialLines(shapeRenderer, cellWidth);
        drawHorizontalSpatialLines(shapeRenderer, cellHeight);
        shapeRenderer.end();
    }

    private void drawHorizontalSpatialLines(ShapeRenderer shapeRenderer, int cellHeight) {
        for (int i = 0; i <= Rules.System.SPATIAL_HEIGHT_DIVISION; i++) {
            int y = Rules.Level.GROUND_Y + cellHeight * i;
            shapeRenderer.line(0, y, 0, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, y, 0);
        }
    }

    private void drawVerticalSpatialLines(ShapeRenderer shapeRenderer, int cellWidth) {
        for (int i = 0; i <= Rules.System.SPATIAL_WIDTH_DIVISION; i++) {
            int x = cellWidth * i;
            shapeRenderer.line(x, Rules.Level.GROUND_Y, 0, x, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION, 0);
        }
    }

    private void drawCoinsEffects(Batch batch, HudCoinEffect HudCoinEffect) {
        boolean isFadingOut = HudCoinEffect.isFadingOut();
        if (isFadingOut) {
            float alpha = HudCoinEffect.getAlpha();
            setFontAlpha(alpha, hudFont);
            HudCoinEffect.setAlpha(alpha - Rules.Misc.ScoreEffect.ALPHA_DELTA);
        }
        String score = "+" + HudCoinEffect.getAmount();
        hudFont.draw(batch, score, HudCoinEffect.getX(), HudCoinEffect.getY());
        if (isFadingOut) {
            setFontAlpha(1, hudFont);
        }
    }

    private void drawAlarm(Batch batch) {
        if (alarmState) {
            Color color = batch.getColor();
            color.a = alarmAlpha;
            batch.setColor(color);
            EffectsManager effectsManager = warScreen.getElements().getEffectsManager();
            batch.draw(effectsManager.ALARM_REGION, 0, 0, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, effectsManager.ALARM_REGION.getRegionHeight());
            batch.draw(effectsManager.ALARM_REGION, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION, 0, 0, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION, effectsManager.ALARM_REGION.getRegionHeight(), 1, 1, 180);
            setFontAlpha(alarmAlpha, messagesFont);
            fontLayout.setText(messagesFont, Assets.Strings.InGameMessages.DAMAGE_CRITICAL);
            messagesFont.draw(batch, Assets.Strings.InGameMessages.DAMAGE_CRITICAL, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2 - fontLayout.width / 2, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 100);
            setFontAlpha(1, messagesFont);
            batch.setColor(currentColor);
        }
    }

    private void drawBackground() {
        if (shadeBackground) {
            batch.setShader(WHITE_SHADER);
            batch.draw(background, 0, 0);
            batch.setShader(DEFAULT_SHADER);
            shadeBackground = false;
        } else {
            batch.draw(background, 0, 0);
        }
        currentColor.set(batch.getColor());
        boolean isNight = warScreen.getElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT;
        if (isNight) {
            batch.setColor(batch.getColor().sub(Rules.Level.GlobalEffects.DARKNESS, Rules.Level.GlobalEffects.DARKNESS, Rules.Level.GlobalEffects.DARKNESS, 0));
        }
        if (landscape != null) {
            batch.draw(landscape, 0, Rules.Level.GROUND_Y);
        }
        if (isNight) {
            batch.setColor(currentColor);
        }
    }

    private void shadeObjects(Array<GameObject> objectsToShade, ShaderProgram shader) {
        if (objectsToShade.size > 0) {
            batch.setShader(shader);
            batch.begin();
            for (int i = 0; i < objectsToShade.size; i++) {
                GameObject currentObject = objectsToShade.get(i);
                currentObject.onDraw(batch);
                currentObject.setShade(Rules.System.GFX.ShadesIds.NO_SHADE);
            }
            objectsToShade.clear();
            batch.end();
            batch.setShader(DEFAULT_SHADER);
        }
    }

    private void drawGameObject(GameObject gameObject) {
        if (gameObject.isDestroyed()) {
            return;
        }
        if (gameObject.getShade() == Rules.System.GFX.ShadesIds.WHITE_SHADE) {
            objectsToWhiteShade.add(gameObject);
        } else if (gameObject.getShade() == Rules.System.GFX.ShadesIds.RED_SHADE) {
            objectsToRedShade.add(gameObject);
        } else {
            gameObject.onDraw(batch);
        }
    }

    private void drawEffects() {
        EffectsManager effectsManager = warScreen.getElements().getEffectsManager();
        drawParticleEffects(effectsManager.getActiveEffects());
        drawParticleEffects(effectsManager.getAdditiveActiveEffects());
        Array<Light> lights = effectsManager.getLights();
        if (lights.size > 0) {
            batch.setBlendFunction(GL20.GL_DST_COLOR, GL20. GL_SRC_ALPHA);
            for (int i = 0; i < lights.size; i++) {
                Light light = lights.get(i);
                TextureAtlas.AtlasRegion lightRegion = effectsManager.getLightRegion(light.getRegion());
                float width = light.getWidth();
                if (width == 0) {
                    width = lightRegion.getRegionWidth();
                }
                float height = light.getHeight();
                if (height == 0) {
                    height = lightRegion.getRegionHeight();
                }
                batch.draw(lightRegion, light.getX() - light.getOriginX(), light.getY() - light.getOriginY(), width, height);
            }
            batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    private void drawParticleEffects(Array<ParticleWrapper> effects) {
        for (int i = 0; i < effects.size; i++) {
            ParticleWrapper wrapper = effects.get(i);
            ParticleEffectPool.PooledEffect effect = wrapper.getEffect();
            float deltaTime = warScreen.isPaused() ? 0 : Gdx.graphics.getDeltaTime();
            effect.draw(batch, deltaTime);
        }
        batch.setBlendFunction(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);
    }

    private void setFontAlpha(float objectAlpha, BitmapFont font) {
        Color color = font.getColor();
        color.a = objectAlpha;
        font.setColor(color);
    }

    public void shadeBackground() {
        shadeBackground = true;
    }

    public void setAlarm(boolean state) {
        alarmState = state;
        if (state) {
            Parastrike.getSoundPlayer().playSound(SFX.Misc.ALARM, true, false);
        } else {
            Parastrike.getSoundPlayer().stopSound(SFX.Misc.ALARM, -1);
        }
    }

    public void setAlarmAlpha(float alarmAlpha) {
        this.alarmAlpha = alarmAlpha;
    }

    public void fadeBrightness(float pace) {
        if (fadeBrightnessPace == 0) {
            colorBeforeFade.set((pace < 0) ? batch.getColor() : currentColor);
        }
        fadeBrightnessPace = pace;
    }

    public boolean getAlarmState() {
        return alarmState;
    }

    public void initializeGroundAndLandscape() {
        Scene theme = warScreen.getElements().getCurrentLevel().getScene();
        AssetManagerWrapper assetsManager = Parastrike.getAssetsManager();
        ground = assetsManager.get(theme.getGroundImage());
        String landscapeImage = warScreen.getElements().getCurrentLevel().getSelectedLandscape();
        if (landscapeImage != null) landscape = assetsManager.get(landscapeImage);
    }

    public void setBoss(Boss boss) {
        this.boss = boss;
    }
}
