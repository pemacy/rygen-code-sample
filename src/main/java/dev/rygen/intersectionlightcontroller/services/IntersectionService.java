package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.repositories.IntersectionRepository;
import dev.rygen.intersectionlightcontroller.repositories.LightRepository;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;
import java.util.List;

@Service
public class IntersectionService {

  private final IntersectionRepository intersectionRepository;

  public IntersectionService(IntersectionRepository intersectionRepository) {
    this.intersectionRepository = intersectionRepository;
  }

  public Intersection createIntersection() {
    Intersection intersection = Intersection.createWithRoads();
    return this.intersectionRepository.save(intersection);
  }

  @Transactional(readOnly = true)
  public Optional<Intersection> getIntersection(Long id) {
    return this.intersectionRepository.findById(id);
  }

  @Transactional
  public Optional<Intersection> activateIntersection(Long id) {
    Optional<Intersection> intersection = this.intersectionRepository.findById(id);
    intersection.ifPresent(inter -> {
      inter.activate();
      intersectionRepository.save(inter);
    });
    return intersection;
  }

  @Transactional
  public Optional<Intersection> deactivateIntersection(Long id) {
    Optional<Intersection> intersection = this.intersectionRepository.findById(id);
    intersection.ifPresent(inter -> {
      inter.deactivate();
      intersectionRepository.save(inter);
    });
    return intersection;
  }

  @Transactional
  public void deleteIntersection(Long id) {
    Optional<Intersection> intersection = this.intersectionRepository.findById(id);
    intersection.ifPresent(inter -> {
      inter.deactivate();
      intersectionRepository.delete(inter);
    });
  }

  @Transactional
  public Optional<Intersection> changeLightColor(Long intersectionId, Long lightId, LightColor newColor) {
    Optional<Light> light = LightRepository
    Optional<Intersection> intersectionOpt = intersectionRepository.findById(intersectionId);

    if (intersectionOpt.isEmpty()) {
      return Optional.empty();
    }

    Intersection intersection = intersectionOpt.get();

    // Find which road the light belongs to
    var roads = intersection.getRoads();
    if (roads.size() < 2) {
      return Optional.empty();
    }

    // Determine if the light is on Road 1 (index 0) or Road 2 (index 1)
    boolean isRoad1 = roads.get(0).getLights().stream()
        .anyMatch(light -> light.getLightId().equals(lightId));

    // Calculate the timestamp offset needed for the desired color
    // This makes the cycle calculation produce the correct color
    long cycleOffset = calculateCycleOffset(newColor, isRoad1);
    long newTimestamp = System.currentTimeMillis() - cycleOffset;

    // Update ALL lights in the intersection to the new timestamp
    // This keeps them synchronized
    for (var road : roads) {
      for (var light : road.getLights()) {
        light.setColorChangedAtMillis(newTimestamp);
      }
    }

    // Save and return
    Intersection saved = intersectionRepository.save(intersection);
    return Optional.of(saved);
  }
}
