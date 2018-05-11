import Utils.ServerConnection;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.Literal;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.UnsupportedEncodingException;

public class SkillExtractor {
    private static Logger rootLogger;


    public static void main(String[] args) throws UnsupportedEncodingException {
        String rdfFile = "C:\\Users\\Moudi\\Downloads\\NewSkillOnto.rdf";
        Model model = ModelFactory.createDefaultModel();
        model.read(rdfFile);

        String query = "PREFIX j:<http://www.w3.org/2004/02/skos/core#>" +
                "SELECT (STR(?skill) AS ?sk) WHERE {?source j:prefLabel ?skill}";
        ServerConnection serverConnection = new ServerConnection();
        try(QueryExecution qexec = QueryExecutionFactory.create(query,model)){
            ResultSet results = qexec.execSelect();
            for(;results.hasNext();){
                QuerySolution soln = results.nextSolution() ;
                Literal skill = soln.getLiteral("sk");
                Gson gson = new Gson();

                String s = gson.toJson(skill.getString());
                JsonObject skillJ = new JsonObject();
                skillJ.addProperty("skill", s);
                serverConnection.postToServer("http://localhost:3000/skill",skillJ.toString());
            }
        }
    }

    private static void configureLogger() {
        // This is the root logger provided by log4j
        rootLogger = Logger.getRootLogger();
        rootLogger.setLevel(org.apache.log4j.Level.INFO);

        // Define log pattern layout
        PatternLayout layout = new PatternLayout("%d{ISO8601} [%t] %-5p %c %x - %m%n");

        // Add console appender to root logger
        if (!rootLogger.getAllAppenders().hasMoreElements()) {
            rootLogger.addAppender(new ConsoleAppender(layout));
        }
    }
}
