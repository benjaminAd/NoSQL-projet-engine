package qengine.program.index.OPS;

import qengine.program.index.MyIndex;

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

    public void add(String subject,String predicate, String object){
        int subjectIndex  = dictionary.getIndexFromElement(subject);
        int predicateIndex = dictionary.getIndexFromElement(predicate);
        int objectIndex = dictionary.getIndexFromElement(object);
        this.addStatementToIndex(objectIndex,predicateIndex,subjectIndex);
    }
}