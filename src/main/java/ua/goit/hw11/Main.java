package ua.goit.hw11;

import ua.goit.hw11.services.PlanetCrudService;
import ua.goit.hw11.services.TicketCrudService;
import ua.goit.hw11.models.Client;
import ua.goit.hw11.models.Ticket;

import java.time.LocalDateTime;
import java.util.List;

public class Main {
    public static void main(String[] args) {

        new DatabaseInitService().initDB();
        HibernateUtil hibernateUtil = HibernateUtil.getInstance();

        PlanetCrudService planetCrudService = new PlanetCrudService(hibernateUtil);
        TicketCrudService ticketCrudService = new TicketCrudService(hibernateUtil);

        Client client = new Client();

        Ticket ticket1 = new Ticket();
        ticket1.setCreatedAt(LocalDateTime.now());
        client.setId(5);
        ticket1.setClient(client);

        ticket1.setFromPlanet(planetCrudService.getById("TEST1"));
        ticket1.setToPlanet(planetCrudService.getById("TEST2"));

        ticketCrudService.addTicket(ticket1);
        System.out.println("New ticket: " + ticket1);

        System.out.println("Get ticket by id: " + ticketCrudService.getById(11));

        Ticket updateTicketById = ticketCrudService.getById(11);
        updateTicketById.setToPlanet(planetCrudService.getById("ELY"));
        ticketCrudService.updateTicket(updateTicketById, 11);
        System.out.println("Update ticket by id: " + ticketCrudService.getById(11));

        ticketCrudService.deleteById(4);

        List<Ticket> allTickets = ticketCrudService.getAllTickets();
        System.out.println("All Tickets");
        for (Ticket ticket : allTickets) {
            System.out.println(ticket);
        }
    }
}