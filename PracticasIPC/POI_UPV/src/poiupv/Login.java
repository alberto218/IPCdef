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
import javafx.event.ActionEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import model.NavDAOException;

/**
 *
 * @author Diego Molina
 */
public class Login {

    @FXML
    private BorderPane borderpane;
    @FXML
    private Label labelRegistro;
    @FXML
    private GridPane gridpane2;
    @FXML
    private TextField nickText;
    @FXML
    private PasswordField contraText;
    @FXML
    private Button botonLogin;
    @FXML
    private Label labelNick;
    @FXML
    private Label labelEstado;
    @FXML
    private Label titulo;

    @FXML
    private Label nicknameErrorLabel;
    @FXML
    private Label passwordErrorLabel;
    
    public void initialize() {
        clearErrors();

        labelRegistro.setOnMouseClicked(event -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Register.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) labelRegistro.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (IOException e) {
                e.printStackTrace();
                labelEstado.setText("Error al cargar la ventana de registro.");;
            }
        });
    }

    private void handleLogin() throws NavDAOException {
        clearErrors();
        String nick = nickText.getText().trim();
        String password = contraText.getText().trim();

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
        passwordErrorLabel.setText("");}

    @FXML
    private void handleLabelSalida(MouseEvent event) {
    }

    @FXML
    private void handleLabelRegistro(MouseEvent event) {
    }

    @FXML
    private void registrarse(MouseEvent event) {
    }

    @FXML
    private void iniciarSesion(ActionEvent event) {
    }

}