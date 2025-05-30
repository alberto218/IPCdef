package poiupv;

/**
 *
 * @author alber
 */
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.ScrollEvent;
import javafx.scene.layout.Pane;

import java.io.InputStream;
import java.net.URL;
import java.util.ResourceBundle;

public class MapaController implements Initializable {

    @FXML
    private ImageView imageView;

    @FXML
    private Pane paneZoom;

    private double scaleValue = 1.0;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            InputStream is = getClass().getResourceAsStream("/resources/carta_nautica.jpg");
            Image image = new Image(is);
            imageView.setImage(image);
            imageView.setPreserveRatio(true);
            imageView.setFitWidth(800); // Tamaño inicial
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Habilitar zoom con scroll del ratón
        paneZoom.setOnScroll((ScrollEvent event) -> {
            double delta = 1.1;
            if (event.getDeltaY() < 0) {
                scaleValue /= delta;
            } else {
                scaleValue *= delta;
            }
            imageView.setScaleX(scaleValue);
            imageView.setScaleY(scaleValue);
        });
    }
}