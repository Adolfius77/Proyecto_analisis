package implementaciones;

import java.util.*;

/**
 * Representa un grafo implementado mediante una matriz de adyacencia.
 * La clase permite crear grafos dirigidos/no dirigidos y ponderados/no ponderados.
 * Utiliza un mapa para asociar los nombres de los vértices (String) con sus índices (Integer) en la matriz,
 * lo que facilita el acceso y la manipulación.
 * La capacidad inicial del grafo es fija.
 */
public class Grafo {

    /** Matriz que almacena los pesos de las aristas. Un 0 indica la ausencia de arista. */
    private int[][] matrizAdyacencia;
    /** Mapea el nombre de cada vértice a su índice numérico en la matriz. */
    private final Map<String, Integer> indices;
    /** Mapea cada índice a su nombre de vértice correspondiente. Permite recuperar nombres a partir de índices. */
    private final List<String> vertices;
    /** Número actual de vértices en el grafo. */
    private int numeroVertices;
    /** Capacidad máxima inicial de vértices que el grafo puede contener. */
    private final int capacidad = 10;
    /** Define si el grafo es dirigido. */
    private final boolean esDirigido;
    /** Define si las aristas del grafo tienen peso. */
    private final boolean esPonderado;

    /**
     * Construye un nuevo grafo, inicializando la matriz de adyacencia y las estructuras de mapeo.
     *
     * @param esDirigido  Si es true, el grafo será dirigido; de lo contrario, no dirigido.
     * @param esPonderado Si es true, el grafo será ponderado; de lo contrario, no ponderado.
     */
    public Grafo(boolean esDirigido, boolean esPonderado) {
        this.esDirigido = esDirigido;
        this.esPonderado = esPonderado;
        this.indices = new HashMap<>();
        this.vertices = new ArrayList<>();
        this.matrizAdyacencia = new int[capacidad][capacidad];
        this.numeroVertices = 0;
    }

    /**
     * Devuelve si el grafo es dirigido.
     * @return true si el grafo es dirigido, false en caso contrario.
     */
    public boolean esDirigido() {
        return esDirigido;
    }

    /**
     * Devuelve si el grafo es ponderado.
     * @return true si el grafo es ponderado, false en caso contrario.
     */
    public boolean esPonderado() {
        return esPonderado;
    }

    /**
     * Inserta un nuevo vértice en el grafo.
     * No se pueden añadir más vértices si se alcanza la capacidad máxima.
     *
     * @param vertice El nombre del vértice a insertar.
     * @return Un mensaje indicando si el vértice fue insertado, si ya existía o si se alcanzó la capacidad máxima.
     */
    public String insertarVertice(String vertice) {
        if (indices.containsKey(vertice)) {
            return "Info: El vertices '" + vertice + "' ya existía.";
        }
        if (numeroVertices >= capacidad) {
            return "Error: Se ha alcanzado la capacidad maxima de vertices.";
        }
        indices.put(vertice, numeroVertices);
        vertices.add(vertice);
        numeroVertices++;
        return "Vértice '" + vertice + "' insertado con exito.";
    }

    /**
     * Elimina un vértice del grafo. Esta es una operación costosa, ya que implica reconstruir la matriz
     * de adyacencia y actualizar los índices de todos los vértices posteriores al eliminado.
     *
     * @param vertice El nombre del vértice a eliminar.
     * @return Un mensaje indicando si el vértice fue eliminado o si no existía.
     */
    public String eliminarVertice(String vertice) {
        Integer indice = indices.get(vertice);
        if (indice == null) {
            return "Error: Vertice '" + vertice + "' no existe.";
        }

        int n = numeroVertices;
        int[][] nuevaMatriz = new int[n - 1][n - 1];

        // Copiar la matriz omitiendo la fila y columna del vértice eliminado
        for (int i = 0, oldI = 0; i < n - 1; i++, oldI++) {
            if (oldI == indice) oldI++; // Salta la fila del vértice eliminado
            for (int j = 0, oldJ = 0; j < n - 1; j++, oldJ++) {
                if (oldJ == indice) oldJ++; // Salta la columna del vértice eliminado
                nuevaMatriz[i][j] = matrizAdyacencia[oldI][oldJ];
            }
        }

        // Actualizar las estructuras de mapeo
        vertices.remove(indice.intValue());
        indices.remove(vertice);
        for (int i = indice; i < vertices.size(); i++) {
            indices.put(vertices.get(i), i);
        }

        matrizAdyacencia = nuevaMatriz;
        numeroVertices--;

        return "Vertice '" + vertice + "' eliminado.";
    }

