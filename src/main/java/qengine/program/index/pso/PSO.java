package qengine.program.index.pso;

import qengine.program.index.MyIndex;

import java.util.List;

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

    public void add(String subject, String predicate, String object) {
        TIMERS.setIndexesTimer();
        int subjectIndex = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(predicateIndex, subjectIndex, objectIndex);
        TIMERS.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(predicate, subject);
    }

    @Override
    public List<Integer> secondRes(int subject, int predicate, int object, List<Integer> oldRes) {
        return getSecondResGeneral(predicate, subject, oldRes);
    }

    @Override
    public String toString() {
        return "PSO";
    }
}