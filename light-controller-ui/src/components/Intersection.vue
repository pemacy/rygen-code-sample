<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { usePolling } from '../composables/usePolling'
import axios from 'axios'
import type { Intersection, Road, Light } from '../types/'
import TrafficLight from './TrafficLight.vue'
import { global } from '../stores/appState'
import { activateIntersection, deactivateIntersection, createIntersection, fetchIntersectionState } from '../services'

const { stopPolling, startPolling } = usePolling(fetchIntersectionState, 50)
global.stopPolling = stopPolling
global.startPolling = startPolling

onMounted(async () => {
  await createIntersection()
})

onUnmounted(() => {
  global.stopPolling()
})
</script>
<template>
  <div class='intersection'>
    <div class='north-light'>
      <TrafficLight :light="global.intersectionData.value?.roads[0]?.lights[0]" />
    </div>
    <div class='east-west-lights'>
      <TrafficLight :light="global.intersectionData.value?.roads[1]?.lights[0]" />
      <TrafficLight :light="global.intersectionData.value?.roads[1]?.lights[1]" />
    </div>
    <div class='south-light'>
      <TrafficLight :light="global.intersectionData.value?.roads[0]?.lights[1]" />
    </div>
  </div>
  <div class='footer'>
    <div v-if="global.intersectionData.value?.active">
      <button @click="deactivateIntersection">DeActivate Intersection</button>
    </div>
    <div v-else>
      <button @click="activateIntersection">Activate Intersection</button>
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
}
</style>
