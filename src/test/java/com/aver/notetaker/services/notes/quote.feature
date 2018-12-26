Feature:

  Scenario: Get a chuck norris joke
    Given A request for a random chuck norris joke
    When Get a joke
    Then single joke is returned