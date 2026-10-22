package ProiectSCD_Dorel.service;

import ProiectSCD_Dorel.model.*;
import ProiectSCD_Dorel.repo.*;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.util.*;

@Service
public class AIScoutService {

    @Autowired
    private teamRepo teamRepository;
    @Autowired
    private playerRepo playerRepository;
    @Autowired
    private leagueRepo leagueRepository;

    private final String OLLAMA_URL = "http://localhost:11434/api/generate";

    @Transactional
    public Team scoutFullTeamFromSoFifa(String teamName, int localLeagueId) throws Exception {
        System.out.println("🤖 OLLAMA SCOUT: Analizez " + teamName);

        League league = leagueRepository.findById(localLeagueId)
                .orElseThrow(() -> new RuntimeException("Liga nu a fost găsită!"));

        String promptText = "Act as a football database. Provide data for '" + teamName + "'. " +
                "(requirements: 11 players must be on the team with their actual FIFA ratings + stats, and also the team actual FIFA rating + stats) Return ONLY a STRICT JSON: " +
                "{" +
                "  \"official_name\": \"string\"," +
                "  \"att\": 0-100, \"mid\": 0-100, \"def\": 0-100, \"ovr\": 0-100," +
                "  \"players\": [" +
                "    { \"name\": \"string\", \"pos\": \"string\", \"rat\": \"string\", \"age\": 0, \"nat\": \"string\" }" +
                "  ]" +
                "}";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3");
        requestBody.put("prompt", promptText);
        requestBody.put("stream", false);
        requestBody.put("format", "json");

        RestTemplate restTemplate = new RestTemplate();

        try {
            Map<String, Object> response = restTemplate.postForObject(OLLAMA_URL, requestBody, Map.class);
            String aiResponse = (String) response.get("response");

            ObjectMapper mapper = new ObjectMapper();
            JsonNode data = mapper.readTree(aiResponse);

            Team team = new Team();
            team.setName(data.path("official_name").asText(teamName));
            team.setAttack(data.path("att").asInt());
            team.setMidfield(data.path("mid").asInt());
            team.setDefence(data.path("def").asInt());
            team.setRating(data.path("ovr").asInt());

            // FIX 1: Aici punem TARA din liga
            team.setCountry(league.getCountry());
            team.setLeague(league);

            Team savedTeam = teamRepository.save(team);

            JsonNode players = data.path("players");
            if (players.isArray()) {
                for (JsonNode pNode : players) {
                    Player p = new Player();
                    p.setName(pNode.path("name").asText());
                    p.setPosition(pNode.path("pos").asText());
                    p.setRating(pNode.path("rat").asText());
                    p.setAge(pNode.path("age").asInt());

                    // FIX 3: Tara jucătorului (nat de la AI sau țara echipei)
                    String playerNat = pNode.path("nat").asText();
                    p.setCountry(playerNat.isEmpty() ? savedTeam.getCountry() : playerNat);

                    p.setTeam(savedTeam);
                    playerRepository.save(p);
                }
            }

            return savedTeam;
        } catch (Exception e) {
            throw new RuntimeException("Eroare: " + e.getMessage());
        }
    }

    /**
     * Metoda separata pentru adăugarea unui singur jucător folosind AI.
     * Aceasta preia statusurile curente bazate pe numele jucătorului.
     */
    @Transactional
    public Player addPlayerWithAI(String playerName, int teamId) throws Exception {
        System.out.println("🤖 AI SCOUT: Căutare statistici pentru jucătorul " + playerName);

        Team team = teamRepository.findById(teamId)
                .orElseThrow(() -> new RuntimeException("Echipa nu a fost găsită!"));

        String promptText = "Act as a football scout. Provide current real-life stats for the player '" + playerName + "'. " +
                "Return ONLY a STRICT JSON: " +
                "{" +
                "  \"name\": \"Full Name\"," +
                "  \"pos\": \"Position (e.g. ST, CM, CB)\"," +
                "  \"rat\": \"Current overall rating\"," +
                "  \"age\": 25," +
                "  \"nat\": \"Country of origin\"" +
                "}";

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("model", "llama3");
        requestBody.put("prompt", promptText);
        requestBody.put("stream", false);
        requestBody.put("format", "json");

        RestTemplate restTemplate = new RestTemplate();
        Map<String, Object> response = restTemplate.postForObject(OLLAMA_URL, requestBody, Map.class);
        String aiResponse = (String) response.get("response");

        ObjectMapper mapper = new ObjectMapper();
        JsonNode pNode = mapper.readTree(aiResponse);

        Player p = new Player();
        p.setName(pNode.path("name").asText());
        p.setPosition(pNode.path("pos").asText());
        p.setRating(pNode.path("rat").asText());
        p.setAge(pNode.path("age").asInt());

        String nat = pNode.path("nat").asText();
        p.setCountry(nat.isEmpty() ? team.getCountry() : nat);

        p.setTeam(team);
        return playerRepository.save(p);
    }
}