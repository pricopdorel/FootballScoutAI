package ProiectSCD_Dorel.controller;

import ProiectSCD_Dorel.model.Team;
import ProiectSCD_Dorel.service.TeamService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/team")
@CrossOrigin(origins = "*") // Permite accesul din Frontend (Vue) si Desktop (C#)
public class TeamController {

    @Autowired
    private TeamService teamService;

    // 1. GET: Toate echipele
    @GetMapping("/all")
    public List<Team> getAllTeams() {
        return teamService.getTeam();
    }

    // 2. GET: O singură echipă după ID
    @GetMapping("/{id}")
    public Team getTeamById(@PathVariable int id) {
        return teamService.getTeamById(id);
    }

    // 3. POST: Adăugare echipă (Create item)
    @PostMapping("/add")
    public Team createTeam(@RequestBody Team team) {
        return teamService.createTeam(team);
    }

    // 4. PUT: Actualizare echipă (Edit item)
    @PutMapping("/update/{id}")
    public Team updateTeam(@PathVariable int id, @RequestBody Team team) {
        return teamService.updateTeam(id, team);
    }

    // 5. DELETE: Ștergere echipă
    @DeleteMapping("/delete/{id}")
    public String deleteTeam(@PathVariable int id) {
        teamService.deleteTeam(id);
        return "Team with ID " + id + " has been deleted.";
    }

    // 6. GET: Filtrare echipe după țară (Custom Query / Filtered view)
    @GetMapping("/filter/country/{country}")
    public List<Team> getTeamsByCountry(@PathVariable String country) {
        return teamService.getTeamsByCountry(country);
    }

    @GetMapping("/league/{leagueId}")
    public List<Team> getTeamsByLeague(@PathVariable int leagueId) {
        return teamService.getTeamsByLeague(leagueId);
    }

    // 8. GET: Top echipe după ratingul de atac (Custom logic)
    @GetMapping("/top-attack")
    public List<Team> getTopAttackTeams() {
        return teamService.getTopAttackTeams();
    }
}