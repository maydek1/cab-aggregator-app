Feature: Passenger Service Component Tests

  Scenario: Create a new passenger
    When I create a passenger with email "test@mail.com", phone "123456789", and money "100"
    Then the passenger should be created with email "test@mail.com" and phone "123456789"

  Scenario: Create a passenger with an existing email
    Given A passenger exists with email "test@mail.com"
    When I try to create a passenger with email "test@mail.com", phone "987654321"
    Then I should receive an error "Passenger with email 'test@mail.com' already exist"

  Scenario: Create a passenger with an existing phone
    Given A passenger exists with phone "123456789"
    When I try to create a passenger with email "new@mail.com", phone "123456789"
    Then I should receive an error "Passenger with phone '123456789' already exist"

  Scenario: Update a passenger's information
    Given a passenger exists with id 1, email "test@mail.com", and phone "123456789"
    When I update the passenger with id 1, email "updated@mail.com", and phone "987654321"
    Then the passenger should be updated with email "updated@mail.com" and phone "987654321"

  Scenario: Update a passenger's email to an existing email
    Given a passenger exists with id 1, email "test@mail.com", and phone "12345678"
    And Phone unique is: "false"
    And  Email unique is: "true"
    When I update the passenger with id 1, email "updated@mail.com", and phone "987654321"
    Then I should receive an error "Passenger with email 'updated@mail.com' already exist"

  Scenario: Delete a passenger
    Given a passenger exists with id 1, email "test@mail.com", and phone "123456789"
    When I delete the passenger with id 1
    Then the passenger should be deleted

  Scenario: Get a passenger by ID
    Given a passenger exists with id 1, email "test@mail.com", and phone "123456789"
    When I request the passenger by id 1
    Then I should receive the passenger with email "test@mail.com"

  Scenario: Get a passenger that does not exist
    When I request a passenger by id 999
    Then I should receive an error "Not found passenger with id: '999'"

  Scenario: Charge money to a passenger
    Given a passenger exists with id 1 and current money "100"
    When I charge "50" to the passenger with id 1
    Then the passenger's money should be "150"

  Scenario: Charge money to a non-existent passenger
    When I try to charge "50" to a passenger with id 999
    Then I should receive an error "Not found passenger with id: '999'"
