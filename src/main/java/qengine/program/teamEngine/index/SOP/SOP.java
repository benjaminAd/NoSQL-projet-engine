package qengine.program.teamEngine.index.SOP;

import qengine.program.teamEngine.index.MyIndex;

import java.util.List;

public class SOP extends MyIndex {
    private static SOP instance = null;

    private SOP() {
        super();
    }

    public static SOP getInstance() {
        if (instance == null) {
            instance = new SOP();
        }
        return instance;
    }

    public void add(String subject, String predicate, String object) {
        TIMERS.setIndexesTimer();
        int subjectIndex = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(subjectIndex, objectIndex, predicateIndex);
        TIMERS.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(subject, object);
    }

    @Override
    public List<Integer> secondRes(int subject, int predicate, int object, List<Integer> oldRes) {
        return getSecondResGeneral(subject, object, oldRes);
    }

    @Override
    public String toString() {
        return "SOP";
    }
}