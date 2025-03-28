package grammaranalyzer.controller;

import grammaranalyzer.model.Grammar;
import grammaranalyzer.model.TreeNode;
import grammaranalyzer.model.DerivationResult;

import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;

/**
 * Controlador principal que coordina la interacción entre la interfaz de usuario
 * y los modelos de datos y lógica de la aplicación
 */
public class GrammarController {
    private Grammar grammar;
    private GrammarParser parser;
    private DerivationResult lastResult;

    /**
     * Constructor
     */
    public GrammarController() {
        grammar = new Grammar();
        parser = new GrammarParser(grammar);
        lastResult = null;
    }

    /**
     * Agrega un símbolo terminal a la gramática
     * @param terminal Símbolo terminal a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addTerminal(String terminal) {
        return grammar.addTerminal(terminal);
    }

    /**
     * Agrega un símbolo no terminal a la gramática
     * @param nonTerminal Símbolo no terminal a agregar
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addNonTerminal(String nonTerminal) {
        return grammar.addNonTerminal(nonTerminal);
    }

    /**
     * Establece el símbolo inicial de la gramática
     * @param symbol Símbolo inicial
     * @return true si se estableció correctamente, false en caso contrario
     */
    public boolean setStartSymbol(String symbol) {
        return grammar.setStartSymbol(symbol);
    }

    /**
     * Agrega una producción a la gramática
     * @param nonTerminal Símbolo no terminal (lado izquierdo)
     * @param production Producción (lado derecho)
     * @return true si se agregó correctamente, false en caso contrario
     */
    public boolean addProduction(String nonTerminal, String production) {
        return grammar.addProduction(nonTerminal, production);
    }

    /**
     * Verifica si la gramática actual es válida
     * @return true si es válida, false en caso contrario
     */
    public boolean isGrammarValid() {
        return grammar.isValid();
    }

    /**
     * Obtiene la representación en texto de la gramática
     * @return Gramática en formato de texto
     */
    public String getGrammarAsText() {
        return grammar.toString();
    }

    /**
     * Verifica si una palabra pertenece al lenguaje generado por la gramática
     * @param word Palabra a verificar
     * @return Resultado de la derivación
     */
    public DerivationResult analyzeWord(String word) {
        if (!isGrammarValid()) {
            return new DerivationResult(word, false);
        }

        lastResult = parser.analyzeWord(word);
        return lastResult;
    }

    /**
     * Obtiene el resultado del último análisis
     * @return Último resultado o null si no hay análisis previo
     */
    public DerivationResult getLastResult() {
        return lastResult;
    }

    /**
     * Genera el árbol de derivación específico para la última palabra analizada
     * @param treeView Control TreeView donde se mostrará el árbol
     */
    public void populateDerivationTree(TreeView<String> treeView) {
        if (lastResult != null && lastResult.belongsToLanguage() && lastResult.getDerivationTree() != null) {
            TreeItem<String> root = lastResult.getDerivationTree().toTreeItem();
            treeView.setRoot(root);
        } else {
            treeView.setRoot(null);
        }
    }

    /**
     * Genera el árbol general de la gramática
     * @param treeView Control TreeView donde se mostrará el árbol
     * @param maxDepth Profundidad máxima del árbol
     */
    public void populateGeneralTree(TreeView<String> treeView, int maxDepth) {
        if (isGrammarValid()) {
            TreeNode generalTreeRoot = parser.generateGeneralTree(maxDepth);
            TreeItem<String> root = generalTreeRoot.toTreeItem();
            treeView.setRoot(root);
        } else {
            treeView.setRoot(null);
        }
    }

    /**
     * Muestra una alerta informativa
     * @param title Título de la alerta
     * @param message Mensaje a mostrar
     */
    public void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Muestra una alerta de error
     * @param title Título de la alerta
     * @param message Mensaje a mostrar
     */
    public void showErrorAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    /**
     * Valida que la gramática cumpla con los requisitos mínimos:
     * - Al menos 2 símbolos terminales
     * - Al menos 3 símbolos no terminales
     * - Al menos 3 producciones
     *
     * @return true si cumple los requisitos, false en caso contrario
     */
    public boolean validateMinimumRequirements() {
        int terminalsCount = grammar.getTerminals().size();
        int nonTerminalsCount = grammar.getNonTerminals().size();

        // Contar producciones
        int productionsCount = 0;
        for (String nonTerminal : grammar.getNonTerminals()) {
            productionsCount += grammar.getProductionsFor(nonTerminal).size();
        }

        return terminalsCount >= 2 && nonTerminalsCount >= 3 && productionsCount >= 3;
    }

    /**
     * Obtiene un informe de los requisitos mínimos
     * @return Informe en texto
     */
    public String getRequirementsReport() {
        StringBuilder sb = new StringBuilder();

        int terminalsCount = grammar.getTerminals().size();
        int nonTerminalsCount = grammar.getNonTerminals().size();

        // Contar producciones
        int productionsCount = 0;
        for (String nonTerminal : grammar.getNonTerminals()) {
            productionsCount += grammar.getProductionsFor(nonTerminal).size();
        }

        sb.append("Requisitos mínimos:\n");
        sb.append("- Símbolos terminales: ").append(terminalsCount).append(" de 2 mínimo");
        sb.append(terminalsCount >= 2 ? " ✓\n" : " ✗\n");

        sb.append("- Símbolos no terminales: ").append(nonTerminalsCount).append(" de 3 mínimo");
        sb.append(nonTerminalsCount >= 3 ? " ✓\n" : " ✗\n");

        sb.append("- Producciones: ").append(productionsCount).append(" de 3 mínimo");
        sb.append(productionsCount >= 3 ? " ✓\n" : " ✗\n");

        return sb.toString();
    }
}