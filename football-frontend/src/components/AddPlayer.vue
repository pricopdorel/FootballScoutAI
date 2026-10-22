<template>
  <div class="fifa-card scout-terminal wide-card shadow-fix">
    <div class="card-label">ULTIMATE AI SCOUTING SYSTEM</div>
    
    <div class="terminal-header">
      <h2 class="terminal-title">🤖 AI PLAYER SIGNING</h2>
      <div class="status-badge" :class="{ 'status-active': isLoading }">
        {{ isLoading ? 'SCANNING...' : 'SYSTEM READY' }}
      </div>
    </div>
    
    <div class="form-container">
      <div class="input-wrapper">
        <label class="input-hint">TARGET PLAYER NAME</label>
        <input 
          v-model="playerName" 
          placeholder="ENTER FULL NAME (e.g. Jude Bellingham, Kylian Mbappe...)" 
          class="fifa-input scout-input"
          :disabled="isLoading"
          @keyup.enter="scoutAndSign"
        />
      </div>
    </div>

    <button 
      class="btn-scout-action" 
      @click="scoutAndSign"
      :disabled="isLoading || !playerName"
    >
      <div class="btn-content">
        <span v-if="!isLoading">INITIATE AI TRANSFER PROTOCOL</span>
        <span v-else class="loading-text">📡 CONNECTING TO OLLAMA DATABASE...</span>
      </div>
    </button>

    <div v-if="isLoading" class="scanning-overlay"></div>
    <div v-if="isLoading" class="loader-bar"></div>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  props: ['currentTeamId'],
  data() { 
    return { 
      playerName: '',
      isLoading: false
    }; 
  },
  methods: {
    async scoutAndSign() {
      if (!this.playerName) {
        alert("⚠️ PLEASE ENTER A PLAYER NAME!");
        return;
      }
      if (!this.currentTeamId) {
        alert("⚠️ PLEASE SELECT A CLUB FIRST!");
        return;
      }

      this.isLoading = true;

      try {
        const response = await api.scoutSinglePlayer(this.playerName, this.currentTeamId);
        alert(`✅ SCOUTING COMPLETE: ${response.data.name} (RAT: ${response.data.rating}) signed!`);
        this.playerName = ''; 
        this.$emit('player-added'); 
      } catch (err) {
        console.error("AI Scouting Error:", err);
        alert("❌ AI ERROR: Could not retrieve player data. Is Ollama running?");
      } finally {
        this.isLoading = false;
      }
    }
  }
}
</script>

<style scoped>
/* --- Layout principal --- */
.wide-card {
  max-width: 800px;
  margin: 20px auto;
  padding: 30px !important;
  background: linear-gradient(135deg, rgba(10, 10, 10, 0.95) 0%, rgba(25, 25, 25, 0.95) 100%);
  border: 1px solid rgba(0, 255, 133, 0.2);
  position: relative;
  overflow: hidden;
}

.shadow-fix {
  border-left: 4px solid #00ff85;
  box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

/* --- Header Terminal --- */
.terminal-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 25px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  padding-bottom: 15px;
}

.terminal-title {
  font-size: 22px !important;
  font-family: 'Orbitron', sans-serif;
  color: #fff;
  letter-spacing: 2px;
  margin: 0;
}

.status-badge {
  font-family: 'Orbitron', sans-serif;
  font-size: 10px;
  padding: 4px 12px;
  background: rgba(255, 255, 255, 0.05);
  color: #888;
  border-radius: 20px;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.status-active {
  color: #00ff85;
  border-color: #00ff85;
  box-shadow: 0 0 10px rgba(0, 255, 133, 0.2);
}

/* --- Formular --- */
.form-container {
  margin-bottom: 25px;
}

.input-wrapper {
  display: flex;
  flex-direction: column;
  gap: 10px;
}

.input-hint {
  font-family: 'Orbitron', sans-serif;
  font-size: 9px;
  color: #00ff85;
  letter-spacing: 1px;
}

.scout-input {
  font-size: 18px !important;
  padding: 15px 20px !important;
  background: rgba(0, 0, 0, 0.8);
  border: 1px solid rgba(0, 255, 133, 0.3);
  transition: all 0.3s;
  color: #00ff85 !important; /* MODIFICARE: Culoarea textului tastat este acum Verde Neon */
}

.scout-input:focus {
  border-color: #00ff85;
  box-shadow: 0 0 15px rgba(0, 255, 133, 0.1);
}

/* --- Buton Acțiune --- */
.btn-scout-action {
  width: 100%;
  height: 60px;
  background: #00ff85;
  color: #000;
  border: none;
  font-family: 'Orbitron', sans-serif;
  font-weight: 900;
  font-size: 14px;
  cursor: pointer;
  clip-path: polygon(2% 0%, 100% 0%, 98% 100%, 0% 100%);
  transition: all 0.3s;
}

.btn-scout-action:hover:not(:disabled) {
  background: #fff;
  transform: translateY(-2px);
  box-shadow: 0 5px 20px rgba(0, 255, 133, 0.4);
}

.btn-scout-action:disabled {
  background: #222;
  color: #555;
  cursor: not-allowed;
}

/* --- Efecte Vizuale --- */
.loader-bar {
  position: absolute;
  bottom: 0;
  left: 0;
  height: 4px;
  background: #00ff85;
  width: 100%;
  box-shadow: 0 0 15px #00ff85;
  animation: scanning 2.5s infinite linear;
}

@keyframes scanning {
  0% { transform: translateX(-100%); }
  100% { transform: translateX(100%); }
}

.scanning-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: linear-gradient(rgba(0, 255, 133, 0.05) 50%, transparent 50%);
  background-size: 100% 4px;
  pointer-events: none;
  z-index: 1;
}

.loading-text {
  animation: blink 0.8s infinite;
}

@keyframes blink {
  50% { opacity: 0.3; }
}
</style>