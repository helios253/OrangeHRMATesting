import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;

public class OrangeHRMTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private String newUsername = "Paulaas" + System.currentTimeMillis();

    @BeforeMethod
    public void setup() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--remote-allow-origins=*");
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.manage().window().maximize();
    }

    @Test
    public void testUserManagement() {
        // 1. Navigate to OrangeHRM
        driver.get("https://opensource-demo.orangehrmlive.com/");

        // 2 & 3. Login with credentials
        WebElement usernameField = wait.until(ExpectedConditions.presenceOfElementLocated(By.name("username")));
        WebElement passwordField = driver.findElement(By.name("password"));
        usernameField.sendKeys("Admin");
        passwordField.sendKeys("admin123");

        // 4. Click login button
        WebElement loginButton = driver.findElement(By.xpath("//button[@type='submit']"));
        loginButton.click();

        // 5. Click on Admin tab
        WebElement adminTab = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//span[text()='Admin']")));
        adminTab.click();
        
        // 6. Get initial number of records
        int recordCount = getRecordNumberCount();

        // 7, 8, 9. Click add button
        AddUser_ShouldAddUserSuccessfully(newUsername);
      
        // 10. Verify record count increased by 1
        int countAfterAdd = getRecordNumberCount();
        Assert.assertTrue(countAfterAdd == recordCount + 1);

        //11. Search for new user
        WebElement usernameSearchField = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-input-group')]/div/input")));
        usernameSearchField.sendKeys(newUsername);

        WebElement searchButton = wait.until(ExpectedConditions.elementToBeClickable(
                By.xpath("//button[@type='submit']")));
        searchButton.click();

        // Wait for search results
        wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//div[contains(@class, 'oxd-table-card')]")));

        // 12. Delete the new user
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

        // 13. Verify record count decreased by 1
         int countAfterDelete = getRecordNumberCount();
         Assert.assertTrue(countAfterDelete == recordCount);
    
}

    private void AddUser_ShouldAddUserSuccessfully(String newUsername) {

         WebElement addButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[@type='button' and contains(@class, 'oxd-button--secondary')]")));
               addButton.click();
        
               // 8. Fill required data
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
        
               // 9. Click save button
               WebElement saveButton = wait.until(ExpectedConditions.elementToBeClickable(
                       By.xpath("//button[@type='submit']")));
               saveButton.click();
               // Wait for successful save
               wait.until(ExpectedConditions.or(
                       ExpectedConditions.urlContains("/admin/viewSystemUsers"),
                       ExpectedConditions.visibilityOfElementLocated(By.className("oxd-toast-container"))
               ));
    }

    private int getRecordNumberCount() {
       WebElement spanElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//span[contains(normalize-space(), 'Records Found')]")));
        String recordText = spanElement.getText();
        String recordNumber = recordText.replaceAll(".*\\((\\d+)\\).*", "$1");
        int recordNumberCount = Integer.parseInt(recordNumber);

        return recordNumberCount;
    }

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
} 