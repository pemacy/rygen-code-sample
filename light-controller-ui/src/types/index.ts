import type { Ref } from 'vue'

export interface Light {
  lightId: number
  color: string
  active: boolean
  colorChangedAtMillis: number
  elapsedTimeMillis: number
  durationForCurrentColor: number
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
}

export interface GlobalObject {
  intersectionData?: Ref<Intersection | null>
  road1?: Road
  road2?: Road
  startPolling?: () => void
  stopPolling?: () => void
  API_URL: string
}
