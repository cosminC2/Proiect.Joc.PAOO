package PaooGame.Misc.Sound;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[32];
    Boolean isPlaying = false;
    Integer index;
    public Sound() {
        soundURL[0] = getClass().getResource("/Audio/PreciousThings.wav");
        soundURL[1] = getClass().getResource("/Audio/WinningRoad.wav");
        soundURL[2] = getClass().getResource("/Audio/Attack Hit 1.wav");
        soundURL[3] = getClass().getResource("/Audio/Death.wav");
        soundURL[4] = getClass().getResource("/Audio/Final Hit.wav");
        soundURL[5] = getClass().getResource("/Audio/Next Turn.wav");
        soundURL[6] = getClass().getResource("/Audio/No Damage.wav");
        soundURL[7] = getClass().getResource("/Audio/Eclipse3.wav");
        soundURL[8] = getClass().getResource("/Audio/Select 1.wav");
        soundURL[9] = getClass().getResource("/Audio/Game Over.wav");
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            index = i;
        }catch(Exception e){
        }

    }
    public void play(){
        clip.start();
        isPlaying = true;
    }
    public void loop(){
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }
    public void stop(){
        clip.stop();
        index = null;
        isPlaying = false;
    }

    public Boolean getPlaying() {
        return isPlaying;
    }

    public Integer getIndex() {
        return index;
    }
}
