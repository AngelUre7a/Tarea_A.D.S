package cr.ac.una.Tarea_A.D.S.util;

import javafx.scene.control.Button;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

public class Sonidos {

    private static MediaPlayer player;

    private static void playSound(String filename) {
        detener(); // Detenemos cualquier sonido anterior
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

    public static void golDeOroTension() {
        playSound("tension.mp3");
    }
//    
//    // 🔊 Método general para asignar sonido al hacer hover en botones
//    public static void agregarHover(Button... botones) {
//        for (Button boton : botones) {
//            boton.setOnMouseEntered(e -> {
//                if (player == null || player.getStatus() != MediaPlayer.Status.PLAYING) {
//                    silbato(); // Cambiá por otro si querés otro sonido
//                }
//            });
//        }
//    }
}
