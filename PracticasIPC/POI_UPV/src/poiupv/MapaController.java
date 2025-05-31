package poiupv;

import static java.awt.SystemColor.text;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.ColorPicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MenuButton;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import javafx.util.Duration;
import poiupv.Poi;

/**
 *
 * @author jsoler
 */
public class MapaController implements Initializable {

    //=======================================
    // hashmap para guardar los puntos de interes POI
    private final HashMap<String, Poi> hm = new HashMap<>();
    private ObservableList<Poi> data;
   
    
    private Group zoomGroup;
    private Group drawnMarksGroup; // New Group specifically for drawn marks

    // --- New variables for drawing ---
    private String currentTool = "None"; // To keep track of the selected tool
    private Line drawingLine = null;
    private Line previewLine = null;
    private Arc drawingArc = null;
    private Point2D startPoint; // For lines and arcs
    private Point2D releasedPoint;
    private Point2D currentPoint;
    private Point2D tempLineStart = null;
    private Point2D arcCenter = null;
    private double arcRadius = -1;
    private Double arcStartAngle = null;
    private Line arcPreviewLine = null;
    

    @FXML
    private ListView<Poi> map_listview;
    @FXML
    private ScrollPane map_scrollpane;
    @FXML
    private Slider zoom_slider;
    @FXML
    private SplitPane splitPane;
    @FXML
    private Label mousePosition;
    @FXML
    private ColorPicker colorPicker;
    @FXML
    private MenuButton map_pin;
    @FXML
    private MenuItem pin_info;
    @FXML
    private MenuButton herramienta;
    @FXML 
    private MenuItem pointItem;
    @FXML 
    private MenuItem lineItem;
    @FXML 
    private MenuItem arcItem;
    @FXML 
    private MenuItem textItem;
    @FXML 
    private MenuItem changeColorItem;
    @FXML 
    private MenuItem deleteItem;
    @FXML
    private MenuItem limpiarItem;
    
    


