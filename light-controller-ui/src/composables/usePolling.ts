import { onMounted, onUnmounted } from 'vue'

export const usePolling = (cb: () => void, interval: number) => {
  let pollingInterval: number | null = null

  onMounted(() => {
    cb()
    pollingInterval = setInterval(cb, interval)
  })

  onUnmounted(() => {
    if (pollingInterval) clearInterval(pollingInterval)
  })

  const stopPolling = () => {
    if (pollingInterval) {
      clearInterval(pollingInterval)
      pollingInterval = null
    }
  }

  const startPolling = () => {
    if (!pollingInterval) {
      cb()
      pollingInterval = setInterval(cb, interval)
    }
  }

  return { stopPolling, startPolling }
}
