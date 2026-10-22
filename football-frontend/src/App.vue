<template>
  <div id="app">
    <div class="bg-glow"></div>
    <header class="main-header">
      <div class="logo-area">
        <span class="ea-badge">FM26</span>
        <h1>FOOTBALL <span class="accent">MANAGER</span> 26</h1>
      </div>
      <div class="header-divider"></div>
    </header>

    <div class="dashboard">
      <div class="side-column">
        <div class="fifa-card selector-card compact-card shadow-fix">
          <div class="card-label">COMPETITION</div>
          <div class="carousel">
            <button @click="prevLeague" class="arrow-btn">❮</button>
            <div class="display-name">{{ currentLeagueName }}</div>
            <button @click="nextLeague" class="arrow-btn">❯</button>
          </div>
        </div>

        <div class="fifa-card selector-card compact-card shadow-fix club-container-wrapper">
          <div class="club-container">
            <div class="selection-side">
              <div class="card-label">CLUB SELECTION</div>
              <div class="carousel">
                <button @click="prevTeam" class="arrow-btn" :disabled="teams.length <= 1">❮</button>
                <div class="display-name">{{ currentTeamName }}</div>
                <button @click="nextTeam" class="arrow-btn" :disabled="teams.length <= 1">❯</button>
              </div>
            </div>
            
            <div class="logo-side">
              <div class="card-label">BADGE</div>
              <div class="team-logo-display square-ratio">
                 <img v-if="currentTeamId && !logoError" 
                      :src="`http://localhost:8083/uploads/${currentTeamId}.png?t=${new Date().getTime()}`" 
                      @error="handleLogoError"
                      class="badge-img" 
                      :key="currentTeamId" />
                 
                 <div v-else class="no-logo-text">NO LOGO</div>
              </div>
            </div>
          </div>

          <div class="team-stats-row" v-if="teams[teamIndex]">
            <div class="stat-item">
              <div class="stat-value">{{ teams[teamIndex].attack || 0 }}</div>
              <div class="stat-label">ATT</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ teams[teamIndex].midfield || 0 }}</div>
              <div class="stat-label">MID</div>
            </div>
            <div class="stat-divider"></div>
            <div class="stat-item">
              <div class="stat-value">{{ teams[teamIndex].defence || 0 }}</div>
              <div class="stat-label">DEF</div>
            </div>
            <div class="stat-divider rating-main-divider"></div>
            <div class="stat-item rating-overall">
              <div class="stat-value accent">{{ teams[teamIndex].rating || 0 }}</div>
              <div class="stat-label">OVR</div>
            </div>
          </div>
        </div>

        <div class="fifa-card players-card compact-card shadow-fix">
          <div class="card-header-flex">
            <h2>SQUAD MANAGEMENT</h2>
          </div>
          <div class="scroll-container">
            <PlayerManagement 
              ref="playerList" 
              :players="players" 
              @refresh="fetchPlayers" 
            />
          </div>
        </div>
      </div>

      <div class="main-column admin-section">
        <AddLeague @league-added="fetchInitialData" />
        <AddTeam :currentLeagueId="currentLeagueId" :currentLeagueCountry="currentLeagueCountry" @team-added="fetchTeams" />
        <AddPlayer :currentTeamId="currentTeamId" @player-added="fetchPlayers" />
      </div>
    </div>
  </div>
</template>

<script>
import api from './services/api';
import PlayerManagement from './components/PlayerManagement.vue';
import AddPlayer from './components/AddPlayer.vue';
import AddLeague from './components/AddLeague.vue';
import AddTeam from './components/AddTeam.vue';

export default {
  components: { PlayerManagement, AddPlayer, AddLeague, AddTeam },
  data() {
    return {
      leagues: [],
      teams: [],
      players: [],
      leagueIndex: 0,
      teamIndex: 0,
      logoError: false 
    };
  },
  computed: {
    currentLeagueName() { return this.leagues[this.leagueIndex]?.name?.toUpperCase() || "SELECT LEAGUE"; },
    currentLeagueId() { return this.leagues[this.leagueIndex]?.id || null; },
    currentLeagueCountry() { return this.leagues[this.leagueIndex]?.country || ""; },
    currentTeamName() { return this.teams[this.teamIndex]?.name?.toUpperCase() || "NO TEAM SELECTED"; },
    currentTeamId() { return this.teams[this.teamIndex]?.id || null; }
  },
  watch: {
    currentTeamId() {
      this.logoError = false;
    }
  },
  methods: {
    handleLogoError() {
      this.logoError = true;
    },
    async fetchInitialData() {
      try {
        const res = await api.getLeagues();
        this.leagues = res.data;
        if (this.leagues.length > 0) {
          if(this.leagueIndex >= this.leagues.length) this.leagueIndex = 0;
          await this.fetchTeams();
        }
      } catch (err) { console.error("Data fetch error:", err); }
    },
    async fetchTeams() {
      if (!this.currentLeagueId) { this.teams = []; this.players = []; return; }
      try {
        const res = await api.getTeamsByLeague(this.currentLeagueId);
        this.teams = res.data;
        this.teamIndex = 0;
        await this.fetchPlayers();
      } catch (err) { console.error(err); }
    },
    async fetchPlayers() {
      if (this.currentTeamId) {
        const res = await api.getPlayersByTeam(this.currentTeamId);
        this.players = res.data;
      } else { this.players = []; }
    },
    nextLeague() { this.leagueIndex = (this.leagueIndex + 1) % this.leagues.length; this.fetchTeams(); },
    prevLeague() { this.leagueIndex = (this.leagueIndex - 1 + this.leagues.length) % this.leagues.length; this.fetchTeams(); },
    nextTeam() { if (this.teams.length) { this.teamIndex = (this.teamIndex + 1) % this.teams.length; this.fetchPlayers(); } },
    prevTeam() { if (this.teams.length) { this.teamIndex = (this.teamIndex - 1 + this.teams.length) % this.teams.length; this.fetchPlayers(); } }
  },
  mounted() { this.fetchInitialData(); }
}
</script>

