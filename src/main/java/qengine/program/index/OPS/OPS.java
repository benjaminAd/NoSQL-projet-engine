package qengine.program.index.OPS;

import qengine.program.index.MyIndex;

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
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(object,predicate);
    }

    @Override
    public Map<Integer, List<Map<Integer, Integer>>> compareRes(Map<Integer, List<Map<Integer, Integer>>> oldres, Map<Integer, List<Map<Integer, Integer>>> tmp) {
        Map<Integer, List<Map<Integer, Integer>>> res = new HashMap<>();

        return res;
    }
}