import axios from 'axios';

const apiClient = axios.create({
  // Verifică portul (8083) să coincidă cu cel din application.properties din IntelliJ
  baseURL: 'http://localhost:8083/api',
  headers: { 'Content-Type': 'application/json' }
});

export default {
  // --- LEAGUES ---
  getLeagues() {
    return apiClient.get('/league/all');
  },

  addLeague(league) {
    return apiClient.post('/league/add', league);
  },

  // --- TEAMS ---
  getTeamsByLeague(leagueId) {
    return apiClient.get(`/team/league/${leagueId}`);
  },

  addTeam(team) {
    return apiClient.post('/team/add', team);
  },

  // --- PLAYERS ---
  getPlayers() {
    return apiClient.get('/player/all');
  },

  getPlayersByTeam(teamId) {
    return apiClient.get(`/player/team/${teamId}`);
  },

  getPlayerById(id) {
    return apiClient.get(`/player/${id}`);
  },

  addPlayer(player) {
    return apiClient.post('/player/add', player);
  },

  updatePlayer(id, player) {
    return apiClient.put(`/player/update/${id}`, player);
  },

  deletePlayer(id) {
    return apiClient.delete(`/player/delete/${id}`);
  },

  // Filtrare dupa tara
  filterPlayers(country) {
    return apiClient.get(`/player/filter/${country}`);
  },

  // --- AI SCOUTING (OLLAMA) ---

  /**
   * Generează o echipă completă (11 jucători + statistici) folosind AI.
   * Trimite 'name' și 'leagueId' ca Query Parameters.
   */
  scoutFullTeam(name, leagueId) {
    return apiClient.post(`/ai/scout?name=${name}&leagueId=${leagueId}`);
  },

  /**
   * Adaugă un singur jucător cu statistici generate de AI într-o echipă existentă.
   * Trimite 'playerName' și 'teamId' ca Query Parameters.
   */
  scoutSinglePlayer(playerName, teamId) {
    return apiClient.post(`/ai/scout-player?playerName=${playerName}&teamId=${teamId}`);
  }
};