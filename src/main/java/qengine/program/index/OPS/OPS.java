package qengine.program.index.OPS;

import qengine.program.index.MyIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OPS extends MyIndex {
    private static OPS instance = null;

    private OPS() {
        super();
    }

    public static OPS getInstance() {
        if (instance == null) {
            instance = new OPS();
        }
        return instance;
    }

    public void add(String subject, String predicate, String object) {
        int subjectIndex = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(objectIndex, predicateIndex, subjectIndex);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> getRes(int subject, int predicate, int object) {
        Map<Integer, List<Map<Integer, Integer>>> res = new HashMap<>();
        List<Map<Integer, Integer>> pairPredSubList = tree.get(object);
        for (Map<Integer, Integer> pairPredSubject : pairPredSubList) {
            if (!pairPredSubject.containsKey(predicate)) continue;
            res = addElementToMap(object, res, pairPredSubject);
            if (res.containsKey(object)) res.get(object).add(pairPredSubject);
            else {
                List<Map<Integer, Integer>> list = new ArrayList<>();
                list.add(pairPredSubject);
                res.put(object, list);
            }
        }
        return res;
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> compareRes(Map<Integer, List<Map<Integer, Integer>>> res, Map<Integer, List<Map<Integer, Integer>>> tmp) {
        // TODO
        return null;
    }
}