package stepdefinitions;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import java.time.Duration;

public class OrangeHRMSteps {
    private WebDriver driver;
    private WebDriverWait wait;
    private int initialRecordCount;
    private String newUsername;

    @Before
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
        newUsername = "Paulaas" + System.currentTimeMillis();
    }

    @Given("I am on the OrangeHRM login page")
    public void iAmOnTheOrangeHRMLoginPage() {
        driver.get("https://opensource-demo.orangehrmlive.com/");
    }

    @When("I login with username {string} and password {string}")
    public void iLoginWithUsernameAndPassword(String username, String password) {
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        WebElement passwordField = driver.findElement(By.name("password"));
        usernameField.sendKeys(username);
        passwordField.sendKeys(password);

        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();
    }

    @And("I navigate to the Admin page")
    public void iNavigateToTheAdminPage() {
        WebElement adminTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));
        adminTab.click();
    }

    @Then("I should see the current record count")
    public void iShouldSeeTheCurrentRecordCount() {
        initialRecordCount = getRecordNumberCount();
    }

    @When("I add a new user with Admin role")
    public void iAddANewUserWithAdminRole() {
        WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='button' and contains(@class, 'oxd-button--secondary')]")));
        addButton.click();

        // Select User Role dropdown
        WebElement userRoleDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class, 'oxd-select-text--after')])[1]")));
        userRoleDropdown.click();
        WebElement adminOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class, 'oxd-select-option')])[2]")));
        adminOption.click();

        // Select Status dropdown
        WebElement statusDropdown = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class, 'oxd-select-text--after')])[2]")));
        statusDropdown.click();
        WebElement enabledOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("(//div[contains(@class, 'oxd-select-option')])[2]")));
        enabledOption.click();

        // Enter employee name
        WebElement employeeNameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//input[@placeholder='Type for hints...']")));
        employeeNameInput.sendKeys("Peter Mac Anderson");
        WebElement employeeOption = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[@role='listbox']/div[contains(@class, 'oxd-autocomplete-option')]/span")));
        employeeOption.click();

        // Enter username
        WebElement usernameInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//label[contains(text(),'Username')]/following::input[1]")));
        usernameInput.sendKeys(newUsername);

        // Enter password
        WebElement passwordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//label[contains(text(),'Password')]/following::input[1]")));
        passwordInput.sendKeys("Test@123");

        // Confirm password
        WebElement confirmPasswordInput = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//label[contains(text(),'Confirm Password')]/following::input[1]")));
        confirmPasswordInput.sendKeys("Test@123");

        // Click save button
        WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")));
        saveButton.click();
        
        // Wait for successful save
        wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("/admin/viewSystemUsers"),
                ExpectedConditions.visibilityOfElementLocated(By.className("oxd-toast-container"))
        ));
    }

    @Then("the record count should increase by {int}")
    public void theRecordCountShouldIncreaseBy(int countIncrease) {
        int countAfterAdd = getRecordNumberCount();
        Assert.assertEquals(countAfterAdd, initialRecordCount + countIncrease);
    }

    @When("I search for the newly created user")
    public void iSearchForTheNewlyCreatedUser() {
        WebElement usernameSearchField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-input-group')]/div/input")));
        usernameSearchField.sendKeys(newUsername);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")));
        searchButton.click();

        // Wait for search results
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-table-card')]")));
    }

    @And("I delete the user")
    public void iDeleteTheUser() {
        WebElement deleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'oxd-table-cell-actions')]/button[1]")));
        deleteButton.click();

        WebElement confirmDeleteButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//div[contains(@class, 'orangehrm-modal-footer')]/button[contains(@class, 'oxd-button--label-danger')]")));
        confirmDeleteButton.click();

        // Wait for successful deletion
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.className("oxd-toast-container")));

        WebElement adminTabAfter = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));
        adminTabAfter.click();
    }

    @Then("the record count should be the same as initial count")
    public void theRecordCountShouldBeTheSameAsInitialCount() {
        int countAfterDelete = getRecordNumberCount();
        Assert.assertEquals(countAfterDelete, initialRecordCount);
    }

    private int getRecordNumberCount() {
        WebElement spanElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(normalize-space(), 'Records Found')]")));
        String recordText = spanElement.getText();
        String recordNumber = recordText.replaceAll(".*\\((\\d+)\\).*", "$1");
        return Integer.parseInt(recordNumber);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
} 