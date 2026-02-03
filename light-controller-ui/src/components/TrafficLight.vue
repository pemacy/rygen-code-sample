<script setup lang="ts">
import axios from 'axios'
import { ref, watch } from 'vue'
import type { Light } from '../types/'
import { global } from '../stores/'

const props = defineProps<{ light?: Light }>()
const activeLight = ref<string | null>(null)

watch(() => props.light, (newLight) => {
  if (newLight) {
    console.log(`Light ${newLight.lightId} - ${newLight.color.toLowerCase()}`)
    activeLight.value = newLight.color.toLowerCase()
  }
}, { immediate: true })

const handleActiveLightChange = () => {
  axios.put(`http://localhost:8080/intersections/${global.intersectionData.value.intersectionId}/lights`,
    { newColor: activeLight.value.toUpperCase(), lightId: props.light.lightId })
    .then(console.log)
    .catch(console.error)
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

      <p>Active light: {{ activeLight }}</p>
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
