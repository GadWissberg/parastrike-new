package com.gadarts.parashoot.utils;


import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.model.object_factories.WeaponType;
import com.gadarts.parashoot.weapons.BulletType;

import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.*;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.Shop.*;
import static com.gadarts.parashoot.assets.Assets.Strings.Menu.ShopImproved.AttributesDescriptions;

/**
 * Created by Gad on 05/02/2015.
 */
public final class Rules {

    public static final class Server {

        public static final String REQ_ID = "req_id";
        public static final String DAILY_GIFT = "daily_gift";
        public static final String RESPONSE_VALUE_TRUE = "true";
        public static final String PASS = "pass";
    }

    public static final class Cannons {
        public static final BulletType[] CANNONS_ORDER = {BulletType.CANNON_BALL, BulletType.SPREAD_CANNON_BALL, BulletType.CHAIN_GUN_BULLET, BulletType.ROCKET, BulletType.HOMING_MISSILE, BulletType.BLASTER, BulletType.LASER, BulletType.SHOCK_WAVE};

        public static final int MAXIMUM_LEVEL = 40;
        public static final int UPGRADE_UNIT = 1;
        public static final float MINIMAL_ATTACK_FLOAT = 0.5f;
        public static final int INITIAL_AMMO = 7;
        public static final int MAX_AMMO = 15;

        public final class OverHeat {
            public static final float OVER_HEAT_DECAY = 0.2f;
        }

        public final class CannonBall {
            public static final String NAME = "cannon_ball";
            public static final float SHOOTING_RATE = 500;
            public static final float SHOOTING_RATE_MIN = 350;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float DAMAGE_VALUE = 0.35f;
            public static final float DAMAGE_VALUE_MAX = 0.8f;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final float SPEED = 8;
            public static final float HEAT_USE = 2;
            public static final int UPGRADE_COST = 5;
            public static final int AMMO_COST = -1;
        }

        public final class SpreadCannonBall {
            public static final float OTHER_BULLET_DIRECTION_DIFFERENCE = 45;
            public static final String NAME = "triple_cannon";
            public static final float SHOOTING_RATE = 1000;
            public static final float SHOOTING_RATE_MIN = 500;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float DAMAGE_VALUE = 1.1f;
            public static final float DAMAGE_VALUE_MAX = 2.1f;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final float SPEED = 7;
            public static final int MIN_GENERATOR_REQ = 2;
            public static final float HEAT_USE = 33;
            public static final int UPGRADE_COST = 15;
            public static final int AMMO_COST = 200;
            public static final int COST = 4000;
        }

        public final class ChainGun {
            public static final String NAME = "chain_gun";
            public static final float TURRET_SHOOT_FRAME_DURATION = 0.05f;
            public static final float SHOOTING_RATE = 110;
            public static final float SHOOTING_RATE_MIN = 60;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float DAMAGE_VALUE = 0.21f;
            public static final float DAMAGE_VALUE_MAX = 0.27f;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final float SPEED = 18;
            public static final float HEAT_USE = 11;
            public static final int UPGRADE_COST = 25;
            public static final int AMMO_COST = 400;
            public static final int MIN_GENERATOR_REQ = 4;
            public static final int COST = 13000;
        }

        public final class RocketLauncher {
            public static final float DAMAGE_VALUE = 8;
            public static final float DAMAGE_VALUE_MAX = 12;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final String NAME = "rocket_launcher";
            public static final float SHOOTING_RATE = 1700;
            public static final float SHOOTING_RATE_MIN = 900;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float SPEED = 6;
            public static final float HEAT_USE = 55;
            public static final int UPGRADE_COST = 35;
            public static final int AMMO_COST = 800;
            public static final int COST = 20000;
            public static final int MIN_GENERATOR_REQ = 2;
        }

        public final class IronDome {
            public static final float DAMAGE_VALUE = 0.15f;
            public static final float DAMAGE_VALUE_MAX = 1.0f;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final String NAME = "missle_launcher";
            public static final float TURRET_SHOOT_FRAME_DURATION = 0.1f;
            public static final float HOMING_ROTATION = 1.5f;
            public static final float START_HOMING_DELAY = 0.1f;
            public static final float SHOOTING_RATE = 510;
            public static final float SHOOTING_RATE_MIN = 210;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float SPEED = 8;
            public static final float NON_HOMING_SPEED = 6;
            public static final float HEAT_USE = 40;
            public static final float TURRET_RANGE = 35;
            public static final int UPGRADE_COST = 45;
            public static final int AMMO_COST = 1000;
            public static final int COST = 28000;
            public static final int MIN_GENERATOR_REQ = 4;
            public static final float LEFT_MAX_ANGLE_FOR_HOMING = 175;
            public static final float RIGHT_MIN_ANGLE_FOR_HOMING = 5;
        }

        public final class Blaster {
            public static final float SPEED = 7;
            public static final float SMALL_BULLET_DAMAGE_VALUE = 3;
            public static final float SMALL_BULLET_SPEED = 10;
            public static final String NAME = "split_laser";
            public static final float SMALL_BULLET_CREATION_DELAY = 0.4f;
            public static final float SHOOTING_RATE = 1700;
            public static final float SHOOTING_RATE_MIN = 800;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float DAMAGE_VALUE = 6;
            public static final float DAMAGE_VALUE_MAX = 10;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final float HEAT_USE = 55;
            public static final int UPGRADE_COST = 55;
            public static final int AMMO_COST = 2000;
            public static final int COST = 47000;
            public static final int MIN_GENERATOR_REQ = 2;
        }

        public final class TwinLaser {
            public static final float DAMAGE_VALUE = 0.2f;
            public static final float DAMAGE_VALUE_MAX = 0.5f;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final String NAME = "twin_turret";
            public static final float SHOOTING_RATE = 200;
            public static final float SHOOTING_RATE_MIN = 100;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float SPEED = 10;
            public static final float TURRET_SHOOT_FRAME_DURATION = 0.1f;
            public static final int DISTANCE_FROM_CREATION_ORIGIN = 10;
            public static final float HEAT_USE = 32;
            public static final int UPGRADE_COST = 65;
            public static final int AMMO_COST = 3000;
            public static final int COST = 81000;
            public static final int MIN_GENERATOR_REQ = 6;
        }

        public final class ShockWave {
            public static final float DAMAGE_VALUE = 7;
            public static final float DAMAGE_VALUE_MAX = 20;
            public static final float DAMAGE_VALUE_UPGRADE = (DAMAGE_VALUE_MAX - DAMAGE_VALUE) / Cannons.MAXIMUM_LEVEL;
            public static final String NAME = "shock_wave";
            public static final float SHOOTING_RATE = 2000;
            public static final float SHOOTING_RATE_MIN = 500;
            public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
            public static final float HEAT_USE = 99;
            public static final int UPGRADE_COST = 70;
            public static final int AMMO_COST = 5000;
            public static final int COST = 137000;
            public static final int MIN_GENERATOR_REQ = 9;
        }
    }

    public final class System {

        public static final float OBJECT_SPEED_MULTIPLICATION = 50;
        public static final String TIME_EPOCH_URL = "http://currentmillis.com/time/seconds-since-unix-epoch.php";
        public static final int SPATIAL_WIDTH_DIVISION = 9;
        public static final int SPATIAL_HEIGHT_DIVISION = 7;
        public static final String SAVED_GAME_NAME = "parastrike_game_save";
        public static final long ADS_REQUEST_MIN_INTERVAL_SECONDS = 150;

        public final class FontsParameters {
            public static final int ARMY_BORDER_WIDTH = 2;

            public final class ArmyFontNames {
                public static final String PREFIX = "army_";
                public static final String HUGE = PREFIX + FontSizes.HUGE + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String BIG = PREFIX + FontSizes.BIG + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String MEDIUM = PREFIX + FontSizes.MEDIUM + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String SMALL = PREFIX + FontSizes.SMALL + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String TINY = PREFIX + FontSizes.TINY + "." + Assets.FontsFileNames.DATA_EXTENSION;
            }

            public final class DigitalFontNames {
                public static final String PREFIX = "digital_";
                public static final String HUGE = PREFIX + FontSizes.HUGE + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String BIG = PREFIX + FontSizes.BIG + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String MEDIUM = PREFIX + FontSizes.MEDIUM + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String SMALL = PREFIX + FontSizes.SMALL + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String TINY = PREFIX + FontSizes.TINY + "." + Assets.FontsFileNames.DATA_EXTENSION;
            }

