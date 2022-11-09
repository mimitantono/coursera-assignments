/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

public class Percolation {
    private boolean[][] site;
    private int openCount;

    private int m;
    private WeightedQuickUnionUF q;

    // creates n-by-n grid, with all sites initially blocked
    public Percolation(int n) {
        if (n < 0) {
            throw new IllegalArgumentException();
        }
        this.m = n * (n + 2) + 2;
        this.site = new boolean[n + 2][n];

        q = new WeightedQuickUnionUF(m);
        for (int i = 1; i <= n; i++) {
            site[0][i - 1] = true;
            site[n + 1][i - 1] = true;
            q.union(0, toIndex(0, i));
            q.union(m - 1, toIndex(n + 1, i));
        }
    }

    // opens the site (row, col) if it is not open already
    public void open(int row, int col) {
        if (!valid(row, col) || row <= 0 || row >= site.length - 1) {
            throw new IllegalArgumentException();
        }
        if (!site[row][col - 1]) {
            openCount++;
        }
        site[row][col - 1] = true;
        if (isOpen(row, col) && valid(row - 1, col) && isOpen(row - 1, col)) {
            q.union(toIndex(row, col), toIndex(row - 1, col));
        }
        if (isOpen(row, col) && valid(row + 1, col) && isOpen(row + 1, col)) {
            q.union(toIndex(row, col), toIndex(row + 1, col));
        }
        if (isOpen(row, col) && valid(row, col - 1) && isOpen(row, col - 1)) {
            q.union(toIndex(row, col), toIndex(row, col - 1));
        }
        if (isOpen(row, col) && valid(row, col + 1) && isOpen(row, col + 1)) {
            q.union(toIndex(row, col), toIndex(row, col + 1));
        }
    }

    private boolean valid(int row, int col) {
        if (row < 0 || row > site.length - 1 || col < 1 || col > site.length - 2) {
            return false;
        }
        return true;
    }

    // is the site (row, col) open?
    public boolean isOpen(int row, int col) {
        if (!valid(row, col)) {
            throw new IllegalArgumentException();
        }
        return site[row][col - 1];
    }

    // is the site (row, col) full?
    public boolean isFull(int row, int col) {
        return q.find(0) == q.find(toIndex(row, col));
    }

    // returns the number of open sites
    public int numberOfOpenSites() {
        return openCount;
    }

    // does the system percolate?
    public boolean percolates() {
        return q.find(0) == q.find(m - 1);
    }

    private int toIndex(int row, int col) {
        return (row) * (site.length - 2) + col;
    }

    // test client (optional)
    public static void main(String[] args) {
        Percolation percolation = new Percolation(3);
        System.out.println(percolation.isOpen(1, 1));
        System.out.println(percolation.isFull(1, 1));
        percolation.open(1, 3);
        System.out.println(percolation.isOpen(1, 3));
        System.out.println(percolation.isFull(1, 3));
    }
}
