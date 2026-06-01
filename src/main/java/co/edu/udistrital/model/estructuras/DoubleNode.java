/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package co.edu.udistrital.model.estructuras;

/**
 *
 * @author Manuel Salazar
 * @since 0.1
 */
public class DoubleNode<T> {

    private DoubleNode<T> siguiente;
    private DoubleNode<T> anterior;
    private T dato;

    public DoubleNode() {
        this.siguiente = null;
        this.anterior = null;
    }

    public DoubleNode(T dato) {
        this.dato = dato;
        this.siguiente = null;
        this.anterior = null;
    }

    public DoubleNode(T dato, DoubleNode<T> siguiente) {
        this.dato = dato;
        this.siguiente = siguiente;
        this.anterior = null;
    }

    public DoubleNode(T dato, DoubleNode<T> siguiente, DoubleNode<T> anterior) {
        this.dato = dato;
        this.siguiente = siguiente;
        this.anterior = anterior;
    }

    public DoubleNode<T> getSiguiente() {
        return siguiente;
    }

    public void setSiguiente(DoubleNode<T> siguiente) {
        this.siguiente = siguiente;
    }

    public T getDato() {
        return dato;
    }

    public void setDato(T dato) {
        this.dato = dato;
    }

    public DoubleNode<T> getAnterior() {
        return anterior;
    }

    public void setAnterior(DoubleNode<T> anterior) {
        this.anterior = anterior;
    }

    @Override
    public String toString() {
        return dato.toString();
    }
}