            public final class RegularFontNames {
                public static final String PREFIX = "regular_";
                public static final String HUGE = PREFIX + FontSizes.HUGE + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String BIG = PREFIX + FontSizes.BIG + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String MEDIUM = PREFIX + FontSizes.MEDIUM + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String SMALL = PREFIX + FontSizes.SMALL + "." + Assets.FontsFileNames.DATA_EXTENSION;
                public static final String TINY = PREFIX + FontSizes.TINY + "." + Assets.FontsFileNames.DATA_EXTENSION;
            }

            public final class FontSizes {
                public static final int HUGE = 80;
                public static final int BIG = 60;
                public static final int MEDIUM = 40;
                public static final int SMALL = 30;
                public static final int TINY = 20;
            }
        }

        public final class Resolution {
            public static final int WIDTH_TARGET_RESOLUTION = 1280;
            public static final int HEIGHT_TARGET_RESOLUTION = 768;
        }

        public final class SFX {
            public static final float MUSIC_VOLUME = 1;
        }

        public final class GFX {

            public final class ShadesIds {
                public static final int NO_SHADE = 0;
                public static final int WHITE_SHADE = 1;
                public static final int RED_SHADE = 2;
            }
        }


        public final class GameObjectTypes {
            public static final int PLAYER_BULLETS = 0;
            public static final int AIR_CRAFTS = 1;
            public static final int MISC = 2;
            public static final int PARATROOPERS = 3;
            public static final int EXPLOSIONS = 4;
            public static final int BONUSES = 5;
            public static final int ENEMY_BULLETS = 6;
            public static final int GROUND_CRAFTS = 7;
        }

        public final class Analytics {
            public static final String ANALYTICS_KEY = "MKSFTN5S4PPX2GSVKYZ9";

            public final class Events {
                public static final String ENTERED_GAME = "entered_game";
                public static final String CANNON_UPGRADE = "cannon_upgrade";
                public static final String CANNON_PURCHASED = "cannon_purchased";
                public static final String SIDE_KICK_PURCHASED = "side_kick_purchased";
                public static final String BOMB_PURCHASED = "bomb_purchased";
                public static final String LEVEL_BEGIN = "level_begin";
                public static final String LEVEL_END = "level_end";
                public static final String IN_GAME = "in_game";
                public static final String USER_RATED = "user_rated";
            }

            public final class Attributes {
                public final class MenuScreen {
                    public static final String COINS = "coins";
                    public static final String RATING = "rating";
                }

                public final class InGame {
                    public static final String SELECTED_CANNON = "selected_cannon";
                    public static final String SELECTED_SIDE_KICK = "selected_side_kick";
                    public static final String LEVEL_NUMBER = "level_number";
                    public static final String LEVEL_SKILL = "level_skill";
                    public static final String SCORE = "score";
                    public static final String STATE = "state";
                }
            }
        }

        public final class Vibration {
            public static final int PAIN_VIBRATION_DURATION = 200;
            public static final int VIBRATION_INTERVALS = 10000;
        }

    }

    public static final class Level {
        public static final int GROUND_Y = 140;
        public static final float GRAVITY_DIRECTION = 270;
        public static final int BASIC_LEVEL_STAR_COINS_WORTH = 200;
        public static final float MIN_AMB_INTERVAL = 15;

        public final class Scenes {
            public final class Names {
                public static final String MILITARY_ZONE = "Restricted Area";
                public static final String ARCTIC = "Arctic Apocalypse";
                public static final String BEACH = "Shores of Hell";
                public static final String MEADOW = "Atom Heart Mother";
                public static final String CITY = "Urban Inferno";
                public static final String MOUNTAINS = "Scorched Earth";
                public static final String FOREST = "Black Forest";
                public static final String DESERT = "Deserted";
                public static final String BOSS = "Coming Soon...";
            }

            public final class Targets {
                public final class Coins {
                    public static final int MEADOW = 7000;
                    public static final int CITY = 12000;
                    public static final int MOUNTAINS = 22000;
                    public static final int FOREST = 33000;
                    public static final int DESERT = 41000;
                }

                public final class Stars {
                    public static final int ARCTIC = 14;
                    public static final int BEACH = 28;
                    public static final int MEADOW = 43;
                    public static final int CITY = 74;
                    public static final int MOUNTAINS = 96;
                    public static final int FOREST = 132;
                    public static final int DESERT = 160;
                    public static final int BOSS = 180;
                }
            }

            public static final float PLAY_SOUND_MINIMUM_INTERVAL = 30;

            public final class MovingOrnaments {
                public static final int BIRD_SPEED = 1;
                public static final int FAR_PLANE_SPEED = 4;
                public static final int MINIMUM_INTERVAL = 30;
                public static final int START_LEFT_SIDE = -125;
                public static final int START_RIGHT_SIDE = System.Resolution.WIDTH_TARGET_RESOLUTION + (START_LEFT_SIDE * -1);
                public static final int MINIMUM_Y = Level.GROUND_Y + 300;
                public static final int PENGUIN_SPEED = 1;
                public static final int BOAT_SPEED = 1;
                public static final int SHEEP_SPEED = 1;
                public static final float CAMEL_SPEED = 0.2f;
                public static final int TRUCK_SPEED = 3;
                public static final int POLICE_SPEED = 4;
                public static final int BOAT_Y = Rules.Level.GROUND_Y + 25;
                public static final float CAMEL_FRAME_DURATION = 0.2f;
            }
        }

        public final class LevelStructure {
            public static final float BEGIN_DELAY = 2;
            public static final float END_DELAY = 3;

            public final class EnemyAppearance {
                public final class Focus {
                    public static final String JSON_KEY = "focus";
                    public static final String DELAY = "delay";
                    public static final String MESSAGE = "message";
                    public static final String SHOW_FINGER = "show_finger";
                }

                public static final String TYPE = "type";
                public static final String TIMING = "timing";
                public static final String ALIGNMENT = "alignment";
                public static final String MINIMUM_SKILL = "minimum_skill";
            }

            public final class LevelAttributes {
                public static final String NAME = "name";
                public static final String LANDSCAPE = "landscape";
                public static final String OPENING_MESSAGE = "opening_message";
                public static final String LEVEL_COMPLETE_MESSAGE = "level_complete_message";
                public static final String SCENE = "scene";
                public static final String ALLOWED_BONUSES = "allowed_bonuses";
                public static final String ALLOWED_PARATROOPERS = "allowed_paratroopers";
                public static final String ENEMIES = "enemies";
                public static final String EVENTS = "events";
                public static final String SKY = "sky";
                public static final String WEATHER = "weather";
                public static final String MUSIC = "music";
                public static final String SHOW_STATISTICS = "show_statistics";
                public static final String ALLOW_GAIN = "allow_gain";
                public static final String SHOW_OPTIONS_BUTTON = "show_options_button";
                public static final String SHOW_BOMBS_BUTTON = "show_bombs_button";
                public static final String PLAY_GOOD_LUCK_TAUNT = "play_good_luck_taunt";
                public static final String ALLOW_FEATS = "allow_feats";
                public static final String ORNAMENTS = "ornaments";
                public static final String ORNAMENT = "ornament";
                public static final String AMBIENCE_SOUNDS = "ambience_sounds";
                public static final String X = "x";
                public static final String DELTA_Y = "delta_y";
                public static final String MOVING = "moving";
                public static final String FORCE_SIMPLE_CANNON_BALL = "force_simple_cannon_ball";
            }

        }

        public static final class GlobalEffects {
            public static final float DARKNESS = 0.3f;
            public static final float LIGHTNING_DURATION = 1;
            public static final int LIGHTNING_MIN_INTERVAL = 20;
            public static final int LIGHTNING_MAX_INTERVAL = 40;


            public enum WeatherTypes {
                REGULAR, RAIN, SNOW
            }

            public enum SkyType {
                DAY, AFTERNOON, NIGHT
            }

            public final class Quake {
                public static final float QUAKE_DURATION = 2;
                public static final int BIG_QUAKE_INTERVAL = 4;
                public static final int SMALL_QUAKE_INTERVAL = 2;
                public static final float DECAY = 0.05f;
                public static final float SMALL_DECAY = 0.01f;
                public static final float MAX_INTERVAL = 4;
                public static final float MINIMUM_INTERVAL = 1.5f;
            }
        }
    }

