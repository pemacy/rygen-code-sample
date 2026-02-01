package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for the Light entity.
 * Tests color transitions, timing constants, and timer behavior.
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
}
