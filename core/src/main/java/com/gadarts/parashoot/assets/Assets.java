package com.gadarts.parashoot.assets;

import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * The assets paths strings.
 */
public final class Assets {
    @SuppressWarnings("WeakerAccess")
    public final class InGameGuides {

        @SuppressWarnings("unused")
        public final class MenuGuides {
            public static final String FOLDER_NAME = "menu_guides";
            public static final String KEY_TUTORS = "tutors";
            public static final String DATA_EXTENSION = "json";
            public static final String LOBBY = "lobby";
        }

        public static final int MESSAGE_DURATION = 5;

        public final class Animation {
            public static final String START_X = "start_x";
            public static final String START_Y = "start_y";
            public static final String END_X = "end_x";
            public static final String END_Y = "end_y";
            public static final String MESSAGE = "message";
            public static final float MESSAGE_Y = Rules.System.Resolution.HEIGHT_TARGET_RESOLUTION / 2 + 200;
        }

        public final class Focus {
            public static final String TARGET = "target";
            public static final String MESSAGE = "message";
            public static final String SHOW_FINGER = "show_finger";
            public static final double PAUSE_LOCK_DURATION = 0.5;
        }

        public static final String ACTION_PARAMETERS = "action_parameters";
        public static final String TIMING = "timing";
        public static final String ACTION = "action";
    }

    public static final class GFX {
        static final String GFX_FOLDER_NAME = "gfx";

        public final class Images {
            static final String IMAGES_FOLDER = GFX_FOLDER_NAME + "/" + Images.FOLDER_NAME + "/";

            public final class General {
                static final String FOLDER_NAME = "general";
                public static final String NIGHT = IMAGES_FOLDER + FOLDER_NAME + "/" + "night.png";
            }

            public final class Menus {
                static final String FOLDER_NAME = "menus";
                static final String MENUS_FOLDER = IMAGES_FOLDER + FOLDER_NAME + "/";
                public static final String SCAN_LINES = MENUS_FOLDER + "generic_screen_background.png";
                public static final String LOGO = MENUS_FOLDER + "logo.png";

                public final class ScenesThumbs {
                    static final String FOLDER_NAME = "scenes_thumbs";
                    static final String SCENES_FOLDER = MENUS_FOLDER + FOLDER_NAME + "/";
                    public static final String MILITARY = SCENES_FOLDER + "military.png";
                    public static final String ARCTIC = SCENES_FOLDER + "arctic.png";
                    public static final String BEACH = SCENES_FOLDER + "beach.png";
                    public static final String MEADOW = SCENES_FOLDER + "meadow.png";
                    public static final String CITY = SCENES_FOLDER + "city.png";
                    public static final String MOUNTAINS = SCENES_FOLDER + "mountains.png";
                    public static final String FOREST = SCENES_FOLDER + "forest.png";
                    public static final String DESERT = SCENES_FOLDER + "desert_thumb.png";
                    public static final String BOSS = SCENES_FOLDER + "boss.png";
                }
            }

            public final class InGame {
                public final class GroundTypes {
                    public static final String MILITARY = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_military.png";
                    public static final String ARCTIC = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_arctic.png";
                    public static final String BEACH = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_beach.png";
                    public static final String MEADOW = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_meadow.png";
                    public static final String CITY = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_city.png";
                    public static final String MOUNTAINS = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_mountains.png";
                    public static final String FOREST = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_forest.png";
                    public static final String DESERT = IMAGES_FOLDER + FOLDER_NAME + "/" + "ground_dunes.png";
                }

                public final class Landscapes {
                    public static final String MILITARY = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_military.png";
                    public static final String ARCTIC = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_arctic.png";
                    public static final String BEACH_1 = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_beach_1.png";
                    public static final String BEACH_2 = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_beach_2.png";
                    public static final String MEADOW = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_meadow.png";
                    public static final String CITY_1 = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_city_1.png";
                    public static final String CITY_2 = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_city_2.png";
                    public static final String MOUNTAINS = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_mountains.png";
                    public static final String FOREST = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_forest.png";
                    public static final String DUNES = IMAGES_FOLDER + FOLDER_NAME + "/" + "landscape_dunes.png";
                }

                static final String FOLDER_NAME = "in_game";
                public static final String DAY = IMAGES_FOLDER + FOLDER_NAME + "/" + "day.png";
                public static final String DAY_CLOUDY = IMAGES_FOLDER + FOLDER_NAME + "/" + "day_cloudy.png";
                public static final String NOON = IMAGES_FOLDER + FOLDER_NAME + "/" + "noon.png";
            }

            static final String FOLDER_NAME = "images";
            static final String DATA_EXTENSION = "png";
        }

        public static final class Sheets {
            static final String SHEETS_FOLDER_NAME = "sheets";
            static final String SHEETS_DATA_EXTENSION = "txt";

            public static final class General {
                static final String FOLDER_NAME = "general";
                public static final String TOP_LOADING_SCREEN_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "top_loading_screen.txt";
                public static final String BOTTOM_LOADING_SCREEN_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "bottom_loading_screen.txt";

                public static final class Bonuses {
                    public static final String DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + General.FOLDER_NAME + "/" + "bonuses.txt";
                    public static final String DATA_FILE_2 = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + General.FOLDER_NAME + "/" + "bonuses_2.txt";
                }
            }

            public static final class InGame {

                static final String FOLDER_NAME = "in_game";
                public static final String HUD_MENU_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "hud_menu.txt";

                public static final class Player {

                    public static final String PLAYER_BULLETS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "player_bullets.txt";

                    public static final class Bunker {
                        public static final String DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "bunker.txt";

                        public static final class Turret {
                            public static final String DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "turrets.txt";
                        }
                    }

                    public static final String BONUS_APPENDIX_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "bonus_appendix.txt";

                    public static final class SideKicks {
                        public static final String INFANTRY_TOWER_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "infantry_tower.txt";
                        public static final String FLAMER_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "flamer.txt";
                        public static final String TESLA_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "tesla.txt";
                        public static final String HEAT_TURRET_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "heat_turret.txt";
                        public static final String JAGUAR_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "auto_turret.txt";
                        public static final String DOME_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "dome.txt";
                        public static final String BEAM_TURRET_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "beam_turret.txt";

                    }


                }

                public static final String BUTTONS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "ingame_buttons.txt";

                public static final String HUD_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "hud.txt";

                public final class Misc {
                    public final class Ornaments {
                        public static final String DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "ornaments_general.txt";

                    }

                    public static final String EXPLOSIONS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "explosions.txt";
                    public static final String FLYING_PARTS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "parts.txt";
                    public static final String PARTICLES_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "particles.txt";

                }

                public final class Enemies {
                    public final class AirCrafts {
                        public static final String ZEPPELIN_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "zeppelin.txt";
                        public static final String SCOUT_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "scout.txt";
                        public static final String BONUS_SCOUT_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "scout_bonus.txt";
                        public static final String OSPREY_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "osprey.txt";
                        public static final String YAK_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "yak.txt";
                        public static final String APACHE_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "apache.txt";
                        public static final String BOSS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "boss.txt";

                    }

                    public final class GroundUnits {
                        public final class GroundCrafts {
                            public static final String APC_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "apc.txt";
                            public static final String TANK_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "tank.txt";

                        }

