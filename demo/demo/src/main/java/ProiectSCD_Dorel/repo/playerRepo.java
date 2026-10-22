package ProiectSCD_Dorel.repo;

import ProiectSCD_Dorel.model.Player;
import ProiectSCD_Dorel.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface playerRepo extends JpaRepository<Player, Integer> {
    // Filtrare după țară (existent)
    List<Player> findByCountryOrderByRatingDesc(String country);
    // În playerRepo.java
    List<Player> findByTeamIdOrderByRatingDesc(int teamId);
    List<Player> findByTeamIdOrderByRatingAsc(int teamId);
    // Filtrare după echipa (ID) - Esențial pentru săgețile tale
    List<Player> findByTeamId(int teamId);

    // Filtrare după un rating minim (ex: caută toți peste 80)
    List<Player> findByRatingGreaterThanEqual(String rating);

    // Query-ul tău elite existent
    @Query("SELECT p FROM Player p WHERE p.team.id = :teamId AND p.rating > '85'")
    List<Player> findElitePlayersByTeam(@Param("teamId") int teamId);
}