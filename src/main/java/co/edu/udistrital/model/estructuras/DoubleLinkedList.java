package co.edu.udistrital.model.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;

/**
 * Implementación de una Lista Doblemente Enlazada genérica.
 * <p>
 * A diferencia de la lista simple, esta estructura mantiene referencias tanto al
 * nodo siguiente como al anterior. Esto permite recorridos bidireccionales y
 * optimiza drásticamente operaciones como la eliminación al final de la lista,
 * reduciendo su complejidad temporal a O(1).
 * </p>
 *
 * @param <T> El tipo de datos almacenados en la lista.
 * @author Manuel Salazar
 * @since 0.1
 */
public class DoubleLinkedList<T> implements Iterable<T> {

    private DoubleNode<T> tail;
    private DoubleNode<T> head;
    private int tamanio;

    /**
     * Construye una lista enlazada doble vacía. 
     * La cabeza, la cola y el tamaño se inicializan adecuadamente.
     */
    public DoubleLinkedList() {
        this.tail = null;
        this.head = null;
        this.tamanio = 0;
    }

    /**
     * Verifica si la lista está o no vacía.
     *
     * @return Retorna {@code true} si el tamaño es 0 o la cabeza es nula.
     */
    public boolean isEmpty() {
        return head == null;
    }

    /**
     * Consulta la cantidad de elementos almacenados en la lista.
     * Operación de tiempo constante O(1).
     *
     * @return Retorna el tamaño actual de la lista.
     */
    public int size() {
        return this.tamanio;
    }

    /**
     * Vacía la lista por completo, desenlazando la cabeza y la cola para que
     * el recolector de basura (Garbage Collector) libere la memoria de los nodos.
     *
     * @return Retorna {@code true} tras vaciar la estructura exitosamente.
     */
    public boolean clear() {
        head = null;
        tail = null;
        tamanio = 0;
        return true;
    }

    /**
     * Revisa si la lista contiene al menos una ocurrencia del elemento especificado.
     * Búsqueda lineal de complejidad O(n).
     *
     * @param dato El dato a buscar en la lista.
     * @return Retorna {@code true} si encontró el elemento, {@code false} en caso contrario.
     * @throws NullPointerException Si el dato proporcionado es nulo.
     */
    public boolean contains(T dato) {
        Objects.requireNonNull(dato, "No se puede buscar un elemento nulo");
        return searchNode(dato) != null;
    }

    /**
     * Convierte la lista en un arreglo estándar de Java, manteniendo 
     * el orden desde la cabeza hasta la cola.
     *
     * @return Retorna un arreglo estático de tipo {@code Object[]} con los elementos.
     */
    public Object[] toArray() {
        Object[] arreglo = new Object[this.tamanio];
        int i = 0;
        for (T elemento : this) {
            arreglo[i++] = elemento;
        }
        return arreglo;
    }

    /**
     * Genera una copia superficial (shallow copy) exacta de la lista.
     *
     * @return Retorna una nueva instancia de {@code DoubleLinkedList} clonada.
     */
    public DoubleLinkedList<T> clone() {
        DoubleLinkedList<T> listaClonada = new DoubleLinkedList<>();
        for (T elemento : this) {
            listaClonada.addLast(elemento);
        }
        return listaClonada;
    }

    /**
     * Inserta un elemento al principio de la lista (nueva cabeza). 
     * Operación de tiempo constante O(1).
     *
     * @param dato El dato a insertar.
     * @throws NullPointerException Si el dato proporcionado es nulo.
     */
    public void addFirst(T dato) {
        Objects.requireNonNull(dato, "No se puede insertar un elemento nulo");
        DoubleNode<T> nuevoNodo = new DoubleNode<>(dato);

        if (isEmpty()) {
            this.head = nuevoNodo;
            this.tail = nuevoNodo;
        } else {
            nuevoNodo.setSiguiente(this.head);
            this.head.setAnterior(nuevoNodo);
            this.head = nuevoNodo;
        }
        this.tamanio++;
    }

