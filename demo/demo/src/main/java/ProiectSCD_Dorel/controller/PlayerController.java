package ProiectSCD_Dorel.controller;

import ProiectSCD_Dorel.model.Player;
import ProiectSCD_Dorel.service.PlayerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/player")
@CrossOrigin(origins = "*")
public class PlayerController {

    @Autowired
    private PlayerService playerService;

    // 1. Vizualizare toți jucătorii
    @GetMapping("/all")
    public List<Player> getAll() {
        return playerService.getAllPlayers();
    }

    // 2. Vizualizare un singur jucător după ID
    @GetMapping("/{id}")
    public Player getById(@PathVariable int id) {
        return playerService.getPlayerById(id);
    }

    // 3. Creare jucător nou
    @PostMapping("/add")
    public Player create(@RequestBody Player p) {
        return playerService.createPlayer(p);
    }

    // 4. Actualizare jucător existent
    @PutMapping("/update/{id}")
    public Player update(@PathVariable int id, @RequestBody Player p) {
        return playerService.updatePlayer(id, p);
    }

    // 5. Ștergere jucător
    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        playerService.deletePlayer(id);
    }

    // 6. Endpoint pentru Query Personalizat (Filtrare după țară)
    @GetMapping("/filter/{country}")
    public List<Player> getByNationality(@PathVariable String country) {
        return playerService.getPlayersByNationality(country);
    }

    // În PlayerController.java
    @GetMapping("/filter/ratingDesc/{teamId}")
    public List<Player> getByRatingDesc(@PathVariable int teamId) {
        // Acesta va returna acum jucătorii echipei ordonați de la rating mare la rating mic
        return playerService.getPlayersByRatingDesc(teamId);
    }
    @GetMapping("/filter/ratingAsc/{teamId}")
    public List<Player> getByRatingAsc(@PathVariable int teamId) {
        // Acesta va returna acum jucătorii echipei ordonați de la rating mare la rating mic
        return playerService.getPlayersByRatingAsc(teamId);
    }
    @GetMapping("/team/{teamId}")
    public List<Player> getByTeam(@PathVariable int teamId) {
        return playerService.getPlayersByTeam(teamId);
    }

    // 8. Filtrare după Rating minim
    @GetMapping("/rating/{minRating}")
    public List<Player> getByMinRating(@PathVariable String minRating) {
        return playerService.getPlayersWithMinRating(minRating);
    }
}