                        public final class Paratroopers {
                            public static final String PARACHUTE_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "parachute.txt";
                            public static final String INFANTRY_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "infantry.txt";
                            public static final String CHAINGUNNER_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "chaingunner.txt";
                            public static final String BAZOOKA_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + InGame.FOLDER_NAME + "/" + "bazooka.txt";
                        }
                    }
                }
            }

            public final class ImagesNames {
                public static final String IDLE = "idle";
                public static final String DOWN = "down";
                public static final String DOWN_RIGHT = "down_right";
                public static final String UP = "up";
                public static final String UP_RIGHT = "up_right";
                public static final String RIGHT = "right";
                public static final String SHOOT_LEFT = "shoot_left";
                public static final String SHOOT_RIGHT = "shoot_right";
                public static final String SHOOT = "shoot";
                public static final String PARACHUTE_OPENS = "parachute_opens";
                public static final String PARACHUTE = "parachute";
                public static final String PARACHUTE_LANDS = "parachute_lands";
                public static final String PARACHUTE_DEAD = "parachute_dead";
                public static final String DEAD = "dead";
                public static final String FALLING = "falling";
                public static final String PART = "scout_part";
                public static final String BIG_PART = "big_scout_part";
                public static final String CANNON_BALL = "cannon_ball";
                public static final String TRIPLE_CANNON_BALL = "triple_cannon_ball";
                public static final String CHAIN_GUN_BULLET = "chain_gun_bullet";
                public static final String SINK = "sink";
                public static final String FACING_LEFT = "facing_left";
                public static final String FACING_RIGHT = "facing_right";
                public static final String DYING = "dying";
                public static final String HOMING_MISSILE = "homing_missile";
                public static final String SPLIT_BULLET = "split_bullet";
                public static final String SMALL_SPLIT_BULLET = "small_split_bullet";
                public static final String BIG_EXP = "bigexp";
                public static final String PURPLE_EXPLOSION = "purple_explosion";
                public static final String SMALL_EXPLOSION = "small_explosion";
                public static final String SMALL_PURPLE_EXPLOSION = "small_purple_explosion";
                public static final String HOMING_PLASMA = "homing_plasma";
                public static final String SMALL_BLUE_EXPLOSION = "small_blue_explosion";
                public static final String ROCKET = "rocket";
                public static final String RAPID_LASER = "rapid_laser";
                public static final String SMALL_GREEN_EXPLOSION = "small_green_exp";
                public static final String BULLET_IMPACT = "bullet_impact";
                public static final String HEAD = "head";
                public static final String ELECTRICITY = "electricity";
                public static final String FLAME = "flame_bullet";
                public static final String BULLET = "bullet";
                public static final String AUTO_TURRET_BULLET = "auto_turret_bullet";
                public static final String BODY_PART_1 = "body_part_1";
                public static final String BODY_PART_2 = "body_part_2";
                public static final String ELECTROCUTED = "electrocute";
                public static final String BALLISTIC = "ballistic";
                public static final String BOMB = "bomb";
                public static final String FIX = "fix";
                public static final String SUPER_FIX = "super_fix";
                public static final String DOLLAR = "dollar";
                public static final String DOLLAR_BAG = "dollar_bag";
                public static final String SHIELD = "shield";
                public static final String BUNKER_SHIELD = "bunker_shield";
                public static final String GAS = "gas";
                public static final String BUTTON_BOMBS = "button_bombs";
                public static final String BUTTON_CHECK = "button_check";
                public static final String BUTTON_CHECK_PRESSED = "button_check_pressed";
                public static final String BUTTON_CANCEL = "button_cancel";
                public static final String BUTTON_CANCEL_PRESSED = "button_cancel_pressed";
                public static final String BUTTON_EXIT = "button_exit";
                public static final String BUTTON_OPTIONS = "button_options";
                public static final String BUTTON_BOMBS_PRESSED = "button_bombs_pressed";
                public static final String BUTTON_HELP = "button_help";
                public static final String BUTTON_SPEAKER_ON = "button_speaker_on";
                public static final String BUTTON_SPEAKER_OFF = "button_speaker_off";
                public static final String WELCOME_SOUND_ON = "welcome_sound_on";
                public static final String WELCOME_SOUND_OFF = "welcome_sound_off";
                public static final String WELCOME_RATE = "welcome_rate";
                public static final String WELCOME_RATE_PRESSED = "welcome_rate_pressed";
                public static final String WELCOME_FACEBOOK = "welcome_facebook";
                public static final String WELCOME_FACEBOOK_PRESSED = "welcome_facebook_pressed";
                public static final String WELCOME_WEBSITE = "welcome_website";
                public static final String WELCOME_WEBSITE_PRESSED = "welcome_website_pressed";
                public static final String WELCOME_RATE_SUBMIT = "rate_submit";
                public static final String WELCOME_RATE_SUBMIT_PRESSED = "rate_submit_pressed";
                public static final String WELCOME_RATE_GOOGLE = "rate_google";
                public static final String WELCOME_RATE_GOOGLE_PRESSED = "rate_google_pressed";
                public static final String WELCOME_HIGHSCORES = "welcome_highscores";
                public static final String WELCOME_HIGHSCORES_PRESSED = "welcome_highscores_pressed";
                public static final String PRESSED = "_pressed";
                public static final String BUTTON_HELP_PRESSED = "button_help_pressed";
                public static final String BUTTON_EXIT_PRESSED = "button_exit_pressed";
                public static final String BUTTON_OPTIONS_PRESSED = "button_options_pressed";
                public static final String BUTTON_BOMBS_DISABLED = "button_bombs_closed";
                public static final String BUTTON_BOMBS_CHECKED = "button_bombs_checked";
                public static final String BUTTON_OPTIONS_CHECKED = "button_options_checked";
                public static final String BUTTON_BIO_HAZARD = "button_biohazard";
                public static final String BUTTON_BIO_HAZARD_PRESSED = "button_biohazard_pressed";
                public static final String BUTTON_AIR_STRIKE = "button_airstrike";
                public static final String BUTTON_ATOM = "button_atom";
                public static final String BUTTON_AIRSTRIKE_PRESSED = "button_airstrike_pressed";
                public static final String BUTTON_ATOM_PRESSED = "button_atom_pressed";
                public static final String BUTTON_BIO_HAZARD_DISABLED = "button_biohazard_closed";
                public static final String BUTTON_AIRSTRIKE_DISABLED = "button_airstrike_closed";
                public static final String BUTTON_ATOM_DISABLED = "button_atom_closed";
                public static final String INGAME_BUTTON_INDICATOR = "ingame_button_indicator";
                public static final String INGAME_BUTTON_HIGHLIGHT = "ingame_button_highlight";
                public static final String AIR_STRIKE_PLANE = "airstrikeplane";
                public static final String AIRSTRIKE = "airstrike";
                public static final String ATOM = "atom";
                public static final String WHITE_FLASH = "white_flash";
                public static final String ALARM = "alarm";
                public static final String HEAT_GAUGE = "heat_gauge";
                public static final String NEEDLE = "needle";
                public static final String MINI_HUD = "mini_hud";
                public static final String INSIDE = "inside";
                public static final String EDGE = "edge";
                public static final String HORIZONTAL = "horizontal";
                public static final String VERTICAL = "vertical";
                public static final String HOLDER = "holder";
                public static final String ICON_SCORE = "score_icon";
                public static final String ICON_COINS = "coins_icon";
                public static final String ICON_CROSSHAIR = "crosshair_icon";
                public static final String SELECTION_SCREEN_LEFT = "selection_screen_left";
                public static final String SELECTION_SCREEN_RIGHT = "selection_screen_right";
                public static final String BOTTOM_TUBE = "bottom_tube";
                public static final String TOP_TUBE = "top_tube";
                public static final String CANNONS_BUTTON = "cannons_button";
                public static final String CANNONS_BUTTON_PRESSED = "cannons_button_pressed";
                public static final String CANNONS_BUTTON_CHECK = "cannons_button_selected";
                public static final String ARMORY_BUTTON = "armory_button";
                public static final String ARMORY_BUTTON_PRESSED = "armory_button_pressed";
                public static final String ARMORY_BUTTON_CHECK = "armory_button_selected";
                public static final String SIDEKICK_BUTTON = "sidekick_button";
                public static final String SIDEKICK_BUTTON_PRESSED = "sidekick_button_pressed";
                public static final String SIDEKICK_BUTTON_CHECK = "sidekick_button_selected";
                public static final String MAIN_SCREEN_LEFT = "main_screen_left";
                public static final String MAIN_SCREEN_RIGHT = "main_screen_right";
                public static final String THUMB_CANNON_BALL = "cannon_ball_thumb";
                public static final String THUMB_SPREAD = "spread_thumb";
                public static final String THUMB_CHAINGUN = "chaingun_thumb";
                public static final String THUMB_ROCKET_LAUNCHER = "rocket_thumb";
                public static final String THUMB_MISSILE_LAUNCHER = "missile_thumb";
                public static final String THUMB_BLASTER = "blaster_thumb";
                public static final String THUMB_TWIN_LASER = "twin_laser_thumb";
                public static final String THUMB_SHOCKWAVE = "shockwave_thumb";
                public static final String THUMB_BUNKER = "bunker_thumb";
                public static final String THUMB_BIO = "bio_thumb";
                public static final String THUMB_AIR = "air_strike_thumb";
                public static final String THUMB_ATOM = "atom_thumb";
                public static final String THUMB_WATCH = "watch_thumb";
                public static final String THUMB_TESLA = "tesla_thumb";
                public static final String THUMB_FLAMER = "flamer_thumb";
                public static final String THUMB_HEAT = "heat_thumb";
                public static final String THUMB_AUTO = "auto_thumb";
                public static final String THUMB_DOME = "dome_thumb";
                public static final String THUMB_PHANTOM = "phantom_thumb";
                public static final String LEFT_ARROW = "left_arrow";
                public static final String RIGHT_ARROW = "right_arrow";
                public static final String RIGHT_ARROW_PRESSED = "right_arrow_pressed";
                public static final String LEFT_ARROW_PRESSED = "left_arrow_pressed";
                public static final String ICON_FIST = "fist_icon";
                public static final String ICON_BULLETS = "bullet_icon";
                public static final String ICON_TIMER = "timer_icon";
                public static final String ICON_ARMOR = "armor_icon";
                public static final String ICON_ERROR = "error_icon";
                public static final String PLUS_BUTTON = "plus_button";
                public static final String PLUS_BUTTON_PRESSED = "plus_button_pressed";
                public static final String UPGRADE_BUTTON = "upgrade_button";
                public static final String UPGRADE_BUTTON_PRESSED = "upgrade_button_pressed";
                public static final String CELL_EMPTY = "empty_cell";
                public static final String CELL_FULL = "full_cell";
                public static final String EMPTY_SCREEN = "empty_screen";
                public static final String CLOSE_BUTTON = "close_button";
                public static final String CLOSE_BUTTON_PRESSED = "close_button_pressed";
                public static final String PURCHASE_BUTTON = "purchase_button";
                public static final String PURCHASE_BUTTON_PRESSED = "purchase_button_pressed";
                public static final String COINS_BUTTON = "coins_button";
                public static final String COINS_BUTTON_PRESSED = "coins_button_pressed";
                public static final String BACK_BUTTON = "back_button";
                public static final String BACK_BUTTON_PRESSED = "back_button_pressed";
                public static final String BIG_ARROW_LEFT = "big_arrow_left";
                public static final String BIG_ARROW_LEFT_PRESSED = "big_arrow_left_pressed";
                public static final String BIG_ARROW_RIGHT = "big_arrow_right";
                public static final String BIG_ARROW_RIGHT_PRESSED = "big_arrow_right_pressed";
                public static final String ICON_GEAR = "gears_icon";
                public static final String ILLUSTRATION_CANNON_BALL = "ill_cannon_ball";
                public static final String ILLUSTRATION_SPREAD = "ill_spread";
                public static final String ILLUSTRATION_CHAIN = "ill_chain";
                public static final String ILLUSTRATION_ROCKET = "ill_rocket";
                public static final String ILLUSTRATION_HOMING = "ill_homing";
                public static final String ILLUSTRATION_BLASTER = "ill_blaster";
                public static final String ILLUSTRATION_TWIN = "ill_twin";
                public static final String ILLUSTRATION_TESLA = "ill_tesla";
                public static final String TOP_LEFT_CORNER = "top_left_corner";
                public static final String TOP_RIGHT_CORNER = "top_right_corner";
                public static final String BOTTOM_LEFT_CORNER = "bottom_left_corner";
                public static final String BOTTOM_RIGHT_CORNER = "bottom_right_corner";
                public static final String SCREEN_HOR_TOP = "screen_hor_top";
                public static final String SCREEN_HOR_BOTTOM = "screen_hor_bottom";
                public static final String SCREEN_VER_LEFT = "screen_ver_left";
                public static final String SCREEN_VER_RIGHT = "screen_ver_right";
                public static final String SCREEN_RED_LIGHT = "screen_red_light";
                public static final String BUTTON_PLAY = "play_button";
                public static final String BUTTON_PLAY_PRESSED = "play_button_pressed";
                public static final String BUTTON_MAIN_HELP = "main_help_button";
                public static final String BUTTON_MAIN_HELP_PRESSED = "main_help_button_pressed";
                public static final String BUTTON_MAIN_OPTIONS = "main_options_button";
                public static final String BUTTON_MAIN_OPTIONS_PRESSED = "main_options_button_pressed";
                public static final String GADARTS = "gadarts";
                public static final String TOP_LEFT_LOADING = "top_left_loading";
                public static final String TOP_RIGHT_LOADING = "top_right_loading";
                public static final String BOTTOM_LEFT_LOADING = "bottom_left_loading";
                public static final String BOTTOM_RIGHT_LOADING = "bottom_right_loading";
                public static final String LOADING_SCREEN = "loading_screen";
                public static final String LIGHT = "light";
                public static final String LOBBY_BUNKER = "lobby_bunker";
                public static final String INFO_BORDER = "info_border";
                public static final String INFO_BORDER_PRESSED = "info_border_pressed";
                public static final String LOBBY_UPGRADE_BUTTON = "lobby_upgrade_button";
                public static final String LOBBY_UPGRADE_BUTTON_PRESSED = "lobby_upgrade_button_pressed";
                public static final String LOBBY_UNLOCK_BUTTON = "lobby_unlock_button";
                public static final String LOBBY_UNLOCK_BUTTON_PRESSED = "lobby_unlock_button_pressed";
                public static final String SMALL_BIO_ICON = "small_bio_icon";
                public static final String SMALL_BOMB_ICON = "small_bomb_icon";
                public static final String SMALL_ATOM_ICON = "small_atom_icon";
                public static final String SMALL_ARMOR_ICON = "small_shield_icon";
                public static final String SMALL_GEAR_ICON = "small_gear_icon";
                public static final String SMALL_COIN_ICON = "small_coin_icon";
                public static final String ICON_CANNON = "icon_cannon";
                public static final String ICON_SPREAD = "icon_spread";
                public static final String ICON_CHAIN = "icon_chain";
                public static final String ICON_ROCKET = "icon_rocket";
                public static final String ICON_MISSILE = "icon_missile";
                public static final String ICON_BLASTER = "icon_blaster";
                public static final String ICON_TWIN = "icon_twin";
                public static final String ICON_SHOCK = "icon_shock";
                public static final String ICON_WATCH_TOWER = "icon_watch";
                public static final String ICON_TESLA = "icon_tesla";
                public static final String ICON_FLAMER = "icon_flamer";
                public static final String ICON_HEAT = "icon_heat";
                public static final String ICON_AUTO = "icon_auto";
                public static final String ICON_HEMISPHERE = "icon_hemisphere";
                public static final String ICON_PHANTOM = "icon_phantom";
                public static final String LOCK = "lock";
                public static final String NO_AMMO_SIGN = "no_ammo_sign";
                public static final String SELECTED_SIGN_BIG = "selected_sign_big";
                public static final String SELECTED_SIGN = "selected_sign";
                public static final String LEVEL_BUTTON = "level_button";
                public static final String LEVEL_BUTTON_PRESSED = "level_button_pressed";
                public static final String STAR_LEVEL_FILLED = "star_level";
                public static final String STAR_LEVEL_EMPTY = "empty_star_level";
                public static final String INFO_BUTTON = "info_button";
                public static final String ATTACK_BUTTON = "attack_button";
                public static final String ATTACK_BUTTON_PRESSED = "attack_button_pressed";
                public static final String WELCOME_RATE_FULL_STAR = "rate_full_star";
                public static final String WELCOME_RATE_BLANK_STAR = "rate_blank_star";
                public static final String PROFILE = "profile";
                public static final String HELP = "help_";
                public static final String HELP_INGAME = "help_ingame";
                public static final String OPTIONS_MESSAGES = "options_messages";
                public static final String OPTIONS_MESSAGES_PRESSED = "options_messages_pressed";
                public static final String OPTIONS_MESSAGES_CHECKED = "options_messages_checked";
                public static final String OPTIONS_EPILEPSY = "options_epilepsy";
                public static final String OPTIONS_EPILEPSY_PRESSED = "options_epilepsy_pressed";
                public static final String OPTIONS_EPILEPSY_CHECKED = "options_epilepsy_checked";
                public static final String OPTIONS_GOOGLE_GAMES = "options_google_games";
                public static final String OPTIONS_GOOGLE_GAMES_PRESSED = "options_google_games_pressed";
                public static final String OPTIONS_GOOGLE_GAMES_CHECKED = "options_google_games_checked";
                public static final String OPTIONS_SWIPE = "options_swipe";
                public static final String OPTIONS_SWIPE_PRESSED = "options_swipe_pressed";
                public static final String OPTIONS_SWIPE_CHECKED = "options_swipe_checked";
                public static final String OPTIONS_TOUCH = "options_touch";
                public static final String OPTIONS_TOUCH_PRESSED = "options_touch_pressed";
                public static final String OPTIONS_TOUCH_CHECKED = "options_touch_checked";
                public static final String OPTIONS_VIBRATION = "options_vibration";
                public static final String OPTIONS_VIBRATION_PRESSED = "options_vibration_pressed";
                public static final String OPTIONS_VIBRATION_CHECKED = "options_vibration_checked";
                public static final String OPTIONS_RESTART = "options_restart";
                public static final String OPTIONS_RESTART_PRESSED = "options_restart_pressed";
                public static final String AMMO = "ammo";
                public static final String CANNON_UPGRADE = "cannon_upgrade";
                public static final String SIDEKICK_UPGRADE = "sidekick_upgrade";
                public static final String BUNKER_UPGRADE = "bunker_upgrade";
                public static final String TUTORIAL_CIRCLE = "tutorial_circle";
                public static final String TUTORIAL_ARROW = "tutorial_arrow";
                public static final String TUTORIAL_POINT = "tutorial_point";
                public static final String ICON_NONE = "icon_none";
                public static final String GAME_SAVED = "game_saved";
                public static final String OPTIONS_CLOUDSAVE = "options_cloudsave";
                public static final String OPTIONS_CLOUDSAVE_PRESSED = "options_cloudsave_pressed";
                public static final String OPTIONS_CLOUDSAVE_CHECKED = "options_cloudsave_checked";
                public static final String OPTIONS_CLOUDSAVE_DISABLED = "options_cloudsave_disabled";
                public static final String OPTIONS_CLOUDLOAD = "options_cloudload";
                public static final String OPTIONS_CLOUDLOAD_PRESSED = "options_cloudload_pressed";
                public static final String OPTIONS_CLOUDLOAD_DISABLED = "options_cloudload_disabled";
                public static final String SCROLL_KNOB = "scroll_knob";
                public static final String SCROLL_KNOB_BACKGROUND = "scroll_knob_background";
                public static final String LIBGDX = "libgdx";
                public static final String TENT = "tent";
                public static final String BIRD = "bird";
                public static final String FAR_PLANE = "far_plane";
                public static final String LAMP = "lamp";
                public static final String CONTAINERS = "containers";
                public static final String FENCE = "fence";
                public static final String SATELLITE = "satellite";
                public static final String LAMP_LIGHT = "lamp_light";
                public static final String CABIN = "cabin";
                public static final String PENGUIN = "penguin";
                public static final String SNOW_MAN = "snow_man";
                public static final String SNOWY_CABIN = "snowy_cabin";
                public static final String SNOWY_TREE = "snowy_tree";
                public static final String PALM_TREE = "palm_tree";
                public static final String PARASOL = "parasol";
                public static final String BOAT = "boat";
                public static final String SHEEP = "sheep";
                public static final String COW = "cow";
                public static final String MEADOW_TREE = "meadow_tree";
                public static final String MEADOW_TREE_SECOND = "meadow_tree_second";
                public static final String TRUCK = "truck";
                public static final String POLICE = "police";
                public static final String DEER = "deer";
                public static final String GIFT = "gift";
                public static final String SMALL_X = "small_x";
                public static final String SMALL_X_PRESSED = "small_x_pressed";
                public static final String TEXT_CURSOR = "text_cursor";
                public static final String TEXT_SELECTION = "text_selection";
                public static final String TEXT_INPUT_BACKGROUND = "text_input_background";
                public static final String DEAD_TREE_FIRST = "dead_tree_first";
                public static final String DEAD_TREE_SECOND = "dead_tree_second";
                public static final String TUTORIAL_BUTTON = "tutorial_button";
                public static final String TUTORIAL_BUTTON_PRESSED = "tutorial_button_pressed";
                public static final String CAMEL = "camel";
                public static final String BOSS_BODY = "boss_body";
                public static final String BOSS_GUN = "boss_gun";
                public static final String BOSS_GUN_DEAD = "boss_gun_dead";
                public static final String BOSS_GUN_SHOOT = "boss_gun_shoot";
                public static final String BOSS_GUN_BULLET = "boss_gun_bullet";
                public static final String BOSS_SHIELD = "boss_shield";
                public static final String MENTOR_POINTER = "mentor_pointer";
                public static final String MENTOR_SWIPE = "mentor_swipe";
                public static final String COINS_1 = "coins_1";
                public static final String COINS_2 = "coins_2";
                public static final String COINS_3 = "coins_3";
                public static final String COINS_4 = "coins_4";
            }

