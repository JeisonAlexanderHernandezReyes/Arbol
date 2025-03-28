package grammaranalyzer;

import grammaranalyzer.controller.GrammarController;
import grammaranalyzer.model.DerivationResult;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;

import java.util.Optional;

/**
 * Clase principal de la aplicación Analizador de Gramáticas
 * Implementa una interfaz de usuario responsiva que se adapta a diferentes tamaños de pantalla
 */
public class GrammarAnalyzerApp extends Application {

    // Controlador principal
    private GrammarController controller;

    // Componentes de la interfaz - Panel de gramática
    private TextField txtTerminal;
    private TextField txtNonTerminal;
    private ComboBox<String> cmbStartSymbol;
    private ComboBox<String> cmbProductionNonTerminal;
    private TextField txtProduction;
    private TextArea txtGrammarDisplay;

    // Componentes de la interfaz - Panel de verificación
    private TextField txtWordToCheck;
    private TextArea txtResultDisplay;

    // Componentes de la interfaz - Árboles
    private TreeView<String> treeViewSpecific;
    private TreeView<String> treeViewGeneral;

    // Componentes responsivos
    private SplitPane mainSplitPane;
    private BorderPane root;

    @Override
    public void start(Stage primaryStage) {
        // Inicializar controlador
        controller = new GrammarController();

        // Crear la interfaz
        root = new BorderPane();
        root.setPadding(new Insets(10));

        // Panel izquierdo: Gramática y Verificación
        VBox leftPanel = new VBox(15);
        leftPanel.setPadding(new Insets(10));
        leftPanel.setPrefWidth(350);
        leftPanel.setMinWidth(250);

        // Scrollable para el panel izquierdo (permite ver todo el contenido en pantallas pequeñas)
        ScrollPane leftScrollPane = new ScrollPane(leftPanel);
        leftScrollPane.setFitToWidth(true);
        leftScrollPane.setFitToHeight(true);
        leftScrollPane.setPrefViewportWidth(350);
        leftScrollPane.setPrefViewportHeight(600);

        // Agregar paneles al panel izquierdo
        leftPanel.getChildren().addAll(
                createGrammarPanel(),
                createVerificationPanel()
        );

        // Panel central/derecho: Resultados y Árboles
        TabPane rightPanel = createResultsPanel();

        // Usar SplitPane para permitir redistribuir el espacio entre paneles
        mainSplitPane = new SplitPane();
        mainSplitPane.getItems().addAll(leftScrollPane, rightPanel);
        mainSplitPane.setDividerPositions(0.35);

        // Hacer que el SplitPane ocupe todo el espacio disponible
        VBox.setVgrow(mainSplitPane, Priority.ALWAYS);
        HBox.setHgrow(mainSplitPane, Priority.ALWAYS);

        // Organizar los paneles principales
        root.setCenter(mainSplitPane);

        // Añadir barra de herramientas con ajustes de visualización
        root.setTop(createToolbar());

        // Configurar y mostrar la escena
        Scene scene = new Scene(root, 1000, 700);

        // Capturar cambios en el tamaño de la ventana para ajustar la interfaz
        scene.widthProperty().addListener((obs, oldVal, newVal) -> {
            adjustLayoutForWidth(newVal.doubleValue());
        });

        primaryStage.setTitle("Analizador de Gramáticas");
        primaryStage.setScene(scene);
        primaryStage.setMinWidth(800);
        primaryStage.setMinHeight(600);
        primaryStage.show();
    }

    /**
     * Crea una barra de herramientas con opciones de visualización
     */
    private ToolBar createToolbar() {
        ToolBar toolbar = new ToolBar();

        // Botón para cambiar entre vista horizontal/vertical
        Button btnToggleOrientation = new Button("Cambiar Orientación");
        btnToggleOrientation.setOnAction(e -> toggleSplitPaneOrientation());

        // Añadir un separador
        Separator sep = new Separator();
        sep.setOrientation(Orientation.VERTICAL);

        // Etiqueta de información
        Label lblInfo = new Label("Redimensione la ventana o ajuste los divisores para personalizar la vista");

        toolbar.getItems().addAll(btnToggleOrientation, sep, lblInfo);

        return toolbar;
    }

