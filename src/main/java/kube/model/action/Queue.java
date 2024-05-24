package kube.model.action;

public class Queue<E> {

    /**********
     * INNER MAILLON CLASS
     **********/

    class Maillon<T> {
        T e;
        Maillon<T> next;
    }

    /**********
     * ATTRIBUTES
     **********/
    Maillon<E> head, queue;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of Queue class
     */
    public Queue() {
        head = queue = null;
    }

    /**********
     * METHODS
     **********/

    /**
     * Add an element to the queue
     * 
     * @param e the element to add
     */
    public synchronized void add(E e) {
        Maillon<E> m = new Maillon<>();
        m.e = e;
        m.next = null;
        if (head == null)
            head = queue = m;
        else {
            queue.next = m;
            queue = m;
        }
        notifyAll();
    }

    /**
     * Remove the first element of the queue
     * 
     * @return the first element of the queue
     */
    public synchronized E remove() {
        while (head == null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }

        }
        Maillon<E> m = head;
        head = head.next;
        if (head == null)
            queue = null;
        return m.e;
    }
}