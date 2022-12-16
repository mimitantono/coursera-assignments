/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CircularSuffixArray {
    private final int[] indices;

    // circular suffix array of s
    public CircularSuffixArray(String s) {
        if (s == null) {
            throw new IllegalArgumentException();
        }
        Map<String, List<Integer>> map = new HashMap<>();
        String[] suffixes = new String[s.length()];
        for (int i = 0; i < s.length(); i++) {
            suffixes[i] = s.substring(i) + s.substring(0, i);
            List<Integer> list = map.getOrDefault(suffixes[i], new ArrayList<>());
            list.add(i);
            map.put(suffixes[i], list);
        }
        Arrays.sort(suffixes);

        indices = new int[s.length()];
        for (int i = 0; i < s.length(); i++) {
            List<Integer> integers = map.get(suffixes[i]);
            indices[i] = integers.get(0);
            integers.remove(0);
        }
    }

    // length of s
    public int length() {
        return indices.length;
    }

    // returns index of ith sorted suffix
    public int index(int i) {
        if (i < 0 || i > length() - 1) {
            throw new IllegalArgumentException();
        }
        return indices[i];
    }

    // unit testing (required)
    public static void main(String[] args) {
        CircularSuffixArray circularSuffixArray = new CircularSuffixArray("couscous");
        for (int i = 0; i < circularSuffixArray.length(); i++) {
            System.out.println(circularSuffixArray.index(i));
        }
    }

}