    public final class Misc {
        public static final int OUTSIDE_ENEMY_DISTANCE_LEFT_SIDE = -100;
        public static final int OUTSIDE_ENEMY_DISTANCE_RIGHT_SIDE = Rules.System.Resolution.WIDTH_TARGET_RESOLUTION + 100;
        public static final int MAX_FALLING_SPEED = 15;
        public static final float GENERAL_ANIMATION_FRAME_DURATION = 0.2f;
        public static final float DEAD_SMOKE_DURATION = 10;
        public static final float SHORT_SMOKE_DURATION = 1;
        public static final float MISSILE_LAUNCH_SMOKE_DURATION = 0.25f;
        public static final float LIGHT_PADDING = 50;
        public static final float RICOCHET_DURATION = 0.1f;
        public static final float FLASH_FADEOUT_PACE = 0.0015f;
        public static final float WHITE_BIG_SMOKE_DURATION = 10;

        public final class AllyPlane {
            public static final float ALLY_PLANE_SPEED = 2;
            public static final float ALLY_PLANE_RELOAD = 2;
        }

        public final class ScoreEffect {
            public static final float DURATION = 5;
            public static final float DURATION_TO_FADEOUT = 3;
            public static final float ALPHA_DELTA = 0.05f;
            public static final float X = Hud.Stats.Coins.X + 150;
        }

        public final class Fire {
            public static final float FIRE_DURATION = 2;
            public static final float FIRE_RADIUS = 100;
        }

        public final class Explosion {
            public static final float BLAST_RADIUS = 200;
        }

        public final class HugeExplosion {
            public static final float BLAST_RADIUS = 300;
        }

        public final class FlyingPart {
            public static final int BIG_FLYING_PART_SPEED = 3;
            public static final int FLYING_PART_SPEED = 5;
            public static final int FLYING_PART_ROTATION_SPEED = 5;
        }

        public final class Gravity {
            public static final float ACCELERATION = 0.2f;
            public static final int DIRECTION = 270;
        }

    }

    public final class Enemies {

        public final class GeneralAttributes {
            public static final float SHOOT_FRAME_DURATION = 0.1f;
            public static final int SHOOT_TIME = 1;
            public static final int AIRCRAFTS_Y_FROM_TOP_MIN = 100;
            public static final int AIRCRAFTS_Y_FROM_TOP = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - AIRCRAFTS_Y_FROM_TOP_MIN;
            public static final int MAXIMUM_LEVEL = 2;
        }

        public final class AirCrafts {
            public static final int SINK_SPEED_ACCELERATION = 3;
            public static final float CRASH_DEGREES_CHANGE = 0.6f;
            public static final float BONUS_CHANCES = 0.05f;
            public static final float PROPELLER_FRAME_DURATION = 0.05f;
            public static final int FLY_AWAY_DISTANCE_ABOVE_SKY = 200;

            public final class BasicScout {
                public final class Easy {
                    public static final float SPEED = 1;
                    public static final float HP = 0.35f;
                    public static final int COINS = 4;
                }

                public final class Medium {
                    public static final float SPEED = 2;
                    public static final float HP = 0.7f;
                    public static final int COINS = 8;
                }

                public final class Hard {
                    public static final float SPEED = 3;
                    public static final float HP = 1.5f;
                    public static final int COINS = 12;
                }

            }

            public final class Boss {
                public static final float SIGHT_SOUND_DELAY = 2;
                public static final float HEALTH_BAR_MAX_WIDTH = 600;
                public static final float HEALTH_BAR_HEIGHT = 100;
                public static final float HEALTH_BAR_Y = Level.GROUND_Y - 125;

                public final class Cannon {
                    public static final float CANNON_X_SMALL_DELTA = 150;
                    public static final float CANNON_X_BIG_DELTA = 325;
                    public static final float CANNON_Y_SMALL_DELTA = 100;
                    public static final float CANNON_MASK_SIZE = 40;

                    public final class Easy {
                        public static final int CANNON_COINS = 500;
                        public static final int CANNON_HP = 50;
                    }

                    public final class Medium {
                        public static final int CANNON_COINS = 1000;
                        public static final int CANNON_HP = 60;
                    }

                    public final class Hard {
                        public static final int CANNON_COINS = 2000;
                        public static final int CANNON_HP = 70;
                    }

                }

                public static final float SPEED = 0.8f;
                public static final int HP = 1;
                public static final int COINS = 16;
                public static final float SPEED_SLOWDOWN_DELTA = 0.04f;
            }

            public final class Scout {
                public final class Easy {
                    public static final float SPEED = 2;
                    public static final int HP = 1;
                    public static final int SECONDS_TO_CREATE_PARATROOPER = 4;
                    public static final int COINS = 16;
                }

                public final class Medium {
                    public static final float SPEED = 3;
                    public static final int HP = 2;
                    public static final int SECONDS_TO_CREATE_PARATROOPER = 3;
                    public static final int COINS = 20;
                }

                public final class Hard {
                    public static final float SPEED = 4;
                    public static final int HP = 3;
                    public static final int SECONDS_TO_CREATE_PARATROOPER = 2;
                    public static final int COINS = 24;
                }

                public static final int GIB_REACH = -2;
            }

            public final class Zeppelin {

                public final class Easy {
                    public static final int HP = 22;
                    public static final float DAMAGE_VALUE = 12;
                    public static final int COINS = 90;
                }

                public final class Medium {
                    public static final int HP = 27;
                    public static final float DAMAGE_VALUE = 22;
                    public static final int COINS = 110;
                }

                public final class Hard {
                    public static final int HP = 32;
                    public static final float DAMAGE_VALUE = 30;
                    public static final int COINS = 130;
                }

                public static final int OUTSIDE_DISTANCE_DESTINATION = 600;
                public static final float WAIT_ABOVE_PLAYER_DURATION = 5;
                public static final int GIB_REACH = -10;
                public static final int ATTACK_REGION = 100;
                public static final float ATTACK_RELOAD = 3;
                public static final int SPEED = 1;
                public static final float ATTACK_IMAGE_DURATION = 1;
                public static final float BOMB_GRAVITY_ACCELERATION = 0.025f;
                public static final float BOMB_MAX_FALLING_SPEED = 4;
                public static final float RANDOM_EXPLOSION_MAX_TIME = 1;
            }

            public final class Yak {

                public final class Easy {
                    public static final float ATTACK_DAMAGE = 1.3f;
                    public static final int HP = 3;
                    public static final float SUPER_ATTACK_DAMAGE = 4f;
                    public static final int SUPER_COINS = 28;
                    public static final int COINS = 24;
                }

                public final class Medium {
                    public static final int HP = 4;
                    public static final float ATTACK_DAMAGE = 1.5f;
                    public static final float SUPER_ATTACK_DAMAGE = 6f;
                    public static final int SUPER_COINS = 30;
                    public static final int COINS = 28;
                }

                public final class Hard {
                    public static final int HP = 5;
                    public static final float ATTACK_DAMAGE = 1.7f;
                    public static final float SUPER_ATTACK_DAMAGE = 8;
                    public static final int SUPER_COINS = 34;
                    public static final int COINS = 32;
                }

                public final class MedicYak {
                    public static final float HP = 0.05f;
                    public static final float FIRST_DELAY_APPEARANCE = 2;
                    public static final float INTERVAL_APPEARANCE = 20;
                }

                public static final float DELAY_ATTACK = 1f;
                public static final int SPEED = 5;
                public static final int GIB_REACH = -2;
                public static final float FLY_TO_PLAYER_REACH = 350;
                public static final float FLY_TO_SKY_REACH = 100;
                public static final int MASK_WIDTH = 20;
                public static final int MASK_HEIGHT = 20;
                public static final float RELOAD = 0.15f;
                public static final float MISSILE_SPEED = 8;
                public static final float ROTATION_CHANGE = 2;
            }

            public final class Apache {
                public final class Easy {
                    public static final float MISSILE_DAMAGE = 5;
                    public static final float ATTACK_DAMAGE = 1.1f;
                    public static final int HP = 6;
                    public static final int COINS = 60;
                }

                public final class Medium {
                    public static final int HP = 8;
                    public static final float ATTACK_DAMAGE = 1.3f;
                    public static final float MISSILE_DAMAGE = 7;
                    public static final int COINS = 70;
                }

                public final class Hard {
                    public static final float MISSILE_DAMAGE = 9;
                    public static final float ATTACK_DAMAGE = 1.5f;
                    public static final int HP = 9;
                    public static final int COINS = 80;
                }

