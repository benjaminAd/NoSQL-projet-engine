package qengine.program.process;

import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.Var;
import qengine.program.index.MyIndex;
import qengine.program.index.OPS.OPS;
import qengine.program.index.OSP.OSP;
import qengine.program.index.POS.POS;
import qengine.program.index.PSO.PSO;
import qengine.program.index.SOP.SOP;
import qengine.program.index.SPO.SPO;
import qengine.program.q1.Dictionary;

import java.util.List;
import java.util.Map;

public class ProcessQuery {
    private static ProcessQuery instance = null;
    private String unknownName;
    private String subjectValue = null, predicateValue = null, objectValue = null;
    private int subjectIndex = -1, predicateIndex = -1, objectIndex = -1;
    private Dictionary dictionary = Dictionary.getInstance();
    private MyIndex index;
    private Map<Integer, List<Map<Integer, Integer>>> res;

    private ProcessQuery() {

    }

    public String getUnknownName() {
        return unknownName;
    }

    public void setUnknownName(String unknownName) {
        this.unknownName = unknownName;
    }

    public void setFirstTriplets(StatementPattern statementPattern) {
        Var subject = statementPattern.getSubjectVar();
        Var predicate = statementPattern.getPredicateVar();
        Var object = statementPattern.getObjectVar();
        if (!(subject.getName().equals(unknownName))) subjectValue = subject.getValue().stringValue();
        if (!(predicate.getName().equals(unknownName))) predicateValue = predicate.getValue().stringValue();
        if (!(object.getName().equals(unknownName))) objectValue = object.getValue().stringValue();

        getIndex(subjectValue, predicateValue, objectValue);
        setIndex();
    }

    private void getIndex(String subject, String predicate, String object) {
        if (subject != null) subjectIndex = dictionary.getIndexFromElement(subject);
        if (predicate != null) predicateIndex = dictionary.getIndexFromElement(predicate);
        if (object != null) objectIndex = dictionary.getIndexFromElement(object);
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
        //TODO --> Résultat des autres patterns
        if (!statementPatterns.isEmpty()) {
            for (StatementPattern statementPattern : statementPatterns) {
                otherRes(statementPattern);
            }
        }
    }

    private void otherRes(StatementPattern statementPattern) {
        this.getIndex(statementPattern.getSubjectVar().getValue().stringValue(),
                statementPattern.getPredicateVar().getValue().stringValue(),
                statementPattern.getObjectVar().getValue().stringValue());
        Map<Integer, List<Map<Integer, Integer>>> tmp = index.getRes(subjectIndex, predicateIndex, objectIndex);

        // TODO: faire des trucs
    }

    private void firstRes() {
        res = index.getRes(subjectIndex, predicateIndex, objectIndex);
    }


    public static ProcessQuery getInstance() {
        if (instance == null) instance = new ProcessQuery();
        return instance;
    }
}
