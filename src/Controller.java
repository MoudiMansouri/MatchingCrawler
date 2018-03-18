import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.Logger;
import org.apache.log4j.PatternLayout;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.ArrayList;

public class Controller {
    private static Logger rootLogger;

    public static void main(String[] args) throws Exception {
      //  long startTime = System.currentTimeMillis();
        configureLogger();
        CollectUrls collectUrls = new CollectUrls();

//        FileReader fileReader = new FileReader("JSON-CV-Storage/Output.txt");
        ArrayList<String> urlList = new ArrayList<>();
//
//        try (BufferedReader br = new BufferedReader(fileReader)) {
//            String line;
//            while ((line = br.readLine()) != null) {
//                urlList.add(line);
//            }
//        }
////      ArrayList<String> urls = collectUrls.next("https://www.indeed.com/resumes?co=GB&q=javascript&l=manchester", stringList, 0);
////      System.out.println("size : " + urls.size());
//        collectUrls.parseAll(urlList);
//        long endTime = System.currentTimeMillis();
//        System.out.println("Execution Time " + (endTime - startTime)/100 + "seconds");
        ArrayList<String> offers = collectUrls.collectOffers("https://www.indeed.co.uk/jobs?q=java&l=London%2C%20Greater%20London", urlList, 1, 2);
        collectUrls.parseOffers(offers);
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
