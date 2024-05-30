package kube.model.action;

public class Queue<E> {

    /**********
     * INNER CLASS MAILLON
     **********/

    private class Maillon {
        E element;
        Maillon next;
    }

    /**********
     * ATTRIBUTES
     **********/

    private Maillon head, queue;

    /**********
     * CONSTRUCTOR
     **********/

    /**
     * Constructor of the class Queue
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
     * @param element the element to add
     */
    public synchronized void add(E element) {

        Maillon m;

        m = new Maillon();
        m.element = element;
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

        Maillon m;

        while (head == null) {
            try {
                wait();
            } catch (InterruptedException e) {
            }

        }

        m = head;
        head = head.next;

        if (head == null)
            queue = null;

        return m.element;
    }
}