package qengine.program;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.Parameter;
import com.opencsv.CSVWriter;
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
import qengine.program.dictionary.Dictionary;
import qengine.program.index.ops.OPS;
import qengine.program.index.osp.OSP;
import qengine.program.index.pos.POS;
import qengine.program.index.pso.PSO;
import qengine.program.index.sop.SOP;
import qengine.program.index.spo.SPO;
import qengine.program.process.ProcessQuery;
import qengine.program.timers.Timers;
import qengine.program.utils.Constants;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;
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
    static final String BASE_URI = null;
    static final Timers TIME = Timers.getInstance();
    static final ProcessQuery PROCESS_QUERY = ProcessQuery.getInstance();
    static List<ParsedQuery> queries = new ArrayList<>();
    private static final Logger logger = LogManager.getLogger(Main.class);

    @Parameter(names = "-queries")
    static String queriesFolder = "";

    @Parameter(names = "-data")
    static String dataFile = "";

    @Parameter(names = "-output")
    static String output = "";

    @Parameter(names = "-export_query_result")
    static String resOutput = "";

    static List<String> resForCSV = new ArrayList<>();
    static List<Integer> resNumberPerQueries = new ArrayList<>();
    static List<String> request = new ArrayList<>();
    //    static List<String> resList = new ArrayList<>();
