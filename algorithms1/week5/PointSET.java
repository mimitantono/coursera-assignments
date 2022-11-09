import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

import java.util.Comparator;
import java.util.TreeSet;
import java.util.stream.Collectors;

/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */
public class PointSET {
    private TreeSet<Point2D> treeSet = new TreeSet<Point2D>();

    public PointSET() {

    }                          // construct an empty set of points

    public boolean isEmpty() {
        return treeSet.isEmpty();
    }                  // is the set empty?

    public int size() {
        return treeSet.size();
    }              // number of points in the set

    public void insert(Point2D p) {
        verify(p);
        treeSet.add(p);
    }        // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        verify(p);
        return treeSet.contains(p);
    }      // does the set contain point p?

    private void verify(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public void draw() {
        treeSet.forEach(Point2D::draw);
    }               // draw all points to standard draw

    public Iterable<Point2D> range(RectHV rect) {
        verify(rect);
        return treeSet.stream().filter(rect::contains).collect(Collectors.toList());
    }         // all points that are inside the rectangle (or on the boundary)

    public Point2D nearest(Point2D p) {
        verify(p);
        return treeSet.stream().min(Comparator.comparing(x -> x.distanceSquaredTo(p))).orElse(null);
    }        // a nearest neighbor in the set to point p; null if the set is empty

    public static void main(String[] args) {

    }          // unit testing of the methods (optional)
}
