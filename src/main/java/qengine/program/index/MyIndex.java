package qengine.program.index;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class MyIndex {
    public Map<Integer, Map<Integer, Integer>> tree;

    public MyIndex() {
        tree = new HashMap<>();
    }

    public void sortedByKey(){
        tree = new TreeMap<>(tree);
    }
}
