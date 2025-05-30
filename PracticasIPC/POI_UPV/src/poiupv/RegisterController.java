import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import java.io.File;
import java.time.LocalDate;
import java.time.Period;

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
        // Configurar el botón de selección de avatar
        chooseAvatarBtn.setOnAction(event -> selectAvatar());
        
        // Configurar el botón de registro
        registerBtn.setOnAction(event -> registerUser());
        
        // Inicializar el DatePicker con valores razonables
        birthdatePicker.setValue(LocalDate.now().minusYears(18));
    }

    private void selectAvatar() {
        FileChooser fileChooser = new FileChooser();
        fileChooser.setTitle("Seleccionar Avatar");
        fileChooser.getExtensionFilters().addAll(
            new FileChooser.ExtensionFilter("Imágenes", "*.png", "*.jpg", "*.jpeg")
        );
        
        File selectedFile = fileChooser.showOpenDialog(avatarPreview.getScene().getWindow());
        if (selectedFile != null) {
            avatarPath = selectedFile.getAbsolutePath();
            avatarPreview.setImage(new Image("file:" + avatarPath));
        }
    }

    private void registerUser() {
        // Validar campos
        if (!validateFields()) {
            return;
        }
        
        // Crear el nuevo perfil (aquí iría tu lógica de base de datos)
        String nickname = nicknameField.getText();
        String email = emailField.getText();
        String password = passwordField.getText();
        LocalDate birthdate = birthdatePicker.getValue();
        
        // Aquí normalmente guardarías en la base de datos
        System.out.println("Nuevo usuario registrado:");
        System.out.println("Nickname: " + nickname);
        System.out.println("Email: " + email);
        System.out.println("Contraseña: " + password);
        System.out.println("Fecha nacimiento: " + birthdate);
        System.out.println("Avatar: " + (avatarPath != null ? avatarPath : "Ninguno"));
        
        // Mostrar mensaje de éxito y cerrar ventana
        showAlert("Registro exitoso", "Usuario creado correctamente", Alert.AlertType.INFORMATION);
        ((Stage) registerBtn.getScene().getWindow()).close();
    }

    private boolean validateFields() {
        // Validar nickname
        if (nicknameField.getText().isEmpty()) {
            errorLabel.setText("El nickname es obligatorio");
            return false;
        }
        
        // Validar email
        if (emailField.getText().isEmpty() || !emailField.getText().contains("@")) {
            errorLabel.setText("Ingrese un email válido");
            return false;
        }
        
        // Validar contraseña
        if (passwordField.getText().length() < 6) {
            errorLabel.setText("La contraseña debe tener al menos 6 caracteres");
            return false;
        }
        
        // Validar fecha de nacimiento
        if (birthdatePicker.getValue() == null) {
            errorLabel.setText("Seleccione una fecha de nacimiento");
            return false;
        }
        
        // Validar edad mínima (18 años)
        Period age = Period.between(birthdatePicker.getValue(), LocalDate.now());
        if (age.getYears() < 18) {
            errorLabel.setText("Debes tener al menos 18 años para registrarte");
            return false;
        }
        
        errorLabel.setText("");
        return true;
    }

    private void showAlert(String title, String message, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}