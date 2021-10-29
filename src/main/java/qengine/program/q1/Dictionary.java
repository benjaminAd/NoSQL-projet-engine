package qengine.program.q1;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {

    private static Dictionary instance = null;

    public HashMap<Integer, String> dico;
    public List<String> allStatementsSplit;

    private Dictionary() {
        this.dico = new HashMap<>();
        this.allStatementsSplit = new ArrayList<>();
    }

    public void convertToDico() {
        final int[] dicoIndex = {0};
        List<String> elements = Dictionary.getInstance().allStatementsSplit.stream().distinct().collect(Collectors.toList());
        elements.forEach((element) -> {
            dico.put(dicoIndex[0], element);
            dicoIndex[0] += 1;
        });
    }

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }
}
