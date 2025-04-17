package cr.ac.una.tarea_a.d.s.util;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import javafx.scene.Node;

public class Animaciones {

    public static void animarEquipo(ImageView imgEquipo) {
        TranslateTransition slideUp = new TranslateTransition(Duration.seconds(1.5), imgEquipo);
        slideUp.setFromY(400);
        slideUp.setToY(0);
        slideUp.setInterpolator(Interpolator.EASE_OUT);

        ScaleTransition rebote = new ScaleTransition(Duration.seconds(1.5), imgEquipo);
        rebote.setFromX(0.8);
        rebote.setFromY(0.8);
        rebote.setToX(1);
        rebote.setToY(1);
        rebote.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(slideUp, rebote).play();
    }

    public static void animarBalon(ImageView imgBalon) {
        RotateTransition rotar = new RotateTransition(Duration.seconds(2), imgBalon);
        rotar.setByAngle(360);
        rotar.setCycleCount(Animation.INDEFINITE);
        rotar.setInterpolator(Interpolator.LINEAR);
        rotar.play();
    }

    public static void animarTexto(Label lblCampeon) {
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), lblCampeon);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        ScaleTransition zoom = new ScaleTransition(Duration.seconds(1.5), lblCampeon);
        zoom.setFromX(0.5);
        zoom.setFromY(0.5);
        zoom.setToX(1);
        zoom.setToY(1);
        zoom.setInterpolator(Interpolator.EASE_OUT);

        new ParallelTransition(fadeIn, zoom).play();
    }

    public static void animarConfeti(Label lblConfeti) {
        TranslateTransition caer = new TranslateTransition(Duration.seconds(2), lblConfeti);
        caer.setFromY(-100);
        caer.setToY(0);
        caer.setCycleCount(Animation.INDEFINITE);
        caer.setAutoReverse(true);

        RotateTransition girar = new RotateTransition(Duration.seconds(1), lblConfeti);
        girar.setFromAngle(-10);
        girar.setToAngle(10);
        girar.setCycleCount(Animation.INDEFINITE);
        girar.setAutoReverse(true);

        new ParallelTransition(caer, girar).play();
    }

   
    public static void animarCreditos(Node nodo, double fromY, double toY, double duracionSegundos) {
        TranslateTransition subir = new TranslateTransition(Duration.seconds(duracionSegundos), nodo);
        subir.setFromY(fromY);
        subir.setToY(toY);
        subir.setCycleCount(TranslateTransition.INDEFINITE); // o 1 si querés solo una vez
        subir.setAutoReverse(false);
        subir.play();
    }


    public static Image convertirBase64AImage(String base64) {
        try {
            if (base64 == null || base64.isEmpty()) {
                return null;
            }
            if (base64.contains(",")) {
                base64 = base64.split(",")[1];
            }
            byte[] bytes = Base64.getDecoder().decode(base64);
            return new Image(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            System.err.println("❌ Error cargando imagen base64");
            e.printStackTrace();
            return null;
        }
    }
}