            public static final class Menus {
                static final String FOLDER_NAME = "menus";
                public static final String GENERAL_MENU_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "general_menu.txt";
                public static final String MAIN_MENU_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "main_menu_screen.txt";
                public static final String HELP_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "help_screen.txt";
                public static final String HELP_2_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "help_screen_2.txt";

                public static final String LOBBY_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "lobby.txt";
                public static final String SCENE_SELECTION_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "scene_selection.txt";
                public static final String WELCOME_BUTTONS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "welcome_buttons.txt";
                public static final String OPTIONS_BUTTONS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "options_buttons.txt";

                public static final class ShopMenu {

                    public static final String SHOP_MENU_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "shop_menu.txt";
                    public static final String SHOP_MENU_ADDITIONAL_SCREENS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "shop_additional_screens.txt";
                    public static final String SHOP_MENU_MAIN_SCREEN_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "buy_screen.txt";
                    public static final String CANNONS_THUMBS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "cannons_thumbs.txt";
                    public static final String ARMORY_THUMBS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "armory_thumbs.txt";
                    public static final String SIDEKICKS_THUMBS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "sidekicks_thumbs.txt";
                    public static final String SHOP_MENU_ILLUSTRATIONS_DATA_FILE = GFX_FOLDER_NAME + "/" + SHEETS_FOLDER_NAME + "/" + FOLDER_NAME + "/" + "illustrations.txt";

                }

            }


        }
    }


