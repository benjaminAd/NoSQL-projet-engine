package qengine.program.index.SOP;

import qengine.program.index.MyIndex;

import java.util.List;
import java.util.Map;

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

    public void add(String subject,String predicate, String object){
        TIMER.setTimer();
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(subjectIndex,objectIndex,predicateIndex);
        TIMER.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(subject,object);
    }
}