package qengine.program.index.SPO;

import qengine.program.index.MyIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SPO extends MyIndex {
    private static SPO instance = null;

    private SPO() {
        super();
    }

    public static SPO getInstance() {
        if (instance == null) {
            instance = new SPO();
        }
        return instance;
    }

    public void add(String subject,String predicate, String object){
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(subjectIndex,predicateIndex,objectIndex);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> getRes(int subject, int predicate, int object) {
        Map<Integer, List<Map<Integer, Integer>>> res = new HashMap<>();
        List<Map<Integer, Integer>> pairPredObjList = tree.get(subject);

        for (Map<Integer, Integer> pairPredObj : pairPredObjList) {
            if (!pairPredObj.containsKey(predicate)) continue;
            res = addElementToMap(subject, res, pairPredObj);
            if (res.containsKey(subject)) res.get(subject).add(pairPredObj);
            else {
                List<Map<Integer, Integer>> list = new ArrayList<>();
                list.add(pairPredObj);
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