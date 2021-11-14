package qengine.program.index.POS;

import qengine.program.index.MyIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class POS extends MyIndex {
    private static POS instance = null;

    private POS() {
        super();
    }

    public static POS getInstance() {
        if (instance == null) {
            instance = new POS();
        }
        return instance;
    }

    public void add(String subject,String predicate, String object){
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(predicateIndex,objectIndex,subjectIndex);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> getRes(int subject, int predicate, int object) {
        Map<Integer, List<Map<Integer, Integer>>> res = new HashMap<>();
        List<Map<Integer, Integer>> pairObjSubList = tree.get(predicate);

        for (Map<Integer, Integer> pairObjSub : pairObjSubList) {
            if (!pairObjSub.containsKey(object)) continue;
            res = addElementToMap(predicate, res, pairObjSub);
            if (res.containsKey(predicate)) res.get(predicate).add(pairObjSub);
            else {
                List<Map<Integer, Integer>> list = new ArrayList<>();
                list.add(pairObjSub);
                res.put(predicate, list);
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