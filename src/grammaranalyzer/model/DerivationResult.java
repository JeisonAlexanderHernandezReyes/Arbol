package grammaranalyzer.model;

/**
 * Clase que representa el resultado de una derivación
 * Contiene información sobre si una palabra pertenece al lenguaje
 * y los árboles de derivación asociados
 */
public class DerivationResult {
    private String word;             // Palabra analizada
    private boolean belongsToLanguage; // Indica si pertenece al lenguaje
    private TreeNode derivationTree; // Árbol de derivación específico para la palabra
    private String derivationSteps;  // Pasos de derivación en formato textual

    /**
     * Constructor
     * @param word Palabra analizada
     * @param belongsToLanguage Indica si pertenece al lenguaje
     */
    public DerivationResult(String word, boolean belongsToLanguage) {
        this.word = word;
        this.belongsToLanguage = belongsToLanguage;
        this.derivationTree = null;
        this.derivationSteps = "";
    }

    /**
     * Constructor completo
     * @param word Palabra analizada
     * @param belongsToLanguage Indica si pertenece al lenguaje
     * @param derivationTree Árbol de derivación
     * @param derivationSteps Pasos de derivación
     */
    public DerivationResult(String word, boolean belongsToLanguage,
                            TreeNode derivationTree, String derivationSteps) {
        this.word = word;
        this.belongsToLanguage = belongsToLanguage;
        this.derivationTree = derivationTree;
        this.derivationSteps = derivationSteps;
    }

    /**
     * Obtiene la palabra analizada
     * @return Palabra analizada
     */
    public String getWord() {
        return word;
    }

    /**
     * Indica si la palabra pertenece al lenguaje
     * @return true si pertenece, false en caso contrario
     */
    public boolean belongsToLanguage() {
        return belongsToLanguage;
    }

    /**
     * Obtiene el árbol de derivación
     * @return Árbol de derivación o null si no existe
     */
    public TreeNode getDerivationTree() {
        return derivationTree;
    }

    /**
     * Establece el árbol de derivación
     * @param derivationTree Árbol de derivación
     */
    public void setDerivationTree(TreeNode derivationTree) {
        this.derivationTree = derivationTree;
    }

    /**
     * Obtiene los pasos de derivación
     * @return Pasos de derivación en formato textual
     */
    public String getDerivationSteps() {
        return derivationSteps;
    }

    /**
     * Establece los pasos de derivación
     * @param derivationSteps Pasos de derivación
     */
    public void setDerivationSteps(String derivationSteps) {
        this.derivationSteps = derivationSteps;
    }

    /**
     * Representación en cadena del resultado
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();

        sb.append("Palabra analizada: ").append(word).append("\n");
        sb.append("Pertenencia al lenguaje: ");
        sb.append(belongsToLanguage ? "SÍ pertenece" : "NO pertenece").append("\n");

        if (belongsToLanguage && !derivationSteps.isEmpty()) {
            sb.append("\nPasos de derivación:\n");
            sb.append(derivationSteps);
        }

        return sb.toString();
    }

    /**
     * Genera un informe detallado del resultado
     * @return Informe en formato texto
     */
    public String generateDetailedReport() {
        StringBuilder sb = new StringBuilder();

        sb.append("=== INFORME DE ANÁLISIS ===\n\n");
        sb.append("Palabra analizada: \"").append(word).append("\"\n\n");

        sb.append("Resultado: ");
        if (belongsToLanguage) {
            sb.append("La palabra SÍ pertenece al lenguaje.\n\n");

            if (!derivationSteps.isEmpty()) {
                sb.append("Derivación:\n");
                sb.append(derivationSteps).append("\n");
            }

            if (derivationTree != null) {
                sb.append("Árbol de derivación horizontal:\n");
                sb.append(derivationTree.toStringHorizontal()).append("\n");
            }
        } else {
            sb.append("La palabra NO pertenece al lenguaje.\n\n");
            sb.append("No existe una secuencia de derivación que genere esta palabra.\n");
        }

        return sb.toString();
    }
}