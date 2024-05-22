package com.gadarts.parashoot.model.object_factories;

import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.paratroopers.Parachute;
import com.gadarts.parashoot.level_model.OrnamentAppearance;
import com.gadarts.parashoot.level_model.OrnamentType;
import com.gadarts.parashoot.misc.IndependentEffect;
import com.gadarts.parashoot.misc.Misc;
import com.gadarts.parashoot.misc.stuff.*;
import com.gadarts.parashoot.model.*;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.Electricity;

public class MiscFactory extends ObjectFactory {


    public enum MiscType {
        ELECTRICITY, PARACHUTE, FLYING_PART, BIG_SCOUT_PART, BODY_PART, ALLY_PLANE, ALLY_BIOHAZARD_BOMB, ALLY_ATOMIC_BOMB
    }

    public MiscFactory(Factories factories, PlayerHandler playerHandler) {
        super(factories, playerHandler);
    }

    public Misc createMisc(MiscType type) {
        return createMisc(type, 0, 0);
    }

    public Misc createMisc(MiscType type, float x, float y) {
        return createMisc(type, x, y, 0);
    }

    public Ornament createOrnament(OrnamentAppearance ornamentAppearance) {
        return createOrnament(ornamentAppearance, ornamentAppearance.getX(), Rules.Level.GROUND_Y);
    }

    public Ornament createOrnament(OrnamentAppearance ornamentAppearance, int x, int y) {
        Ornament ornament = Pools.obtain(Ornament.class);
        OrnamentType ornamentType = ornamentAppearance.getOrnamentType();
        ornament.init(Parastrike.getAssetsManager().get(Assets.GFX.Sheets.InGame.Misc.Ornaments.DATA_FILE, TextureAtlas.class), ornamentType.getRegion(), x, y, getWarScreenElements(), ornamentType.getSpeed(), ornamentType.getFrameDuration(), ornamentType.getSound(), ornamentType.isFacingDirection());
        createOrnamentLight(ornament, ornamentType);
        getWarScreenElements().addObjectToMap(ornament, Rules.System.GameObjectTypes.MISC, ornamentType.getSpeed() > 0);
        return ornament;
    }

    private void createOrnamentLight(Ornament ornament, OrnamentType ornamentType) {
        String lightRegion = ornamentType.getLightRegion();
        if (getWarScreenElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT && lightRegion != null) {
            createLight(ornament, lightRegion);
        }
    }

    public Misc createMisc(MiscType type, float x, float y, float direction) {
        Misc gameObject = null;
        int listName = -1;
        WarScreenElements elements = getWarScreenElements();
        switch (type) {

            case ELECTRICITY:
                Electricity electricity = Pools.obtain(Electricity.class);
                electricity.init(x, y, direction, elements);
                gameObject = electricity;
                break;

            case ALLY_PLANE:
                AllyPlane allyPlane = Pools.obtain(AllyPlane.class);
                allyPlane.init(elements, getFactories());
                gameObject = allyPlane;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case ALLY_BIOHAZARD_BOMB:
                AllyFallingBomb allyBomb = Pools.obtain(AllyFallingBomb.class);
                allyBomb.init(elements, x, y, getFactories());
                gameObject = allyBomb;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case ALLY_ATOMIC_BOMB:
                AllyFallingBomb allyAtomBomb = Pools.obtain(AllyFallingBomb.class);
                allyAtomBomb.init(elements, x, y, getFactories());
                allyAtomBomb.setAtomic(true);
                gameObject = allyAtomBomb;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case PARACHUTE:
                Parachute parachute = Pools.obtain(Parachute.class);
                parachute.init(elements);
                gameObject = parachute;
                break;

            case FLYING_PART:
                FlyingPart flyingPart = Pools.obtain(FlyingPart.class);
                flyingPart.init(Assets.GFX.Sheets.ImagesNames.PART, x, y, direction, Rules.Misc.FlyingPart.FLYING_PART_SPEED, elements);
                gameObject = flyingPart;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case BIG_SCOUT_PART:
                FlyingPart bigScoutPart = Pools.obtain(FlyingPart.class);
                bigScoutPart.init(Assets.GFX.Sheets.ImagesNames.BIG_PART, x, y, direction, Rules.Misc.FlyingPart.BIG_FLYING_PART_SPEED, elements);
                bigScoutPart.emitSmoke(Assets.Configs.ParticleEffects.SMALL_SMOKE);
                gameObject = bigScoutPart;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case BODY_PART:
                FlyingPart bodyPart = Pools.obtain(FlyingPart.class);
                bodyPart.init(x, y, direction, Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.BODY_PART_SPEED, elements);
                bodyPart.setRandomFrames(Assets.GFX.Sheets.ImagesNames.BODY_PART_1, Assets.GFX.Sheets.ImagesNames.BODY_PART_2);
                elements.getEffectsManager().createEffect(Assets.Configs.ParticleEffects.BLOOD, bodyPart);
                gameObject = bodyPart;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

        }
        if (listName != -1) {
            elements.addObjectToMap(gameObject, listName);
        }
        return gameObject;
    }

