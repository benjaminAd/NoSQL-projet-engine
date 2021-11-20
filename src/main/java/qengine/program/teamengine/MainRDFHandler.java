package qengine.program.teamengine;

import org.eclipse.rdf4j.model.Statement;
import org.eclipse.rdf4j.rio.helpers.AbstractRDFHandler;
import qengine.program.teamengine.index.OPS.OPS;
import qengine.program.teamengine.index.OSP.OSP;
import qengine.program.teamengine.index.POS.POS;
import qengine.program.teamengine.index.PSO.PSO;
import qengine.program.teamengine.index.SOP.SOP;
import qengine.program.teamengine.index.SPO.SPO;
import qengine.program.teamengine.q1.Dictionary;

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
        Dictionary dictionary = Dictionary.getInstance();
        String subject = st.getSubject().stringValue();
        String predicate = st.getPredicate().stringValue();
        String object = st.getObject().stringValue();

        //Add dictionary
        dictionary.add(subject);
        dictionary.add(object);
        dictionary.add(predicate);

        //Create indexes
        OPS.getInstance().add(subject, predicate, object);
        OSP.getInstance().add(subject, predicate, object);
        PSO.getInstance().add(subject, predicate, object);
        POS.getInstance().add(subject, predicate, object);
        SOP.getInstance().add(subject, predicate, object);
        SPO.getInstance().add(subject, predicate, object);
    }

}