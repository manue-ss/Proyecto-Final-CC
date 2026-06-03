package co.edu.udistrital.model.structures;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.io.Serializable;

public class Stack<T> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private Node<T> top;
    private int tamanio;

    public Stack() {
        this.top = null;
        this.tamanio = 0;
    }

    public void push(T d) {
        Node<T> nuevo = new Node<>(d);

        nuevo.setSiguiente(top);
        top = nuevo;

        tamanio++;
    }

    public T pop() {
        if (isEmpty()) {

            throw new NoSuchElementException("No se puede desapilar: La pila está vacía.");
        }

        Node<T> antiguaCabeza = top;

        top = antiguaCabeza.getSiguiente();

        tamanio--;
        return antiguaCabeza.getDato();

    }

    public boolean isEmpty() {
        return top == null;
    }

    public T peek() {
        if (isEmpty()) {
            throw new NoSuchElementException("La pila está vacía. No hay elementos en el frente.");
        }
        return this.top.getDato();

    }

    public int size() {
        return tamanio;
    }

    public void clear() {
        this.top = null;
        this.tamanio = 0;
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Top -> ");

        for (T elemento : this) {
            sb.append(elemento).append(" -> ");
        }
        return sb.toString();
    }

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
