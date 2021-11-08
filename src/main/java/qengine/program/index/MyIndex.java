package qengine.program.index;
import qengine.program.q1.Dictionary;

import java.util.*;

public abstract class MyIndex {
    public Map<Integer, List<Map<Integer, Integer>>> tree;
    protected Dictionary dictionary = Dictionary.getInstance();

    public MyIndex() {
        tree = new HashMap<>();
    }

    protected void addStatementToIndex(Integer a, Integer b, Integer c) {
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

    protected abstract void add(String subject, String predicate, String object);
}
