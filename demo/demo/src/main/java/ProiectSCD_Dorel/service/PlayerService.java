package ProiectSCD_Dorel.service;

import ProiectSCD_Dorel.model.Player;
import ProiectSCD_Dorel.repo.playerRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class PlayerService {

    @Autowired
    private playerRepo playerRepository;

    // Returnează toți jucătorii
    public List<Player> getAllPlayers() {
        return playerRepository.findAll();
    }

    // Găsește un jucător după ID (Adăugată pentru a fi la fel ca în LeagueService)
    public Player getPlayerById(int id) {
        return playerRepository.findById(id).orElse(null);
    }

    // Creează un jucător nou
    public Player createPlayer(Player player) {
        return playerRepository.save(player);
    }

    // Actualizează un jucător existent
    public Player updatePlayer(int id, Player details) {
        Player p = playerRepository.findById(id).orElseThrow();
        p.setName(details.getName());
        p.setCountry(details.getCountry());
        p.setAge(details.getAge());
        p.setPosition(details.getPosition());
        p.setRating(details.getRating());
        p.setTeam(details.getTeam()); // Asigură-te că setTeam există în modelul Player
        return playerRepository.save(p);
    }

    // Șterge un jucător
    public void deletePlayer(int id) {
        playerRepository.deleteById(id);
    }

    // Metodă personalizată (Custom Query)
    public List<Player> getPlayersByNationality(String nationality) {
        return playerRepository.findByCountryOrderByRatingDesc(nationality);
    }

    // În PlayerService.java
    public List<Player> getPlayersByRatingDesc(int teamId) {
        // Schimbăm apelul pentru a returna lista sortată descrescător după rating
        return playerRepository.findByTeamIdOrderByRatingDesc(teamId);
    }
    public List<Player> getPlayersByRatingAsc(int teamId) {
        // Schimbăm apelul pentru a returna lista sortată descrescător după rating
        return playerRepository.findByTeamIdOrderByRatingAsc(teamId);
    }
    public List<Player> getPlayersByTeam(int teamId) {
        return playerRepository.findByTeamId(teamId);
    }

    // Filtrare după rating minim
    public List<Player> getPlayersWithMinRating(String minRating) {
        return playerRepository.findByRatingGreaterThanEqual(minRating);
    }
}