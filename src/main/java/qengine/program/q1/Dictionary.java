package qengine.program.q1;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {

    private static Dictionary instance = null;

    private final HashMap<String, Integer> dico;
    private final HashMap<Integer, String> dicoFromIndex;
    private final List<String> allStatementsSplit;

    private Dictionary() {
        this.dico = new HashMap<>();
        this.dicoFromIndex = new HashMap<>();
        this.allStatementsSplit = new ArrayList<>();
    }

    public void convertToDico() {
        int dicoIndex = 1;
        List<String> elements = allStatementsSplit.stream().distinct().collect(Collectors.toList());
        for (String element : elements) {
            dico.put(element, dicoIndex);
            dicoFromIndex.put(dicoIndex, element);
            dicoIndex += 1;
        }
        allStatementsSplit.clear();
    }

    public void add(String s){
        this.allStatementsSplit.add(s);
    }

    public String getElementFromIndex(int index) {
        return this.dicoFromIndex.get(index);
    }

    public Integer getIndexFromElement(String element) {
        return this.dico.get(element);
    }

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }
}
