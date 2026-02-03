import { ref } from 'vue'
import type { GlobalObject } from '../types'

const API_URL: string = 'http://localhost:8080/intersections'

export const global: GlobalObject = {
  API_URL,
  intersectionData: ref(null),
}
