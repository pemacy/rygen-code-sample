package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.enums.LightColor;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@Table(name = "road")
public class Road {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "road_id")
  private Long roadId;

  @Column(name = "active")
  private boolean active = false;

  @ManyToOne
  @JoinColumn(name = "intersection_id")
  @ToString.Exclude // Prevent circular reference in toString
  private Intersection intersection;

  @OneToMany(mappedBy = "road", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Light> lights = new ArrayList<>();

  public void initializeLights(LightColor initialColor) {
    for (int i = 0; i < 2; i++) {
      Light light = new Light(initialColor);
      light.setRoad(this);
      lights.add(light);
    }
  }

  public Light getLight(int index) {
    return getLights().get(index - 1);
  }

  public void activate() {
    this.active = true;
    for (Light light : lights) {
      light.activate();
    }
  }

  public void deactivate() {
    this.active = false;
    for (Light light : lights) {
      light.deactivate();
    }
  }

  public void onLightChanged(LightColor newColor) {
    synchronizeLights(newColor);
    if (intersection != null) {
      intersection.onRoadLightChanged(this, newColor);
    }
  }

  public synchronized void synchronizeLights(LightColor color) {
    for (Light light : lights) {
      // prevent race condition by stopping and starting timer
      light.stopTimer();
      light.setColorInternal(color);
      if (light.isActive()) {
        light.startTimer();
      }
    }
  }

  public LightColor getCurrentColor() {
    if (lights.isEmpty())
      return null;
    return lights.get(0).getColor();
  }

  public boolean isActive() {
    return this.active;
  }
}
