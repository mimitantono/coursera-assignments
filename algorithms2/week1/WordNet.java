/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Topological;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordNet {

    private final Map<String, List<Integer>> nounMap = new HashMap<>();
    private final String[] synsetMap;
    private final Digraph digraph;

    private final SAP sap;

    private int V;

    // constructor takes the name of the two input files
    public WordNet(String synsets, String hypernyms) {
        if (synsets == null || hypernyms == null) {
            throw new IllegalArgumentException();
        }
        synsetMap = populateSynsets(synsets);
        digraph = populateHypernyms(hypernyms);
        sap = new SAP(digraph);
        DirectedCycle directedCycle = new DirectedCycle(digraph);
        if (directedCycle.hasCycle()) {
            throw new IllegalArgumentException();
        }
        Topological topological = new Topological(digraph);
        if (!topological.hasOrder()) {
            throw new IllegalArgumentException();
        }
    }

    private String[] populateSynsets(String synsets) {
        String[] lines = readFile(synsets);
        String[] synsetMap = new String[lines.length];
        for (String line : lines) {
            String[] split = line.split(",");
            int synsetId = Integer.parseInt(split[0]);

            for (String noun : split[1].split(" ")) {
                addNounsToMap(noun, synsetId);
            }
            synsetMap[synsetId] = split[1];
            V++;
        }
        return synsetMap;
    }

    private void addNounsToMap(String synset, int synsetId) {
        List<Integer> list = nounMap.getOrDefault(synset, new ArrayList<>());
        list.add(synsetId);
        nounMap.put(synset, list);
    }

    private Digraph populateHypernyms(String hypernyms) {
        Digraph G = new Digraph(V);
        for (String line : readFile(hypernyms)) {
            String[] split = line.split(",");
            int synsetId = Integer.parseInt(split[0]);
            for (int i = 1; i < split.length; i++) {
                int hypernym = Integer.parseInt(split[i]);
                G.addEdge(synsetId, hypernym);
            }
        }
        return G;
    }

    private String[] readFile(String filename) {
        return new In(filename).readAllLines();
    }


    // returns all WordNet nouns
    public Iterable<String> nouns() {
        return nounMap.keySet();
    }

    // is the word a WordNet noun?
    public boolean isNoun(String word) {
        if (word == null) {
            throw new IllegalArgumentException();
        }
        return nounMap.containsKey(word);
    }

    // distance between nounA and nounB (defined below)
    public int distance(String nounA, String nounB) {
        verify(nounA, nounB);
        return sap.length(nounMap.get(nounA), nounMap.get(nounB));
    }

    private void verify(String nounA, String nounB) {
        if (!isNoun(nounA) || !isNoun(nounB)) {
            throw new IllegalArgumentException();
        }
    }

    // a synset (second field of synsets.txt) that is the common ancestor of nounA and nounB
    // in a shortest ancestral path (defined below)
    public String sap(String nounA, String nounB) {
        verify(nounA, nounB);
        int ancestor = sap.ancestor(nounMap.get(nounA), nounMap.get(nounB));
        return synsetMap[ancestor];
    }


    // do unit testing of this class
    public static void main(String[] args) {
        WordNet wordNet = new WordNet("synsets.txt", "hypernyms.txt");
        System.out.println(wordNet.sap("bird", "worm"));
    }
}
