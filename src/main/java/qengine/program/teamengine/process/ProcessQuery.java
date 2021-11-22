package qengine.program.teamengine.process;

import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import qengine.program.teamengine.index.MyIndex;
import qengine.program.teamengine.index.ops.OPS;
import qengine.program.teamengine.index.osp.OSP;
import qengine.program.teamengine.index.pos.POS;
import qengine.program.teamengine.index.pso.PSO;
import qengine.program.teamengine.index.sop.SOP;
import qengine.program.teamengine.index.spo.SPO;
import qengine.program.teamengine.dictionary.Dictionary;

import java.util.List;
import java.util.stream.Collectors;

public class ProcessQuery {
    private static ProcessQuery instance = null;
    private String unknownName;
    private String subjectValue = "";
    private String predicateValue = "";
    private String objectValue = "";
    private int subjectIndex = -1;
    private int predicateIndex = -1;
    private int objectIndex = -1;
    private final Dictionary dictionary = Dictionary.getInstance();
    private MyIndex index;
    private List<Integer> res;

    private ProcessQuery() {

    }

    public String getUnknownName() {
        return unknownName;
    }

    public void setUnknownName(String unknownName) {
        this.unknownName = unknownName;
    }

    public String getRes() {
        String result = (res.isEmpty()) ? "Aucune Réponse n'a été trouvé." : displayRes(res.stream().map(dictionary::getElementFromIndex).collect(Collectors.toList()).stream().distinct().collect(Collectors.toList()));
        res.clear();
        return result;
    }

    public String displayRes(List<String> list) {
        StringBuilder st = new StringBuilder("Voici les résultats de votre requêtes\n");
        for (int i = 0; i < list.size(); i++) {
            st.append(list.get(i));
            if (i != list.size() - 1) st.append("\n");
        }
        return st.toString();
    }

    private void resetValues() {
        subjectValue = "";
        predicateValue = "";
        objectValue = "";
        subjectIndex = -1;
        predicateIndex = -1;
        objectIndex = -1;
    }

    private void statementToValue(StatementPattern statementPattern) {
        Var subject = statementPattern.getSubjectVar();
        Var predicate = statementPattern.getPredicateVar();
        Var object = statementPattern.getObjectVar();
        if (subject.getValue() != null) subjectValue = subject.getValue().stringValue();
        if (predicate.getValue() != null) predicateValue = predicate.getValue().stringValue();
        if (object.getValue() != null) objectValue = object.getValue().stringValue();
        getIndex(subjectValue, predicateValue, objectValue);
    }

    public void setFirstTriplets(StatementPattern statementPattern) {
        this.statementToValue(statementPattern);
        setIndex();

    }

    private void getIndex(String subject, String predicate, String object) {
        if (!subject.equals("")) subjectIndex = dictionary.getIndexFromElement(subject);
        if (!predicate.equals("")) predicateIndex = dictionary.getIndexFromElement(predicate);
        if (!object.equals("")) objectIndex = dictionary.getIndexFromElement(object);
    }

    private void setIndex() {
        if (subjectIndex != -1) {
            if (predicateIndex != -1) {
                index = (subjectIndex < predicateIndex) ? SPO.getInstance() : PSO.getInstance();
                return;
            }
            if (objectIndex != -1) {
                index = (subjectIndex < objectIndex) ? SOP.getInstance() : OSP.getInstance();
                return;
            }
        }
        if (objectIndex != -1) {
            index = (objectIndex < predicateIndex) ? OPS.getInstance() : POS.getInstance();
        }
    }

    public void solve(List<StatementPattern> statementPatterns) {
        firstRes();
        this.resetValues();
        if (!statementPatterns.isEmpty()) {
            for (StatementPattern statementPattern : statementPatterns) {
                setIndex();
                otherRes(statementPattern);
                this.resetValues();
                if (res.isEmpty()) break;
            }
        }
    }

    private void otherRes(StatementPattern statementPattern) {
        this.statementToValue(statementPattern);
        res = index.secondRes(subjectIndex, predicateIndex, objectIndex, res);
    }

    private void firstRes() {
        res = index.getRes(subjectIndex, predicateIndex, objectIndex);
    }


    public static ProcessQuery getInstance() {
        if (instance == null) instance = new ProcessQuery();
        return instance;
    }
}