    /**
     * Inserta una arista entre dos vértices. Si el grafo no es ponderado, el peso se establece en 1.
     * Si no es dirigido, se añade una arista simétrica.
     *
     * @param origen  El vértice de origen.
     * @param destino El vértice de destino.
     * @param peso    El peso de la arista (solo para grafos ponderados).
     * @return Un mensaje de estado sobre la operación.
     */
    public String insertarArista(String origen, String destino, int peso) {
        Integer indiceOrigen = indices.get(origen);
        Integer indiceDestino = indices.get(destino);

        if (indiceOrigen == null || indiceDestino == null) {
            return String.format("Error: Vértice origen '%s' o destino '%s' no existe.", origen, destino);
        }

        int pesoReal = esPonderado ? peso : 1;
        matrizAdyacencia[indiceOrigen][indiceDestino] = pesoReal;

        String resultado = "Arista de '" + origen + "' a '" + destino + "' con peso " + pesoReal + " insertada.";

        if (!esDirigido) {
            matrizAdyacencia[indiceDestino][indiceOrigen] = pesoReal;
            resultado += "\n   (Grafo no dirigido) Arista de '" + destino + "' a '" + origen + "' insertada.";
        }
        return resultado;
    }

    /**
     * Elimina una arista entre dos vértices, estableciendo su valor a 0 en la matriz.
     * Si el grafo no es dirigido, elimina también la arista simétrica.
     *
     * @param origen  El vértice de origen.
     * @param destino El vértice de destino.
     * @return Un mensaje indicando si la arista fue eliminada o no se encontró.
     */
    public String eliminarArista(String origen, String destino) {
        Integer indiceOrigen = indices.get(origen);
        Integer indiceDestino = indices.get(destino);

        if (indiceOrigen == null || indiceDestino == null) {
            return "Error: No se encontro la arista porque uno de los vertices no existe.";
        }

        boolean removido = matrizAdyacencia[indiceOrigen][indiceDestino] != 0;
        if (removido) {
            matrizAdyacencia[indiceOrigen][indiceDestino] = 0;
            if (!esDirigido) {
                matrizAdyacencia[indiceDestino][indiceOrigen] = 0;
            }
        }

        return removido ? "Arista entre '" + origen + "' y '" + destino + "' eliminada." : "Error: No se encontró la arista.";
    }

    /**
     * Actualiza el peso de una arista existente. Solo aplicable a grafos ponderados.
     *
     * @param origen    El vértice de origen.
     * @param destino   El vértice de destino.
     * @param nuevoPeso El nuevo peso para la arista.
     * @return Un mensaje de estado sobre la operación.
     */
    public String actualizarPonderacion(String origen, String destino, int nuevoPeso) {
        if (!esPonderado) {
            return "Operacion no permitida: el grafo no es ponderado.";
        }
        Integer indiceOrigen = indices.get(origen);
        Integer indiceDestino = indices.get(destino);

        if (indiceOrigen == null || indiceDestino == null || matrizAdyacencia[indiceOrigen][indiceDestino] == 0) {
            return "Error: No se encontro la arista.";
        }

        matrizAdyacencia[indiceOrigen][indiceDestino] = nuevoPeso;
        if (!esDirigido) {
            matrizAdyacencia[indiceDestino][indiceOrigen] = nuevoPeso;
        }
        return "Ponderacion de '" + origen + "' a '" + destino + "' actualizada a " + nuevoPeso + ".";
    }

