<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { usePolling } from '../composables/usePolling'
import axios from 'axios'
import type { Intersection, Road, Light } from '../types/'
import TrafficLight from './TrafficLight.vue'
import { global } from '../stores/appState'
import { activateIntersection, deactivateIntersection, createIntersection, fetchIntersectionState } from '../services'

const { stopPolling, startPolling } = usePolling(fetchIntersectionState, 100)
global.stopPolling = stopPolling
global.startPolling = startPolling

const showInfo = ref(false)

onMounted(async () => {
  await createIntersection()
})

onUnmounted(() => {
  if (typeof global.stopPolling === 'function') {
    global.stopPolling()
  }
})

const totalLights = computed(() => {
  if (!global.intersectionData?.value) return 0
  return global.intersectionData.value.roads.reduce(
    (sum, road) => sum + road.lights.length, 0
  )
})

const formatDuration = (ms: number | undefined): string => {
  if (ms === undefined) return 'N/A'
  return `${(ms / 1000).toFixed(1)}s`
}

const totalCycleSeconds = computed(() => {
  const timing = global.intersectionData?.value?.durationForAllColors
  if (!timing) return 'N/A'
  const total = timing.green + timing.yellow + timing.red
  return `${(total / 1000).toFixed(1)}s`
})
</script>
<template>
  <div class='intersection'>
    <div class='north-light'>
      <TrafficLight :light="global.intersectionData?.value?.roads[0]?.lights[0]" />
    </div>
    <div class='east-west-lights'>
      <TrafficLight :light="global.intersectionData?.value?.roads[1]?.lights[0]" />
      <TrafficLight :light="global.intersectionData?.value?.roads[1]?.lights[1]" />
    </div>
    <div class='south-light'>
      <TrafficLight :light="global.intersectionData?.value?.roads[0]?.lights[1]" />
    </div>
  </div>
  <div class='footer'>
    <div v-if="global.intersectionData?.value?.active">
      <button @click="deactivateIntersection">DeActivate Intersection</button>
    </div>
    <div v-else>
      <button @click="activateIntersection">Activate Intersection</button>
    </div>
    <button class="info-toggle" @click="showInfo = !showInfo">
      {{ showInfo ? 'Hide Info' : 'View Info' }}
    </button>
  </div>

  <div v-if="showInfo" class="info-panel">
    <h3>Intersection Information</h3>
    <div class="info-row">
      <span class="label">Intersection ID:</span>
      <span>{{ global.intersectionData?.value?.intersectionId }}</span>
    </div>
    <div class="info-row">
      <span class="label">Status:</span>
      <span>{{ global.intersectionData?.value?.active ? 'Active' : 'Inactive' }}</span>
    </div>
    <div class="info-row">
      <span class="label">Roads:</span>
      <span>{{ global.intersectionData?.value?.roads.length }}</span>
    </div>
    <div class="info-row">
      <span class="label">Total Lights:</span>
      <span>{{ totalLights }}</span>
    </div>
    <h4>Default Timing Configuration</h4>
    <div class="info-row">
      <span class="label">Green Duration:</span>
      <span>{{ formatDuration(global.intersectionData?.value?.durationForAllColors?.green) }}</span>
    </div>
    <div class="info-row">
      <span class="label">Yellow Duration:</span>
      <span>{{ formatDuration(global.intersectionData?.value?.durationForAllColors?.yellow) }}</span>
    </div>
    <div class="info-row">
      <span class="label">Red Duration:</span>
      <span>{{ formatDuration(global.intersectionData?.value?.durationForAllColors?.red) }}</span>
    </div>
    <div class="info-row total-cycle">
      <span class="label">Total Cycle Time:</span>
      <span>{{ totalCycleSeconds }}</span>
    </div>
  </div>
</template>
<style>
.intersection {
  display: flex;
  flex-direction: column;
  gap: 2rem;
}

.north-light,
.south-light {
  display: flex;
  justify-content: center;
  align-items: center;
  flex: 1;
}

.east-west-lights {
  display: flex;
  justify-content: center;
  align-items: center;
  gap: 2rem;
  flex: 1;
}

.east-west-lights>* {
  flex: 1;
}

button {
  padding: 0.5rem 1rem;
  background-color: #2c3e50;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

button:hover {
  background-color: #9eaeb0;
}

.footer {
  margin-top: 50px;
  text-align: center;
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 0.5rem;
}

.info-toggle {
  background-color: #34495e;
}

.info-toggle:hover {
  background-color: #5d6d7e;
}

.info-panel {
  margin-top: 1.5rem;
  padding: 1rem 1.5rem;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 8px;
  max-width: 400px;
  margin-left: auto;
  margin-right: auto;
  color: black;
}

.info-panel h3 {
  margin: 0 0 1rem 0;
  color: #2c3e50;
  font-size: 1.1rem;
  border-bottom: 2px solid #2c3e50;
  padding-bottom: 0.5rem;
}

.info-panel h4 {
  margin: 1rem 0 0.5rem 0;
  color: #495057;
  font-size: 0.95rem;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 0.35rem 0;
  border-bottom: 1px solid #e9ecef;
}

.info-row:last-child {
  border-bottom: none;
}

.info-row .label {
  font-weight: 600;
  color: #495057;
}

.info-row.total-cycle {
  margin-top: 0.5rem;
  padding-top: 0.5rem;
  border-top: 2px solid #dee2e6;
  font-weight: bold;
}

.info-row.total-cycle .label {
  color: #2c3e50;
}
</style>
