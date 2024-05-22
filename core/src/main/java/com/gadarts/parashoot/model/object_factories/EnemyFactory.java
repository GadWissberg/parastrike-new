package com.gadarts.parashoot.model.object_factories;

import com.badlogic.gdx.utils.Pools;
import com.gadarts.parashoot.enemies.Enemy;
import com.gadarts.parashoot.enemies.aircrafts.*;
import com.gadarts.parashoot.enemies.ground_crafts.APC;
import com.gadarts.parashoot.enemies.ground_crafts.Tank;
import com.gadarts.parashoot.enemies.paratroopers.BazookaGuy;
import com.gadarts.parashoot.enemies.paratroopers.Chaingunner;
import com.gadarts.parashoot.enemies.paratroopers.Infantry;
import com.gadarts.parashoot.model.Factories;
import com.gadarts.parashoot.model.ObjectFactory;
import com.gadarts.parashoot.model.PlayerHandler;
import com.gadarts.parashoot.model.WarScreenElements;
import com.gadarts.parashoot.player.bunker.Bunker;
import com.gadarts.parashoot.utils.GameUtils;
import com.gadarts.parashoot.utils.Rules;

import static com.gadarts.parashoot.model.object_factories.EnemyFactory.EnemyType.BOSS_CANNON;
import static com.gadarts.parashoot.utils.Rules.Enemies.AirCrafts.Boss.Cannon.*;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION;
import static com.gadarts.parashoot.utils.Rules.System.Resolution.WIDTH_TARGET_RESOLUTION;

public class EnemyFactory extends ObjectFactory {

    public enum EnemyType {
        BASIC_SCOUT, SCOUT, ZEPPELIN, BALLISTIC, YAK, SUPER_YAK, MEDIC_YAK, APACHE, BONUS_SCOUT,
        OSPREY, APC, TANK, INFANTRY, CHAINGUNNER, BAZOOKA_GUY, BOSS, BOSS_CANNON
    }

    public EnemyFactory(Factories factories, PlayerHandler playerHandler) {
        super(factories, playerHandler);
    }

    public Enemy createEnemy(EnemyType type, float x, float y) {
        return createEnemy(type, x, y, 0);
    }

