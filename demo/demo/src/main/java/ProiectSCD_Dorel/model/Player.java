package ProiectSCD_Dorel.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String country;
    private Integer age;
    private String position;
    private String rating;

    @ManyToOne
    @JoinColumn(name = "team_id")
    @JsonIgnoreProperties("players") // Afișează echipa, dar ignoră lista de jucători din echipa aia
    private Team team;
}