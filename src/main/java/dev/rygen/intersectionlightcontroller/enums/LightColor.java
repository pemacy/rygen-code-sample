package dev.rygen.intersectionlightcontroller.enums;

public enum LightColor {
  GREEN,
  YELLOW,
  RED;

  public LightColor next () {
    return switch (this) {
      case GREEN -> YELLOW;
      case YELLOW -> RED;
      case RED -> GREEN;
    };
  }

  public LightColor opposite () {
    return switch(this) {
      case GREEN -> RED;
      case RED -> GREEN;
      case YELLOW -> YELLOW;
    };
  }
}
