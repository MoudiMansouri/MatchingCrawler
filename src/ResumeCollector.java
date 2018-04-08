import Model.*;
import com.google.gson.Gson;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.openqa.selenium.*;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public class ResumeCollector extends UrlCollector{

    UrlCollector collector;

    public ResumeCollector(UrlCollector collector) {
        this.collector = collector;
    }

    public ArrayList<String> collectURL(String url, ArrayList<String> argUrlsList, int start) throws IOException {
        WebDriver driver = this.collector.getDriver();
        System.out.println("Size : " + argUrlsList.size());
        if (start == 4) {
            driver.quit();
            return argUrlsList;
        }
        if (start < 4) {
            driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
            driver.get(url);
            List<WebElement> elements = driver.findElements(By.xpath("//div[@class=\"app_name\"]//a"));
            FileWriter fw = new FileWriter("JSON-CV-Storage/Output.txt", true);

            for (WebElement e : elements) {
                fw.write(e.getAttribute("href") + "\n");
                argUrlsList.add(e.getAttribute("href"));
            }
            fw.close();

            WebElement element = driver.findElement(By.xpath("//*[contains(@class, 'next')]"));

            String href = element.getAttribute("href");
            start = start + 1;
            collectURL(href, argUrlsList, start);
        }
        return argUrlsList;
    }

    private void downloadCV(String url) {
        WebDriver driver = this.collector.getDriver();

        driver.get(url);
        Candidate candidate = new Candidate();
        if (this.collector.elementExists("//*[@id=\"resume-contact\"]", driver.findElement(By.xpath("//*[@id=\"resume_content\"]")))) {
            //name
            if (this.collector.elementExists("//*[contains(@class,'work-experience-section')]",
                    driver.findElement(By.xpath("//*[@id=\"resume-contact\"]")))) {

                WebElement nameElement = driver.findElement(By.xpath("//*[@id=\"resume-contact\"]"));
                String name = nameElement.getText();
                String splitName[] = name.split(" ");
                candidate.setFirstName(splitName[0]);
                candidate.setLastName(splitName[splitName.length - 1]);

            }
            //work
            ArrayList<Experience> experiences = new ArrayList<>();

            if (this.collector.elementExists("//*[@id=\"work-experience-items\"]",
                    driver.findElement(By.xpath("//*[@id=\"resume_body\"]")))) {

                List<WebElement> workElements = driver.findElements(By.xpath("//*[contains(@class,'work-experience-section')]"));
                for (WebElement e : workElements) {
                    Experience exp = new Experience();

                    if (this.collector.elementExists(".//p[contains(@class,'work_title')]", e)) {
                        String role = e.findElement(By.xpath(".//p[contains(@class,'work_title')]")).getText();
                        exp.setRole(role);
                    }
                    if (this.collector.elementExists(".//p[contains(@class,'work_company')]", e)) {
                        String inst = e.findElement(By.className("work_company")).getText();
                        exp.setInstitution(inst);
                    }
                    if (this.collector.elementExists(".//p[contains(@class,'work_description')]", e)) {
                        String desc = e.findElement(By.className("work_description")).getText();
                        ;
                        exp.setDescription(desc);
                    }
                    experiences.add(exp);
                }
            }

            //education
            ArrayList<Education> educations = new ArrayList<>();
            if (this.collector.elementExists("//*[contains(@class,'education-section')]",
                    driver.findElement(By.xpath("//*[@id=\"resume_body\"]")))) {
                List<WebElement> educationElements = driver.findElements(By.xpath("//*[contains(@class,'education-section')]"));
                for (WebElement e : educationElements) {
                    Education edu = new Education();

                    if (this.collector.elementExists(".//p[contains(@class,'edu_title')]", e)) {
                        String title = e.findElement(By.xpath("//*[contains(@class,'edu_title')]")).getText();
                        edu.setDescription(title);
                    }
                    if (this.collector.elementExists(".//div[contains(@class,'edu_school')]", e)) {
                        String inst = e.findElement(By.xpath(".//div[contains(@class,'edu_school')]")).getText();
                        edu.setInstitution(inst);
                    }
//                    if (elementExists(".//p[contains(@class,'edu_dates')]", e)) {
//                        String desc = e.findElement(By.xpath(".//p[contains(@class,'edu_dates')]")).getText();
//                        edu.setDescription(desc);
//                    }
                    educations.add(edu);
                }
            }

            //skills
            ArrayList<Skill> skills = new ArrayList<>();
            if (this.collector.elementExists("//*[@id=\"skills-items\"]",
                    driver.findElement(By.xpath("//*[@id=\"resume_body\"]")))) {
                WebElement skillsElement = driver.findElement(By.xpath("//*[@id=\"skills-items\"]"));
                List<WebElement> skillElements = skillsElement.findElements(By.xpath("//*[contains(@class,'skill-text')]"));

                for (WebElement e : skillElements) {
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
        ServerConnection serverConnection = new ServerConnection();
        serverConnection.postToServer("http://localhost:3000/candidate", gsonString);
    }

    public void parseCVs(List<String> urls) throws InterruptedException {
        WebDriver driver = this.collector.getDriver();

        ProgressBar bar = new ProgressBar();
        System.out.println("Parsing...");
        bar.update(0, urls.size());
        for (String url : urls) {
            downloadCV(url);
            bar.update(urls.indexOf(url), urls.size());
            if (urls.indexOf(url) == urls.size()) {
                driver.quit();
            }
        }
    }



    public void dummySave() throws IOException {
        WebDriver driver = this.collector.getDriver();

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

        ArrayList<Experience> experiences = new ArrayList<>();
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
        String urlPost = "http://localhost:3000/candidate";
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpPost post = new HttpPost(urlPost);
        StringEntity postString = new StringEntity(gsonString);
        post.setEntity(postString);
        post.setHeader("Content-type", "application/json");
        HttpResponse response = httpClient.execute(post);

        driver.quit();
    }






}