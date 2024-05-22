package com.gadarts.parashoot.model;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.utils.GdxRuntimeException;
import com.gadarts.parashoot.Parastrike;
import com.gadarts.parashoot.assets.Assets;
import com.gadarts.parashoot.assets.SFX;
import com.gadarts.parashoot.utils.GameSettings;
import com.gadarts.parashoot.utils.Rules;

import java.util.Collection;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by Gad on 04/08/2015.
 */
public class SoundPlayer {

    private HashMap<String, Sound> sounds = new HashMap<String, Sound>();
    private Music music;
    private Random randomizer = new Random();

    public long playSound(SFX fileName) {
        return playSound(fileName, false);
    }

    public long playSound(SFX fileName, boolean loop, float volume) {
        return playSound(fileName, loop, volume, true);
    }

    public long playSound(SFX fileName, boolean loop, boolean randomPitch) {
        return playSound(fileName, loop, 1, randomPitch);
    }

    public long playSound(SFX fileName, boolean loop) {
        return playSound(fileName, loop, 1, true);
    }

    /**
     * Plays a sound.
     *
     * @param fileName    The sound's file name.
     * @param loop        TEMPORARILY DISABLED BECAUSE OF AN UNKNOWN BUG.
     * @param volume      Specified volume.
     * @param randomPitch Whether to play in random pitch or not.
     * @return The played sound id. Returns -1 if failed.
     */
    public long playSound(SFX fileName, boolean loop, float volume, boolean randomPitch) {
        if (GameSettings.SOUND_TOGGLE && fileName != null) {
//            if (fileName.isFpsDependent() && Gdx.graphics.getFramesPerSecond() < 56) {
//                return -1;
//            }
            try {
                Sound sound = prepareSound(fileName);
                if (randomPitch && GameSettings.SOUND_RANDOM_PITCH) {
                    return playRandomPitch(loop, sound, volume);
                }
                if (loop) {
                    return sound.loop(volume);
                } else {
                    return sound.play(volume);
                }
            } catch (GdxRuntimeException e) {
                e.printStackTrace();
            }
        }
        return -1;
    }

    private long playRandomPitch(boolean loop, Sound sound, float volume) {
        float randomPitch = randomizer.nextFloat() + 0.8f;
        if (randomPitch < 0.7 || randomPitch > 1.3) {
            randomPitch = 1;
        }
        long id;
        if (loop) {
            id = sound.loop(volume, randomPitch, 0);
        } else {
            id = sound.play(volume, randomPitch, 0);
        }
        return id;
    }

    private Sound prepareSound(SFX soundFile) throws GdxRuntimeException {
        try {
            String soundPath = SFX.fileAttribute.SFX_FOLDER_NAME.getValue() + "/" + SFX.fileAttribute.SOUNDS_FOLDER_NAME.getValue() + "/" + soundFile.getParentDir().getValue() + "/" + soundFile.getValue() + "." + soundFile.getFormat().getValue();
            Sound sound = sounds.get(soundFile.getValue());
            if (sound == null) {
                sound = Parastrike.getAssetsManager().get(soundPath, Sound.class);
                if (sound == null) {
                    throw new GdxRuntimeException("preparing sound failed - sound not found.");
                }
                sounds.put(soundFile.getValue(), sound);
            }
            return sound;
        } catch (GdxRuntimeException e) {
            throw e;
        }
    }

    public void playMusic(SFX.Music musicFile) {
        music = Parastrike.getAssetsManager().get(SFX.fileAttribute.SFX_FOLDER_NAME.getValue() + "/" + musicFile.getParentDir().getValue() + "/" + musicFile.getValue() + "." + musicFile.getFormat().getValue());
        if (GameSettings.SOUND_TOGGLE && GameSettings.PLAY_MUSIC) {
            music.setVolume(Rules.System.SFX.MUSIC_VOLUME);
            music.play();
            music.setLooping(true);
        }
    }

    public void playCurrentMusic() {
        if (GameSettings.SOUND_TOGGLE && GameSettings.PLAY_MUSIC && music != null) {
            music.setVolume(Rules.System.SFX.MUSIC_VOLUME);
            music.play();
            music.setLooping(true);
        }
    }

    public void stopMusic() {
        if (music != null) {
            music.stop();
        }
    }

    public HashMap<String, Sound> getSounds() {
        return sounds;
    }

    public void stopAllSounds() {
        Collection<Sound> soundObjects = sounds.values();
        for (Sound sound : soundObjects) {
            sound.stop();
            sound.dispose();
        }
    }

    public void dispose() {
        sounds.clear();
    }

    public void stopSound(SFX fileName, long soundId) {
        Sound sound = sounds.get(fileName.getValue());
        if (sound != null) {
            if (soundId >= 0) {
                sound.stop(soundId);
            } else {
                sound.stop();
            }
        }
    }

    public void toggleSound(boolean toggle) {
        GameSettings.SOUND_TOGGLE = toggle;
        Gdx.app.getPreferences(Assets.Configs.Preferences.Settings.PREF_SETTINGS).putBoolean(Assets.Configs.Preferences.Settings.SOUND, toggle).flush();
        if (!toggle) {
            stopAllSounds();
            stopMusic();
        } else {
            playCurrentMusic();
        }
    }


    public void pauseAllSounds() {
        if (GameSettings.SOUND_TOGGLE) {
            Collection<Sound> soundObjects = sounds.values();
            for (Sound sound : soundObjects) {
                sound.pause();
            }
        }
    }

    public void resumeAllSounds() {
        if (GameSettings.SOUND_TOGGLE) {
            Collection<Sound> soundObjects = sounds.values();
            for (Sound sound : soundObjects) {
                sound.resume();
            }
        }
    }

    public boolean isMusicPlaying() {
        return music != null && music.isPlaying();
    }

    public void setVolume(SFX fileName, long soundId, float volume) {
        if (GameSettings.SOUND_TOGGLE) {
            Sound sound = sounds.get(fileName.getValue());
            if (sound != null) {
                sound.setVolume(soundId, volume);
            }
        }
    }

}