    /**
     * Cambia la orientación del SplitPane entre horizontal y vertical
     */
    private void toggleSplitPaneOrientation() {
        if (mainSplitPane.getOrientation() == Orientation.HORIZONTAL) {
            mainSplitPane.setOrientation(Orientation.VERTICAL);
            mainSplitPane.setDividerPositions(0.4);
        } else {
            mainSplitPane.setOrientation(Orientation.HORIZONTAL);
            mainSplitPane.setDividerPositions(0.35);
        }
    }

    /**
     * Ajusta el diseño basado en el ancho de la ventana
     */
    private void adjustLayoutForWidth(double width) {
        if (width < 900) {
            // En pantallas estrechas, usar orientación vertical
            mainSplitPane.setOrientation(Orientation.VERTICAL);
            mainSplitPane.setDividerPositions(0.4);
        } else {
            // En pantallas anchas, usar orientación horizontal
            mainSplitPane.setOrientation(Orientation.HORIZONTAL);
            mainSplitPane.setDividerPositions(0.35);
        }
    }

    /**
     * Crea el panel para definir la gramática
     */
    private VBox createGrammarPanel() {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-padding: 10;");

        // Hacer que el panel se expanda verticalmente
        VBox.setVgrow(panel, Priority.ALWAYS);

        // Título del panel
        Label lblTitle = new Label("Definición de la Gramática");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Sección de símbolos terminales (usando GridPane para mejor responsividad)
        GridPane terminalGrid = new GridPane();
        terminalGrid.setHgap(5);
        terminalGrid.setVgap(5);
        terminalGrid.setAlignment(Pos.CENTER_LEFT);

        Label lblTerminal = new Label("Símbolo Terminal:");
        txtTerminal = new TextField();
        txtTerminal.setPrefWidth(50);
        Button btnAddTerminal = new Button("Agregar");
        btnAddTerminal.setOnAction(e -> handleAddTerminal());

        // Hacer que el campo de texto se expanda horizontalmente
        GridPane.setHgrow(txtTerminal, Priority.ALWAYS);

        terminalGrid.add(lblTerminal, 0, 0);
        terminalGrid.add(txtTerminal, 1, 0);
        terminalGrid.add(btnAddTerminal, 2, 0);

        // Sección de símbolos no terminales
        GridPane nonTerminalGrid = new GridPane();
        nonTerminalGrid.setHgap(5);
        nonTerminalGrid.setVgap(5);
        nonTerminalGrid.setAlignment(Pos.CENTER_LEFT);

        Label lblNonTerminal = new Label("Símbolo No Terminal:");
        txtNonTerminal = new TextField();
        txtNonTerminal.setPrefWidth(50);
        Button btnAddNonTerminal = new Button("Agregar");
        btnAddNonTerminal.setOnAction(e -> handleAddNonTerminal());

        // Hacer que el campo de texto se expanda horizontalmente
        GridPane.setHgrow(txtNonTerminal, Priority.ALWAYS);

        nonTerminalGrid.add(lblNonTerminal, 0, 0);
        nonTerminalGrid.add(txtNonTerminal, 1, 0);
        nonTerminalGrid.add(btnAddNonTerminal, 2, 0);

        // Sección de símbolo inicial
        GridPane startSymbolGrid = new GridPane();
        startSymbolGrid.setHgap(5);
        startSymbolGrid.setVgap(5);
        startSymbolGrid.setAlignment(Pos.CENTER_LEFT);

        Label lblStartSymbol = new Label("Símbolo Inicial:");
        cmbStartSymbol = new ComboBox<>();
        cmbStartSymbol.setPrefWidth(100);
        Button btnSetStartSymbol = new Button("Establecer");
        btnSetStartSymbol.setOnAction(e -> handleSetStartSymbol());

        // Hacer que el combobox se expanda horizontalmente
        GridPane.setHgrow(cmbStartSymbol, Priority.ALWAYS);

        startSymbolGrid.add(lblStartSymbol, 0, 0);
        startSymbolGrid.add(cmbStartSymbol, 1, 0);
        startSymbolGrid.add(btnSetStartSymbol, 2, 0);

        // Sección de producciones
        VBox productionBox = new VBox(10);

        Label lblProduction = new Label("Agregar Producción:");

        GridPane productionGrid = new GridPane();
        productionGrid.setHgap(5);
        productionGrid.setVgap(5);
        productionGrid.setAlignment(Pos.CENTER_LEFT);

        Label lblProductionNonTerminal = new Label("No Terminal:");
        cmbProductionNonTerminal = new ComboBox<>();
        cmbProductionNonTerminal.setPrefWidth(80);

        Label lblProductionArrow = new Label("→");

        txtProduction = new TextField();
        txtProduction.setPrefWidth(150);
        Button btnAddProduction = new Button("Agregar");
        btnAddProduction.setOnAction(e -> handleAddProduction());

        // Hacer que el campo de texto se expanda horizontalmente
        GridPane.setHgrow(txtProduction, Priority.ALWAYS);

        productionGrid.add(lblProductionNonTerminal, 0, 0);
        productionGrid.add(cmbProductionNonTerminal, 1, 0);
        productionGrid.add(lblProductionArrow, 2, 0);
        productionGrid.add(txtProduction, 3, 0);
        productionGrid.add(btnAddProduction, 4, 0);

        Label lblInfo = new Label("Nota: Use 'ε' para representar la producción vacía");
        lblInfo.setStyle("-fx-font-style: italic; -fx-font-size: 11;");

        productionBox.getChildren().addAll(lblProduction, productionGrid, lblInfo);

        // Visualización de la gramática definida
        Label lblGrammarDisplay = new Label("Gramática Definida:");
        txtGrammarDisplay = new TextArea();
        txtGrammarDisplay.setEditable(false);
        txtGrammarDisplay.setPrefHeight(150);

        // Hacer que el área de texto se expanda para ocupar el espacio disponible
        VBox.setVgrow(txtGrammarDisplay, Priority.ALWAYS);

        // Agregar todos los componentes al panel
        panel.getChildren().addAll(
                lblTitle,
                terminalGrid,
                nonTerminalGrid,
                startSymbolGrid,
                new Separator(),
                productionBox,
                new Separator(),
                lblGrammarDisplay,
                txtGrammarDisplay
        );

        return panel;
    }

