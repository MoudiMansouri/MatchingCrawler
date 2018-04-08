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
      //  UrlCollector urlCollector = new UrlCollector();
        OfferCollector offerCollector = new OfferCollector();

        FileReader fileReader = new FileReader("JSON-CV-Storage/offers.txt");
        ArrayList<String> urlList = new ArrayList<>();
        System.out.println("Done opening");
//
//        try (BufferedReader br = new BufferedReader(fileReader)) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                urlList.add(line);
//            }
//        }
//        resumeCollector.parseCVs(urlList);
        ArrayList<String> offers = new ArrayList<>();
        offers = offerCollector.collectOffers("https://www.indeed.co.uk/jobs?q=java&l=London%2C%20Greater%20London", offers, 1, 5);
        System.out.println(offers.size());
//        resumeCollector.parseOffers(urlList);
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
