package implementaciones;

/**
 * Representa una arista en el grafo.
 * Contiene el destino de la arista y su peso.
 */
public class Arista {
    String destino;
    int peso;

    /**
     * Construye una nueva arista.
     *
     * @param destino El vértice de destino de la arista.
     * @param peso    El peso de la arista.
     */
    public Arista(String destino, int peso) {
        this.destino = destino;
        this.peso = peso;
    }
}