    /**
     * Calcula y devuelve el grado de un vértice.
     * - En grafos dirigidos: calcula grado de entrada y de salida.
     * - En grafos no dirigidos: calcula el grado total.
     *
     * @param vertice El nombre del vértice.
     * @return Una cadena con la descripción del grado del vértice.
     */
    public String obtenerGradoVertice(String vertice) {
        Integer indice = indices.get(vertice);
        if (indice == null) {
            return "Error: El vertice '" + vertice + "' no existe.";
        }

        if (esDirigido) {
            int gradoSalida = 0;
            for (int j = 0; j < numeroVertices; j++) {
                if (matrizAdyacencia[indice][j] != 0) gradoSalida++;
            }
            int gradoEntrada = 0;
            for (int i = 0; i < numeroVertices; i++) {
                if (matrizAdyacencia[i][indice] != 0) gradoEntrada++;
            }
            return "Vertice '" + vertice + "': Grado de Salida = " + gradoSalida + ", Grado de Entrada = " + gradoEntrada;
        } else {
            int grado = 0;
            for (int j = 0; j < numeroVertices; j++) {
                if (matrizAdyacencia[indice][j] != 0) grado++;
            }
            return "Vertice '" + vertice + "': Grado = " + grado;
        }
    }

    /**
     * Obtiene una lista de los vértices adyacentes a un vértice dado.
     * Para grafos ponderados, incluye el peso de la arista entre paréntesis.
     *
     * @param vertice El vértice de origen.
     * @return Una cadena que lista los vértices adyacentes.
     */
    public String obtenerAdyacencias(String vertice) {
        Integer indice = indices.get(vertice);
        if (indice == null) {
            return "Error: Vertice '" + vertice + "' no existe.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Vértices adyacentes a '" + vertice + "': ");
        List<String> adyacentes = new ArrayList<>();
        for (int j = 0; j < numeroVertices; j++) {
            if (matrizAdyacencia[indice][j] != 0) {
                String adyacente = vertices.get(j);
                if (esPonderado) {
                    adyacente += "(" + matrizAdyacencia[indice][j] + ")";
                }
                adyacentes.add(adyacente);
            }
        }

        if (adyacentes.isEmpty()) {
            sb.append("Ninguno.");
        } else {
            sb.append(String.join(", ", adyacentes));
        }
        return sb.toString();
    }

    /**
     * Devuelve el número total de vértices y aristas en el grafo.
     * En grafos no dirigidos, las aristas se cuentan una sola vez.
     *
     * @return Una cadena con el recuento de vértices y aristas.
     */
    public String obtenerNumeroVerticesYAristas() {
        int numeroAristas = 0;
        for (int i = 0; i < numeroVertices; i++) {
            for (int j = 0; j < numeroVertices; j++) {
                if (matrizAdyacencia[i][j] != 0) {
                    numeroAristas++;
                }
            }
        }
        if (!esDirigido) {
            numeroAristas /= 2;
        }
        return "Total de vertices: " + numeroVertices + "\nTotal de aristas: " + numeroAristas;
    }

    /**
     * Genera una representación en formato de cadena de la matriz de adyacencia del grafo.
     *
     * @return Una cadena con la matriz de adyacencia formateada.
     */
    public String obtenerMatrizDeAdyacencia() {
        if (numeroVertices == 0) return "El grafo esta vacio.";

        StringBuilder sb = new StringBuilder("Matriz de Adyacencia:\n");

        sb.append(String.format("%6s", ""));
        for (int i = 0; i < numeroVertices; i++) sb.append(String.format("| %-5s", vertices.get(i)));
        sb.append("\n");

        for (int i = 0; i < numeroVertices; i++) {
            sb.append(String.format("%-5s ", vertices.get(i)));
            for (int j = 0; j < numeroVertices; j++) {
                sb.append(String.format("| %-5d", matrizAdyacencia[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
