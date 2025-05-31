/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;

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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalDate;



/**
 *
 * @author Diego Molina
 */
/*public class modificarPerfilController {

    @FXML
    private TextField nicknameField;
    @FXML
    private TextField emailField;
    @FXML
    private PasswordField passwordField;
    @FXML
    private DatePicker birthdatePicker;
    @FXML
    private ImageView avatarPreview;
    @FXML
    private Button chooseAvatarBtn;
    @FXML
    private Label errorLabel;
    @FXML
    private Button backBtn;
    @FXML
    private Button updateBtn;
    
    private Image selectedAvatar = null;
    private User currentUser;
    
    @FXML
    public void initialize() {
        currentUser = Navigation.getInstance().getCurrentUser();

        if (currentUser != null) {
            nicknameField.setText(currentUser.getNickName());
            nicknameField.setDisable(true); // no editable
            emailField.setText(currentUser.getEmail());
            passwordField.setText(currentUser.getPassword());
            birthdatePicker.setValue(currentUser.getBirthDate());

            if (currentUser.getAvatar() != null) {
                avatarPreview.setImage(currentUser.getAvatar());
                selectedAvatar = currentUser.getAvatar();
            }
        }

        chooseAvatarBtn.setOnAction(e -> chooseAvatar());

        backBtn.setOnAction(e -> {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
                Parent root = loader.load();
                Stage stage = (Stage) backBtn.getScene().getWindow();
                stage.setScene(new Scene(root));
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
    }

    private void chooseAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar nuevo avatar");
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
    private void updateProfile() {
        String email = emailField.getText().trim();
        String password = passwordField.getText().trim();
        LocalDate birthdate = birthdatePicker.getValue();

        if (!User.checkEmail(email)) {
            errorLabel.setText("Correo electrónico no válido.");
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

        try {
            currentUser.setEmail(email);
            currentUser.setPassword(password);
            currentUser.setBirthDate(birthdate);
            currentUser.setAvatar(selectedAvatar);
            Navigation.getInstance().updateUser(currentUser);

            errorLabel.setStyle("-fx-text-fill: green;");
            errorLabel.setText("Perfil actualizado correctamente.");
        } catch (Exception e) {
            e.printStackTrace();
            errorLabel.setText("Error al actualizar el perfil.");
        }
    }
    
}*/
