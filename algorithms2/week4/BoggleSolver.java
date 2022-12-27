/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class BoggleSolver {
    private final String[] dictionary;

    private final Node dictTrie;

    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) {
        this.dictionary = dictionary.clone();
        this.dictTrie = buildDictionaryTrie();
    }

    private Node buildDictionaryTrie() {
        Node root = new Node('0');
        for (String s : this.dictionary) {
            insert(root, s);
        }
        return root;
    }

    private void insert(Node root, String s) {
        Node curr = root;
        char[] chars = s.toCharArray();
        for (int i = 0; i < chars.length; i++) {
            char c = chars[i];
            int index = getIndex(c);
            if (curr.children[index] == null) {
                curr.children[index] = new Node(c);
            }
            curr = curr.children[index];
            if (c == 'Q') {
                i++;
            }
        }

    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board) {
        Map<Dice, Set<Dice>> graph = buildGraph(board);
        Node node = buildTrie(board, graph);
        List<String> validwords = new ArrayList<>();
        
        // Optimization! instead of going word by word, use dictTrie and no need
        // to go further if it already failed on the prefix
        for (String s : dictionary) {
            if (isValid(node, s.toCharArray())) {
                if (s.length() > 2) {
                    validwords.add(s);
                }
            }
        }
        return validwords;
    }

    private boolean isValid(Node node, char[] chars) {
        return isValid(node, chars, true);
    }

    private boolean isValid(Node node, char[] chars, boolean handleQ) {
        Node curr = node;
        for (int i = 0; i < chars.length; i++) {
            int index = getIndex(chars[i]);
            if (curr.children[index] == null) {
                return false;
            }
            curr = curr.children[index];
            if (handleQ && chars[i] == 'Q') {
                i++;
                if (i > chars.length - 1 || chars[i] != 'U') {
                    return false;
                }
            }
        }
        return true;
    }

    private Map<Dice, Set<Dice>> buildGraph(BoggleBoard board) {
        Map<Dice, Set<Dice>> dices = new HashMap<>();
        for (int i = 0; i < board.rows(); i++) {
            for (int j = 0; j < board.cols(); j++) {
                HashSet<Dice> adj = new HashSet<>();
                dices.put(new Dice(i, j), adj);
                addIfValid(adj, i - 1, j, board.rows(), board.cols()); // up
                addIfValid(adj, i + 1, j, board.rows(), board.cols()); // down
                addIfValid(adj, i, j - 1, board.rows(), board.cols()); // left
                addIfValid(adj, i, j + 1, board.rows(), board.cols()); // right
                addIfValid(adj, i - 1, j - 1, board.rows(), board.cols()); // up-left
                addIfValid(adj, i - 1, j + 1, board.rows(), board.cols()); // up-right
                addIfValid(adj, i + 1, j - 1, board.rows(), board.cols()); // down-left
                addIfValid(adj, i + 1, j + 1, board.rows(), board.cols()); // down-right
            }
        }
        return dices;
    }

    private void addIfValid(Set<Dice> dices, int i, int j, int rows, int cols) {
        if (i >= 0 && j >= 0 && i < rows && j < cols) {
            dices.add(new Dice(i, j));
        }
    }

    private Node buildTrie(BoggleBoard board, Map<Dice, Set<Dice>> graph) {
        Node root = new Node('0');
        for (Dice dice : graph.keySet()) {
            buildPossiblePaths(root, dice, new ArrayList<>(), board, graph);
        }
        return root;
    }

    private Node buildPossiblePaths(Node node, Dice dice, List<Dice> visited, BoggleBoard board,
                                    Map<Dice, Set<Dice>> graph) {
        if (visited.contains(dice)) {
            return node;
        }
        char c = board.getLetter(dice.i, dice.j);
        int index = getIndex(c);
        if (node.children[index] == null) {
            node.children[index] = new Node(c);
        }
        visited.add(dice);
        if (!isUsefulPrefix(visited, board)) {
            return node;
        }
        Set<Dice> possiblePath = graph.get(dice);
        for (Dice next : possiblePath) {
            buildPossiblePaths(node.children[index], next, new ArrayList<>(visited), board, graph);
        }
        return node;
    }

    private boolean isUsefulPrefix(List<Dice> visited, BoggleBoard board) {
        return isValidRawQ(dictTrie, getChars(visited, board));
    }

    private boolean isValidRawQ(Node dictTrie, char[] chars) {
        return isValid(dictTrie, chars, false);
    }

    private char[] getChars(List<Dice> visited, BoggleBoard board) {
        char[] chars = new char[visited.size()];
        for (int i = 0; i < visited.size(); i++) {
            Dice dice = visited.get(i);
            chars[i] = board.getLetter(dice.i, dice.j);
        }
        return chars;
    }

    private static int getIndex(char board) {
        return board - 'A';
    }

    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word) {
        if (!isValid(dictTrie, word.toCharArray())) {
            return 0;
        }
        if (word.length() <= 2) {
            return 0;
        }
        else if (word.length() <= 4) {
            return 1;
        }
        else if (word.length() <= 5) {
            return 2;
        }
        else if (word.length() <= 6) {
            return 3;
        }
        else if (word.length() <= 7) {
            return 5;
        }
        return 11;
    }

    private static class Node {
        private Node[] children = new Node[26];
        private char c;

        private Node(char c) {
            this.c = c;
        }

        public String toString() {
            return String.valueOf(c);
        }
    }

    private static class Dice {
        private final int i;
        private final int j;

        private Dice(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Dice dice = (Dice) o;
            return i == dice.i && j == dice.j;
        }

        public int hashCode() {
            return Objects.hash(i, j);
        }

        public String toString() {
            return i + "," + j;
        }
    }
}
