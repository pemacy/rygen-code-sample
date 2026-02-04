<script setup lang="ts">
import axios from 'axios'
import { ref, watch, computed } from 'vue'
import type { Light } from '../types/'
import { global } from '../stores/'

const props = defineProps<{ light?: Light }>()
const activeLight = ref<string | null>(null)
const showSettings = ref(false)

watch(() => props.light, (newLight) => {
  if (newLight) {
    activeLight.value = newLight.color.toLowerCase()
  }
}, { immediate: true })

const handleActiveLightChange = () => {
  axios.put(`http://localhost:8080/intersections/${global.intersectionData?.value?.intersectionId}/lights`,
    { newColor: activeLight.value?.toUpperCase(), lightId: props.light?.lightId })
    .then(console.log)
    .catch(console.error)
}
const handleLightActivateChange = () => {
  if (props.light?.active) {
    axios.post(`${global.API_URL}/${global.intersectionData?.value?.intersectionId}/lights/${props.light?.lightId}/deactivate`)
  } else {
    axios.post(`${global.API_URL}/${global.intersectionData?.value?.intersectionId}/lights/${props.light?.lightId}/activate`)
  }
}

const formatDuration = (ms: number | undefined): string => {
  if (ms === undefined) return 'N/A'
  return `${(ms / 1000).toFixed(1)}s`
}

const timeRemaining = computed(() => {
  if (!props.light) return 0
  return Math.max(0, props.light.durationForCurrentColor - props.light.elapsedTimeMillis)
})
</script>

<template>
  <main>
    <div class="light-controller">
      <div class="light">
        <label>
          <input type="radio" value="red" class="red" v-model="activeLight" :name="`light-${light?.lightId}`"
            @change="handleActiveLightChange" /> Red
        </label>
        <label>
          <input type="radio" value="yellow" class="yellow" v-model="activeLight" :name="`light-${light?.lightId}`"
            @change="handleActiveLightChange" /> Yellow
        </label>
        <label>
          <input type="radio" value="green" class="green" v-model="activeLight" :name="`light-${light?.lightId}`"
            @change="handleActiveLightChange" /> Green
        </label>
      </div>

      <p>{{ light?.active ? "Activated" : "De-Activated" }}</p>
      <div v-if="light?.active">
        <button @click="handleLightActivateChange">Deactivate</button>
      </div>
      <div v-else>
        <button @click="handleLightActivateChange">Activate</button>
      </div>

      <button class="settings-toggle" @click="showSettings = !showSettings">
        {{ showSettings ? 'Hide Settings' : 'View Settings' }}
      </button>

      <div v-if="showSettings" class="settings-panel">
        <div class="setting-row">
          <span class="label">Light ID:</span>
          <span>{{ light?.lightId }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Initial Color:</span>
          <span>{{ light?.initialColor ?? 'N/A' }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Current Color:</span>
          <span>{{ light?.color }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Active:</span>
          <span>{{ light?.active ? 'Yes' : 'No' }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Green Duration:</span>
          <span>{{ formatDuration(light?.greenDurationMillis) }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Yellow Duration:</span>
          <span>{{ formatDuration(light?.yellowDurationMillis) }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Red Duration:</span>
          <span>{{ formatDuration(light?.redDurationMillis) }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Time Elapsed:</span>
          <span>{{ formatDuration(light?.elapsedTimeMillis) }}</span>
        </div>
        <div class="setting-row">
          <span class="label">Time Remaining:</span>
          <span>{{ formatDuration(timeRemaining) }}</span>
        </div>
      </div>
    </div>
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    margin: calc(var(--section-gap) / 4);
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}

.light-controller {
  display: grid;
  place-items: center;
  gap: 1rem;

  .light {
    display: grid;
    gap: .5rem;
  }

}

input[type='radio'].red {
  accent-color: #cc3232;
}

input[type='radio'].yellow {
  accent-color: #e7b416;
}

input[type='radio'].green {
  accent-color: #2dc937;
}

.settings-toggle {
  margin-top: 0.5rem;
  padding: 0.25rem 0.5rem;
  font-size: 0.85rem;
  background-color: #34495e;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
}

.settings-toggle:hover {
  background-color: #5d6d7e;
}

.settings-panel {
  margin-top: 0.75rem;
  padding: 0.75rem;
  background-color: #f8f9fa;
  border: 1px solid #dee2e6;
  border-radius: 6px;
  text-align: left;
  font-size: 0.85rem;
  color: black;
}

.setting-row {
  display: flex;
  justify-content: space-between;
  padding: 0.25rem 0;
  border-bottom: 1px solid #e9ecef;
}

.setting-row:last-child {
  border-bottom: none;
}

.setting-row .label {
  font-weight: 600;
  color: #495057;
}
</style>
