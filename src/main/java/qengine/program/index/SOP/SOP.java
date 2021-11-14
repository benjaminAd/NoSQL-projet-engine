package qengine.program.index.SOP;

import qengine.program.index.MyIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SOP extends MyIndex {
    private static SOP instance = null;

    private SOP() {
        super();
    }

    public static SOP getInstance() {
        if (instance == null) {
            instance = new SOP();
        }
        return instance;
    }

    public void add(String subject,String predicate, String object){
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(subjectIndex,objectIndex,predicateIndex);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> getRes(int subject, int predicate, int object) {
        Map<Integer, List<Map<Integer, Integer>>> res = new HashMap<>();
        List<Map<Integer, Integer>> pairObjPredList = tree.get(subject);

        for (Map<Integer, Integer> pairObjPred : pairObjPredList) {
            if (!pairObjPred.containsKey(predicate)) continue;
            res = addElementToMap(subject, res, pairObjPred);
            if (res.containsKey(subject)) res.get(subject).add(pairObjPred);
            else {
                List<Map<Integer, Integer>> list = new ArrayList<>();
                list.add(pairObjPred);
                res.put(subject, list);
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