    /**
     * Crea el panel para verificar palabras
     */
    private VBox createVerificationPanel() {
        VBox panel = new VBox(10);
        panel.setStyle("-fx-border-color: #CCCCCC; -fx-border-radius: 5; -fx-padding: 10;");

        // Título del panel
        Label lblTitle = new Label("Verificación de Palabras");
        lblTitle.setFont(Font.font("System", FontWeight.BOLD, 14));

        // Sección de entrada de palabra (usando GridPane para mejor responsividad)
        GridPane wordGrid = new GridPane();
        wordGrid.setHgap(5);
        wordGrid.setVgap(5);
        wordGrid.setAlignment(Pos.CENTER_LEFT);

        Label lblWord = new Label("Palabra a verificar:");
        txtWordToCheck = new TextField();
        txtWordToCheck.setPrefWidth(150);
        Button btnVerify = new Button("Verificar");
        btnVerify.setOnAction(e -> handleVerifyWord());

        // Hacer que el campo de texto se expanda horizontalmente
        GridPane.setHgrow(txtWordToCheck, Priority.ALWAYS);

        wordGrid.add(lblWord, 0, 0);
        wordGrid.add(txtWordToCheck, 1, 0);
        wordGrid.add(btnVerify, 2, 0);

        // Agregar componentes al panel
        panel.getChildren().addAll(
                lblTitle,
                wordGrid
        );

        return panel;
    }

