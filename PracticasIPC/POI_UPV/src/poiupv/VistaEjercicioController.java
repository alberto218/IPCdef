/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
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
}
