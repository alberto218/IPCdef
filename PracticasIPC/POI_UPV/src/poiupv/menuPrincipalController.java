/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import javafx.scene.control.Label;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;

/**
 *
 * @author Diego Molina
 */

public class menuPrincipalController {

    @FXML
    private ImageView avatarImageView;
    @FXML
    private Label bienvenidaLabel;
    @FXML
    private Button ejerciciosBtn;
    @FXML
    private Button estadisticasBtn;
    @FXML
    private Button modificarPerfilBtn;
    @FXML
    private Button cerrarSesionBtn;
    
    private User usuario;
    
     public void setUsuario(User usuario) {
        this.usuario = usuario;

        // Mostrar nombre en etiqueta
        bienvenidaLabel.setText("¡Bienvenid@, " + usuario.getNickName() + "!");

        // Mostrar imagen de perfil si existe
        if (usuario.getAvatar() != null) {
            avatarImageView.setImage(usuario.getAvatar());
        }
    }
    

    @FXML
    private void handleEjerciciosBtn() {
        cargarPantalla("Lista.fxml", "Lista de Ejercicios");
    }
    @FXML
    private void handleModificarPerfilBtn() {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("modificarPerfil.fxml"));
        Parent root = loader.load();

        // PASO CLAVE: Pasar el usuario al controlador siguiente
        modificarPerfilController controller = loader.getController();
        controller.setUsuario(usuario);

        Stage stage = new Stage();
        stage.setTitle("Modificar Perfil");
        stage.setScene(new Scene(root));
        stage.show();

        // Cerrar esta ventana si quieres
        Stage currentStage = (Stage) modificarPerfilBtn.getScene().getWindow();
        currentStage.close();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }

    @FXML
    private void handleCerrarSessionBtn() {
        cargarPantalla("Login.fxml", "Inicio de Sesión");
    }

    private void cargarPantalla(String fxmlFile, String title) {
        try {
            // Cargar la nueva pantalla
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile));
            Stage stage = new Stage();
            stage.setTitle(title);
            stage.setScene(new Scene(root));
            
            // Cerrar la pantalla actual
            Stage currentStage = (Stage) cerrarSesionBtn.getScene().getWindow();
            currentStage.close();
            
            // Mostrar la nueva pantalla
            stage.show();
        }catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error al cargar el archivo FXML: " + fxmlFile);
        }
    }
}    