    public final class Configs {
        public static final String CONFIGS_FOLDER_NAME = "configs";
        public static final String GFX_FOLDER_NAME = "gfx";
        public static final String PARTICLE_CONFIGS_FOLDER_NAME = "particle_effects";
        public static final String PARTICLE_CONFIGS_DATA_EXTENSION = "p";
        public static final String PLAY_STORE_URL = "https://play.google.com/store/apps/details?id=com.gadarts.parashoot.android";
        public static final String LEADERBOARDS_ID = "CgkIpf2rw7cdEAIQAQ";

        public final class GFX {
            public static final String VERTEX_SHADER = "vertex_shader.vs";
            public static final String RED_SHADER = "red_shader.fs";
            public static final String WHITE_SHADER = "white_shader.fs";
        }

        public final class ParticleEffects {
            public static final String SMOKE = "smoke";
            public static final String SMALL_SMOKE = "small_smoke";
            public static final String EXPLOSION = "exp";
            public static final String BULLET_IMPACT = "bullet_impact";
            public static final String SMALL_PURPLE_SMOKE = "small_purple_smoke";
            public static final String PURPLE_EXPLOSION = "purple_exp";
            public static final String SMALL_BLUE_SMOKE = "small_blue_smoke";
            public static final String DAMAGE_SMOKE = "damage_smoke";
            public static final String BIG_DAMAGE_SMOKE = "big_damage_smoke";
            public static final String DEAD_SMOKE = "dead_smoke";
            public static final String BIG_EXPLOSION_UP = "big_exp_up";
            public static final String HUGE_EXPLOSION_UP = "huge_exp_up";
            public static final String ATOMIC_EXPLOSION = "atomic_exp";
            public static final String HUGE_EXPLOSION = "huge_exp";
            public static final String HUGE_EXPLOSION_2 = "huge_exp_2";
            public static final String FIRE = "fire";
            public static final String SMALL_FIRE = "small_fire";
            public static final String ELECTRIC_HIT = "electric_hit";
            public static final String FLYING_CARTRIDGES = "flying_cartridges";
            public static final String SMALL_FLYING_CARTRIDGES = "small_flying_cartridges";
            public static final String WHITE_SMALL_SMOKE_UP = "white_small_smoke_up";
            public static final String WHITE_BIG_SMOKE_UP = "white_big_smoke_up";
            public static final String DOME_BLAST = "dome_blast";
            public static final String HOMING_MISSILE_SMOKE = "homing_missile_smoke";
            public static final String BALLISTIC_SMOKE = "ballistic_smoke";
            public static final String BLAST_RING = "blast_ring";
            public static final String BLAST_HORIZONTAL_RING = "blast_horizontal_ring";
            public static final String SMALL_BLAST_RING = "small_blast_ring";
            public static final String SHOCK_WAVE_BLAST = "shockwave";
            public static final String BLOOD = "blood";
            public static final String STARS = "stars";
            public static final String MISSILE_LAUNCH_SMOKE = "missile_launch_smoke";
            public static final String RAIN = "rain";
            public static final String SNOW = "snow";
            public static final String RICOCHET = "ricochet";
            public static final String CANNON_BALL_SMOKE = "cannon_ball_smoke";
            public static final String SPREAD_CANNON_SMOKE = "spread_cannon_smoke";
            public static final String PLASMA_BLAST = "plasma_blast";
            public static final String LOADING_PLASMA = "loading_plasma";
            public static final String BIG_BLAST_RING = "big_blast_ring";
            public static final String BONUS_STARS = "bonus_stars";
            public static final String BIOHAZARD_SMOKE = "biohazard_smoke";
            public static final String MENU_PARATROOPERS = "menu_paratroopers";
            public static final String SCENE_UNLOCKED_STARS = "scene_unlocked_stars";
        }

