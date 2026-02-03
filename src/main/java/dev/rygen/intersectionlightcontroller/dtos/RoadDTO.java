package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Road;
import lombok.Builder;

import java.util.List;

@Builder
public record RoadDTO(
    Long roadId,
    boolean active,
    List<LightDTO> lights) {
  public static RoadDTO fromEntity(Road road) {
    List<LightDTO> lightDTOs = road.getLights().stream()
        .map(LightDTO::fromEntity)
        .toList();

    return RoadDTO.builder()
        .roadId(road.getRoadId())
        .active(road.isActive())
        .lights(lightDTOs)
        .build();
  }
}
