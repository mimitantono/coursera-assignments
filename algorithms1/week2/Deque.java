/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {
    private Node<Item> head;
    private Node<Item> tail;

    // construct an empty deque
    public Deque() {
    }

    // is the deque empty?
    public boolean isEmpty() {
        return head == null;
    }

    // return the number of items on the deque
    public int size() {
        int count = 0;
        Iterator<Item> iterator = iterator();
        while (iterator.hasNext()) {
            iterator.next();
            count++;
        }
        return count;
    }

    // add the item to the front
    public void addFirst(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> add = new Node<>(item);
        add.next = head;
        if (head != null) {
            head.prev = add;
        }
        else {
            tail = add;
        }
        head = add;
    }

    // add the item to the back
    public void addLast(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        Node<Item> add = new Node<>(item);
        add.prev = tail;
        if (tail != null) {
            tail.next = add;
        }
        else {
            head = add;
        }
        tail = add;
    }

    // remove and return the item from the front
    public Item removeFirst() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> first = head;
        if (tail == head) {
            tail = null;
        }
        head = head.next;
        if (head != null) {
            head.prev = null;
        }
        return first.item;
    }

    // remove and return the item from the back
    public Item removeLast() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        Node<Item> last = tail;
        if (tail == head) {
            head = null;
        }
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        }
        return last.item;
    }

    // return an iterator over items in order from front to back
    public Iterator<Item> iterator() {
        return new DequeIterator<>(this);
    }

    // unit testing (required)
    public static void main(String[] args) {
        Deque<Integer> deque = new Deque<>();
        deque.addFirst(2);
        deque.addLast(4);
        deque.addFirst(1);
        deque.addLast(3);

        for (int i : deque) {
            System.out.println(i);
        }

        // 1 2 4 3
        System.out.println("size: " + deque.size());
        System.out.println("isEmpty: " + deque.isEmpty());
        System.out.println("content: " + deque.removeFirst() + ", " + deque.removeFirst());
        System.out.println("content: " + deque.removeLast() + ", " + deque.removeLast());
        System.out.println("isEmpty: " + deque.isEmpty());
        System.out.println("size: " + deque.size());
    }

    private static class DequeIterator<Item> implements Iterator<Item> {
        private Node<Item> current;

        public DequeIterator(Deque<Item> deque) {
            this.current = deque.head;
        }

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (current == null) {
                throw new NoSuchElementException();
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

    private static class Node<Item> {
        Item item;
        Node<Item> next;
        Node<Item> prev;

        public Node(Item item) {
            this.item = item;
        }
    }
}
