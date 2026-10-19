package org.egibide;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;

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

    // TODO : Hacer lo de "Implementación de suite de tests mediante JUnit."

    // Comprobar la conexion con la base de datos
    public void testConexion() throws Exception {

        Connection conexion = DatabaseConnection
                .getInstance()
                .getConnection();

        assertNotNull(conexion);
        assertFalse(conexion.isClosed());
    }

    // Probar los metodos de AsociacionDAO
    public void testAsociacionDAO() {

        //
    }
}

/*
Resultado de los Test:


 */