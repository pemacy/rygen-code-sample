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
class RoadTest {

  private Road road;

  @BeforeEach
  void setUp() {
    road = new Road();
  }

  @AfterEach
  void tearDown() {
    if (road != null) {
      road.deactivate();
    }
  }

  @Test
  @DisplayName("Initialized Road has no lights")
  void testInitializedRoad() {
    assertThat(road.getLights()).hasSize(0);
  }

  @Test
  @DisplayName("initializeLights: creates 2 lights")
  void testInitializeLights() {
    road.initializeLights(LightColor.GREEN);
    assertThat(road.getLights()).hasSize(2);
    assertThat(road.getLight(1).getColor()).isEqualTo(LightColor.GREEN);
    assertThat(road.getLight(2).getColor()).isEqualTo(LightColor.GREEN);
  }

  @Test
  @DisplayName("activate: Activates lights")
  void testActivateRoad() {
    road.initializeLights(LightColor.GREEN);
    road.activate();
    assertThat(road.getLight(1).isActive()).isTrue();
    assertThat(road.getLight(2).isActive()).isTrue();
  }

  @Test
  @DisplayName("deactivate: DeActivates lights")
  void testDeActivateRoad() {
    road.initializeLights(LightColor.GREEN);
    road.activate();
    assertThat(road.getLight(1).isActive()).isTrue();
    assertThat(road.getLight(2).isActive()).isTrue();
    road.deactivate();
    assertThat(road.getLight(1).isActive()).isFalse();
    assertThat(road.getLight(2).isActive()).isFalse();
  }
}
