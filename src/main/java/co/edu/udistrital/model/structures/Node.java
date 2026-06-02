package co.edu.udistrital.model.structures;
import java.io.Serializable;

class Node<T> implements Serializable {
    
    private static final long serialVersionUID = 1L;
    
    private Node<T> siguiente;
    private T dato;

    public Node() {
        this.siguiente = null;
    }

    public Node(T dato) {
        this.dato = dato;
        this.siguiente = null;
    }
    
    public Node(T dato, Node<T> siguiente) {
        this.dato = dato;
        this.siguiente = siguiente;
    }

    public Node<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(Node<T> siguiente) {
        this.siguiente = siguiente;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    @Override
    public String toString() {
        return dato.toString();
    }

}