        public final class Preferences {


            public final class Mentors {
                public static final String PREF_MENTORS = "mentors";
            }

            public final class InGameGuides {
                public static final String PREF_GUIDES = "guides";
                public static final String TUTORIAL_COMPLETE = "tutorial_complete";
            }

            public final class Settings {
                public static final String DONT_ASK_TO_LOGIN = "dont_ask_to_login";
                public static final String PREF_SETTINGS = "settings";
                public static final String MESSAGES = "messages";
                public static final String VIBRATION = "vibration";
                public static final String EPILEPSY = "epilepsy";
                public static final String SWIPE = "swipe";
                public static final String TOUCH = "touch";
                public static final String GOOGLE_GAMES = "google_games";
                public static final String AUTO_CLOUD_SAVING = "auto_cloud_saving";
                public static final String SOUND = "sound";
                public static final String DAILY_GIFT_APPEAR = "daily_gift_appear";
                public static final String DAILY_GIFT_LAST_ENABLED = "daily_gift_last_enabled";
                public static final String DAILY_GIFT_LAST_CHECKED = "daily_gift_last_checked";
                public static final String INTRO_WAS_SHOWN = "intro_was_shown";
            }

            public final class Player {
                public static final String LEVELS_STATES = "levels";
                public static final String SCENES_STATES = "scenes";
                public static final String CANNONS = "cannons";
                public static final String SELECTED_CANNON = "selected_cannon";
                public static final String SELECTED_SIDE_KICK = "selected_side_kick";
                public static final String PREF_PLAYER = "player";
                public static final String BOMBS = "bombs";
                public static final String SIDE_KICKS = "side_kicks";
                public static final String BUNKER_ARMOR = "bunker_armor";
                public static final String BUNKER_GENERATOR = "bunker_generator";
                public static final String COINS = "coins";
                public static final String STARS = "stars";
                public static final String STATE = "state";
                public static final String SCORE = "score";
                public static final String VISITED_FACEBOOK = "visited_facebook";
            }
        }
    }

