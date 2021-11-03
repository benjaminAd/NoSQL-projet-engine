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
}