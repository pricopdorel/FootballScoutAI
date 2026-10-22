package ProiectSCD_Dorel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "team")
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id; // ID manual pentru a se potrivi cu scriptul SQL
    private String name;
    private String country;

    @ManyToOne
    @JoinColumn(name = "league_id")
    @JsonIgnoreProperties("teams") // Afișează liga, dar ignoră lista de echipe din interiorul ei
    private League league;

    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonIgnoreProperties("team") // Afișează jucătorii, dar ignoră câmpul 'team' din fiecare jucător
    private List<Player> players;

    private Integer attack;
    private Integer midfield;
    private Integer defence;
    private Integer rating;
}