    public static final class Strings {
        public static final String PUBLIC_KEY = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAz8KwbcSekZu7IL59NdyyXJW2ftt7wSwddo8E+/S1+huwEama7Byj1tsesP9IUi1jPptYfvaDmnkjFrMgimPh8F2lDix2+S/wpv7tzRHmmae5EbLo9vTwCQFqifpYjtyKf33HRqIXEIsvulevY+bgQA8JIUdD7IOOIChZ/a1+aGa2lLTxJX01mwYINbtlxzZtM9Y61GsChQAuVEqJjtHl74DJyTQmZeP2AfzUIL4XFxKoAonckGJ677LcPOz/FKXYTFUR6iwbu3e7FnsQT3cBHNUPPIp46bnW97a9y8VhWduDFIq+gMByaGI2/s+lSyGKl13ggzOG90SQWd1lnbVxPQIDAQAB";
        public static final String FACEBOOK_PAGE = "https://www.facebook.com/parastrike";
        public static final String SERVER_ADDRESS = "http://18.221.180.105:80";


        public static final class Menu {

            public static final class MentorsMessages {

                public static final String UPGRADE_GENERATOR = "Your generator has reached his " +
                        "over-heat point in the last battle. You really should upgrade it in " +
                        "the Armory section to avoid that situation again.";

                public static final String UPGRADE_ARMOR = "You should upgrade your bunker's " +
                        "armor in order to avoid critical damage situations!";

                public static final String SCROLLABLE = "The screens are scrollable - swipe " +
                        "vertically to reveal more features.";

                public static final String CHANGE_CANNON = "Click on the cannon's icon in order " +
                        "to change selected cannon before entering the war zone.";

                public static final String CHANGE_SIDE_KICK = "Click on the side-kick's icon in " +
                        "order to change selected side-kick before entering the war zone.";

                public static final String GO_TO_BATTLE = "Click the crosshair in order to " +
                        "select level to play.";

                public static final String AMMUNITION_DEPLETED = "WATCH OUT! Your selected " +
                        "cannon's ammunition has depleted! Another cannon from your arsenal has " +
                        "been automatically selected for you. Visit the shop in order to " +
                        "purchase additional ammunition.";
            }

            public static final class GetMoreCoinsMonitor {
                public static final String HEADER = "Get Coins!";
                public static final String ADS_REMOVE = "Any purchase will remove all ads!";
                public static final String TAX = "Note: The prices don't include tax.";
                public static final String PLEASE_WAIT = "Please wait...";
            }

            public static final class ShopImproved {
                public static final class Monitor {

                    public static final String HEADER = "Shop";
                    public static final String UPGRADED_SUCCESSFUL = "%s was upgraded successfuly!";
                    public static final String PRICE = "Price: %sc";
                    public static final String MAXED = "Maxed Out!";
                    public static final String UNLIMITED = "Unlimited Ammo";
                    public static final String YOU_HAVE = "You have: %s/%s";
                    public static final String AMMO_PURCHASED = "One ammo unit was purchased succesfully!";
                    public static final String MAX_REACHED = "You've reached the maximum value!";
                    public static final String NOT_ENOUGH = "You don't have enough coins!";
                    public static final String UPGRADE_GEN = "Your generator has to be level %s or higher!";
                }

                public static final class AttributesDescriptions {
                    public static final String STRENGTH = "Determines the strength of each one " +
                            "of your bullets.";
                    public static final String RATE = "Determines the firing-rate of your " +
                            "weapon. Note yourself that regarding cannons - the higher the " +
                            "fire-rate is, the more generator power consumption is. But for " +
                            "side-kicks it doesn't matter.";
                    public static final String AMMO = "In order to use the cannon, you need to " +
                            "supply it with ammo. Every ammo unit is enough for only one level.";
                    public static final String ARMOR = "Determines the durability against enemy " +
                            "attacks.";
                    public static final String GENERATOR = "Determines your generator's ability " +
                            "to tolerate your cannons use and avoid over-heat. If your generator " +
                            "over-heats often, you should upgrade it.";
                }

            }

            public static final class SceneSelection {
                public static final class StarsInfoMonitor {
                    public static final String STARS_INFO_MONITOR_HEADER = "Stars";

                    public static final String TEXT = "Stars can unlock episodes. Every level has three skills that each one gives you a star when completing it. So go on and complete levels in order to gain more stars!";
                }

                public static final class LockedSceneMonitor {
                    public static final String HEADER = "Episode is Locked!";
                    public static final String LEFT_TEXT = "In order to unlock this episode, all you have left to reach is:";
                    public static final String YOU_HAVE_TEXT = "You have: ";
                }

                public static final String HEADER = "Select Episode";
                public static final String REQUIRES = "Requires:";
            }

            public static final class Options {
                public static final String HEADER_OPTIONS = "Options";
                public static final String MESSAGES = "Allow in-game messages";
                public static final String VIBRATION = "Allow vibration";
                public static final String EPILEPSY = "Allow sudden white screen flash";
                public static final String GOOGLE_GAMES = "Connect/Disconnect your Google Play account";
                public static final String CLOUD_LOAD = "Restore your progress from the cloud";
                public static final String CLOUD_SAVE = "Allow auto-saving your progress to the cloud ";
                public static final String RESTART = "Delete your progress";
                public static final String CONTROL = "Choose whether touching screen to shoot, swipe field below player to aim and shoot, or both";
                public static final String RESTART_CONFIRMATION = "Are you sure? This will delete all the progress you've achieved!";
                public static final String LOAD_CONFIRMATION = "Are you sure? This will overwrite all the progress you currently have on this phone!";
                public static final String IN_GAME = "In-Game";
                public static final String GENERAL = "General";
                public static final String RESTARTED = "Your game's progress has been reset";
            }

            public static final class Help {

                public static final class Bonuses {

                    public static final String FIX = "Fixes " + Rules.Player.Bonus.Fix.FIX_VALUE_PERCENT + "% of your bunker's armor.";
                    public static final String SUPER_FIX = "Fixes " + Rules.Player.Bonus.Fix.SUPER_FIX_VALUE_PERCENT + "% of your bunker's armor.";
                    public static final String MONEY = "Gives you " + Rules.Player.Bonus.Money.MONEY_VALUE + " coins.";
                    public static final String SUPER_MONEY = "Gives you " + Rules.Player.Bonus.Money.SUPER_MONEY_VALUE + " coins.";
                    public static final String SHIELD = "Wraps your bunker with an invulnerable shield for " + Rules.Player.Bonus.Shield.DURATION + " seconds.";
                    public static final String AMMO = "Gives you one ammo unit for your current cannon. Won't appear if the current cannon is " + BulletType.CANNON_BALL.getName();
                    public static final String CANNON_UPGRADE = "Upgrades all of your current cannon's attributes.";
                    public static final String SIDEKICK_UPGRADE = "Upgrades all of your current side-kick's attributes. Won't appear if there are no side-kicks.";
                    public static final String BUNKER_UPGRADE = "Upgrades all of your current bunker's attributes.";
                    public static final String GAS_BOMB = "Calls for an allied air-craft to drop a bio-hazard bomb, which on ground impact emits gases that eliminate all landed paratroopers. Lasts for " + Rules.Player.Bonus.BIOHAZARD_DURATION + " seconds.";
                    public static final String AIR_STRIKE = "Calls for an air-strike.";
                    public static final String ATOM = "Calls for an allied air-craft to drop an atomic bomb.";
                }

