package kube.view;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import java.io.IOException;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;

public class Music {

    private Clip clip;

    public Music(String audioFilePath) {
        String fullPath = "musics/" + audioFilePath + ".wav";
        try {
            AudioInputStream audioStream = AudioSystem
                    .getAudioInputStream(ResourceLoader.getResourceAsStream(fullPath));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            Config.error("Music " + fullPath + " uses an unsupported file encoding. Try 24KHz wave.");
        } catch (NullPointerException e) {
            Config.error("Music " + fullPath + " not found. Disabling music.");
        }
    }

    public void play() {
        if (clip == null) {
            Config.error("Music not loaded, maybe due to missing file.");
            return;
        }
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop() {
        if (clip == null) {
            Config.debug("Music not stopped : music was not playing.");
            return;
        }
        clip.stop();
    }
}
