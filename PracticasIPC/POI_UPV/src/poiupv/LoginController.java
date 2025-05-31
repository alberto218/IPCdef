package poiupv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Navigation;
import model.User;

import java.io.IOException;
import model.NavDAOException;

/**
 *
 * @author Diego Molina
 */
public class LoginController {

    @FXML
    private Label titulo;   
    @FXML
    private Label registerLabel;
    @FXML
    private TextField nicknameField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Button loginBtn;
    @FXML
    private Label nicknameErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    
    

    @FXML
    public void initialize() {
        clearErrors();

        registerLabel.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) registerLabel.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                nicknameErrorLabel.setText("Error al cargar la ventana de registro.");
            }
        });
    }

    @FXML
    private void handleLogin() throws NavDAOException {
        clearErrors();
        String nick = nicknameField.getText().trim();
        String password = passwordField.getText().trim();

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
            // Éxito: avanzar a la siguiente ventana
            System.out.println("Login correcto");
            // cargarMenuPrincipal(user);
        }
    }

    private void clearErrors() {
        nicknameErrorLabel.setText("");
        passwordErrorLabel.setText("");
    }

}
