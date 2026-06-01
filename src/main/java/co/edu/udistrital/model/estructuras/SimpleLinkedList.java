package co.edu.udistrital.model.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.io.Serializable;
/**
 * Implementación de una Lista Simplemente Enlazada genérica.
 *
 * @param <T> El tipo de datos almacenados en la lista.
 * @author Manuel Salazar
 * @since 0.1
 */
public class SimpleLinkedList<T> implements Iterable<T>, Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Node<T> tail;
    private Node<T> head;
    private int tamanio;

    /**
     * Construye una lista enlazada simple vacía. La cabeza, la cola y el tamaño
     * se inicializan adecuadamente.
     */
    public SimpleLinkedList() {
        this.tail = null;
        this.head = null;
        this.tamanio = 0;
    }

    /**
     * Verifica si la lista está o no vacía.
     *
     * @return Retorna {@code true} si está vacía.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Consulta el tamaño de la lista.
     *
     * @return Retorna el tamaño de la lista.
     */
    public int size() {
        return this.tamanio;
    }

    /**
     * Vacía la lista.
     *
     * @return Retorna {@code true} si logró vaciar la lista.
     */
    public boolean clear() {
        head = null;
        tail = null;
        tamanio = 0;
        return true;
    }

    /**
     * Revisa si la lista contiene un elemento.
     *
     * @param dato Dato a buscar en la lista.
     * @return Retorna {@code true} si encontró el elemento.
     */
    public boolean contains(T dato) {
        Objects.requireNonNull(dato, "No se puede buscar un elemento nulo");
        for (T elemento : this) {
            if (Objects.equals(elemento, dato)) {
                return true;
            }
        }
        return false;
    }

    /**
     * Convierte la lista en un arreglo estándar de Java.
     *
     * @return Retorna un arreglo estático con los elementos de la lista.
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
     * Genera una copia exacta de la lista.
     *
     * @return Retorna una nueva instancia de {@code SimpleLinkedList} clonada.
     */
    public SimpleLinkedList<T> clone() {
        SimpleLinkedList<T> listaClonada = new SimpleLinkedList<>();
        for (T elemento : this) {
            listaClonada.addLast(elemento);
        }
        return listaClonada;
    }

    /**
     * Inserta un elemento al principio de la lista (nueva cabeza). Operación de
     * tiempo constante O(1).
     *
     * @param dato El dato a insertar.
     */
    public void addFirst(T dato) {
        Objects.requireNonNull(dato, "No se puede insertar un elemento vacio o nulo");
        Node<T> nuevoNodo = new Node<>(dato, this.head);
        this.head = nuevoNodo;

        if (this.tail == null) {
            this.tail = this.head;
        }
        this.tamanio++;
    }

    /**
     * Inserta un elemento al final de la lista (nueva cola). Operación de
     * tiempo constante O(1) gracias a la referencia {@code tail}.
     *
     * @param dato El dato a insertar.
     */
    public void addLast(T dato) {
        Objects.requireNonNull(dato, "No se puede insertar un elemento vacio o nulo");
        Node<T> nuevoNodo = new Node<>(dato);
        
        if (isEmpty()) {
            this.head = nuevoNodo;
            this.tail = nuevoNodo;
        } else {
            this.tail.setSiguiente(nuevoNodo);
            this.tail = nuevoNodo;
        }
        this.tamanio++;
    }

    /**
     * Inserta un nuevo elemento {@code nuevoDato} inmediatamente después de la
     * primera ocurrencia del nodo que contiene {@code objetivo}. 
     * La búsqueda es O(n), la inserción es O(1).
     *
     * @param objetivo  El dato de referencia.
     * @param nuevoDato El dato a insertar.
     * @return {@code true} si se insertó con éxito, {@code false} si no se encontró.
     */
    public boolean insertAfter(T objetivo, T nuevoDato) {
        Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        Objects.requireNonNull(nuevoDato, "El dato a insertar no puede ser nulo");

        Node<T> actual = head;

        // Cambiado a actual != null para evaluar también la cola
        while (actual != null) {
            if (Objects.equals(actual.getDato(), objetivo)) {
                Node<T> nuevoNodo = new Node<>(nuevoDato, actual.getSiguiente());
                actual.setSiguiente(nuevoNodo);
                
                // Si insertamos después de la cola, actualizamos la cola
                if (actual == this.tail) {
                    this.tail = nuevoNodo;
                }
                
                this.tamanio++; // Agregado el incremento de tamaño faltante
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Alias conveniente para {@link #addLast(Object)}.
     *
     * @param dato El dato a agregar.
     */
    public void add(T dato) {
        addLast(dato);
    }

    /**
     * Elimina y devuelve el elemento al principio de la lista. Operación O(1).
     *
     * @return El dato del elemento eliminado.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía, no se puede eliminar al inicio.");
        }
        T datoEliminado = this.head.getDato();
        this.head = this.head.getSiguiente();
        this.tamanio--;

        if (isEmpty()) {
            this.tail = null;
        }
        return datoEliminado;
    }

    /**
     * Elimina y devuelve el elemento al final de la lista. Operación O(n).
     *
     * @return El dato del elemento eliminado.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía, no se puede eliminar al final.");
        }
        
        // Si hay un solo elemento, es lo mismo que eliminar al inicio
        if (head == tail) {
            return removeFirst();
        }

        Node<T> actual = head;
        // Avanzamos hasta el penúltimo nodo
        while (actual.getSiguiente() != tail) {
            actual = actual.getSiguiente();
        }

        T datoEliminado = this.tail.getDato();
        this.tail = actual;
        this.tail.setSiguiente(null);
        this.tamanio--;
        
        return datoEliminado;
    }

    /**
     * Elimina la primera ocurrencia del elemento especificado. Búsqueda O(n).
     *
     * @param dato El dato a eliminar.
     * @return {@code true} si fue eliminado, {@code false} en caso contrario.
     */
    public boolean remove(T dato) {
        Objects.requireNonNull(dato, "No se puede remover un elemento vacio o nulo");

        if (isEmpty()) {
            return false;
        }

        if (Objects.equals(this.head.getDato(), dato)) {
            removeFirst();
            return true; // Faltaba el return aquí
        }

        Node<T> actual = this.head.getSiguiente();
        Node<T> anterior = this.head;

        // Cambiado a actual != null para evaluar también la cola
        while (actual != null) {
            if (Objects.equals(actual.getDato(), dato)) {
                anterior.setSiguiente(actual.getSiguiente());

                if (actual == this.tail) {
                    this.tail = anterior;
                }

                this.tamanio--;
                return true;
            }
            anterior = actual;
            actual = actual.getSiguiente();
        }
        return false;
    }

    /**
     * Retorna el valor del primer dato de la lista sin extraerlo.
     *
     * @return El primer dato de la lista.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return this.head.getDato();
    }

    /**
     * Retorna el valor del último dato de la lista sin extraerlo.
     *
     * @return El último dato de la lista.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T getLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return this.tail.getDato();
    }

    @Override
    public Iterator<T> iterator() {
        return new SimpleLinkedListIterator(); 
    }

    /**
     * Clase interna privada que implementa la lógica de iteración.
     */
    private class SimpleLinkedListIterator implements Iterator<T> {

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