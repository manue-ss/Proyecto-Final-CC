package co.edu.udistrital.model.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;
/**
 * Cola genérica con referencia al tope - {@link #top} - para reducir la
 * complejidad temporal.
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class Stack<T> implements Iterable<T>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Node<T> top;
    private int tamanio;

    /**
     * Constructor por defecto. Inicializa los valores de {@link #top} en nulo y
     * el tamaño en 0.
     */
    public Stack() {
        this.top = null;
        this.tamanio = 0;
    }

    /**
     * Añade un elemento al inicio de la pila.
     *
     * @param d Valor del elemento a añadir a la cola.
     *
     */
    public void push(T d) {
        Node<T> nuevo = new Node<>(d);

        nuevo.setSiguiente(top);
        top = nuevo;

        tamanio++;
    }

    /**
     * Extrae el elemento en la cima de la pila.
     *
     * @return El valor del elemento en la cima de la pila.
     *
     * @throws NoSuchElementException Si se intenta desapilar cuando la pila
     *                                está vacía.
     */
    public T pop() {
        if (isEmpty()) {
            // Detiene la ejecución y lanza un mensaje descriptivo
            throw new NoSuchElementException("No se puede desapilar: La pila está vacía.");
        }

        Node<T> antiguaCabeza = top;

        top = antiguaCabeza.getSiguiente();

        tamanio--;
        return antiguaCabeza.getDato();

    }

    /**
     * Verifica si la cola está o no vacía.
     *
     * @return Retorna {@code True} si esta vacia.
     */
    public boolean isEmpty() {
        return top == null;
    }

    /**
     * Obtiene el valor del elemento al tope de la pila sin extraerlo.
     *
     * @return El valor del elemento en tope de la pila.
     *
     * @throws NoSuchElementException Si se consulta el frente cuando la cola
     *                                está vacía.
     */
    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("La pila está vacía. No hay elementos en el frente.");
        }
        return this.top.getDato();

    }

    /**
     * Consulta el tamaño de la pila.
     *
     * @return Retorna el tamaño de la pila.
     */
    public int size() {
        return tamanio;
    }

    /**
     * Vacia la pila
     */
    public void clear() {
        this.top = null;
        this.tamanio = 0;
    }

    /**
     * Convierte una pila en una arreglo estandar de Java.
     *
     * @return Retorna un arreglo estatico.
     */
    public Object[] toArray() {
        Object[] arreglo = new Object[this.tamanio];
        int i = 0;

        for (T elemento : this) {
            arreglo[i] = elemento;
            i++;
        }
        return arreglo;
    }

    /**
     * Genera una copia de la Pila.
     *
     * @return Retorn una pila de tipo stack {@code Stack} clonada.
     */
    public Stack<T> clone() {
        Stack<T> pilaClonada = new Stack<>();
        Stack<T> pilaTemporal = new Stack<>();

        for (T elemento : this) {
            pilaTemporal.push(elemento);
        }

        for (T elemento : pilaTemporal) {
            pilaClonada.push(elemento);
        }

        return pilaClonada;
    }

    /**
     * Muestra en formma de texto la pila.
     *
     * @return Un objeto de tipo {@code String} con la sucesion de elementos de
     *         la pila.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Top -> ");
        // El for-each usa tu iterador por debajo automáticamente
        for (T elemento : this) {
            sb.append(elemento).append(" -> ");
        }
        return sb.toString();
    }

    /**
     * Retorna un el iterador de la pila.
     *
     * @return Retorna el objeto de tipo {@code Iterator}.
     */
    @Override
    public Iterator<T> iterator() {
        return new StackIterator();
    }

    private class StackIterator implements Iterator<T> {

        private Node<T> actual = top;

        @Override

        public boolean hasNext() {
            return this.actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException();
            }
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            return dato;
        }

    }

}
