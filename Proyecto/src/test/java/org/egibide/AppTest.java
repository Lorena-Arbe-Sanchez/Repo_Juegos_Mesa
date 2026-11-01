package org.egibide;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.egibide.dao.*;
import org.egibide.idao.*;
import org.egibide.models.*;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

/**
 * Pruebas unitarias para comprobar el funcionamiento de la conexión
 * y de los DAO de la aplicación de gestión de la asociación de juegos.
 */
public class AppTest extends TestCase {
    /**
     * Constructor de la clase de pruebas.
     *
     * @param testName nombre del test
     */
    public AppTest(String testName) {
        super(testName);
    }

    /**
     * Crea la suite con todos los métodos de prueba de esta clase.
     *
     * @return conjunto de pruebas
     */
    public static Test suite() {
        return new TestSuite(AppTest.class);
    }

    /**
     * Comprueba que la conexión con la base de datos funciona correctamente.
     *
     * @throws Exception si ocurre algún error al obtener la conexión
     */
    public void testConexion() throws Exception {

        Connection conexion = DatabaseConnection.getInstance().getConnection();

        assertNotNull("La conexión no debería ser null", conexion);
        assertFalse("La conexión debería estar abierta", conexion.isClosed());
    }

    /**
     * Comprueba las operaciones CRUD del DAO de asociaciones.
     *
     * @throws Exception si ocurre algún error durante las operaciones
     */
    public void testAsociacionDAO() throws Exception {
        AsociacionDao dao = new AsociacionDaoImpl();

        int id = 0;
        String cif = "TEST" + System.currentTimeMillis();

        try {
            Asociacion asociacion = new Asociacion(
                    0,
                    "Asociacion de prueba",
                    "Asociacion de prueba S.L.",
                    LocalDate.of(2025, 1, 15),
                    "Calle de prueba, Vitoria-Gasteiz",
                    "600123456",
                    "prueba@example.com",
                    cif
            );

            // Insertar asociación.
            id = dao.add(asociacion);
            assertTrue("No se ha insertado la asociación", id > 0);

            // Buscar asociación por ID.
            Asociacion obtenida = dao.getAsociacion(id);
            assertNotNull(obtenida);
            assertEquals("Asociacion de prueba", obtenida.getNombre());
            assertEquals(cif, obtenida.getCif());

            // Comprobar listado.
            List<Asociacion> lista = dao.getAsociaciones();
            boolean encontrada = false;

            for (Asociacion a : lista) {
                if (a.getId() == id) {
                    encontrada = true;
                    break;
                }
            }

            assertTrue("La asociación no aparece en el listado", encontrada);

            // Actualizar asociación.
            obtenida.setNombre("Asociacion modificada");
            assertTrue(dao.update(obtenida));

            Asociacion modificada = dao.getAsociacion(id);
            assertEquals("Asociacion modificada", modificada.getNombre());

            // Eliminar asociación.
            dao.delete(id);
            assertNull(dao.getAsociacion(id));

            id = 0;

        } finally {
            // Limpiar el registro si el test falla antes de eliminarlo.
            if (id > 0) {
                dao.delete(id);
            }
        }
    }

    /**
     * Comprueba las operaciones CRUD y las búsquedas del DAO de socias.
     *
     * @throws Exception si ocurre algún error durante las operaciones
     */
    public void testSociaDAO() throws Exception {
        SociaDao dao = new SociaDaoImpl();

        String documento = "TST" + System.currentTimeMillis();

        try {
            Socia socia = new Socia();
            socia.setDocumento(documento);
            socia.setNombre("NombreTest");
            socia.setApellido1("ApellidoTest");
            socia.setApellido2("SegundoTest");
            socia.setTelefono("600123456");
            socia.setEdad(25);
            socia.setEmail("socia.test@example.com");

            // Insertar socia.
            dao.insertar(socia);

            // Buscar por documento.
            Socia obtenida = dao.buscarDocumento(documento);

            assertNotNull(obtenida);
            assertEquals(documento, obtenida.getDocumento());
            assertEquals("NombreTest", obtenida.getNombre());

            // Comprobar listado.
            List<Socia> lista = dao.listar();
            boolean encontrada = false;

            for (Socia s : lista) {
                if (s.getDocumento().equals(documento)) {
                    encontrada = true;
                    break;
                }
            }

            assertTrue("La socia no aparece en el listado", encontrada);

            // Buscar por nombre.
            assertFalse(dao.buscarPorNombre("NombreTest").isEmpty());

            // Buscar por primer apellido.
            assertFalse(dao.buscarPorPrimerApellido("ApellidoTest").isEmpty());

            // Buscar por segundo apellido.
            assertFalse(dao.buscarPorSegundoApellido("SegundoTest").isEmpty());

            // Actualizar socia.
            obtenida.setNombre("NombreModificado");
            dao.actualizar(obtenida);

            Socia modificada = dao.buscarDocumento(documento);
            assertEquals("NombreModificado", modificada.getNombre());

            // Eliminar socia.
            dao.eliminar(documento);
            assertNull(dao.buscarDocumento(documento));

        } finally {
            // Limpiar el registro si el test falla antes de eliminarlo.
            dao.eliminar(documento);
        }
    }

