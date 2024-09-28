package ua.goit.hw11.services;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import ua.goit.hw11.HibernateUtil;
import ua.goit.hw11.models.Client;
import ua.goit.hw11.models.Planet;
import ua.goit.hw11.models.Ticket;

import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.List;

public class TicketCrudService {
    private final ClientCrudService clientCrudService = new ClientCrudService();
    private final PlanetCrudService planetCrudService = new PlanetCrudService();

    public Ticket create(Ticket ticket) throws SQLException {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Ticket newTicket = new Ticket();
            newTicket.setCreatedAt(LocalDateTime.now());

            long clientId = ticket.getClient().getId();
            if(clientId != 0 && clientCrudService.getById(clientId) != null ){
                newTicket.setClient(ticket.getClient());
            } else {
                Client newClient = clientCrudService.create(ticket.getClient());
                newTicket.setClient(newClient);
            }

            String fromPlanetId = ticket.getFromPlanet().getId();
            if(fromPlanetId != null && planetCrudService.getById(fromPlanetId) != null ){
                newTicket.setFromPlanet(ticket.getFromPlanet());
            } else {
                Planet newPlanet = planetCrudService.create(ticket.getFromPlanet());
                newTicket.setFromPlanet(newPlanet);
            }

            String toPlanetId = ticket.getToPlanet().getId();
            if(toPlanetId != null && planetCrudService.getById(toPlanetId) != null ){
                newTicket.setToPlanet(ticket.getToPlanet());
            } else {
                Planet newPlanet = planetCrudService.create(ticket.getToPlanet());
                newTicket.setToPlanet(newPlanet);
            }
            session.persist(newTicket);
            transaction.commit();
            return newTicket;
        }
    }

    public Ticket getById(long id) throws SQLException {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.get(Ticket.class, id);
        }
    }

    public Ticket update(long id, Ticket ticket) throws SQLException {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Ticket existing = session.get(Ticket.class, id);
            existing.setClient(ticket.getClient());
            existing.setFromPlanet(ticket.getFromPlanet());
            existing.setToPlanet(ticket.getToPlanet());
            session.persist(existing);
            transaction.commit();
            return existing;
        }
    }

    public boolean delete(long id) throws SQLException {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            Ticket ticket = session.get(Ticket.class, id);
            if (ticket != null) {
                session.remove(ticket);
                transaction.commit();
                return true;
            }
            return false;
        }
    }

    public List<Ticket> getAll() throws SQLException {
        try (Session session = HibernateUtil.getInstance().getSessionFactory().openSession()) {
            return session.createQuery("FROM Ticket", Ticket.class).list();
        }
    }
}

