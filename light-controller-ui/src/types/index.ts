import type { Ref, ComputedRef } from 'vue'

interface DurationForAllColors {
  green: number
  yellow: number
  red: number
}

export interface Light {
  lightId: number
  color: string
  active: boolean
  colorChangedAtMillis: number
  elapsedTimeMillis: number
  durationForCurrentColor: number
  initialColor?: string
  durationForAllColors?: DurationForAllColors
}

export interface LightTimingInfo {
  greenDurationMillis: number
  yellowDurationMillis: number
  redDurationMillis: number
}

export interface Road {
  roadId: number
  active: boolean
  lights: Light[]
}

export interface Intersection {
  intersectionId: number
  active: boolean
  roads: Road[]
  durationForAllColors?: DurationForAllColors
}

export interface GlobalObject {
  intersectionData?: Ref<Intersection | null>
  road1?: Road
  road2?: Road
  startPolling?: () => void
  stopPolling?: () => void
  API_URL: string
}
