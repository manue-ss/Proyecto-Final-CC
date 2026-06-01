package co.edu.udistrital.model.estructuras;

import java.util.*;

/**
 * Implementación de una Cola de Prioridad basada en un Árbol Max-Heap.
 * <p>
 * Esta estructura garantiza que el elemento con mayor prioridad (el valor
 * "mayor" según su interfaz {@link Comparable}) siempre estará en el frente de
 * la cola, permitiendo extracciones e inserciones eficientes en tiempo
 * logarítmico O(log n). Utiliza internamente un arreglo dinámico para simular
 * el árbol binario completo.
 * </p>
 *
 * @param <T> El tipo de elementos en la cola. Debe implementar
 *            {@link Comparable}.
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class MaxHeapPriorityQueue<T extends Comparable<T>> implements Iterable<T> {

    /**
     * * Arreglo dinámico que almacena los elementos manteniendo la propiedad
     * Max-Heap.
     */
    private final List<T> heap;
    private final Comparator<T> comparador;

    /**
     * Construye una cola de prioridad vacía.
     */
    public MaxHeapPriorityQueue() {
        this.heap = new ArrayList<>();
        this.comparador = null;
    }

    /**
     * Constructor que acepta una regla de comparación externa.
     *
     * @param comparador La regla para decidir la prioridad.
     */
    public MaxHeapPriorityQueue(Comparator<T> comparador) {
        this.heap = new ArrayList<>();
        this.comparador = comparador;
    }

    // ==========================================
    // 1. API PRINCIPAL DE LA COLA DE PRIORIDAD
    // ==========================================
    /**
     * Inserta un nuevo elemento en la cola y lo reubica según su prioridad.
     * Complejidad temporal: O(log n).
     *
     * @param dato El elemento a insertar.
     *
     * @throws IllegalArgumentException Si el elemento proporcionado es nulo.
     */
    public void enqueue(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se permiten elementos nulos en la cola de prioridad");
        }

        // 1. Insertar en la última posición disponible (siguiente hoja)
        heap.add(dato);

        // 2. Flotar el elemento hasta restaurar la propiedad Max-Heap
        flotar(heap.size() - 1);
    }

    /**
     * Extrae y devuelve el elemento con la máxima prioridad (la raíz del
     * árbol). Complejidad temporal: O(log n).
     *
     * @return El elemento con el valor máximo.
     *
     * @throws NoSuchElementException Si la cola está vacía.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola de prioridad está vacía. No hay elementos para extraer.");
        }

        T maximo = heap.get(0);
        int ultimoIndice = heap.size() - 1;

        // Reemplazar la raíz con la última hoja del árbol
        heap.set(0, heap.get(ultimoIndice));
        heap.remove(ultimoIndice);

        // Hundir la nueva raíz a su posición correcta si aún quedan elementos
        if (!isEmpty()) {
            hundir(0);
        }

        return maximo;
    }

    /**
     * Consulta el elemento con mayor prioridad sin extraerlo de la cola.
     * Complejidad temporal: O(1).
     *
     * @return El elemento en el frente de la cola (raíz del Heap).
     *
     * @throws NoSuchElementException Si la cola está vacía.
     */
    public T peekMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola de prioridad está vacía.");
        }
        return heap.get(0);
    }

    // ==========================================
    // 2. API DE ESTADO Y COLECCIÓN
    // ==========================================
    /**
     * Verifica si la cola de prioridad no contiene elementos.
     *
     * @return {@code true} si está vacía, {@code false} en caso contrario.
     */
    public boolean isEmpty() {
        return heap.isEmpty();
    }

    /**
     * Consulta la cantidad total de elementos almacenados.
     *
     * @return El número de elementos en la cola.
     */
    public int size() {
        return heap.size();
    }

    /**
     * Vacía completamente la cola de prioridad.
     */
    public void clear() {
        this.heap.clear();
    }

    /**
     * Verifica si un elemento específico existe dentro de la cola. Complejidad
     * temporal: O(n), ya que debe recorrer el arreglo linealmente.
     *
     * @param dato El dato a buscar.
     *
     * @return {@code true} si el dato existe, {@code false} si es nulo o no se
     *         encuentra.
     */
    public boolean contains(T dato) {
        if (dato == null) {
            return false;
        }
        for (T elemento : this) {
            if (elemento.equals(dato)) {
                return true;
            }
        }
        return false;
    }

    // ==========================================
    // 3. CONVERSIÓN Y UTILIDADES
    // ==========================================
    /**
     * Convierte la estructura interna en un arreglo estándar de Java. Nota: El
     * arreglo resultante NO estará ordenado de mayor a menor, simplemente
     * representará el estado estructural del árbol en el arreglo.
     *
     * @return Un arreglo con los elementos de la cola.
     */
    public Object[] toArray() {
        return this.heap.toArray();
    }

    /**
     * Genera una copia superficial exacta de la cola de prioridad. Optimización
     * O(n): Copia el arreglo interno directamente en lugar de encolar uno por
     * uno, ya que la estructura actual ya es un Max-Heap válido.
     *
     * @return Una nueva instancia de {@code MaxHeapPriorityQueue} con los
     *         mismos elementos.
     */
    public MaxHeapPriorityQueue<T> clone() {
        MaxHeapPriorityQueue<T> clon = new MaxHeapPriorityQueue<>();
        clon.heap.addAll(this.heap); // Operación optimizada a O(n)
        return clon;
    }

    /**
     * Retorna el iterador estándar de la colección interna. Advertencia: El
     * recorrido a través del iterador NO garantiza extraer los elementos en
     * orden estricto de prioridad.
     *
     * @return Un iterador para recorrer la cola.
     */
    @Override
    public Iterator<T> iterator() {
        return this.heap.iterator();
    }

    // ==========================================
    // 4. MOTOR INTERNO DEL HEAP (MÉTODOS PRIVADOS)
    // ==========================================
    /**
     * Método central para comparar dos elementos. Si hay un comparador
     * inyectado, lo usa. Si no, intenta usar el orden natural.
     *
     * @param a Primer elemeento a comparar.
     * @param b Segundo elemento a comparar.
     *
     * @return Retorna {@code -1} si a es menor que b, {@code 0} si a es igual
     *         que b y {@code 1} si a es mayor que b.
     */
    @SuppressWarnings("unchecked")
    private int comparar(T a, T b) {
        if (this.comparador != null) {
            // Usar la regla externa
            return this.comparador.compare(a, b);
        } else {
            // Asumir orden natural. Si T no implementa Comparable, Java lanzará un ClassCastException
            return ((Comparable<? super T>) a).compareTo(b);
        }
    }

    /**
     * Mueve un nodo hacia la raíz del árbol hasta que sea menor o igual a su
     * padre.
     *
     * @param indice El índice del elemento a reubicar hacia arriba.
     */
    private void flotar(int indice) {
        int indicePadre = getPadre(indice);

        while (indice > 0 && comparar(heap.get(indice), heap.get(indicePadre)) > 0) {
            intercambiar(indice, indicePadre);
            indice = indicePadre;
            indicePadre = getPadre(indice);
        }
    }

    /**
     * Mueve la raíz hacia las hojas hasta que sea mayor o igual a ambos hijos.
     *
     * @param indice El índice del elemento a reubicar hacia abajo.
     */
    private void hundir(int indice) {
        int tamaño = heap.size();

        while (true) {
            int hijoIzq = getHijoIzq(indice);
            int hijoDer = getHijoDer(indice);
            int indiceMayor = indice;

            if (hijoIzq < tamaño && comparar(heap.get(hijoIzq),
                    heap.get(indiceMayor)) > 0) {
                indiceMayor = hijoIzq;
            }

            if (hijoDer < tamaño && comparar(heap.get(hijoDer),
                    heap.get(indiceMayor)) > 0) {
                indiceMayor = hijoDer;
            }

            if (indiceMayor == indice) {
                break; // El nodo ya cumple la propiedad de Max-Heap
            }

            intercambiar(indice, indiceMayor);
            indice = indiceMayor;
        }
    }

    /**
     * Intercambia las posiciones de dos elementos dentro del arreglo.
     */
    private void intercambiar(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

    // ==========================================
    // 5. CÁLCULO MATEMÁTICO DE ÍNDICES
    // ==========================================
    private int getPadre(int i) {
        return (i - 1) / 2;
    }

    private int getHijoIzq(int i) {
        return (2 * i) + 1;
    }

    private int getHijoDer(int i) {
        return (2 * i) + 2;
    }
}