    /**
     * Crea el panel de resultados con pestañas
     */
    private TabPane createResultsPanel() {
        TabPane tabPane = new TabPane();

        // Hacer que el panel de pestañas se expanda para ocupar el espacio disponible
        VBox.setVgrow(tabPane, Priority.ALWAYS);
        HBox.setHgrow(tabPane, Priority.ALWAYS);

        // Tab para el resultado de verificación
        Tab tabResult = new Tab("Resultado");
        tabResult.setClosable(false);

        txtResultDisplay = new TextArea();
        txtResultDisplay.setEditable(false);
        txtResultDisplay.setPrefHeight(600);

        // Envolver en BorderPane para que ocupe todo el espacio
        BorderPane resultPane = new BorderPane(txtResultDisplay);
        VBox.setVgrow(resultPane, Priority.ALWAYS);
        HBox.setHgrow(resultPane, Priority.ALWAYS);

        tabResult.setContent(resultPane);

        // Tab para el árbol de derivación específico
        Tab tabSpecificTree = new Tab("Árbol de Derivación Específico");
        tabSpecificTree.setClosable(false);

        treeViewSpecific = new TreeView<>();

        // Envolver en BorderPane para que ocupe todo el espacio
        BorderPane specificTreePane = new BorderPane(treeViewSpecific);
        VBox.setVgrow(specificTreePane, Priority.ALWAYS);
        HBox.setHgrow(specificTreePane, Priority.ALWAYS);

        tabSpecificTree.setContent(specificTreePane);

        // Tab para el árbol de derivación general
        Tab tabGeneralTree = new Tab("Árbol de Derivación General");
        tabGeneralTree.setClosable(false);

        treeViewGeneral = new TreeView<>();
        Button btnGenerateGeneralTree = new Button("Generar Árbol General");
        btnGenerateGeneralTree.setOnAction(e -> handleGenerateGeneralTree());

        VBox generalTreeBox = new VBox(10);
        generalTreeBox.setPadding(new Insets(10));
        generalTreeBox.getChildren().addAll(btnGenerateGeneralTree, treeViewGeneral);

        // Hacer que el área del árbol se expanda
        VBox.setVgrow(treeViewGeneral, Priority.ALWAYS);
        VBox.setVgrow(generalTreeBox, Priority.ALWAYS);

        tabGeneralTree.setContent(generalTreeBox);

        // Agregar pestañas al TabPane
        tabPane.getTabs().addAll(tabResult, tabSpecificTree, tabGeneralTree);

        return tabPane;
    }

    /**
     * Manejador para agregar un símbolo terminal
     */
    private void handleAddTerminal() {
        String terminal = txtTerminal.getText().trim();

        if (terminal.isEmpty()) {
            controller.showErrorAlert("Error", "El símbolo terminal no puede estar vacío.");
            return;
        }

        if (terminal.length() > 1) {
            controller.showErrorAlert("Error", "El símbolo terminal debe ser un solo carácter.");
            return;
        }

        if (controller.addTerminal(terminal)) {
            txtTerminal.clear();
            updateGrammarDisplay();
            controller.showAlert("Éxito", "Símbolo terminal '" + terminal + "' agregado correctamente.");
        } else {
            controller.showErrorAlert("Error", "El símbolo terminal ya existe o no es válido.");
        }
    }

    /**
     * Manejador para agregar un símbolo no terminal
     */
    private void handleAddNonTerminal() {
        String nonTerminal = txtNonTerminal.getText().trim();

        if (nonTerminal.isEmpty()) {
            controller.showErrorAlert("Error", "El símbolo no terminal no puede estar vacío.");
            return;
        }

        if (nonTerminal.length() > 1) {
            controller.showErrorAlert("Error", "El símbolo no terminal debe ser un solo carácter.");
            return;
        }

        if (controller.addNonTerminal(nonTerminal)) {
            txtNonTerminal.clear();

            // Actualizar ComboBox de símbolos iniciales y no terminales
            cmbStartSymbol.getItems().add(nonTerminal);
            cmbProductionNonTerminal.getItems().add(nonTerminal);

            updateGrammarDisplay();
            controller.showAlert("Éxito", "Símbolo no terminal '" + nonTerminal + "' agregado correctamente.");
        } else {
            controller.showErrorAlert("Error", "El símbolo no terminal ya existe o no es válido.");
        }
    }