    /**
     * Comprueba las operaciones CRUD del DAO de invitadas.
     *
     * @throws Exception si ocurre algún error durante las operaciones
     */
    public void testInvitadaDAO() throws Exception {
        InvitadaDao dao = new InvitadaDaoImpl();

        String documento = "TST" + System.currentTimeMillis();

        try {
            Invitada invitada = new Invitada(
                    documento,
                    "InvitadaTest",
                    "ApellidoTest",
                    "SegundoTest",
                    "600123456",
                    24,
                    "invitada.test@example.com"
            );

            // Insertar invitada.
            dao.insertar(invitada);

            // Buscar por documento.
            Invitada obtenida = dao.buscarDocumento(documento);

            assertNotNull(obtenida);
            assertEquals(documento, obtenida.getDocumento());
            assertEquals("InvitadaTest", obtenida.getNombre());

            // Comprobar listado.
            List<Invitada> lista = dao.listar();
            boolean encontrada = false;

            for (Invitada i : lista) {
                if (i.getDocumento().equals(documento)) {
                    encontrada = true;
                    break;
                }
            }

            assertTrue("La invitada no aparece en el listado", encontrada);

            // Actualizar invitada.
            obtenida.setNombre("InvitadaModificada");
            dao.actualizar(obtenida);

            Invitada modificada = dao.buscarDocumento(documento);
            assertEquals("InvitadaModificada", modificada.getNombre());

            // Eliminar invitada.
            dao.eliminar(documento);
            assertNull(dao.buscarDocumento(documento));

        } finally {
            // Limpiar el registro si el test falla antes de eliminarlo.
            dao.eliminar(documento);
        }
    }

    /**
     * Comprueba las operaciones CRUD y las búsquedas del DAO de juegos.
     *
     * @throws Exception si ocurre algún error durante las operaciones
     */
    public void testJuegoDAO() throws Exception {
        JuegoDao dao = new JuegoDaoImpl();
        AsociacionDao asociacionDao = new AsociacionDaoImpl();

        int id = 0;
        String nombre = "JuegoTest" + System.currentTimeMillis();

        try {
            // Comprobar que existe la asociación propietaria.
            Asociacion asociacion = asociacionDao.getAsociacion(1);
            assertNotNull("No existe la asociación con ID 1", asociacion);

            Juego juego = new Juego();
            juego.setNombre(nombre);
            juego.setAutor1("AutorTest");
            juego.setAutor2(null);
            juego.setDescripcion("Descripcion de prueba");
            juego.setMinJugadores(2);
            juego.setMaxJugadores(6);
            juego.setDuracion(60);
            juego.setTematica("TematicaTest");
            juego.setCompetitivo(true);
            juego.setUbicacion("EstanteriaTest");
            juego.setEstado("disponible");
            juego.setAsociacionPropietaria(asociacion);
            juego.setSociaPropietaria(null);
            juego.setFechaAlta(LocalDate.of(2026, 1, 15));

            // Insertar juego.
            id = dao.insertar(juego);
            assertTrue("No se ha insertado el juego", id > 0);

            // Buscar por ID.
            Juego obtenido = dao.buscarPorId(id);

            assertNotNull(obtenido);
            assertEquals(nombre, obtenido.getNombre());
            assertNotNull("No se ha recuperado la asociación propietaria",
                    obtenido.getAsociacionPropietaria());
            assertEquals(1, obtenido.getAsociacionPropietaria().getId());

            // Comprobar listado.
            List<Juego> lista = dao.listar();
            boolean encontrado = false;

            for (Juego j : lista) {
                if (j.getId() == id) {
                    encontrado = true;
                    break;
                }
            }

            assertTrue("El juego no aparece en el listado", encontrado);

            // Buscar por nombre.
            assertFalse(dao.buscarPorNombre(nombre).isEmpty());

            // Buscar por temática.
            assertFalse(dao.buscarPorTematica("TematicaTest").isEmpty());

            // Buscar por número de jugadores.
            assertFalse(dao.buscarPorNumeroJugadores(4).isEmpty());

            // Actualizar juego.
            obtenido.setNombre(nombre + "Modificado");
            dao.actualizar(obtenido);

            Juego modificado = dao.buscarPorId(id);
            assertEquals(nombre + "Modificado", modificado.getNombre());

            // Eliminar juego.
            dao.eliminar(id);
            assertNull(dao.buscarPorId(id));

            id = 0;

        } finally {
            // Limpiar el juego si el test falla antes de eliminarlo.
            if (id > 0) {
                dao.eliminar(id);
            }
        }
    }

