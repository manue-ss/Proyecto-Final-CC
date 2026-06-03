package co.edu.udistrital.model.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.io.Serializable;

public class SimpleLinkedList<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private Node<T> tail;
    private Node<T> head;
    private int tamanio;

    public SimpleLinkedList() {
        this.tail = null;
        this.head = null;
        this.tamanio = 0;
    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return this.tamanio;
    }

    public boolean clear() {
        head = null;
        tail = null;
        tamanio = 0;
        return true;
    }

    public boolean contains(T dato) {
        Objects.requireNonNull(dato, "No se puede buscar un elemento nulo");
        for (T elemento : this) {
            if (Objects.equals(elemento, dato)) {
                return true;
            }
        }
        return false;
    }

    public Object[] toArray() {
        Object[] arreglo = new Object[this.tamanio];
        int i = 0;
        for (T elemento : this) {
            arreglo[i] = elemento;
            i++;
        }
        return arreglo;
    }

    public SimpleLinkedList<T> clone() {
        SimpleLinkedList<T> listaClonada = new SimpleLinkedList<>();
        for (T elemento : this) {
            listaClonada.addLast(elemento);
        }
        return listaClonada;
    }

    public void addFirst(T dato) {
        Objects.requireNonNull(dato, "No se puede insertar un elemento vacio o nulo");
        Node<T> nuevoNodo = new Node<>(dato, this.head);
        this.head = nuevoNodo;

        if (this.tail == null) {
            this.tail = this.head;
        }
        this.tamanio++;
    }

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

    public boolean insertAfter(T objetivo, T nuevoDato) {
        Objects.requireNonNull(objetivo, "El objetivo no puede ser nulo");
        Objects.requireNonNull(nuevoDato, "El dato a insertar no puede ser nulo");

        Node<T> actual = head;

        while (actual != null) {
            if (Objects.equals(actual.getDato(), objetivo)) {
                Node<T> nuevoNodo = new Node<>(nuevoDato, actual.getSiguiente());
                actual.setSiguiente(nuevoNodo);

                if (actual == this.tail) {
                    this.tail = nuevoNodo;
                }

                this.tamanio++;
                return true;
            }
            actual = actual.getSiguiente();
        }
        return false;
    }

    public void add(T dato) {
        addLast(dato);
    }

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

    public T removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía, no se puede eliminar al final.");
        }

        if (head == tail) {
            return removeFirst();
        }

        Node<T> actual = head;

        while (actual.getSiguiente() != tail) {
            actual = actual.getSiguiente();
        }

        T datoEliminado = this.tail.getDato();
        this.tail = actual;
        this.tail.setSiguiente(null);
        this.tamanio--;

        return datoEliminado;
    }

    public boolean remove(T dato) {
        Objects.requireNonNull(dato, "No se puede remover un elemento vacio o nulo");

        if (isEmpty()) {
            return false;
        }

        if (Objects.equals(this.head.getDato(), dato)) {
            removeFirst();
            return true;
        }

        Node<T> actual = this.head.getSiguiente();
        Node<T> anterior = this.head;

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

    public T getFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException("La lista está vacía.");
        }
        return this.head.getDato();
    }

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
