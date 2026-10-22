package ProiectSCD_Dorel.service;

import ProiectSCD_Dorel.model.Team;
import ProiectSCD_Dorel.repo.teamRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private teamRepo teamRepository;

    // 1. Corespunde getAllTeams()
    public List<Team> getTeam() {
        return teamRepository.findAll();
    }

    // 2. Corespunde getTeamById()
    public Team getTeamById(int id) {
        return teamRepository.findById(id).orElse(null);
    }

    // 3. Corespunde createTeam()
    public Team createTeam(Team team) {
        return teamRepository.save(team);
    }

    // 4. Corespunde updateTeam()
    public Team updateTeam(int id, Team details) {
        Team team = teamRepository.findById(id).orElseThrow(() -> new RuntimeException("Team not found"));
        team.setName(details.getName());
        team.setCountry(details.getCountry());
        team.setAttack(details.getAttack());
        team.setMidfield(details.getMidfield());
        team.setDefence(details.getDefence());
        team.setRating(details.getRating());
        team.setLeague(details.getLeague());
        return teamRepository.save(team);
    }

    // 5. Corespunde deleteTeam()
    public void deleteTeam(int id) {
        teamRepository.deleteById(id);
    }

    // 6. Corespunde getTeamsByCountry() (Custom Logic)
    public List<Team> getTeamsByCountry(String country) {
        return teamRepository.findAll().stream()
                .filter(team -> team.getCountry() != null && team.getCountry().equalsIgnoreCase(country))
                .collect(Collectors.toList());
    }
    // 8. Corespunde getTopAttackTeams() (Custom Query Logic)
    public List<Team> getTopAttackTeams() {
        return teamRepository.findAll().stream()
                .filter(team -> team.getAttack() != null && team.getAttack() > 80)
                .sorted((t1, t2) -> t2.getAttack().compareTo(t1.getAttack()))
                .collect(Collectors.toList());
    }

    public List<Team> getTeamsByLeague(int leagueId) {
        return teamRepository.findByLeagueId(leagueId);
    }
}