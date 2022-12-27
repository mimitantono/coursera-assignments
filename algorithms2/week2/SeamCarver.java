import edu.princeton.cs.algs4.Picture;

/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */
public class SeamCarver {
    private final double[][] energy;
    private final int[][] colors;

    private int width;
    private int height;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {
        this.width = picture.width();
        this.height = picture.height();
        this.colors = createColor(picture);
        this.energy = calculateEnergy();
    }

    private int[][] createColor(Picture picture) {
        int[][] color = new int[width()][height()];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                color[i][j] = picture.getRGB(i, j);
            }
        }
        return color;
    }

    private double[][] initDistTo() {
        double[][] distTo = new double[width()][height()];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                distTo[i][j] = Double.POSITIVE_INFINITY;
            }
        }
        return distTo;
    }

    private double[][] calculateEnergy() {
        double[][] arr = new double[width()][height()];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                arr[i][j] = calculateEnergy(i, j);
            }
        }
        return arr;
    }

    // current picture
    public Picture picture() {
        Picture picture = new Picture(width, height);
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                picture.setRGB(i, j, colors[i][j]);
            }
        }
        return picture;
    }

    // width of current picture
    public int width() {
        return width;
    }

    // height of current picture
    public int height() {
        return height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {
        if (x < 0 || y < 0 || x >= width || y >= height) {
            throw new IllegalArgumentException();
        }
        return energy[x][y];
    }

    private double calculateEnergy(int x, int y) {
        if (isBorder(x, y)) {
            return 1000d;
        }

        double deltaX = Math.pow((getRed(x - 1, y) - getRed(x + 1, y)), 2);
        deltaX += Math.pow((getGreen(x - 1, y) - getGreen(x + 1, y)), 2);
        deltaX += Math.pow((getBlue(x - 1, y) - getBlue(x + 1, y)), 2);
        double deltaY = Math.pow((getRed(x, y - 1) - getRed(x, y + 1)), 2);
        deltaY += Math.pow((getGreen(x, y - 1) - getGreen(x, y + 1)), 2);
        deltaY += Math.pow((getBlue(x, y - 1) - getBlue(x, y + 1)), 2);
        return Math.sqrt(deltaX + deltaY);
    }

    private int getGreen(int x, int y) {
        return (colors[x][y] >> 8) & 0xFF;
    }

    private int getRed(int x, int y) {
        return (colors[x][y] >> 16) & 0xFF;
    }

    private int getBlue(int x, int y) {
        return (colors[x][y] >> 0) & 0xFF;
    }

    private boolean isBorder(int x, int y) {
        return x <= 0 || y <= 0 || x >= width() - 1 || y >= height() - 1;
    }

    private void relax(double[][] distTo, Edge[][] edge, Pixel from, Pixel to) {
        if (!isValid(to)) {
            return;
        }
        if (distTo[to.x][to.y] > distTo[from.x][from.y] + energy[to.x][to.y]) {
            distTo[to.x][to.y] = distTo[from.x][from.y] + energy[to.x][to.y];
            edge[to.x][to.y] = new Edge(from, to);
        }
    }

    private boolean isValid(Pixel pixel) {
        return pixel.x >= 0 && pixel.y >= 0 && pixel.x < width && pixel.y < height;
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {
        double[][] distTo = initDistTo();
        Edge[][] edge = new Edge[width][height];
        for (int j = 0; j < height(); j++) {
            distTo[0][j] = energy[0][j];
            edge[0][j] = new Edge(new Pixel(0, j), new Pixel(0, j));
        }

        for (int i = 0; i < width(); i++) {
            for (int j = 0; j < height(); j++) {
                relax(distTo, edge, new Pixel(i, j), new Pixel(i + 1, j - 1));
                relax(distTo, edge, new Pixel(i, j), new Pixel(i + 1, j));
                relax(distTo, edge, new Pixel(i, j), new Pixel(i + 1, j + 1));
            }
        }

        // find minimum path
        int minRow = getMinimumRow(distTo);
        int[] seam = new int[width];
        Edge e = edge[width - 1][minRow];
        int index = width - 1;
        while (index >= 0) {
            seam[index--] = e.to.y;
            e = edge[e.from.x][e.from.y];
        }
        return seam;
    }

    private int getMinimumRow(double[][] distTo) {
        int minDest = 0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < height; i++) {
            if (distTo[width - 1][i] < min) {
                min = distTo[width - 1][i];
                minDest = i;
            }
        }
        return minDest;
    }

    private int getMinimumCol(double[][] distTo) {
        int minDest = 0;
        double min = Double.POSITIVE_INFINITY;
        for (int i = 0; i < width; i++) {
            if (distTo[i][height - 1] < min) {
                min = distTo[i][height - 1];
                minDest = i;
            }
        }
        return minDest;
    }

    // sequence of indices for vertical seam
    // improve speed by reusing distTo, edge, only need to re-relax the +/- x from seam?
    public int[] findVerticalSeam() {
        double[][] distTo = initDistTo();
        Edge[][] edge = new Edge[width][height];
        for (int j = 0; j < width(); j++) {
            distTo[j][0] = energy[j][0];
            edge[j][0] = new Edge(new Pixel(j, 0), new Pixel(j, 0));
        }

        for (int j = 0; j < height(); j++) {
            for (int i = 0; i < width(); i++) {
                relax(distTo, edge, new Pixel(i, j), new Pixel(i - 1, j + 1));
                relax(distTo, edge, new Pixel(i, j), new Pixel(i, j + 1));
                relax(distTo, edge, new Pixel(i, j), new Pixel(i + 1, j + 1));
            }
        }

        // find minimum path
        int minCol = getMinimumCol(distTo);
        int[] seam = new int[height];
        Edge e = edge[minCol][height - 1];
        int index = height - 1;
        while (index >= 0) {
            seam[index--] = e.to.x;
            e = edge[e.from.x][e.from.y];
        }
        return seam;
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null || !isValidSeam(seam, width, height)) {
            throw new IllegalArgumentException();
        }
        height--;
        for (int i = 0; i < seam.length; i++) {
            for (int j = seam[i]; j < height; j++) {
                colors[i][j] = colors[i][j + 1];
                energy[i][j] = energy[i][j + 1];
            }
	    //BUG! it's not enough to only calculate on seam
            energy[i][seam[i]] = calculateEnergy(i, seam[i]);
        }
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {
        if (seam == null || !isValidSeam(seam, height, width)) {
            throw new IllegalArgumentException();
        }
        width--;
        for (int i = 0; i < seam.length; i++) {
            for (int j = seam[i]; j < width; j++) {
                colors[j][i] = colors[j + 1][i];
            }
	    //BUG! it's not enough to only calculate on seam
            energy[seam[i]][i] = calculateEnergy(seam[i], i);
        }
    }

    private boolean isValidSeam(int[] seam, int length, int range) {
        if (range <= 1) {
            throw new IllegalArgumentException();
        }
        if (seam.length == length) {
            for (int i = 1; i < length; i++) {
                if (seam[i] < 0 || seam[i] > range - 1) {
                    throw new IllegalArgumentException();
                }
                if (Math.abs(seam[i] - seam[i - 1]) > 1) {
                    return false;
                }
            }
            return true;
        }
        else {
            return false;
        }
    }

    private static class Edge {
        private final Pixel from;
        private final Pixel to;

        public Edge(Pixel from, Pixel to) {
            this.from = from;
            this.to = to;
        }
    }

    private static class Pixel {
        private final int x;
        private final int y;

        public Pixel(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }

    public static void main(String[] args) {
        SeamCarver seamCarver = new SeamCarver(new Picture("10x10.png"));
        seamCarver.removeHorizontalSeam(new int[] { 8, 9, 10, 9, 9, 9, 8, 7, 6, 6 });
    }
}
