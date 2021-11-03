package qengine.program.index;

import java.util.*;

public class MyIndex {
    public Map<Integer, List<Map<Integer, Integer>>> tree;

    public MyIndex() {
        tree = new HashMap<>();
    }

    public void addStatementToIndex(Integer a, Integer b, Integer c) {
        HashMap<Integer, Integer> intermediaire = new HashMap<>();
        intermediaire.put(b, c);
        if (tree.containsKey(a)) {
            tree.get(a).add(intermediaire);
        } else {
            List<Map<Integer, Integer>> intermediairelist = new ArrayList<>();
            intermediairelist.add(intermediaire);
            tree.put(a, intermediairelist);
        }

    }

    public void sortedByKey() {
        tree = new TreeMap<>(tree);
    }
}