                public static final int SPEED = 4;
                public static final int GIB_REACH = -3;
                public static final float DISTANCE_FROM_DESTINATION_REACH = 450;
                public static final float SHORT_DISTANCE_FROM_DESTINATION_REACH = 20;
                public static final int MASK_WIDTH = 20;
                public static final int MASK_HEIGHT = 20;
                public static final float DELAY_ATTACK = 2;
                public static final float RELOAD = 0.15f;
                public static final float RELOAD_MISSILE = 3f;
                public static final float MISSILE_LAUNCH_IMAGE_DURATION = 0.3f;
                public static final float STRAFE_INTERVAL = 2f;
                public static final float ATTACK_DURATION = 7;
                public static final float SPEED_DELTA = 0.05f;
                public static final float ROTATION_DELTA = 0.2f;
                public static final float RIGHT_ROTATION_MAX = 345;
                public static final float LEFT_ROTATION_MAX = 15;
                public static final float MINIMUM_DISTANCE_FROM_GROUND = Level.GROUND_Y + 200;
            }

            public final class Ballistic {
                public final class Easy {
                    public static final float ATTACK_DAMAGE = 22;
                    public static final int HP = 2;
                    public static final int COINS = 60;
                }

                public final class Medium {
                    public static final float ATTACK_DAMAGE = 32;
                    public static final int HP = 4;
                    public static final int COINS = 70;
                }

                public final class Hard {
                    public static final float ATTACK_DAMAGE = 42;
                    public static final int HP = 6;
                    public static final int COINS = 80;
                }

                public static final int SPEED = 3;
                public static final float DISTANCE_FROM_DESTINATION_REACH = 250;
                public static final float GRAVITY_ACCELERATION = 0.05f;
                public static final float DECELERATION = 0.012f;
                public static final float ROTATION_CHANGE = 0.5f;
                public static final float MAXIMUM_DELTA_HEIGHT = 200;
            }

            public final class BonusScout {
                public final class Easy {
                    public static final float SPEED = 3;
                    public static final int HP = 4;
                    public static final int COINS = 80;
                }

                public final class Medium {
                    public static final float SPEED = 4;
                    public static final int HP = 5;
                    public static final int COINS = 100;
                }

                public final class Hard {
                    public static final int HP = 6;
                    public static final float SPEED = 5;
                    public static final int COINS = 120;
                }

                public static final int GIB_REACH = 0;
            }

            public final class Osprey {
                public final class Easy {
                    public static final int SECONDS_TO_CREATE_PARATROOPER = 3;
                    public static final int SPEED = 1;
                    public static final int HP = 2;
                    public static final int COINS = 20;
                }

                public final class Medium {
                    public static final int SECONDS_TO_CREATE_PARATROOPER = 2;
                    public static final int SPEED = 2;
                    public static final int HP = 3;
                    public static final int COINS = 24;
                }

                public final class Hard {
                    public static final int SECONDS_TO_CREATE_PARATROOPER = 1;
                    public static final int HP = 4;
                    public static final int SPEED = 3;
                    public static final int COINS = 28;
                }

                public static final int GIB_REACH = -2;
            }
        }

        public final class GroundUnits {

            public final class GroundCrafts {

                public static final float LEFT_SIDE_DESTINATION_X_FAREST = 100;
                public static final float LEFT_SIDE_DESTINATION_X_CLOSEST = 200;
                public static final float RIGHT_SIDE_DESTINATION_X_CLOSEST = LEFT_SIDE_DESTINATION_X_CLOSEST;
                public static final float RIGHT_SIDE_DESTINATION_X_FAREST = System.Resolution.WIDTH_TARGET_RESOLUTION - LEFT_SIDE_DESTINATION_X_FAREST;
                public static final float SPEED_DELTA = 0.025f;

                public final class APC {
                    public final class Easy {
                        public static final int SPEED = 1;
                        public static final int HP = 8;
                        public static final int COINS = 36;
                    }

                    public final class Medium {
                        public static final int SPEED = 2;
                        public static final int HP = 10;
                        public static final int COINS = 40;
                    }

                    public final class Hard {
                        public static final int SPEED = 3;
                        public static final int HP = 12;
                        public static final int COINS = 42;
                    }

                    public static final float TIME_RELEASE = 2;
                    public static final int NUMBER_OF_PARATROOPERS_TO_RELEASE = 3;
                }

                public final class Tank {
                    public final class Easy {
                        public static final int SPEED = 1;
                        public static final int HP = 13;
                        public static final float ATTACK_DAMAGE = 12;
                        public static final int COINS = 100;
                    }

                    public final class Medium {
                        public static final int SPEED = 1;
                        public static final int HP = 18;
                        public static final float ATTACK_DAMAGE = 17;
                        public static final int COINS = 120;
                    }

                    public final class Hard {
                        public static final int SPEED = 2;
                        public static final int HP = 23;
                        public static final float ATTACK_DAMAGE = 22;
                        public static final int COINS = 140;
                    }

                    public static final float RELOAD_TIME = 3;
                    public static final float SMOKE_X_OFFSET = 50;
                    public static final float SHOOT_TIME = 0.12f;
                    public static final int MASK_WIDTH = 40;
                }
            }

            public final class Paratroopers {
                public final class GeneralAttributes {
                    public static final int MAX_NUMBER_OF_BODY_PARTS = 4;
                    public static final float BODY_PART_SPEED = 3;
                    public static final float ELECTROCUTION_FRAME_DURATION = 0.05f;
                    public static final float GIB_REACH = -5;
                    public static final int MASK_WIDTH = 10;
                    public static final float FLY_UP_LEFT = 135;
                    public static final float FLY_UP_RIGHT = 45;

                    public final class BodyFadeOutTimes {
                        public static final float ELECTROCUTED_BODY_FADE_OUT = 1;
                        public static final int TIME_TO_BODY_FADE_OUT = 2;
                    }

                    public final class DamageEffect {
                        public static final int REGULAR = 0;
                        public static final int ELECTRICITY = 1;
                        public static final int FLAME = 2;
                    }

                    public final class Speed {
                        public static final float FLY_AWAY_SPEED = 4;
                        public static final int SINK_SPEED = 1;
                        public static final float MAX_FALLING_SPEED = 15;
                        public static final float START_FALLING_SPEED = 0.8f;
                        public static final float MAXIMUM_SAFE_FALL_SPEED = 4;
                    }

                    public final class Direction {
                        public static final int LOWEST_SINK_DIRECTION_DEGREES = 225;
                        public static final int HIGHEST_SINK_DIRECTION_DEGREES_RELATIVE = 90;
                        public static final int LOWEST_SINK_SPRITE_DIRECTION_DEGREES = 355;
                        public static final int HIGHEST_SINK_SPRITE_DIRECTION_DEGREES_RELATIVE = 5;
                    }

                    public static final float TIME_TO_OPEN_PARACHUTE = 0.5f;
                    public static final float GRAVITY_ACCELERATION = 0.05f;
                    public static final int MAXIMUM_PARATROOPERS_ALLOWED = 10;
                    public static final float BULLET_IMPACT_RELATIVE_Y = -20;
                }

                public final class Parachute {
                    public static final float OPEN_ANIMATION_SPEED = 0.1f;
                    public static final float SWING_ANIMATION_SPEED = 0.2f;
                    public static final float LAND_STARTING_GRAVITY_SPEED = 0.5f;
                    public static final float TORN_STARTING_GRAVITY_SPEED = 1;
                }

                public final class UnitTypes {
                    public final class Infantry {
                        public final class Easy {
                            public static final float HP = 0.4f;
                            public static final float ATTACK_DAMAGE = 2.5f;
                            public static final int COINS = 4;
                        }

                        public final class Medium {
                            public static final float HP = 1;
                            public static final float ATTACK_DAMAGE = 3;
                            public static final int COINS = 8;
                        }

                        public final class Hard {
                            public static final float HP = 1.5f;
                            public static final float ATTACK_DAMAGE = 3.5f;
                            public static final int COINS = 12;
                        }

                        public static final int RELOAD_TIME = 2;
                    }

                    public final class Chaingunner {
                        public final class Easy {
                            public static final float HP = 0.4f;
                            public static final float ATTACK_DAMAGE = 1.1f;
                            public static final int COINS = 8;
                        }

