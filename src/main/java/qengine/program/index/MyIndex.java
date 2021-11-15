package qengine.program.index;

import qengine.program.q1.Dictionary;

import java.util.*;

public abstract class MyIndex {
    public Map<Integer, List<Map<Integer, Integer>>> tree;
    protected Dictionary dictionary = Dictionary.getInstance();

    public MyIndex() {
        tree = new HashMap<>();
    }

    //  Ajout des indices à l'arbre
    protected void addStatementToIndex(Integer a, Integer b, Integer c) {
        HashMap<Integer, Integer> intermediaire = new HashMap<>();
        intermediaire.put(b, c);
        addElementToMap(a, tree, intermediaire);

    }

    protected Map<Integer, List<Map<Integer, Integer>>> addElementToMap(Integer a, Map<Integer, List<Map<Integer, Integer>>> map,Map<Integer, Integer> intermediaire) {
        if (map.containsKey(a)) {
            map.get(a).add(intermediaire);
        } else {
            List<Map<Integer, Integer>> intermediairelist = new ArrayList<>();
            intermediairelist.add(intermediaire);
            map.put(a, intermediairelist);
        }
        return map;
    }

    protected List<Integer> getResGeneral(Integer i1,Integer i2){
        List<Integer> res = new ArrayList<>();
        List<Map<Integer, Integer>> pairPredSubList = tree.get(i1);
        for (Map<Integer, Integer> pairPredSubject : pairPredSubList) {
            if (!pairPredSubject.containsKey(i2)) continue;
            res.add(pairPredSubject.get(i2));
        }
        return res;
    }


    public void sortedByKey() {
        tree = new TreeMap<>(tree);
    }

    //  Ajout d'un triplet selon l'index
    protected abstract void add(String subject, String predicate, String object);

    public abstract List<Integer> getRes(int subject, int predicate, int object);

    public abstract Map<Integer, List<Map<Integer, Integer>>> compareRes(Map<Integer, List<Map<Integer, Integer>>> res, Map<Integer, List<Map<Integer, Integer>>> tmp);

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for (Map.Entry<Integer, List<Map<Integer, Integer>>> entry : tree.entrySet()) {
            for (Map<Integer, Integer> integerIntegerMap : entry.getValue()) {
                for (Map.Entry<Integer, Integer> entry1 : integerIntegerMap.entrySet()) {
                    st.append("<").append(entry.getKey()).append(",").append(entry1.getKey()).append(",").append(entry1.getValue()).append(">");
                    if (!entry1.getKey().equals(integerIntegerMap.entrySet().toArray()[integerIntegerMap.entrySet().size() - 1])) {
                        st.append("\n");
                    }
                }
            }
        }
        return st.toString();
    }
}
