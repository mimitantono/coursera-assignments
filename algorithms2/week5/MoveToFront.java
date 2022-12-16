/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

public class MoveToFront {

    private static final int EXTENDED_ASCII = 256;

    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode() {
        String s = BinaryStdIn.readString();
        char[] map = createAlphabetMap();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);
            char write = (char) getPosition(map, c);
            BinaryStdOut.write(write);
            moveToFront(map, c);
        }
        BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {
        String s = BinaryStdIn.readString();

        char[] map = createAlphabetMap();

        for (int i = 0; i < s.length(); i++) {
            int d = s.charAt(i);
            BinaryStdOut.write(map[d]);
            moveToFront(map, map[d]);
        }
        BinaryStdOut.flush();
    }

    private static char[] createAlphabetMap() {
        char[] map = new char[EXTENDED_ASCII];
        for (int i = 0; i < EXTENDED_ASCII; i++) {
            map[i] = (char) i;
        }
        return map;
    }

    private static void moveToFront(char[] map, char toMove) {
        if (map[0] == toMove) {
            return;
        }
        int initialPosition = getPosition(map, toMove);

        for (int i = initialPosition; i > 0; i--) {
            map[i] = map[i - 1];
        }
        map[0] = toMove;
    }

    private static int getPosition(char[] map, char c) {
        for (int i = 0; i < map.length; i++) {
            if (map[i] == c) {
                return i;
            }
        }
        throw new IllegalArgumentException();
    }

    // if args[0] is "-", apply move-to-front encoding
    // if args[0] is "+", apply move-to-front decoding
    public static void main(String[] args) {
        if (args.length == 0) {
            throw new IllegalArgumentException();
        }
        if ("-".equals(args[0])) {
            encode();
        }
        else if ("+".equals(args[0])) {
            decode();
        }
        else {
            throw new IllegalArgumentException();
        }
    }
}
