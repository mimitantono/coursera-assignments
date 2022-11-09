/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;
import edu.princeton.cs.algs4.StdStats;

public class PercolationStats {
    private static final double CONFIDENCE_95 = 1.96;
    private double[] trialResult;
    private int trials;

    private double mean = -1;

    private double stddev = -1;

    // perform independent trials on an n-by-n grid
    public PercolationStats(int n, int trials) {
        this.trials = trials;
        this.trialResult = new double[trials];
        for (int i = 0; i < trials; i++) {
            trialResult[i] = runTrial(n);
        }
    }

    private double runTrial(int n) {
        Percolation percolation = new Percolation(n);
        do {
            int row = -1;
            int col = -1;
            while (row < 0 || percolation.isOpen(row, col)) {
                row = StdRandom.uniformInt(1, n + 1);
                col = StdRandom.uniformInt(1, n + 1);
            }
            percolation.open(row, col);
        } while (!percolation.percolates());
        return (double) percolation.numberOfOpenSites() / (n * n);
    }

    // sample mean of percolation threshold
    public double mean() {
        if (mean < 0) {
            mean = StdStats.mean(trialResult);
        }
        return mean;
    }

    // sample standard deviation of percolation threshold
    public double stddev() {
        if (stddev < 0) {
            stddev = StdStats.stddev(trialResult);
        }
        return stddev;
    }

    // low endpoint of 95% confidence interval
    public double confidenceLo() {
        return mean() - (CONFIDENCE_95 * stddev() / Math.sqrt(trials));
    }

    // high endpoint of 95% confidence interval
    public double confidenceHi() {
        return mean() + (CONFIDENCE_95 * stddev() / Math.sqrt(trials));
    }

    // test client (see below)
    public static void main(String[] args) {
        int n = Integer.parseInt(args[0]);
        int trials = Integer.parseInt(args[1]);
        if (n < 0) {
            throw new IllegalArgumentException("n must be a positive number");
        }
        PercolationStats percolationStats = new PercolationStats(n, trials);
        System.out.println("mean                    = " + percolationStats.mean());
        System.out.println("stddev                  = " + percolationStats.stddev());
        System.out.println("95% confidence interval = [" + percolationStats.confidenceLo() + ", "
                                   + percolationStats.confidenceHi() + "]");
    }
}
