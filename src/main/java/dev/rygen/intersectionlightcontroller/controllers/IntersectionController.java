package dev.rygen.intersectionlightcontroller.controllers;

import dev.rygen.intersectionlightcontroller.dtos.ChangeLightColorRequest;
import dev.rygen.intersectionlightcontroller.dtos.IntersectionDTO;
import dev.rygen.intersectionlightcontroller.entities.Intersection;
import dev.rygen.intersectionlightcontroller.services.IntersectionService;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin(origins = "*")
@RequestMapping("/intersections")
public class IntersectionController {

  private final IntersectionService intersectionService;

  public IntersectionController(IntersectionService intersectionService) {
    this.intersectionService = intersectionService;
  }

  @PostMapping
  public ResponseEntity<IntersectionDTO> createIntersection() {
    Intersection intersection = intersectionService.createIntersection();
    IntersectionDTO dto = IntersectionDTO.fromEntity(intersection);
    return ResponseEntity.status(HttpStatus.CREATED).body(dto);
  }

  @GetMapping("/{id}")
  public ResponseEntity<IntersectionDTO> getIntersection(@PathVariable Long id) {
    return intersectionService.getIntersection(id)
        .map(IntersectionDTO::fromEntity)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{id}/activate")
  @CrossOrigin(origins = "*")
  public ResponseEntity<IntersectionDTO> activateIntersection(@PathVariable Long id) {
    return intersectionService.activateIntersection(id)
        .map(IntersectionDTO::fromEntity)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping("/{id}/deactivate")
  @CrossOrigin(origins = "*")
  public ResponseEntity<IntersectionDTO> deactivateIntersection(@PathVariable Long id) {
    return intersectionService.deactivateIntersection(id)
        .map(IntersectionDTO::fromEntity)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PutMapping("/{id}/lights")
  public ResponseEntity<IntersectionDTO> changeLightColor(
      @PathVariable Long id,
      @RequestBody ChangeLightColorRequest request) {
    return intersectionService.changeLightColor(id, request.lightId(), request.newColor())
        .map(IntersectionDTO::fromEntity)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}
