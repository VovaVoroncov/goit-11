import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import ua.goit.hw11.HibernateUtil;
import ua.goit.hw11.models.Client;
import ua.goit.hw11.services.ClientCrudService;

import java.util.Collections;
import java.util.List;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ClientCrudServiceTest {

    private ClientCrudService clientCrudService;
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @BeforeEach
    void setUp() {
        sessionFactory = mock(SessionFactory.class);
        session = mock(Session.class);
        transaction = mock(Transaction.class);
        when(sessionFactory.openSession()).thenReturn(session);
        when(session.beginTransaction()).thenReturn(transaction);
        clientCrudService = new ClientCrudService(new HibernateUtil(sessionFactory));
    }

    @Test
    void testAddClient() {
        Client client = new Client();
        client.setId(1);

        clientCrudService.addClient(client);

        verify(session).persist(client);
        verify(transaction).commit();
    }

    @Test
    void testGetById() {
        Client client = new Client();
        client.setId(1);

        when(session.get(Client.class, 1)).thenReturn(client);

        Client result = clientCrudService.getById(1);

        assertNotNull(result);
        assertEquals(1, result.getId());
    }

    @Test
    void testUpdateClient() {
        Client client = new Client();
        client.setId(1);

        when(session.merge(client)).thenReturn(client);

        clientCrudService.updateClient(client, 1);

        verify(session).merge(client);
        verify(transaction).commit();
    }

    @Test
    void testDeleteById() {
        Client client = new Client();
        client.setId(1);

        when(session.get(Client.class, 1)).thenReturn(client);

        clientCrudService.deleteById(1);

        verify(session).remove(client);
        verify(transaction).commit();
    }

    @Test
    void testGetAllClients() {
        Client client = new Client();
        client.setId(1);

        when(session.createQuery("from Client", Client.class)).thenReturn(mockQuery(client));

        List<Client> clients = clientCrudService.getAllClients();

        assertNotNull(clients);
        assertEquals(1, clients.size());
        assertEquals(1, clients.get(0).getId());
    }

    private Query<Client> mockQuery(Client client) {
        Query<Client> query = mock(Query.class);
        when(query.list()).thenReturn(Collections.singletonList(client));
        return query;
    }
}
