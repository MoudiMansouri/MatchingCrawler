import Model.*;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class CollectUrls {

    private WebDriver driver;

    public CollectUrls() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\Moudi\\IdeaProjects\\Craweler\\chromedriver.exe");
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--disable-impl-side-painting"); //it worked for me
        String downloadPath = "C:\\Users\\Moudi\\Desktop\\Project-Prep\\Node-Parser\\IndeedJSONs";
        HashMap<String, Object> chromePrefs = new HashMap<String, Object>();
        // chromeOptions.addArguments("--no-startup-window");
        chromePrefs.put("profile.default_content_settings.popups", 0);
        chromePrefs.put("download.default_directory", downloadPath);
        ChromeOptions options = new ChromeOptions();
        DesiredCapabilities cap = DesiredCapabilities.chrome();
        cap.setCapability(CapabilityType.ACCEPT_SSL_CERTS, true);
        cap.setCapability(ChromeOptions.CAPABILITY, options);
        this.driver = new ChromeDriver(chromeOptions);
        this.driver.get("https://www.indeed.com/account/login");
        this.driver.findElement(By.id("signin_email")).sendKeys("spam.reciever.moudi@gmail.com");
        this.driver.findElement(By.id("signin_password")).sendKeys("Fils411838!");
        this.driver.findElement(By.className("btn-signin")).click();
      //  Thread.sleep(3000);
        this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
    }

    protected ArrayList<String> next(String url, ArrayList<String> argUrlsList, int start) throws IOException {
        System.out.println("Size : " + argUrlsList.size());
        if (start == 4) {
            this.driver.quit();
            return argUrlsList;
        }
        if (start < 4) {
            this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            this.driver.get(url);
            List<WebElement> elements = this.driver.findElements(By.xpath("//div[@class=\"app_name\"]//a"));
            FileWriter fw = new FileWriter("JSON-CV-Storage/Output.txt", true);

            for (WebElement e : elements) {
        //        System.out.println(e.getAttribute("href"));
                fw.write(e.getAttribute("href") + "\n");
                argUrlsList.add(e.getAttribute("href"));
            }
            fw.close();

            WebElement element = this.driver.findElement(By.xpath("//*[contains(@class, 'next')]"));

            String href = element.getAttribute("href");
            start = start + 1;
            next(href, argUrlsList, start);
        }
        return argUrlsList;
    }

    private void downloadCV(String url) {
        this.driver.get(url);
        Candidate candidate = new Candidate();
        if(elementExists("//*[@id=\"resume-contact\"]", this.driver.findElement(By.xpath("//*[@id=\"resume_content\"]")))) {
            //name
            if(elementExists("//*[contains(@class,'work-experience-section')]",
                    this.driver.findElement(By.xpath("//*[@id=\"resume-contact\"]")))) {
                WebElement nameElement = this.driver.findElement(By.xpath("//*[@id=\"resume-contact\"]"));
                String name = nameElement.getText();
                System.out.println(name);
                String splitName[] = name.split(" ");
                candidate.setFirstName(splitName[0]);
                candidate.setLastName(splitName[splitName.length-1]);
                //work
                ArrayList<Experience> experiences = new ArrayList<>();

                if(elementExists("//*[contains(@class,'work-experience-section')]",
                        this.driver.findElement(By.xpath("//*[@id=\"resume_body\"]")))) {
                    List<WebElement> workElements = this.driver.findElements(By.xpath("//*[contains(@class,'work-experience-section')]"));
                    for (WebElement e: workElements) {
                        Experience exp = new Experience();

                        if(elementExists("//*[contains(@class,'work_title')]", e)) {
                            String role = e.findElement(By.xpath("//*[contains(@class,'work_title')]")).getText();
                            exp.setRole(role);
                        }
                        if(elementExists("//*[contains(@class,'work_company')]", e)) {
                            String inst = e.findElement(By.xpath("//*[contains(@class,'work_company')]")).getText();
                            exp.setInstitution(inst);
                        }
                        if(elementExists("//*[contains(@class,'work_description')]", e)) {
                            String desc = e.findElement(By.xpath("//*[contains(@class,'work_description')]")).getText();
                            exp.setDescription(desc);
                        }
                        experiences.add(exp);
                    }
                }
                //education
                ArrayList<Education> educations = new ArrayList<>();
                if(elementExists("//*[contains(@class,'education-section')]",
                        this.driver.findElement(By.xpath("//*[@id=\"resume_body\"]")))) {
                    List<WebElement> educationElements = this.driver.findElements(By.xpath("//*[contains(@class,'education-section')]"));
                    for (WebElement e: educationElements) {
                        Education edu = new Education();

                        if(elementExists("//*[contains(@class,'edu_title')]", e)) {
                            String desc = e.findElement(By.xpath("//*[contains(@class,'edu_title')]")).getText();
                            edu.setDescription(desc);
                        }
                        if(elementExists("//*[contains(@class,'edu_school')]", e)) {
                            String inst = e.findElement(By.xpath("//*[contains(@class,'edu_school')]")).getText();
                            edu.setInstitution(inst);
                        }
                        if(elementExists("//*[contains(@class,'edu_dates')]", e)) {
                            String desc = e.findElement(By.xpath("//*[contains(@class,'edu_dates')]")).getText();
                            edu.setDescription(desc);
                        }
                        educations.add(edu);
                    }
                }
                //skills
                ArrayList<Skill> skills = new ArrayList<>();
                if(elementExists("//*[@id=\"skills-items\"]",
                        this.driver.findElement(By.xpath("//*[@id=\"resume_body\"]")))) {
                    WebElement skillsElement = this.driver.findElement(By.xpath("//*[@id=\"skills-items\"]"));
                    List<WebElement> skillElements = skillsElement.findElements(By.xpath("//*[contains(@class,'skill-text')]"));

                    for (WebElement e: skillElements) {
                        Skill skill = new Skill();
                        skill.setDescription(e.getText());
                        skills.add(skill);
                    }
                }
                candidate.setExperiences(experiences);
                candidate.setEducations(educations);
                candidate.setSkills(skills);
            }
            Gson gson = new Gson();
            String gsonString = gson.toJson(candidate);
            String urlPost = "http://localhost:3000/candidate";
            postToServer(gsonString);

        }
    }

    public void parseAll(List<String> urls) {
        ProgressBar bar = new ProgressBar();
        System.out.println("Parsing...");
        bar.update(0,urls.size());
        for (String url : urls) {
            downloadCV(url);
            bar.update(urls.indexOf(url), urls.size());
            if(urls.indexOf(url) == urls.size()) {
                this.driver.quit();
            }
        }
    }

    private boolean elementExists(String xpath, WebElement e)
    {
        this.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        try {
            e.findElement(By.xpath(xpath));
        }
        catch (NoSuchElementException | StaleElementReferenceException noEl) {
            this.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

            return false;
        }
        this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return true;
    }


    public void dummySave () throws IOException {
        Candidate dummy = new Candidate();
        dummy.setFirstName("Moudi");
        dummy.setLastName("Mansouri");

        ArrayList<Skill> skills = new ArrayList<>();
        Skill skill1 = new Skill();
        skill1.setDescription("Skill 1");

        Skill skill2 = new Skill();
        skill2.setDescription("Skill 2");

        skills.add(skill1);
        skills.add(skill2);
        dummy.setSkills(skills);

        ArrayList<Experience> experiences= new ArrayList<>();
        Experience experience = new Experience();
        experience.setDescription("Desc 1");
        experience.setInstitution("Inst 1");
        experience.setRole("Role 1");
        experiences.add(experience);
        dummy.setExperiences(experiences);

        ArrayList<Education> educations = new ArrayList<>();
        Education education = new Education();
        education.setDescription("Desc 1 edu");
        education.setInstitution("Inst 1 edu");
        educations.add(education);
        dummy.setEducations(educations);

        Gson gson = new Gson();
        String gsonString = gson.toJson(dummy);
        System.out.println(gsonString);
        String urlPost = "http://localhost:3000/candidate";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlPost);
        StringEntity postString = new StringEntity(gsonString);
        post.setEntity(postString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);

        this.driver.quit();
    }

    public ArrayList<String> collectOffers(String url, ArrayList<String> argUrlsList, int start, int limit) throws IOException {
        System.out.println(start);
        if(start > 1) {
            StringBuilder builder = new StringBuilder(url);
            String intsoon = "" + url.charAt(url.length()-2) + url.charAt(url.length()-1);
            int next = Integer.parseInt(intsoon);
            next += 10;
            builder.replace(url.length()-2, url.length(), Integer.toString(next));
            url = builder.toString();
        }
        if (start == limit) {
            return argUrlsList;
        }
        if (start < limit) {
            this.driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            this.driver.get(url);
            List<WebElement> elements = this.driver.findElements(By.xpath("//*[contains(@data-tn-element,'jobTitle')]"));

            for (WebElement e : elements) {
                argUrlsList.add(e.getAttribute("href"));
            }

            List<WebElement> navElements = this.driver.findElements(By.xpath("//*[contains(@class, 'pagination')]/a[5]"));

            String href = navElements.get(0).getAttribute("href");
            start = start + 1;
            collectOffers(href, argUrlsList, start, limit);
        }
        return argUrlsList;
    }

    public void parseOffers(ArrayList<String> cvURLS) {
        ProgressBar bar = new ProgressBar();
        System.out.println("Parsing...");
        bar.update(0,cvURLS.size());
        for (String url : cvURLS) {
            downloadOffer(url);
            bar.update(cvURLS.indexOf(url), cvURLS.size());
            if(cvURLS.indexOf(url) == cvURLS.size()) {
                this.driver.quit();
            }
        }

    }

    private void downloadOffer(String url) {
        this.driver.get(url);
        Offer offer = new Offer();
        if(elementExists("//b[contains(@class,'jobtitle')]",
                this.driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
            String title = this.driver.findElement(By.xpath("//b[contains(@class,'jobtitle')]")).getText();
            offer.setJobTitle(title);
        }
        if(elementExists("//span[contains(@class,'company')]",
                this.driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
            String organization = this.driver.findElement(By.xpath("//span[contains(@class,'company')]")).getText();
            offer.setOrganization(organization);
        }
        if(elementExists("//*[@id=\"job_summary\"]",
                this.driver.findElement(By.xpath("//*[@id=\"job-content\"]")))) {
            String description = this.driver.findElement(By.xpath("//*[@id=\"job_summary\"]")).getText();
            offer.setDescription(description);
        }

        Gson gson = new Gson();
        String gsonString = gson.toJson(offer);
        System.out.println(gsonString);
        postToServer(gsonString);

    }

    private void postToServer(String gsonString) {
        StringEntity postString;
        String urlPost = "http://localhost:3000/offer";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlPost);

        try {
            postString = new StringEntity(gsonString);
            post.setEntity(postString);
            post.setHeader("Content-type", "application/json");
            HttpResponse response = httpClient.execute(post);
        } catch (IOException ioException) {
            ioException.printStackTrace();
            System.out.println("Something went wrong");
        }
    }
}