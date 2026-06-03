package co.edu.udistrital.model.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.io.Serializable;

public class DoubleLinkedList<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private DoubleNode<T> tail;
    private DoubleNode<T> head;
    private int tamanio;

    public DoubleLinkedList() {
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
        return searchNode(dato) != null;
    }

    public Object[] toArray() {
        Object[] arreglo = new Object[this.tamanio];
        int i = 0;
        for (T elemento : this) {
            arreglo[i++] = elemento;
        }
        return arreglo;
    }

    public DoubleLinkedList<T> clone() {
        DoubleLinkedList<T> listaClonada = new DoubleLinkedList<>();
        for (T elemento : this) {
            listaClonada.addLast(elemento);
        }
        return listaClonada;
    }

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

    public void add(T dato) {
        addLast(dato);
    }

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
        return new ForwardIterator();
    }

    public Iterable<T> descendingIterator() {
        return () -> new ReverseIterator();
    }

    private class ForwardIterator implements Iterator<T> {

        private DoubleNode<T> actual = head;

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

    private class ReverseIterator implements Iterator<T> {

        private DoubleNode<T> actual = tail;

        @Override
        public boolean hasNext() {
            return this.actual != null;
        }

        @Override
        public T next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No hay más elementos en la iteración inversa.");
            }
            T dato = actual.getDato();
            actual = actual.getAnterior();
            return dato;
        }
    }

}
