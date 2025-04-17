package cr.ac.una.tarea_a.d.s.controller;

import cr.ac.una.Tarea_A.D.S.util.Sonidos;
import cr.ac.una.tarea_a.d.s.util.Animaciones;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
        Image imagenEscudo = Animaciones.convertirBase64AImage(base64Escudo);
        Image imagenBalon = Animaciones.convertirBase64AImage(base64Balon);

        if (imagenEscudo != null) imgEquipo.setImage(imagenEscudo);
        if (imagenBalon != null) imgBalon.setImage(imagenBalon);

        lblCampeon.setText("¡" + nombreEquipo + " CAMPEÓN!");
        Sonidos.aplausos();

        Animaciones.animarEquipo(imgEquipo);
        Animaciones.animarBalon(imgBalon);
        Animaciones.animarTexto(lblCampeon);
        Animaciones.animarConfeti(lblConfeti);
    }
}
