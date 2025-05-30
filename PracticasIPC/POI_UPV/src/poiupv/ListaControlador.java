package poiupv;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import poiupv.PoiUPVApp;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;


import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.text.Text;
import static poiupv.PoiUPVApp.controllersMap;
import static poiupv.PoiUPVApp.escenasMap;
/**
 * FXML Controller class
 *
 * @author alber 
 */
public class ListaControlador implements Initializable {

    private ListView<ejercicios> list;
    @FXML
    private Button btnRealizarEjercicio;
    @FXML
    private Button btnVolver;
    private List<ejercicios> ejercicios = new ArrayList<>();
    @FXML
    private Button btnaleatorio;
    int id;
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
      cargarEjerciciosDesdeBD();
    list.getItems().addAll(ejercicios);
     list.setCellFactory(lv -> new ListCell<ejercicios>() {
        private final Text text;

        {
            text = new Text();
            text.wrappingWidthProperty().bind(lv.widthProperty().subtract(20));
           
        }

        @Override
        protected void updateItem(ejercicios item, boolean empty) {
            super.updateItem(item, empty);
            if (empty || item == null) {
                setGraphic(null);
            } else {
                text.setText(item.toString());
                setGraphic(text);
            }
        }
    });
    btnRealizarEjercicio.disableProperty().bind(
        list.getSelectionModel().selectedItemProperty().isNull()
            
    );
     id =0;
     btnVolver.getStyleClass().add("btnWithLabel2");
    }    
private void cargarEjerciciosDesdeBD() {
    try (Connection conn = DriverManager.getConnection("jdbc:sqlite:data.db")) {
        String data = "SELECT * FROM problem";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(data);
        while (rs.next()) {
    List<String> respuestas = Arrays.asList(
        rs.getString("answer1"), rs.getString("answer2"),   rs.getString("answer3"),
        rs.getString("answer4")   );
    int correcta = -1;
    if (rs.getString("val1").equalsIgnoreCase("true")) correcta = 0;
    if (rs.getString("val2").equalsIgnoreCase("true")) correcta = 1;
    if (rs.getString("val3").equalsIgnoreCase("true")) correcta = 2;
    if (rs.getString("val4").equalsIgnoreCase("true")) correcta = 3;
          
    
    ejercicios.add(new ejercicios(correcta, rs.getString("text"), respuestas));
        }
        
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    @FXML
    private void buttonHandler(ActionEvent event) {
        
        Object source = event.getSource();
        String id = ((Button)source).getId();
        
        switch(id) {
            case "btnVolver":
                    
                PoiUPVApp.setRoot("principal3");
                break;
                
            case "btnRealizarEjercicio":
                   ejercicios seleccionado = list.getSelectionModel().getSelectedItem();
                  
                if (seleccionado == null) return;

                
              
                }
        
        }

    
}