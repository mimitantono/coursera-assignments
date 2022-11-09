/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> strings = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            strings.enqueue(StdIn.readString());
        }
        for (String s : strings) {
            if (--k < 0) {
                break;
            }
            System.out.println(s);
        }
    }
}
