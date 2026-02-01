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
class LightTest {

  private Light light;

  @BeforeEach
  void setUp() {
    light = new Light(LightColor.GREEN);
  }

  @AfterEach
  void tearDown() {
    if (light != null) {
      light.deactivate();
    }
  }

  // ==================== Color Transition Tests ====================

  @Test
  @DisplayName("Color sequence: GREEN -> YELLOW -> RED -> GREEN")
  void testColorTransitionSequence() {
    // Start with GREEN
    assertThat(light.getColor()).isEqualTo(LightColor.GREEN);

    // GREEN -> YELLOW
    light.setColorInternal(LightColor.GREEN);
    LightColor next1 = light.getColor().next();
    assertThat(next1).isEqualTo(LightColor.YELLOW);

    // YELLOW -> RED
    light.setColorInternal(LightColor.YELLOW);
    LightColor next2 = light.getColor().next();
    assertThat(next2).isEqualTo(LightColor.RED);

    // RED -> GREEN
    light.setColorInternal(LightColor.RED);
    LightColor next3 = light.getColor().next();
    assertThat(next3).isEqualTo(LightColor.GREEN);
  }

  @Test
  @DisplayName("Initialized light starts out deactivated")
  void testDeactivatedInitializedLight() {
    assertThat(light.isActive()).isFalse();
  }

  @Test
  @DisplayName("getDurationForCurrentColor: Returns correct duration for colors")
  void testDurationForCurrentColor() {
    light.setColor(LightColor.GREEN);
    assertThat(light.getDurationForCurrentColor()).isEqualTo(3000);

    light.setColor(LightColor.YELLOW);
    assertThat(light.getDurationForCurrentColor()).isEqualTo(1000);

    light.setColor(LightColor.RED);
    assertThat(light.getDurationForCurrentColor()).isEqualTo(4000);
  }

  @Test
  @DisplayName("Activate light sets active to true and sets milliseconds changed")
  void testLightActivation() {
    light.activate();
    assertThat(light.isActive()).isTrue();
    try {

      Thread.sleep(50);
      assertThat(light.getElapsedTimeMillis()).isGreaterThan(0);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
  }

  @Test
  @DisplayName("getElapsedTimeMillis: Returns correct elapsed time")
  void testGetElapsedTimeMillis() {
    light.setColorChangedAtMillis(System.currentTimeMillis() - 1001);
    assertThat(light.getElapsedTimeMillis()).isGreaterThan(1000);
  }

  @Test
  @DisplayName("checkAndTransition: GREEN transitions to YELLOW after 4 seconds")
  void testGreenTransitionToYellow() {
    LightColor testColor = LightColor.GREEN;
    light.setColorInternal(testColor);
    light.setActive(true);

    light.setColorChangedAtMillis(System.currentTimeMillis() - 4001);
    ;
    light.checkAndTransition();

    assertThat(light.getColor()).isEqualTo(testColor.next());
  }
}