    public void createFlyingParts(MiscType gameObjectId, float x, float y, int numberOfTimes) {
        for (int i = 0; i < numberOfTimes; i++) {
            float direction = (float) (180 * Math.random());
            createMisc(gameObjectId, x, y, direction);
        }
    }

    public enum IndependentEffectType {
        FIRE, BURNING_THING, STARS, DOME_BLAST, FLYING_CARTRIDGES, DAMAGE_SMOKE, BIG_DAMAGE_SMOKE, DEAD_SMOKE, SHORT_SMOKE, MISSILE_LAUNCH_SMOKE, WHITE_SMALL_SMOKE_UP, WHITE_BIG_SMOKE_UP, RICOCHET, ATOMIC_EXPLOSION, INDEPENDENT_EFFECT, RAIN, SNOW, LOADING_PLASMA, BIOHAZARD_SMOKE

    }

    public IndependentEffect createIndependentEffect(IndependentEffectType type, float x, float y) {
        IndependentEffect gameObject = null;
        int listName = -1;
        switch (type) {
            case BURNING_THING:
                IndependentEffect burningThing = Pools.obtain(IndependentEffect.class);
                burningThing.init(x, y, Assets.Configs.ParticleEffects.SMALL_FIRE, Rules.Misc.Fire.FIRE_DURATION, SFX.Misc.FIRE, getWarScreenElements());
                burningThing.setGravity(true);
                if (getWarScreenElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT) {
                    createLight(burningThing);
                }
                gameObject = burningThing;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case FIRE:
                Fire fire = Pools.obtain(Fire.class);
                fire.init(x, y, Assets.Configs.ParticleEffects.FIRE, Rules.Misc.Fire.FIRE_DURATION, SFX.Misc.FIRE, getWarScreenElements());
                fire.setBlastRadius(Rules.Misc.Fire.FIRE_RADIUS);
                fire.setGravity(true);
                if (getWarScreenElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT) {
                    createLight(fire);
                }
                gameObject = fire;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case STARS:
                IndependentEffect stars = Pools.obtain(IndependentEffect.class);
                stars.init(x, y, Assets.Configs.ParticleEffects.STARS, 0, getWarScreenElements());
                gameObject = stars;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case ATOMIC_EXPLOSION:
                IndependentEffect atomic = Pools.obtain(IndependentEffect.class);
                atomic.init(x, y, Assets.Configs.ParticleEffects.ATOMIC_EXPLOSION, 16, getWarScreenElements());
                atomic.setBlastRadius(Rules.Misc.HugeExplosion.BLAST_RADIUS);
                gameObject = atomic;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case DOME_BLAST:
                IndependentEffect domeBlast = Pools.obtain(IndependentEffect.class);
                domeBlast.init(x, y, Assets.Configs.ParticleEffects.DOME_BLAST, getWarScreenElements());
                gameObject = domeBlast;
                if (getWarScreenElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT) {
                    createLight(domeBlast);
                }
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case FLYING_CARTRIDGES:
                IndependentEffect flyingCartridges = Pools.obtain(IndependentEffect.class);
                flyingCartridges.init(x, y, Assets.Configs.ParticleEffects.FLYING_CARTRIDGES, 0, null, getWarScreenElements());
                flyingCartridges.setGravity(false);
                gameObject = flyingCartridges;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case DAMAGE_SMOKE:
                IndependentEffect damageSmoke = Pools.obtain(IndependentEffect.class);
                damageSmoke.init(x, y, Assets.Configs.ParticleEffects.DAMAGE_SMOKE, getWarScreenElements());
                damageSmoke.setGravity(false);
                gameObject = damageSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case BIOHAZARD_SMOKE:
                IndependentEffect biohazard = Pools.obtain(IndependentEffect.class);
                biohazard.init(x, y, Assets.Configs.ParticleEffects.BIOHAZARD_SMOKE, -1, getWarScreenElements());
                biohazard.setGravity(false);
                gameObject = biohazard;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case BIG_DAMAGE_SMOKE:
                IndependentEffect bigDamageSmoke = Pools.obtain(IndependentEffect.class);
                bigDamageSmoke.init(x, y, Assets.Configs.ParticleEffects.BIG_DAMAGE_SMOKE, getWarScreenElements());
                bigDamageSmoke.setGravity(false);
                gameObject = bigDamageSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case DEAD_SMOKE:
                IndependentEffect deadSmoke = Pools.obtain(IndependentEffect.class);
                deadSmoke.init(x, y, Assets.Configs.ParticleEffects.DEAD_SMOKE, Rules.Misc.DEAD_SMOKE_DURATION, getWarScreenElements());
                deadSmoke.setGravity(false);
                gameObject = deadSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case SHORT_SMOKE:
                IndependentEffect shortSmoke = Pools.obtain(IndependentEffect.class);
                shortSmoke.init(x, y, Assets.Configs.ParticleEffects.DAMAGE_SMOKE, Rules.Misc.SHORT_SMOKE_DURATION, getWarScreenElements());
                shortSmoke.setGravity(false);
                gameObject = shortSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case LOADING_PLASMA:
                IndependentEffect loadingPlasma = Pools.obtain(IndependentEffect.class);
                loadingPlasma.init(x, y, Assets.Configs.ParticleEffects.LOADING_PLASMA, Rules.Misc.SHORT_SMOKE_DURATION, getWarScreenElements());
                loadingPlasma.setGravity(false);
                gameObject = loadingPlasma;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case MISSILE_LAUNCH_SMOKE:
                IndependentEffect missileLaunchSmoke = Pools.obtain(IndependentEffect.class);
                missileLaunchSmoke.init(x, y, Assets.Configs.ParticleEffects.MISSILE_LAUNCH_SMOKE, Rules.Misc.MISSILE_LAUNCH_SMOKE_DURATION, getWarScreenElements());
                missileLaunchSmoke.setGravity(false);
                gameObject = missileLaunchSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case WHITE_SMALL_SMOKE_UP:
                IndependentEffect whiteSmallSmoke = Pools.obtain(IndependentEffect.class);
                whiteSmallSmoke.init(x, y, Assets.Configs.ParticleEffects.WHITE_SMALL_SMOKE_UP, Rules.Misc.Fire.FIRE_DURATION, getWarScreenElements());
                whiteSmallSmoke.setGravity(false);
                gameObject = whiteSmallSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case WHITE_BIG_SMOKE_UP:
                IndependentEffect whiteBigSmoke = Pools.obtain(IndependentEffect.class);
                whiteBigSmoke.init(x, y, Assets.Configs.ParticleEffects.WHITE_BIG_SMOKE_UP, Rules.Misc.WHITE_BIG_SMOKE_DURATION, getWarScreenElements());
                whiteBigSmoke.setGravity(false);
                gameObject = whiteBigSmoke;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case RICOCHET:
                IndependentEffect ricochet = Pools.obtain(IndependentEffect.class);
                ricochet.init(x, y, Assets.Configs.ParticleEffects.RICOCHET, Rules.Misc.RICOCHET_DURATION, getWarScreenElements());
                ricochet.setGravity(false);
                gameObject = ricochet;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case INDEPENDENT_EFFECT:
                IndependentEffect effect = Pools.obtain(IndependentEffect.class);
                effect.init(x, y, null, 0, null, getWarScreenElements());
                gameObject = effect;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case RAIN:
                IndependentEffect rain = Pools.obtain(IndependentEffect.class);
                rain.init(x, y, null, -1, null, getWarScreenElements());
                rain.setEffect(Assets.Configs.ParticleEffects.RAIN);
                gameObject = rain;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

            case SNOW:
                IndependentEffect snow = Pools.obtain(IndependentEffect.class);
                snow.init(x, y, null, -1, null, getWarScreenElements());
                snow.setEffect(Assets.Configs.ParticleEffects.SNOW);
                gameObject = snow;
                listName = Rules.System.GameObjectTypes.MISC;
                break;

        }
        //noinspection ConstantConditions
        if (listName != -1) {
            getWarScreenElements().addObjectToMap(gameObject, listName);
        }
        return gameObject;
    }

