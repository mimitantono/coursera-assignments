/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

import java.util.Arrays;

public class FastCollinearPoints {

    private final LineSegment[] lineSegments;

    private final Point[][] resulted;
    private int count;

    public FastCollinearPoints(Point[] points) {
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
        resulted = new Point[points.length][2];
        if (points.length > 1) {
            fastCollinear(points);
        }
    }  // finds all line segments containing 4 points

    private Point[] copyExcept(Point[] points, int skip) {
        Point[] copy = new Point[points.length - 1];
        int index = 0;
        for (int i = 0; i < points.length; i++) {
            if (i != skip) {
                copy[index++] = points[i];
            }
        }
        return copy;
    }

    private void fastCollinear(Point[] points) {
        for (int i = 0; i < points.length; i++) {
            Point[] clone = copyExcept(points, i);
            Arrays.sort(clone, points[i].slopeOrder().thenComparing(Point::compareTo));
            int j = 1;
            int index = 0;
            double slope = clone[0].slopeTo(points[i]);
            Point[] linear = new Point[4];
            while (j < clone.length) {
                if (clone[j].compareTo(points[i]) == 0) {
                    throw new IllegalArgumentException();
                }
                if (clone[j].slopeTo(points[i]) == slope) {
                    linear[index++] = clone[j];
                }
                else {
                    index = 0;
                    slope = clone[j].slopeTo(points[i]);
                    linear[index++] = clone[j];
                }
                if (index == 3) {
                    break;
                }
                j++;
            }
            if (index >= 3) {
                linear[index] = points[i];
                Arrays.sort(linear);
                if (!duplicateSegment(linear[0], linear[3])) {
                    LineSegment lineSegment = new LineSegment(linear[0], linear[3]);
                    resulted[count][0] = linear[0];
                    resulted[count][1] = linear[3];
                    lineSegments[count++] = lineSegment;
                }
            }
        }
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
    }         // the line segments

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
        FastCollinearPoints collinear = new FastCollinearPoints(points);
        for (LineSegment segment : collinear.segments()) {
            StdOut.println(segment);
        }
        System.out.println(collinear.count);
    }

    private boolean duplicateSegment(Point start, Point end) {
        for (int i = 0; i < count; i++) {
            if (resulted[i][0].slopeTo(resulted[i][1]) == start.slopeTo(end)
                    && start.compareTo(resulted[i][0]) == 0) {
                return true;
            }
        }
        return false;
    }
}
