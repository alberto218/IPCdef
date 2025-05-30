package poiupv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Navigation;
import model.User;
import model.NavDAOException;
import java.io.IOException;

public class LoginController {

    @FXML private TextField nickText;
    @FXML private TextField contraText;
    @FXML private Button botonLogin;
    @FXML private Label labelRegistro;

    @FXML
    private void initialize() {
        // Configurar evento para el label de registro
        labelRegistro.setOnMouseClicked(event -> {
            try {
                Parent root = FXMLLoader.load(getClass().getResource("Registrarse.fxml"));
                Stage stage = (Stage) labelRegistro.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();
            } catch (IOException e) {
                showAlert("Error", "No se pudo cargar la pantalla de registro");
            }
        });
    }

    @FXML
    private void iniciarSesion() {
        String nickname = nickText.getText();
        String password = contraText.getText();

        if (nickname.isEmpty() || password.isEmpty()) {
            showAlert("Error", "Por favor ingrese nombre de usuario y contraseña");
            return;
        }

        try {
            Navigation navigation = Navigation.getInstance();
            
            // Usuario por defecto para pruebas (según documentación)
            if (nickname.equals("user1") && password.equals("User123!")) {
                openMainScreen();
                return;
            }

            User user = navigation.authenticate(nickname, password);
            if (user == null) {
                showAlert("Error", "Nombre de usuario o contraseña incorrectos");
                return;
            }

            openMainScreen();
            
        } catch (NavDAOException e) {
            showAlert("Error de base de datos", "No se pudo acceder a los datos de usuario: " + e.getMessage());
        }
    }

    private void openMainScreen() {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("PantallaPrincipal.fxml"));
            Stage stage = (Stage) botonLogin.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            showAlert("Error", "No se pudo cargar la pantalla principal");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}