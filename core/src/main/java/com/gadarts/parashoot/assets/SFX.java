package com.gadarts.parashoot.assets;

/**
 * Enum values for SFX files.
 */
public interface SFX {

    String getValue();

    fileAttribute getFormat();

    fileAttribute getParentDir();

    boolean isFpsDependent();

    enum Menu implements SFX {

        SYSTEM_CONFIRM("system_confirm"),
        SYSTEM_ERROR("system_error"),
        SYSTEM_WARNING("system_warning"),
        SCENE_UNLOCKED("scene_unlocked"),
        CANNON_PURCHASED("cannon_purchased");

        private final String value;

        Menu(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public boolean isFpsDependent() {
            return false;
        }

        public fileAttribute getParentDir() {
            return fileAttribute.MENUS_FOLDER_NAME;
        }

        @Override
        public fileAttribute getFormat() {
            return fileAttribute.SOUNDS_DATA_EXTENSION;
        }
    }

    enum Music implements SFX {
        BATTLE_1("battle_1"),
        BATTLE_2("battle_2"),
        BATTLE_3("battle_3"),
        BATTLE_4("battle_4"),
        BATTLE_5("battle_5"),
        BATTLE_6("battle_6"),
        BATTLE_7("battle_7"),
        BATTLE_8("battle_8"),
        BOSS("boss"),
        MENU("menu"),
        TUTORIAL("tutorial");
        private final String value;

        Music(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public boolean isFpsDependent() {
            return false;
        }

        public fileAttribute getParentDir() {
            return fileAttribute.MUSICS_FOLDER_NAME;
        }

        @Override
        public fileAttribute getFormat() {
            return fileAttribute.MUSIC_DATA_EXTENSION;
        }
    }

    enum Weapons implements SFX {

        CANNON_BALL("cannon_ball"),
        TRIPLE_CANNON_BALL("triple_cannon"),
        CHAIN_GUN("chain_gun"),
        ROCKET_LAUNCHER("rocket_launcher"),
        SPLIT_LASER("split_laser"),
        HOMING_LASER("homing_laser"),
        MISSILE_LAUNCH("missile_launch"),
        RAPID_LASER("rapid_laser"),
        SMALL_SPLIT_LASER("small_split"),
        OVER_HEAT("overheat"),
        SHOCK_WAVE("shockwave");

        private final String value;

        Weapons(String value) {
            this.value = value;
        }

        public boolean isFpsDependent() {
            return false;
        }

        public String getValue() {
            return value;
        }

        public fileAttribute getParentDir() {

            return fileAttribute.IN_GAME_FOLDER_NAME;
        }

        @Override
        public fileAttribute getFormat() {
            return fileAttribute.SOUNDS_DATA_EXTENSION;
        }
    }

    enum Misc implements SFX {
        EXPLOSION("explosion"),
        HIT("hit"),
        BIG_EXPLOSION("big_explosion"),
        HUGE_EXPLOSION("huge_explosion"),
        FIRE("fire"),
        FLAME_HIT("flame_hit"),
        GIB("gib"),
        THUNDER_1("thunder_1"),
        THUNDER_2("thunder_2"),
        ALARM("alarm"),
        BIRD("bird"),
        SHEEP("sheep"),
        COW("cow"),
        FAR_PLANE("far_plane"),
        HORN("horn"),
        AMB_BIRD("amb_bird"),
        AMB_DOG("amb_dog"),
        AMB_CAR_ALARM("amb_car_alarm"),
        AMB_DISTANT_EXP("amb_distant_exp"),
        AMB_DISTANT_SHOOTINGS("amb_distant_shootings"),
        AMB_NATURE("amb_nature"),
        AMB_SEAGULL("amb_seagull"),
        AMB_OWL("amb_owl"),
        AMB_CRICKETS("amb_crickets"),
        AMB_WIND("amb_wind"),
        SIREN("siren");

        private final String value;

        Misc(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public boolean isFpsDependent() {
            return true;
        }

        public fileAttribute getParentDir() {
            return fileAttribute.IN_GAME_FOLDER_NAME;
        }

        @Override
        public fileAttribute getFormat() {
            return fileAttribute.SOUNDS_DATA_EXTENSION;
        }
    }

    enum fileAttribute {
        SOUNDS_FOLDER_NAME("sounds"),
        SOUNDS_DATA_EXTENSION("wav"),
        IN_GAME_FOLDER_NAME("in_game"),
        MENUS_FOLDER_NAME("menus"),
        GENERAL_FOLDER_NAME("general"),
        MUSICS_FOLDER_NAME("music"),
        MUSIC_DATA_EXTENSION("ogg"),
        SFX_FOLDER_NAME("sfx");

        private final String value;

        fileAttribute(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

    }

    interface Player {

        enum SideKicks implements SFX {
            INFANTRY_TOWER_SHOOT("infantry_tower_shoot"),
            FLAME_THROWER_SHOOT("flame_thrower"),
            TESLA_LOAD("electricity"),
            TESLA_SHOOT("lightning_hit"),
            HEAT_BEAM("heat_beam"),
            HEAT_BEAM_ON("heat_beam_on"),
            AUTO_TURRET_SHOOT("auto_turret_shoot"),
            BEAM_TURRET_SHOOT("beam_turret_shoot"),
            DOME_LOADING_PLASMA("loading_plasma");

            private final String value;

            SideKicks(String value) {
                this.value = value;
            }

            public boolean isFpsDependent() {
                return true;
            }

