/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.Stack;
import edu.princeton.cs.algs4.StdOut;

import java.util.Comparator;

public class Solver {
    private boolean solvable = true;

    private Step solutionStep = null;

    // find a solution to the initial board (using the A* algorithm)
    public Solver(Board initial) {
        if (initial == null) {
            throw new IllegalArgumentException();
        }
        MinPQ<Step> pq = new MinPQ<>(initial.manhattan(), Step.comparator());
        pq.insert(new Step(0, initial, null));

        Board twin = initial.twin();
        MinPQ<Step> twinPq = new MinPQ<>(twin.manhattan(), Step.comparator());
        twinPq.insert(new Step(0, twin, null));

        while (!pq.isEmpty() && !twinPq.isEmpty()) {
            Step min = currentBoard(pq);
            if (min == null) {
                break;
            }

            Step minTwin = currentBoard(twinPq);
            if (minTwin == null) {
                solvable = false;
                break;
            }
        }
    }

    private Step currentBoard(MinPQ<Step> pq) {
        Step min = pq.min();
        pq.delMin();
        if (min.board.isGoal() || min.move >= 999) { // we clean steps after that
            solutionStep = min;
            return null;
        }
        for (Board neighbor : min.board.neighbors()) {
            if (min.parent == null || !min.parentContains(neighbor)) {
                pq.insert(new Step(min.move + 1, neighbor, min));
            }
        }
        return min;
    }

    // is the initial board solvable? (see below)
    public boolean isSolvable() {
        return solvable;
    }

    // min number of moves to solve initial board; -1 if unsolvable
    public int moves() {
        if (!solvable || solutionStep == null) {
            return -1;
        }
        Step current = solutionStep;
        int count = 0;
        while (current != null) {
            current = current.parent;
            count++;
        }
        return count - 1;
    }

    // sequence of boards in a shortest solution; null if unsolvable
    public Iterable<Board> solution() {
        if (!solvable) {
            return null;
        }
        Stack<Board> path = new Stack<>();
        Step current = solutionStep;
        while (current != null) {
            path.push(current.board);
            current = current.parent;
        }
        return path;
    }

    // test client (see below)
    public static void main(String[] args) {
        int[][] tiles = new int[][] { { 1, 2, 3 }, { 4, 5, 0 }, { 7, 8, 6 } };
        Board board = new Board(tiles);
        Solver solver = new Solver(board);
        System.out.println(solver.moves());
        for (Board step : solver.solution())
            System.out.println(step);
    }

    private static class Step {
        private int move;
        private Board board;

        private Step parent;

        public Step(int move, Board board, Step parent) {
            this.move = move;
            this.board = board;
            this.parent = parent;
        }

        public static Comparator<Step> comparator() {
            return Comparator.comparing(Step::comparation);
        }

        private int comparation() {
            return board.manhattan() + move;
        }


        private int manhattan() {
            return board.manhattan();
        }

        private int move() {
            return move;
        }

        public String toString() {
            return move + " " + manhattan();
        }

        public boolean parentContains(Board neighbor) {
            if (parent.board.equals(neighbor)) {
                StdOut.println("Prevented adding loop");
                return true;
            }
            return false;
        }
    }
}
