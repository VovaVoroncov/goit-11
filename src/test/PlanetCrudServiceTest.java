import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ua.goit.hw11.HibernateUtil;
import ua.goit.hw11.models.Planet;
import ua.goit.hw11.services.PlanetCrudService;

import java.util.Collections;
import java.util.List;

import static jdk.internal.org.objectweb.asm.util.CheckClassAdapter.verify;
import static org.junit.jupiter.api.Assertions.*;

class PlanetCrudServiceTest {

    private PlanetCrudService planetCrudService;
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
        planetCrudService = new PlanetCrudService(new HibernateUtil(sessionFactory));
    }

    @Test
    void testAddPlanet() {
        Planet planet = new Planet();
        planet.setId("earth");

        planetCrudService.addPlanet(planet);

        verify(session).persist(planet);
        verify(transaction).commit();
    }

    @Test
    void testGetById() {
        Planet planet = new Planet();
        planet.setId("earth");

        when(session.get(Planet.class, "earth")).thenReturn(planet);

        Planet result = planetCrudService.getById("earth");

        assertNotNull(result);
        assertEquals("earth", result.getId());
    }

    @Test
    void testUpdatePlanet() {
        Planet planet = new Planet();
        planet.setId("earth");

        when(session.merge(planet)).thenReturn(planet);

        planetCrudService.updatePlanet(planet, "earth");

        verify(session).merge(planet);
        verify(transaction).commit();
    }

    @Test
    void testDeleteById() {
        Planet planet = new Planet();
        planet.setId("earth");

        when(session.get(Planet.class, "earth")).thenReturn(planet);

        planetCrudService.deleteById("earth");
    }

    @Test
    void testGetAllPlanets() {
        Planet planet = new Planet();
        planet.setId("earth");

        when(session.createQuery("from Planet", Planet.class)).thenReturn(mockQuery(planet));

        List<Planet> planets = planetCrudService.getAllPlanets();

        assertNotNull(planets);
        assertEquals(1, planets.size());
        assertEquals("earth", planets.get(0).getId());
    }

    private Query<Planet> mockQuery(Planet planet) {
        Query<Planet> query = mock(Query.class);
        when(query.list()).thenReturn(Collections.singletonList(planet));
        return query;
    }
}