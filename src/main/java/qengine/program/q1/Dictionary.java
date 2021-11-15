package qengine.program.q1;

import qengine.program.timers.Timer;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Dictionary {

    private static Dictionary instance = null;

    private final HashMap<String, Integer> dico;
    private final HashMap<Integer, String> dicoFromIndex;
    //  Une liste de tout les éléments de chaque triplets
    private final List<String> allStatementsSplit;
    private static final Timer TIMER = Timer.getInstance();

    private Dictionary() {
        TIMER.setTimer();
        this.dico = new HashMap<>();
        this.dicoFromIndex = new HashMap<>();
        this.allStatementsSplit = new ArrayList<>();
        TIMER.addTimerToDictionary();
    }

    //  Création du dictionnaire
    public void convertToDico() {
        TIMER.setTimer();
        int dicoIndex = 1;
        List<String> elements = allStatementsSplit.stream().distinct().collect(Collectors.toList());
        for (String element : elements) {
            dico.put(element, dicoIndex);
            dicoFromIndex.put(dicoIndex, element);
            dicoIndex += 1;
        }
        allStatementsSplit.clear();
        TIMER.addTimerToDictionary();
    }

    public void add(String s) {
        TIMER.setTimer();
        this.allStatementsSplit.add(s);
        TIMER.addTimerToDictionary();
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
