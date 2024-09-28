package ua.goit.hw11.models;

import jakarta.persistence.*;
import lombok.Data;
import lombok.ToString;

import java.time.LocalDateTime;

@Data
@Entity
@Table(name = "ticket")
public class Ticket {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name="client_id", nullable=false)
    private Client client;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name="from_planet_id", nullable=false)
    private Planet fromPlanet;

    @ToString.Exclude
    @ManyToOne()
    @JoinColumn(name="to_planet_id", nullable=false)
    private Planet toPlanet;

}