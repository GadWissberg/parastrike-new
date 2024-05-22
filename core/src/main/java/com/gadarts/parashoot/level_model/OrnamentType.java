package com.gadarts.parashoot.level_model;

import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.utils.Rules;

import static com.gadarts.parashoot.assets.Assets.GFX.Sheets.ImagesNames.LAMP_LIGHT;
import static com.gadarts.parashoot.utils.Rules.Level.GROUND_Y;
import static com.gadarts.parashoot.utils.Rules.Level.Scenes.MovingOrnaments.*;

/**
 * Created by Gad on 14/08/2017.
 */

public enum OrnamentType {
    TENT(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.TENT),
    SATELLITE(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.SATELLITE),
    FENCE(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.FENCE),
    BIRD(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.BIRD, BIRD_SPEED, -1, SFX.Misc.BIRD, true),
    FAR_PLANE(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.FAR_PLANE, FAR_PLANE_SPEED, -1, SFX.Misc.FAR_PLANE, true),
    LAMP(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.LAMP, LAMP_LIGHT),
    CONTAINERS(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.CONTAINERS),
    CABIN(Scene.MILITARY_ZONE, Assets.GFX.Sheets.ImagesNames.CABIN),
    SNOW_MAN(Scene.ARCTIC, Assets.GFX.Sheets.ImagesNames.SNOW_MAN),
    SNOWY_CABIN(Scene.ARCTIC, Assets.GFX.Sheets.ImagesNames.SNOWY_CABIN),
    SNOWY_TREE(Scene.ARCTIC, Assets.GFX.Sheets.ImagesNames.SNOWY_TREE),
    PENGUIN(Scene.ARCTIC, Assets.GFX.Sheets.ImagesNames.PENGUIN, PENGUIN_SPEED, GROUND_Y),
    PALM_TREE(Scene.BEACH, Assets.GFX.Sheets.ImagesNames.PALM_TREE),
    PARASOL(Scene.BEACH, Assets.GFX.Sheets.ImagesNames.PARASOL),
    BOAT(Scene.BEACH, Assets.GFX.Sheets.ImagesNames.BOAT, BOAT_SPEED, BOAT_Y),
    COW(Scene.MEADOW, Assets.GFX.Sheets.ImagesNames.COW, 0, GROUND_Y, SFX.Misc.COW, false),
    MEADOW_TREE(Scene.MEADOW, Assets.GFX.Sheets.ImagesNames.MEADOW_TREE),
    MEADOW_TREE_SECOND(Scene.MEADOW, Assets.GFX.Sheets.ImagesNames.MEADOW_TREE_SECOND),
    SHEEP(Scene.MEADOW, Assets.GFX.Sheets.ImagesNames.SHEEP, SHEEP_SPEED, GROUND_Y, SFX.Misc.SHEEP, true),
    TRUCK(Scene.CITY, Assets.GFX.Sheets.ImagesNames.TRUCK, TRUCK_SPEED, GROUND_Y, SFX.Misc.HORN, true),
    POLICE(Scene.CITY, Assets.GFX.Sheets.ImagesNames.POLICE, POLICE_SPEED, GROUND_Y, SFX.Misc.SIREN, true),
    DEER(Scene.FOREST, Assets.GFX.Sheets.ImagesNames.DEER, SHEEP_SPEED, GROUND_Y, true),
    CAMEL(Scene.DESERT, Assets.GFX.Sheets.ImagesNames.CAMEL, CAMEL_SPEED, GROUND_Y, true, CAMEL_FRAME_DURATION),
    DEAD_TREE_FIRST(Scene.MOUNTAINS, Assets.GFX.Sheets.ImagesNames.DEAD_TREE_FIRST),
    DEAD_TREE_SECOND(Scene.MOUNTAINS, Assets.GFX.Sheets.ImagesNames.DEAD_TREE_SECOND);

    private final String region;
    private final float speed;
    private final SFX sound;
    private final String lightRegion;
    private final Scene scene;
    private final int y;
    private final boolean faceDirection;
    private final float frameDuration;

    OrnamentType(Scene scene, String region) {
        this(scene, region, null, 0, GROUND_Y, null, false, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    OrnamentType(Scene scene, String region, String lightRegion) {
        this(scene, region, lightRegion, 0, GROUND_Y, null, false, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    OrnamentType(Scene scene, String region, float speed, int y) {
        this(scene, region, speed, y, null, false);
    }

    OrnamentType(Scene scene, String region, float speed, int y, boolean faceDirection) {
        this(scene, region, null, speed, y, null, faceDirection, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    OrnamentType(Scene scene, String region, float speed, int y, boolean faceDirection, float frameDuration) {
        this(scene, region, null, speed, y, null, faceDirection, frameDuration);
    }

    OrnamentType(Scene scene, String region, float speed, int y, SFX sound, boolean faceDirection) {
        this(scene, region, null, speed, y, sound, faceDirection, Rules.Misc.GENERAL_ANIMATION_FRAME_DURATION);
    }

    OrnamentType(Scene scene, String region, String lightRegion, float speed, int y, SFX sound, boolean faceDirection, float frameDuration) {
        this.region = region;
        this.speed = speed;
        this.sound = sound;
        this.lightRegion = lightRegion;
        this.scene = scene;
        this.y = y;
        this.faceDirection = faceDirection;
        this.frameDuration = frameDuration;
    }

    public String getRegion() {
        return region;
    }

    public Scene getScene() {
        return scene;
    }

    public boolean isFacingDirection() {
        return faceDirection;
    }

    public String getLightRegion() {
        return lightRegion;
    }

    public float getSpeed() {
        return speed;
    }

    public SFX getSound() {
        return sound;
    }

    public int getY() {
        return y;
    }

    public float getFrameDuration() {
        return frameDuration;
    }
}