    /**
     * Manejador para establecer el símbolo inicial
     */
    private void handleSetStartSymbol() {
        String startSymbol = cmbStartSymbol.getValue();

        if (startSymbol == null) {
            controller.showErrorAlert("Error", "Debe seleccionar un símbolo no terminal.");
            return;
        }

        if (controller.setStartSymbol(startSymbol)) {
            updateGrammarDisplay();
            controller.showAlert("Éxito", "Símbolo inicial establecido como '" + startSymbol + "'.");
        } else {
            controller.showErrorAlert("Error", "No se pudo establecer el símbolo inicial.");
        }
    }

    /**
     * Manejador para agregar una producción
     */
    private void handleAddProduction() {
        String nonTerminal = cmbProductionNonTerminal.getValue();
        String production = txtProduction.getText().trim();

        if (nonTerminal == null) {
            controller.showErrorAlert("Error", "Debe seleccionar un símbolo no terminal.");
            return;
        }

        if (production.isEmpty()) {
            controller.showErrorAlert("Error", "La producción no puede estar vacía.");
            return;
        }

        if (controller.addProduction(nonTerminal, production)) {
            txtProduction.clear();
            updateGrammarDisplay();
            controller.showAlert("Éxito", "Producción '" + nonTerminal + " → " + production + "' agregada correctamente.");
        } else {
            controller.showErrorAlert("Error", "La producción contiene símbolos no definidos en la gramática.");
        }
    }

    /**
     * Manejador para verificar una palabra
     */
    private void handleVerifyWord() {
        String word = txtWordToCheck.getText().trim();

        if (!controller.isGrammarValid()) {
            controller.showErrorAlert("Error", "La gramática no está completamente definida.");
            return;
        }

        if (!controller.validateMinimumRequirements()) {
            String report = controller.getRequirementsReport();
            controller.showErrorAlert("Requisitos no cumplidos",
                    "La gramática no cumple con los requisitos mínimos:\n" + report);
            return;
        }

        // Analizar la palabra
        DerivationResult result = controller.analyzeWord(word);

        // Mostrar el resultado
        txtResultDisplay.setText(result.generateDetailedReport());

        // Actualizar el árbol de derivación específico
        controller.populateDerivationTree(treeViewSpecific);

        // Mostrar mensaje según el resultado
        if (result.belongsToLanguage()) {
            controller.showAlert("Verificación completada",
                    "La palabra '" + word + "' SÍ pertenece al lenguaje generado por la gramática.");
        } else {
            controller.showAlert("Verificación completada",
                    "La palabra '" + word + "' NO pertenece al lenguaje generado por la gramática.");
        }
    }

    /**
     * Manejador para generar el árbol general
     */
    private void handleGenerateGeneralTree() {
        if (!controller.isGrammarValid()) {
            controller.showErrorAlert("Error", "La gramática no está completamente definida.");
            return;
        }

        // Solicitar profundidad máxima
        TextInputDialog dialog = new TextInputDialog("3");
        dialog.setTitle("Profundidad del árbol");
        dialog.setHeaderText("Ingrese la profundidad máxima para el árbol general");
        dialog.setContentText("Profundidad (1-10):");

        Optional<String> result = dialog.showAndWait();
        if (result.isPresent()) {
            try {
                int depth = Integer.parseInt(result.get());
                if (depth < 1 || depth > 10) {
                    controller.showErrorAlert("Error", "La profundidad debe estar entre 1 y 10.");
                    return;
                }

                // Generar el árbol
                controller.populateGeneralTree(treeViewGeneral, depth);
                controller.showAlert("Árbol generado", "Árbol general de la gramática generado correctamente.");

            } catch (NumberFormatException e) {
                controller.showErrorAlert("Error", "Debe ingresar un número válido.");
            }
        }
    }

    /**
     * Actualiza la visualización de la gramática en el TextArea
     */
    private void updateGrammarDisplay() {
        txtGrammarDisplay.setText(controller.getGrammarAsText());
    }

    /**
     * Método principal
     */
    public static void main(String[] args) {
        launch(args);
    }
}