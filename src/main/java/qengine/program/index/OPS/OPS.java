package qengine.program.index.OPS;

import qengine.program.utils.Triplet;

import java.util.ArrayList;
import java.util.List;

public class OPS {
    private static OPS instance = null;
    public List<Triplet> triplets;

    private OPS() {
        this.triplets = new ArrayList<>();
    }

    public static OPS getInstance() {
        if (instance == null) {
            instance = new OPS();
        }
        return instance;
    }
}
