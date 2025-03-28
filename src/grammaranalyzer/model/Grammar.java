package grammaranalyzer.model;

import java.util.*;

/**
 * Clase que representa una gramática formal G = ({ΣT},{ΣNT},S,P)
 * donde:
 * - ΣT: conjunto de símbolos terminales
 * - ΣNT: conjunto de símbolos no terminales
 * - S: símbolo inicial
 * - P: conjunto de producciones
 */
public class Grammar {
    private Set<String> terminals;         // Símbolos terminales (ΣT)
    private Set<String> nonTerminals;      // Símbolos no terminales (ΣNT)
    private String startSymbol;            // Símbolo inicial (S)
    private Map<String, List<String>> productions; // Producciones (P)

    /**
     * Constructor por defecto
     */
    public Grammar() {
        terminals = new HashSet<>();
        nonTerminals = new HashSet<>();
        startSymbol = "";
        productions = new HashMap<>();
    }

    /**
     * Agrega un símbolo terminal al conjunto ΣT
     * @param terminal Símbolo terminal a agregar
     * @return true si se agregó correctamente, false si ya existía
     */
    public boolean addTerminal(String terminal) {
        return terminals.add(terminal);
    }

    /**
     * Agrega un símbolo no terminal al conjunto ΣNT
     * @param nonTerminal Símbolo no terminal a agregar
     * @return true si se agregó correctamente, false si ya existía
     */
    public boolean addNonTerminal(String nonTerminal) {
        return nonTerminals.add(nonTerminal);
    }

    /**
     * Establece el símbolo inicial S
     * @param symbol Símbolo inicial
     * @return true si es un no terminal válido, false en caso contrario
     */
    public boolean setStartSymbol(String symbol) {
        if (nonTerminals.contains(symbol)) {
            startSymbol = symbol;
            return true;
        }
        return false;
    }

    /**
     * Agrega una producción a la gramática
     * @param nonTerminal Símbolo no terminal (lado izquierdo)
     * @param production Producción (lado derecho)
     * @return true si se agregó correctamente, false si el no terminal no existe
     */
    public boolean addProduction(String nonTerminal, String production) {
        if (!nonTerminals.contains(nonTerminal)) {
            return false;
        }

        // Validar que la producción solo contenga símbolos válidos
        if (!isValidProduction(production)) {
            return false;
        }

        if (!productions.containsKey(nonTerminal)) {
            productions.put(nonTerminal, new ArrayList<>());
        }

        productions.get(nonTerminal).add(production);
        return true;
    }

    /**
     * Verifica si una producción contiene solo símbolos válidos
     * @param production Producción a validar
     * @return true si la producción es válida, false en caso contrario
     */
    private boolean isValidProduction(String production) {
        // Caso especial: producción vacía (epsilon)
        if (production.equals("ε")) {
            return true;
        }

        // Verificar cada carácter de la producción
        for (int i = 0; i < production.length(); i++) {
            String symbol = production.substring(i, i + 1);
            if (!terminals.contains(symbol) && !nonTerminals.contains(symbol)) {
                return false;
            }
        }

        return true;
    }

    /**
     * Verifica si la gramática está correctamente definida
     * @return true si la gramática es válida, false en caso contrario
     */
    public boolean isValid() {
        return !terminals.isEmpty() &&
                !nonTerminals.isEmpty() &&
                !startSymbol.isEmpty() &&
                !productions.isEmpty();
    }

    // Getters

    public Set<String> getTerminals() {
        return new HashSet<>(terminals);
    }

    public Set<String> getNonTerminals() {
        return new HashSet<>(nonTerminals);
    }

    public String getStartSymbol() {
        return startSymbol;
    }

    public Map<String, List<String>> getProductions() {
        // Retornar una copia profunda para evitar modificaciones externas
        Map<String, List<String>> copy = new HashMap<>();
        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            copy.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        return copy;
    }

    /**
     * Obtiene las producciones de un símbolo no terminal específico
     * @param nonTerminal Símbolo no terminal
     * @return Lista de producciones o lista vacía si no hay producciones
     */
    public List<String> getProductionsFor(String nonTerminal) {
        return productions.getOrDefault(nonTerminal, new ArrayList<>());
    }

    /**
     * Representación en cadena de la gramática
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("G = ({");
        sb.append(String.join(", ", terminals));
        sb.append("}, {");
        sb.append(String.join(", ", nonTerminals));
        sb.append("}, ");
        sb.append(startSymbol);
        sb.append(", P)\n\nDonde P contiene:\n");

        for (Map.Entry<String, List<String>> entry : productions.entrySet()) {
            for (String production : entry.getValue()) {
                sb.append(entry.getKey()).append(" -> ").append(production).append("\n");
            }
        }

        return sb.toString();
    }
}