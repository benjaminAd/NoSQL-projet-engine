package qengine.program.teamengine.index.spo;

import qengine.program.teamengine.index.MyIndex;

import java.util.List;

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

    public void add(String subject, String predicate, String object) {
        TIMERS.setIndexesTimer();
        int subjectIndex = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(subjectIndex, predicateIndex, objectIndex);
        TIMERS.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(subject, predicate);
    }

    @Override
    public List<Integer> secondRes(int subject, int predicate, int object, List<Integer> oldRes) {
        return getSecondResGeneral(subject, predicate, oldRes);
    }

    @Override
    public String toString() {
        return "SPO";
    }
}