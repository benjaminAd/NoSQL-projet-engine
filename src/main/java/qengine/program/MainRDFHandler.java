package qengine.program;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import qengine.program.index.OPS.OPS;
import qengine.program.index.OSP.OSP;
import qengine.program.index.POS.POS;
import qengine.program.index.PSO.PSO;
import qengine.program.index.SOP.SOP;
import qengine.program.index.SPO.SPO;
import qengine.program.q1.Dictionary;
import qengine.program.timers.Timer;

/**
 * Le RDFHandler intervient lors du parsing de données et permet d'appliquer un traitement pour chaque élément lu par le parseur.
 *
 * <p>
 * Ce qui servira surtout dans le programme est la méthode {@link #handleStatement(Statement)} qui va permettre de traiter chaque triple lu.
 * </p>
 * <p>
 * À adapter/réécrire selon vos traitements.
 * </p>
 */
public final class MainRDFHandler extends AbstractRDFHandler {

    @Override
    public void handleStatement(Statement st) {
        long start = System.nanoTime();
        Dictionary dictionary = Dictionary.getInstance();
        String subject = st.getSubject().stringValue();
        String predicate = st.getPredicate().stringValue();
        String object = st.getObject().stringValue();

        //Add dictionary
        dictionary.add(subject);
        dictionary.add(object);
        dictionary.add(predicate);
        Timer.getInstance().addTimerToDictionary((System.nanoTime() - start));
    }

    ;

}