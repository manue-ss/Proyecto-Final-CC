package co.edu.udistrital.model.structures;

import java.util.*;
import java.io.Serializable;

public class MaxHeapPriorityQueue<T extends Comparable<T>> implements Iterable<T>, Serializable {

    private static final long serialVersionUID = 1L;

    private final List<T> heap;
    private final Comparator<T> comparador;

    public MaxHeapPriorityQueue() {
        this.heap = new ArrayList<>();
        this.comparador = null;
    }

    public MaxHeapPriorityQueue(Comparator<T> comparador) {
        this.heap = new ArrayList<>();
        this.comparador = comparador;
    }

    public void enqueue(T dato) {
        if (dato == null) {
            throw new IllegalArgumentException("No se permiten elementos nulos en la cola de prioridad");
        }

        heap.add(dato);

        flotar(heap.size() - 1);
    }

    public T dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola de prioridad está vacía. No hay elementos para extraer.");
        }

        T maximo = heap.get(0);
        int ultimoIndice = heap.size() - 1;

        heap.set(0, heap.get(ultimoIndice));
        heap.remove(ultimoIndice);

        if (!isEmpty()) {
            hundir(0);
        }

        return maximo;
    }

    public T peekMax() {
        if (isEmpty()) {
            throw new NoSuchElementException("La cola de prioridad está vacía.");
        }
        return heap.get(0);
    }

    public boolean isEmpty() {
        return heap.isEmpty();
    }

    public int size() {
        return heap.size();
    }

    public void clear() {
        this.heap.clear();
    }

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

    public Object[] toArray() {
        return this.heap.toArray();
    }

    public MaxHeapPriorityQueue<T> clone() {
        MaxHeapPriorityQueue<T> clon = new MaxHeapPriorityQueue<>();
        clon.heap.addAll(this.heap);
        return clon;
    }

    @Override
    public Iterator<T> iterator() {
        return this.heap.iterator();
    }

    @SuppressWarnings("unchecked")
    private int comparar(T a, T b) {
        if (this.comparador != null) {
            return this.comparador.compare(a, b);
        } else {

            return ((Comparable<? super T>) a).compareTo(b);
        }
    }

    private void flotar(int indice) {
        int indicePadre = getPadre(indice);

        while (indice > 0 && comparar(heap.get(indice), heap.get(indicePadre)) > 0) {
            intercambiar(indice, indicePadre);
            indice = indicePadre;
            indicePadre = getPadre(indice);
        }
    }

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
                break;
            }

            intercambiar(indice, indiceMayor);
            indice = indiceMayor;
        }
    }

    private void intercambiar(int i, int j) {
        T temp = heap.get(i);
        heap.set(i, heap.get(j));
        heap.set(j, temp);
    }

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
