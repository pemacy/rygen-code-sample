package dev.rygen.intersectionlightcontroller.entities;

import dev.rygen.intersectionlightcontroller.enums.LightColor;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.OneToMany;

import java.util.ArrayList;
import java.util.List;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@Table(name = "intersection")
public class Intersection {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "intersection_id")
  private int intersectionId;

  @Column(name = "active")
  private boolean active = false;

  @OneToMany(mappedBy = "intersetion", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
  private List<Road> roads = new ArrayList<>();

  public Intersection(boolean initialize) {
    if (initialize) {
      initializeRoads();
    }
  }

  public static Intersection createWithRoads() {
    Intersection intersection = new Intersection(true);
    return intersection;
  }

  public void initializeRoads() {
    Road road1 = new Road();
    road1.setIntersection(this);
    road1.initializeLights(LightColor.GREEN);
    Road road2 = new Road();
    road2.setIntersection(this);
    road1.initializeLights(LightColor.RED);

    roads.add(road1);
    roads.add(road2);
  }

  public void activate() {
    for (Road road : roads) {
      road.activate();
    }
  }

  public void deactivate() {
    for (Road road : roads) {
      road.deactivate();
    }
  }
}
