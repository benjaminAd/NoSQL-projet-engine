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
import qengine.program.timers.Timer;

public class IndexHandler extends AbstractRDFHandler {

    @Override
    public void handleStatement(Statement st) throws RDFHandlerException {
        long start = System.nanoTime();
        String subject = st.getSubject().stringValue();
        String predicate = st.getPredicate().stringValue();
        String object = st.getObject().stringValue();
        OPS.getInstance().add(subject, predicate, object);
        OSP.getInstance().add(subject, predicate, object);
        PSO.getInstance().add(subject, predicate, object);
        POS.getInstance().add(subject, predicate, object);
        SOP.getInstance().add(subject, predicate, object);
        SPO.getInstance().add(subject, predicate, object);
        Timer.getInstance().addTimerToIndexes((System.nanoTime() - start));
    }
}
