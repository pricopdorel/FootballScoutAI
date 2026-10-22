<template>
  <div class="card">
    <h2>🔍 Filtrare & AI Scout</h2>
    <input v-model="searchCountry" @input="filter" placeholder="Caută după țară (ex: Romania)..." />
    <div v-for="p in filtered" :key="p.id" class="item">
      <span>{{ p.name }} ({{ p.rating }})</span>
      <button class="btn-ai" @click="getAiReport(p)">Analiză AI</button>
    </div>
  </div>
</template>

<script>
import api from '../services/api';
export default {
  data() { return { searchCountry: '', filtered: [] }; },
  methods: {
    filter() {
      if(this.searchCountry.length > 2) {
        api.filterPlayers(this.searchCountry).then(res => this.filtered = res.data);
      }
    },
    getAiReport(player) {
      // Integrare AI Simpla conform cerintei
      const report = `AI Analysis: Jucătorul ${player.name} are un rating de ${player.rating}. 
                      Recomandare: ${player.rating > 85 ? 'Transfer Prioritar' : 'Monitorizare Scout'}.`;
      alert(report);
    }
  }
}
</script>