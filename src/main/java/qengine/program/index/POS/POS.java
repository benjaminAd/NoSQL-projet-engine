package qengine.program.index.POS;

import qengine.program.index.MyIndex;

import java.util.List;

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
        TIMERS.setIndexesTimer();
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(predicateIndex,objectIndex,subjectIndex);
        TIMERS.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(predicate,object);
    }

    @Override
    public String toString() {
        return "POS";
    }
}