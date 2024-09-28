package ua.goit.hw11.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "planet")
public class Planet {
    @Id
    @Column(updatable = false, nullable = false)
    private String id;

    @Column(name = "name",length = 500)
    private String name;

    @OneToMany(mappedBy = "fromPlanet", fetch = FetchType.EAGER)
    private List<Ticket> fromTickets;

    @OneToMany(mappedBy = "toPlanet", fetch = FetchType.EAGER)
    private List<Ticket> toTickets;
}