<script setup lang="ts">
import { ref, onMounted, onUnmounted, computed } from 'vue'
import { usePolling } from '../composables/usePolling'
import axios from 'axios'
import type { Intersection, Road, Light } from '../types/'
import TrafficLight from './TrafficLight.vue'
import { global } from '../stores/appState'
import { isIntersectionActive } from '../services'
import { activateIntersection, deactivateIntersection, createIntersection } from '../services'

onMounted(async () => {
  await createIntersection()
  { startPolling, stopPolling } = usePolling(fetchIntersectionState, 5000)
})

onUnmounted(() => {
  stopPolling()
})
</script>
<template>
  <div class='intersection'>
    <div class='north-light'>
      <TrafficLight />
    </div>
    <div class='east-west-lights'>
      <TrafficLight />
      <TrafficLight />
    </div>
    <div class='south-light'>
      <TrafficLight />
    </div>
  </div>
  <div class='footer'>
    <div v-if="isIntersectionActive">
      <button @click="activateIntersection">Activate Intersection</button>
    </div>
    <div v-else>
      <button @click="deactivateIntersection">DeActivate Intersection</button>
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
