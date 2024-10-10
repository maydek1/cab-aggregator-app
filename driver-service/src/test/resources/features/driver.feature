Feature: Testing DriverService

  Scenario: Get driver by id
    Given a driver with id 1, email "Nikita@gmail.com" and carId 11 exists
    When I request to get driver by id 1
    Then I should receive a driver with email "Nikita@gmail.com" and carId 11

  Scenario: Get non-existent driver by id
    When I request to get a non-existent driver by id 1
    Then I should get a DriverNotFoundException

  Scenario: Delete driver by id
    Given a driver with id 1, email "Nikita@gmail.com" and carId 11 exists
    When I delete driver with id 1
    And I try to get this driver
    Then I should get a DriverNotFoundException

  Scenario: Update driver email
    Given a driver with id 1, email "Nikita@gmail.com" and carId 11 exists
    When I update driver with id 1 to change email to "NeNikita@gmail.com"
    Then I should receive a driver with email "NeNikita@gmail.com" and carId 11

  Scenario: Update driver with existing email
    Given a driver with id 1, email "Nikita@gmail.com" and carId 11 exists
    And a driver with id 2, email "NeNikita@gmail.com" and carId 12 exists
    When I update driver with id 1 to change email to already exist email "NeNikita@gmail.com"
    Then I should get an EmailAlreadyExistException

  Scenario: Create driver successfully
    When I create a driver with id 1, email "Nikita@gmail.com" and carId 11
    Then I should receive a driver with email "Nikita@gmail.com" and carId 11

  Scenario: Create driver with non-existent carId
    When I create a driver with email "Nikita@gmail.com" and non-existent carId 11
    Then I should get a CarNotFoundException

  Scenario: Create driver with existing email
    Given a driver with email "Nikita@gmail.com" already exists
    When I create a driver with email "Nikita@gmail.com" and carId 11
    Then I should get an EmailAlreadyExistException

  Scenario: Get all drivers
    Given a driver with id 1, email "Nikita@gmail.com" and carId 11 exists
    When I request to get all drivers
    Then I should get a list of drivers with ids 1

  Scenario: Toggle driver's availability
    Given a driver with id 1, email "Nikita@gmail.com" and availability set to false exists
    When I toggle availability of driver with id 1
    Then I should receive a driver with email "Nikita@gmail.com" and availability set to true

  Scenario: Update driver's rating
    Given a driver with id 1, email "Nikita@gmail.com" and rate 5 and ratingCount 1 exists
    When I update the rating with rate 3
    Then I should receive a driver with email "Nikita@gmail.com", rate 4 and ratingCount 2
