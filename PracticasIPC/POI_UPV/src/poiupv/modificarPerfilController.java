/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package poiupv;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.Period;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import model.Navigation;
import model.User;



/**
 *
 * @author Diego Molina
 */
public class modificarPerfilController {

    @FXML private Label labelNickname;
    @FXML private ImageView imageAvatar;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker birthdatePicker;
    @FXML private Label feedbackLabel;

    private User currentUser;
    private Image newAvatar;

    public void initializeData(User user) {
        this.currentUser = user;
        labelNickname.setText(user.getNickName());
        imageAvatar.setImage(user.getAvatar());
        emailField.setText(user.getEmail());
        birthdatePicker.setValue(user.getBirthdate());
    }

    @FXML
    private void handleCambiarAvatar(ActionEvent event) {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar nuevo avatar");
        fileChooser.getExtensionFilters().add(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        File selectedFile = fileChooser.showOpenDialog(null);
        if (selectedFile != null) {
            try {
                newAvatar = new Image(new FileInputStream(selectedFile));
                imageAvatar.setImage(newAvatar);
            } catch (IOException e) {
                feedbackLabel.setText("Error cargando imagen.");
            }
        }
    }

    @FXML
    private void handleGuardarCambios(ActionEvent event) {
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate birthdate = birthdatePicker.getValue();

        // Validaciones
        if (!User.checkEmail(email)) {
            feedbackLabel.setText("Correo inválido.");
            return;
        }

        if (!User.checkPassword(password)) {
            feedbackLabel.setText("Contraseña inválida.");
            return;
        }

        if (birthdate == null || Period.between(birthdate, LocalDate.now()).getYears() < 16) {
            feedbackLabel.setText("Debes tener al menos 16 años.");
            return;
        }

        try {
            currentUser.setEmail(email);
            currentUser.setPassword(password);
            currentUser.setBirthdate(birthdate);
            if (newAvatar != null) {
                currentUser.setAvatar(newAvatar);
            }
            feedbackLabel.setText("Perfil actualizado correctamente.");
        } catch (Exception e) {
            feedbackLabel.setText("Error actualizando perfil.");
        }
    }
    
}
