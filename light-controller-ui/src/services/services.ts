import { global } from '@/stores/appState'
import axios from 'axios'

export const createIntersection = async () => {
  console.log('Creating intersection...')

  try {
    const res = await axios.post(global.API_URL, {})
    const data = res.data
    if (!global.intersectionData) throw new Error('global.intersectionData not defined')
    global.intersectionData.value = res.data
    global.road1 = data.roads[0]
    global.road2 = data.roads[1]
    console.log('Intersection Created:', data.intersectionId)
  } catch {
    console.error('Intersection failed to be created')
  }
}

export const fetchIntersectionState = async () => {
  try {
    if (global.intersectionData?.value?.intersectionId) {
      const res = await axios.get(
        `${global.API_URL}/${global.intersectionData.value.intersectionId}`,
      )
      global.intersectionData.value = res.data
      console.log('New intersection data:', global.intersectionData.value)
      console.info(
        'Intersection is:',
        global.intersectionData?.value?.active ? 'Active' : 'Deactive',
      )
    }
  } catch {
    console.info('Intersection data failed to fetch')
  }
}

export const activateIntersection = async () => {
  console.log('Activating intersection')
  if (global.intersectionData?.value?.intersectionId && global.startPolling) {
    try {
      const res = await axios.post(
        `${global.API_URL}/${global.intersectionData.value?.intersectionId}/activate`,
      )
      if (res.status === 200) {
        global.startPolling()
        global.intersectionData.value = res.data
      } else {
        console.error(
          'Intersection not activated, something when wrote. Response code: ',
          res.status,
        )
      }
    } catch {
      console.error('An error occured while activating the intersection')
    }
  } else {
    console.log('Activation failed')
  }
}

export const deactivateIntersection = async () => {
  if (global.intersectionData?.value?.intersectionId && global.stopPolling) {
    try {
      const res = await axios.post(
        `${global.API_URL}/${global.intersectionData.value?.intersectionId}/deactivate`,
      )
      if (res.status === 200) {
        global.intersectionData.value = res.data
        global.stopPolling()
      } else {
        console.error('Intersection not deactivated, something when wrote')
      }
    } catch {
      console.error('An error occured while de-activating the intersection')
    }
  }
}
