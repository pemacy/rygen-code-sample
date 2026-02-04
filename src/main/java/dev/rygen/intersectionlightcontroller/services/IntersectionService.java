package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import dev.rygen.intersectionlightcontroller.repositories.*;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.Optional;

@Service
public class IntersectionService {

  private final IntersectionRepository intersectionRepository;
  private final LightRepository lightRepository;

  public IntersectionService(
      IntersectionRepository intersectionRepository,
      LightRepository lightRepository) {
    this.intersectionRepository = intersectionRepository;
    this.lightRepository = lightRepository;
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
  public Optional<Intersection> deactivateLight(Long intersectionId, Long lightId) {
    Optional<Light> light = this.lightRepository.findById(lightId);
    light.ifPresent(lt -> {
      lt.deactivate();
      lightRepository.save(lt);
    });
    Optional<Intersection> intersection = this.intersectionRepository.findById(intersectionId);
    return intersection;
  }

  @Transactional
  public Optional<Intersection> activateLight(Long intersectionId, Long lightId) {
    Optional<Light> light = this.lightRepository.findById(lightId);
    light.ifPresent(lt -> {
      lt.activate();
      lightRepository.save(lt);
    });
    Optional<Intersection> intersection = this.intersectionRepository.findById(intersectionId);
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
    Optional<Intersection> intersectionOpt = intersectionRepository.findById(intersectionId);

    if (intersectionOpt.isEmpty()) {
      return Optional.empty();
    }

    Intersection intersection = intersectionOpt.get();

    Light targetLight = null;
    for (var road : intersection.getRoads()) {
      for (var light : road.getLights()) {
        if (light.getLightId().equals(lightId)) {
          targetLight = light;
          break;
        }
      }
      if (targetLight != null)
        break;
    }

    if (targetLight == null) {
      return Optional.empty();
    }

    targetLight.changeColor(newColor);

    Intersection saved = intersectionRepository.save(intersection);
    return Optional.of(saved);
  }
}
