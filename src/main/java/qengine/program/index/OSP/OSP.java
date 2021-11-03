package qengine.program.index.OSP;

import qengine.program.index.MyIndex;

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
}