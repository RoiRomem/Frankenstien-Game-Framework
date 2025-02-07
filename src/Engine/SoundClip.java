package Engine;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class SoundClip {
    private Clip clip;
    private boolean loop;

    public SoundClip(String clipPath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File(clipPath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
    }

    public SoundClip(String clipPath, boolean loop) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        this.loop = loop;
        File soundFile = new File(clipPath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        this.clip = AudioSystem.getClip();
        this.clip.open(audioStream);
    }

    public void play() {
        clip.setFramePosition(0);
        if (loop) clip.loop(Clip.LOOP_CONTINUOUSLY);
        clip.start();
    }

    public void play(int times) {
        clip.setFramePosition(0);
        clip.loop(times);
        clip.start();
    }

    public void stop() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            clip.close();
        }
    }

    public void ChangeClip(String clipPath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        File soundFile = new File(clipPath);
        AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
        clip = AudioSystem.getClip();
        clip.open(audioStream);
    }
}
