package com.gadarts.parashoot.level_model;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.utils.Rules;

public enum Scene {
    MILITARY_ZONE(Rules.Level.Scenes.Names.MILITARY_ZONE, Assets.GFX.Images.Menus.ScenesThumbs.MILITARY, Assets.GFX.Images.InGame.GroundTypes.MILITARY, Assets.GFX.Images.InGame.Landscapes.MILITARY),
    ARCTIC(Rules.Level.Scenes.Names.ARCTIC, Assets.GFX.Images.Menus.ScenesThumbs.ARCTIC, Assets.GFX.Images.InGame.GroundTypes.ARCTIC, Rules.Level.Scenes.Targets.Stars.ARCTIC, Assets.GFX.Images.InGame.Landscapes.ARCTIC),
    BEACH(Rules.Level.Scenes.Names.BEACH, Assets.GFX.Images.Menus.ScenesThumbs.BEACH, Assets.GFX.Images.InGame.GroundTypes.BEACH, Rules.Level.Scenes.Targets.Stars.BEACH, Assets.GFX.Images.InGame.Landscapes.BEACH_1, Assets.GFX.Images.InGame.Landscapes.BEACH_2),
    MEADOW(Rules.Level.Scenes.Names.MEADOW, Assets.GFX.Images.Menus.ScenesThumbs.MEADOW, Assets.GFX.Images.InGame.GroundTypes.MEADOW, Rules.Level.Scenes.Targets.Stars.MEADOW, Rules.Level.Scenes.Targets.Coins.MEADOW, false, Assets.GFX.Images.InGame.Landscapes.MEADOW),
    CITY(Rules.Level.Scenes.Names.CITY, Assets.GFX.Images.Menus.ScenesThumbs.CITY, Assets.GFX.Images.InGame.GroundTypes.CITY, Rules.Level.Scenes.Targets.Stars.CITY, Rules.Level.Scenes.Targets.Coins.CITY, false, Assets.GFX.Images.InGame.Landscapes.CITY_1, Assets.GFX.Images.InGame.Landscapes.CITY_2),
    MOUNTAINS(Rules.Level.Scenes.Names.MOUNTAINS, Assets.GFX.Images.Menus.ScenesThumbs.MOUNTAINS, Assets.GFX.Images.InGame.GroundTypes.MOUNTAINS, Rules.Level.Scenes.Targets.Stars.MOUNTAINS, Rules.Level.Scenes.Targets.Coins.MOUNTAINS, false, Assets.GFX.Images.InGame.Landscapes.MOUNTAINS),
    FOREST(Rules.Level.Scenes.Names.FOREST, Assets.GFX.Images.Menus.ScenesThumbs.FOREST, Assets.GFX.Images.InGame.GroundTypes.FOREST, Rules.Level.Scenes.Targets.Stars.FOREST, Rules.Level.Scenes.Targets.Coins.FOREST, false, Assets.GFX.Images.InGame.Landscapes.FOREST),
    DESERT(Rules.Level.Scenes.Names.DESERT, Assets.GFX.Images.Menus.ScenesThumbs.DESERT, Assets.GFX.Images.InGame.GroundTypes.DESERT, Rules.Level.Scenes.Targets.Stars.DESERT, Rules.Level.Scenes.Targets.Coins.DESERT, false, Assets.GFX.Images.InGame.Landscapes.DUNES),
    BOSS(Rules.Level.Scenes.Names.BOSS, Assets.GFX.Images.Menus.ScenesThumbs.BOSS, Assets.GFX.Images.InGame.GroundTypes.MILITARY, 0, 0, true);

    private final boolean locked;

    private int starsTarget;
    private int coinsTarget;
    private String displayName;
    private String thumb;
    private String groundImage;
    private String[] landscape;

    Scene(String displayName, String thumb, String groundImage, String... landscapeImages) {
        this(displayName, thumb, groundImage, 0, 0, false, landscapeImages);
    }

    Scene(String displayName, String thumb, String groundImage, int starsTarget, String... landscapeImages) {
        this(displayName, thumb, groundImage, starsTarget, 0, false, landscapeImages);
    }

    Scene(String displayName, String thumb, String groundImage, int starsTarget, int coinsTarget, boolean locked, String... landscapeImages) {
        this.displayName = displayName;
        this.thumb = thumb;
        this.groundImage = groundImage;
        this.landscape = landscapeImages;
        this.starsTarget = starsTarget;
        this.coinsTarget = coinsTarget;
        this.locked = locked;
    }

    public int getCoinsTarget() {
        return coinsTarget;
    }

    public int getStarsTarget() {
        return starsTarget;
    }

    public boolean isLocked() {
        return locked;
    }

    public String[] getLandscape() {
        return landscape;
    }

    public String getGroundImage() {
        return groundImage;
    }

    public String getDisplayName() {
        return displayName;
    }

    public String getThumb() {
        return thumb;
    }

}
