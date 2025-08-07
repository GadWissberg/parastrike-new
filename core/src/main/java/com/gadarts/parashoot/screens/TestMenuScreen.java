package com.gadarts.parashoot.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.model.object_factories.SideKickFactory;
import com.gadarts.parashoot.utils.Rules;
import com.gadarts.parashoot.weapons.BulletType;

/**
 * Created by Gad on 29/09/2015.
 */
public class TestMenuScreen implements Screen {
    private final BitmapFont buttonsFont;
    private final OrthographicCamera camera;
    private final TextureAtlas buttonsAtlas;
    private final Skin buttonSkin;
    private final BitmapFont textFont;
    private SpriteBatch batch = new SpriteBatch();
    private final Stage stage;
    private int selectedWeaponIndex;
    private int selectedSideKickIndex;
    private final BulletType[] weaponsOrder = new BulletType[8];
    private final SideKickFactory.SideKickType[] sideKicksOrder = new SideKickFactory.SideKickType[8];
    private int sideKickArmor = 1;
    private int sideKickShootingRate = 1;
    private int weaponShootingRate = 1;
    private int sideKickStrength = 1;
    private int weaponStrength = 1;
    private int enemyLevel;
    private int playerArmor = 1;
    private int generator = 1;

    public TestMenuScreen(final Parastrike main) {
        camera = new OrthographicCamera();
        camera.setToOrtho(false, 640, 480);

//        playerPrefs = Gdx.app.getPreferences(Assets.Configs.Preferences.Player.PREF_PLAYER);
//        if (!playerPrefs.contains(Assets.Configs.Preferences.Player.NAME)) {
//            Input.TextInputListener listener = new TextInputListener();
//            Gdx.input.getTextInput(listener, "What's your name?", "Fighter", "My Name");
//        }

        weaponsOrder[0] = BulletType.CANNON_BALL;
        weaponsOrder[1] = BulletType.SPREAD_CANNON_BALL;
        weaponsOrder[2] = BulletType.CHAIN_GUN_BULLET;
        weaponsOrder[3] = BulletType.ROCKET;
        weaponsOrder[4] = BulletType.HOMING_MISSILE;
        weaponsOrder[5] = BulletType.BLASTER;
        weaponsOrder[6] = BulletType.LASER;
        weaponsOrder[7] = BulletType.SHOCK_WAVE;

        sideKicksOrder[0] = null;
        sideKicksOrder[1] = SideKickFactory.SideKickType.INFANTRY_TOWER;
        sideKicksOrder[2] = SideKickFactory.SideKickType.TESLA;
        sideKicksOrder[3] = SideKickFactory.SideKickType.FLAMER;
        sideKicksOrder[4] = SideKickFactory.SideKickType.HEAT_TURRET;
        sideKicksOrder[5] = SideKickFactory.SideKickType.AUTO_TURRET;
        sideKicksOrder[6] = SideKickFactory.SideKickType.HEMISPHERE;
        sideKicksOrder[7] = SideKickFactory.SideKickType.PHANTOM;

        selectedSideKickIndex = 0;
        selectedWeaponIndex = 0;

        batch = new SpriteBatch();

        buttonsAtlas = new TextureAtlas("gfx/sheets/in_game/buttons.txt");
        buttonSkin = new Skin();
        buttonSkin.addRegions(buttonsAtlas);
        buttonsFont = new BitmapFont();
        buttonsFont.getData().setScale(2, 2);
        textFont = new BitmapFont();
        textFont.setColor(Color.RED);

        stage = new Stage();
        stage.clear();
        Gdx.input.setInputProcessor(stage);

        TextButton.TextButtonStyle style = new TextButton.TextButtonStyle();
        style.up = buttonSkin.getDrawable("button");
        style.font = buttonsFont;
        addArrowButton(style, "<", 20, Gdx.graphics.getHeight() - 200, 0);
        addArrowButton(style, ">", 600, Gdx.graphics.getHeight() - 200, 0);

        addArrowButton(style, "<", Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 200, 1);
        addArrowButton(style, ">", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 200, 1);

        addArrowButton(style, "<", 200, Gdx.graphics.getHeight() - 400, 8);
        addArrowButton(style, ">", 500, Gdx.graphics.getHeight() - 400, 8);

        addArrowButton(style, "<", 200, Gdx.graphics.getHeight() - 500, 5);
        addArrowButton(style, ">", 500, Gdx.graphics.getHeight() - 500, 5);

        addArrowButton(style, "<", 200, Gdx.graphics.getHeight() - 500, 5);
        addArrowButton(style, ">", 500, Gdx.graphics.getHeight() - 500, 5);

        addArrowButton(style, "<", 200, Gdx.graphics.getHeight() - 600, 6);
        addArrowButton(style, ">", 500, Gdx.graphics.getHeight() - 600, 6);

        addArrowButton(style, "<", Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 400, 2);
        addArrowButton(style, ">", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 400, 2);

        addArrowButton(style, "<", Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 500, 3);
        addArrowButton(style, ">", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 500, 3);

        addArrowButton(style, "<", Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 600, 4);
        addArrowButton(style, ">", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 600, 4);

        addArrowButton(style, "<", Gdx.graphics.getWidth() - 500, Gdx.graphics.getHeight() - 750, 7);
        addArrowButton(style, ">", Gdx.graphics.getWidth() - 200, Gdx.graphics.getHeight() - 750, 7);

        addArrowButton(style, "<", 200, Gdx.graphics.getHeight() - 700, 9);
        addArrowButton(style, ">", 500, Gdx.graphics.getHeight() - 700, 9);

        TextButton button = addButton(style, "QUIT", Gdx.graphics.getWidth() - 300, 10);
        button.addListener(new InputListener() {
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                Gdx.app.exit();
                return true;
            }
        });
        TextButton beginButton = addButton(style, "Begin", 10, 10);
        beginButton.addListener(new InputListener() {
            @Override
            public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                main.actionResolver.StartSession();
                main.goToBattleTest(weaponsOrder[selectedWeaponIndex], sideKicksOrder[selectedSideKickIndex], sideKickArmor, sideKickShootingRate, sideKickStrength, weaponShootingRate, weaponStrength, enemyLevel, playerArmor, generator);
                System.gc();
                return true;
            }
        });

    }

    private TextButton addButton(TextButton.TextButtonStyle style, String text, float x, float  y) {
        TextButton button = new TextButton(text, style);
        button.setPosition(x, y);
        button.setHeight(150);
        button.setWidth(300);
        stage.addActor(button);
        return button;
    }

    private void addArrowButton(TextButton.TextButtonStyle style, final String text, float x, float y, final int type) {
        TextButton button = new TextButton(text, style);
        button.setPosition(x, y);
        button.setHeight(75);
        button.setWidth(150);
        button.addListener(new InputListener() {
                               public boolean touchDown(InputEvent event, float x, float y, int pointer, int button) {
                                   switch (type) {
                                       case 0:
                                           if (text.equals(">")) {
                                               if (selectedWeaponIndex == weaponsOrder.length - 1) {
                                                   selectedWeaponIndex = 0;
                                               } else {
                                                   selectedWeaponIndex++;
                                               }
                                           } else {
                                               if (selectedWeaponIndex == 0) {
                                                   selectedWeaponIndex = weaponsOrder.length - 1;
                                               } else {
                                                   selectedWeaponIndex--;
                                               }
                                           }
                                           break;
                                       case 1:
                                           if (text.equals(">")) {
                                               if (selectedSideKickIndex == sideKicksOrder.length - 1) {
                                                   selectedSideKickIndex = 0;
                                               } else {
                                                   selectedSideKickIndex++;
                                               }
                                           } else {
                                               if (selectedSideKickIndex == 0) {
                                                   selectedSideKickIndex = sideKicksOrder.length - 1;
                                               } else {
                                                   selectedSideKickIndex--;
                                               }
                                           }
                                           break;
                                       case 2:
                                           if (text.equals(">")) {
                                               if (sideKickArmor < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   sideKickArmor += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   sideKickArmor = 1;
                                               }
                                           } else {
                                               if (sideKickArmor > 1) {
                                                   sideKickArmor -= Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   sideKickArmor = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 3:
                                           if (text.equals(">")) {
                                               if (sideKickShootingRate < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   sideKickShootingRate += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   sideKickShootingRate = 1;
                                               }
                                           } else {
                                               if (sideKickShootingRate > 1) {
                                                   sideKickShootingRate -= Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   sideKickShootingRate = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 4:
                                           if (text.equals(">")) {
                                               if (sideKickStrength < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   sideKickStrength += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   sideKickStrength = 1;
                                               }
                                           } else {
                                               if (sideKickStrength > 1) {
                                                   sideKickStrength -= Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   sideKickStrength = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 5:
                                           if (text.equals(">")) {
                                               if (weaponShootingRate < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   weaponShootingRate += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   weaponShootingRate = 1;
                                               }
                                           } else {
                                               if (weaponShootingRate > 1) {
                                                   weaponShootingRate -= Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   weaponShootingRate = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 6:
                                           if (text.equals(">")) {
                                               if (weaponStrength < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   weaponStrength += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   weaponStrength = 1;
                                               }
                                           } else {
                                               if (weaponStrength > 1) {
                                                   weaponStrength -= Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   weaponStrength = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 7:
                                           if (text.equals(">")) {
                                               if (enemyLevel < Rules.Enemies.GeneralAttributes.MAXIMUM_LEVEL) {
                                                   enemyLevel += 1;
                                               } else {
                                                   enemyLevel = 0;
                                               }
                                           } else {
                                               if (enemyLevel > 1) {
                                                   enemyLevel -= 1;
                                               } else {
                                                   enemyLevel = Rules.Enemies.GeneralAttributes.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 8:
                                           if (text.equals(">")) {
                                               if (playerArmor < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   playerArmor += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   playerArmor = 1;
                                               }
                                           } else {
                                               if (playerArmor > 1) {
                                                   playerArmor -= 1;
                                               } else {
                                                   playerArmor = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                       case 9:
                                           if (text.equals(">")) {
                                               if (generator < Rules.Cannons.MAXIMUM_LEVEL) {
                                                   generator += Rules.Cannons.UPGRADE_UNIT;
                                               } else {
                                                   generator = 1;
                                               }
                                           } else {
                                               if (generator > 1) {
                                                   generator -= 1;
                                               } else {
                                                   generator = Rules.Cannons.MAXIMUM_LEVEL;
                                               }
                                           }
                                           break;
                                   }
                                   return true;
                               }
                           }
        );
        stage.addActor(button);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.update();
        stage.act();
        batch.begin();
        stage.draw();
        batch.end();
        batch.begin();
        textFont.getData().setScale(3, 3);
        textFont.draw(batch, "SELECT WEAPON:", 50, Gdx.graphics.getHeight() - 50);
        textFont.draw(batch, "Skills:", Gdx.graphics.getWidth() / 2, Gdx.graphics.getHeight() - 250);
        textFont.draw(batch, "SELECT SIDEKICK:", Gdx.graphics.getWidth() - 700, Gdx.graphics.getHeight() - 50);
        textFont.draw(batch, "Armor:", Gdx.graphics.getWidth() - 650, Gdx.graphics.getHeight() - 350);
        textFont.draw(batch, "Armor:", 30, Gdx.graphics.getHeight() - 350);
        textFont.draw(batch, Integer.toString(sideKickArmor), Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 350);
        textFont.draw(batch, "Rate:", 30, Gdx.graphics.getHeight() - 450);
        textFont.draw(batch, Integer.toString(weaponShootingRate), 400, Gdx.graphics.getHeight() - 450);
        textFont.draw(batch, Integer.toString(playerArmor), 400, Gdx.graphics.getHeight() - 350);
        if (selectedSideKickIndex != 4) {
            textFont.draw(batch, "Rate:", Gdx.graphics.getWidth() - 650, Gdx.graphics.getHeight() - 450);
            textFont.draw(batch, Integer.toString(sideKickShootingRate), Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 450);
        }
        textFont.draw(batch, "Strength:", 15, Gdx.graphics.getHeight() - 550);
        textFont.draw(batch, "Gen.:", 15, Gdx.graphics.getHeight() - 650);
        textFont.draw(batch, Integer.toString(generator), 400, Gdx.graphics.getHeight() - 650);
        textFont.draw(batch, "Strength:", Gdx.graphics.getWidth() - 700, Gdx.graphics.getHeight() - 550);
        textFont.draw(batch, Integer.toString(weaponStrength), 400, Gdx.graphics.getHeight() - 550);
        textFont.draw(batch, Integer.toString(sideKickStrength), Gdx.graphics.getWidth() - 300, Gdx.graphics.getHeight() - 550);
        textFont.draw(batch, "Enemy Level:", Gdx.graphics.getWidth() - 800, Gdx.graphics.getHeight() - 700);
        textFont.draw(batch, levelToName(), Gdx.graphics.getWidth() - 325, Gdx.graphics.getHeight() - 700);
        drawWeaponName();
        drawSideKickName();
        textFont.getData().setScale(2, 2);
        textFont.draw(batch, "Parashoot 0.3 - Weapons & SideKicks Test\nUse the arrows to choose\nPress Back anytime to change weapon\nMade by Gad Wissberg 2016", (Gdx.graphics.getWidth() / 2) - 500, 300);
        batch.end();
    }

    private String levelToName() {
        if (enemyLevel == 0) {
            return "Easy";
        } else if (enemyLevel == 1) {
            return "Medium";
        } else {
            return "Hard";
        }
    }

    private void drawWeaponName() {
        String selectedWeaponName = null;
        switch (selectedWeaponIndex) {
            case 0:
                selectedWeaponName = "Cannon";
                break;
            case 1:
                selectedWeaponName = "Spread Cannon";
                break;
            case 2:
                selectedWeaponName = "Chain Cannon";
                break;
            case 3:
                selectedWeaponName = "Rocket Launcher";
                break;
            case 4:
                selectedWeaponName = "H. Missile Launcher";
                break;
            case 5:
                selectedWeaponName = "Blaster";
                break;
            case 6:
                selectedWeaponName = "Twin Cannon";
                break;
            case 7:
                selectedWeaponName = "Shock Wave";
                break;
        }
        textFont.draw(batch, selectedWeaponName, 200, Gdx.graphics.getHeight() - 150);
    }

    private void drawSideKickName() {
        String selectedSideKickName = null;
        switch (selectedSideKickIndex) {
            case 0:
                selectedSideKickName = "NO SIDE-KICKS";
                break;
            case 1:
                selectedSideKickName = "Infantry Tower";
                break;
            case 2:
                selectedSideKickName = "Tesla Coil";
                break;
            case 3:
                selectedSideKickName = "Flame Thrower";
                break;
            case 4:
                selectedSideKickName = "Heat Needle";
                break;
            case 5:
                selectedSideKickName = "Mini-Gunners";
                break;
            case 6:
                selectedSideKickName = "Dome";
                break;
            case 7:
                selectedSideKickName = "Phantom";
                break;
        }
        textFont.draw(batch, selectedSideKickName, Gdx.graphics.getWidth() - 620, Gdx.graphics.getHeight() - 150);
    }

    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        batch.dispose();
        buttonSkin.dispose();
        buttonsAtlas.dispose();
        buttonsFont.dispose();
        stage.dispose();
    }


}
