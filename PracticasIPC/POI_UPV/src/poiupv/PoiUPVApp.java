package poiupv;

/**
 *
 * @author alber
 */
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javafx.application.Application;
import static javafx.application.Application.launch;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

/**
 *
 * @author jose
 */
public class PoiUPVApp extends Application {
    
    private static Stage primaryStage;
    
    public static void setRoot(String fxmlName) {
        try {
            FXMLLoader loader = new FXMLLoader(PoiUPVApp.class.getResource("/poiupv/menuPrincipal.fxml"));
            Parent root = loader.load();
            primaryStage.setScene(new Scene(root));
            primaryStage.setTitle("POI UPV");
            primaryStage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("No se pudo cargar la escena:  menuPrincipal.fxml");
        }
    }
    
     @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/poiupv/Login.fxml"));
        
        Scene scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Inicio de Sesión");
        stage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
        
    }
    
    public static Map<String, Object> controllersMap;
    public static Map<String, Object> escenasMap = new HashMap<>();
}
