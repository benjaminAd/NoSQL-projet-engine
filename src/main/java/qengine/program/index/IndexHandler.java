package qengine.program.index;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import qengine.program.index.OPS.OPS;
import qengine.program.index.OSP.OSP;
import qengine.program.index.POS.POS;
import qengine.program.index.PSO.PSO;
import qengine.program.index.SOP.SOP;
import qengine.program.index.SPO.SPO;
import qengine.program.q1.Dictionary;

import java.util.HashMap;

public class IndexHandler extends AbstractRDFHandler {

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        Dictionary dictionary = Dictionary.getInstance();
        OPS.getInstance().addStatementToIndex(dictionary.getIndexFromElement(st.getObject().stringValue()), dictionary.getIndexFromElement(st.getPredicate().stringValue()), dictionary.getIndexFromElement(st.getSubject().stringValue()));
        OSP.getInstance().addStatementToIndex(dictionary.getIndexFromElement(st.getObject().stringValue()), dictionary.getIndexFromElement(st.getSubject().stringValue()), dictionary.getIndexFromElement(st.getPredicate().stringValue()));
        POS.getInstance().addStatementToIndex(dictionary.getIndexFromElement(st.getPredicate().stringValue()), dictionary.getIndexFromElement(st.getObject().stringValue()), dictionary.getIndexFromElement(st.getSubject().stringValue()));
        PSO.getInstance().addStatementToIndex(dictionary.getIndexFromElement(st.getPredicate().stringValue()), dictionary.getIndexFromElement(st.getSubject().stringValue()), dictionary.getIndexFromElement(st.getObject().stringValue()));
        SOP.getInstance().addStatementToIndex(dictionary.getIndexFromElement(st.getSubject().stringValue()), dictionary.getIndexFromElement(st.getObject().stringValue()), dictionary.getIndexFromElement(st.getPredicate().stringValue()));
        SPO.getInstance().addStatementToIndex(dictionary.getIndexFromElement(st.getSubject().stringValue()), dictionary.getIndexFromElement(st.getPredicate().stringValue()), dictionary.getIndexFromElement(st.getObject().stringValue()));

    }
}