                public static final String HEADER_BONUSES = "Bonuses";

                public static final String HEADER_CREDITS = "Credits";
                public static final String HEADER_HOW_TO_PLAY = "How To Play";
                public static final String LEFT_SIDE_CREDITS = "All programming, graphics, sound editing and music producing were made by Gad Wissberg.\n\n© 2018 GAD WISSBERG ALL RIGHTS RESERVED";
                public static final String ADDITIONAL_CREDITS = "Consulting: Barak Samnon.\nGame testing: Aviel Niego, Barak Samnon, Eli Aboudi, Guy Lifshitz, Hadas Feldbaum, Or Ben Ishayahoo and Ziv Slavin.\nSounds effects: freesound.org, findsounds.com. TTS: fromtexttospeech.com.\nI'd like to thank my girl-friend, family and friends for supporting me doing this project and all life challenges.";
                public static final String LIBGDX = "This game was made using the libGDX framework";
                public static final String ABOUT_ME_HEADER = "About Me:";
                public static final String ABOUT_ME = "My name is Gad Wissberg, a 25-years-old computer-science student, living with my girl-friend Liat and our dog Mona. The high-tech and arts fields are a major interest in my life, which makes the game development world the most attracting zone for me. Since I was young I loved to draw, sculpt, compose music and messing around with every creative hobby I could find. While doing that, playing video-games was my activity to expand the boundaries of my imagination. This game is the product of my passion to create. Also, as a computer science student with no time for full-time job, I hope this game would help me provide my-self. I hope you enjoy this game as much as I enjoyed creating it.";
                public static final String TAP_TO_MAIL = "Click to mail me";
                public static final String MAIL_ADDRESS = "gadw17@gmail.com";
                public static final String MAIL_SUBJECT = "About Para-Strike";
            }

            public static final class WelcomeScreen {
                public static final String RATE_HEADER = "Rate This Game!";
                public static final String THANK_YOU_HEADER = "Thank you!";
                public static final String PLAY_STORE_TEXT = "Please rate this game also in the Google Play Store too!";
                public static final String VISIT_FACEBOOK = "Like ParaStrike's Facebook page and get %d coins!";
                public static final String VISIT_FACEBOOK_HEADER = "On Facebook";
                public static final String THANKS_TEXT = "Thank you for rating this game!";
            }

            public static final class LevelSelection {

                public static final class SceneCompletedMonitor {
                    public static final String HOORAY = "Hooray!";
                    public static final String TEXT = "Congratulations! You've finished %s! You are rewarded with %d coins!";
                }

                public static final String HEADER = "Select Level";
            }

            public static final class Lobby {
                public static final class GiftMonitor {

                    public static final String HEADER = "Daily Gift!";
                    public static final String MESSAGE = "Visit the game's facebook page, look for the daily password and get " + Rules.Menu.Lobby.Gift.GIFT_VALUE + " coins!";
                    public static final String INPUT_HEADER = "Password?";
                    public static final String INPUT_HINT = "Insert here the password...";
                    public static final String INITIAL_VALUE = "Tap here to enter password";
                    public static final String MESSAGE_SUCCESS = "Excellent! You got " + Rules.Menu.Lobby.Gift.GIFT_VALUE + " coins! Keep Rocking!";
                    public static final String MESSAGE_FAIL = "Wrong! You can try again.";
                }

                public static final String HEADER_CANNON_SELECTION = "Select a Cannon";
                public static final String HEADER_SIDE_KICK_SELECTION = "Select a Side-Kick";
                public static final String AMMO_HEADER = "AMMO";

                public static final class InfoBorders {

                    public static final String BOMBS_HEADER = "Your Bombs:";
                    public static final String CANNON_HEADER = "Selected Cannon:";
                    public static final String SIDEKICK_HEADER = "Selected S.K.:";
                    public static final String BUNKER_HEADER = "Your Bunker:";
                    public static final String LABEL_CHANGE = "TAP TO CHANGE";
                }

                public static final String HEADER = "BUNKER MANAGEMENT";
            }

            public static final class Shop {

                public static final String NOT_ENOUGH_COINS = "Not enough coins!";
                public static final String UPGRADE_GENERATOR = "Generator level required: ";
                public static final String PURCHASED_SUCCESSFULLY = "You've purchased a new item!";
                public static final String UPGRADED_SUCCESSFULLY = "Upgraded successfully!";
                public static final String AMMO_PURCHASED_SUCCESSFULLY = "Ammo has been purchased!";
                public static final String YOU_HAVE = "You have: ";
                public static final String REACHED_MAX = "You've reached the maximum value!";
                public static final String GET_MORE_COINS = "Get More Coins";
                public static final String MIN_GENERATOR_REQ = "Min. generator level required: ";

                public static final class Stats {
                    public static final String ENABLED = "Enabled";
                    public static final String STRENGTH = "Strength";
                    public static final String AMMO = "Ammo";
                    public static final String AMOUNT = "Amount";
                    public static final String RATE = "Fire-Rate";
                    public static final String ARMOR = "Armor";
                    public static final String GENERATOR = "Gen.";
                    public static final String COINS = "Coins";
                    public static final String UNLIMITED_AMMO = "Unlimited Ammo";
                }

                public static final class WeaponsDescriptions {

                    public static final String CANNON = "Your default choice for combat. Shoots single projectiles in a low rate-fire. Even though it has been defined to be the weakest arm to use, upgrading it's abilities can make it an effective weapon considering the cost. Do not underestimate it's value in hard days.";
                    public static final String SPREAD_CANNON = "Triple your cannon ball with the Bear Claw. This weapon is excellent for cleaning your field of view filled with scattered enemy units, by using three cannons instead of one. Since this arm needs more power to handle it's attack, note yourself that you'll have to invest more in order for it to become useful against heavy targets.";
                    public static final String CHAIN_GUN = "Great choice for a quick annihilation against hordes of small-medium targets. Shoots small projectiles in a high-rate attack. Handle quickly multiple targets in different locations. Strong generators are extremely recommended for it to function effectively.";
                    public static final String ROCKET_LAUNCHER = "Your top choice for a cheap fire-power against heavy targets and hordes of small enemy units. This machine launches explosive rockets, which explode with a large blast radius. Since this is an extremely heavy arm, it is recommended to invest on it's fire-rate upgrades.";
                    public static final String MISSILE_LAUNCHER = "Excellent solution for taking down fast enemy units. The Iron Dome launches couples of missiles - One homing and one straight-forward - in a medium fire-rate. Remember, the Iron Dome's homing missiles home enemy air-crafts only!";
                    public static final String BLASTER = "The next generation of the heavy laser cannons. This cannon launches heavy plasma balls, creating four smaller plasma balls in it's impact - Great way for taking care of targets in different locations. Just like the Bomber, it is recommended to invest on it's fire-rate upgrades.";
                    public static final String LASER = "The ultimate weapon to handle any kind of threat. Shoots powerful laser beams in a high fire-rate, using a double cannon. It's price worth it's benefits. This fire-power requires a powerful generator!";
                    public static final String SHOCK = "The ECP-5000 is a top-secret military project. This device uses the Tesla lightning technology, giving the ability to focus a very high electricity energy in the selected position on the screen. No need to aim, just click the position and it'll unleash a very high electricity energy in the selected position. This device requires a very powerful generator.";
                }

