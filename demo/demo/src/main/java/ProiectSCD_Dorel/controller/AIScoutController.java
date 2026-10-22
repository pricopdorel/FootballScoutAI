package ProiectSCD_Dorel.controller;

import ProiectSCD_Dorel.model.Player;
import ProiectSCD_Dorel.model.Team;
import ProiectSCD_Dorel.service.AIScoutService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/ai")
@CrossOrigin(origins = "*")
public class AIScoutController {

    @Autowired
    private AIScoutService aiScoutService;

    /**
     * Endpoint pentru generarea unei echipe întregi (11 jucători + atribute echipă)
     */
    @PostMapping("/scout")
    public Team scoutTeam(@RequestParam String name, @RequestParam int leagueId) {
        try {
            return aiScoutService.scoutFullTeamFromSoFifa(name, leagueId);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la scouting echipă: " + e.getMessage());
        }
    }

    /**
     * Endpoint pentru adăugarea unui singur jucător într-o echipă existentă.
     * AI-ul va căuta automat poziția, rating-ul, vârsta și naționalitatea.
     */
    @PostMapping("/scout-player")
    public Player scoutPlayer(@RequestParam String playerName, @RequestParam int teamId) {
        try {
            return aiScoutService.addPlayerWithAI(playerName, teamId);
        } catch (Exception e) {
            throw new RuntimeException("Eroare la scouting jucător: " + e.getMessage());
        }
    }
}