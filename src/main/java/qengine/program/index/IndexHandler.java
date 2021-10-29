package qengine.program.index;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.RDFHandlerException;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import qengine.program.index.OPS.OPS;
import qengine.program.q1.Dictionary;

import java.util.HashMap;

public class IndexHandler extends AbstractRDFHandler {

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        HashMap<Integer, Integer> OPShash_map = new HashMap<>();
        OPShash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        OPS.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), OPShash_map);
    }
}
