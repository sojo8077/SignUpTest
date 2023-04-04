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
            waitUntilPresent(By.id("email"));
            driver.findElement(By.id("email")).sendKeys(email + RandomText() + domain);
        }
    }

    @Given("I enter {string} as username")
    public void iEnterUsername(String username) {
        waitUntilPresent(By.id("new_username"));
        WebElement field = driver.findElement(By.id("new_username"));
        field.click();
        field.clear();
        if (username.equalsIgnoreCase("long")) {
            username = "longlonglonglonglonglonglonglonglonglonglonglonlonglonglonglonglonglonglonglonglonglonglonglonglonglonglong";
            username += RandomText();
        } else if (username.equalsIgnoreCase("existing")) {
        } else {
            username += RandomText();
        }
        field.sendKeys(username);
    }

    @Given("I enter {string} as password")
    public void iEnterPassword(String password) {
        waitUntilPresent(By.id("new_password"));
        driver.findElement(By.id("new_password")).sendKeys(password);
    }

    @When("I press Sign Up")
    public void iPressSignUp() {
        Actions action = new Actions(driver);
        WebElement checkBox = driver.findElement(By.name("marketing_newsletter"));
        checkBox.click();

        waitUntilPresent(By.id("create-account-enabled"));
        WebElement button = driver.findElement(By.id("create-account-enabled"));
        action.moveToElement(button).perform();
        button.click();
    }

    @Then("I am signed up {string}")
    public void iAmSignedUp(String signedUp) {

        boolean expected = signedUp.equalsIgnoreCase("success");
        boolean actual;
        wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.id("resend-email-link")),
                ExpectedConditions.presenceOfElementLocated(By.cssSelector(".error-page centered")),
                ExpectedConditions.textToBePresentInElementLocated(By.className("invalid-error"), "An email address must contain a single @."),
                ExpectedConditions.textToBePresentInElementLocated(By.className("invalid-error"), "Enter a value less than 100 characters long"),
                ExpectedConditions.textToBePresentInElementLocated(By.className("invalid-error"), "Great minds think alike - someone already has this username."),
                ExpectedConditions.frameToBeAvailableAndSwitchToIt(3)));

        if (signedUp.equalsIgnoreCase("success") && elementExists(By.id("resend-email-link"))) {
            System.out.println("Success");
            actual = true;
        } else if (!signedUp.equalsIgnoreCase("success") && elementExists(By.className("invalid-error"))) {
            if (driver.findElement(By.className("invalid-error")).getText().equals("An email address must contain a single @.")) {
                System.out.println("An email address must contain a single @.");
                actual = false;
            } else if (driver.findElement(By.className("invalid-error")).getText().equals("Enter a value less than 100 characters long")) {
                System.out.println("Enter a value less than 100 characters long");
                actual = false;
            } else if (driver.findElement(By.className("invalid-error")).getText().contains("Great minds think alike - someone already has this username.")) {
                System.out.println("Great minds think alike - someone already has this username.");
                actual = false;
            } else {
                System.out.println("Other");
                actual = !expected;
            }
        } else if (!signedUp.equalsIgnoreCase("success") && elementExists(By.cssSelector(".error-page centered"))) {
            System.out.println("Error page");
            actual = false;
        } else if (elementExists(By.id("recaptcha-help-button"))) {
            actual = expected;
            System.out.println("Captcha");
        } else {
            System.out.println("Unexpected error");
            actual = !expected;
        }

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

    private boolean elementExists(By by) {
        try {
            driver.findElement(by);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void waitUntilPresent(By by) {
        wait.until(ExpectedConditions.presenceOfElementLocated(by));
    }

}
