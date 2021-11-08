package qengine.program.index.POS;

import qengine.program.index.MyIndex;

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
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(predicateIndex,objectIndex,subjectIndex);
    }
}