                        public final class Medium {
                            public static final float HP = 1;
                            public static final float ATTACK_DAMAGE = 1.2f;
                            public static final int COINS = 12;
                        }

                        public final class Hard {
                            public static final float ATTACK_DAMAGE = 1.3f;
                            public static final float HP = 1.5f;
                            public static final int COINS = 16;
                        }

                        public static final float RELOAD_TIME = 0.5f;
                    }

                    public final class Bazooka {
                        public final class Easy {
                            public static final float HP = 0.4f;
                            public static final float ATTACK_DAMAGE = 4;
                            public static final int COINS = 12;
                        }

                        public final class Medium {
                            public static final float HP = 1;
                            public static final float ATTACK_DAMAGE = 6;
                            public static final int COINS = 16;
                        }

                        public final class Hard {
                            public static final float ATTACK_DAMAGE = 8;
                            public static final float HP = 1.5f;
                            public static final int COINS = 20;
                        }

                        public static final float SHOOT_TIME = 0.5f;
                        public static final float RELOAD_TIME = 3;
                        public static final float MISSILE_Y_ORIGIN = -15;
                    }
                }
            }
        }
    }

    public static final class Player {
        public static final int GIB_REACH = -5;
        public static final int BEGIN_COINS = 0;

        public interface UpgradeableAttribute {
            String getAttributeName();

            String getIcon();

            String getDescription();

            boolean isCostConstant();

            int getMaxValue();

            enum WeaponAttribute implements UpgradeableAttribute {
                STRENGTH(Stats.STRENGTH, ICON_FIST, AttributesDescriptions.STRENGTH),
                ENABLED(Stats.ENABLED),
                RATE(Stats.RATE, ICON_TIMER, AttributesDescriptions.RATE),
                AMMO(Stats.AMMO, ICON_BULLETS, AttributesDescriptions.AMMO, true, Cannons.MAX_AMMO);

                private final String attributeName;
                private final String icon;
                private final String description;
                private final boolean isCostConstant;
                private final int maxValue;

                WeaponAttribute(String name) {
                    this(name, null, null, false, Rules.Cannons.MAXIMUM_LEVEL);
                }

                WeaponAttribute(String name, String icon, String description) {
                    this(name, icon, description, false, Rules.Cannons.MAXIMUM_LEVEL);
                }

                WeaponAttribute(String name, String icon, String description, boolean isCostConstant, int maxValue) {
                    this.attributeName = name;
                    this.icon = icon;
                    this.description = description;
                    this.isCostConstant = isCostConstant;
                    this.maxValue = maxValue;
                }

                @Override
                public boolean isCostConstant() {
                    return isCostConstant;
                }

                @Override
                public int getMaxValue() {
                    return maxValue;
                }

                @Override
                public String getIcon() {
                    return icon;
                }

                @Override
                public String getDescription() {
                    return description;
                }

                @Override
                public String getAttributeName() {
                    return attributeName;
                }
            }

            enum PlayerCharacterAttribute implements UpgradeableAttribute {
                ARMOR(Stats.ARMOR, ICON_ARMOR, AttributesDescriptions.ARMOR, Bunker.ATTRIBUTE_MAXIMUM_LEVEL),
                SIDE_KICK_ARMOR(Stats.ARMOR, ICON_ARMOR, AttributesDescriptions.ARMOR, SideKicks.ATTRIBUTE_MAXIMUM_LEVEL),
                GENERATOR(Stats.GENERATOR, ICON_GEAR, AttributesDescriptions.GENERATOR, Bunker.ATTRIBUTE_MAXIMUM_LEVEL);

                private final String attributeName;
                private final boolean isCostConstant;
                private final String icon;
                private final int maxValue;
                private String description;

                PlayerCharacterAttribute(String name, String iconArmor, String description, int maxValue) {
                    this(name, iconArmor, description, maxValue, false);
                }

                PlayerCharacterAttribute(String name, String icon, String description, int maxValue, boolean isCostConstant) {
                    this.maxValue = maxValue;
                    this.attributeName = name;
                    this.icon = icon;
                    this.description = description;
                    this.isCostConstant = isCostConstant;
                }


                @Override
                public boolean isCostConstant() {
                    return isCostConstant;
                }

                @Override
                public String getDescription() {
                    return description;
                }

                @Override
                public int getMaxValue() {
                    return maxValue;
                }

                @Override
                public String getIcon() {
                    return icon;
                }

                @Override
                public String getAttributeName() {
                    return attributeName;
                }
            }
        }

        public static final class Bunker {
            public static final float ARMOR = 100;
            public static final float MAX_ARMOR = 300;
            public static final int ATTRIBUTE_MAXIMUM_LEVEL = 80;
            public static final float ARMOR_UPGRADE_UNIT = (MAX_ARMOR - ARMOR) / ATTRIBUTE_MAXIMUM_LEVEL;
            public static final int SHOOTING_TIME = 1;
            public static final int SHOOTING_RATE = 2;
            public static final float ATTACK_STRENGTH = 1;
            public static final int MASK_WIDTH = 20;
            public static final int UPGRADE_BASIC_COST = 37;

            public static final class Turret {
                public static final int ANIMATION_TIME = 100;
                public static final float HEAT_RESISTANCE = 0;
                public static final float HEAT_RESISTANCE_MAX = 2;
                public static final float HEAT_RESISTANCE_UPGRADE_UNIT = (HEAT_RESISTANCE_MAX - HEAT_RESISTANCE) / Cannons.MAXIMUM_LEVEL;
            }
        }

        public static final class SideKicks {
            public static final int DISTANCE_FROM_BUNKER = 100;
            public static final int LEFT_POSITION_X = (System.Resolution.WIDTH_TARGET_RESOLUTION / 2) - DISTANCE_FROM_BUNKER;
            public static final int RIGHT_POSITION_X = (System.Resolution.WIDTH_TARGET_RESOLUTION / 2) + DISTANCE_FROM_BUNKER;
            public static final int POSITION_Y = Level.GROUND_Y;
            public static final int ATTRIBUTE_MAXIMUM_LEVEL = 40;

            public static final class InfantryTower {
                public static final float SHOOT_FRAME_DURATION = 0.08f;
                public static final int COST = 12000;
                public static final int UPGRADE_BASIC_COST = 25;

                public static final class Armor {
                    public static final float ARMOR = 40;
                    public static final float ARMOR_MAX = 70;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class ShootingRate {
                    public static final float SHOOTING_RATE = 1;
                    public static final float SHOOTING_RATE_MAX = 0.2f;
                    public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MAX) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 0.5f;
                    public static final float STRENGTH_MAX = 2;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }
            }

            public static final class Tesla {
                public static final float SHOOT_FRAME_DURATION = 0.07f;
                public static final float ELECTRICITY_LIFE_DURATION = 0.1f;
                public static final float ATTACK_DELAY = 0.7f;
                public static final int COST = 20000;
                public static final int UPGRADE_BASIC_COST = 50;

                public static final class Armor {
                    public static final float ARMOR = 50;
                    public static final float ARMOR_MAX = 80;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class ShootingRate {
                    public static final float SHOOTING_RATE = 3.8f;
                    public static final float SHOOTING_RATE_MAX = 0.5f;
                    public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MAX) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 1.7f;
                    public static final float STRENGTH_MAX = 4.5f;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }
            }

            public static final class Flamer {

                public static final float BULLET_SPEED = 7;
                public static final float SHOOTING_IMAGE_SHOWN_DURATION = 0.5f;
                public static final int COST = 28000;
                public static final int UPGRADE_BASIC_COST = 50;

                public static final class Armor {
                    public static final int ARMOR = 45;
                    public static final int ARMOR_MAX = 75;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class ShootingRate {
                    public static final float SHOOTING_RATE = 4;
                    public static final float SHOOTING_RATE_MAX = 1;
                    public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MAX) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 0.5f;
                    public static final float STRENGTH_MAX = 2;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }
            }

            public static final class HeatTurret {
                public static final float SHOOT_FRAME_DURATION = 0.2f;
                public static final int MAX_TARGETS = 3;
                public static final int COST = 37000;
                public static final int UPGRADE_BASIC_COST = 100;

                public static final class Armor {
                    public static final int ARMOR = 47;
                    public static final int ARMOR_MAX = 77;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 0.006f;
                    public static final float STRENGTH_MAX = 0.016f;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }
            }

