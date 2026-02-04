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
        "green", LightColor.GREEN.duration(),
        "yellow", LightColor.YELLOW.duration(),
        "red", LightColor.RED.duration());
  }

  public long getDurationForCurrentColor() {
    return color.duration();
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
    long duration = color.duration();

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
