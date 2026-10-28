package org.egibide;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.egibide.dao.AsociacionDaoImpl;
import org.egibide.idao.AsociacionDao;
import org.egibide.models.Asociacion;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.util.List;

/**
 * Unit test for simple App.
 */
public class AppTest extends TestCase {
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp() {
        assertTrue(true);
    }

    // Comprobar la conexion con la base de datos
    public void testConexion() throws Exception {

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        assertNotNull(conexion);
        assertFalse(conexion.isClosed());
    }

    // Probar los metodos de AsociacionDAO
    public void testAsociacionDAO() throws Exception {

        AsociacionDao asociacionDao = new AsociacionDaoImpl();

        // Crear una asociación de prueba
        String cifPrueba = "TEST" + System.currentTimeMillis();

        Asociacion asociacion = new Asociacion(
                0,
                "Asociacion de prueba",
                "Asociacion de prueba S.L.",
                LocalDate.of(2025, 1, 15),
                "Calle de prueba, Vitoria-Gasteiz",
                "600123456",
                "prueba@example.com",
                cifPrueba
        );

        // INSERT: comprobar que se inserta y devuelve un ID
        int id = asociacionDao.add(asociacion);
        assertTrue("No se ha insertado la asociación", id > 0);

        // GET: comprobar que se recupera por su ID
        Asociacion asociacionObtenida = asociacionDao.getAsociacion(id);

        assertNotNull(asociacionObtenida);
        assertEquals("Asociacion de prueba", asociacionObtenida.getNombre());
        assertEquals(cifPrueba, asociacionObtenida.getCif());

        // LIST: comprobar que aparece en la lista de asociaciones
        List<Asociacion> lista = asociacionDao.getAsociaciones();

        boolean encontrada = false;

        for (Asociacion a : lista) {
            if (a.getId() == id) {
                encontrada = true;
                break;
            }
        }

        assertTrue("La asociación no aparece en la lista", encontrada);

        // UPDATE: modificar el nombre de la asociación
        asociacionObtenida.setNombre("Asociacion modificada");

        assertTrue(asociacionDao.update(asociacionObtenida));

        Asociacion asociacionModificada = asociacionDao.getAsociacion(id);

        assertEquals("Asociacion modificada", asociacionModificada.getNombre());

        // DELETE: eliminar la asociación de prueba
        asociacionDao.delete(id);

        assertNull(asociacionDao.getAsociacion(id));
    }

    // TODO : Hacer lo de "Implementación de suite de tests mediante JUnit."
}