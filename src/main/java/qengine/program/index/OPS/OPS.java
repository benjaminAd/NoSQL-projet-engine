package qengine.program.index.OPS;

import qengine.program.index.MyIndex;

import java.util.List;

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
        TIMERS.setIndexesTimer();
        int subjectIndex = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(objectIndex, predicateIndex, subjectIndex);
        TIMERS.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(object,predicate);
    }

    @Override
    public String toString() {
        return "OPS";
    }
}