    public enum ExplosionType {
        EXPLOSION, SHOCK_WAVE_BLAST, BIG_EXPLOSION_UP, HUGE_EXPLOSION_UP, HUGE_EXPLOSION, PURPLE_EXPLOSION, SMALL_EXPLOSION, SMALL_PURPLE_EXPLOSION, SMALL_BLUE_EXPLOSION, SMALL_GREEN_EXPLOSION, BULLET_IMPACT
    }

    public Explosion createExplosion(ExplosionType type, float x, float y) {
        Explosion gameObject = null;
        int listName = -1;
        switch (type) {
            case EXPLOSION:
                Explosion explosion = Pools.obtain(Explosion.class);
                explosion.init(Assets.GFX.Sheets.ImagesNames.BIG_EXP, x, y, Assets.Configs.ParticleEffects.EXPLOSION, getWarScreenElements());
                explosion.setBlastRadius(Rules.Misc.Explosion.BLAST_RADIUS);
                gameObject = explosion;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                createEffect(Assets.Configs.ParticleEffects.BLAST_RING, gameObject);
                createEffect(Assets.Configs.ParticleEffects.BLAST_HORIZONTAL_RING, gameObject);
                if (getWarScreenElements().getCurrentLevel().getSky() == Rules.Level.GlobalEffects.SkyType.NIGHT) {
                    createLight(explosion);
                }
                break;

            case SHOCK_WAVE_BLAST:
                Explosion shockWaveBlast = Pools.obtain(Explosion.class);
                shockWaveBlast.init(Assets.GFX.Sheets.ImagesNames.SMALL_EXPLOSION, x, y, Assets.Configs.ParticleEffects.SHOCK_WAVE_BLAST, getWarScreenElements());
                shockWaveBlast.setBlastRadius(Rules.Misc.Explosion.BLAST_RADIUS);
                gameObject = shockWaveBlast;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                createEffect(Assets.Configs.ParticleEffects.BLAST_RING, gameObject);
                break;

            case BIG_EXPLOSION_UP:
                Explosion bigExplosionUp = Pools.obtain(Explosion.class);
                bigExplosionUp.init(Assets.GFX.Sheets.ImagesNames.BIG_EXP, x, y, Assets.Configs.ParticleEffects.BIG_EXPLOSION_UP, getWarScreenElements());
                bigExplosionUp.setBlastRadius(Rules.Misc.Explosion.BLAST_RADIUS);
                getWarScreenElements().getEffectsManager().shakeScreen(Rules.Level.GlobalEffects.Quake.SMALL_QUAKE_INTERVAL);
                gameObject = bigExplosionUp;
                createEffect(Assets.Configs.ParticleEffects.BIG_BLAST_RING, gameObject);
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case HUGE_EXPLOSION_UP:
                Explosion hugeExplosionUp = Pools.obtain(Explosion.class);
                hugeExplosionUp.init(Assets.GFX.Sheets.ImagesNames.BIG_EXP, x, y, Assets.Configs.ParticleEffects.HUGE_EXPLOSION_UP, getWarScreenElements());
                hugeExplosionUp.setBlastRadius(Rules.Misc.HugeExplosion.BLAST_RADIUS);
                gameObject = hugeExplosionUp;
                getWarScreenElements().getEffectsManager().shakeScreen(Rules.Level.GlobalEffects.Quake.BIG_QUAKE_INTERVAL);
                createEffect(Assets.Configs.ParticleEffects.BIG_BLAST_RING, gameObject);
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case HUGE_EXPLOSION:
                Explosion hugeExplosion = Pools.obtain(Explosion.class);
                String randomParticle = (Math.random() >= 0.5) ? Assets.Configs.ParticleEffects.HUGE_EXPLOSION : Assets.Configs.ParticleEffects.HUGE_EXPLOSION_2;
                hugeExplosion.init(Assets.GFX.Sheets.ImagesNames.BIG_EXP, x, y, randomParticle, getWarScreenElements());
                hugeExplosion.setBlastRadius(Rules.Misc.HugeExplosion.BLAST_RADIUS);
                getWarScreenElements().getEffectsManager().shakeScreen(Rules.Level.GlobalEffects.Quake.BIG_QUAKE_INTERVAL);
                gameObject = hugeExplosion;
                createEffect(Assets.Configs.ParticleEffects.BLAST_HORIZONTAL_RING, gameObject);
                createEffect(Assets.Configs.ParticleEffects.BIG_BLAST_RING, gameObject);
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case PURPLE_EXPLOSION:
                Explosion purpleExplosion = Pools.obtain(Explosion.class);
                purpleExplosion.init(Assets.GFX.Sheets.ImagesNames.PURPLE_EXPLOSION, x, y, Assets.Configs.ParticleEffects.PURPLE_EXPLOSION, getWarScreenElements());
                purpleExplosion.setBlastRadius(Rules.Misc.Explosion.BLAST_RADIUS);
                gameObject = purpleExplosion;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                break;

            case SMALL_EXPLOSION:
                Explosion smallExplosion = Pools.obtain(Explosion.class);
                smallExplosion.init(Assets.GFX.Sheets.ImagesNames.SMALL_EXPLOSION, x, y, null, getWarScreenElements());
                gameObject = smallExplosion;
                listName = Rules.System.GameObjectTypes.MISC;
                createEffect(Assets.Configs.ParticleEffects.SMALL_BLAST_RING, gameObject);
                break;

            case SMALL_PURPLE_EXPLOSION:
                Explosion smallPurpleExplosion = Pools.obtain(Explosion.class);
                smallPurpleExplosion.init(Assets.GFX.Sheets.ImagesNames.SMALL_PURPLE_EXPLOSION, x, y, null, getWarScreenElements());
                gameObject = smallPurpleExplosion;
                listName = Rules.System.GameObjectTypes.MISC;
                createEffect(Assets.Configs.ParticleEffects.SMALL_BLAST_RING, gameObject);
                break;

            case SMALL_BLUE_EXPLOSION:
                Explosion smallBlueExplosion = Pools.obtain(Explosion.class);
                smallBlueExplosion.init(Assets.GFX.Sheets.ImagesNames.SMALL_BLUE_EXPLOSION, x, y, null, getWarScreenElements());
                smallBlueExplosion.setBlastRadius(Rules.Misc.Explosion.BLAST_RADIUS);
                gameObject = smallBlueExplosion;
                listName = Rules.System.GameObjectTypes.EXPLOSIONS;
                createEffect(Assets.Configs.ParticleEffects.BIG_BLAST_RING, gameObject);
                createEffect(Assets.Configs.ParticleEffects.PLASMA_BLAST, gameObject);
                break;

            case SMALL_GREEN_EXPLOSION:
                Explosion smallGreenExplosion = Pools.obtain(Explosion.class);
                smallGreenExplosion.init(Assets.GFX.Sheets.ImagesNames.SMALL_GREEN_EXPLOSION, x, y, null, getWarScreenElements());
                gameObject = smallGreenExplosion;
                listName = Rules.System.GameObjectTypes.MISC;
                createEffect(Assets.Configs.ParticleEffects.SMALL_BLAST_RING, gameObject);
                break;

            case BULLET_IMPACT:
                Explosion bulletImpact = Pools.obtain(Explosion.class);
                bulletImpact.init(Assets.GFX.Sheets.ImagesNames.BULLET_IMPACT, x, y, Assets.Configs.ParticleEffects.BULLET_IMPACT, getWarScreenElements());
                gameObject = bulletImpact;
                listName = Rules.System.GameObjectTypes.MISC;
                createEffect(Assets.Configs.ParticleEffects.DAMAGE_SMOKE, gameObject);
                break;

        }
        getWarScreenElements().addObjectToMap(gameObject, listName);
        return gameObject;
    }

    public Explosion createExplosion(ExplosionType explosion, float x, float y, float width, float height) {
        float randomX = (float) (x + Math.random() * width);
        float randomY = (float) (y + Math.random() * height);
        return createExplosion(explosion, randomX, randomY);
    }
}