            public String getValue() {

                return value;
            }

            public fileAttribute getParentDir() {
                return fileAttribute.IN_GAME_FOLDER_NAME;
            }

            @Override
            public fileAttribute getFormat() {
                return fileAttribute.SOUNDS_DATA_EXTENSION;
            }
        }

        enum Bonus implements SFX {
            BONUS("bonus"),
            ATOM("atom"),
            SHIELD_ACTIVATED("shield_activated"),
            SHIELD_DEPLETED("shield_depleted"),
            SHIELD_OFF("shield_off");
            private final String value;

            Bonus(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public boolean isFpsDependent() {
                return false;
            }

            public fileAttribute getParentDir() {
                return fileAttribute.IN_GAME_FOLDER_NAME;
            }

            @Override
            public fileAttribute getFormat() {
                return fileAttribute.SOUNDS_DATA_EXTENSION;
            }
        }

    }

    enum Taunts implements SFX {
        DAMAGE_CRITICAL("taunt_damage_critical"),
        CANNON_UPGRADE("cannon_upgraded"),
        SIDEKICK_UPGRADE("sidekick_upgraded"),
        BUNKER_UPGRADE("bunker_upgraded"),
        SIDE_KICK_LOST("taunt_sidekick_lost"),
        INCOMING_AIR_STRIKE("taunt_airstrike"),
        SHIELD_ON("taunt_shield_on"),
        SHIELD_OFF("taunt_shield_off"),
        DAMAGE_FIXED("taunt_damage_fix"),
        GOOD_LUCK("taunt_good_luck"),
        MISSION_ACCOMPLISHED("taunt_finish"),
        GAME_OVER("taunt_gameover"),
        ACHIEVEMENT_LOST("taunt_achievement_lost");

        private final String value;

        Taunts(String value) {
            this.value = value;
        }

        public String getValue() {

            return value;
        }

        public boolean isFpsDependent() {
            return false;
        }

        public fileAttribute getParentDir() {
            return fileAttribute.IN_GAME_FOLDER_NAME;
        }

        @Override
        public fileAttribute getFormat() {
            return fileAttribute.SOUNDS_DATA_EXTENSION;
        }
    }

    enum HUD implements SFX {
        BUTTON_CLICK("button_click"),
        PAUSE_MENU_MOVES("pause_menu_moves"),
        ENGINE_MOVE("engine_move");

        private final String value;

        HUD(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }

        public boolean isFpsDependent() {
            return false;
        }

        public fileAttribute getParentDir() {
            return fileAttribute.GENERAL_FOLDER_NAME;
        }

        @Override
        public fileAttribute getFormat() {
            return fileAttribute.SOUNDS_DATA_EXTENSION;
        }
    }

    interface Enemies {
        interface GroundUnits {
            enum GroundCrafts implements SFX {
                TANK_SHOOT("tank_shoot"),
                VEHICLE_MOVE("tank_move");
                private final String value;

                GroundCrafts(String value) {
                    this.value = value;
                }

                public String getValue() {
                    return value;
                }

                public boolean isFpsDependent() {
                    return true;
                }

                public fileAttribute getParentDir() {
                    return fileAttribute.IN_GAME_FOLDER_NAME;
                }

                @Override
                public fileAttribute getFormat() {
                    return fileAttribute.SOUNDS_DATA_EXTENSION;
                }
            }

            enum Paratroopers implements SFX {

                DEATH_1("soldier_die1"),
                DEATH_2("soldier_die2"),
                DEATH_3("soldier_die3"),
                DEATH_4("soldier_die4"),
                DEATH_5("soldier_die5"),
                DEATH_6("soldier_die6"),
                FALLING_1("soldier_fall_1"),
                FALLING_2("soldier_fall_2"),
                FALLING_3("soldier_fall_3"),
                FALLING_4("soldier_fall_4"),
                ELECTRIFIED("soldier_electrified"),
                INFANTRY_SHOOT("infantry_shoot"),
                CHAINGUNNER_SHOOT("chaingunner_shoot"),
                COUGH_1("caugh_1"),
                COUGH_2("caugh_2");
                private final String value;

                Paratroopers(String value) {
                    this.value = value;
                }

                public String getValue() {

                    return value;
                }

                public boolean isFpsDependent() {
                    return true;
                }

                public fileAttribute getParentDir() {
                    return fileAttribute.IN_GAME_FOLDER_NAME;
                }

                @Override
                public fileAttribute getFormat() {
                    return fileAttribute.SOUNDS_DATA_EXTENSION;
                }
            }

        }

        enum AirCrafts implements SFX {
            PLANE_FLY("plane_fly"),
            BOSS_SIGHT("boss_sight"),
            BOSS_PAIN("boss_pain"),
            BOSS_DIE("boss_die"),
            PLANE_FLYBY("yak_flyby"),
            FALLING_BALLISTIC("falling_ballistic"),
            PLANE_CRASH("plane_crash"),
            HELICOPTER_FLY("helicopter_fly"),
            BALLISTIC_FLY("ballistic_fly");

            private final String value;

            AirCrafts(String value) {
                this.value = value;
            }

            public String getValue() {
                return value;
            }

            public boolean isFpsDependent() {
                return true;
            }

            public fileAttribute getParentDir() {
                return fileAttribute.IN_GAME_FOLDER_NAME;
            }

            @Override
            public fileAttribute getFormat() {
                return fileAttribute.SOUNDS_DATA_EXTENSION;
            }
        }
    }
}