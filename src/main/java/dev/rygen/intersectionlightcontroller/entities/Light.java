package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import java.util.Map;

@Entity
@Data
@NoArgsConstructor
@Table(name = "light")
public class Light {
  public static final long GREEN_DURATION_MILLIS = 3000;
  public static final long RED_DURATION_MILLIS = 4000;
  public static final long YELLOW_DURATION_MILLIS = 1000;

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "light_id")
  private Long lightId;

  @Enumerated(EnumType.STRING)
  @Column(name = "initial_color")
  private LightColor initialColor;

  @Enumerated(EnumType.STRING)
  @Column(name = "color")
  private LightColor color;

  @Column(name = "active")
  private boolean active = false;

  @Column(name = "color_changed_at_millis")
  private Long colorChangedAtMillis;

  @ManyToOne
  @JoinColumn(name = "road_id")
  @ToString.Exclude
  private Road road;

  public Light(LightColor initialColor) {
    this.initialColor = initialColor;
    this.color = LightColor.OFF;
    this.colorChangedAtMillis = System.currentTimeMillis();
  }

  public Map<String, Long> getDurationForAllColors() {
    return Map.of(
        "green", GREEN_DURATION_MILLIS,
        "yellow", YELLOW_DURATION_MILLIS,
        "red", RED_DURATION_MILLIS);
  }

  public long getDurationForCurrentColor() {
    return switch (color) {
      case GREEN -> GREEN_DURATION_MILLIS;
      case YELLOW -> YELLOW_DURATION_MILLIS;
      case RED -> RED_DURATION_MILLIS;
      case OFF -> Long.MAX_VALUE;
    };
  }

  public long getElapsedTimeMillis() {
    if (colorChangedAtMillis == null) {
      return 0;
    }
    return System.currentTimeMillis() - colorChangedAtMillis;
  }

  public void setColorInternal(LightColor newColor) {
    this.color = newColor;
    this.colorChangedAtMillis = System.currentTimeMillis();
  }

  public void changeColor(LightColor newColor) {
    setColorInternal(newColor);

    if (road != null) {
      road.onLightChanged(newColor);
    }
  }

  public synchronized void checkAndTransition() {
    if (!active) {
      setColorInternal(LightColor.OFF);
      return;
    }

    long elapsed = getElapsedTimeMillis();
    long duration = getDurationForCurrentColor();

    if (elapsed >= duration) {
      // System.out.println("Light " + lightId + ": " + color);
      LightColor nextColor = color.next();
      changeColor(nextColor);
    }
  }

  public void deactivate() {
    System.out.println("Light " + lightId + ": DeActivated");
    setColorInternal(LightColor.OFF);
    this.active = false;
  }

  public void activate() {
    System.out.println("Light " + lightId + ": Activated");
    setColorInternal(initialColor);
    this.active = true;
    this.colorChangedAtMillis = System.currentTimeMillis();
  }

  public boolean isActive() {
    return this.active;
  }
}