    /**
     * Comprueba las operaciones CRUD y las búsquedas del DAO de partidas.
     * También comprueba que se recupera correctamente el juego asociado.
     *
     * @throws Exception si ocurre algún error durante las operaciones
     */
    public void testPartidaDAO() throws Exception {
        PartidaDao partidaDao = new PartidaDaoImpl();
        JuegoDao juegoDao = new JuegoDaoImpl();
        AsociacionDao asociacionDao = new AsociacionDaoImpl();

        int idJuego = 0;
        int idPartida = 0;

        LocalDate fecha = LocalDate.of(2099, 5, 20);

        try {
            // Crear un juego para asociarlo a la partida.
            Asociacion asociacion = asociacionDao.getAsociacion(1);
            assertNotNull("No existe la asociación con ID 1", asociacion);

            Juego juego = new Juego();
            juego.setNombre("JuegoPartidaTest" + System.currentTimeMillis());
            juego.setAutor1("AutorTest");
            juego.setDescripcion("Juego para probar partidas");
            juego.setMinJugadores(2);
            juego.setMaxJugadores(6);
            juego.setDuracion(60);
            juego.setTematica("PartidaTest");
            juego.setCompetitivo(true);
            juego.setUbicacion("EstanteriaTest");
            juego.setEstado("disponible");
            juego.setAsociacionPropietaria(asociacion);
            juego.setFechaAlta(LocalDate.of(2026, 1, 15));

            idJuego = juegoDao.insertar(juego);
            assertTrue(idJuego > 0);

            // Crear partida.
            Juego juegoObtenido = juegoDao.buscarPorId(idJuego);

            Partida partida = new Partida();
            partida.setJuego(juegoObtenido);
            partida.setFecha(fecha);
            partida.setHora(LocalTime.of(17, 30));
            partida.setJugadoresDeseados(4);
            partida.setPlazasLibres(3);

            // Insertar partida.
            idPartida = partidaDao.insertar(partida);
            assertTrue("No se ha insertado la partida", idPartida > 0);

            // Buscar por ID.
            Partida obtenida = partidaDao.buscarporId(idPartida);

            assertNotNull(obtenida);
            assertEquals(idPartida, obtenida.getId());
            assertEquals(fecha, obtenida.getFecha());
            assertNotNull("No se ha recuperado el juego asociado", obtenida.getJuego());
            assertEquals(idJuego, obtenida.getJuego().getId());
            assertEquals(juegoObtenido.getNombre(), obtenida.getJuego().getNombre());

            // Comprobar listado.
            List<Partida> lista = partidaDao.listar();
            boolean encontrada = false;

            for (Partida p : lista) {
                if (p.getId() == idPartida) {
                    encontrada = true;
                    assertNotNull("El listado no recupera el juego asociado", p.getJuego());
                    assertEquals(idJuego, p.getJuego().getId());
                    break;
                }
            }

            assertTrue("La partida no aparece en el listado", encontrada);

            // Buscar por juego.
            assertFalse(partidaDao.buscarPorJuego(juegoObtenido.getNombre()).isEmpty());

            // Buscar por fecha.
            assertFalse(partidaDao.buscarPorFecha(fecha).isEmpty());

            // Actualizar partida.
            obtenida.setPlazasLibres(2);
            partidaDao.actualizar(obtenida);

            Partida modificada = partidaDao.buscarporId(idPartida);
            assertEquals(2, modificada.getPlazasLibres());

            // Eliminar partida.
            partidaDao.eliminar(idPartida);
            assertNull(partidaDao.buscarporId(idPartida));

            idPartida = 0;

        } finally {
            // Eliminar primero la partida y después el juego.
            if (idPartida > 0) {
                partidaDao.eliminar(idPartida);
            }

            if (idJuego > 0) {
                juegoDao.eliminar(idJuego);
            }
        }
    }
}