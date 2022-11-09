/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.ArrayList;
import java.util.List;

public class Board {
    private int[][] tiles;
    private Integer manhattan;
    private Integer hamming;

    private Board twin;

    // create a board from an n-by-n array of tiles,
    // where tiles[row][col] = tile at (row, col)
    public Board(int[][] init) {
        if (init == null) {
            throw new IllegalArgumentException();
        }
        this.tiles = getCloneTiles(init);
    }

    // string representation of this board
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(dimension());
        sb.append("\n");
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                sb.append(String.format("%2s ", tiles[i][j]));
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    // board dimension n
    public int dimension() {
        return tiles.length;
    }

    public int hamming() {
        if (hamming == null) {
            hamming = calculateHamming();
        }
        return this.hamming;
    }

    // number of tiles out of place
    private int calculateHamming() {
        int count = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (outOfPlace(i, j, tiles[i][j])) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean outOfPlace(int i, int j, int tile) {
        if (i == dimension() - 1 && j == dimension() - 1) {
            return false;
        }
        return i * dimension() + j + 1 != tile;
    }

    public int manhattan() {
        if (manhattan == null) {
            manhattan = calculateManhattan();
        }
        return this.manhattan;
    }

    // sum of Manhattan distances between tiles and goal
    private int calculateManhattan() {
        int distance = 0;
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                distance += distance(i, j, tiles[i][j]);
            }
        }
        return distance;
    }

    private int distance(int i, int j, int tile) {
        if (tile == 0) {
            return 0;
        }
        int goalRow = ((tile - 1) / dimension());
        int goalCol = (tile - 1) % dimension();
        return Math.abs(goalRow - i) + Math.abs(goalCol - j);
    }

    // is this board the goal board?
    public boolean isGoal() {
        return hamming() == 0;
    }

    // does this board equal y?
    public boolean equals(Object y) {
        if (y == null || !y.getClass().equals(this.getClass())) {
            return false;
        }
        if (dimension() != ((Board) y).dimension()) {
            return false;
        }
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] != ((Board) y).tiles[i][j]) {
                    return false;
                }
            }
        }
        return true;
    }

    // all neighboring boards
    public Iterable<Board> neighbors() {
        for (int i = 0; i < tiles.length; i++) {
            for (int j = 0; j < tiles.length; j++) {
                if (tiles[i][j] == 0) {
                    return getNeighbors(i, j);
                }
            }
        }
        throw new IllegalArgumentException();
    }

    private List<Board> getNeighbors(int i, int j) {
        List<Board> neighbors = new ArrayList<>();
        Board up = slideUp(i, j);
        if (up != null) neighbors.add(up);
        Board down = slideDown(i, j);
        if (down != null) neighbors.add(down);
        Board left = slideLeft(i, j);
        if (left != null) neighbors.add(left);
        Board right = slideRight(i, j);
        if (right != null) neighbors.add(right);
        return neighbors;
    }

    private Board slideUp(int i, int j) {
        if (i == 0) {
            return null;
        }
        Board board = new Board(tiles);
        board.tiles[i][j] = board.tiles[i - 1][j];
        board.tiles[i - 1][j] = 0;
        return board;
    }

    private Board slideDown(int i, int j) {
        if (i == dimension() - 1) {
            return null;
        }
        Board board = new Board(tiles);
        board.tiles[i][j] = board.tiles[i + 1][j];
        board.tiles[i + 1][j] = 0;
        return board;
    }

    private Board slideLeft(int i, int j) {
        if (j == 0) {
            return null;
        }
        Board board = new Board(tiles);
        board.tiles[i][j] = board.tiles[i][j - 1];
        board.tiles[i][j - 1] = 0;
        return board;
    }

    private Board slideRight(int i, int j) {
        if (j == dimension() - 1) {
            return null;
        }
        Board board = new Board(tiles);
        board.tiles[i][j] = board.tiles[i][j + 1];
        board.tiles[i][j + 1] = 0;
        return board;
    }

    private int[][] getCloneTiles(int[][] toClone) {
        int[][] clonedTiles = new int[toClone.length][toClone.length];
        for (int i = 0; i < toClone.length; i++) {
            for (int j = 0; j < toClone.length; j++) {
                clonedTiles[i][j] = toClone[i][j];
            }
        }
        return clonedTiles;
    }

    // a board that is obtained by exchanging any pair of tiles
    public Board twin() {
        if (twin != null) {
            return twin;
        }
        Board board = new Board(tiles);
        while (true) {
            int row = StdRandom.uniformInt(tiles.length);
            int col = StdRandom.uniformInt(tiles.length);
            int swapRow = StdRandom.uniformInt(tiles.length);
            int swapCol = StdRandom.uniformInt(tiles.length);
            if (row != swapRow && col != swapCol && tiles[row][col] != 0
                    && tiles[swapRow][swapCol] != 0) {
                board.tiles[row][col] = tiles[swapRow][swapCol];
                board.tiles[swapRow][swapCol] = tiles[row][col];
                this.twin = board;
                return twin;
            }
        }
    }

    // unit testing (not graded)
    public static void main(String[] args) {
        int[][] tiles = new int[][] { { 8, 0, 3 }, { 4, 1, 2 }, { 7, 6, 5 } };
        Board board = new Board(tiles);
        System.out.println(board);
        System.out.println("dimension : " + board.dimension());
        System.out.println("hamming   : " + board.hamming());
        System.out.println("manhattan : " + board.manhattan());
        for (Board neighbor : board.neighbors()) {
            System.out.println(neighbor);
        }
        System.out.println(board.twin());
    }

}
