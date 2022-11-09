/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] items;
    private int pointer;

    // construct an empty randomized queue
    public RandomizedQueue() {
        items = (Item[]) new Object[10];
    }

    // is the randomized queue empty?
    public boolean isEmpty() {
        return pointer == 0;
    }

    // return the number of items on the randomized queue
    public int size() {
        return pointer;
    }

    private void growOrTrim() {
        // trim
        if (items.length > 10 && pointer < 0.25 * items.length) {
            newArray(items.length / 2);
        }
        // expand
        if (pointer > 0.5 * items.length) {
            newArray(items.length * 2);
        }
    }

    private void newArray(int newSize) {
        Item[] newItems = (Item[]) new Object[newSize];
        for (int i = 0; i <= pointer; i++) {
            newItems[i] = items[i];
        }
        items = newItems;
    }

    // add the item
    public void enqueue(Item item) {
        if (item == null) {
            throw new IllegalArgumentException();
        }
        growOrTrim();
        items[pointer++] = item;
    }

    // remove and return a random item
    public Item dequeue() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        growOrTrim();
        return items[--pointer];
    }

    // return a random item (but do not remove it)
    public Item sample() {
        if (isEmpty()) {
            throw new NoSuchElementException();
        }
        return items[StdRandom.uniformInt(pointer)];
    }

    // return an independent iterator over items in random order
    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator<>(this);
    }

    // unit testing (required)
    public static void main(String[] args) {
        RandomizedQueue<Integer> randomizedQueue = new RandomizedQueue<>();
        for (int i = 0; i < 20; i++) {
            randomizedQueue.enqueue(i);

        }
        System.out.println("content:");
        for (int i : randomizedQueue) {
            System.out.println(i);
        }
        System.out.println("size: " + randomizedQueue.size());
        System.out.println("delete: ");
        while (!randomizedQueue.isEmpty()) {
            System.out.println(randomizedQueue.dequeue());
        }
    }

    private static class RandomizedQueueIterator<Item> implements Iterator<Item> {
        private Item[] items;

        private int[] randomizedIds;
        private int pointer;

        public RandomizedQueueIterator(RandomizedQueue<Item> randomizedQueue) {
            this.items = randomizedQueue.items;
            this.randomizedIds = StdRandom.permutation(randomizedQueue.pointer);
        }

        public boolean hasNext() {
            return pointer < randomizedIds.length;
        }

        public Item next() {
            if (pointer >= randomizedIds.length) {
                throw new NoSuchElementException();
            }
            return items[randomizedIds[pointer++]];
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }
    }

}
