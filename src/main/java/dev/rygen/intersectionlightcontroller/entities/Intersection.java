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

  @OneToMany(mappedBy = "intersection", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
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
    road2.initializeLights(LightColor.RED);

    roads.add(road1);
    roads.add(road2);
  }

  public void activate() {
    this.active = true;
    for (Road road : roads) {
      road.activate();
    }
  }

  public void deactivate() {
    this.active = false;
    for (Road road : roads) {
      road.deactivate();
    }
  }

  public boolean isActive() {
    return this.active;
  }

  public Road getRoad(int index) {
    index -= 1;
    if (index < 0 || index >= roads.size())
      return null;
    return roads.get(index);
  }

  public synchronized void onRoadLightChanged(Road sourceRoad, LightColor newColor) {
    Road oppositeRoad = getOppositeRoad(sourceRoad);

    if (oppositeRoad != null) {
      LightColor oppositeColor = newColor.opposite();
      oppositeRoad.synchronizeLights(oppositeColor);
    }
  }

  public Road getOppositeRoad(Road sourceRoad) {
    boolean roadExists = roads.stream().anyMatch(r -> r == sourceRoad);

    if (!roadExists)
      return null;

    return roads.stream()
        .filter(r -> !r.equals(sourceRoad))
        .findFirst()
        .orElse(null);
  }
}
