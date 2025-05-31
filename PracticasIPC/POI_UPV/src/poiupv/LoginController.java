package poiupv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Navigation;
import model.User;
import javafx.event.ActionEvent;
import java.io.IOException;
import model.NavDAOException;
/**
 *
 * @author Diego Molina
 */
public class LoginController {
 
   @FXML
    private Label registerLabel;
    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label nicknameErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    @FXML
    private Button botonLogin;

    public void initialize() {
        clearErrors();

        registerLabel.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Registrarse.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) registerLabel.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                passwordErrorLabel.setText("Error al cargar la ventana de registro.");
            }
        });
    }

    private void clearErrors() {
        nicknameErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
        clearErrors();
        String nick = nicknameField.getText().trim();
        String password = passwordField.getText().trim();

        try {
            Navigation nav = Navigation.getInstance();

            if (nick.isEmpty()) {
                nicknameErrorLabel.setText("El nombre de usuario es obligatorio.");
                return;
            }

            if (!nav.exitsNickName(nick)) {
                nicknameErrorLabel.setText("El nombre de usuario no existe.");
                return;
            }

            User user = nav.authenticate(nick, password);
            if (user == null) {
                passwordErrorLabel.setText("Contraseña incorrecta.");
            } else {
                System.out.println("Login correcto");
                // Aquí puedes redirigir a la vista principal
                // por ejemplo: cargarMenuPrincipal(user);
            }

        } catch (NavDAOException e) {
            e.printStackTrace();
            nicknameErrorLabel.setText("Error de conexión.");
        }
    }
    
}
