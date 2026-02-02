package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/*
* UNIT Tests
*/
class IntersectionTest {

  private Intersection intersection;

  @BeforeEach
  void setUp() {
    intersection = new Intersection();
  }

  @AfterEach
  void tearDown() {
    if (intersection != null) {
      intersection.deactivate();
    }
  }

  @Test
  @DisplayName("New intersection defaults to no roads")
  void testIntersectionInitialization() {
    assertThat(intersection.getRoads()).hasSize(0);
  }
}