    /**
     * Inserta un elemento al final de la lista (nueva cola). 
     * Operación de tiempo constante O(1) gracias a la referencia {@code tail}.
     *
     * @param dato El dato a insertar.
     * @throws NullPointerException Si el dato proporcionado es nulo.
     */
    public void addLast(T dato) {
        Objects.requireNonNull(dato, "No se puede insertar un elemento nulo");
        DoubleNode<T> nuevoNodo = new DoubleNode<>(dato);

        if (isEmpty()) {
            this.head = nuevoNodo;
            this.tail = nuevoNodo;
        } else {
            nuevoNodo.setAnterior(this.tail);
            this.tail.setSiguiente(nuevoNodo);
            this.tail = nuevoNodo;
        }
        this.tamanio++;
    }

    /**
     * Método privado auxiliar para la búsqueda de un nodo específico.
     *
     * @param dato Valor del elemento a buscar.
     * @return Retorna el objeto {@code DoubleNode} que contiene el dato, 
     * o {@code null} si no se encuentra.
     */
    private DoubleNode<T> searchNode(T dato) {
        DoubleNode<T> actual = head;
        while (actual != null) {
            if (Objects.equals(actual.getDato(), dato)) {
                return actual;
            }
            actual = actual.getSiguiente();
        }
        return null;
    }

    /**
     * Inserta un nuevo elemento inmediatamente después de la primera ocurrencia 
     * del nodo que contiene el dato objetivo. 
     * La búsqueda es O(n), pero la reconexión de punteros es O(1).
     *
     * @param objetivo  El dato de referencia a buscar.
     * @param nuevoDato El nuevo dato a insertar.
     * @return {@code true} si la inserción fue exitosa, {@code false} si el objetivo no existe.
     * @throws NullPointerException Si alguno de los parámetros es nulo.
     */
    public boolean insertAfter(T objetivo, T nuevoDato) {
        Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        Objects.requireNonNull(nuevoDato, "El dato a insertar no puede ser nulo");

        DoubleNode<T> nodoObjetivo = searchNode(objetivo);
        if (nodoObjetivo == null) {
            return false;
        }

        if (nodoObjetivo == tail) {
            addLast(nuevoDato);
            return true;
        }

        DoubleNode<T> nuevoNodo = new DoubleNode<>(nuevoDato);
        DoubleNode<T> nodoSiguiente = nodoObjetivo.getSiguiente();

        nuevoNodo.setAnterior(nodoObjetivo);
        nuevoNodo.setSiguiente(nodoSiguiente);
        nodoObjetivo.setSiguiente(nuevoNodo);
        nodoSiguiente.setAnterior(nuevoNodo);

        this.tamanio++;
        return true;
    }

    /**
     * Inserta un nuevo elemento inmediatamente antes de la primera ocurrencia 
     * del nodo que contiene el dato objetivo. 
     * Aprovecha el doble enlace para evitar rastrear nodos anteriores durante la búsqueda.
     *
     * @param objetivo  El dato de referencia a buscar.
     * @param nuevoDato El nuevo dato a insertar.
     * @return {@code true} si la inserción fue exitosa, {@code false} si el objetivo no existe.
     * @throws NullPointerException Si alguno de los parámetros es nulo.
     */
    public boolean insertBefore(T objetivo, T nuevoDato) {
        Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        Objects.requireNonNull(nuevoDato, "El dato a insertar no puede ser nulo");

        DoubleNode<T> nodoObjetivo = searchNode(objetivo);
        if (nodoObjetivo == null) {
            return false;
        }

        if (nodoObjetivo == head) {
            addFirst(nuevoDato);
            return true;
        }

        DoubleNode<T> nuevoNodo = new DoubleNode<>(nuevoDato);
        DoubleNode<T> nodoAnterior = nodoObjetivo.getAnterior();

        nuevoNodo.setSiguiente(nodoObjetivo);
        nuevoNodo.setAnterior(nodoAnterior);
        nodoAnterior.setSiguiente(nuevoNodo);
        nodoObjetivo.setAnterior(nuevoNodo);

        this.tamanio++;
        return true;
    }

    /**
     * Alias conveniente para la inserción estándar. Agrega un elemento al final de la lista.
     *
     * @param dato El dato a agregar.
     */
    public void add(T dato) {
        addLast(dato);
    }

