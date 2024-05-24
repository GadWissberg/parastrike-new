package com.gadarts.parashoot.utils;

import com.badlogic.gdx.Application;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.level_model.Scene;
import com.gadarts.parashoot.model.object_factories.EnemyFactory;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 05/08/2015.
 */

public class GameSettings {
    public static final boolean SHOW_AND_LOG_FPS = false;
    public static final boolean SEND_SCORE = false;
    public static final boolean ALLOW_BUG_REPORTING = false;
    public static boolean SOUND_TOGGLE = true;
    public static final boolean SKIP_INTRO = false;
    public static final boolean ENEMY_TESTING = false;
    public static final boolean ALLOW_ENEMIES = true;
    public static final EnemyFactory.EnemyType CREATE_ONLY_SPECIFIC_ENEMY = null;
    public static final int FORCE_BEGIN_COINS = -1;
    public static final int FORCE_BEGIN_STARS = -1;
    public static final boolean LEVEL_MANIPULATION_MODE = false;
    public static final Scene LEVEL_MANIPULATION_UNLOCK_SCENE = Scene.MEADOW;
    public static final int LEVEL_MANIPULATION_LEVEL_UNLOCK = 7;
    public static boolean PLAY_MUSIC = true;
    public static final boolean GOD_MODE = false;
    public static final boolean ALWAYS_PLAY_TUTORIAL = false;
    public static final boolean SHOW_TABLES_LINES = false;
    public static final int GDX_DEBUG_LEVEL = Application.LOG_DEBUG;
    public static final int ASSET_MANAGER_DEBUG_LEVEL = Application.LOG_ERROR;
    public static final boolean ALLOW_IAP_DEBUG = false;
    public static final boolean SHOW_SPATIAL_GRID = false;
    public static final boolean SOUND_RANDOM_PITCH = true;
    public static final BulletType FORCE_BEGIN_CANNON = null;
    public static final boolean ALLOW_PARATROOPERS = true;
    public static final boolean ALLOW_PARTICLES = true;
    public static final boolean GL_PROFILE_ENABLED = false;
    public static final boolean ALLOW_LIGHTS = true;
    public static final Rules.Level.GlobalEffects.WeatherTypes FORCE_WEATHER = null;
    public static final boolean TEST_MENU_ACTIVE = false;
    public static final Parastrike.MenuType BEGIN_MENU_SCREEN = Parastrike.MenuType.INTRO;
}
