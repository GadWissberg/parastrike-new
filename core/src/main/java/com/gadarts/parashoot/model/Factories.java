package com.gadarts.parashoot.model;

import com.gadarts.parashoot.model.object_factories.*;
import com.gadarts.parashoot.screens.WarScreen;

public class Factories {
    private BonusFactory bonusFactory;
    private BulletFactory bulletFactory;
    private MiscFactory miscFactory;
    private EnemyFactory enemyFactory;
    private SideKickFactory sideKickFactory;

    public Factories(WarScreen warScreen) {
        PlayerHandler playerHandler = warScreen.getPlayerHandler();
        miscFactory = new MiscFactory(this, playerHandler);
        bonusFactory = new BonusFactory(this, playerHandler, warScreen.getHud());
        bulletFactory = new BulletFactory(this, playerHandler);
        enemyFactory = new EnemyFactory(this, playerHandler);
        sideKickFactory = new SideKickFactory(this,warScreen);
    }

    public SideKickFactory getSideKickFactory() {
        return sideKickFactory;
    }

    public MiscFactory getMiscFactory() {
        return miscFactory;
    }

    public BonusFactory getBonusFactory() {
        return bonusFactory;
    }

    public BulletFactory getBulletFactory() {
        return bulletFactory;
    }

    public EnemyFactory getEnemyFactory() {
        return enemyFactory;
    }

    public void setElements(WarScreenElements mechanics) {
        miscFactory.setElements(mechanics);
        bonusFactory.setElements(mechanics);
        bulletFactory.setElements(mechanics);
        enemyFactory.setElements(mechanics);
        sideKickFactory.setElements(mechanics);
    }
}
