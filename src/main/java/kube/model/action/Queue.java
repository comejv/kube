package kube.model.action;

public class Queue<E> {
    Maillon<E> head, queue;

    public Queue() {
        head = queue = null;
    }

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

class Maillon<E> {
    E e;
    Maillon<E> next;
}