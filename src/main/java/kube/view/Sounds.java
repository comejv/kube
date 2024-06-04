package kube.view;

import java.io.IOException;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;

import kube.configuration.Config;
import kube.configuration.ResourceLoader;

public class Sounds {
    public static void playSound(String s) {
        if (Config.isSoundMute()) {
            return;
        }
        Clip clip;
        try {
            Config.debug("Played ", s);
            AudioInputStream audioStream = AudioSystem
                    .getAudioInputStream(ResourceLoader.getResourceAsStream("sounds/" + s + ".wav"));
            clip = AudioSystem.getClip();
            clip.open(audioStream);
            clip.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            Config.error("Unable to load music file.");
        } catch (IllegalArgumentException e) {
            Config.error("Sound effect " + s + " uses an unsupported file encoding. Try 24KHz wave.");
        } catch (NullPointerException e) {
            Config.error("Sound effect " + s + " not found. Disabling music.");
        }
    }
}