                public static final class SideKickDescriptions {

                    public static final String INFANTRY = "A manned defense tower. Cheap and armed with a machine gun, but can attack only ground threats.";
                    public static final String TESLA = "An excellent choice to handle quickly landed paratroopers. Strikes lightning at it's targets, it takes only one hit to eliminate enemy soldiers. Can handle only ground threats. Since every attack takes a few moments to load, it is recommended to invest on it's fire-rate.";
                    public static final String FLAMER = "The Surtr would make sure to clean a whole area out of enemy soldiers with one hit of flame. Can attack only ground threats.";
                    public static final String HEAT_TURRET = "One of the latest advanced technology. This machine would dispatch high-energized beams up to 3 targets, causing them to heat-up and explode/burn. Unfortunately, it can only aim ground targets.";
                    public static final String AUTO = "Probably one of the greatest side-kick machine there is. The Jaguar would automatically aim & take down every target with it's powerful machine gun - both ground & air targets! Note yourself that it's gun can over-heat.";
                    public static final String HEMISPHERE = "One of the greatest defensive structures. These crystal-blue weapons would gather energy and unleash an homing plasma ball, causing a large blast on impact. Aim ground & air threats.";
                    public static final String PHANTOM = "The strongest structure there is. The Phantom would aim every target and launch a super beam, causing a fire on it's impact.";
                }

                public static final class ArmoryNames {

                    public static final String BUNKER = "Your Bunker";
                    public static final String BIO = "Bio-Hazard";
                    public static final String AIR = "Air-Strike";
                    public static final String ATOM = "Atomic Bomb";

                    public static final class ShortArmoryNames {
                        public static final String BIO = "Bio";
                        public static final String AIR = "Air";
                        public static final String ATOM = "Atom";
                    }
                }

                public static final class ArmoryDescriptions {

                    public static final String YOUR_BUNKER = "This is your bunker, fully controlled by you. You can upgrade it's armor to make it more durable against enemy attacks and upgrade it's generator to handle the cannon's power demand and avoid weapon over-heat. Remember, once the bunker is destroyed - Game is over!";
                    public static final String BIO_HAZARD = "Using this would emit bio-hazard gas for a minute, making sure to clean out landed enemy paratroopers. Note yourself that this bomb doesn't affect ground/air crafts.";
                    public static final String AIR_STRIKE = "In a case of emergency, this would call for an allied air-strike plane and throw lethal bombs and would instantly destroy any enemy unit on impact.";
                    public static final String ATOM = "The best solution to clean the whole area out of enemy units. Detonating one of these would simply erase all enemy units in the war field - So make sure to use this when you're deep in trouble.";
                }

                public static final class WeaponsNames {
                    public static final String CANNON = "CB-100";
                    public static final String SPREAD_CANNON = "Bear Claw";
                    public static final String CHAIN_GUN = "Vulcan";
                    public static final String ROCKET_LAUNCHER = "Bomber";
                    public static final String MISSILE_LAUNCHER = "Iron Dome";
                    public static final String BLASTER = "Blaster";
                    public static final String LASER = "Twin Photon";
                    public static final String SHOCK = "ECP-5000";
                }

                public static final class SideKickNames {
                    public static final String INFANTRY = "Watch Tower";
                    public static final String TESLA = "Tesla Coil";
                    public static final String FLAMER = "Surtr";
                    public static final String HEAT_TURRET = "DEC-2000";
                    public static final String AUTO = "Jaguar";
                    public static final String HEMISPHERE = "Hemisphere";
                    public static final String PHANTOM = "Phantom";
                }
            }
        }

        @SuppressWarnings("WeakerAccess")
        public static final class InGameMessages {
            public static final String SIDE_KICK_LOST = "Side-Kick Lost!";
            public static final String EXIT_MESSAGE = "Are you sure? All progress in the current session will be lost!";
            public static final String GOOD_LUCK = "Good Luck!";
            public static final String MISSION_ACCOMPLISHED = "Mission Accomplished!";
            public static final String GAME_OVER = "Game Over!";

            public static final String PAUSE_MESSAGE = "Click anywhere to continue!";
            public static final String DAMAGE_CRITICAL = "DAMAGE CRITICAL!";
            public static final String OVER_HEAT = "WEAPON OVER-HEAT!";

            public static final class Bonus {
                public static final String FIX = "Damage has been repaired!";
                public static final String SUPER_FIX = "Damage has been fully repaired!";
                public static final String AMMO = "You got an additional ammo!";
                public static final String MONEY = " coins bonus!";
                public static final String SHIELD_ON = "Invulnerability on!";
                public static final String SHIELD_OFF = "Invulnerability off!";
                public static final String BIOHAZARD_BOMB = "You got a bio-Hazard bomb!";
                public static final String AIR_STRIKE_CALL = "You got an air-Strike call!";
                public static final String ATOM_BOMB = "You got an atom bomb!";
                public static final String INCOMING_AIRSTRIKE = "INCOMING AIR-STRIKE!";
                public static final String MAX_BOMBS = "YOU CANNOT HAVE MORE THAN " + Rules.Player.Bonus.MAX_BOMBS_TO_HAVE;
                public static final String MAX_BOMBS_BIOHAZARD = " BIO-HAZARD BOMBS!";
                public static final String MAX_BOMBS_AIRSTRIKE = " AIR-STRIKE CALLS!";
                public static final String MAX_BOMBS_ATOM = " ATOM BOMBS!";
                public static final String AMMO_MAX = "CANNON'S AMMO EXCEEDED!";
                public static final String CANNON_UPGRADED = "Cannon has been upgraded!";
                public static final String BUNKER_UPGRADED = "Bunker has been upgraded!";
                public static final String SIDEKICK_UPGRADED = "Side-Kick has been upgraded!";
            }

        }
    }

    @SuppressWarnings("WeakerAccess")
    public static final class FontsFileNames {
        static final String FONTS_FOLDER_NAME = "fonts";
        public static final String DIGITAL = FONTS_FOLDER_NAME + "/" + "digital.ttf";
        public static final String REGULAR = FONTS_FOLDER_NAME + "/" + "roboto.ttf";
        public static final String ARMY = FONTS_FOLDER_NAME + "/" + "army.ttf";
        public static final String DATA_EXTENSION = "ttf";
    }

    @SuppressWarnings("WeakerAccess")
    public final static class Levels {
        public static final String LEVELS_FOLDER_NAME = "levels";
        public static final String DATA_EXTENSION = "json";
        public static final String SCENE_FOLDER_NAME = "scene";
        public static final String LEVEL_FOLDER_NAME = "level";
        public static final String TUTORIAL_LEVEL_FILE_NAME = "tutorial";
    }
}
