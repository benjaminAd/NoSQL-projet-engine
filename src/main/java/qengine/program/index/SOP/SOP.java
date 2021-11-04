package qengine.program.index.SOP;

import qengine.program.index.MyIndex;

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
}