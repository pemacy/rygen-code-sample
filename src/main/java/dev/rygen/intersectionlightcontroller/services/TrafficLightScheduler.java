package dev.rygen.intersectionlightcontroller.services;

import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.repositories.LightRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class TrafficLightScheduler {

  @Autowired
  private LightRepository lightRepository;

  @Scheduled(fixedRate = 100)
  @Transactional
  public void processAllActiveLights() {
    try {
      List<Light> activeLights = lightRepository.findByActiveTrue();

      for (Light light : activeLights) {
        light.checkAndTransition();
        lightRepository.save(light);
      }
    } catch (Exception e) {
      System.err.println("Error in TrafficLightScheduler: " + e.getMessage());
      e.printStackTrace();
      throw new RuntimeException("Scheduler stopped due to error", e);
    }
  }
}
