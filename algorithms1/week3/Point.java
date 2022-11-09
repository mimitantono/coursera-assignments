/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */

import java.util.Comparator;

public class Point implements Comparable<Point> {

    private final int x;
    private final int y;

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }                  // constructs the point (x, y)

    public void draw() {

    }                  // draws this point

    public void drawTo(Point that) {

    }                 // draws the line segment from this point to that point

    public String toString() {
        return "[" + x + "," + y + "]";
    }                       // string representation

    public int compareTo(Point that) {
        if (this.y == that.y) {
            if (this.x == that.x) {
                return 0;
            }
            return this.x > that.x ? 1 : -1;
        }
        return this.y > that.y ? 1 : -1;
    }  // compare two points by y-coordinates, breaking ties by x-coordinates

    public double slopeTo(Point that) {
        double yElement = (double) that.y - this.y;
        double xElement = (double) that.x - this.x;
        double slope = yElement / xElement;
        if (xElement == 0 || yElement == 0) {
            slope = Math.abs(slope);
        }
        return Double.isNaN(slope) ? Double.NEGATIVE_INFINITY : slope;
    }    // the slope between this point and that point

    public Comparator<Point> slopeOrder() {
        return Comparator.comparing(x -> this.slopeTo(x));
    }
}
