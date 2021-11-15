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
    public List<Integer> getRes(int subject, int predicate, int object) {
       return getResGeneral(subject,predicate);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> compareRes(Map<Integer, List<Map<Integer, Integer>>> res, Map<Integer, List<Map<Integer, Integer>>> tmp) {
        // TODO
        return null;
    }
}