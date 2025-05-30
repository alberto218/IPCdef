package poiupv;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Controlador para la pantalla de inicio de sesión
 */
public class LoginController implements Initializable {

    @FXML
    private TextField nickText;      // Campo de texto para el nombre de usuario
    @FXML
    private TextField contraText;    // Campo de texto para la contraseña
    @FXML
    private Button botonLogin;       // Botón para iniciar sesión
    @FXML
    private Label labelRegistro;     // Etiqueta para ir al registro

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        labelRegistro.setOnMouseClicked(event -> {
    try {
        Parent root = FXMLLoader.load(getClass().getResource("Registrarse.fxml"));
        Stage stage = (Stage) labelRegistro.getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.show();
    } catch (Exception e) {
        e.printStackTrace();
    }
});
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        String usuario = nickText.getText();
        String contrasena = contraText.getText();

        // Validación básica
        if (usuario.isEmpty() || contrasena.isEmpty()) {
            mostrarAlerta("Error", "Por favor ingrese nombre de usuario y contraseña");
            return;
        }

        // Aquí normalmente validarías contra una base de datos
        if (autenticar(usuario, contrasena)) {
            try {
                // Cargar la pantalla principal después del login exitoso
                Parent root = FXMLLoader.load(getClass().getResource("PantallaPrincipal.fxml"));
                Scene scene = new Scene(root);
                Stage stage = (Stage) botonLogin.getScene().getWindow();
                stage.setScene(scene);
                stage.show();
            } catch (Exception e) {
                mostrarAlerta("Error", "No se pudo cargar la pantalla principal");
                e.printStackTrace();
            }
        } else {
            mostrarAlerta("Error de autenticación", "Usuario o contraseña incorrectos");
        }
    }

    // Método que simula la autenticación (debes implementar la lógica real)
    private boolean autenticar(String usuario, String contrasena) {
        // Ejemplo básico - reemplaza con tu lógica de autenticación real
        return !usuario.isEmpty() && !contrasena.isEmpty();
    }

    private void mostrarAlerta(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }
}