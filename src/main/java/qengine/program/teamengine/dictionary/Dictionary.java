package qengine.program.teamengine.dictionary;

import qengine.program.teamengine.timers.Timers;

import java.util.HashMap;
import java.util.Map;

public class Dictionary {

    private static Dictionary instance = null;

    private final HashMap<String, Integer> dico;
    private final HashMap<Integer, String> dicoFromIndex;
    private int dicoIndex = 1;
    private static final Timers TIMERS = Timers.getInstance();

    private int counterTriplets = 0;

    private Dictionary() {
        TIMERS.setDictionaryTimer();
        this.dico = new HashMap<>();
        this.dicoFromIndex = new HashMap<>();
        TIMERS.addTimerToDictionary();
    }

    //  Création du dictionnaire
    public void add(String s) {
        TIMERS.setDictionaryTimer();
        if (!dico.containsKey(s)) {
            dico.put(s, dicoIndex);
            dicoFromIndex.put(dicoIndex, s);
            dicoIndex += 1;
        }
        TIMERS.addTimerToDictionary();
    }

    public String getElementFromIndex(int index) {
        return this.dicoFromIndex.get(index);
    }

    public Integer getIndexFromElement(String element) {
        if (this.dico.containsKey(element)) return this.dico.get(element);
        throw new NullPointerException();
    }

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }

    public void incrementCounter() {
        this.counterTriplets += 1;
    }

    public int getNbTriplets() {
        return this.counterTriplets;
    }

    @Override
    public String toString() {
        StringBuilder st = new StringBuilder();
        for (Map.Entry<String, Integer> entry : dico.entrySet()) {
            st.append("<").append("\"").append(entry.getKey()).append("\"").append(",").append(entry.getValue()).append(">");
            if (!entry.getKey().equals(dico.entrySet().toArray()[dico.entrySet().size() - 1])) {
                st.append("\n");
            }
        }
        return st.toString();
    }
}
