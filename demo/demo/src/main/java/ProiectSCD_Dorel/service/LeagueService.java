package ProiectSCD_Dorel.service;

import ProiectSCD_Dorel.model.League;
import ProiectSCD_Dorel.repo.leagueRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class LeagueService {

    @Autowired
    private leagueRepo lRepo;

    public List<League> getAllLeagues() {
        return lRepo.findAll();
    }

    public League getLeagueById(int id) {
        return lRepo.findById(id).orElse(null);
    }

    public League createLeague(League league) {
        return lRepo.save(league);
    }

    public League updateLeague(int id, League details) {
        League league = lRepo.findById(id).orElseThrow();
        league.setName(details.getName());
        league.setCountry(details.getCountry());
        return lRepo.save(league);
    }

    public void deleteLeague(int id) {
        lRepo.deleteById(id);
    }
}