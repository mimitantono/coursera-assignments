/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SAP {

    private final Digraph digraph;

    // constructor takes a digraph (not necessarily a DAG)
    public SAP(Digraph G) {
        this.digraph = new Digraph(G);
    }

    // length of shortest ancestral path between v and w; -1 if no such path
    public int length(int v, int w) {
        return length(Collections.singletonList(v), Collections.singletonList(w));
    }

    // a common ancestor of v and w that participates in a shortest ancestral path; -1 if no such path
    public int ancestor(int v, int w) {
        return ancestor(Collections.singletonList(v), Collections.singletonList(w));
    }

    // length of shortest ancestral path between any vertex in v and any vertex in w; -1 if no such path
    public int length(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int minLength = Integer.MAX_VALUE;
        for (CAP cap : commonAncestralPath(v, w)) {
            if (cap.path.size() < minLength) {
                minLength = cap.path.size();
            }
        }
        return minLength == Integer.MAX_VALUE ? -1 : minLength - 1;
    }

    // a common ancestor that participates in shortest ancestral path; -1 if no such path
    public int ancestor(Iterable<Integer> v, Iterable<Integer> w) {
        if (v == null || w == null) {
            throw new IllegalArgumentException();
        }
        int min = Integer.MAX_VALUE;
        int ancestral = -1;
        for (CAP cap : commonAncestralPath(v, w)) {
            if (cap.path.size() < min) {
                min = cap.path.size();
                ancestral = cap.ancestral;
            }
        }
        return ancestral;
    }

    private List<CAP> commonAncestralPath(Iterable<Integer> v, Iterable<Integer> w) {
        BreadthFirstDirectedPaths bfs = new BreadthFirstDirectedPaths(digraph, v);
        BreadthFirstDirectedPaths bfsReversed = new BreadthFirstDirectedPaths(digraph, w);

        List<Integer> commonPath = new ArrayList<>();
        for (int i = 0; i < digraph.V(); i++) {
            if (bfs.hasPathTo(i) && bfsReversed.hasPathTo(i)) {
                commonPath.add(i);
            }
        }

        List<CAP> paths = new ArrayList<>();
        for (int c : commonPath) {
            paths.add(getCap(bfs.pathTo(c), bfsReversed.pathTo(c), c));
        }
        return paths;
    }

    private CAP getCap(Iterable<Integer> forward, Iterable<Integer> back, int ancestor) {
        List<Integer> path = new ArrayList<>();
        forward.forEach(path::add);
        Stack<Integer> stack = new Stack<>();
        back.forEach(stack::push);
        while (!stack.isEmpty()) {
            int x = stack.pop();
            if (x != ancestor) {
                path.add(x);
            }
        }
        return new CAP(ancestor, path);
    }

    private static class CAP {
        private final int ancestral;
        private List<Integer> path;

        public CAP(int ancestral, List<Integer> path) {
            this.ancestral = ancestral;
            this.path = path;
        }
    }

    // do unit testing of this class
    public static void main(String[] args) {
        In in = new In("digraph3.txt");
        Digraph G = new Digraph(in);
        SAP sap = new SAP(G);
        int v = 10;
        int w = 7;
        int length = sap.length(v, w);
        int ancestor = sap.ancestor(v, w);
        StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
    }
}
