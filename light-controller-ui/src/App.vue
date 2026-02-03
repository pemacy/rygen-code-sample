<script setup lang="ts">
import IntersectionView from './components/Intersection.vue'
</script>

<template>
  <header>
    <div class="wrapper">
      <h1>Intersection Light Controller</h1>
    </div>
  </header>

  <main>
    <IntersectionView />
  </main>
</template>

<style scoped>
header {
  line-height: 1.5;
  padding: 1rem 0;
  border-bottom: 1px solid #e0e0e0;
}

.wrapper {
  max-width: 1200px;
  margin: 0 auto;
  padding: 0 2rem;
}

h1 {
  font-size: 2rem;
  color: #333;
  margin: 0;
}

main {
  padding: 2rem 0;
}
</style>
