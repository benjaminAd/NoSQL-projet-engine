package qengine.program.index.PSO;

import qengine.program.index.MyIndex;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PSO extends MyIndex {
    private static PSO instance = null;

    private PSO() {
        super();
    }

    public static PSO getInstance() {
        if (instance == null) {
            instance = new PSO();
        }
        return instance;
    }

    public void add(String subject,String predicate, String object){
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(predicateIndex,subjectIndex,objectIndex);
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(predicate,subject);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> compareRes(Map<Integer, List<Map<Integer, Integer>>> res, Map<Integer, List<Map<Integer, Integer>>> tmp) {
        // TODO
        return null;
    }
}