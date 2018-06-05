import Collectors.OfferCollector;
import Collectors.ResumeCollector;
import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Controller {
    private static Logger rootLogger;

    public static void main(String[] args) throws Exception {
        configureLogger();
        String email = "";
        String password = "";
        ResumeCollector collector = new ResumeCollector(email, password);
        OfferCollector offerCollector = new OfferCollector(email, password);



        FileReader fileReader = new FileReader("JSON-CV-Storage/Output.txt");
        ArrayList<String> urlList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(fileReader)) {
            String line;
            while ((line = br.readLine()) != null) {
                urlList.add(line);
            }
        }
        collector.parseCVs(urlList);

        //TODO Set up two example workflows
//        ArrayList<String> offers = new ArrayList<>();
//
//        FileReader fileReader = new FileReader("JSON-CV-Storage/offers.txt");
//        try (BufferedReader br = new BufferedReader(fileReader)) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                offers.add(line);
//            }
//        }
//        offerCollector.parseOffers(offers);
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
