package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.graphics.g2d.ParticleEmitter;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.*;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.aircrafts.Boss;
import com.gadarts.parashoot.level_model.Level;
import com.gadarts.parashoot.misc.IndependentEffect;
import com.gadarts.parashoot.misc.effects.CoinGainEffect;
import com.gadarts.parashoot.misc.effects.HudCoinEffect;
import com.gadarts.parashoot.misc.effects.Light;
import com.gadarts.parashoot.misc.effects.ParticleWrapper;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;
import com.gadarts.parashoot.model.object_factories.MiscFactory;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

import static com.gadarts.parashoot.utils.Rules.Menu.Mentors.Names.UPGRADE_ARMOR;

public class EffectsManager {
    final TextureAtlas.AtlasRegion ALARM_REGION = ((TextureAtlas) Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.HUD_DATA_FILE)).findRegion(Assets.GFX.Sheets.ImagesNames.ALARM);
    private final WarScreenElements elements;
    private final Factories factories;
    private final Painter painter;
    private final HUD hud;
    private Array<ParticleWrapper> activeEffects = new Array<ParticleWrapper>();
    private Array<ParticleWrapper> additiveActiveEffects = new Array<ParticleWrapper>();
    private boolean lightningOn;
    private ArrayMap<String, ParticleEffectPool> effectsPools = new ArrayMap<String, ParticleEffectPool>();
    private float originalXCamera;
    private float originalYCamera;
    private float quakeInterval;
    private Array<Light> lights = new Array<Light>();
    private float alarmAlpha;
    private float alarmAlphaDelta;
    private boolean vibrationReady = true;
    private long lastVibrationTime;
    private IndependentEffect biohazard;
    private float atomicFlash;
    private float quakeDecay;
    private HudCoinEffect hudCoinEffect = new HudCoinEffect();
    private HudCoinEffect scoreEffect = new HudCoinEffect();
    private TextureAtlas.AtlasRegion lightRegion;
    private PlayerHandler playerHandler;
    private boolean vibrationAllowed, epilepsyAllowed;
    private TextureAtlas.AtlasRegion lampLightRegion;
    private Array<SFX.Misc> ambSounds;

    EffectsManager(WarScreenElements elements, Factories factories, PlayerHandler playerHandler, Painter painter, HUD hud) {
        this.painter = painter;
        this.elements = elements;
        this.factories = factories;
        this.playerHandler = playerHandler;
        this.hud = hud;
        initializeVibrationAndEpilepsy();
    }

    private void initializeVibrationAndEpilepsy() {
        vibrationAllowed = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.VIBRATION, true);
        epilepsyAllowed = Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).getBoolean(Assets.Configs.Preferences.Settings.EPILEPSY, true);
    }

    public void initializeEffects() {
        Array<ParticleEffect> effectsFiles = new Array<ParticleEffect>();
        Parastrike.getAssetsManager().getAll(ParticleEffect.class, effectsFiles);
        for (int i = 0; i < effectsFiles.size; i++) {
            ParticleEffect effect = effectsFiles.get(i);
            effect.allowCompletion();
            String assetFileName = Parastrike.getAssetsManager().getAssetFileName(effect);
            int index = assetFileName.lastIndexOf("/");
            effectsPools.put(assetFileName.substring(index + 1, assetFileName.length() - 2), new ParticleEffectPool(effect, 100, 150));
        }
        EffectsManager effectsManager = elements.getEffectsManager();
        Level currentLevel = elements.getCurrentLevel();
        Rules.Level.GlobalEffects.WeatherTypes weather = currentLevel.getWeather();
        if (weather == Rules.Level.GlobalEffects.WeatherTypes.RAIN) {
            effectsManager.startRain();
        } else if (weather == Rules.Level.GlobalEffects.WeatherTypes.SNOW) {
            effectsManager.startSnow();
        }
        TextureAtlas textureAtlas = Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Misc.PARTICLES_DATA_FILE, TextureAtlas.class);
        lightRegion = textureAtlas.findRegion(Assets.GFX.Sheets.ImagesNames.LIGHT);
        lampLightRegion = textureAtlas.findRegion(Assets.GFX.Sheets.ImagesNames.LAMP_LIGHT);
        ambSounds = currentLevel.getAmbSounds();
        if (ambSounds.size > 0) {
            Timer.instance().scheduleTask(TASK_PLAY_RANDOM_AMB_SOUND, (float) (Rules.Level.MIN_AMB_INTERVAL + Math.random() * Rules.Level.MIN_AMB_INTERVAL));
        }
    }

    private void startRain() {
        IndependentEffect rain = factories.getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.RAIN, 0f, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
        if (epilepsyAllowed) {
            Timer.schedule(TASK_LIGHTNING, (float) (20 * Math.random()));
        }
    }

    private void startSnow() {
        factories.getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.SNOW, 0f, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION);
    }

    public ParticleWrapper createEffect(String effectName, GameObject parent) {
        return createEffect(effectName, parent, 0, 0);
    }

    public ParticleWrapper createEffect(String effectName, GameObject parent, float originX, float originY) {
        if (!GameSettings.ALLOW_PARTICLES) {
            return null;
        }
        ParticleEffectPool.PooledEffect effect = effectsPools.get(effectName).obtain();
        ParticleWrapper particleWrapper = Pools.obtain(ParticleWrapper.class);
        particleWrapper.init(effect, effectName, parent, originX, originY);
        ParticleEmitter particleEmitter = particleWrapper.getEffect().getEmitters().get(0);
        particleEmitter.setCleansUpBlendFunction(false);
        if (particleEmitter.isAdditive()) {
            additiveActiveEffects.add(particleWrapper);
        } else {
            activeEffects.add(particleWrapper);
        }
        return particleWrapper;
    }

    public void onUpdate() {
        manageParticleEffects(activeEffects);
        manageParticleEffects(additiveActiveEffects);
        manageLights();
        handleSkyLightning();
        handleQuakes();
        Bunker bunker = playerHandler.getBunker();
        if (!painter.getAlarmState() && bunker.getHealth() <= Rules.Hud.Alarm.DAMAGE_CRITICAL_LIMIT && bunker.getHealth() > 0) {
            painter.setAlarm(true);
            Parastrike.getMentorsManager().readyMentorIfDidntRun(UPGRADE_ARMOR);
            if (!TASK_CREATE_MEDIC_YAK.isScheduled()) {
                Timer.schedule(TASK_CREATE_MEDIC_YAK, Rules.Enemies.AirCrafts.Yak.MedicYak.FIRST_DELAY_APPEARANCE);
            }
            elements.getMessageDisplay().taunt(SFX.Taunts.DAMAGE_CRITICAL);
            alarmAlphaDelta = 0;
            alarmAlpha = 0;
        } else if (painter.getAlarmState() && (bunker.getHealth() > Rules.Hud.Alarm.DAMAGE_CRITICAL_LIMIT || bunker.getHealth() <= 0)) {
            painter.setAlarm(false);
        }
        if (painter.getAlarmState()) {
            if (alarmAlpha <= Rules.Hud.Alarm.ALARM_ALPHA_DELTA) {
                alarmAlphaDelta = Rules.Hud.Alarm.ALARM_ALPHA_DELTA;
            } else if (alarmAlpha >= 1 - Rules.Hud.Alarm.ALARM_ALPHA_DELTA) {
                alarmAlphaDelta = -Rules.Hud.Alarm.ALARM_ALPHA_DELTA;
            }
            alarmAlpha += alarmAlphaDelta;
            painter.setAlarmAlpha(alarmAlpha);
        }
        if (!vibrationReady && TimeUtils.timeSinceMillis(lastVibrationTime) >= Rules.System.Vibration.VIBRATION_INTERVALS) {
            vibrationReady = true;
        }
        if (atomicFlash > 0) {
            atomicFlash -= Rules.Misc.FLASH_FADEOUT_PACE;
        }
    }

    private void handleQuakes() {
        if (quakeInterval != 0) {
            if (quakeInterval >= Rules.Level.GlobalEffects.Quake.MINIMUM_INTERVAL) {
                float deltaX = (elements.getParastrikeCamera().position.x < originalXCamera) ? 1 : -1;
                float deltaY = (elements.getParastrikeCamera().position.y < originalYCamera) ? 1 : -1;
                elements.getParastrikeCamera().translate(deltaX * quakeInterval, deltaY * quakeInterval);
                if (quakeInterval > 0) {
                    quakeInterval -= quakeDecay;
                }
            } else {
                TASK_STOP_SHAKE.run();
            }
        }
    }

    private void handleSkyLightning() {
        if (lightningOn) {
            if (Math.random() <= 0.1) {
                painter.shadeBackground();
            }
        }
    }

    private final Timer.Task TASK_PLAY_RANDOM_AMB_SOUND = new Timer.Task() {
        @Override
        public void run() {
            Parastrike.getSoundPlayer().playSound(ambSounds.random());
            Timer.instance().scheduleTask(TASK_PLAY_RANDOM_AMB_SOUND, (float) (Rules.Level.MIN_AMB_INTERVAL + Math.random() * Rules.Level.MIN_AMB_INTERVAL));
        }
    };

    private void manageParticleEffects(Array<ParticleWrapper> effects) {
        for (int i = 0; i < (effects.size); i++) {
            ParticleWrapper wrapper = effects.get(i);
            ParticleEffectPool.PooledEffect effect = wrapper.getEffect();
            GameObject parent = wrapper.getParent();
            effect.setPosition(parent.getX() + wrapper.getOriginX(), parent.getY() + wrapper.getOriginY());
            if (parent.isDestroyed()) {
                if (wrapper.isComplete()) {
                    wrapper.destroy();
                    effects.removeIndex(i);
                } else {
                    wrapper.allowCompletion();
                    wrapper.setContinuous(false);
                }
            }
        }
    }

    private void manageLights() {
        for (int i = 0; i < lights.size; i++) {
            Light light = lights.get(i);
            GameObject parent = light.getParent();
            light.setPosition(parent.getX(), parent.getY());
            if (parent.isDestroyed()) {
                lights.removeIndex(i);
                light.free();
            }
        }
    }

    Array<ParticleWrapper> getActiveEffects() {
        return activeEffects;
    }

    Array<ParticleWrapper> getAdditiveActiveEffects() {
        return additiveActiveEffects;
    }

    @SuppressWarnings("unused")
    public int getFree(String effect) {
        return effectsPools.get(effect).getFree();
    }

    @SuppressWarnings("unused")
    public int getNumberOfEffects() {
        return activeEffects.size + additiveActiveEffects.size;
    }

    public void shakeScreen(float quakeInterval) {
        shakeScreen(quakeInterval, Rules.Level.GlobalEffects.Quake.DECAY, true);
    }

    private void shakeScreen(float quakeInterval, float decay, boolean autoStop) {
        if (this.quakeInterval == 0) {
            OrthographicCamera mainCamera = elements.getParastrikeCamera();
            originalXCamera = mainCamera.position.x;
            originalYCamera = mainCamera.position.y;
            this.quakeInterval = quakeInterval;
            quakeDecay = decay;
            if (autoStop && !TASK_STOP_SHAKE.isScheduled()) {
                Timer.schedule(TASK_STOP_SHAKE, Rules.Level.GlobalEffects.Quake.QUAKE_DURATION);
            } else {
                TASK_STOP_SHAKE.cancel();
            }
        } else {
            this.quakeInterval = (quakeInterval + this.quakeInterval <= Rules.Level.GlobalEffects.Quake.MAX_INTERVAL) ? this.quakeInterval + quakeInterval : Rules.Level.GlobalEffects.Quake.MAX_INTERVAL;
        }
    }

    private final Timer.Task TASK_STOP_SHAKE = new Timer.Task() {
        @Override
        public void run() {
            quakeInterval = 0;
            OrthographicCamera mainCamera = elements.getParastrikeCamera();
            mainCamera.position.x = originalXCamera;
            mainCamera.position.y = originalYCamera;
        }
    };

    private final Timer.Task TASK_STOP_LIGHTNING = new Timer.Task() {
        @Override
        public void run() {
            lightningOn = false;
            Timer.schedule(TASK_LIGHTNING, (float) (Rules.Level.GlobalEffects.LIGHTNING_MAX_INTERVAL * Math.random() + Rules.Level.GlobalEffects.LIGHTNING_MIN_INTERVAL));
        }
    };

    private final Timer.Task TASK_LIGHTNING = new Timer.Task() {
        @Override
        public void run() {
            lightningOn = true;
            Parastrike.getSoundPlayer().playSound(Math.random() > 0.5 ? SFX.Misc.THUNDER_1 : SFX.Misc.THUNDER_2, false);
            Timer.schedule(TASK_STOP_LIGHTNING, Rules.Level.GlobalEffects.LIGHTNING_DURATION);
        }
    };

    public Light createLight(GameObject parent) {
        float width = parent.getWidth() + Rules.Misc.LIGHT_PADDING;
        float height = parent.getHeight() + Rules.Misc.LIGHT_PADDING;
        return createLight(parent, Assets.GFX.Sheets.ImagesNames.LIGHT, width, height, width / 2, height / 2);
    }

    public Light createLight(GameObject parent, String region, float width, float height, float originX, float originY) {
        if (!GameSettings.ALLOW_LIGHTS) {
            return null;
        }
        Light light = Pools.obtain(Light.class);
        light.init(parent, region);
        light.setWidth(width).setHeight(height);
        light.setOrigin(originX, originY);
        lights.add(light);
        return light;
    }

    Array<Light> getLights() {
        return lights;
    }

    public void vibrate(int duration) {
        if (vibrationAllowed && vibrationReady) {
            Gdx.input.vibrate(duration);
            lastVibrationTime = TimeUtils.millis();
            vibrationReady = false;
        }
    }

    void createCoinEffect(int scoreDelta, float x, float y) {
        hudCoinEffect.initialize(scoreDelta, Rules.Misc.ScoreEffect.X, Rules.Hud.Stats.Coins.Y);
        if (x > -1 && y > -1) {
            CoinGainEffect effect = Pools.get(CoinGainEffect.class).obtain();
            hud.addActor(effect);
            effect.init(x, y, scoreDelta);
        }
    }


    void createScoreEffect(int scoreDelta) {
        scoreEffect.initialize(scoreDelta, Rules.Misc.ScoreEffect.X, Rules.Hud.Stats.Score.Y);
    }

    HudCoinEffect getScoreEffect() {
        return scoreEffect;
    }

    HudCoinEffect getHudCoinEffect() {
        return hudCoinEffect;
    }

    public void activateBioHazardSmoke() {
        if (biohazard == null) {
            biohazard = factories.getMiscFactory().createIndependentEffect(MiscFactory.IndependentEffectType.BIOHAZARD_SMOKE, 0, Rules.Level.GROUND_Y);
            Timer.schedule(TASK_DEACTIVATE_BIOHAZARD, Rules.Player.Bonus.BIOHAZARD_DURATION);
            Parastrike.getSoundPlayer().playSound(SFX.Misc.FLAME_HIT);
        }
    }

    private final Timer.Task TASK_DEACTIVATE_BIOHAZARD = new Timer.Task() {
        @Override
        public void run() {
            if (biohazard != null) {
                biohazard.onDestroy();
                biohazard = null;
            }
        }
    };

    public void activateAtomicFlash() {
        atomicFlash = 1;
        shakeScreen(Rules.Level.GlobalEffects.Quake.BIG_QUAKE_INTERVAL, Rules.Level.GlobalEffects.Quake.SMALL_DECAY, false);
        Parastrike.getSoundPlayer().playSound(SFX.Player.Bonus.ATOM);
    }


    float getAtomicFlash() {
        if (!epilepsyAllowed) {
            return 0;
        }
        return atomicFlash;
    }

    private final Timer.Task TASK_CREATE_MEDIC_YAK = new Timer.Task() {
        @Override
        public void run() {
            if (painter.getAlarmState()) {
                Timer.schedule(TASK_CREATE_MEDIC_YAK, Rules.Enemies.AirCrafts.Yak.MedicYak.INTERVAL_APPEARANCE);
                factories.getEnemyFactory().createEnemy(EnemyFactory.EnemyType.MEDIC_YAK, Rules.System.Resolution.WIDTH_TARGET_RESOLUTION / 2, Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION + 200);
            }
        }
    };

    TextureAtlas.AtlasRegion getLightRegion(String region) {
        if (region == Assets.GFX.Sheets.ImagesNames.LIGHT) {
            return lightRegion;
        } else {
            return lampLightRegion;
        }
    }

    public boolean isBioHazardSmokeExists() {
        return biohazard != null;
    }

    public ArrayMap<String, ParticleEffectPool> getEffectsPools() {
        return effectsPools;
    }

    public void createBossHealthBar(Boss boss) {
        painter.setBoss(boss);
    }

}
