package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Intersection;
import lombok.Builder;

import java.util.List;
import java.util.Map;

@Builder
public record IntersectionDTO(
    int intersectionId,
    boolean active,
    List<RoadDTO> roads,
    Map<String, Long> durationForAllColors) {
  public static IntersectionDTO fromEntity(Intersection intersection) {
    List<RoadDTO> roadDTOs = intersection.getRoads().stream()
        .map(RoadDTO::fromEntity)
        .toList();

    return IntersectionDTO.builder()
        .intersectionId(intersection.getIntersectionId())
        .active(intersection.isActive())
        .roads(roadDTOs)
        .durationForAllColors(intersection.getDurationForAllColors())
        .build();
  }
}
