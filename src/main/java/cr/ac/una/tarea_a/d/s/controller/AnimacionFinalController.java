package cr.ac.una.tarea_a.d.s.controller;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Duration;

import java.io.ByteArrayInputStream;
import java.util.Base64;
import javafx.animation.Animation;
import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.ParallelTransition;
import javafx.animation.RotateTransition;
import javafx.animation.ScaleTransition;
import javafx.animation.TranslateTransition;

public class AnimacionFinalController {

    @FXML
    private Label lblConfeti;
    @FXML
    private ImageView imgEquipo;

    @FXML
    private ImageView imgBalon;

    @FXML
    private Label lblCampeon;

    public void mostrarAnimacion(String base64Escudo, String base64Balon, String nombreEquipo) {
        Image imagenEscudo = convertirBase64AImage(base64Escudo);
        Image imagenBalon = convertirBase64AImage(base64Balon);

        if (imagenEscudo != null) {
            imgEquipo.setImage(imagenEscudo);
        }
        if (imagenBalon != null) {
            imgBalon.setImage(imagenBalon);
        }

        lblCampeon.setText("¡" + nombreEquipo + " CAMPEÓN!");

        animarEquipo();
        animarBalon();
        animarTexto();
        animarConfeti();
    }

//    private void animarEscudo() {
//        TranslateTransition slideUp = new TranslateTransition(Duration.seconds(1.5), imgEquipo);
//        slideUp.setFromY(200);
//        slideUp.setToY(0);
//        slideUp.setInterpolator(Interpolator.EASE_OUT);
//        slideUp.play();
//    }
    private void animarEquipo() {
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

        ParallelTransition entradaEscudo = new ParallelTransition(slideUp, rebote);
        entradaEscudo.play();
    }

    private void animarBalon() {
        RotateTransition rotar = new RotateTransition(Duration.seconds(2), imgBalon);
        rotar.setByAngle(360);
        rotar.setCycleCount(Animation.INDEFINITE);
        rotar.setInterpolator(Interpolator.LINEAR);
        rotar.play();
    }

//    private void animarTexto() {
//        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), lblCampeon);
//        fadeIn.setFromValue(0);
//        fadeIn.setToValue(1);
//        fadeIn.play();
//    }
    private void animarTexto() {
        // Fade in
        FadeTransition fadeIn = new FadeTransition(Duration.seconds(2), lblCampeon);
        fadeIn.setFromValue(0);
        fadeIn.setToValue(1);

        // Zoom con escala
        ScaleTransition zoom = new ScaleTransition(Duration.seconds(1.5), lblCampeon);
        zoom.setFromX(0.5);
        zoom.setFromY(0.5);
        zoom.setToX(1);
        zoom.setToY(1);
        zoom.setInterpolator(Interpolator.EASE_OUT);

        ParallelTransition animacionTexto = new ParallelTransition(fadeIn, zoom);
        animacionTexto.play();
    }

    private void animarConfeti() {
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

        ParallelTransition anim = new ParallelTransition(caer, girar);
        anim.play();
    }

    private Image convertirBase64AImage(String base64) {
        try {
            if (base64 == null || base64.isEmpty()) {
                return null;
            }

            if (base64.contains(",")) {
                base64 = base64.split(",")[1]; // por si tiene encabezado MIME
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