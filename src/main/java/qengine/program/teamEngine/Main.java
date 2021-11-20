package qengine.program;

import org.apache.log4j.PropertyConfigurator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.rdf4j.query.algebra.StatementPattern;
import org.eclipse.rdf4j.query.algebra.helpers.StatementPatternCollector;
import org.eclipse.rdf4j.query.parser.ParsedQuery;
import org.eclipse.rdf4j.query.parser.sparql.SPARQLParser;
import org.eclipse.rdf4j.rio.RDFFormat;
import org.eclipse.rdf4j.rio.RDFParser;
import org.eclipse.rdf4j.rio.Rio;
import qengine.program.teamEngine.index.OPS.OPS;
import qengine.program.teamEngine.index.OSP.OSP;
import qengine.program.teamEngine.index.POS.POS;
import qengine.program.teamEngine.index.PSO.PSO;
import qengine.program.teamEngine.index.SOP.SOP;
import qengine.program.teamEngine.index.SPO.SPO;
import qengine.program.process.ProcessQuery;
import qengine.program.teamEngine.timers.Timers;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
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
    static final String BASE_UR = null;
    static final Timers TIME = Timers.getInstance();
    static final ProcessQuery PROCESS_QUERY = ProcessQuery.getInstance();
    static List<ParsedQuery> queries = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Main.class);

    /**
     * Votre répertoire de travail où vont se trouver les fichiers à lire
     */
    static final String WORKING_DIR = "data/";

    /**
     * Fichier contenant les requêtes sparql
     */
    static final String QUERY_FILE = WORKING_DIR + "request.queryset";

    /**
     * Fichier contenant des données rdf
     */
    static final String DATA_FILE = WORKING_DIR + "100K.nt";

    static final StringBuilder resStringBuilder = new StringBuilder();
    // ========================================================================

    /**
     * Méthode utilisée ici lors du parsing de requête sparql pour agir sur l'objet obtenu.
     */
    public static void processAQuery(ParsedQuery query) {
        List<StatementPattern> patterns = StatementPatternCollector.process(query.getTupleExpr());
        try {
            PROCESS_QUERY.setFirstTriplets(patterns.get(0));
            patterns.remove(0);
            PROCESS_QUERY.solve(patterns);
            resStringBuilder.append(PROCESS_QUERY.getRes()).append("\n----------------------------------\n");
        } catch (NullPointerException e) {
            resStringBuilder.append("Un élément dans votre requête n'existe pas dans notre dictionnaire.\n----------------------------------\n");
        }
    }

    public static void processQueries(List<ParsedQuery> queries) {
        queries.forEach(Main::processAQuery);
    }

    /* Remarque du prof
    - Sauvegarder le dictionnaire (pour pas que l'on le recrée à chaque fois)
     */

    /**
     * Entrée du programme
     */
    public static void main(String[] args) throws Exception {
        String log4ConfPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4ConfPath);

        parseData();
        parseQueries();
        processQueries(queries);
        TIME.displayTimers();
        logger.info(resStringBuilder);
    }

    // ========================================================================

    /**
     * Traite chaque requête lue dans {@link #QUERY_FILE} avec {@link #processAQuery(ParsedQuery)}.
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
        try (Stream<String> lineStream = Files.lines(Paths.get(QUERY_FILE))) {
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
                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), BASE_UR);

                    queries.add(query);

                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
        }
    }

    /**
     * Traite chaque triple lu dans {@link #DATA_FILE} avec {@link MainRDFHandler}.
     */
    private static void parseData() throws FileNotFoundException, IOException {

        try (Reader dataReader = new FileReader(DATA_FILE)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);
            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new MainRDFHandler());
            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, BASE_UR);
            OPS.getInstance().sortedByKey();
            OSP.getInstance().sortedByKey();
            POS.getInstance().sortedByKey();
            PSO.getInstance().sortedByKey();
            SOP.getInstance().sortedByKey();
            SPO.getInstance().sortedByKey();
        }
    }
}
