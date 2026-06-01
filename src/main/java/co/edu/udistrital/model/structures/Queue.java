package co.edu.udistrital.model.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.io.Serializable;
/**
 * Cola genérica con referencia a la cabeza - {@link #head} - y a la cola -
 * {@link #tail} - para reducir la complejidad temporal.
 *
 * @param <T> El tipo de elementos almacenados en la cola.
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class Queue<T> implements Iterable<T>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Node<T> head;
    private Node<T> tail;
    private int tamanio;

    /**
     * Constructor por defecto. Inicializa los valores de {@link #head} y
     * {@link #tail} en nulo y el tamaño en 0.
     */
    public Queue() {
        this.head = null;
        this.tail = null;
        this.tamanio = 0;
    }

    /**
     * Añade un elemento al final de la cola.
     *
     * @param d Valor del elemento a añadir a la cola. No puede ser nulo.
     *
     * @return Retorna {@code true} si la adición fue satisfactoria.
     *
     * @throws NullPointerException Si el elemento {@code d} es nulo.
     */
    public boolean enqueue(T d) {
        // Validación de nulidad estándar
        Objects.requireNonNull(d, "No se permiten elementos nulos en la cola");

        Node<T> nuevo = new Node<>(d);

        if (head == null) {
            head = nuevo;
        } else {
            tail.setSiguiente(nuevo);
        }

        tail = nuevo;
        tamanio++;

        return true;
    }

    /**
     * Extrae el elemento al inicio de la cola.
     *
     * @return El valor del elemento al inicio de la cola.
     *
     * @throws NoSuchElementException Si se intenta desencolar cuando la cola
     *                                está vacía.
     */
    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("No se puede desencolar: La cola está vacía.");
        }

        Node<T> antiguaCabeza = head;
        head = antiguaCabeza.getSiguiente();

        if (antiguaCabeza == tail) {
            tail = null;
        }

        tamanio--;
        return antiguaCabeza.getDato();
    }

    /**
     * Verifica si la cola está o no vacía.
     *
     * @return Retorna {@code true} si está vacía.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Consulta el tamaño de la cola.
     *
     * @return Retorna el tamaño de la cola.
     */
    public int size() {
        return this.tamanio;
    }

    /**
     * Obtiene el valor del elemento al frente de la cola sin extraerlo.
     *
     * @return El valor del elemento en la cabeza de la cola.
     *
     * @throws NoSuchElementException Si se consulta el frente cuando la cola
     *                                está vacía.
     */
    public T front() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola está vacía. No hay elementos en el frente.");
        }
        return this.head.getDato();
    }

    /**
     * Vacía la cola.
     *
     * @return Retorna {@code true} si logró vaciar la cola.
     */
    public boolean clear() {
        head = null;
        tail = null;
        tamanio = 0;

        return true;
    }

    /**
     * Convierte la cola en un arreglo estándar de Java.
     *
     * @return Retorna un arreglo estático con los elementos de la cola.
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
     * Genera una copia exacta de la cola.
     *
     * @return Retorna una nueva instancia de {@code Queue} clonada.
     */
    public Queue<T> clone() {
        Queue<T> colaClonada = new Queue<>();

        for (T elemento : this) {
            colaClonada.enqueue(elemento);
        }

        return colaClonada;
    }

    /**
     * Construye una cadena de texto mostrando los elementos de la cola.
     *
     * @return Retorna un {@code String} con la cola en formato visual.
     */
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Head -> ");

        // ¡Usamos tu iterador aquí también para simplificar!
        for (T elemento : this) {
            sb.append(elemento).append(" -> ");
        }

        sb.append("<- Tail");
        return sb.toString();
    }

    /**
     * Retorna el iterador de la cola.
     *
     * @return Retorna un objeto de tipo {@code Iterator} para recorrer la cola.
     */
    @Override
    public Iterator<T> iterator() {
        return new QueueIterator(); // Renombrado correctamente
    }

    /**
     * Clase interna privada que implementa la lógica de iteración.
     */
    private class QueueIterator implements Iterator<T> {

        private Node<T> actual = head;

        @Override
        public boolean hasNext() {
            return this.actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la iteración.");
            }
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            return dato;
        }
    }

}
