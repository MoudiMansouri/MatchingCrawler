package Collectors;

import Model.Offer;
import Utils.ProgressBar;
import Utils.ServerConnection;
import com.google.gson.Gson;
import org.openqa.selenium.*;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class OfferCollector extends UrlCollector {

    public OfferCollector(String email, String password) {
        super(email, password);
    }

    public ArrayList<String> collectURL(String url, ArrayList<String> argUrlsList, int start, int limit)
            throws IOException {
        WebDriver driver = this.getDriver();

        System.out.println(start + ", size : " + argUrlsList.size());
        if (start > 1) {
            StringBuilder builder = new StringBuilder(url);
            String intsoon = "" + url.charAt(url.length() - 2) + url.charAt(url.length() - 1);
            int next = Integer.parseInt(intsoon);
            next += 10;
            builder.replace(url.length() - 2, url.length(), Integer.toString(next));
            url = builder.toString();
        }
        if (start == limit) {
            driver.quit();
            return argUrlsList;
        }
        if (start < limit) {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.get(url);
            List<WebElement> elements = driver.findElements(By.xpath("//*[contains(@data-tn-element,'jobTitle')]"));
            FileWriter fw = new FileWriter("JSON-CV-Storage/offerss.txt", true);

            for (WebElement e : elements) {
                // fw.write(e.getAttribute("href") + "\n");
                argUrlsList.add(e.getAttribute("href"));
            }
            fw.close();


            List<WebElement> navElements = driver.findElements(By.xpath("//*[contains(@class, 'pagination')]/a[5]"));

            String href = navElements.get(0).getAttribute("href");
            start = start + 1;
            collectURL(href, argUrlsList, start, limit);
        }
        return argUrlsList;
    }

    public void downloadOffer(String url) {
        WebDriver driver = this.getDriver();

        driver.get(url);
        Offer offer = new Offer();
        if(elementExists("//*[@id=\"main\"]", driver.findElement(By.xpath("/html/body")))){
            System.out.println("Can't trick me");
        }else{
            if (elementExists("//b[contains(@class,'jobtitle')]",
                    driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
                String title = driver.findElement(By.xpath("//b[contains(@class,'jobtitle')]")).getText();
                offer.setJobTitle(title);
            }
            if (elementExists("//span[contains(@class,'company')]",
                    driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
                String organization = driver.findElement(By.xpath("//span[contains(@class,'company')]")).getText();
                offer.setOrganization(organization);
            }
            if (elementExists("//*[@id=\"job_summary\"]",
                    driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
                String description = driver.findElement(By.xpath("//*[@id=\"job_summary\"]")).getText();
                offer.setDescription(description);
            }
            if(this.elementExists("//*[contains(@class, 'location')]",driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
                WebElement location= driver.findElement(By.xpath("//*[contains(@class, 'location')]"));
                offer.setLocation(location.getText());
            }
            offer.setUrl(url);
            Gson gson = new Gson();
            String gsonString = gson.toJson(offer);
            ServerConnection serverConnection = new ServerConnection();
            serverConnection.postToServer("http://localhost:3000/offer", gsonString);
        }
    }

    public void parseOffers(ArrayList<String> cvURLS) {
        WebDriver driver = this.getDriver();

        ProgressBar bar = new ProgressBar();
        System.out.println("Parsing...");
        bar.update(0, cvURLS.size());
        for (String url : cvURLS) {
            downloadOffer(url);
            bar.update(cvURLS.indexOf(url), cvURLS.size());
            if (cvURLS.indexOf(url) == cvURLS.size()) {
                driver.quit();
            }
        }

    }

}
