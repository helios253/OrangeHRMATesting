Feature: OrangeHRM User Management
  As an admin user
  I want to manage users in the system
  So that I can control access to the application

  Scenario: Add and Delete User
    Given I am on the OrangeHRM login page
    When I login with username "Admin" and password "admin123"
    And I navigate to the Admin page
    Then I should see the current record count
    When I add a new user with Admin role
    Then the record count should increase by 1
    When I search for the newly created user
    And I delete the user
    Then the record count should be the same as initial count 