            public static final class Minigunner {
                public static final float SHOOT_FRAME_DURATION = 0.1f;
                public static final float BULLET_SPEED = 16;
                public static final int OVER_HEAT = 75;
                public static final float OVER_HEAT_TIME = 5;
                public static final int HEAT_POINTS_INCREASE = 3;
                public static final int COST = 47000;
                public static final int UPGRADE_BASIC_COST = 125;

                public static final class ShootingRate {
                    public static final float SHOOTING_RATE = 0.15f;
                    public static final float SHOOTING_RATE_MAX = 0.06f;
                    public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MAX) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class Armor {
                    public static final int ARMOR = 45;
                    public static final int ARMOR_MAX = 75;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 0.08f;
                    public static final float STRENGTH_MAX = 0.18f;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }

            }

            public static final class Dome {
                public static final float LOAD_BULLET = 2;
                public static final int COST = 58000;
                public static final int UPGRADE_BASIC_COST = 150;

                public static final class Armor {
                    public static final int ARMOR = 50;
                    public static final int ARMOR_MAX = 80;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 1.2f;
                    public static final float STRENGTH_MAX = 3.2f;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class ShootingRate {
                    public static final float SHOOTING_RATE = 5;
                    public static final float SHOOTING_RATE_MIN = 2;
                    public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
                }

                public static final float SHOOTING_IMAGE_SHOWN_DURATION = 1;
                public static final float BULLET_SPEED = 8;
                public static final float HOMING_ROTATION = 2;
            }

            public static final class Phantom {
                public static final float SHOOTING_IMAGE_SHOWN_DURATION = 0.05f;
                public static final int COST = 70000;
                public static final int UPGRADE_BASIC_COST = 175;

                public static final class Armor {
                    public static final int ARMOR = 45;
                    public static final int ARMOR_MAX = 75;
                    public static final float ARMOR_UPGRADE = (ARMOR_MAX - ARMOR) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class ShootingRate {
                    public static final float SHOOTING_RATE = 6;
                    public static final float SHOOTING_RATE_MIN = 2;
                    public static final float SHOOTING_RATE_UPGRADE = (SHOOTING_RATE - SHOOTING_RATE_MIN) / Cannons.MAXIMUM_LEVEL;
                }

                public static final class AttackStrength {
                    public static final float STRENGTH = 4;
                    public static final float STRENGTH_MAX = 7;
                    public static final float STRENGTH_UPGRADE = (STRENGTH_MAX - STRENGTH) / Cannons.MAXIMUM_LEVEL;
                }

            }
        }

        public static final class Bonus {
            public static final int FALLING_SPEED = 3;
            public static final float PROTECTION_TIME = 0.5f;
            public static final float SMALL_WIDTH = 50;
            public static final float SMALL_HEIGHT = 50;
            public static final float IMAGE_RESIZE_PACE = 4;
            public static final float MOVE_AWAY_SPEED = 5;
            public static final float BIOHAZARD_DURATION = 60;
            public static final int MAX_BOMBS_TO_HAVE = 4;

            public static final class Shield {
                public static final float DURATION = 45;
                public static final float SECONDS_BEFORE_DEACTIVATE = 5;
                public static final float WARNING_INTERVAL = 0.5f;
                public static final int WARNING_REPEAT = 10;
            }

            public static final class Money {
                public static final int MONEY_VALUE = 500;
                public static final int SUPER_MONEY_VALUE = MONEY_VALUE * 3;
            }

            public static final class Fix {
                public static final int FIX_VALUE_PERCENT = 20;
                public static final int SUPER_FIX_VALUE_PERCENT = 100;
            }
        }

        public enum ArmoryItem implements WeaponType {

            BUNKER(ArmoryNames.BUNKER, ArmoryDescriptions.YOUR_BUNKER, THUMB_BUNKER, Bunker.UPGRADE_BASIC_COST) {
                @Override
                public UpgradeableAttribute[] getAttributes() {
                    return BUNKER_ATTRIBUTES;
                }

            },
            BIO_HAZARD(ArmoryNames.BIO, ArmoryDescriptions.BIO_HAZARD, THUMB_BIO, 2000, false, Assets.Strings.InGameMessages.Bonus.MAX_BOMBS_BIOHAZARD),
            AIR_STRIKE(ArmoryNames.AIR, ArmoryDescriptions.AIR_STRIKE, THUMB_AIR, 3000, false, Assets.Strings.InGameMessages.Bonus.MAX_BOMBS_AIRSTRIKE),
            ATOM(ArmoryNames.ATOM, ArmoryDescriptions.ATOM, THUMB_ATOM, 6000, false, Assets.Strings.InGameMessages.Bonus.MAX_BOMBS_ATOM);

            private static UpgradeableAttribute[] BUNKER_ATTRIBUTES = {UpgradeableAttribute.PlayerCharacterAttribute.ARMOR, UpgradeableAttribute.PlayerCharacterAttribute.GENERATOR};
            private final boolean upgradeable;
            private String name, description, thumb;
            private int cost;
            private String specialMessage;

            ArmoryItem(String name, String description, String thumb, int cost) {
                this(name, description, thumb, cost, true, null);
            }

            ArmoryItem(String name, String description, String thumb, int cost, boolean upgradeable, String specialMessage) {
                this.cost = cost;
                this.name = name;
                this.thumb = thumb;
                this.description = description;
                this.upgradeable = upgradeable;
                this.specialMessage = specialMessage;
            }

            @Override
            public boolean isUpgradeable() {
                return upgradeable;
            }

            @Override
            public String getEnumName() {
                return name();
            }

            @Override
            public int getUpgradeBasicCost() {
                return cost;
            }

            @Override
            public UpgradeableAttribute[] getAttributes() {
                return null;
            }

            @Override
            public String getDescription() {
                return description;
            }

            @Override
            public String getIcon() {
                return thumb;
            }

            @Override
            public String getThumb() {
                return thumb;
            }

            @Override
            public int getCost() {
                return cost;
            }

            public String getName() {
                return name;
            }

            public String getSpecialMessage() {
                return specialMessage;
            }

        }


    }

    public static final class Hud {
        public static final int SWIPE_ZONE_Y = Level.GROUND_Y - 25;

        public static final class CoinsEffect {
            public static final float AMOUNT_Y = 40;
            public static final float GREEN_DURATION = 1;
            public static final float GOLD_DURATION = 5;
            public static final float FADE_IN_OUT_DURATION = 1;
            public static final int GOLD_VALUE = 25;
            public static final int COLORFUL_VALUE = 50;
            public static final float COLOR_CHANGE_DURATION = 0.25f;
            public static final float GREEN_SCALE = 0.5f;
            public static final float GOLD_SCALE = 0.75f;
            public static final float COLORFUL_SCALE = 1;
        }


        public static final class Monitor {
            public static final float MESSAGE_TIME = 3;
            public static final double MESSAGE_FADE_AWAY_PACE = 0.01;
            public static final double MESSAGE_FADE_AWAY_PACE_FAST = 0.04;
            public static final long TAUNT_INTERVAL = 3000;
        }

        public static final class Alarm {
            public static final float DAMAGE_CRITICAL_LIMIT = 35;
            public static final float ALARM_ALPHA_DELTA = 0.1f;
        }

        public static final class Buttons {
            private static final float INTERVAL = 150;
            private static final float BUTTONS_Y = 5;
            public static final float BOMBS_X = System.Resolution.WIDTH_TARGET_RESOLUTION - 150;
            public static final float BOMBS_Y = BUTTONS_Y;
            public static final float OPTIONS_X = 25;
            public static final float OPTIONS_Y = BUTTONS_Y;
            public static final float BIOHAZARD_X = BOMBS_X - INTERVAL;
            public static final float BIOHAZARD_Y = BUTTONS_Y;
            public static final float AIR_STRIKE_X = BIOHAZARD_X - INTERVAL;
            public static final float AIR_STRIKE_Y = BUTTONS_Y;
            public static final float ATOM_X = AIR_STRIKE_X - INTERVAL;
            public static final float ATOM_Y = BUTTONS_Y;
            public static final float VERTICAL_SPEED = 3;
            public static final float RISE_ALPHA_CHANGE = 0.015f;
            public static final float HIGHLIGHT_FREQ = 0.5f;
            public static final int HIGHLIGHT_DURATION = 20;
        }

        public static final class Gauge {
            public static final float X = 20;
            public static final float Y = System.Resolution.HEIGHT_TARGET_RESOLUTION - 115;
            public static final float SHAKE_INTERVAL = 1;
            public static final float MAX_Y = System.Resolution.HEIGHT_TARGET_RESOLUTION + 300;

