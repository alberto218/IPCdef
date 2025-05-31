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
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.image.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import model.Navigation;
import model.User;
import javafx.scene.Scene;
import javafx.scene.control.TextField;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;



/**
 *
 * @author Diego Molina
 */
public class modificarPerfilController {

    private Label labelNickname;
    private ImageView imageAvatar;
    @FXML private TextField emailField;
    @FXML private PasswordField passwordField;
    @FXML private DatePicker birthdatePicker;
    private Label feedbackLabel;
    private User usuario;
    private User usuarioActual;

    private User currentUser;
    private Image newAvatar;
    @FXML
    private TextField nicknameField;
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

    public void initializeData(User user) {
        this.currentUser = user;
        labelNickname.setText(user.getNickName());
        imageAvatar.setImage(user.getAvatar());
        emailField.setText(user.getEmail());
        birthdatePicker.setValue(user.getBirthdate());
    }

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

    @FXML
    private void handleBackButton(ActionEvent event) {
        try {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("menuPrincipal.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        stage.setScene(new Scene(root));
        stage.setTitle("Menú Principal");
        stage.show();
    } catch (IOException e) {
        e.printStackTrace();
    }
    }
    
    public void setUsuario(User usuario) {
    this.usuario = usuario;
    nicknameField.setText(usuario.getNickName()); // Mostrar el nombre actual
    if (nicknameField != null) {
            nicknameField.setText(usuario.getNickName());
        }
    this.usuarioActual = usuario;

    // Rellenar los campos con la información actual
    nicknameField.setText(usuario.getNickName());  // Este no se modificará
    if (usuario.getBirthdate() != null) {
        birthdatePicker.setValue(usuario.getBirthdate());
    }
    passwordField.setText(usuario.getPassword());
    emailField.setText(usuario.getEmail());
    }

    @FXML
    private void handleUpdatePerfil(ActionEvent event) {
        String nuevaContrasena = passwordField.getText();
    String nuevoEmail = emailField.getText();
    LocalDate nuevaFechaNacimiento = birthdatePicker.getValue();

    if (nuevaContrasena.isEmpty() || nuevoEmail.isEmpty() || nuevaFechaNacimiento == null) {
        errorLabel.setText("Por favor, complete todos los campos.");
        return;
    }

    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db")) {
        String sql = "UPDATE user SET password = ?, email = ?, birthdate = ? WHERE nickname = ?";
        PreparedStatement pstmt = conn.prepareStatement(sql);
        pstmt.setString(1, nuevaContrasena);
        pstmt.setString(2, nuevoEmail);
        pstmt.setString(3, nuevaFechaNacimiento.toString()); // Formato ISO
        pstmt.setString(4, usuarioActual.getNickName());

        int filas = pstmt.executeUpdate();

        if (filas > 0) {
            errorLabel.setText("Perfil modificado, puede volver");
        } else {
            errorLabel.setText("Error al modificar el perfil.");
        }
    } catch (SQLException e) {
        errorLabel.setText("Error en la base de datos.");
        e.printStackTrace();
    }
    }
}
