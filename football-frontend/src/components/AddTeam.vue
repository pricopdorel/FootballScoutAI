<template>
  <div class="fifa-card scout-terminal wide-card shadow-fix">
    <div class="card-label">ULTIMATE AI TEAM SCOUT</div>
    
    <div class="terminal-header">
      <h2 class="terminal-title">🛡️ SCOUT & REGISTER FULL SQUAD</h2>
      <div class="status-badge" :class="{ 'status-active': loading }">
        {{ loading ? 'AI PROCESSING...' : 'SYSTEM READY' }}
      </div>
    </div>
    
    <div class="form-grid">
      <div class="assigned-info" v-if="currentLeagueId">
        CONNECTED TO LEAGUE: <span class="highlight-id">ID {{ currentLeagueId }}</span> 
      </div>

      <div class="ai-search-container">
        <div class="search-controls">
          <label class="input-hint">ENTER OFFICIAL TEAM NAME</label>
          <input 
            v-model="searchQuery" 
            placeholder="EX: REAL MADRID, AC MILAN, FCSB..." 
            class="fifa-input scout-input"
            :disabled="loading"
            @keyup.enter="runFullAIScout"
          />
          <button @click="runFullAIScout" class="btn-scout" :disabled="loading || !searchQuery">
            <span v-if="!loading">🚀 INITIATE FULL SQUAD SCOUT</span>
            <span v-else class="loading-text">📡 ANALYZING VIA OLLAMA...</span>
          </button>
        </div>
      </div>

      <div class="stats-container" v-if="form.name">
        <div class="preview-tag">AI RETRIEVED DATA</div>
        <div class="name-display">
             <label class="input-hint">TEAM IDENTITY</label>
             <input v-model="form.name" class="fifa-input compact-input" readonly />
        </div>
        
        <div class="stats-row">
          <div class="stat-group"><label>ATT</label><input v-model.number="form.attack" type="number" class="fifa-input small-field" readonly /></div>
          <div class="stat-group"><label>MID</label><input v-model.number="form.midfield" type="number" class="fifa-input small-field" readonly /></div>
          <div class="stat-group"><label>DEF</label><input v-model.number="form.defence" type="number" class="fifa-input small-field" readonly /></div>
          <div class="stat-group"><label class="ovr-label">OVR</label><input v-model.number="form.rating" type="number" class="fifa-input small-field rating-highlight" readonly /></div>
        </div>
      </div>
    </div>
    
    <div v-if="loading" class="scanning-overlay"></div>
    <div v-if="loading" class="loader-bar"></div>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  props: ['currentLeagueId', 'currentLeagueCountry'], 
  data() {
    return {
      searchQuery: '',
      loading: false,
      form: {
        name: '',
        attack: 75,
        midfield: 75,
        defence: 75,
        rating: 75
      }
    };
  },
  methods: {
    async runFullAIScout() {
      if (!this.searchQuery.trim() || !this.currentLeagueId) {
        alert("⚠️ NAME AND LEAGUE SELECTION REQUIRED!");
        return;
      }
      
      this.loading = true;
      
      try {
        const response = await api.scoutFullTeam(this.searchQuery, this.currentLeagueId);
        const savedTeam = response.data;
        this.form.name = savedTeam.name;
        this.form.attack = savedTeam.attack;
        this.form.midfield = savedTeam.midfield;
        this.form.defence = savedTeam.defence;
        this.form.rating = savedTeam.rating;

        alert(`✅ SQUAD IMPORTED: ${savedTeam.name} and 11 players registered!`);
        this.$emit('team-added');
        this.searchQuery = '';
      } catch (err) {
        console.error("Full Scout Error:", err);
        alert("❌ AI SCOUTING FAILED. IS OLLAMA RUNNING?");
      } finally {
        this.loading = false;
      }
    },
    resetForm() {
      this.form = { name: '', attack: 75, midfield: 75, defence: 75, rating: 75 };
      this.searchQuery = '';
    }
  }
}
</script>

<style scoped>
.wide-card {
  /* MODIFICARE: Lățime crescută pentru un aspect mai impunător */
  max-width: 1000px; 
  margin: 20px auto;
  padding: 40px !important; /* Spațiere interioară mai generoasă */
  background: linear-gradient(135deg, rgba(10, 10, 10, 0.98) 0%, rgba(20, 20, 20, 0.95) 100%);
  border: 1px solid rgba(0, 255, 133, 0.25);
  position: relative;
  overflow: hidden;
  box-shadow: 0 20px 50px rgba(0, 0, 0, 0.8);
}

