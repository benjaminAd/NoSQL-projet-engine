package qengine.program.index.PSO;

import qengine.program.index.MyIndex;

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
    public Map<Integer, List<Map<Integer, Integer>>> getRes(int subject, int predicate, int object) {
        //TODO
        return null;
    }
}