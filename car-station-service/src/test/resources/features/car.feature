Feature: Testing Ride Service

  Scenario: Retrieve an existing car
    Given a car with id 1 exists
    When I request to get the car by id 1
    Then I should receive a car with id 1

  Scenario: Delete an existing car
    Given a car with id 2 exists
    When I delete the car by id 2
    Then the car with id 2 should not be found

  Scenario: Create a new car
    When I create a new car with number "ABC123" and model "Sedan"
    Then the car should have number "ABC123" and model "Sedan"

  Scenario: Attempt to create a car with an existing number
    Given a car with id 3, number "DEF456" and model "SUV"
    When I try to create a new car with number "DEF456" and model "Coupe"
    Then I should get a CarNumberAlreadyExistException

  Scenario: Update an existing car's number
    Given a car with id 4, number "GHI789" and model "Hatchback"
    When I update car with id 4 to number "JKL012"
    Then the car with id 4 should have number "JKL012"

  Scenario: Attempt to update a car to an existing number
    Given a car with id 5, number "MNO345" and model "Convertible"
    Given a second car with id 6, number "PQR678" and model "Crossover"
    When I try to update car with id 5 to a number "PQR678"
    Then I should get a CarNumberAlreadyExistException
