<template>
  <div class="table-wrapper">
    <table class="fifa-table">
      <thead>
        <tr>
          <th style="width: 25%">NAME</th>
          <th style="width: 15%">COUNTRY</th>
          <th style="width: 10%">AGE</th>
          <th style="width: 10%">POS</th>
          <th style="width: 10%">RAT</th>
          <th class="text-right" style="width: 30%">ACTIONS</th>
        </tr>
      </thead>
      <tbody>
        <tr v-for="p in players" :key="p.id" class="player-row">
          <td class="player-name">{{ p.name }}</td>
          <td class="player-nat">{{ p.country }}</td>
          <td>{{ p.age }}</td>
          <td><span class="pos-tag">{{ p.position }}</span></td>
          <td><span class="rating-badge">{{ p.rating }}</span></td>
          <td class="text-right">
            <div class="action-group">
              <button class="btn-action btn-update" @click="updatePlayerRating(p)">UPDATE</button>
              <button class="btn-action btn-delete" @click="remove(p.id)">DELETE</button>
            </div>
          </td>
        </tr>
      </tbody>
    </table>
  </div>
</template>

<script>
import api from '../services/api';

export default {
  props: ['players'],
  methods: {
    async updatePlayerRating(player) {
      const newRating = prompt(`MODIFICARE RATING PENTRU ${player.name}:`, player.rating);
      
      if (newRating !== null && newRating !== "") {
        const ratingNum = parseInt(newRating);
        
        if (isNaN(ratingNum) || ratingNum < 1 || ratingNum > 99) {
          alert("⚠️ INTRODUCEȚI UN RATING VALID (1-99)!");
          return;
        }

        try {
          const updatedData = { ...player, rating: ratingNum };
          await api.updatePlayer(player.id, updatedData);
          alert("✅ RATING ACTUALIZAT CU SUCCES!");
          this.$emit('refresh');
        } catch (err) {
          console.error("Update error:", err);
          alert("❌ EROARE LA ACTUALIZARE: Verificați conexiunea cu serverul.");
        }
      }
    },
    async remove(id) {
      if(confirm("TERMINATE CONTRACT? This action is irreversible.")) {
        try {
          await api.deletePlayer(id);
          this.$emit('refresh');
        } catch (err) { 
          alert("SYSTEM ERROR: Could not delete player."); 
        }
      }
    }
  }
}
</script>

<style scoped>
/* --- CONTAINER SI TABEL --- */
.table-wrapper {
  width: 100%;
  max-width: 1200px; /* MODIFICAT: de la 1000px la 1200px pentru a fi mai larg */
  margin: 0 auto;
  overflow-x: hidden; 
  padding: 5px;
}

.fifa-table { 
  width: 100%; 
  border-collapse: separate; 
  border-spacing: 0 6px; 
  font-family: 'Rajdhani', sans-serif; 
  table-layout: fixed; 
}

/* --- HEADER --- */
th { 
  text-align: left; 
  color: #00ff85; 
  font-family: 'Orbitron', sans-serif; 
  font-size: 9px; 
  letter-spacing: 1px; 
  padding: 8px 10px;
  text-transform: uppercase;
}

/* --- RANDURI SI CELULE --- */
.player-row td { 
  background: rgba(255, 255, 255, 0.05); 
  padding: 8px 10px; 
  color: #fff; 
  font-weight: 600; 
  text-transform: uppercase; 
  font-size: 12px; 
  border-top: 1px solid rgba(255, 255, 255, 0.05);
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.player-row td:first-child { border-left: 3px solid #00ff85; border-radius: 4px 0 0 4px; }
.player-row td:last-child { border-radius: 0 4px 4px 0; }

.player-name { font-weight: 700; color: #fff; letter-spacing: 0.5px; }
.player-nat { color: #888; font-size: 10px; }

/* --- BADGES --- */
.pos-tag {
  background: rgba(0, 255, 133, 0.1);
  color: #00ff85;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 9px;
}

.rating-badge { 
  color: #00ff85; 
  font-weight: 900; 
  font-size: 14px;
  text-shadow: 0 0 10px rgba(0, 255, 133, 0.3);
}

/* --- ACTIUNI --- */
.action-group {
  display: flex;
  gap: 5px;
  justify-content: flex-end;
}

.btn-action {
  font-family: 'Orbitron', sans-serif; 
  font-size: 8px; 
  padding: 4px 8px; 
  cursor: pointer;
  background: transparent;
  transition: all 0.2s;
  text-transform: uppercase;
  font-weight: 900;
}

.btn-update {
  color: #00d1ff; 
  border: 1px solid rgba(0, 209, 255, 0.5);
}
.btn-update:hover {
  background: #00d1ff;
  color: #000;
  box-shadow: 0 0 8px #00d1ff;
}

.btn-delete {
  color: #ff4d4d; 
  border: 1px solid rgba(255, 77, 77, 0.5);
}
.btn-delete:hover { 
  background: #ff4d4d; 
  color: #fff;
  box-shadow: 0 0 8px #ff4d4d;
}

.text-right { text-align: right; }

/* Scrollbar pentru containerul parinte */
.table-wrapper::-webkit-scrollbar { width: 4px; }
.table-wrapper::-webkit-scrollbar-thumb { background: #00ff85; border-radius: 10px; }
</style>