<script setup lang="ts">
import axios from 'axios'
import { ref, watch } from 'vue'
import type { Light } from '../types/'
import { global } from '../stores/'

const props = defineProps<{ light?: Light }>()
const activeLight = ref<string | null>(null)

watch(() => props.light, (newLight) => {
  if (newLight) {
    activeLight.value = newLight.color.toLowerCase()
  }
}, { immediate: true })

const handleActiveLightChange = () => {
  axios.put(`http://localhost:8080/intersections/${global.intersectionData.value.intersectionId}/lights`,
    { newColor: activeLight.value.toUpperCase(), lightId: props.light.lightId })
    .then(console.log)
    .catch(console.error)
}
const handleLightActivateChange = () => {
  if (props.light.active) {
    axios.post(`${global.API_URL}/${global.intersectionData.value.intersectionId}/lights/${props.light.lightId}/deactivate`)
  } else {
    axios.post(`${global.API_URL}/${global.intersectionData.value.intersectionId}/lights/${props.light.lightId}/activate`)
  }
}
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
</style>