<style>
  @import url('https://fonts.googleapis.com/css2?family=Orbitron:wght@400;700;900&family=Rajdhani:wght@500;700&display=swap');

  .team-stats-row {
    display: flex;
    justify-content: space-around;
    align-items: center;
    margin-top: 15px;
    padding-top: 15px;
    border-top: 1px solid rgba(0, 255, 133, 0.1);
  }

  .stat-item {
    text-align: center;
    display: flex;
    flex-direction: column;
    align-items: center;
  }

  .stat-value {
    font-family: 'Orbitron', sans-serif;
    font-weight: 900;
    font-size: 18px;
    color: #fff;
  }

  .stat-label {
    font-family: 'Orbitron', sans-serif;
    font-size: 8px;
    color: #00ff85;
    letter-spacing: 1px;
    margin-top: 2px;
    opacity: 0.7;
  }

  .stat-divider {
    width: 1px;
    height: 20px;
    background: rgba(255, 255, 255, 0.1);
  }

  .rating-main-divider {
    height: 30px;
    background: rgba(0, 255, 133, 0.3);
  }

  .stat-item.rating-overall .stat-value {
    font-size: 22px;
    text-shadow: 0 0 10px rgba(0, 255, 133, 0.5);
  }

  body { margin: 0; background-color: #0a0e17; background-image: radial-gradient(circle at 50% 50%, #16213e 0%, #0a0e17 100%); font-family: 'Rajdhani', sans-serif; color: #ffffff; overflow-x: hidden; min-height: 100vh; }
  .bg-glow { position: fixed; top: -10%; right: -10%; width: 600px; height: 600px; background: rgba(0, 255, 133, 0.05); filter: blur(120px); z-index: -1; }
  #app { padding: 0 40px 60px 40px; max-width: 1800px; margin: 0 auto; }
  .main-header { display: flex; flex-direction: column; align-items: flex-start; padding: 80px 0 20px 0; margin-left: 100px; }
  .dashboard { display: grid; grid-template-columns: 500px 1fr; gap: 50px; align-items: start; margin-top: 60px; margin-left: 100px; }
  .club-container { display: flex; gap: 15px; align-items: center; }
  .selection-side { flex: 1; }
  .logo-side { width: 80px; }
  .square-ratio { width: 80px; height: 80px; background: rgba(0, 0, 0, 0.6); border: 1px solid rgba(0, 255, 133, 0.2); display: flex; align-items: center; justify-content: center; box-sizing: border-box; }
  .badge-img { width: 100%; height: 100%; object-fit: contain; padding: 5px; }
  .no-logo-text { color: #00ff85; font-family: 'Orbitron'; font-size: 10px; letter-spacing: 1px; text-align: center; opacity: 0.5; text-transform: uppercase; }
  .admin-section { display: flex; flex-direction: column; gap: 25px; }
  .ea-badge { background: #00ff85; color: #000; padding: 6px 14px; font-family: 'Orbitron', sans-serif; font-weight: 900; font-size: 19px; clip-path: polygon(12% 0%, 100% 0%, 88% 100%, 0% 100%); }
  .accent { color: #00ff85; }
  .header-divider { width: 100%; height: 1px; background: linear-gradient(90deg, #00ff85, transparent); margin-top: 25px; opacity: 0.3; }
  .fifa-card { background: rgba(255, 255, 255, 0.02); backdrop-filter: blur(15px); border: 1px solid rgba(255, 255, 255, 0.05); border-left: 4px solid #00ff85; padding: 20px; border-radius: 2px; box-shadow: 0 15px 35px rgba(0,0,0,0.5); }
  .card-label { font-family: 'Orbitron', sans-serif; font-size: 9px; color: #00ff85; margin-bottom: 12px; letter-spacing: 2px; }
  .carousel { display: flex; align-items: center; justify-content: space-between; background: rgba(0,0,0,0.6); padding: 12px; border: 1px solid rgba(0, 255, 133, 0.1); height: 54px; box-sizing: border-box; }
  .display-name { font-family: 'Orbitron', sans-serif; font-weight: 700; font-size: 14px; white-space: nowrap; overflow: hidden; text-overflow: ellipsis; max-width: 150px; }
  .arrow-btn { background: transparent; color: #00ff85; border: 1px solid #00ff85; width: 30px; height: 30px; cursor: pointer; flex-shrink: 0; }
  .arrow-btn:hover:not(:disabled) { background: #00ff85; color: #000; }
  h2 { font-family: 'Orbitron', sans-serif; font-size: 16px; color: #fff; letter-spacing: 1px; margin-bottom: 15px; }
  .scroll-container { max-height: 600px; overflow-y: auto; padding-right: 10px; }
  .scroll-container::-webkit-scrollbar { width: 4px; }
  .scroll-container::-webkit-scrollbar-thumb { background: #00ff85; }
  @media (max-width: 1200px) { .dashboard { grid-template-columns: 1fr; margin-left: 0; } .main-header { margin-left: 0; } }
</style>