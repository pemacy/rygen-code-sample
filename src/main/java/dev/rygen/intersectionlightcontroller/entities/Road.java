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

  @ManyToOne
  @JoinColumn(name = "intersection_id")
  @ToString.Exclude // Prevent circular reference in toString
  private Intersection intersection;

  @OneToMany(mappedBy = "road", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Light> lights = new ArrayList<>();

  /**
   * Initializes the road with 2 lights of the given color.
   */
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
    for (Light light : lights) {
      light.activate();
    }
  }

  public void deactivate() {
    for (Light light : lights) {
      light.deactivate();
    }
  }
}
