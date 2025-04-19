package cr.ac.una.tarea_a.d.s.util;

import javafx.animation.*;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import javafx.scene.Node;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.effect.DropShadow;
import javafx.scene.effect.Glow;
import javafx.scene.paint.Color;

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

    public static void animarBalonGol(ImageView balon) {
        ScaleTransition rebote = new ScaleTransition(Duration.seconds(0.3), balon);
        rebote.setFromX(1.0);
        rebote.setFromY(1.0);
        rebote.setToX(1.3);
        rebote.setToY(1.3);
        rebote.setAutoReverse(true);
        rebote.setCycleCount(2);
        rebote.setInterpolator(Interpolator.EASE_OUT);
        rebote.play();
    }

    public static void mostrarGolAnimado(Label lblGol) {
        lblGol.setVisible(true);
        lblGol.setScaleX(0.5);
        lblGol.setScaleY(0.5);
        lblGol.setOpacity(0);

        ScaleTransition zoom = new ScaleTransition(Duration.seconds(0.6), lblGol);
        zoom.setFromX(0.5);
        zoom.setFromY(0.5);
        zoom.setToX(1.2);
        zoom.setToY(1.2);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.6), lblGol);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pausa = new PauseTransition(Duration.seconds(1.5));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.8), lblGol);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);

        fadeOut.setOnFinished(e -> lblGol.setVisible(false));

        SequentialTransition secuencia = new SequentialTransition(
                new ParallelTransition(zoom, fadeIn),
                pausa,
                fadeOut
        );
        secuencia.play();
    }

    public static void parpadeoGolDeOro(Label label) {
        Timeline timeline = new Timeline(
                new KeyFrame(Duration.seconds(0.5), e -> label.setStyle("-fx-text-fill: #FFD700; -fx-font-weight: bold;")),
                new KeyFrame(Duration.seconds(1), e -> label.setStyle("-fx-text-fill: #FFF9E3; -fx-font-weight: bold;"))
        );
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    public static void mostrarGolDeOro(Label lblGolDeOro) {
        lblGolDeOro.setVisible(true);
        lblGolDeOro.setScaleX(0.5);
        lblGolDeOro.setScaleY(0.5);
        lblGolDeOro.setOpacity(0);

        ScaleTransition zoom = new ScaleTransition(Duration.seconds(0.6), lblGolDeOro);
        zoom.setFromX(0.5);
        zoom.setFromY(0.5);
        zoom.setToX(1.4);
        zoom.setToY(1.4);

        FadeTransition fadeIn = new FadeTransition(Duration.seconds(0.6), lblGolDeOro);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        PauseTransition pausa = new PauseTransition(Duration.seconds(1.8));

        FadeTransition fadeOut = new FadeTransition(Duration.seconds(0.8), lblGolDeOro);
        fadeOut.setFromValue(1);
        fadeOut.setToValue(0);
        fadeOut.setOnFinished(e -> lblGolDeOro.setVisible(false));

        new SequentialTransition(new ParallelTransition(zoom, fadeIn), pausa, fadeOut).play();
    }

    public static void animarBalonDorado(ImageView balon) {
        DropShadow doradoBrillo = new DropShadow();
        doradoBrillo.setColor(Color.web("#FFD700"));
        doradoBrillo.setRadius(20);
        doradoBrillo.setSpread(0.6);
        balon.setEffect(doradoBrillo);

        Timeline brillo = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(doradoBrillo.radiusProperty(), 20)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(doradoBrillo.radiusProperty(), 30)),
                new KeyFrame(Duration.seconds(1), new KeyValue(doradoBrillo.radiusProperty(), 20))
        );
        brillo.setCycleCount(Animation.INDEFINITE);
        brillo.setAutoReverse(true);
        brillo.play();
    }
    public static void animarCanchaDorada(ImageView cancha) {
        DropShadow resplandorDorado = new DropShadow();
        resplandorDorado.setColor(Color.web("#FFD700")); // Dorado puro
        resplandorDorado.setRadius(40);
        resplandorDorado.setSpread(0.6);
        resplandorDorado.setOffsetX(0);
        resplandorDorado.setOffsetY(0);

        Timeline pulso = new Timeline(
                new KeyFrame(Duration.seconds(0), new KeyValue(resplandorDorado.radiusProperty(), 30)),
                new KeyFrame(Duration.seconds(0.5), new KeyValue(resplandorDorado.radiusProperty(), 50)),
                new KeyFrame(Duration.seconds(1), new KeyValue(resplandorDorado.radiusProperty(), 30))
        );
        pulso.setCycleCount(Animation.INDEFINITE);
        pulso.setAutoReverse(true);
        pulso.play();

        cancha.setEffect(resplandorDorado);
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
