package qengine.program.jenaengine;

import org.apache.jena.query.*;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Stream;

public class JenaEngine {
    static List<Query> queries = new ArrayList<>();
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

    public static void main(String[] args) throws IOException {
        Model model = ModelFactory.createDefaultModel();
        model.read(DATA_FILE);
        parseQueries();
        for (Query query : queries) {
            QueryExecution execution = QueryExecutionFactory.create(query, model);
            try {
                ResultSet rs = execution.execSelect();
                ResultSetFormatter.out(System.out, rs);
            } finally {
                execution.close();
            }
        }
    }

    private static void parseQueries() throws IOException {
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
                    System.out.println(queryString.toString());
                    queries.add(QueryFactory.create(queryString.toString()));
                    queryString.setLength(0); // Reset le buffer de la requête en chaine vide
                }
            }
        }
    }
}
