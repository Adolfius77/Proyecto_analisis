package implementaciones;

import java.util.*;

/**
 * Representa un grafo que puede ser dirigido o no dirigido, y ponderado o no ponderado.
 * La implementación utiliza una lista de adyacencia.
 */
public class Grafo {
    private final Map<String, List<Arista>> listaDeAdyacencia;
    private final boolean esDirigido;
    private final boolean esPonderado;

    public Grafo(boolean esDirigido, boolean esPonderado) {
        this.listaDeAdyacencia = new HashMap<>();
        this.esDirigido = esDirigido;
        this.esPonderado = esPonderado;
    }

    public Map<String, List<Arista>> getListaDeAdyacencia() {
        return listaDeAdyacencia;
    }

    public boolean esDirigido() {
        return esDirigido;
    }

    public boolean esPonderado() {
        return esPonderado;
    }

    public String insertarVertice(String vertice) {
        if (listaDeAdyacencia.putIfAbsent(vertice, new ArrayList<>()) == null) {
            return "Vértice '" + vertice + "' insertado con éxito.";
        } else {
            return "Info: El vértice '" + vertice + "' ya existía.";
        }
    }

    public String eliminarVertice(String vertice) {
        if (listaDeAdyacencia.remove(vertice) != null) {
            for (List<Arista> aristas : listaDeAdyacencia.values()) {
                aristas.removeIf(arista -> arista.destino.equals(vertice));
            }
            return "Vértice '" + vertice + "' eliminado.";
        } else {
            return "Error: Vértice '" + vertice + "' no existe.";
        }
    }

    public String insertarArista(String origen, String destino, int peso) {
        if (!listaDeAdyacencia.containsKey(origen) || !listaDeAdyacencia.containsKey(destino)) {
            return String.format("Error: Vértice origen '%s' o destino '%s' no existe.", origen, destino);
        }
        int pesoReal = esPonderado ? peso : 1;
        listaDeAdyacencia.get(origen).add(new Arista(destino, pesoReal));
        String resultado = "Arista de '" + origen + "' a '" + destino + "' con peso " + pesoReal + " insertada.";

        if (!esDirigido) {
            listaDeAdyacencia.get(destino).add(new Arista(origen, pesoReal));
            resultado += "\n   (Grafo no dirigido) Arista de '" + destino + "' a '" + origen + "' insertada.";
        }
        return resultado;
    }

    public String eliminarArista(String origen, String destino) {
        boolean removido = false;
        if (listaDeAdyacencia.containsKey(origen)) {
            removido = listaDeAdyacencia.get(origen).removeIf(arista -> arista.destino.equals(destino));
        }

        if (!esDirigido && listaDeAdyacencia.containsKey(destino)) {
            listaDeAdyacencia.get(destino).removeIf(arista -> arista.destino.equals(origen));
        }

        return removido ? "Arista entre '" + origen + "' y '" + destino + "' eliminada." : "Error: No se encontró la arista.";
    }

    public String actualizarPonderacion(String origen, String destino, int nuevoPeso) {
        if (!esPonderado) {
            return "Operación no permitida: el grafo no es ponderado.";
        }
        boolean actualizada = false;
        if (listaDeAdyacencia.containsKey(origen)) {
            for (Arista arista : listaDeAdyacencia.get(origen)) {
                if (arista.destino.equals(destino)) {
                    arista.peso = nuevoPeso;
                    actualizada = true;
                    break;
                }
            }
        }
        if (!esDirigido && listaDeAdyacencia.containsKey(destino)) {
            for (Arista arista : listaDeAdyacencia.get(destino)) {
                if (arista.destino.equals(origen)) {
                    arista.peso = nuevoPeso;
                }
            }
        }
        return actualizada ? "Ponderación de '" + origen + "' a '" + destino + "' actualizada a " + nuevoPeso + "." : "Error: No se encontró la arista.";
    }

    public String obtenerGradoVertice(String vertice) {
        if (!listaDeAdyacencia.containsKey(vertice)) {
            return "Error: El vértice '" + vertice + "' no existe.";
        }

        if (esDirigido) {
            int gradoSalida = listaDeAdyacencia.get(vertice).size();
            int gradoEntrada = 0;
            for (List<Arista> aristas : listaDeAdyacencia.values()) {
                for (Arista arista : aristas) {
                    if (arista.destino.equals(vertice)) {
                        gradoEntrada++;
                    }
                }
            }
            return "Vértice '" + vertice + "': Grado de Salida = " + gradoSalida + ", Grado de Entrada = " + gradoEntrada;
        } else {
            int grado = listaDeAdyacencia.get(vertice).size();
            return "Vértice '" + vertice + "': Grado = " + grado;
        }
    }

    public String obtenerAdyacencias(String vertice) {
        if (!listaDeAdyacencia.containsKey(vertice)) {
            return "Error: Vértice '" + vertice + "' no existe.";
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Vértices adyacentes a '" + vertice + "': ");
        List<Arista> aristas = listaDeAdyacencia.get(vertice);
        if (aristas.isEmpty()) {
            sb.append("Ninguno.");
        } else {
            for (int i = 0; i < aristas.size(); i++) {
                Arista arista = aristas.get(i);
                sb.append(arista.destino).append(esPonderado ? "(" + arista.peso + ")" : "");
                if (i < aristas.size() - 1) {
                    sb.append(", ");
                }
            }
        }
        return sb.toString();
    }

    public String obtenerNumeroVerticesYAristas() {
        int numeroVertices = listaDeAdyacencia.size();
        int numeroAristas = 0;
        for (List<Arista> aristas : listaDeAdyacencia.values()) {
            numeroAristas += aristas.size();
        }
        if (!esDirigido) {
            numeroAristas /= 2;
        }
        return "Total de vértices: " + numeroVertices + "\nTotal de aristas: " + numeroAristas;
    }

    public String obtenerMatrizDeAdyacencia() {
        List<String> vertices = new ArrayList<>(listaDeAdyacencia.keySet());
        Collections.sort(vertices);
        int n = vertices.size();
        if (n == 0) return "El grafo está vacío.";

        StringBuilder sb = new StringBuilder("Matriz de Adyacencia:\n");
        int[][] matriz = new int[n][n];
        Map<String, Integer> indices = new HashMap<>();
        for (int i = 0; i < n; i++) indices.put(vertices.get(i), i);

        for (int i = 0; i < n; i++) {
            for (Arista arista : listaDeAdyacencia.get(vertices.get(i))) {
                matriz[i][indices.get(arista.destino)] = arista.peso;
            }
        }

        sb.append(String.format("%6s", ""));
        for (String v : vertices) sb.append(String.format("| %-5s", v));
        sb.append("\n");

        for (int i = 0; i < n; i++) {
            sb.append(String.format("%-5s ", vertices.get(i)));
            for (int j = 0; j < n; j++) {
                sb.append(String.format("| %-5d", matriz[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}
