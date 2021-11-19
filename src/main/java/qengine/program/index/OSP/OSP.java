package qengine.program.index.OSP;

import qengine.program.index.MyIndex;

import java.util.List;

public class OSP extends MyIndex {
    private static OSP instance = null;

    private OSP() {
        super();
    }

    public static OSP getInstance() {
        if (instance == null) {
            instance = new OSP();
        }
        return instance;
    }

    public void add(String subject,String predicate, String object){
        TIMERS.setIndexesTimer();
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(objectIndex,subjectIndex,predicateIndex);
        TIMERS.addTimerToIndexes();
    }

    @Override
    public List<Integer> getRes(int subject, int predicate, int object) {
        return getResGeneral(object,subject);
    }

    @Override
    public String toString() {
        return "OSP";
    }
}