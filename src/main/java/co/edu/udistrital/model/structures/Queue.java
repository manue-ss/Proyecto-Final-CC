package co.edu.udistrital.model.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.io.Serializable;

public class Queue<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private Node<T> head;
    private Node<T> tail;
    private int tamanio;

    public Queue() {
        this.head = null;
        this.tail = null;
        this.tamanio = 0;
    }

    public boolean enqueue(T d) {

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

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return this.tamanio;
    }

    public T front() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola está vacía. No hay elementos en el frente.");
        }
        return this.head.getDato();
    }

    public boolean clear() {
        head = null;
        tail = null;
        tamanio = 0;

        return true;
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

    public Queue<T> clone() {
        Queue<T> colaClonada = new Queue<>();

        for (T elemento : this) {
            colaClonada.enqueue(elemento);
        }

        return colaClonada;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Head -> ");

        for (T elemento : this) {
            sb.append(elemento).append(" -> ");
        }

        sb.append("<- Tail");
        return sb.toString();
    }

    @Override
    public Iterator<T> iterator() {
        return new QueueIterator();
    }

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