//    static StringBuilder resString = new StringBuilder("");
//    static StringBuilder nbPatterns = new StringBuilder("Nombre de Patterns");
    static HashMap<Integer, Integer> nbConditions = new HashMap<>();
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
            if (!resOutput.equals("")) resForCSV.add(PROCESS_QUERY.getResWithCsvFormat());
            resNumberPerQueries.add(PROCESS_QUERY.getResSize());
            if(PROCESS_QUERY.getResSize()==0){
                if (nbConditions.containsKey(patterns.size())) {
                    nbConditions.replace(patterns.size(), nbConditions.get(patterns.size()) + 1);
                } else {
                    nbConditions.put(patterns.size(), 1);
                }
            }
            logger.info(PROCESS_QUERY.getRes()+"\n----------------------------------\n");
        } catch (NullPointerException e) {
            logger.info("Un élément dans votre requête n'existe pas dans notre dictionnaire.\n----------------------------------\n");
            resNumberPerQueries.add(0);
            if (!resOutput.equals("")) resForCSV.add("Pas de réponse");
            if (nbConditions.containsKey(patterns.size())) {
                nbConditions.replace(patterns.size(), nbConditions.get(patterns.size()) + 1);
            } else {
                nbConditions.put(patterns.size(), 1);
            }
        }
    }

    public static void processQueries(List<ParsedQuery> queries) {
        TIME.setQueriesProcessTimer();
        queries.forEach(Main::processAQuery);
        TIME.addTimerToQueriesProcess();
    }

    /**
     * Entrée du programme
     */
    public static void main(String[] args) throws Exception {
        TIME.setWorkloadTimer();
        JCommander.newBuilder()
                .addObject(new Main())
                .build()
                .parse(args);
        String log4ConfPath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "main" + File.separator + "resources" + File.separator + "log4j.properties";
        PropertyConfigurator.configure(log4ConfPath);
        if (queriesFolder.equals("") || dataFile.equals("")) {
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
        logger.info("Nom du fichier : {} | Nom du dossier de requête : {} | Nombre de triplets rdf : {} | Nombre de requêtes : {}", getQueriesFolderFileName(dataFile), getQueriesFolderFileName(queriesFolder), Dictionary.getInstance().getNbTriplets(), queries.size());
//        System.out.println(request.size());
//        System.out.println(request.stream().distinct().count());
//        shuyffleQuerySet("queries");
//        System.exit(0);
//        request = request.stream().distinct().collect(Collectors.toList());
//        SPARQLParser sparqlParser = new SPARQLParser();
//        for (int i = 0; i < request.size(); i++) {
//            ParsedQuery query = sparqlParser.parseQuery(request.get(i), BASE_URI);
//            queries.add(query);
//        }
        logger.info("\nVoici les réponses à vos requêtes : \n \n");
        processQueries(queries);
        TIME.addWorkloadTimer();
        if (!output.equals("")) {
            try {
                exportToCSV();
            } catch (Exception e) {
                logger.error(e.getMessage());
                System.exit(1);
            }
        }

        if (!resOutput.equals("")) {
            try {
                exportResToCSV();
            } catch (Exception e) {
                logger.error(e.getMessage());
                System.exit(1);
            }
        }
        logger.info("Nombre de requêtes sans réponses : " + numberOfNoAnswersRequest() + "/" + queries.size());
        logger.info("Nombre de requêtes avec au moins une réponse : " + numberOfRequestWithAnswer() + "/" + queries.size());
        nbConditions.forEach((key,value)->{
            System.out.println("key : "+key+" value : "+value);
        });
        logger.info("Temps de lecture des données : {}ms | Temps de lecture des requêtes : {}ms | Temps de création dico : {}ms | nombre d'index : {} | Temps de créations des index : {}ms | Temps total d'évaluation des requêtes : {}ms | Temps total : {}ms", TIME.getParsingDataTimer(), TIME.getQueriesParsingTimer(), TIME.getDictionaryTimer(), PROCESS_QUERY.getNbIndexUsed(), TIME.getIndexesTimer(), TIME.getQueriesProcessTimer(), TIME.getWorkloadTimer());
    }

    private static void parseQueriesFolder() throws IOException {
        TIME.setQueryParsingTimer();
        File folder = new File(queriesFolder);
        if (folder.isFile()) {
            if (getExtension(folder.getName()).equals("queryset")) parseQueriesFile(folder.getAbsolutePath());
            else throw new IOException(Constants.ERROR_FILE_EXTENSION);
        } else if ((folder.isDirectory()) && (folder.listFiles() != null)) {
            for (final File fileEntry : folder.listFiles()) {
                if (getExtension(fileEntry.getName()).equals("queryset")) parseQueriesFile(fileEntry.getAbsolutePath());
            }
        } else {
            throw new NoSuchFileException(Constants.ERROR_NO_FILE_NO_DIRECTORY);
        }
        TIME.addTimerToQueriesParsing();
    }

    // ========================================================================

    /**
     * Traite chaque requête lue dans {@link #queriesFolder} avec {@link #processAQuery(ParsedQuery)}.
     */
    private static void parseQueriesFile(String queriesFile) throws IOException {
        /**
         * Try-with-resources
         *
         * @see <a href="https://docs.oracle.com/javase/tutorial/essential/exceptions/tryResourceClose.html">Try-with-resources</a>
         */
        /*
         * On utilise un stream pour lire les lignes une par une, sans avoir à toutes les stocker
         * entièrement dans une collection.
         */
        try (Stream<String> lineStream = Files.lines(Paths.get(queriesFile))) {
            SPARQLParser sparqlParser = new SPARQLParser();
            Iterator<String> lineIterator = lineStream.iterator();
            StringBuilder queryString = new StringBuilder();
//            StringBuilder queryString2 = new StringBuilder();

            while (lineIterator.hasNext())
                /*
                 * On stocke plusieurs lignes jusqu'à ce que l'une d'entre elles se termine par un '}'
                 * On considère alors que c'est la fin d'une requête
                 */ {
                String line = lineIterator.next();
                queryString.append(line);
//                queryString2.append(line).append("\n");
                if (line.trim().endsWith("}")) {
                    ParsedQuery query = sparqlParser.parseQuery(queryString.toString(), BASE_URI);
//                    request.add(queryString2.toString());
                    queries.add(query);
//                    queryString2.setLength(0);
                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
        } catch (IOException ioException) {
            throw new IOException(Constants.ERROR_IO);
        }
    }

    /**
     * Traite chaque triple lu dans {@link #dataFile} avec {@link MainRDFHandler}.
     */
    private static void parseData() throws IOException {
        TIME.setParsingDataTimer();
        if (!getExtension(dataFile).equals("nt")) throw new IOException(Constants.ERROR_FILE_EXTENSION);
        try (Reader dataReader = new FileReader(dataFile)) {
            // On va parser des données au format ntriples
            RDFParser rdfParser = Rio.createParser(RDFFormat.NTRIPLES);
            // On utilise notre implémentation de handler
            rdfParser.setRDFHandler(new MainRDFHandler());
            // Parsing et traitement de chaque triple par le handler
            rdfParser.parse(dataReader, BASE_URI);
            OPS.getInstance().sortedByKey();
            OSP.getInstance().sortedByKey();
            POS.getInstance().sortedByKey();
            PSO.getInstance().sortedByKey();
            SOP.getInstance().sortedByKey();
            SPO.getInstance().sortedByKey();
        } catch (IOException ioException) {
            throw new IOException(Constants.ERROR_IO);
        }
        TIME.addParsingDataTimer();
    }

    private static String getExtension(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    private static String getQueriesFolderFileName(String file) {
        File folder = new File(file);
        if (folder.isDirectory() || folder.isFile()) {
            return folder.getName();
        } else {
            return "NON_DISPONIBLE";
        }
    }

    private static void exportToCSV() throws IOException {
        List<String[]> dataCSV = new ArrayList<>(Arrays.asList(
                new String[]{"data", "queries", "triplets_number_rdf", "queries_number", "data_parsing", "queries_parsing", "dico_creation_times", "indexes_used_number", "indexes_creation_time", "queries_process_time", "workload_time"},
                new String[]{getQueriesFolderFileName(dataFile), getQueriesFolderFileName(queriesFolder), String.valueOf(Dictionary.getInstance().getNbTriplets()), String.valueOf(queries.size()), String.valueOf(TIME.getParsingDataTimer()), String.valueOf(TIME.getQueriesParsingTimer()), String.valueOf(TIME.getDictionaryTimer()), String.valueOf(PROCESS_QUERY.getNbIndexUsed()), String.valueOf(TIME.getIndexesTimer()), String.valueOf(TIME.getQueriesProcessTimer()), String.valueOf(TIME.getWorkloadTimer())}
        ));

        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(output))) {
            csvWriter.writeAll(dataCSV);
        } catch (Exception e) {
            throw new IOException(Constants.ERROR_CSV_WRITER);
        }
        logger.info(Constants.CSV_CREATED);
    }

    private static void exportResToCSV() throws IOException {
        List<String[]> dataCSV = new ArrayList<>();
        dataCSV.add(new String[]{"id_request", "res"});
        int i = 0;
        for (String resOfAQuery : resForCSV) {
            dataCSV.add(new String[]{request.get(i), resOfAQuery});
            i += 1;
        }
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter(resOutput))) {
            csvWriter.writeAll(dataCSV);
        } catch (Exception e) {
            throw new IOException(Constants.ERROR_CSV_WRITER);
        }
        logger.info(Constants.CSV_CREATED);
    }

    private static void exportResNumberToCSV() throws IOException {
        List<String[]> dataCSV = new ArrayList<>();
        dataCSV.add(new String[]{"request", "number res"});
        int i = 0;
        for (int number : resNumberPerQueries) {
            dataCSV.add(new String[]{request.get(i), String.valueOf(number)});
            i += 1;
        }
        try (CSVWriter csvWriter = new CSVWriter(new FileWriter("/Users/benjaminadolphe/Downloads/answers500K.csv"))) {
            csvWriter.writeAll(dataCSV);
        } catch (Exception e) {
            throw new IOException(Constants.ERROR_CSV_WRITER);
        }
        logger.info(Constants.CSV_CREATED);
    }

    private static String numberOfNoAnswersRequest() {
        return String.valueOf(queries.size() - resNumberPerQueries.stream().filter(element -> element > 0).count());
    }

    private static String numberOfRequestWithAnswer() {
        return String.valueOf(resNumberPerQueries.stream().filter(element -> element > 0).count());
    }

    private static void saveRequests(String name) throws IOException {
        if (Integer.parseInt(numberOfRequestWithAnswer()) == 0) return;
        int counter = 0;
        FileWriter myFile = new FileWriter("./data/" + name + "_" + counter + ".queryset");
        for (int i = 0; i < resNumberPerQueries.size(); i++) {
            if (i % 1000 == 0 && i != 0) {
                myFile.close();
                counter += 1;
                myFile = new FileWriter("./data/" + name + "_" + counter + ".queryset");
            }
            if (resNumberPerQueries.get(i) > 0) {
                myFile.write(request.get(i));
            }
        }
        myFile.close();
        logger.info("Informations enregistrées");
    }

    private static void doubleQuerySet(String name) throws IOException {
        if (Integer.parseInt(numberOfRequestWithAnswer()) == 0) return;
        for (int i = 0; i < 5; i++) {
            Collections.shuffle(request);
        }
        int counter = 0;
        FileWriter myFile = new FileWriter("./queries4/" + name + "_" + counter + ".queryset");
        int number_of_doublons = 4074;
        int count = 0;
        for (int i = 0; i < resNumberPerQueries.size(); i++) {
            if (count == number_of_doublons) break;
            if (i % 1000 == 0 && i != 0) {
                myFile.close();
                counter += 1;
                myFile = new FileWriter("./queries4/" + name + "_" + counter + ".queryset");
            }
            if (resNumberPerQueries.get(i) > 0) {
                myFile.write(request.get(i));
                count += 1;
            }
        }
        myFile.close();
        logger.info("Informations enregistrées");
    }

    private static void noResponse(String name) throws IOException {
        if (Integer.parseInt(numberOfRequestWithAnswer()) == 0) return;
        int counter = 0;
        FileWriter myFile = new FileWriter("./queries4/" + name + "_" + counter + ".queryset");
        int numbers = 5703;
        int count = 0;
        for (int i = 0; i < resNumberPerQueries.size(); i++) {
//            if (count == numbers) break;
            if (i % 1000 == 0 && i != 0) {
                myFile.close();
                counter += 1;
                myFile = new FileWriter("./queries4/" + name + "_" + counter + ".queryset");
            }
            if (resNumberPerQueries.get(i) > 0) {
                myFile.write(request.get(i));
                count += 1;
            }
        }
        myFile.close();
        logger.info("Informations enregistrées");
    }

    private static void shuyffleQuerySet(String name) throws IOException {
        System.out.println(request.size());
        for (int i = 0; i < 10; i++) {
            Collections.shuffle(request);
        }
        System.out.println(request.stream().distinct().count());
        System.out.println();
        List<String> requestNew = request.stream().distinct().collect(Collectors.toList());
        int counter = 0;
        FileWriter myFile = new FileWriter("./queries2/" + name + "_" + counter + ".queryset");
        for (int i = 0; i < requestNew.size(); i++) {
            if (i % 1000 == 0 && i != 0) {
                myFile.close();
                counter += 1;
                myFile = new FileWriter("./queries2/" + name + "_" + counter + ".queryset");
            }
            myFile.write(requestNew.get(i));
        }
        myFile.close();
        logger.info("Informations enregistrées");
    }

}
