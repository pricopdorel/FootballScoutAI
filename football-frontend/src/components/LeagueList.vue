<template>
  <div class="card">
    <h2>🏆 Ligi Disponibile</h2>
    <div v-for="league in leagues" :key="league.id" class="item">
      <strong>{{ league.name }}</strong> - {{ league.country }}
    </div>
  </div>
</template>

<script>
import api from '../services/api';
export default {
  data() { return { leagues: [] }; },
  mounted() { api.getLeagues().then(res => this.leagues = res.data); }
}
</script>