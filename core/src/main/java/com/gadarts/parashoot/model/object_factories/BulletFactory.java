package com.gadarts.parashoot.model.object_factories;

import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.enemies.AirCraft;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.ObjectFactory;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.*;

public class BulletFactory extends ObjectFactory {

    public BulletFactory(Factories factories, PlayerHandler playerHandler) {
        super(factories, playerHandler);
    }

    public Bullet createBullet(BulletType type, float x, float y, float direction) {
        Bullet gameObject = null;
        int listName = -1;
        WarScreenElements mechanics = getWarScreenElements();
        switch (type) {
            case CANNON_BALL:
                Bullet cannonBall = Pools.obtain(Bullet.class);
                cannonBall.init(Assets.GFX.Sheets.ImagesNames.CANNON_BALL, x, y, direction, Rules.Cannons.CannonBall.SPEED, Rules.Cannons.CannonBall.DAMAGE_VALUE, mechanics, getFactories());
                cannonBall.setAsPlayerBullet(true);
                cannonBall.setExplosion(MiscFactory.ExplosionType.SMALL_EXPLOSION, SFX.Misc.HIT);
                cannonBall.emitSmoke(Assets.Configs.ParticleEffects.CANNON_BALL_SMOKE);
                gameObject = cannonBall;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case SPREAD_CANNON_BALL:
                Bullet spreadBullet = Pools.obtain(Bullet.class);
                spreadBullet.init(Assets.GFX.Sheets.ImagesNames.TRIPLE_CANNON_BALL, x, y, direction, Rules.Cannons.SpreadCannonBall.SPEED, Rules.Cannons.SpreadCannonBall.DAMAGE_VALUE, mechanics, getFactories());
                spreadBullet.setAsPlayerBullet(true);
                spreadBullet.setExplosion(MiscFactory.ExplosionType.SMALL_EXPLOSION, SFX.Misc.HIT);
                spreadBullet.emitSmoke(Assets.Configs.ParticleEffects.SPREAD_CANNON_SMOKE);
                gameObject = spreadBullet;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case CHAIN_GUN_BULLET:
                Bullet chainGunBullet = Pools.obtain(Bullet.class);
                chainGunBullet.init(Assets.GFX.Sheets.ImagesNames.CHAIN_GUN_BULLET, x, y, direction, Rules.Cannons.ChainGun.SPEED, Rules.Cannons.ChainGun.DAMAGE_VALUE, mechanics, getFactories());
                chainGunBullet.setAsPlayerBullet(true);
                chainGunBullet.setExplosion(MiscFactory.ExplosionType.SMALL_EXPLOSION);
                gameObject = chainGunBullet;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case ROCKET:
                Bullet rocket = Pools.obtain(Bullet.class);
                rocket.init(Assets.GFX.Sheets.ImagesNames.ROCKET, x, y, direction, Rules.Cannons.RocketLauncher.SPEED, Rules.Cannons.RocketLauncher.DAMAGE_VALUE, mechanics, getFactories());
                rocket.setAsPlayerBullet(true);
                rocket.emitSmoke(Assets.Configs.ParticleEffects.SMALL_SMOKE);
                rocket.setExplosion(MiscFactory.ExplosionType.EXPLOSION, SFX.Misc.HIT);
                gameObject = rocket;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case HOMING_MISSILE:
                Bullet homingMissile = Pools.obtain(Bullet.class);
                homingMissile.init(Assets.GFX.Sheets.ImagesNames.HOMING_MISSILE, x, y, direction, Rules.Cannons.IronDome.SPEED, Rules.Cannons.IronDome.DAMAGE_VALUE, mechanics, getFactories());
                homingMissile.setAsPlayerBullet(true);
                if ((direction <= Rules.Cannons.IronDome.LEFT_MAX_ANGLE_FOR_HOMING && direction >= Rules.Cannons.IronDome.LEFT_MAX_ANGLE_FOR_HOMING) || direction > Rules.Cannons.IronDome.RIGHT_MIN_ANGLE_FOR_HOMING) {
                    AirCraft target = mechanics.getInteractionsManager().findHomeableAirCraftInRange();
                    homingMissile.setTarget(target);
                }
                homingMissile.emitSmoke(Assets.Configs.ParticleEffects.HOMING_MISSILE_SMOKE);
                homingMissile.setExplosion(MiscFactory.ExplosionType.SMALL_EXPLOSION, SFX.Misc.HIT);
                gameObject = homingMissile;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case NON_HOMING_MISSILE:
                Bullet nonMissile = Pools.obtain(Bullet.class);
                nonMissile.init(Assets.GFX.Sheets.ImagesNames.HOMING_MISSILE, x, y, direction, Rules.Cannons.IronDome.NON_HOMING_SPEED, Rules.Cannons.IronDome.DAMAGE_VALUE, mechanics, getFactories());
                nonMissile.setAsPlayerBullet(true);
                nonMissile.emitSmoke(Assets.Configs.ParticleEffects.HOMING_MISSILE_SMOKE);
                nonMissile.setExplosion(MiscFactory.ExplosionType.SMALL_EXPLOSION, SFX.Misc.HIT);
                gameObject = nonMissile;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case BAZOOKA_MISSILE:
                Bullet missile = Pools.obtain(Bullet.class);
                missile.init(Assets.GFX.Sheets.ImagesNames.HOMING_MISSILE, x, y, direction, Rules.Cannons.IronDome.SPEED, mechanics, getFactories());
                missile.setAsPlayerBullet(false);
                missile.emitSmoke(Assets.Configs.ParticleEffects.HOMING_MISSILE_SMOKE);
                missile.setExplosion(MiscFactory.ExplosionType.SMALL_EXPLOSION, SFX.Misc.HIT);
                gameObject = missile;
                listName = Rules.System.GameObjectTypes.ENEMY_BULLETS;
                break;

            case ZEPPELIN_BOMB:
                ZeppelinBomb zeppelinBomb = Pools.obtain(ZeppelinBomb.class);
                zeppelinBomb.init(x, y, direction, mechanics, getFactories());
                zeppelinBomb.setAsPlayerBullet(false);
                gameObject = zeppelinBomb;
                listName = Rules.System.GameObjectTypes.ENEMY_BULLETS;
                break;

            case ALLY_BOMB:
                ZeppelinBomb allyBomb = Pools.obtain(ZeppelinBomb.class);
                allyBomb.init(x, y, direction, mechanics, getFactories());
                allyBomb.setAsPlayerBullet(true);
                allyBomb.setDamageValue(Rules.Enemies.AirCrafts.Zeppelin.Hard.DAMAGE_VALUE);
                gameObject = allyBomb;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case BLASTER:
                BlasterBullet splitLaser = Pools.obtain(BlasterBullet.class);
                splitLaser.init(Assets.GFX.Sheets.ImagesNames.SPLIT_BULLET, x, y, direction, Rules.Cannons.Blaster.SPEED, Rules.Cannons.Blaster.DAMAGE_VALUE, mechanics, getFactories());
                splitLaser.setAsPlayerBullet(true);
                gameObject = splitLaser;
                splitLaser.emitSmoke(Assets.Configs.ParticleEffects.SMALL_PURPLE_SMOKE);
                splitLaser.setExplosion(MiscFactory.ExplosionType.PURPLE_EXPLOSION, SFX.Misc.HIT);
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case SMALL_SPLIT_BULLET:
                Bullet smallSplitLaser = Pools.obtain(Bullet.class);
                smallSplitLaser.init(Assets.GFX.Sheets.ImagesNames.SMALL_SPLIT_BULLET, x, y, direction, Rules.Cannons.Blaster.SMALL_BULLET_SPEED, Rules.Cannons.Blaster.SMALL_BULLET_DAMAGE_VALUE, mechanics, getFactories());
                smallSplitLaser.setAsPlayerBullet(true);
                smallSplitLaser.emitSmoke(Assets.Configs.ParticleEffects.SMALL_PURPLE_SMOKE);
                smallSplitLaser.setExplosion(MiscFactory.ExplosionType.SMALL_PURPLE_EXPLOSION, SFX.Misc.HIT);
                gameObject = smallSplitLaser;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case HOMING_PLASMA:
                Bullet homingPlasma = Pools.obtain(Bullet.class);
                homingPlasma.init(Assets.GFX.Sheets.ImagesNames.HOMING_PLASMA, x, y, 0, Rules.Player.SideKicks.Dome.BULLET_SPEED, Rules.Cannons.ShockWave.DAMAGE_VALUE, mechanics, getFactories());
                homingPlasma.setAsPlayerBullet(true);
                homingPlasma.emitSmoke(Assets.Configs.ParticleEffects.SMALL_BLUE_SMOKE);
                homingPlasma.setExplosion(MiscFactory.ExplosionType.SMALL_BLUE_EXPLOSION, SFX.Misc.HIT);
                homingPlasma.setHomingRotation(Rules.Player.SideKicks.Dome.HOMING_ROTATION);
                gameObject = homingPlasma;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case LASER:
                Bullet laser = Pools.obtain(Bullet.class);
                laser.init(Assets.GFX.Sheets.ImagesNames.RAPID_LASER, x, y, direction, Rules.Cannons.TwinLaser.SPEED, Rules.Cannons.TwinLaser.DAMAGE_VALUE, mechanics, getFactories(), false);
                laser.setAsPlayerBullet(true);
                laser.setExplosion(MiscFactory.ExplosionType.SMALL_GREEN_EXPLOSION);
                gameObject = laser;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case FLAME:
                Flame flame = Pools.obtain(Flame.class);
                flame.init(Assets.GFX.Sheets.ImagesNames.FLAME, x, y, direction, Rules.Player.SideKicks.Flamer.BULLET_SPEED, Rules.Player.SideKicks.Flamer.AttackStrength.STRENGTH, mechanics, getFactories());
                flame.setAsPlayerBullet(true);
                gameObject = flame;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case JAGUAR_BULLET:
                Bullet jaguarBullet = Pools.obtain(Bullet.class);
                jaguarBullet.init(Assets.GFX.Sheets.ImagesNames.AUTO_TURRET_BULLET, x, y, direction, Rules.Player.SideKicks.Minigunner.BULLET_SPEED, Rules.Player.SideKicks.Minigunner.AttackStrength.STRENGTH, false, mechanics, getFactories());
                jaguarBullet.setAsPlayerBullet(true);
                jaguarBullet.setExplosion(null);
                gameObject = jaguarBullet;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

            case ENEMY_MINIGUN_BULLET:
                EnemyMiniGunBullet enemyMinigunBullet = Pools.obtain(EnemyMiniGunBullet.class);
                enemyMinigunBullet.init(Assets.GFX.Sheets.ImagesNames.AUTO_TURRET_BULLET, x, y, direction, Rules.Player.SideKicks.Minigunner.BULLET_SPEED, mechanics, getFactories());
                enemyMinigunBullet.setAsPlayerBullet(false);
                enemyMinigunBullet.setExplosion(null);
                gameObject = enemyMinigunBullet;
                listName = Rules.System.GameObjectTypes.ENEMY_BULLETS;
                break;

            case SHOCK_WAVE:
                ShockWave shockWave = Pools.obtain(com.gadarts.parashoot.weapons.ShockWave.class);
                shockWave.init(x, y, direction, mechanics, getFactories());
                shockWave.setAsPlayerBullet(true);
                gameObject = shockWave;
                listName = Rules.System.GameObjectTypes.PLAYER_BULLETS;
                break;

        }
        //noinspection ConstantConditions
        if (listName != -1) {
            mechanics.addObjectToMap(gameObject, listName);
        }
        return gameObject;
    }

}

