package qengine.program.index;

import qengine.program.q1.Dictionary;
import qengine.program.timers.Timers;

import java.util.*;

public abstract class MyIndex {
    private Map<Integer, Map<Integer, List<Integer>>> tree;
    protected Dictionary dictionary = Dictionary.getInstance();
    protected static final Timers TIMERS = Timers.getInstance();

    public MyIndex() {
        TIMERS.setIndexesTimer();
        tree = new HashMap<>();
        TIMERS.addTimerToIndexes();
    }

    //  Ajout des indices à l'arbre
    protected void addStatementToIndex(Integer a, Integer b, Integer c) {
        if (tree.containsKey(a)) {
            if (tree.get(a).containsKey(b)) {
                tree.get(a).get(b).add(c);
            } else {
                List<Integer> list = new ArrayList<>();
                list.add(c);
                tree.get(a).put(b, list);
            }
        } else {
            HashMap<Integer, List<Integer>> intermediaire = new HashMap<>();
            List<Integer> list2 = new ArrayList<>();
            list2.add(c);
            intermediaire.put(b, list2);
            tree.put(a, intermediaire);
        }
    }

    protected List<Integer> getResGeneral(Integer i1, Integer i2) {
        List<Integer> res = new ArrayList<>();
        for (Map.Entry<Integer, List<Integer>> pairPredSubject : tree.get(i1).entrySet()) {
            if (!pairPredSubject.getKey().equals(i2)) continue;
            res.addAll(pairPredSubject.getValue());
        }
        return res;
    }


    public void sortedByKey() {
        TIMERS.setIndexesTimer();
        tree = new TreeMap<>(tree);
        TIMERS.addTimerToIndexes();
    }

    //  Ajout d'un triplet selon l'index
    protected abstract void add(String subject, String predicate, String object);

    public abstract List<Integer> getRes(int subject, int predicate, int object);

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for (Map.Entry<Integer, Map<Integer, List<Integer>>> entry : tree.entrySet()) {
            for(Map.Entry<Integer,List<Integer>> entry1 : entry.getValue().entrySet()){
                for (Integer integer : entry1.getValue()) {
                    st.append("<").append(entry.getKey()).append(",").append(entry1.getKey()).append(",").append(integer).append(">\n");

                }
            }
        }
        return st.toString();
    }
}
