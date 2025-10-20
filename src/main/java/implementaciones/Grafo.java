package implementaciones;

import java.util.*;

/**
 * Representa un grafo implementado con una matriz de adyacencia.
 * Puede ser dirigido o no dirigido, y ponderado o no ponderado.
 */
public class Grafo {

    private int[][] matrizAdyacencia;
    private final Map<String, Integer> indices; // Mapea el nombre del vertice a su indice en la matriz
    private final List<String> vertices;       // Mapea el indice al nombre del vertice
    private int numeroVertices;
    private final boolean esDirigido;
    private final boolean esPonderado;
    private static final int CAPACIDAD_MAXIMA = 100; // Capacidad maxima de vertices

    public Grafo(boolean esDirigido, boolean esPonderado) {
        this.esDirigido = esDirigido;
        this.esPonderado = esPonderado;
        this.indices = new HashMap<>();
        this.vertices = new ArrayList<>();
        this.matrizAdyacencia = new int[CAPACIDAD_MAXIMA][CAPACIDAD_MAXIMA];
        this.numeroVertices = 0;
    }

    public boolean esDirigido() {
        return esDirigido;
    }

    public boolean esPonderado() {
        return esPonderado;
    }

    public String insertarVertice(String vertice) {
        if (indices.containsKey(vertice)) {
            return "Info: El vertice '" + vertice + "' ya existia.";
        }
        if (numeroVertices >= CAPACIDAD_MAXIMA) {
            return "Error: Se ha alcanzado la capacidad maxima de vertices.";
        }
        indices.put(vertice, numeroVertices);
        vertices.add(vertice);
        numeroVertices++;
        return "Vertice '" + vertice + "' insertado con exito.";
    }

    public String eliminarVertice(String vertice) {
        Integer indice = indices.get(vertice);
        if (indice == null) {
            return "Error: Vertice '" + vertice + "' no existe.";
        }

        int n = numeroVertices;
        // Crear una nueva matriz mas pequena
        int[][] nuevaMatriz = new int[CAPACIDAD_MAXIMA][CAPACIDAD_MAXIMA];

        // Remover el vertice de las listas de mapeo
        vertices.remove(indice.intValue());
        indices.remove(vertice);

        // Actualizar los indices de los vertices restantes
        for(int i = indice; i < vertices.size(); i++) {
            indices.put(vertices.get(i), i);
        }

        // Copiar los valores a la nueva matriz, omitiendo la fila y columna del vertice eliminado
        for (int i = 0, oldI = 0; i < n - 1; i++, oldI++) {
            if (oldI == indice) oldI++; // Salta la fila del vertice eliminado
            for (int j = 0, oldJ = 0; j < n - 1; j++, oldJ++) {
                if (oldJ == indice) oldJ++; // Salta la columna del vertice eliminado
                nuevaMatriz[i][j] = matrizAdyacencia[oldI][oldJ];
            }
        }

        matrizAdyacencia = nuevaMatriz;
        numeroVertices--;

        return "Vertice '" + vertice + "' eliminado.";
    }


    public String insertarArista(String origen, String destino, int peso) {
        Integer indiceOrigen = indices.get(origen);
        Integer indiceDestino = indices.get(destino);

        if (indiceOrigen == null || indiceDestino == null) {
            return String.format("Error: Vertice origen '%s' o destino '%s' no existe.", origen, destino);
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

        return removido ? "Arista entre '" + origen + "' y '" + destino + "' eliminada." : "Error: No se encontro la arista.";
    }

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

    public String obtenerAdyacencias(String vertice) {
        Integer indice = indices.get(vertice);
        if (indice == null) {
            return "Error: Vertice '" + vertice + "' no existe.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Vertices adyacentes a '" + vertice + "': ");
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

    public String obtenerMatrizDeAdyacencia() {
        if (numeroVertices == 0) return "El grafo esta vacio.";

        StringBuilder sb = new StringBuilder("Matriz de Adyacencia:\n");

        sb.append(String.format("%6s", ""));
        for (int i=0; i < numeroVertices; i++) sb.append(String.format("| %-5s", vertices.get(i)));
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