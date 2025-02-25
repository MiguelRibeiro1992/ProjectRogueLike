package pt.upskill.projeto1.game;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {

    Clip clip;

    URL[] soundURL = new URL[30];

    public Sound(){

        soundURL [0] = getClass().getResource("/sounds/Theme song.wav");
        soundURL [1] = getClass().getResource("/sounds/Game Over.wav");
        soundURL [2] = getClass().getResource("/sounds/Health regeneration.wav");
        soundURL [3] = getClass().getResource("/sounds/Hit sound.wav");
        soundURL [4] = getClass().getResource("/sounds/Key.wav");
        soundURL [5] = getClass().getResource("/sounds/Level completion.wav");
        soundURL [6] = getClass().getResource("/sounds/Start Game.wav");
        soundURL [7] = getClass().getResource("/sounds/Sword hit.wav");
        soundURL [8] = getClass().getResource("/sounds/Explosion+7.wav");
        soundURL [9] = getClass().getResource("/sounds/bear-trap-103800.wav");
        soundURL [10] = getClass().getResource("/sounds/male_hurt7-48124.wav");
    }

    public void setFile(int i){
        try {
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
        } catch (Exception e) {
        }

    }

    public void play(){

        clip.start();
    }

    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void stop(){

        clip.stop();

    }
}
