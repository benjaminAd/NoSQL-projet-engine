package qengine.program.index.SPO;

import qengine.program.index.MyIndex;

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
}