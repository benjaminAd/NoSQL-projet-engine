package qengine.program.index.OSP;

import qengine.program.index.MyIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OSP extends MyIndex {
    private static OSP instance = null;

    private OSP() {
        super();
    }

    public static OSP getInstance() {
        if (instance == null) {
            instance = new OSP();
        }
        return instance;
    }

    public void add(String subject,String predicate, String object){
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(objectIndex,subjectIndex,predicateIndex);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> getRes(int subject, int predicate, int object) {
        Map<Integer, List<Map<Integer, Integer>>> res = new HashMap<>();
        List<Map<Integer, Integer>> pairSubPredList = tree.get(object);

        for (Map<Integer, Integer> pairSubPred : pairSubPredList) {
            if (!pairSubPred.containsKey(subject)) continue;
            res = addElementToMap(object, res, pairSubPred);
            if (res.containsKey(object)) res.get(object).add(pairSubPred);
            else {
                List<Map<Integer, Integer>> list = new ArrayList<>();
                list.add(pairSubPred);
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