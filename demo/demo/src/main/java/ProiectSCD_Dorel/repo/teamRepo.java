package ProiectSCD_Dorel.repo;

import ProiectSCD_Dorel.model.Team;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface teamRepo extends JpaRepository<Team, Integer> {
    // Spring Boot va genera automat: SELECT * FROM team WHERE league_id = ?
    List<Team> findByLeagueId(int leagueId);
}
