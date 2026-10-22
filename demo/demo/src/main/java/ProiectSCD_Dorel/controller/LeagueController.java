package ProiectSCD_Dorel.controller;

import ProiectSCD_Dorel.model.League;
import ProiectSCD_Dorel.service.LeagueService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/league")
@CrossOrigin(origins = "*")
public class LeagueController {

    @Autowired
    private LeagueService leagueService;

    @GetMapping("/all")
    public List<League> getAll() {
        return leagueService.getAllLeagues();
    }

    @GetMapping("/{id}")
    public League getById(@PathVariable int id) {
        return leagueService.getLeagueById(id);
    }

    @PostMapping("/add")
    public League create(@RequestBody League league) {
        return leagueService.createLeague(league);
    }

    @PutMapping("/update/{id}")
    public League update(@PathVariable int id, @RequestBody League league) {
        return leagueService.updateLeague(id, league);
    }

    @DeleteMapping("/delete/{id}")
    public void delete(@PathVariable int id) {
        leagueService.deleteLeague(id);
    }
}