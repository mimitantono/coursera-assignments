/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class BurrowsWheeler {

    // apply Burrows-Wheeler transform,
    // reading from standard input and writing to standard output
    public static void transform() {
        String s = BinaryStdIn.readString();
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray(s);
        int index = 0;
        char[] burrows = new char[s.length()];
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            if (circularSuffixArray.index(i) == 0) {
                index = i;
                burrows[i] = s.charAt(s.length() - 1);
            }
            else {
                burrows[i] = s.charAt(circularSuffixArray.index(i) - 1);
            }
        }
        BinaryStdOut.write(index);
        for (char c : burrows) {
            BinaryStdOut.write(c);
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler inverse transform,
    // reading from standard input and writing to standard output
    public static void inverseTransform() {
    }

    // if args[0] is "-", apply Burrows-Wheeler transform
    // if args[0] is "+", apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException();
        }
        if ("-".equals(args[0])) {
            transform();
        }
        else if ("+".equals(args[0])) {
            inverseTransform();
        }
        else {
            throw new IllegalArgumentException();
        }
    }

}
