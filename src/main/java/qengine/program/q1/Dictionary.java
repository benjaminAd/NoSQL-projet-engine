package qengine.program.q1;

import org.eclipse.rdf4j.model.Statement;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class Dictionary {

    private static Dictionary instance = null;

    public HashMap<Integer, String> dico;
    public List<Statement> table;
    public List<String> allStatementsSplit;

    private Dictionary() {
        this.dico = new HashMap<>();
        this.table = new ArrayList<>();
        this.allStatementsSplit = new ArrayList<>();
    }

    public void convertToDico() {
        final int[] i = {0};
        List<String> uniquesplitstatment = Dictionary.getInstance().allStatementsSplit.stream().distinct().collect(Collectors.toList());
        uniquesplitstatment.forEach((element) -> {
            dico.put(i[0], element);
            System.out.println(i[0] + ", " + element);
            i[0] += 1;
        });
    }

    public static Dictionary getInstance() {
        if (instance == null) {
            instance = new Dictionary();
        }
        return instance;
    }
}
