package ProiectSCD_Dorel.repo;

import ProiectSCD_Dorel.model.League;
import org.springframework.data.jpa.repository.JpaRepository;

public interface leagueRepo extends JpaRepository<League, Integer> {
}