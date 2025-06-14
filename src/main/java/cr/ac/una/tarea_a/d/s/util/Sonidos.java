package cr.ac.una.Tarea_A.D.S.util;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sonidos {

    private static MediaPlayer player;

    private static void playSound(String filename) {
        detener();
        Media sound = new Media(Sonidos.class.getResource("/cr/ac/una/tarea_a/d/s/resources/sonidos/" + filename).toExternalForm());
        player = new MediaPlayer(sound);
        player.play();
    }

    public static void detener() {
        if (player != null) {
            player.stop();
        }
    }

    public static void ganador() {
        playSound("ganador.mp3");
    }

    public static void aplausos() {
        playSound("aplausos.mp3");
    }

    public static void acercaDe() {
        playSound("acercaDe.mp3");
    }

    public static void silbato() {
        playSound("silbato.mp3");
    }

    public static void musicaDesempate() {
        playSound("minijuegoSound.mp3");
    }

    public static void click() {
        playSound("click.mp3");
    }

}