    /**
     * Elimina y devuelve el elemento al principio de la lista. 
     * Actualiza correctamente el enlace anterior de la nueva cabeza. Operación O(1).
     *
     * @return El dato del elemento eliminado.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía, no se puede eliminar al inicio.");
        }

        T datoEliminado = this.head.getDato();

        if (this.head == this.tail) { 
            clear();
        } else {
            this.head = this.head.getSiguiente();
            this.head.setAnterior(null);
            this.tamanio--;
        }
        return datoEliminado;
    }

    /**
     * Elimina y devuelve el elemento al final de la lista. 
     * A diferencia de la lista simple, gracias al doble enlace esta operación es de tiempo constante O(1).
     *
     * @return El dato del elemento eliminado.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía, no se puede eliminar al final.");
        }

        T datoEliminado = this.tail.getDato();

        if (this.head == this.tail) {
            clear();
        } else {
            this.tail = this.tail.getAnterior();
            this.tail.setSiguiente(null);
            this.tamanio--;
        }
        return datoEliminado;
    }

    /**
     * Elimina la primera ocurrencia del elemento especificado en la lista. 
     * La búsqueda es O(n), pero la desconexión es O(1) directa al conocer el nodo previo y siguiente.
     *
     * @param dato El dato a eliminar.
     * @return {@code true} si fue eliminado, {@code false} en caso de no encontrarlo.
     * @throws NullPointerException Si el dato proporcionado es nulo.
     */
    public boolean remove(T dato) {
        Objects.requireNonNull(dato, "No se puede remover un elemento nulo");

        DoubleNode<T> nodoAEliminar = searchNode(dato);
        if (nodoAEliminar == null) {
            return false;
        }

        if (nodoAEliminar == head) {
            removeFirst();
            return true;
        }
        
        if (nodoAEliminar == tail) {
            removeLast();
            return true;
        }

        DoubleNode<T> anterior = nodoAEliminar.getAnterior();
        DoubleNode<T> siguiente = nodoAEliminar.getSiguiente();

        anterior.setSiguiente(siguiente);
        siguiente.setAnterior(anterior);

        nodoAEliminar.setSiguiente(null);
        nodoAEliminar.setAnterior(null);

        this.tamanio--;
        return true;
    }

    /**
     * Retorna el valor del primer dato de la lista sin extraerlo.
     *
     * @return El primer dato almacenado.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T getFirst() {
        if (isEmpty()) throw new NoSuchElementException("La lista está vacía.");
        return this.head.getDato();
    }

    /**
     * Retorna el valor del último dato de la lista sin extraerlo.
     *
     * @return El último dato almacenado.
     * @throws NoSuchElementException si la lista está vacía.
     */
    public T getLast() {
        if (isEmpty()) throw new NoSuchElementException("La lista está vacía.");
        return this.tail.getDato();
    }

    /**
     * Retorna el iterador estándar para recorrer la lista en orden secuencial (cabeza a cola).
     *
     * @return Un objeto {@link Iterator} para la lista.
     */
    @Override
    public Iterator<T> iterator() {
        return new ForwardIterator();
    }

    /**
     * Proporciona un mecanismo para iterar la lista en orden inverso (cola a cabeza).
     * * <pre>{@code
     * for(T dato : miListaDoble.descendingIterator()) {
     * System.out.println(dato);
     * }
     * }</pre>
     *
     * @return Un objeto {@link Iterable} configurado para recorrido inverso.
     */
    public Iterable<T> descendingIterator() {
        return () -> new ReverseIterator();
    }

    /**
     * Clase interna privada que implementa la lógica de iteración hacia adelante.
     */
    private class ForwardIterator implements Iterator<T> {
        private DoubleNode<T> actual = head;

        @Override
        public boolean hasNext() {
            return this.actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException("No hay más elementos en la iteración.");
            T dato = actual.getDato();
            actual = actual.getSiguiente();
            return dato;
        }
    }

    /**
     * Clase interna privada que implementa la lógica de iteración hacia atrás.
     */
    private class ReverseIterator implements Iterator<T> {
        private DoubleNode<T> actual = tail;

        @Override
        public boolean hasNext() {
            return this.actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) throw new NoSuchElementException("No hay más elementos en la iteración inversa.");
            T dato = actual.getDato();
            actual = actual.getAnterior();
            return dato;
        }
    }
}