.shadow-fix {
  border-left: 5px solid #00ff85;
  box-shadow: -10px 0 30px rgba(0, 255, 133, 0.1);
}

.terminal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 20px;
}

.terminal-title {
  font-size: 26px !important; /* Titlu mai mare */
  font-family: 'Orbitron', sans-serif;
  color: #fff;
  letter-spacing: 3px;
  text-transform: uppercase;
}

.status-badge {
  font-family: 'Orbitron', sans-serif;
  font-size: 11px;
  padding: 6px 16px;
  background: rgba(0, 255, 133, 0.05);
  color: #888;
  border-radius: 4px;
  border: 1px solid rgba(255, 255, 255, 0.1);
  letter-spacing: 1px;
}

.status-active {
  color: #00ff85;
  border-color: #00ff85;
  box-shadow: 0 0 15px rgba(0, 255, 133, 0.2);
  background: rgba(0, 255, 133, 0.1);
}

.ai-search-container {
  display: flex;
  background: rgba(0, 255, 133, 0.03);
  padding: 30px; /* Mai mult spațiu în zona de search */
  border-radius: 4px;
  border: 1px dashed rgba(0, 255, 133, 0.3);
  margin-bottom: 30px;
}

.search-controls { flex: 1; display: flex; flex-direction: column; gap: 15px; }

.input-hint {
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  color: #00ff85;
  letter-spacing: 2px;
  margin-bottom: 5px;
}

.scout-input {
  font-size: 20px !important; /* Input text mai mare */
  padding: 15px 20px !important;
  color: #00ff85 !important;
  background: rgba(0, 0, 0, 0.8);
  border: 1px solid rgba(0, 255, 133, 0.4);
  letter-spacing: 1px;
}

.btn-scout {
  background: #00ff85;
  color: #000;
  border: none;
  font-family: 'Orbitron', sans-serif;
  font-weight: 900;
  font-size: 14px;
  padding: 18px;
  cursor: pointer;
  clip-path: polygon(1% 0%, 100% 0%, 99% 100%, 0% 100%);
  transition: 0.4s cubic-bezier(0.175, 0.885, 0.32, 1.275);
}

.btn-scout:hover:not(:disabled) {
  background: #fff;
  box-shadow: 0 0 30px #00ff85;
  transform: translateY(-2px);
}

.stats-container {
  background: rgba(0, 0, 0, 0.6);
  padding: 30px;
  border: 1px solid rgba(255, 255, 255, 0.05);
}

.stats-row {
  display: flex;
  justify-content: space-between;
  margin-top: 25px;
}

.stat-group {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 10px;
}

.stat-group label {
  font-size: 12px;
  font-family: 'Orbitron';
  color: #00ff85;
  letter-spacing: 1px;
}

.ovr-label {
  color: #fff !important;
  text-shadow: 0 0 10px #00ff85;
}

.fifa-input {
  background: rgba(0, 0, 0, 0.7);
  border: 1px solid rgba(255, 255, 255, 0.1);
  color: #00ff85;
  font-family: 'Rajdhani', sans-serif;
  text-transform: uppercase;
  outline: none;
}

.compact-input {
  font-size: 18px !important;
  padding: 12px !important;
}

.small-field {
  width: 80px !important; /* Câmpuri de stats mai late */
  text-align: center;
  font-size: 20px;
  font-weight: 700;
  border-color: rgba(0, 255, 133, 0.3);
}

.rating-highlight {
  border-color: #00ff85;
  box-shadow: 0 0 15px rgba(0, 255, 133, 0.3);
  color: #fff;
}

.loader-bar {
  height: 6px; /* Bară de loading mai groasă */
  background: #00ff85;
  width: 100%;
  position: absolute;
  bottom: 0;
  left: 0;
  box-shadow: 0 0 20px #00ff85;
  animation: scan 3s infinite linear;
}

@keyframes scan {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.scanning-overlay {
  position: absolute;
  top: 0; left: 0; width: 100%; height: 100%;
  background: linear-gradient(rgba(0, 255, 133, 0.04) 50%, transparent 50%);
  background-size: 100% 6px;
  pointer-events: none;
}

.loading-text { animation: blink 1s infinite; }
@keyframes blink { 50% { opacity: 0.4; } }

.highlight-id { color: #fff; font-weight: bold; font-size: 1.1em; }
</style>