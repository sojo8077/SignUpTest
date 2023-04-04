Feature: Sign up

  Scenario Outline:
    Given I use "<browser>"
    Given I enter "<email>" as email
    Given I enter "<username>" as username
    Given I enter "<password>" as password
    When I press Sign Up
    Then I am signed up "<signedUp>"
    Examples:
      | browser | email  | username | password        | signedUp |
      | chrome  | blabla | blabla   | .BQwvnE-Nu!L4aw | success  |
      | chrome  | a      | long     | .BQwvnE-Nu!L4aw | fail     |
      | chrome  | x      | existing | .BQwvnE-Nu!L4aw | fail     |
      | chrome  |        | anon     | .BQwvnE-Nu!L4aw | fail     |
      | edge    | blabla | haha     | .BQwvnE-Nu!L4aw | success  |
      | edge    | b      | long     | .BQwvnE-Nu!L4aw | fail     |
      | edge    | c      | existing | .BQwvnE-Nu!L4aw | fail     |
      | edge    |        | anon     | .BQwvnE-Nu!L4aw | fail     |
