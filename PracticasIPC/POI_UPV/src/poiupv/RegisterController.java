/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;

import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import model.Navigation;
import model.User;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;
import model.NavDAOException;
/**
 *
 * @author Diego Molina
 */
public class RegisterController {
    
    @FXML
    private TextField nicknameField;

    @FXML
    private TextField emailField;

    @FXML
    private PasswordField passwordField;

    @FXML
    private DatePicker birthdatePicker;

    @FXML
    private Button chooseAvatarBtn;

    @FXML
    private ImageView avatarPreview;

    @FXML
    private Label errorLabel;

    @FXML
    private Button registerBtn;

    private Image selectedAvatar = null;

    public void initialize() {
        errorLabel.setText("");
    }

    @FXML
    private void chooseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar avatar");
        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg"));
        File file = fileChooser.showOpenDialog(chooseAvatarBtn.getScene().getWindow());

        if (file != null) {
            try {
                selectedAvatar = new Image(new FileInputStream(file));
                avatarPreview.setImage(selectedAvatar);
            } catch (FileNotFoundException e) {
                errorLabel.setText("Error al cargar la imagen.");
            }
        }
    }

    @FXML
    private void registerUser() throws NavDAOException {
        String nick = nicknameField.getText().trim();
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        LocalDate birthdate = birthdatePicker.getValue();

        Navigation nav = Navigation.getInstance();

        // Validaciones
        if (!User.checkNickName(nick)) {
            errorLabel.setText("El nombre de usuario no es válido.");
            return;
        }

        if (nav.exitsNickName(nick)) {
            errorLabel.setText("El nombre de usuario ya existe.");
            return;
        }

        if (!User.checkEmail(email)) {
            errorLabel.setText("Correo no válido.");
            return;
        }

        if (!User.checkPassword(password)) {
            errorLabel.setText("Contraseña insegura.");
            return;
        }

        if (birthdate == null || birthdate.isAfter(LocalDate.now().minusYears(16))) {
            errorLabel.setText("Debes tener al menos 16 años.");
            return;
        }

        // Registro
        try {
            User u = nav.registerUser(nick, email, password, selectedAvatar, birthdate);
            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Usuario registrado correctamente.");
            clearFields();
        } catch (Exception e) {
            errorLabel.setText("Error al registrar usuario.");
            e.printStackTrace();
        }
    }

    private void clearFields() {
        nicknameField.clear();
        emailField.clear();
        passwordField.clear();
        birthdatePicker.setValue(null);
        avatarPreview.setImage(null);
        selectedAvatar = null;
    }
}
