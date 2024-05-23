package PaooGame.Misc.Sound;
import javax.sound.sampled.*;
import java.io.IOException;
import java.net.URL;

public class Sound {
    Clip clip;
    URL soundURL[] = new URL[32];
    Boolean isPlaying = false;
    Integer index = -1;
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
        soundURL[10]= getClass().getResource("/Audio/MainTheme.wav");
        //initializare fisiere de sunet
    }

    public void setFile(int i){
        try{
            AudioInputStream ais = AudioSystem.getAudioInputStream(soundURL[i]);
            clip = AudioSystem.getClip();
            clip.open(ais);
            index = i;
        }catch (LineUnavailableException | IOException | UnsupportedAudioFileException e) {
            throw new RuntimeException(e);
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
        if(clip!=null)clip.stop();
        index = null;
        isPlaying = false;
    }

    public Integer getIndex() {
        return index;
    }
}
