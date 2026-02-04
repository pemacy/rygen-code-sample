
# We do not expect you to complete all scenarios.
# Complete as many scenarios as you feel accurately demonstrate your abilities.
# Feel free to add additional scenarios you feel are necessary.
# You may also refactor any parts of this sample as you see fit.

Feature: Intersection Light Controller
  As a city traffic manager
  I want to control and manage traffic lights at intersections
  So that I can efficiently regulate traffic flow

  Background:
    Given an intersection with two roads
    And each road has two sets of traffic lights
    And lights have standard colors: Green, Yellow, and Red
    And lights cycle through colors in order: Green -> Yellow -> Red -> Green

  Scenario: Turning on lights at an intersection - COMPLETE
    When I activate the lights for a specific intersection
    Then the lights should start their color cycling sequence
    And each road's lights should operate independently

  Scenario: Turning off lights at an intersection - COMPLETE
    Given the lights are currently active at an intersection
    When I deactivate the lights for that intersection
    Then all lights at the intersection should be turned off

  Scenario: Updating light configuration - TODO
    Given an existing intersection
    When I update the configuration for a specific light
    Then the light's parameters should be modified as requested
    And the change should be immediately reflected in the light's behavior

  Scenario: Retrieving light configuration - COMPLETE
    Given an existing intersection
    When I request the configuration for a specific light
    Then I should receive the current settings for that light
    And the configuration should include all relevant parameters

  Scenario: Ensuring light synchronization within an intersection - COMPLETE for intersection, TODO per light
    Given an intersection with multiple lights
    When the lights are activated
    Then the lights on each road should be coordinated
    And no conflicting signals should be displayed simultaneously

  Scenario Outline: Color sequence validation - COMPLETE
    Given a traffic light at an intersection
    When the light starts its cycle
    Then it should progress through colors in order
    Examples:
      | Current Color | Next Color |
      | Green         | Yellow     |
      | Yellow        | Red        |
      | Red           | Green      |