    public Enemy createEnemy(EnemyType type, float x, float y, float direction) {
        Enemy gameObject = null;
        int listName = -1;
        WarScreenElements mechanics = getWarScreenElements();
        switch (type) {
            case BASIC_SCOUT:
                Scout basicScout = Pools.obtain(Scout.class);
                basicScout.init(x, y, direction, true, mechanics, getPlayerHandler(), getFactories());
                gameObject = basicScout;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case BOSS:
                Boss boss = Pools.obtain(Boss.class);
                boss.init(mechanics, getPlayerHandler(), getFactories());
                float height = boss.getHeight();
                boss.setPosition(WIDTH_TARGET_RESOLUTION / 2, HEIGHT_TARGET_RESOLUTION + height);
                createBossCannons(boss);
                gameObject = boss;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case BOSS_CANNON:
                BossCannon bossCannon = Pools.obtain(BossCannon.class);
                bossCannon.init(direction, mechanics, getPlayerHandler(), getFactories());
                gameObject = bossCannon;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case SCOUT:
                Scout scout = Pools.obtain(Scout.class);
                scout.init(x, y, direction, false, mechanics, getPlayerHandler(), getFactories());
                gameObject = scout;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case ZEPPELIN:
                Zeppelin zeppelin = Pools.obtain(Zeppelin.class);
                zeppelin.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                zeppelin.setTarget(getPlayerHandler().getBunker());
                gameObject = zeppelin;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case BALLISTIC:
                Ballistic ballistic = Pools.obtain(Ballistic.class);
                ballistic.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                gameObject = ballistic;
                ballistic.setDirection(90);
                ballistic.setY(Rules.Level.GROUND_Y);
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case YAK:
                Yak yak = Pools.obtain(Yak.class);
                yak.init((float) (WIDTH_TARGET_RESOLUTION * Math.random()), y, Yak.Type.REGULAR, mechanics, getPlayerHandler(), getFactories());
                yak.setTarget(getPlayerHandler().getBunker());
                gameObject = yak;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case SUPER_YAK:
                Yak superYak = Pools.obtain(Yak.class);
                superYak.init((float) (WIDTH_TARGET_RESOLUTION * Math.random()), y, Yak.Type.SUPER, mechanics, getPlayerHandler(), getFactories());
                gameObject = superYak;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case MEDIC_YAK:
                Yak medicYak = Pools.obtain(Yak.class);
                medicYak.init((float) (WIDTH_TARGET_RESOLUTION * Math.random()), y, Yak.Type.MEDIC, mechanics, getPlayerHandler(), getFactories());
                gameObject = medicYak;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case APACHE:
                Apache apache = Pools.obtain(Apache.class);
                apache.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                apache.setTarget(getPlayerHandler().getBunker());
                gameObject = apache;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case BONUS_SCOUT:
                BonusScout bonusScout = Pools.obtain(BonusScout.class);
                bonusScout.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                gameObject = bonusScout;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case OSPREY:
                Osprey osprey = Pools.obtain(Osprey.class);
                osprey.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                gameObject = osprey;
                listName = Rules.System.GameObjectTypes.AIR_CRAFTS;
                break;

            case APC:
                APC apc = Pools.obtain(APC.class);
                apc.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                gameObject = apc;
                listName = Rules.System.GameObjectTypes.GROUND_CRAFTS;
                break;

            case TANK:
                Tank tank = Pools.obtain(Tank.class);
                tank.init(x, y, direction, mechanics, getPlayerHandler(), getFactories());
                tank.setReloadTime(Rules.Enemies.GroundUnits.GroundCrafts.Tank.RELOAD_TIME);
                gameObject = tank;
                listName = Rules.System.GameObjectTypes.GROUND_CRAFTS;
                break;

            case INFANTRY:
                if (mechanics.getObjectsMap().get(Rules.System.GameObjectTypes.PARATROOPERS).size < Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.MAXIMUM_PARATROOPERS_ALLOWED) {
                    Infantry infantry = Pools.obtain(Infantry.class);
                    infantry.init(x, y, mechanics, getPlayerHandler(), getFactories());
                    infantry.setReloadTime(Rules.Enemies.GroundUnits.Paratroopers.UnitTypes.Infantry.RELOAD_TIME);
                    gameObject = infantry;
                    listName = Rules.System.GameObjectTypes.PARATROOPERS;
                }
                break;

            case CHAINGUNNER:
                if (mechanics.getObjectsMap().get(Rules.System.GameObjectTypes.PARATROOPERS).size < Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.MAXIMUM_PARATROOPERS_ALLOWED) {
                    Chaingunner chaingunner = Pools.obtain(Chaingunner.class);
                    chaingunner.init(x, y, mechanics, getPlayerHandler(), getFactories());
                    gameObject = chaingunner;
                    listName = Rules.System.GameObjectTypes.PARATROOPERS;
                }
                break;

            case BAZOOKA_GUY:
                if (mechanics.getObjectsMap().get(Rules.System.GameObjectTypes.PARATROOPERS).size < Rules.Enemies.GroundUnits.Paratroopers.GeneralAttributes.MAXIMUM_PARATROOPERS_ALLOWED) {
                    BazookaGuy bazookaGuy = Pools.obtain(BazookaGuy.class);
                    bazookaGuy.init(x, y, mechanics, getPlayerHandler(), getFactories());
                    gameObject = bazookaGuy;
                    listName = Rules.System.GameObjectTypes.PARATROOPERS;
                }
                break;

        }
        if (listName != -1) {
            mechanics.addObjectToMap(gameObject, listName);
        }
        return gameObject;
    }

    private void createBossCannons(Boss boss) {
        createBossCannon(boss, -CANNON_X_SMALL_DELTA, -CANNON_Y_SMALL_DELTA);
        createBossCannon(boss, -CANNON_X_BIG_DELTA, -CANNON_Y_SMALL_DELTA);
        createBossCannon(boss, CANNON_X_SMALL_DELTA, -CANNON_Y_SMALL_DELTA);
        createBossCannon(boss, CANNON_X_BIG_DELTA, -CANNON_Y_SMALL_DELTA);
    }

    private void createBossCannon(Boss boss, float bodyRelativeX, float bodyRelativeY) {
        Bunker bunker = getPlayerHandler().getBunker();
        float x2 = boss.getX() + bodyRelativeX;
        float y2 = boss.getY() + bodyRelativeY;
        float dirToPlayer = GameUtils.getDistanceBetweenObjectToPoint(bunker, x2, y2);
        BossCannon nearLeftCannon = (BossCannon) createEnemy(BOSS_CANNON, 0, 0, dirToPlayer);
        nearLeftCannon.setBodyRelativeX(bodyRelativeX);
        nearLeftCannon.setBodyRelativeY(bodyRelativeY);
        boss.addCannon(nearLeftCannon);
    }

}