            public static final class Needle {
                public static final float NEEDLE_ORIGIN_X = 15;
                public static final float NEEDLE_Y_OFFSET = 2;
                public static final float NEEDLE_ROTATION_DELTA = 2;
            }
        }

        public static final class Stats {
            public static final class Health {
                public static final float X = 160;
                public static final float Y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 41;
                public static final float MAX_WIDTH = 136;
                public static final float HEIGHT = 17;
            }

            public final static class Coins {
                public static final float X = 161;
                public static final float Y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 56;
            }

            public final static class Score {
                public static final float X = Coins.X;
                public static final float Y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION - 87;
            }
        }

        public final static class StatisticsWindow {
            public static final float WIDTH = 550;
            public static final float HEIGHT = 500;
            public static final float HEADER_Y_MARGIN = 50;
            public static final float COINS_ICON_X_MARGIN = 200;
            public static final float COINS_ICON_Y_MARGIN = 170;
            public static final float COINS_VALUE_X_MARGIN = 100;
            public static final float COINS_VALUE_Y_MARGIN = 130;
            public static final float SCORE_ICON_Y_MARGIN = 240;
            public static final float SCORE_VALUE_Y_MARGIN = 200;
            public static final float CROSSHAIR_ICON_Y_MARGIN = 310;
            public static final float CROSSHAIR_VALUE_Y_MARGIN = 270;
            public static final float VERIFY_BUTTON_Y_MARGIN = 10;
            public static final float VERIFY_BUTTON_X_MARGIN = 150;
            public static final float SHARE_BUTTON_X_MARGIN = 275;
        }

        public final static class PauseWindow {
            public static final float MAX_SLIDE_SPEED = 11;
            public static final float SPEED_ACCELERATION = 0.4f;
            public static final float WIDTH = 600;
            public static final float HEIGHT = 400;

            public final static class ExitPopup {
                public static final float WIDTH = 600;
                public static final float HEIGHT = 300;
                public static final float TEXT_Y_BOTTOM_MARGIN = 100;
                public static final float PADDING = 30;
            }

            public final static class HelpPopup {
                public static final float WIDTH = 600;
                public static final float HEIGHT = 500;
                public static final float PADDING = 30;
                public static final float REGION_MARGIN_TOP = 50;
                public static final float VERIFY_MARGIN_BOTTOM = 20;
            }
        }
    }

    public final static class Menu {
        public static final float RED_LIGHT_X_OFFSET = 25;
        public static final float RED_LIGHT_Y_OFFSET = 60;
        public static final float INTRO_DURATION = 3;
        public static final String DEFAULT_MONITOR_NAME = "main_monitor";

        public final static class Mentors {
            public static final float PADDING_LEFT = 5;
            public static final float PADDING_RIGHT = 5;
            public static final float WAIT_TIME = 9;
            public static final float SWIPE_DURATION = 1;
            public static final float INITIAL_DELAY = 3;

            public final static class Names {

                public static final String SCROLLABLE = "scrollable";
                public static final String UPGRADE_GENERATOR = "upgrade_generator";
                public static final String UPGRADE_ARMOR = "upgrade_armor";
                public static final String CHANGE_CANNON = "change_cannon";
                public static final String CHANGE_SIDE_KICK = "change_side_kick";
                public static final String GO_TO_BATTLE = "go_to_battle";
                public static final String AMMUNITION_DEPLETED = "ammunition_depleted";
            }

        }

        public final static class GetMoreCoinsMonitor {
            public static final float COINS_BUTTON_FADE_DURATION = 2;

            public static final float WIDTH = 650;
            public static final float HEIGHT = 600;
            public static final float Y = System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - 300;
            public static final String NAME = "get_more_coins";
            public static final float COINS_PRICE_PADDING_TOP = 2;
            public static final float ADS_TEXT_PADDING_BOTTOM = 15;
            public static final float TAX_TEXT_PADDING_TOP = 10;
            public static final float BUY_PRODUCT_PADDING_BOTTOM = 10;
            public static final float DELAY_AFTER_PURCHASE = 2;
        }

        public final static class ShopScreenImproved {
            public final static class InfoMonitor {
                public static final float WIDTH = 600;
                public static final float HEIGHT = 300;
                public static final float TARGET_Y = System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - HEIGHT / 2;
                public static final String NAME = "monitor_info";
            }

            public final static class Monitor {

                public static final float HEIGHT = 600;
                public static final float WIDTH = 1100;
                public static final float Y = 100;
                public static final float CATEGORY_BUTTONS_PADDING_VERTICAL = 10;
                public static final float ITEM_PADDING_BOTTOM = 20;
                public static final float ITEM_FADE_DURATION = 0.5f;
                public static final float MAIN_CONTENT_TEXT_WIDTH = 750;
                public static final float UPGRADERS_TABLE_PADDING_TOP = 20;
                public static final float UPGRADER_PADDING_BOTTOM = 50;
                public static final float UPGRADER_LABEL_PADDING_HORIZONTAL = 10;
                public static final float UPGRADE_BUTTON_LABEL_RELATIVE_X = 57;
                public static final float UPGRADE_BUTTON_LABEL_RELATIVE_Y = 30;
                public static final float UPGRADE_BUTTON_CELL_RELATIVE_X = 67;
                public static final float UPGRADE_BUTTON_FULL_CELL_RELATIVE_Y = 5;
                public static final float UPGRADE_BUTTON_EMPTY_CELL_RELATIVE_Y = 6;
                public static final float INFO_BUTTON_PADDING_RIGHT = 20;
                public static final float CANNONS_CATEGORY_BUTTON_PADDING_LEFT = 5;
                public static final float UPGRADER_CONFIRM_PADDING_TOP = 20;
                public static final float UPGRADER_CONFIRM_DURATION = 3;
                public static final float ILLUSTRATION_PADDING_BOTTOM = 10;
                public static final float YOU_HAVE_PADDING_LEFT = 10;
                public static final float UPGRADE_STACK_WIDTH = 300;
                public static final float ERROR_DURATION = 7;
                public static final float ERROR_MESSAGE_PADDING_TOP = 10;
                public static final float PURCHASE_COST_PADDING_BOTTOM = 25;
                public static final float PURCHASE_ADDITIONAL_LABEL_PADDING_TOP = 20;
                public static final float MIDDLE_STACK_SIZE_UPGRADERS = 400;
                public static final float MIDDLE_STACK_SIZE_PURCHASE = 300;
                public static final float ITEMS_STACK_WIDTH = 315;
                public static final float ERROR_LOCK_DURATION = 1;
            }

        }

        public final static class WelcomeScreen {
            public final static class VisitFacebookMonitor {
                public static final float VISIT_FACEBOOK_MONITOR_DELAY = 2;
                public static final float MONITOR_WIDTH = 800;
                public static final float MONITOR_HEIGHT = 400;
                public static final String MONITOR_NAME = "visit_monitor";
                public static final float TARGET_Y = 200;
            }

            public final static class LogoEffect {
                public static final float MOVE_DURATION = 2;
            }

            public final static class Version {
                public static final float X = 300;
                public static final float Y = 30;
                public static final float DELAY = 2;
            }

            public final static class RateMonitor {
                public static final float RATE_MONITOR_WIDTH = 700;
                public static final float RATE_MONITOR_HEIGHT = 400;
                public static final float STARS_PADDING_BOTTOM = 10;
                public static final float STARS_INTERVAL = 10;
                public static final String MONITOR_NAME = "rate_monitor";
                public static final float TARGET_Y = 200;
            }

            public final static class BigButtons {
                public static final int MARGIN_HORIZONTAL = 15;
                public static final float MOVE_DURATION = 1;
            }

            public final static class MainMonitor {
                public static final float MONITOR_Y = 100;
                public static final float MONITOR_WIDTH = 700;
                public static final float MONITOR_HEIGHT = 300;
                public static final float PLAY_PADDING_BOTTOM = 5;
            }

        }

        public final static class Lobby {
            public final static class Gift {
                public static final int GIFT_VALUE = 1000;

                public final static class Monitor {
                    public static final String NAME = "gift";
                    public static final float WIDTH = 700;
                    public static final float HEIGHT = 600;
                    public static final float SUBMIT_PADDING_VERTICAL = 10;
                    public static final float Y = 100;
                    public static final float TEXT_FIELD_PADDING_VERTICAL = 20;
                    public static final float TEXT_FIELD_WIDTH = 450;
                }

