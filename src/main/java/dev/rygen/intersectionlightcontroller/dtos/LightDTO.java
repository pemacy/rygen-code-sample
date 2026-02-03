package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.entities.Light;
import dev.rygen.intersectionlightcontroller.enums.LightColor;
import lombok.Builder;

@Builder
public record LightDTO(
    Long lightId,
    LightColor color,
    boolean active,
    Long colorChangedAtMillis,
    long elapsedTimeMillis,
    long durationForCurrentColor) {
  public static LightDTO fromEntity(Light light) {
    return LightDTO.builder()
        .lightId(light.getLightId())
        .color(light.getColor())
        .active(light.isActive())
        .colorChangedAtMillis(light.getColorChangedAtMillis())
        .elapsedTimeMillis(light.getElapsedTimeMillis())
        .durationForCurrentColor(light.getDurationForCurrentColor())
        .build();
  }
}
