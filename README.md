# OrangeHRM Test Automation Project

## Overview
This project contains automated tests for the OrangeHRM demo application (https://opensource-demo.orangehrmlive.com/). The tests are implemented using:
- Selenium WebDriver for browser automation
- TestNG for test execution and assertions
- Cucumber for BDD-style test specifications

## Project Features
The automation tests the user management functionality in OrangeHRM:
- Login to the application with admin credentials
- Navigate to the Admin section
- View existing user records
- Create a new user with Admin role
- Verify user creation by checking record count
- Search for the newly created user
- Delete the user
- Verify user deletion by checking record count

## Project Structure
```
src/
├── test/
    ├── java/
    │   ├── OrangeHRMTest.java (TestNG test)
    │   ├── runners/
    │   │   └── TestRunner.java (Cucumber test runner)
    │   └── stepdefinitions/
    │       ├── OrangeHRMSteps.java (Step definitions)
    │       └── Hooks.java (Cucumber hooks)
    └── resources/
        └── features/
            └── OrangeHRMUserManagement.feature (Cucumber scenarios)
```

## Requirements
- Java JDK 21
- Chrome browser
- Chrome WebDriver (automatically managed by WebDriverManager)

## How to Run
You can run the tests in several ways:

### Using an IDE (IntelliJ IDEA, Eclipse)
1. Import the project as a Maven project
2. Run the OrangeHRMTest class directly (for TestNG)
3. Run the TestRunner class (for Cucumber)

### Using Maven
If Maven is installed:
```
mvn clean test
```

## Test Reports
After running the Cucumber tests, reports will be generated in:
- HTML Report: `target/cucumber-reports/cucumber-pretty.html`
- JSON Report: `target/cucumber-reports/CucumberTestReport.json`

## Notes
- The test uses dynamic user generation with timestamps to ensure test independence
- Explicit waits are used throughout the test to handle dynamic loading elements
- The project uses WebDriverManager to automatically download and configure the ChromeDriver 