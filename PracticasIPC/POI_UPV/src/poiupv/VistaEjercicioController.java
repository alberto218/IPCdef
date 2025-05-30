package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Stage;

/**
 * FXML Controller class
 *
 * @author alber
 */
public class VistaEjercicioController implements Initializable {

    @FXML
    private Label enunciado;
    @FXML
    private RadioButton respuesta1;
    @FXML
    private RadioButton respuesta2;
    @FXML
    private RadioButton respuesta3;
    @FXML
    private RadioButton respuesta4;
    @FXML
    private Button butnVolver;
    @FXML
    private Button btnMapa;
    @FXML
    private Button btnCorregir;
    
    private ToggleGroup grupoRespuestas;
    private ejercicios ejercicioActual;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    public void setEjercicio(ejercicios ej){
        respuesta1.setText(ej.respuestas.get(0));
        respuesta2.setText(ej.respuestas.get(1));
        respuesta3.setText(ej.respuestas.get(2));
        respuesta4.setText(ej.respuestas.get(3));
        enunciado.setText(ej.getTexto());
    
        grupoRespuestas = new ToggleGroup();
        respuesta1.setToggleGroup(grupoRespuestas);
        respuesta2.setToggleGroup(grupoRespuestas);
        respuesta3.setToggleGroup(grupoRespuestas);
        respuesta4.setToggleGroup(grupoRespuestas);
    }
    
    @FXML
    private void abrirMapa() {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/poiupv/mapa.fxml")); // Ruta relativa al archivo FXML
        Parent root = loader.load();
        Stage stage = new Stage();
        stage.setTitle("Carta Náutica");
        stage.setScene(new Scene(root));
        stage.show();
        } catch (IOException e) {
        e.printStackTrace();
        }
    }
    
    @FXML
    private void corregirResponseta() {
        RadioButton seleccionada = (RadioButton) grupoRespuestas.getSelectedToggle();

        if (seleccionada == null) {
        System.out.println("Por favor selecciona una respuesta.");
        return;
        }

        int indexSeleccionado = -1;
        if (seleccionada == respuesta1) indexSeleccionado = 0;
        else if (seleccionada == respuesta2) indexSeleccionado = 1;
        else if (seleccionada == respuesta3) indexSeleccionado = 2;
        else if (seleccionada == respuesta4) indexSeleccionado = 3;

    // Cambio aquí: usar ejercicioActual en lugar de la clase ejercicios
        int indexCorrecto = ejercicioActual.getRespuestaCorrecta();

        if (indexSeleccionado == indexCorrecto) {
        seleccionada.setStyle("-fx-background-color: #90ee90"); // Verde claro para correcto
        System.out.println("Respuesta correcta");
        } else {
        seleccionada.setStyle("-fx-background-color: #ffcccb"); // Rojo claro para incorrecto
        RadioButton correcta = switch (indexCorrecto) {
            case 0 -> respuesta1;
            case 1 -> respuesta2;
            case 2 -> respuesta3;
            case 3 -> respuesta4;
            default -> null;
        };

        if (correcta != null) {
            correcta.setStyle("-fx-background-color: #90ee90"); // Resalta la correcta
        }
        System.out.println("Respuesta incorrecta");
        }

        respuesta1.setDisable(true);
        respuesta2.setDisable(true);
        respuesta3.setDisable(true);
        respuesta4.setDisable(true);
    }

}
