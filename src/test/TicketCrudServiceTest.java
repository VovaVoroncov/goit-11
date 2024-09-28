import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.goit.hw11.HibernateUtil;
import ua.goit.hw11.models.Client;
import ua.goit.hw11.models.Planet;
import ua.goit.hw11.models.Ticket;
import ua.goit.hw11.services.ClientCrudService;
import ua.goit.hw11.services.PlanetCrudService;
import ua.goit.hw11.services.TicketCrudService;

import javax.management.Query;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TicketCrudServiceTest {

    private TicketCrudService ticketCrudService;
    private ClientCrudService clientCrudService;
    private PlanetCrudService planetCrudService;
    private Session session;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        clientCrudService = mock(ClientCrudService.class);
        planetCrudService = mock(PlanetCrudService.class);
        ticketCrudService = new TicketCrudService(clientCrudService, planetCrudService);

        session = mock(Session.class);
        transaction = mock(Transaction.class);
        when(session.beginTransaction()).thenReturn(transaction);

        HibernateUtil.setSessionFactory(mockSessionFactory(session)); // Use your method to set up a mock SessionFactory
    }

    @Test
    void testCreate_NewClientAndPlanets() throws SQLException {
        Ticket ticket = new Ticket();
        Client client = new Client();
        client.setId(0); // New client
        ticket.setClient(client);

        Planet fromPlanet = new Planet();
        fromPlanet.setId(null); // New planet
        ticket.setFromPlanet(fromPlanet);

        Planet toPlanet = new Planet();
        toPlanet.setId(null); // New planet
        ticket.setToPlanet(toPlanet);

        when(clientCrudService.create(client)).thenReturn(client);
        when(planetCrudService.create(fromPlanet)).thenReturn(fromPlanet);
        when(planetCrudService.create(toPlanet)).thenReturn(toPlanet);

        Ticket createdTicket = ticketCrudService.create(ticket);

        assertNotNull(createdTicket);
        assertNotNull(createdTicket.getClient());
        assertNotNull(createdTicket.getFromPlanet());
        assertNotNull(createdTicket.getToPlanet());
        assertNotNull(createdTicket.getCreatedAt());

        verify(session).persist(any(Ticket.class));
        verify(transaction).commit();
    }

    @Test
    void testGetById() throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(1);

        when(session.get(Ticket.class, 1)).thenReturn(ticket);

        Ticket result = ticketCrudService.getById(1);

        assertNotNull(result);
        assertEquals(ticket.getId(), result.getId());
    }

    @Test
    void testUpdate() throws SQLException {
        Ticket existingTicket = new Ticket();
        existingTicket.setId(1);

        Ticket updateTicket = new Ticket();
        Client newClient = new Client();
        Planet newFromPlanet = new Planet();
        Planet newToPlanet = new Planet();

        updateTicket.setClient(newClient);
        updateTicket.setFromPlanet(newFromPlanet);
        updateTicket.setToPlanet(newToPlanet);

        when(session.get(Ticket.class, 1)).thenReturn(existingTicket);

        Ticket updatedTicket = ticketCrudService.update(1, updateTicket);

        assertNotNull(updatedTicket);
        assertEquals(existingTicket.getId(), updatedTicket.getId());
        assertEquals(newClient, updatedTicket.getClient());
        assertEquals(newFromPlanet, updatedTicket.getFromPlanet());
        assertEquals(newToPlanet, updatedTicket.getToPlanet());

        verify(session).persist(existingTicket);
        verify(transaction).commit();
    }

    @Test
    void testDelete() throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(1);

        when(session.get(Ticket.class, 1)).thenReturn(ticket);

        boolean result = ticketCrudService.delete(1);

        assertTrue(result);
        verify(session).remove(ticket);
        verify(transaction).commit();
    }

    @Test
    void testGetAll() throws SQLException {
        Ticket ticket = new Ticket();
        ticket.setId(1);

        when(session.createQuery("FROM Ticket", Ticket.class)).thenReturn(mockQuery(ticket));

        List<Ticket> tickets = ticketCrudService.getAll();

        assertNotNull(tickets);
        assertEquals(1, tickets.size());
        assertEquals(ticket.getId(), tickets.get(0).getId());
    }

    private Query<Ticket> mockQuery(Ticket ticket) {
        Query<Ticket> query = mock(Query.class);
        when(query.list()).thenReturn(Collections.singletonList(ticket));
        return query;
    }

    private SessionFactory mockSessionFactory(Session session) {
        SessionFactory sessionFactory = mock(SessionFactory.class);
        when(sessionFactory.openSession()).thenReturn(session);
        return sessionFactory;
    }
}
