package qengine.program;

import org.eclipse.rdf4j.query.algebra.Projection;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.AbstractQueryModelVisitor;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import qengine.program.index.OPS.OPS;
import qengine.program.index.IndexHandler;
import qengine.program.index.OSP.OSP;
import qengine.program.index.POS.POS;
import qengine.program.index.PSO.PSO;
import qengine.program.index.SOP.SOP;
import qengine.program.index.SPO.SPO;
import qengine.program.q1.Dictionary;
import qengine.program.timers.Timer;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Stream;

/**
 * Programme simple lisant un fichier de requête et un fichier de données.
 *
 * <p>
 * Les entrées sont données ici de manière statique,
 * à vous de programmer les entrées par passage d'arguments en ligne de commande comme demandé dans l'énoncé.
 * </p>
 *
 * <p>
 * Le présent programme se contente de vous montrer la voie pour lire les triples et requêtes
 * depuis les fichiers ; ce sera à vous d'adapter/réécrire le code pour finalement utiliser les requêtes et interroger les données.
 * On ne s'attend pas forcémment à ce que vous gardiez la même structure de code, vous pouvez tout réécrire.
 * </p>
 *
 * @author Olivier Rodriguez <olivier.rodriguez1@umontpellier.fr>
 */
final class Main {
    static final String baseURI = null;
    static final Timer time = Timer.getInstance();

    /**
     * Votre répertoire de travail où vont se trouver les fichiers à lire
     */
    static final String workingDir = "data/";

    /**
     * Fichier contenant les requêtes sparql
     */
    static final String queryFile = workingDir + "sample_query.queryset";

    /**
     * Fichier contenant des données rdf
     */
    static final String dataFile = workingDir + "sample_data.nt";

    // ========================================================================

    /**
     * Méthode utilisée ici lors du parsing de requête sparql pour agir sur l'objet obtenu.
     */
    public static void processAQuery(ParsedQuery query) {
        List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());

        System.out.println("first pattern : " + patterns.get(0));

        System.out.println("object of the first pattern : " + patterns.get(0).getObjectVar().getValue());

        System.out.println("variables to project : ");

        // Utilisation d'une classe anonyme
        query.getTupleExpr().visit(new AbstractQueryModelVisitor<RuntimeException>() {

            public void meet(Projection projection) {
                System.out.println(projection.getProjectionElemList().getElements());
            }
        });
    }

    /**
     * Entrée du programme
     */
    public static void main(String[] args) throws Exception {
        parseData();
        createIndexes();
        time.displayTimers();
        parseQueries();
    }

    // ========================================================================

    /**
     * Traite chaque requête lue dans {@link #queryFile} avec {@link #processAQuery(ParsedQuery)}.
     */
    private static void parseQueries() throws FileNotFoundException, IOException {
        /**
         * Try-with-resources
         *
         * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
         */
        /*
         * On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
         * entièrement dans une collection.
         */
        try (Stream<String> lineStream = Files.lines(Paths.get(queryFile))) {
            SPARQLParser sparqlParser = new SPARQLParser();
            Iterator<String> lineIterator = lineStream.iterator();
            StringBuilder queryString = new StringBuilder();

            while (lineIterator.hasNext())
                /*
                 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par un '}'
                 * On considère alors que c'est la fin d'une requête
                 */ {
                String line = lineIterator.next();
                queryString.append(line);

                if (line.trim().endsWith("}")) {
                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), baseURI);

                    processAQuery(query); // Traitement de la requête, à adapter/réécrire pour votre programme

                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
        }
    }

    /**
     * Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
     */
    private static void parseData() throws FileNotFoundException, IOException {

        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new MainRDFHandler());

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, baseURI);
            long start = System.nanoTime();
            Dictionary.getInstance().convertToDico();
            time.addTimerToDictionary((System.nanoTime() - start));
        }
    }

    private static void createIndexes() throws FileNotFoundException, IOException {
        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);

            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new IndexHandler());

            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, baseURI);
            System.out.println("----OPS--------");
            long start = System.nanoTime();
            OPS.getInstance().sortedByKey();
            time.addTimerToIndexes((System.nanoTime() - start));
            OPS.getInstance().tree.forEach((object, children) -> {
                children.forEach((map) -> {
                    map.forEach((property, subject) -> {
                        System.out.println("<" + object + "," + property + "," + subject + ">");
                    });
                });
            });

            System.out.println("----OSP-------");
            start = System.nanoTime();
            OSP.getInstance().sortedByKey();
            time.addTimerToIndexes((System.nanoTime() - start));
            OSP.getInstance().tree.forEach((object, children) -> {
                children.forEach((map) -> {
                    map.forEach((property, subject) -> {
                        System.out.println("<" + object + "," + property + "," + subject + ">");
                    });
                });
            });

            System.out.println("----POS--------");
            start = System.nanoTime();
            POS.getInstance().sortedByKey();
            time.addTimerToIndexes((System.nanoTime() - start));
            POS.getInstance().tree.forEach((object, children) -> {
                children.forEach((map) -> {
                    map.forEach((property, subject) -> {
                        System.out.println("<" + object + "," + property + "," + subject + ">");
                    });
                });
            });

            System.out.println("----PSO--------");
            start = System.nanoTime();
            PSO.getInstance().sortedByKey();
            time.addTimerToIndexes((System.nanoTime() - start));
            PSO.getInstance().tree.forEach((object, children) -> {
                children.forEach((map) -> {
                    map.forEach((property, subject) -> {
                        System.out.println("<" + object + "," + property + "," + subject + ">");
                    });
                });
            });

            System.out.println("----SOP--------");
            start = System.nanoTime();
            SOP.getInstance().sortedByKey();
            time.addTimerToIndexes((System.nanoTime() - start));
            SOP.getInstance().tree.forEach((object, children) -> {
                children.forEach((map) -> {
                    map.forEach((property, subject) -> {
                        System.out.println("<" + object + "," + property + "," + subject + ">");
                    });
                });
            });

            System.out.println("----SPO--------");
            start = System.nanoTime();
            SPO.getInstance().sortedByKey();
            time.addTimerToIndexes((System.nanoTime() - start));
            SPO.getInstance().tree.forEach((object, children) -> {
                children.forEach((map) -> {
                    map.forEach((property, subject) -> {
                        System.out.println("<" + object + "," + property + "," + subject + ">");
                    });
                });
            });
        }
    }
}
