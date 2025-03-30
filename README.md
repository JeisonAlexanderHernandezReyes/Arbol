# Grammar Analyzer Application

## Descripción
La aplicación "Grammar Analyzer" (Analizador de Gramáticas) es una herramienta educativa que permite a los usuarios definir gramáticas formales y verificar si palabras específicas pertenecen al lenguaje generado por dichas gramáticas. La aplicación proporciona una interfaz gráfica intuitiva y responsiva desarrollada con JavaFX.

## Características Principales

- **Definición de Gramáticas**: Permite ingresar los componentes básicos de una gramática formal G = ({ΣT},{ΣNT},S,P).
  - Símbolos terminales (ΣT)
  - Símbolos no terminales (ΣNT)
  - Símbolo inicial (S)
  - Producciones (P)

- **Verificación de Palabras**: Comprueba si una palabra ingresada pertenece al lenguaje generado por la gramática y muestra:
  - El resultado de pertenencia o no pertenencia
  - Los pasos de derivación utilizados
  - El árbol de derivación específico para la palabra

- **Visualización de Árboles de Derivación**:
  - Árbol de derivación específico para la palabra ingresada (formato horizontal)
  - Árbol de derivación general de la gramática (con profundidad configurable)

- **Interfaz Adaptativa**: La interfaz se ajusta automáticamente a diferentes tamaños de pantalla.

## Requisitos del Sistema

- **Java**: JDK 11 o superior
- **JavaFX**: Incluido en el JDK 11 (para versiones posteriores puede requerir instalación separada)
- **Sistema Operativo**: Windows, macOS o Linux

## Información Técnica

| Aspecto | Detalle |
|---------|---------|
| Lenguaje de Programación | Java 11 |
| Framework de Interfaz | JavaFX |
| Patrón de Diseño | Modelo-Vista-Controlador (MVC) |
| Sistema Operativo de Desarrollo | Windows 10 |
| IDE Recomendado | IntelliJ IDEA o Eclipse |

## Estructura del Proyecto

```
grammaranalyzer/
│
├── controller/
│   ├── GrammarController.java      # Controlador principal
│   └── GrammarParser.java          # Analizador de gramáticas
│
├── model/
│   ├── Grammar.java                # Modelo de gramática formal
│   ├── TreeNode.java               # Nodo para árboles de derivación
│   └── DerivationResult.java       # Resultado de análisis de palabras
│
├── view/
│   └── TreeVisualizer.java         # Visualizador personalizado de árboles
│
└── GrammarAnalyzerApp.java         # Clase principal con la interfaz gráfica
```

## Guía de Uso

### 1. Definición de la Gramática

1. **Agregar Símbolos Terminales**:
   - Ingrese un carácter en el campo "Símbolo Terminal"
   - Haga clic en "Agregar"

2. **Agregar Símbolos No Terminales**:
   - Ingrese un carácter en el campo "Símbolo No Terminal"
   - Haga clic en "Agregar"

3. **Establecer Símbolo Inicial**:
   - Seleccione un símbolo no terminal del menú desplegable
   - Haga clic en "Establecer"

4. **Agregar Producciones**:
   - Seleccione un símbolo no terminal del menú desplegable
   - Ingrese la producción (use 'ε' para representar la producción vacía)
   - Haga clic en "Agregar"

### 2. Verificación de Palabras

1. **Ingrese la Palabra**:
   - Escriba la palabra a verificar
   - Haga clic en "Verificar"

2. **Visualización de Resultados**:
   - La pestaña "Resultado" mostrará si la palabra pertenece al lenguaje
   - La pestaña "Árbol de Derivación Específico" mostrará el árbol para la palabra verificada
   - La pestaña "Árbol de Derivación General" permitirá generar el árbol completo de la gramática

## Requisitos Mínimos de la Gramática

La aplicación requiere que la gramática cumpla con los siguientes requisitos mínimos:
- Al menos 2 símbolos terminales
- Al menos 3 símbolos no terminales
- Al menos 3 producciones

## Instrucciones de Ejecución

### Usando IntelliJ IDEA:
1. Abra el proyecto en IntelliJ IDEA
2. Configure el JDK (Java 11 o superior)
3. Ejecute la clase `grammaranalyzer.GrammarAnalyzerApp`

### Usando el Script de Ejecución:
1. Compile el proyecto con `javac`
2. Use el script proporcionado `run.sh` (Linux/Mac) o `run.bat` (Windows)

## Algoritmo de Verificación

La aplicación utiliza un algoritmo recursivo para verificar si una palabra pertenece al lenguaje:
1. Comienza con el símbolo inicial de la gramática
2. Aplica recursivamente las producciones disponibles
3. Controla la profundidad máxima para evitar recursión infinita
4. Construye simultáneamente el árbol de derivación

## Ejemplo de Uso

### Gramática para el lenguaje a^n b^n (n ≥ 1):

- **Terminales**: a, b
- **No Terminales**: S, A, B
- **Símbolo Inicial**: S
- **Producciones**:
  - S → aAb
  - A → aAb
  - A → ε

### Verificación de palabras:
- "ab" - Pertenece al lenguaje
- "aabb" - Pertenece al lenguaje
- "aaabbb" - Pertenece al lenguaje
- "aabbb" - No pertenece al lenguaje
- "abb" - No pertenece al lenguaje

## Limitaciones

- El analizador está optimizado para gramáticas regulares y libres de contexto simples
- Para gramáticas más complejas, la profundidad de derivación está limitada para prevenir problemas de rendimiento

## Autor

[Tu Nombre]

## Licencia

Este proyecto es parte de un trabajo académico y está sujeto a los términos especificados por [Tu Institución Educativa].
