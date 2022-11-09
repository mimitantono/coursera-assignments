import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/* *****************************************************************************
 *  Name:              Ada Lovelace
 *  Coursera User ID:  123456
 *  Last modified:     October 16, 1842
 **************************************************************************** */
public class KdTree {

    private Node root;

    public boolean isEmpty() {
        return size() == 0;
    }                  // is the set empty?

    public int size() {
        return size(root);
    }              // number of points in the set

    private Node put(Node x, Point2D p, Node parent) {
        if (x == null) {
            Node node = createNode(p, parent);
            return node;
        }
        int cmp = p.equals(x.point2D) ? 0 : compare(p, x);
        if (cmp < 0) x.left = put(x.left, p, x);
        else if (cmp > 0) x.right = put(x.right, p, x);
        else x.point2D = p;
        x.size = 1 + size(x.left) + size(x.right);
        return x;
    }

    private int compare(Point2D p, Node node) {
        if (isVertical(node)) {
            return p.x() < node.point2D.x() ? -1 : 1;
        }
        return p.y() < node.point2D.y() ? -1 : 1;
    }

    private Node createNode(Point2D p, Node parent) {
        if (parent == null) {
            // horizontal
            return new Node(p, new RectHV(0, 0, 1.0, 1.0), true);
        }
        // vertical, now draw horizontal
        // same vertical wall, split horizontal
        // horizontal, now draw vertical
        // same horizontal wall, split vertical (x)
        if (isVertical(parent)) {
            if (p.x() < parent.point2D.x()) {
                return new Node(p, new RectHV(parent.rectHV.xmin(), parent.rectHV.ymin(),
                                              parent.point2D.x(), parent.rectHV.ymax()), false);
            }
            return new Node(p, new RectHV(parent.point2D.x(), parent.rectHV.ymin(),
                                          parent.rectHV.xmax(), parent.rectHV.ymax()), false);
        }
        if (p.y() < parent.point2D.y()) {
            return new Node(p, new RectHV(parent.rectHV.xmin(), parent.rectHV.ymin(),
                                          parent.rectHV.xmax(), parent.point2D.y()), true);
        }
        else {
            return new Node(p, new RectHV(parent.rectHV.xmin(), parent.point2D.y(),
                                          parent.rectHV.xmax(), parent.rectHV.ymax()), true);
        }
    }

    private static boolean isVertical(Node node) {
        if (node == null) {
            return false;
        }
        return node.vertical;
    }

    private int size(Node node) {
        if (node == null) {
            return 0;
        }
        return node.size;
    }

    public void insert(Point2D p) {
        verify(p);
        root = put(root, p, null);
    }        // add the point to the set (if it is not already in the set)

    public boolean contains(Point2D p) {
        return contains(root, p) != null;
    }      // does the set contain point p?


    private Point2D contains(Node node, Point2D p) {
        if (node == null) {
            return null;
        }
        int cmp = p.equals(node.point2D) ? 0 : compare(p, node);
        if (cmp == 0) {
            return node.point2D;
        }
        if (cmp < 0) {
            return contains(node.left, p);
        }
        else {
            return contains(node.right, p);
        }
    }

    public void draw() {
        draw(root);
    }               // draw all points to standard draw

    private void draw(Node node) {
        node.point2D.draw();
        StdDraw.setPenColor(Color.RED);
        node.rectHV.draw();
        StdDraw.setPenColor(Color.BLACK);
        if (node.left != null) {
            draw(node.left);
        }
        if (node.right != null) {
            draw(node.right);
        }
    }

    private void verify(Object obj) {
        if (obj == null) {
            throw new IllegalArgumentException();
        }
    }

    public Iterable<Point2D> range(RectHV rect) {
        verify(rect);
        return range(root, rect);
    }         // all points that are inside the rectangle (or on the boundary)

    private Iterable<Point2D> range(Node node, RectHV rect) {
        if (node == null) {
            return new ArrayList<>();
        }
        List<Point2D> list = new ArrayList<>();
        if (rect.contains(node.point2D)) {
            list.add(node.point2D);
        }
        if (node.left != null && rect.intersects(node.left.rectHV)) {
            range(node.left, rect).forEach(list::add);
        }
        if (node.right != null && rect.intersects(node.right.rectHV)) {
            range(node.right, rect).forEach(list::add);
        }
        return list;
    }

    public Point2D nearest(Point2D p) {
        verify(p);
        return nearest(root, p, null);
    }        // a nearest neighbor in the set to point p; null if the set is empty

    private Point2D nearest(Node node, Point2D p, Point2D champion) {
        if (champion == null || node.point2D.distanceSquaredTo(p) < champion.distanceSquaredTo(p)) {
            if (node == null) {
                return null;
            }
            champion = node.point2D;
        }
        int cmp = p.equals(champion) ? 0 : compare(p, node);
        if (cmp == 0) {
            return champion;
        }
        if (cmp < 0) {
            champion = findLeft(node, p, champion);
            champion = findRight(node, p, champion);
        }
        else {
            champion = findRight(node, p, champion);
            champion = findLeft(node, p, champion);
        }
        return champion;
    }

    private Point2D findRight(Node node, Point2D p, Point2D champion) {
        if (node.right != null
                && node.right.rectHV.distanceSquaredTo(p) <= champion.distanceSquaredTo(p)) {
            champion = nearest(node.right, p, champion);
        }
        return champion;
    }

    private Point2D findLeft(Node node, Point2D p, Point2D champion) {
        if (node.left != null
                && node.left.rectHV.distanceSquaredTo(p) <= champion.distanceSquaredTo(p)) {
            champion = nearest(node.left, p, champion);
        }
        return champion;
    }

    public static void main(String[] args) {
        KdTree kdTree = new KdTree();
        kdTree.insert(new Point2D(1, 1));
        System.out.println(kdTree.size());
        System.out.println(kdTree.isEmpty());
        kdTree.insert(new Point2D(1, 1));
        System.out.println(kdTree.size());
        System.out.println(kdTree.contains(new Point2D(1, 1)));
        System.out.println(kdTree.contains(new Point2D(1, 2)));
    }

    private static class Node {
        private Point2D point2D;
        private RectHV rectHV;
        private Node left;
        private Node right;
        private int size;
        private boolean vertical;

        public Node(Point2D point2D, RectHV rectHV, boolean vertical) {
            this.point2D = point2D;
            this.rectHV = rectHV;
            this.size = 1;
            this.vertical = vertical;
        }
    }
}
