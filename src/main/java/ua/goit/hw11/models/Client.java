package ua.goit.hw11.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "client")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(length = 200)
    private String name;

    @OneToMany(mappedBy = "client", fetch = FetchType.EAGER)
    private List<Ticket> tickets;
}