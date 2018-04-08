import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import java.util.HashMap;
import java.util.concurrent.TimeUnit;

public class UrlCollector {

    private WebDriver driver;

    public WebDriver getDriver() {
        return driver;
    }

    public void setDriver(WebDriver driver) {
        this.driver = driver;
    }

    public UrlCollector() {
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

    public boolean elementExists(String xpath, WebElement e) {
        this.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
        try {
            e.findElement(By.xpath(xpath));
        } catch (NoSuchElementException | StaleElementReferenceException noEl) {
            this.driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);
            return false;
        }
        this.driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);

        return true;
    }

}
