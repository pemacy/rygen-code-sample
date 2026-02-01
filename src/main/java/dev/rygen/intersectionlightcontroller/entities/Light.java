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
import jakarta.persistence.Transient;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

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
    this.color = initialColor;
    this.colorChangedAtMillis = System.currentTimeMillis();
  }

  public long getDurationForCurrentColor() {
    return switch (color) {
      case GREEN -> GREEN_DURATION_MILLIS;
      case YELLOW -> YELLOW_DURATION_MILLIS;
      case RED -> RED_DURATION_MILLIS;
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

  public void activate() {
    this.active = true;
    this.colorChangedAtMillis = System.currentTimeMillis();
  }

  public synchronized void checkAndTransition() {
    if (!active)
      return;

    long elapsed = getElapsedTimeMillis();
    long duration = getDurationForCurrentColor();

    if (elapsed >= duration) {
      LightColor nextColor = color.next();
      setColorInternal(nextColor);
    }
  }

  public void deactivate() {
    this.active = false;
  }

  public boolean isActive() {
    return this.active;
  }
}
