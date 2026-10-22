<template>
  <div class="fifa-card compact-card shadow-fix">
    <div class="card-label">ADMINISTRATION</div>
    <h2 class="compact-title">🏆 REGISTER NEW LEAGUE</h2>
    
    <div class="form-grid">
      <input 
        v-model="name" 
        placeholder="LEAGUE NAME (ex: PREMIER LEAGUE)" 
        class="fifa-input-compact" 
      />
      <input 
        v-model="country" 
        placeholder="COUNTRY" 
        class="fifa-input-compact" 
      />
    </div>

    <button class="btn-save-compact" @click="save">
      CONFIRM LEAGUE
    </button>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  data() {
    return {
      name: '',
      country: ''
    };
  },
  methods: {
    save() {
      // 1. Validare simplă
      if (!this.name.trim()) {
        alert("⚠️ LEAGUE NAME REQUIRED!");
        return;
      }

      /* STRATEGIE AUTO-INCREMENT: 
         Construim obiectul fără câmpul ID. 
         Baza de date va detecta lipsa ID-ului și va pune automat următorul ID liber.
      */
      const leagueToSave = {
        name: this.name.trim(),
        country: this.country.trim()
      };

      console.log("Sending data for Auto-Increment:", leagueToSave);

      // 2. Apel API folosind metoda POST
      api.addLeague(leagueToSave)
        .then(() => {
          alert("✅ LEAGUE CREATED SUCCESSFULLY!");
          // Resetăm câmpurile
          this.name = '';
          this.country = '';
          // Anunțăm părintele (App.vue) să reîncarce lista
          this.$emit('league-added');
        })
        .catch(err => {
          console.error("Save Error:", err);
          /* Dacă dă eroare aici, verifică în IntelliJ dacă entitatea League 
             are @GeneratedValue(strategy = GenerationType.IDENTITY) pe ID.
          */
          alert("❌ DATABASE ERROR: Could not assign league ID automatically.");
        });
    }
  }
}
</script>

<style scoped>
.compact-card { max-width: 440px; padding: 18px !important; box-sizing: border-box; }
.shadow-fix { border-left: 3px solid #00ff85 !important; }
.compact-title { font-size: 15px !important; margin-bottom: 12px !important; color: #fff; font-family: 'Orbitron', sans-serif; }
.form-grid { display: flex; flex-direction: column; gap: 8px; margin-bottom: 12px; }

.fifa-input-compact {
  background: rgba(0, 0, 0, 0.5);
  border: 1px solid rgba(255, 255, 255, 0.15);
  color: #00ff85;
  font-family: 'Rajdhani', sans-serif;
  font-weight: 600;
  padding: 10px 12px;
  font-size: 14px;
  width: 100%;
  box-sizing: border-box;
  outline: none;
  text-transform: uppercase;
}

.btn-save-compact {
  width: 100%;
  background: #00ff85;
  color: #000;
  border: none;
  font-family: 'Orbitron', sans-serif;
  font-weight: 900;
  cursor: pointer;
  padding: 12px;
  font-size: 12px;
  clip-path: polygon(5% 0%, 100% 0%, 95% 100%, 0% 100%);
  transition: 0.2s;
}
.btn-save-compact:hover { background: #fff; }
</style>