package stepDefs;

import io.cucumber.java.After;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.internal.ExitCode;

import java.time.Duration;
import java.util.Random;

import static org.junit.Assert.assertEquals;

public class MyStepdefs {
    WebDriver driver;
    private WebDriverWait wait;

    @Given("I use {string}")
    public void iUse(String browser) {
         if (browser.equalsIgnoreCase("edge")) {
            System.setProperty("webdriver.edge.driver", "D:\\Program\\EdgeDriver/msedgedriver.exe");
            EdgeOptions edgeOptions = new EdgeOptions();
            edgeOptions.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");

            driver = new EdgeDriver(edgeOptions);
        } else {
            System.setProperty("webdriver.chrome.driver", "D:\\Program\\ChromeDriver/chromedriver.exe");
            ChromeOptions chromeOptions = new ChromeOptions();
            chromeOptions.addArguments("--remote-allow-origins=*", "ignore-certificate-errors");

            driver = new ChromeDriver(chromeOptions);
        }
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        driver.get("https://login.mailchimp.com/signup/");
    }

    @Given("I enter {string} as email")
    public void iEnterEmail(String email) {
        String domain = "@a.b";
        if (!email.equalsIgnoreCase("")) {
            waitUntilPresent("email");
            driver.findElement(By.id("email")).sendKeys(email + RandomText() + domain);
        }
    }

    @Given("I enter {string} as username")
    public void iEnterUsername(String username) {
        waitUntilPresent("new_username");
        WebElement field = driver.findElement(By.id("new_username"));
        field.click();
        field.clear();
        if (username.equalsIgnoreCase("long")) {
            for (int i = 0; i < 5; i++) {
                username += username;
            }
            username += RandomText();
        } else if (username.equalsIgnoreCase("existing")) {
        } else {
            username += RandomText();
        }
        field.sendKeys(username);
    }

    @Given("I enter {string} as password")
    public void iEnterPassword(String password) {
        waitUntilPresent("new_password");
        driver.findElement(By.id("new_password")).sendKeys(password);
    }

    @When("I press Sign Up")
    public void iPressSignUp() {
        Actions action = new Actions(driver);
        WebElement checkBox = driver.findElement(By.name("marketing_newsletter"));
        checkBox.click();

        waitUntilPresent("create-account-enabled");
        WebElement button = driver.findElement(By.id("create-account-enabled"));
        action.moveToElement(button).perform();
        button.click();
    }

    @Then("I am signed up {string}")
    public void iAmSignedUp(String signedUp) {

        boolean expected = signedUp.equalsIgnoreCase("success");
        boolean actual;
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.className("invalid-error")),
                ExpectedConditions.presenceOfElementLocated(By.id("resend-email-link")),
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(3)));

        if (signedUp.equalsIgnoreCase("success") && elementExistsID("resend-email-link")) {
            actual = true;
        } else if (!signedUp.equalsIgnoreCase("success") && elementExistsClass("invalid-error")) {
            actual = false;
        } else if (signedUp.equalsIgnoreCase("success") && elementExistsID("recaptcha-help-button")) {
            actual = true;
        } else if (!signedUp.equalsIgnoreCase("success") && elementExistsID("recaptcha-help-button")) {
            actual = false;
        } else actual = !expected;

        assertEquals(expected, actual);
    }

    @After
    public void tearDown() {
        driver.close();
        driver.quit();
    }

    public String RandomText() {
        Random rnd = new Random();
        int n = 100000 + rnd.nextInt(900000);

        return String.valueOf(n);
    }

    private boolean elementExistsID(String id) {
        try {
            driver.findElement(By.id(id));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean elementExistsClass(String className) {
        try {
            driver.findElement(By.className(className));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void waitUntilPresent(String id) {
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id(id)));
    }
}
