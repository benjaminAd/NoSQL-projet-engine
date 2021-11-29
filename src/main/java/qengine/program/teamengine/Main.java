package qengine.program.teamengine;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;

import org.apache.commons.io.FilenameUtils;
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

import qengine.program.teamengine.index.ops.OPS;
import qengine.program.teamengine.index.osp.OSP;
import qengine.program.teamengine.index.pos.POS;
import qengine.program.teamengine.index.pso.PSO;
import qengine.program.teamengine.index.sop.SOP;
import qengine.program.teamengine.index.spo.SPO;
import qengine.program.teamengine.process.ProcessQuery;
import qengine.program.teamengine.timers.Timers;
import qengine.program.teamengine.utils.Constants;

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

    @Parameter(names = "-queries")
    static String QUERIES_FOLDER = "";

    @Parameter(names = "-data")
    static String DATA_FILE = "";

    @Parameter(names = "-output")
    static String OUTPUT = "";

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
        JCommander.newBuilder()
                .addObject(new Main())
                .build()
                .parse(args);
        String log4ConfPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4ConfPath);
        if (QUERIES_FOLDER.equals("") || DATA_FILE.equals("")) {
            logger.error(Constants.ERROR_NO_ARGUMENTS);
            System.exit(1);
        }
        try {
            parseData();
            parseQueriesFolder();
        } catch (Exception e) {
            logger.error(e.getMessage());
            System.exit(1);
        }
        processQueries(queries);
        TIME.displayTimers();
        logger.info(resStringBuilder);
    }

    private static void parseQueriesFolder() throws Exception {
        File folder = new File(QUERIES_FOLDER);
        if (folder.isFile()) {
            if (getExtension(folder.getName()).equals("queryset")) parseQueriesFile(folder.getAbsolutePath());
            else throw new Exception(Constants.ERROR_FILE_EXTENSION);
        } else if ((folder.isDirectory()) && (folder.listFiles() != null)) {
            for (final File fileEntry : folder.listFiles()) {
                if (getExtension(fileEntry.getName()).equals("queryset")) parseQueriesFile(fileEntry.getAbsolutePath());
            }
        } else {
            throw new Exception(Constants.ERROR_NO_FILE_NO_DIRECTORY);
        }
    }

    // ========================================================================

    /**
     * Traite chaque requête lue dans {@link #QUERIES_FOLDER} avec {@link #processAQuery(ParsedQuery)}.
     */
    private static void parseQueriesFile(String queries_file) throws IOException {
        /**
         * Try-with-resources
         *
         * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
         */
        /*
         * On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
         * entièrement dans une collection.
         */
        try (Stream<String> lineStream = Files.lines(Paths.get(queries_file))) {
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
        } catch (IOException ioException) {
            throw new IOException(Constants.ERROR_IO);
        }
    }

    /**
     * Traite chaque triple lu dans {@link #DATA_FILE} avec {@link MainRDFHandler}.
     */
    private static void parseData() throws IOException {
        if (!getExtension(DATA_FILE).equals("nt")) throw new IOException(Constants.ERROR_FILE_EXTENSION);
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
        } catch (IOException ioException) {
            throw new IOException(Constants.ERROR_IO);
        }
    }

    private static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }
}