    @FXML
    void zoomIn(ActionEvent event) {
        //================================================
        // el incremento del zoom dependerá de los parametros del
        // slider y del resultado esperado
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal += 0.02);
    }

    @FXML
    void zoomOut(ActionEvent event) {
        double sliderVal = zoom_slider.getValue();
        zoom_slider.setValue(sliderVal + -0.02);
    }
    
    // esta funcion es invocada al cambiar el value del slider zoom_slider
    private void zoom(double scaleValue) {
        //===================================================
        //guardamos los valores del scroll antes del escalado
        double scrollH = map_scrollpane.getHvalue();
        double scrollV = map_scrollpane.getVvalue();
        //===================================================
        // escalamos el zoomGroup en X e Y con el valor de entrada
        zoomGroup.setScaleX(scaleValue);
        zoomGroup.setScaleY(scaleValue);
        //===================================================
        // recuperamos el valor del scroll antes del escalado
        map_scrollpane.setHvalue(scrollH);
        map_scrollpane.setVvalue(scrollV);
    }

    @FXML
    void listClicked(MouseEvent event) {
        Poi itemSelected = map_listview.getSelectionModel().getSelectedItem();
            
            // Animación del scroll hasta la mousePosistion del item seleccionado
            double mapWidth = zoomGroup.getBoundsInLocal().getWidth();
            double mapHeight = zoomGroup.getBoundsInLocal().getHeight();
            double scrollH = itemSelected.getPosition().getX() / mapWidth;
            double scrollV = itemSelected.getPosition().getY() / mapHeight;
            final Timeline timeline = new Timeline();
            final KeyValue kv1 = new KeyValue(map_scrollpane.hvalueProperty(), scrollH);
            final KeyValue kv2 = new KeyValue(map_scrollpane.vvalueProperty(), scrollV);
            final KeyFrame kf = new KeyFrame(Duration.millis(500), kv1, kv2);
            timeline.getKeyFrames().add(kf);
            timeline.play();
            
            map_pin.setLayoutX(itemSelected.getPosition().getX());
            map_pin.setLayoutY(itemSelected.getPosition().getY());
            pin_info.setText(itemSelected.getDescription());
            map_pin.setVisible(true);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        
        //initData();
        
        // inicializamos el slider y enlazamos con el zoom
        zoom_slider.setMin(0.075);
        zoom_slider.setMax(0.4);
        zoom_slider.setValue(0.15);
        zoom_slider.valueProperty().addListener((o, oldVal, newVal) -> zoom((Double) newVal));

        //=========================================================================
        //Envuelva el contenido de scrollpane en un grupo para que
        //ScrollPane vuelva a calcular las barras de desplazamiento tras el escalado
        Group contentGroup = new Group();
        zoomGroup = new Group();
        drawnMarksGroup = new Group(); // Initialize the group for drawn marks
        
        // The order matters here.
        // First, add the original content of the scrollpane (your map image) to zoomGroup.
        Node scrollContent = map_scrollpane.getContent();
        if (scrollContent != null) {
            zoomGroup.getChildren().add(scrollContent);
        } else {
            System.err.println("Warning: map_scrollpane content is null. Ensure your FXML has an ImageView or similar inside the ScrollPane.");
        }
        
        // Then add the drawnMarksGroup to zoomGroup, so drawn items are on top of the map. 
        //zoomGroup.getChildren().add(map_scrollpane.getContent());
        zoomGroup.getChildren().add(drawnMarksGroup);
        contentGroup.getChildren().add(zoomGroup);
        map_scrollpane.setContent(contentGroup);
        
       
        
        pointItem.setOnAction(e -> selectTool("Punto"));
        lineItem.setOnAction(e -> selectTool("Línea"));
        arcItem.setOnAction(e -> selectTool("Arco"));
        textItem.setOnAction(e -> selectTool("Texto"));
        changeColorItem.setOnAction(e -> selectTool("Cambiar Color"));
        deleteItem.setOnAction(e -> selectTool("Eliminar"));

        herramienta.setText("Marca");
        
        // Set default color
        colorPicker.setValue(Color.RED);

        // Add mouse event handlers to the zoomGroup (where drawing happens)
        // This ensures coordinates are relative to the zoomed content
        zoomGroup.setOnMousePressed(this::handleMousePressed);
        zoomGroup.setOnMouseDragged(this::handleMouseDragged);
        zoomGroup.setOnMouseReleased(this::handleMouseReleased);
        zoomGroup.setOnMouseMoved(this::handleMouseMoved);
    }
    
    @FXML
    private void clearMarks() {
        Alert confirmDialog = new Alert(Alert.AlertType.CONFIRMATION);
        confirmDialog.setTitle("Confirmar limpieza");
        confirmDialog.setHeaderText("¿Quieres borrar todas las marcas del mapa?");
        confirmDialog.setContentText("Esta acción no se puede deshacer.");

        Optional<ButtonType> result = confirmDialog.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Eliminar todos los nodos que sean marcas (sin eliminar nodos no marcables si los hay)
            drawnMarksGroup.getChildren().removeIf(mark ->
                mark instanceof Circle || mark instanceof Line || mark instanceof Arc || mark instanceof Text
            );

            // Reset de herramientas
            arcCenter = null;
            arcRadius = -1;
            arcStartAngle = null;
            tempLineStart = null;
            previewLine = null;
            arcPreviewLine = null;

            System.out.println("Todas las marcas han sido eliminadas.");
        }
    }
    

    @FXML
    private void showPosition(MouseEvent event) {
        // Correctly get local coordinates relative to the zoomed content
        Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
        mousePosition.setText("sceneX: " + (int) event.getSceneX() + ", sceneY: " + (int) event.getSceneY() + "\n"
                + "         X: " + (int) localPoint.getX() + ",          Y: " + (int) localPoint.getY());
    }

    private void closeApp(ActionEvent event) {
        ((Stage) zoom_slider.getScene().getWindow()).close();
    }

    @FXML
    private void about(ActionEvent event) {
        Alert mensaje = new Alert(Alert.AlertType.INFORMATION);
        // Acceder al Stage del Dialog y cambiar el icono
        Stage dialogStage = (Stage) mensaje.getDialogPane().getScene().getWindow();
        dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));
        mensaje.setTitle("Acerca de");
        mensaje.setHeaderText("IPC - 2025");
        mensaje.showAndWait();
    }

    @FXML
    private void addPoi(MouseEvent event) {

        if (event.isControlDown()) {
            Dialog<Poi> poiDialog = new Dialog<>();
            poiDialog.setTitle("Nuevo POI");
            poiDialog.setHeaderText("Introduce un nuevo POI");
            // Acceder al Stage del Dialog y cambiar el icono
            Stage dialogStage = (Stage) poiDialog.getDialogPane().getScene().getWindow();
            dialogStage.getIcons().add(new Image(getClass().getResourceAsStream("/resources/logo.png")));

            ButtonType okButton = new ButtonType("Aceptar", ButtonBar.ButtonData.OK_DONE);
            poiDialog.getDialogPane().getButtonTypes().addAll(okButton, ButtonType.CANCEL);

            TextField nameField = new TextField();
            nameField.setPromptText("Nombre del POI");

            TextArea descArea = new TextArea();
            descArea.setPromptText("Descripción...");
            descArea.setWrapText(true);
            descArea.setPrefRowCount(5);

            VBox vbox = new VBox(10, new Label("Nombre:"), nameField, new Label("Descripción:"), descArea);
            poiDialog.getDialogPane().setContent(vbox);

            poiDialog.setResultConverter(dialogButton -> {
                if (dialogButton == okButton) {
                    return new Poi(nameField.getText().trim(), descArea.getText().trim(), 0, 0);
                }
                return null;
            });
            Optional<Poi> result = poiDialog.showAndWait();

            if(result.isPresent()) {
                Point2D localPoint = zoomGroup.sceneToLocal(event.getSceneX(), event.getSceneY());
                Poi poi=result.get();
                poi.setPosition(localPoint);
                map_listview.getItems().add(poi);
            }
        }
    }

    // --- Mouse event handlers for drawing ---
    private void handleMousePressed(MouseEvent event) {
        
        event.consume(); 
        
        Node source = (Node) event.getSource();
        Point2D scenePoint = source.localToScene(event.getX(), event.getY());
        startPoint = zoomGroup.sceneToLocal(scenePoint); // Use getX(), getY() for local coordinates on zoomGroup
        

        if (event.isPrimaryButtonDown()) { // Only draw with primary mouse button
            Color selectedColor = colorPicker.getValue();

            switch (currentTool) {
                case "Punto": // 3.2 Mark a point
                    Circle point = new Circle(startPoint.getX(), startPoint.getY(), 20); // Increased radius for better visibility
                    point.setFill(selectedColor);
                    point.setStroke(Color.BLACK); // Add a stroke for better visibility
                    point.setStrokeWidth(1);
                    drawnMarksGroup.getChildren().add(point);
                    System.out.println("Point added at: " + startPoint); // Debug print
                    break;
                case "Línea":
                    if ("Línea".equals(currentTool)) {
                        if (tempLineStart == null) {
                            // Primer clic → guarda el inicio y crea línea de vista previa
                            tempLineStart = startPoint;

                            previewLine = new Line();
                            previewLine.setStartX(tempLineStart.getX());
                            previewLine.setStartY(tempLineStart.getY());
                            previewLine.setEndX(tempLineStart.getX());
                            previewLine.setEndY(tempLineStart.getY());
                            previewLine.setStroke(colorPicker.getValue());
                            previewLine.setStrokeWidth(10);
                            previewLine.getStrokeDashArray().addAll(10.0, 5.0); // línea discontinua
                            drawnMarksGroup.getChildren().add(previewLine);
                        } else {
                            // Segundo clic → traza línea definitiva
                            Line finalLine = new Line(
                                tempLineStart.getX(), tempLineStart.getY(),
                                startPoint.getX(), startPoint.getY()
                            );
                            finalLine.setStroke(colorPicker.getValue());
                            finalLine.setStrokeWidth(10);
                            drawnMarksGroup.getChildren().add(finalLine);

                            // Elimina la línea de vista previa
                            drawnMarksGroup.getChildren().remove(previewLine);
                            previewLine = null;
                            tempLineStart = null;
                        }
                    }
                    break;
                case "Arco": {

                    if (arcCenter == null) {
                        // Primer clic: definir centro
                        arcCenter = startPoint;

                        // Pedir radio
                        TextInputDialog dialog = new TextInputDialog("1000");
                        dialog.setTitle("Radio del Arco");
                        dialog.setHeaderText("Introduce el radio del arco (en píxeles):");

                        Optional<String> result = dialog.showAndWait();
                        result.ifPresent(input -> {
                            try {
                                arcRadius = Double.parseDouble(input);
                                if (arcRadius <= 0) throw new NumberFormatException();
                                System.out.println("Centro definido: " + arcCenter + ", radio: " + arcRadius);
                            } catch (NumberFormatException ex) {
                                showAlert("Entrada no válida", "Introduce un número válido para el radio.");
                                arcCenter = null;
                                arcRadius = -1;
                            }
                        });
                    } else if (arcStartAngle == null) {
                        // Segundo clic → definir ángulo inicial
                        arcStartAngle = angleFromCenter(startPoint);

                        // Línea de vista previa: desde centro al cursor
                        if (arcPreviewLine == null) {
                            arcPreviewLine = new Line();
                            arcPreviewLine.setStroke(Color.GRAY);
                            arcPreviewLine.getStrokeDashArray().addAll(8.0, 6.0);
                            arcPreviewLine.setStrokeWidth(10);
                            drawnMarksGroup.getChildren().add(arcPreviewLine);
                        }
                    } else {
                        // Tercer clic → ángulo final
                        double arcEndAngle = angleFromCenter(startPoint);
                        double length = arcEndAngle - arcStartAngle;

                        // ➕ Corrección para ángulo menor a 180 grados
                        if (length < 0) length += 360;
                        if (length > 180) length -= 360;

                        Arc arc = new Arc();
                        arc.setCenterX(arcCenter.getX());
                        arc.setCenterY(arcCenter.getY());
                        arc.setRadiusX(arcRadius);
                        arc.setRadiusY(arcRadius);
                        arc.setStartAngle(arcStartAngle);
                        arc.setLength(length);
                        arc.setType(ArcType.OPEN);
                        arc.setStroke(colorPicker.getValue());
                        arc.setStrokeWidth(10);
                        arc.setFill(Color.TRANSPARENT);

                        drawnMarksGroup.getChildren().add(arc);

                        arcCenter = null;
                        arcRadius = -1;
                        arcStartAngle = null;
                    }
                    break;
                }
                case "Texto": // 3.5 Anotar texto
                    // This part should be handled on MouseReleased or with a dialog for text input
                    break;
                case "Cambiar Color": // 3.6 Cambiar el color de una marca
                    // This will be handled on mouse click for selection in MouseReleased
                    break;
                case "Eliminar": // 3.7 Eliminar una marca
                    // This will be handled on mouse click for selection in MouseReleased
                    break;
            }
        }
    }

    private void handleMouseDragged(MouseEvent event) {
        // Ensure event is handled by primary button down
        if (!event.isPrimaryButtonDown()) {
            return;
        }
        
        Point2D currentPoint = zoomGroup.sceneToLocal(event.getX(), event.getY());

        switch (currentTool) {
            case "Línea":
                if (drawingLine != null) {
                    drawingLine.setEndX(currentPoint.getX());
                    drawingLine.setEndY(currentPoint.getY());
                }
                break;
            case "Arco":
                if (drawingArc != null) {
                    double radiusX = Math.abs(currentPoint.getX() - startPoint.getX());
                    double radiusY = Math.abs(currentPoint.getY() - startPoint.getY());

                    drawingArc.setRadiusX(radiusX);
                    drawingArc.setRadiusY(radiusY);
                }
                break;
        }
    }

    private void handleMouseReleased(MouseEvent event) {
        // Ensure event is handled by primary button up
        if (event.getButton() != javafx.scene.input.MouseButton.PRIMARY) {
            return;
        }

        // Reset drawing objects after completion
        drawingLine = null;
        drawingArc = null;
        
        Node source = (Node) event.getSource();
        Point2D scenePoint = source.localToScene(event.getX(), event.getY());
        releasedPoint = zoomGroup.sceneToLocal(scenePoint);
        

        switch (currentTool) {
            case "Texto": 
                if (!"Texto".equals(currentTool)) return;

                Dialog<String> textDialog = new Dialog<>();
                textDialog.setTitle("Anotar texto");

                TextField inputField = new TextField();
                inputField.setPromptText("Escribe aquí...");

                VBox textBox = new VBox(new Label("Texto:"), inputField);
                textBox.setSpacing(10);
                textDialog.getDialogPane().setContent(textBox);
                textDialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

                textDialog.setResultConverter(dialogButton -> {
                    if (dialogButton == ButtonType.OK) {
                        return inputField.getText();
                    }
                    return null;
                });

                Optional<String> resultText = textDialog.showAndWait();
                resultText.ifPresent(textContent -> {
                    Text textNode = new Text(releasedPoint.getX(), releasedPoint.getY(), textContent);
                    textNode.setFill(colorPicker.getValue());
                    textNode.setStyle("-fx-font-size: 100px;");
                    drawnMarksGroup.getChildren().add(textNode);
                });
                break;
            case "Cambiar Color": // 3.6 Cambiar el color de una marca
                // Iterate through drawn marks in reverse to select the topmost one
                for (int i = drawnMarksGroup.getChildren().size() - 1; i >= 0; i--) {
                    Node mark = drawnMarksGroup.getChildren().get(i);
                    boolean match = false;

                    if (mark instanceof Circle circle) {
                        match = circle.contains(circle.sceneToLocal(event.getSceneX(), event.getSceneY()));
                    } else if (mark instanceof Arc arc) {
                        match = arc.getBoundsInParent().contains(releasedPoint);
                    } else if (mark instanceof Text text) {
                        match = text.getBoundsInParent().contains(releasedPoint);
                    } else if (mark instanceof Line line) {
                        match = isPointNearLine(line, releasedPoint, 5); // 5px de tolerancia
                    }

                    if (match) {
                        Color newColor = colorPicker.getValue();
                        if (mark instanceof Circle circle) {
                            circle.setFill(newColor);
                            circle.setStroke(newColor.darker());
                        } else if (mark instanceof Line line) {
                            line.setStroke(newColor);
                        } else if (mark instanceof Arc arc) {
                            arc.setStroke(newColor);
                        } else if (mark instanceof Text text) {
                            text.setFill(newColor);
                        }
                        System.out.println("Changed color of: " + mark.getClass().getSimpleName());
                        break;
                    }
                }
                break;
            case "Eliminar": // 3.7 Eliminar una marca
                for (int i = drawnMarksGroup.getChildren().size() - 1; i >= 0; i--) {
                    Node mark = drawnMarksGroup.getChildren().get(i);
                    boolean match = false;

                    if (mark instanceof Circle circle) {
                        match = circle.getBoundsInParent().contains(releasedPoint.getX(), releasedPoint.getY());
                    } else if (mark instanceof Arc arc) {
                        match = arc.getBoundsInParent().contains(releasedPoint.getX(), releasedPoint.getY());
                    } else if (mark instanceof Text text) {
                        match = text.getBoundsInParent().contains(releasedPoint.getX(), releasedPoint.getY());
                    } else if (mark instanceof Line line) {
                        match = isPointNearLine(line, releasedPoint, 5); // 5 píxeles de tolerancia
                    }

                    if (match) {
                        drawnMarksGroup.getChildren().remove(mark);
                        System.out.println("Deleted: " + mark.getClass().getSimpleName());
                        break;
                    }
                }
                break;
        }
    }
    
    private void handleMouseMoved(MouseEvent event) {
        if ("Línea".equals(currentTool)) {
            if (tempLineStart == null || previewLine == null) return;

            Node source = (Node) event.getSource();
            Point2D scenePoint = source.localToScene(event.getX(), event.getY());
            currentPoint = zoomGroup.sceneToLocal(scenePoint);

            previewLine.setEndX(currentPoint.getX());
            previewLine.setEndY(currentPoint.getY());
        }
    }
    
    private boolean isPointNearLine(Line line, Point2D point, double tolerance) {
        double x1 = line.getStartX();
        double y1 = line.getStartY();
        double x2 = line.getEndX();
        double y2 = line.getEndY();

        // distancia del punto a la línea
        double distance = pointToSegmentDistance(point.getX(), point.getY(), x1, y1, x2, y2);
        return distance <= tolerance;
    }
    
    private double pointToSegmentDistance(double px, double py, double x1, double y1, double x2, double y2) {
        double dx = x2 - x1;
        double dy = y2 - y1;
        if (dx == 0 && dy == 0) {
            dx = px - x1;
            dy = py - y1;
            return Math.sqrt(dx * dx + dy * dy);
        }

        double t = ((px - x1) * dx + (py - y1) * dy) / (dx * dx + dy * dy);
        t = Math.max(0, Math.min(1, t));

        double nearestX = x1 + t * dx;
        double nearestY = y1 + t * dy;
        dx = px - nearestX;
        dy = py - nearestY;
        return Math.sqrt(dx * dx + dy * dy);
    }
    
    private void selectTool(String tool) {
        currentTool = tool;
        herramienta.setText(tool);

        previewLine = null;
        tempLineStart = null;
        arcCenter = null;
        arcRadius = -1;
    }
    
    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle(title);
        alert.setContentText(content);
        alert.showAndWait();
    }
    
    private double angleFromCenter(Point2D point) {
        double dx = point.getX() - arcCenter.getX();
        double dy = point.getY() - arcCenter.getY();
        return Math.toDegrees(Math.atan2(-dy, dx)); // -dy por sistema de coordenadas JavaFX
    }
    
    private Node getTopMostMarkAt(Point2D point) {
        for (int i = drawnMarksGroup.getChildren().size() - 1; i >= 0; i--) {
            Node mark = drawnMarksGroup.getChildren().get(i);
            if (mark instanceof Circle && ((Circle) mark).getBoundsInParent().contains(point)) return mark;
            // etc.
        }
        return null;
    }

}