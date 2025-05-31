package poiupv;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Navigation;
import model.User;
import model.NavDAOException;
import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;
import javafx.event.ActionEvent;

public class RegisterController {

    @FXML private TextField nicknameField;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker birthdatePicker;
    @FXML private Button chooseAvatarBtn;
    @FXML private ImageView avatarPreview;
    @FXML private Label errorLabel;
    @FXML private Button registerBtn;

    private String avatarPath;

    @FXML
    private void initialize() {
        birthdatePicker.setValue(LocalDate.now().minusYears(18));
        birthdatePicker.setDayCellFactory(picker -> new DateCell() {
            @Override
            public void updateItem(LocalDate date, boolean empty) {
                super.updateItem(date, empty);
                LocalDate minDate = LocalDate.now().minusYears(100);
                LocalDate maxDate = LocalDate.now().minusYears(16);
                setDisable(empty || date.isBefore(minDate) || date.isAfter(maxDate));
            }
        });
    }

    @FXML
    private void registerUser() {
        String nickname = nicknameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText();
        LocalDate birthdate = birthdatePicker.getValue();

        if (!validateFields(nickname, email, password, birthdate)) {
            return;
        }

        try {
            Navigation navigation = Navigation.getInstance();

            if (navigation.authenticate(nickname, password) != null) {
                errorLabel.setText("El nombre de usuario ya existe");
                return;
            }

            Image avatar = avatarPath != null ? new Image("file:" + avatarPath) : null;
            User newUser = navigation.registerUser(nickname, email, password, avatar, birthdate);

            if (newUser != null) {
                errorLabel.setText("Usuario creado correctamente");

                // Cargar login.fxml
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();

                Stage stage = (Stage) registerBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
                stage.show();

            } else {
                errorLabel.setText("Error al registrar el usuario");
            }

        } catch (NavDAOException e) {
            errorLabel.setText("Error de base de datos: " + e.getMessage());
        } catch (IOException e) {
            errorLabel.setText("Error al volver al login: " + e.getMessage());
        } catch (Exception e) {
            errorLabel.setText("Error inesperado: " + e.getMessage());
        }
    }

    private boolean validateFields(String nickname, String email, String password, LocalDate birthdate) {
        if (nickname == null || nickname.length() < 6 || nickname.length() > 15 || nickname.contains(" ")) {
            errorLabel.setText("Nickname debe tener 6-15 caracteres sin espacios");
            return false;
        }

        if (email == null || !User.checkEmail(email)) {
            errorLabel.setText("Ingrese un email válido");
            return false;
        }

        if (password == null || !User.checkPassword(password)) {
            errorLabel.setText("La contraseña debe tener 8-20 caracteres, incluyendo mayúsculas, minúsculas, números y caracteres especiales");
            return false;
        }

        if (birthdate == null || Period.between(birthdate, LocalDate.now()).getYears() < 16) {
            errorLabel.setText("Debes tener al menos 16 años para registrarte");
            return false;
        }

        errorLabel.setText("");
        return true;
    }

    @FXML
    private void chooseAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Avatar");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );

        File selectedFile = fileChooser.showOpenDialog(avatarPreview.getScene().getWindow());
        if (selectedFile != null) {
            try {
                avatarPath = selectedFile.getAbsolutePath();
                avatarPreview.setImage(new Image("file:" + avatarPath));
            } catch (Exception e) {
                errorLabel.setText("Error al cargar la imagen");
            }
        }
    }
}