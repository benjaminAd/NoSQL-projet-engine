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
        HashMap<Integer, Integer> OPShash_map = new HashMap<>();
        HashMap<Integer, Integer> OSPhash_map = new HashMap<>();
        HashMap<Integer, Integer> POShash_map = new HashMap<>();
        HashMap<Integer, Integer> PSOhash_map = new HashMap<>();
        HashMap<Integer, Integer> SOPhash_map = new HashMap<>();
        HashMap<Integer, Integer> SPOhash_map = new HashMap<>();

        OPShash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        OPS.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), OPShash_map);

        OSPhash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        OSP.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), OSPhash_map);

        POShash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        POS.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), POShash_map);

        PSOhash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        PSO.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), PSOhash_map);

        SOPhash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        SOP.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), SOPhash_map);

        SPOhash_map.put(Dictionary.getInstance().dico.get(st.getPredicate().stringValue()), Dictionary.getInstance().dico.get(st.getSubject().stringValue()));
        SPO.getInstance().tree.put(Dictionary.getInstance().dico.get(st.getObject().stringValue()), SPOhash_map);
    }
}
