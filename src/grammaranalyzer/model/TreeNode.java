package grammaranalyzer.model;

import java.util.*;

/**
 * Clase que representa un nodo en un árbol de derivación
 * Permite construir tanto árboles de derivación específicos para palabras
 * como árboles generales para representar la estructura de una gramática
 */
public class TreeNode {
    private String value;             // Valor del nodo
    private List<TreeNode> children;  // Hijos del nodo
    private boolean isTerminal;       // Indica si es un símbolo terminal

    /**
     * Constructor principal
     * @param value Valor del nodo
     * @param isTerminal Indica si es un símbolo terminal
     */
    public TreeNode(String value, boolean isTerminal) {
        this.value = value;
        this.children = new ArrayList<>();
        this.isTerminal = isTerminal;
    }

    /**
     * Constructor simplificado
     * @param value Valor del nodo
     */
    public TreeNode(String value) {
        this(value, false);
    }

    /**
     * Agrega un hijo al nodo
     * @param child Hijo a agregar
     */
    public void addChild(TreeNode child) {
        children.add(child);
    }

    /**
     * Crea y agrega un nuevo hijo al nodo
     * @param value Valor del hijo
     * @param isTerminal Indica si el hijo es un símbolo terminal
     * @return El nodo hijo creado
     */
    public TreeNode addChild(String value, boolean isTerminal) {
        TreeNode child = new TreeNode(value, isTerminal);
        children.add(child);
        return child;
    }

    /**
     * Obtiene el valor del nodo
     * @return Valor del nodo
     */
    public String getValue() {
        return value;
    }

    /**
     * Establece el valor del nodo
     * @param value Nuevo valor
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Obtiene la lista de hijos del nodo
     * @return Lista de hijos
     */
    public List<TreeNode> getChildren() {
        return Collections.unmodifiableList(children);
    }

    /**
     * Indica si el nodo es un símbolo terminal
     * @return true si es terminal, false en caso contrario
     */
    public boolean isTerminal() {
        return isTerminal;
    }

    /**
     * Indica si el nodo es una hoja (no tiene hijos)
     * @return true si es hoja, false en caso contrario
     */
    public boolean isLeaf() {
        return children.isEmpty();
    }

    /**
     * Obtiene la representación de árbol en formato de cadena horizontal
     * @return Representación del árbol
     */
    public String toStringHorizontal() {
        StringBuilder sb = new StringBuilder();
        buildStringHorizontal(sb, "", "");
        return sb.toString();
    }

    /**
     * Construye la representación horizontal del árbol
     * @param sb StringBuilder para construir la cadena
     * @param prefix Prefijo para la línea actual
     * @param childrenPrefix Prefijo para los hijos
     */
    private void buildStringHorizontal(StringBuilder sb, String prefix, String childrenPrefix) {
        sb.append(prefix);
        sb.append(value);
        sb.append('\n');

        for (Iterator<TreeNode> it = children.iterator(); it.hasNext();) {
            TreeNode child = it.next();
            if (it.hasNext()) {
                child.buildStringHorizontal(sb, childrenPrefix + "├── ", childrenPrefix + "│   ");
            } else {
                child.buildStringHorizontal(sb, childrenPrefix + "└── ", childrenPrefix + "    ");
            }
        }
    }

    /**
     * Convierte el árbol a una estructura compatible con JavaFX TreeView
     * Este método puede ser adaptado según la implementación específica
     * @return Objeto que representa el nodo para TreeView
     */
    public javafx.scene.control.TreeItem<String> toTreeItem() {
        javafx.scene.control.TreeItem<String> item = new javafx.scene.control.TreeItem<>(value);

        // Recursivamente convertir los hijos
        for (TreeNode child : children) {
            item.getChildren().add(child.toTreeItem());
        }

        // Expandir por defecto para mejor visualización
        item.setExpanded(true);

        return item;
    }
}