                public static final float CREATION_X = System.Resolution.WIDTH_TARGET_RESOLUTION + 100;
                public static final float TARGET_X = 1060;
                public static final float Y = 350;
                public static final float MOVE_DURATION = 5;
                public static final float ROTATE_DELTA = 22;
                public static final float ROTATE_DURATION = 0.5f;
                public static final float ROTATE_INTERVAL = 2;
                public static final float ENTRY_DELAY = 2;
                public static final float FADE_DURATION = 1;
                public static final float CLOSE_BUTTON_PADDING_LEFT = 10;
                public static final long NEW_PASS_TIME_INTERVAL = 24 * 60 * 60 * 1000;
                public static final long TIME_CHECK_INTERVAL = 12 * 60 * 60 * 1000;
            }

            public final static class MainMonitor {
                public final static class AttackButton {
                    public static final float SIZE_EFFECT_DELTA = 25;
                    public static final float EXPAND_DURATION = 0.2f;
                    public static final float SHRINK_DURATION = 1f;
                    public static final float INTERVAL = 5f;
                }

                public static final float UPGRADE_BUTTON_X = 463;
                public static final float UPGRADE_BUTTON_Y = -2;
                public static final float CLOUD_OFFSET_X = 40;
                public static final float CLOUD_OFFSET_Y = 250;

                public final static class Ammo {
                    public static final float VALUE_LABEL_X = 865;
                    public static final float VALUE_LABEL_Y = 378;
                    public static final float HEADER_LABEL_Y = 400;
                    public static final float HEADER_LABEL_X = 832;
                }

                public static final float MONITOR_WIDTH = 1100;
                public static final float MONITOR_HEIGHT = 600;
                public static final float MONITOR_Y = 100;
                public static final float STACK_PADDING_LEFT = 125;
                public static final float COINS_BUTTON_X = 925;

                public final static class InfoBorders {
                    public static final float X = 75;
                    public static final float Y = 25;
                    public static final float BUNKER_X = 285;
                    public static final float BUNKER_Y = 275;
                    public static final float CANNONS_X = 585;
                    public static final float BOMBS_X = 75;
                    public static final float SIDEKICKS_X = 810;
                    public static final float BOMB_Y = 25;
                    public static final float HEADER_PADDING_BOTTOM = 10;
                    public static final float TABLE_PADDING_TOP = 10;
                    public static final float TABLE_PADDING_HORIZONTAL = 15;
                    public static final float ATTRIBUTE_PADDING_BOTTOM = 10;
                    public static final float ATTRIBUTE_LABEL_PADDING_LEFT = 10;
                    public static final float TAP_TO_CHANGE_PADDING_BOTTOM = 10;
                    public static final float WEAPON_PADDING_BOTTOM = 10;
                    public static final float CANNONS_FLICKER_INTERVAL = 0.5f;
                    public static final int CANNONS_FLICKER_REPEAT = 11;
                }
            }

            public static final float BACK_Y_OFFSET = 375;

            public final static class SelectionMonitors {
                public static final float WIDTH = 950;
                public static final float HEIGHT = 450;
                public static final float TARGET_Y = 200;
                public static final float WEAPON_NAME_PADDING_BOTTOM = 10;
                public static final float BUTTON_SPACING = 5;
            }

        }

        public final static class SceneSelection {

            public final static class Monitor {
                public final static class LockedSceneMonitor {
                    public static final float WIDTH = 800;
                    public static final float HEIGHT = 450;
                    public static final String NAME = "locked_scene";
                    public static final float Y = System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - 200;
                    public static final float REQUIREMENT_PADDING_TOP = 20;
                    public static final float STARS_ICON_PADDING_RIGHT = 10;
                    public static final float COINS_ICON_PADDING_RIGHT = 15;
                    public static final float YOU_HAVE_PADDING_BOTTOM = 10;
                }

                public final static class StarsDisplay {
                    public static final float STARS_VALUE_PADDING_RIGHT = 15;
                    public static final float STARS_VALUE_PADDING_LEFT = 10;

                    public static final float MONITOR_WIDTH = 600;
                    public static final float MONITOR_HEIGHT = 500;
                    public static final float MONITOR_Y = System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - 200;
                    public static final String MONITOR_NAME = "stars_info";
                }

                public static final float BACK_X_OFFSET = 12;
                public static final float MONITOR_WIDTH = 1100;
                public static final float MONITOR_HEIGHT = 550;
                public static final float SCENE_UNLOCK_EFFECT_DELAY = 1;
                public static final float STACK_PADDING_HORIZONTAL = 20;
                public static final float TABLE_PADDING_HORIZONTAL = 150;
                public static final float REQUIREMENTS_TABLE_PADDING_TOP = 10;
                public static final float REQUIREMENTS_IMAGE_PADDING_RIGHT = 10;
                public static final float REQUIREMENTS_STAR_VALUE_PADDING_RIGHT = 25;
                public static final float REQUIREMENTS_LABEL_PADDING_RIGHT = 20;
            }
        }

        public final static class Help {
            public final static class Monitor {
                public static final float ADDITIONAL_CREDITS_PADDING_TOP = 20;
                public static final float PROFILE_IMAGE_PADDING_RIGHT = 20;
                public static final float ABOUT_ME_HEADER_PADDING_TOP = 20;
                public static final float LIBGDX_IMAGE_PADDING_TOP = 30;
                public static final float LIBGDX_TEXT_PADDING_BOTTOM = 10;
                public static final float TUTORIAL_BUTTON_PADDING_RIGHT = 15;

                public final static class Bonus {
                    public static final float IMAGE_PADDING_RIGHT = 10;
                    public static final float DESCRIPTION_WIDTH = 400;
                    public static final float ROW_PADDING_BOTTOM = 40;
                    public static final float ROW_PADDING_LEFT = 10;
                }

                public static final float MONITOR_WIDTH = 1000;
                public static final float MONITOR_HEIGHT = 500;
                public static final float MONITOR_Y = 150;
                public static final int NUMBER_OF_LAYOUTS = 10;
                public static final float LEFT_SIDE_CREDITS_WIDTH = 400;
                public static final float ADDITIONAL_CREDITS_WIDTH = 600;
                public static final float GADARTS_PADDING_TOP = 25;
            }

        }

        public final static class Options {
            public final static class Monitor {
                public static final float MONITOR_WIDTH = 800;
                public static final float MONITOR_HEIGHT = 500;
                public static final float PADDING = 10;
                public static final float BUTTON_RIGHT_PADDING = 10;
                public static final float CONTROL_BUTTONS_SPACING = 100;
                public static final float CONFIRMATION_MONITOR_WIDTH = 400;
                public static final float CONFIRMATION_MONITOR_HEIGHT = 300;
                public static final float BUTTONS_SPACING = 30;
                public static final float HEADER_TOP_PADDING = 40;
                public static final String DELETE_CONFIRMATION_MONITOR_NAME = "conf_monitor";
                public static final String LOAD_CONFIRMATION_MONITOR_NAME = "load_monitor";
                public static final float LABEL_WIDTH = 600;
            }
        }

        public final static class LevelSelection {


            public final static class SceneCompletedMonitor {
                public static final float WIDTH = 600;
                public static final float HEIGHT = 400;
                public static final float Y = System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 - 200;
                public static final String NAME = "scene_completed";
                public static final int REWARD = 2000;
            }

            public final static class LevelsButtons {
                public static final float LEVEL_BUTTONS_PADDING = 20;
                public static final float STARS_PADDING_BOTTOM = 10;
            }
        }

        public final static class Shop {
            public static final float LOADING_DOORS_SPEED = 9;

            public final static class CloseButton {
                public static final int MARGIN_RIGHT = 125;
                public static final int MARGIN_TOP = 155;
            }


            public final static class MainScreen {
                public final static class Coins {
                    public final static class CoinsPacks {

                        public enum PurchasePack {
                            coins_1(15000), coins_2(30000),
                            coins_3(60000), coins_4(120000);
                            private final int value;

                            PurchasePack(int value) {
                                this.value = value;
                            }

                            public int getValue() {
                                return value;
                            }

                        }
                    }
                }

                public static final float X = 365;
                public static final float Y = 250;
            }

            public static final float MONITOR_ACCELERATION = 0.7f;
        }
    }
}
