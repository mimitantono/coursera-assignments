/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class BruteCollinearPoints {

    private final LineSegment[] lineSegments;
    private int count = 0;
    private final Point[] resulted;

    public BruteCollinearPoints(Point[] points) {
        if (points == null) {
            throw new IllegalArgumentException();
        }
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
        }
        Arrays.sort(points);
        Point prev = null;
        for (int i = 0; i < points.length; i++) {
            if (prev != null && points[i].compareTo(prev) == 0) {
                throw new IllegalArgumentException();
            }
            prev = points[i];
        }
        lineSegments = new LineSegment[points.length];
        resulted = new Point[points.length];
        bruteForceCollinear(points);
    }  // finds all line segments containing 4 points

    private void bruteForceCollinear(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            if (points[i] == null) {
                throw new IllegalArgumentException();
            }
            for (int j = i + 1; j < points.length; j++) {
                for (int k = j + 1; k < points.length; k++) {
                    for (int l = k + 1; l < points.length; l++) {
                        if (points[i].slopeTo(points[j]) == points[j].slopeTo(points[k])
                                && points[j].slopeTo(points[k]) == points[k].slopeTo(points[l])) {
                            if (!duplicateSegment(points[i])) {
                                resulted[count] = points[i];
                                lineSegments[count++] = new LineSegment(points[i], points[l]);
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean duplicateSegment(Point start) {
        for (int i = 0; i < count; i++) {
            if (resulted[i].compareTo(start) == 0) {
                return true;
            }
        }
        return false;
    }

    public int numberOfSegments() {
        return count;
    }      // the number of line segments

    public LineSegment[] segments() {
        LineSegment[] result = new LineSegment[count];
        for (int i = 0; i < count; i++) {
            result[i] = lineSegments[i];
        }
        return result;
    }            // the line segments

    public static void main(String[] args) {

        // read the n points from a file
        In in = new In(args[0]);
        int n = in.readInt();
        Point[] points = new Point[n];
        for (int i = 0; i < n; i++) {
            int x = in.readInt();
            int y = in.readInt();
            points[i] = new Point(x, y);
        }
        // print and draw the line segments
        BruteCollinearPoints collinear = new BruteCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
    }
}
