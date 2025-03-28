package grammaranalyzer.controller;

import grammaranalyzer.model.Grammar;
import grammaranalyzer.model.TreeNode;
import grammaranalyzer.model.DerivationResult;

import java.util.*;

/**
 * Analizador de gramáticas que implementa los algoritmos para verificar
 * si una palabra pertenece al lenguaje y para generar árboles de derivación
 */
public class GrammarParser {
    private Grammar grammar;
    private int maxDepth = 10; // Profundidad máxima para evitar recursión infinita

    /**
     * Constructor
     * @param grammar Gramática a analizar
     */
    public GrammarParser(Grammar grammar) {
        this.grammar = grammar;
    }

    /**
     * Actualiza la gramática del analizador
     * @param grammar Nueva gramática
     */
    public void setGrammar(Grammar grammar) {
        this.grammar = grammar;
    }

    /**
     * Verifica si una palabra pertenece al lenguaje generado por la gramática
     * y genera el árbol de derivación correspondiente
     *
     * @param word Palabra a verificar
     * @return Resultado de la derivación
     */
    public DerivationResult analyzeWord(String word) {
        // Verificar que la gramática esté correctamente definida
        if (!grammar.isValid()) {
            return new DerivationResult(word, false);
        }

        // Inicializar resultado
        DerivationResult result = new DerivationResult(word, false);

        // Intentar derivar la palabra
        try {
            // Para gramáticas regulares, utilizaremos un enfoque de derivación directa
            // Para gramáticas más complejas se podría implementar CYK u otros algoritmos

            StringBuilder steps = new StringBuilder();
            TreeNode rootNode = new TreeNode(grammar.getStartSymbol());

            boolean canDerive = deriveWord(word, grammar.getStartSymbol(), rootNode, steps, 0);

            // Actualizar el resultado
            result = new DerivationResult(word, canDerive, rootNode, steps.toString());

        } catch (Exception e) {
            System.err.println("Error al analizar la palabra: " + e.getMessage());
            e.printStackTrace();
        }

        return result;
    }

    /**
     * Método recursivo para intentar derivar una palabra
     *
     * @param targetWord Palabra objetivo
     * @param currentString Cadena actual en la derivación
     * @param parentNode Nodo padre en el árbol de derivación
     * @param steps Pasos de derivación (output)
     * @param depth Profundidad actual de recursión
     * @return true si se puede derivar la palabra, false en caso contrario
     */
    private boolean deriveWord(String targetWord, String currentString,
                               TreeNode parentNode, StringBuilder steps, int depth) {
        // Controlar profundidad máxima para evitar recursión infinita
        if (depth > maxDepth) {
            return false;
        }

        // Si la cadena actual ya excede la longitud de la palabra objetivo, fallar
        if (currentString.length() > targetWord.length()) {
            return false;
        }

        // Si hemos llegado a la palabra objetivo, éxito
        if (currentString.equals(targetWord)) {
            steps.append(depth).append(". ").append(currentString).append(" (Éxito!)\n");
            return true;
        }

        // Registrar paso actual
        steps.append(depth).append(". ").append(currentString).append("\n");

        // Intentar todas las posibles derivaciones
        // Para cada símbolo no terminal en la cadena actual
        for (int i = 0; i < currentString.length(); i++) {
            String symbol = currentString.substring(i, i + 1);

            // Si es un símbolo no terminal, intentar aplicar producciones
            if (grammar.getNonTerminals().contains(symbol)) {
                // Obtener todas las producciones para este no terminal
                List<String> productions = grammar.getProductionsFor(symbol);

                // Probar cada producción
                for (String production : productions) {
                    // Construir la nueva cadena reemplazando el no terminal por su producción
                    String prefix = currentString.substring(0, i);
                    String suffix = currentString.substring(i + 1);

                    // Manejar épsilon (producción vacía)
                    String newString;
                    if (production.equals("ε")) {
                        newString = prefix + suffix;
                    } else {
                        newString = prefix + production + suffix;
                    }

                    // Crear nodo para esta derivación
                    TreeNode derivationNode = new TreeNode(symbol + " -> " + production);
                    parentNode.addChild(derivationNode);

                    // Intentar derivar recursivamente con la nueva cadena
                    if (deriveWord(targetWord, newString, derivationNode, steps, depth + 1)) {
                        // Si tuvimos éxito, propagar el éxito hacia arriba
                        return true;
                    }
                }
            }
        }

        // Si llegamos aquí, ninguna derivación tuvo éxito
        return false;
    }

    /**
     * Genera el árbol general de la gramática hasta una profundidad máxima
     *
     * @param maxDepth Profundidad máxima del árbol
     * @return Raíz del árbol general
     */
    public TreeNode generateGeneralTree(int maxDepth) {
        // Verificar que la gramática esté correctamente definida
        if (!grammar.isValid()) {
            return new TreeNode("Gramática inválida");
        }

        // Crear nodo raíz con el símbolo inicial
        TreeNode root = new TreeNode(grammar.getStartSymbol());

        // Generar el árbol de forma recursiva
        expandGeneralTree(root, grammar.getStartSymbol(), new HashSet<>(), 0, maxDepth);

        return root;
    }

    /**
     * Método recursivo para expandir el árbol general de la gramática
     *
     * @param parentNode Nodo padre actual
     * @param symbol Símbolo a expandir
     * @param visited Conjunto de símbolos ya visitados (para evitar ciclos)
     * @param currentDepth Profundidad actual
     * @param maxDepth Profundidad máxima
     */
    private void expandGeneralTree(TreeNode parentNode, String symbol,
                                   Set<String> visited, int currentDepth, int maxDepth) {
        // Control de profundidad y ciclos
        if (currentDepth >= maxDepth || visited.contains(symbol)) {
            return;
        }

        // Marcar como visitado para esta rama
        visited.add(symbol);

        // Si es un símbolo no terminal, expandir
        if (grammar.getNonTerminals().contains(symbol)) {
            // Obtener producciones para este símbolo
            List<String> productions = grammar.getProductionsFor(symbol);

            // Para cada producción
            for (String production : productions) {
                // Crear nodo para esta producción
                TreeNode productionNode = new TreeNode(symbol + " -> " + production);
                parentNode.addChild(productionNode);

                // Si la producción es épsilon, no seguir expandiendo
                if (production.equals("ε")) {
                    continue;
                }

                // Para cada símbolo en la producción
                for (int i = 0; i < production.length(); i++) {
                    String nextSymbol = production.substring(i, i + 1);

                    // Crear nodo hijo para este símbolo
                    boolean isTerminal = grammar.getTerminals().contains(nextSymbol);
                    TreeNode symbolNode = new TreeNode(nextSymbol, isTerminal);
                    productionNode.addChild(symbolNode);

                    // Si es no terminal, expandir recursivamente
                    if (!isTerminal) {
                        // Clonar conjunto de visitados para esta rama
                        Set<String> newVisited = new HashSet<>(visited);
                        expandGeneralTree(symbolNode, nextSymbol, newVisited, currentDepth + 1, maxDepth);
                    }
                }
            }
        }

        // Remover de visitados al salir de esta rama
        visited.remove(symbol);
    }
}