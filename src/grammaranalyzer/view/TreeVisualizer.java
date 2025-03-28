package grammaranalyzer.view;

import grammaranalyzer.model.TreeNode;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.paint.Color;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

/**
 * Clase que implementa la visualización personalizada de árboles de derivación
 * en formato horizontal para una mejor presentación
 */
public class TreeVisualizer {

    // Constantes para la visualización
    private static final int NODE_HEIGHT = 30;
    private static final int NODE_SPACING = 50;
    private static final int LEVEL_SPACING = 80;

    /**
     * Genera un nodo visual JavaFX para un nodo del árbol
     * @param isTerminal Indica si es un símbolo terminal
     * @return Nodo visual JavaFX
     */
    private static Node createVisualNode(String text, boolean isTerminal) {
        Text textNode = new Text(text);
        textNode.setFont(Font.font("System", isTerminal ? FontWeight.NORMAL : FontWeight.BOLD, 12));
        textNode.setFill(isTerminal ? Color.BLUE : Color.BLACK);
        return textNode;
    }

    /**
     * Genera una visualización horizontal del árbol
     * @param root Raíz del árbol
     * @return Grupo JavaFX con la visualización
     */
    public static Group createHorizontalTreeView(TreeNode root) {
        Group group = new Group();
        visualizeTree(group, root, 0, 0, calculateTreeHeight(root));
        return group;
    }

    /**
     * Método recursivo para visualizar el árbol
     * @param group Grupo donde se agregarán los nodos visuales
     * @param node Nodo actual
     * @param level Nivel de profundidad
     * @param startY Posición Y inicial
     * @param levelHeight Altura del nivel
     * @return Posición Y final
     */
    private static double visualizeTree(Group group, TreeNode node, int level, double startY, int[] levelHeight) {
        if (node == null) {
            return startY;
        }

        // Crear nodo visual
        Node visualNode = createVisualNode(node.getValue(), node.isTerminal());

        // Posicionar el nodo
        double x = level * LEVEL_SPACING + 20;
        double y = startY + NODE_HEIGHT;

        visualNode.setLayoutX(x);
        visualNode.setLayoutY(y);

        // Agregar el nodo al grupo
        group.getChildren().add(visualNode);

        // Si no tiene hijos, retornar la posición Y para el siguiente nodo
        if (node.getChildren().isEmpty()) {
            return y + NODE_HEIGHT;
        }

        // Procesar los hijos recursivamente
        double childStartY = startY;
        double[] childEndPositions = new double[node.getChildren().size()];

        for (int i = 0; i < node.getChildren().size(); i++) {
            TreeNode child = node.getChildren().get(i);
            childEndPositions[i] = visualizeTree(group, child, level + 1, childStartY, levelHeight);
            childStartY = childEndPositions[i] + NODE_SPACING;
        }

        // Calcular la posición Y del nodo actual (centro de sus hijos)
        double middleY = (childEndPositions[0] + childEndPositions[childEndPositions.length - 1]) / 2 - NODE_HEIGHT;
        visualNode.setLayoutY(middleY);

        // Dibujar líneas a los hijos
        for (int i = 0; i < node.getChildren().size(); i++) {
            double childX = (level + 1) * LEVEL_SPACING + 20;
            double childY = childEndPositions[i] - NODE_HEIGHT * 1.5;

            Line line = new Line(x + 50, middleY + 6, childX, childY + 6);
            line.setStroke(Color.GRAY);
            group.getChildren().add(line);
        }

        return childStartY;
    }

    /**
     * Calcula la altura total del árbol
     * @param root Raíz del árbol
     * @return Arreglo con la altura de cada nivel
     */
    private static int[] calculateTreeHeight(TreeNode root) {
        if (root == null) {
            return new int[0];
        }

        // Encontrar la profundidad máxima
        int maxDepth = findMaxDepth(root);
        int[] levelHeight = new int[maxDepth + 1];

        // Calcular la cantidad de nodos por nivel
        countNodesPerLevel(root, 0, levelHeight);

        return levelHeight;
    }

    /**
     * Encuentra la profundidad máxima del árbol
     * @param node Nodo actual
     * @return Profundidad máxima
     */
    private static int findMaxDepth(TreeNode node) {
        if (node == null) {
            return 0;
        }

        int maxChildDepth = 0;
        for (TreeNode child : node.getChildren()) {
            int childDepth = findMaxDepth(child);
            if (childDepth > maxChildDepth) {
                maxChildDepth = childDepth;
            }
        }

        return maxChildDepth + 1;
    }

    /**
     * Cuenta la cantidad de nodos por nivel
     * @param node Nodo actual
     * @param level Nivel actual
     * @param levelHeight Arreglo con la altura de cada nivel
     */
    private static void countNodesPerLevel(TreeNode node, int level, int[] levelHeight) {
        if (node == null) {
            return;
        }

        levelHeight[level]++;

        for (TreeNode child : node.getChildren()) {
            countNodesPerLevel(child, level + 1, levelHeight);
        }
    }
}