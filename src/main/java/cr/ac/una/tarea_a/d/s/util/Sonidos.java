package cr.ac.una.Tarea_A.D.S.util;

import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sonidos {

    private static void playSound(String filename) {
        Media sound = new Media(Sonidos.class.getResource("/cr/ac/una/tarea_a/d/s/resources/sonidos/" + filename).toExternalForm());
        MediaPlayer player = new MediaPlayer(sound);
        player.play();
    }
    
    public static void aplausos(){
        playSound("aplausos.mp3");
    }
}