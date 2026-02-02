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

  @Test
  @DisplayName("New intersection defaults to deactivated")
  void testIntersectionInitializationIsDeactivated() {
    assertThat(intersection.isActive()).isFalse();
  }

  @Test
  @DisplayName("Intersection can activate")
  void testIntersectionActivation() {
    intersection.initializeRoads();
    intersection.activate();
    assertThat(intersection.isActive()).isTrue();
    assertThat(intersection.getRoad(1).isActive()).isTrue();
  }

  @Test
  @DisplayName("oppositeRoad Test")
  void testOppositeRoad() {
    intersection.initializeRoads();
    Road road1 = intersection.getRoad(1);
    Road road2 = intersection.getRoad(2);

    Road oppositeRoad = intersection.getOppositeRoad(road1);
    assertThat(oppositeRoad).isEqualTo(road2);
  }

  @Test
  @DisplayName("onRoadLightChanged Test road1 RED changes road2 to GREEN")
  void testOnRoadLightChangedRed() {
    intersection.initializeRoads();
    Road road1 = intersection.getRoad(1);
    Road road2 = intersection.getRoad(2);

    assertThat(road1.getCurrentColor()).isEqualTo(LightColor.GREEN);
    assertThat(road2.getCurrentColor()).isEqualTo(LightColor.RED);

    intersection.onRoadLightChanged(road1, LightColor.RED);

    assertThat(road2.getCurrentColor()).isEqualTo(LightColor.GREEN);
  }

  @Test
  @DisplayName("onRoadLightChanged Test road1 YELLOW does not change road2 color")
  void testOnRoadLightChangedYellow() {
    intersection.initializeRoads();
    Road road1 = intersection.getRoad(1);
    Road road2 = intersection.getRoad(2);

    assertThat(road1.getCurrentColor()).isEqualTo(LightColor.GREEN);
    assertThat(road2.getCurrentColor()).isEqualTo(LightColor.RED);

    intersection.onRoadLightChanged(road1, LightColor.YELLOW);

    assertThat(road2.getCurrentColor()).isEqualTo(LightColor.RED);
  }
}
