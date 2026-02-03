import { global } from '@/stores/appState'
import type { Intersection } from '@/types'
import axios from 'axios'

export const createIntersection = async () => {
  console.log('Creating intersection...')

  try {
    const res = await axios.post(API_URL, {})
    if (global.intersectionData?.value != null) {
      const data = res.data
      global.intersectionData.value = res.data
      global.road1 = data.roads[0]
      global.road2 = data.roads[1]
      console.log('Intersection Created:', data.intersectionId)
    }
  } catch {
    console.error('Intersection failed to be created')
  }
}

export const fetchIntersectionState = async () => {
  try {
    const res = await axios.get(`${API_URL}/${intersectionData.value.intersectionId}`)
    intersectionData.value = res.data
    console.log('New intersection data:', intersectionData.value)
  } catch {
    console.info('Intersection data failed to fetch')
  }
}
