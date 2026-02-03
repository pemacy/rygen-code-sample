package dev.rygen.intersectionlightcontroller.dtos;

import dev.rygen.intersectionlightcontroller.enums.LightColor;

public record ChangeLightColorRequest(
    Long lightId,
    LightColor newColor) {
}
