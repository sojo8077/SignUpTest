Feature: Sign up

  Scenario Outline:
    Given I use "<browser>"
    Given I enter "<email>" as email
    Given I enter "<username>" as username
    Given I enter "<password>" as password
    When I press Sign Up
    Then I am signed up "<signedUp>"
    Examples:
      | browser | email    | username | password        | signedUp |
      | chrome  | valid    | valid    | .BQwvnE-Nu!L4aw | success  |
      | chrome  | long     | long     | .BQwvnE-Nu!L4aw | fail     |
      | chrome  | existing | existing | .BQwvnE-Nu!L4aw | fail     |
      | chrome  |          | anon     | .BQwvnE-Nu!L4aw | fail     |
      | edge    | valid    | valid    | .BQwvnE-Nu!L4aw | success  |
      | edge    | long     | long     | .BQwvnE-Nu!L4aw | fail     |
      | edge    | existing | existing | .BQwvnE-Nu!L4aw | fail     |
      | edge    |          | anon     | .BQwvnE-Nu!L4aw | fail     |
