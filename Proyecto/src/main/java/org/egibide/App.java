package org.egibide;

import org.egibide.dao.AsociacionDaoImpl;
import org.egibide.dao.JuegoDaoImpl;
import org.egibide.idao.AsociacionDao;
import org.egibide.idao.JuegoDao;
import org.egibide.models.Juego;
import org.egibide.utils.ExportarDatos;
import org.egibide.utils.MetadatosBD;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.models.Asociacion;
import org.egibide.models.Partida;
import org.egibide.models.Socia;
import org.egibide.utils.GestorUsuarios; //nuevo
import org.egibide.utils.RegistroSesiones;
import java.io.IOException;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;

// TODO : No dejar esta clase con demasiadas líneas; hacer funciones y separar código en diferentes ficheros y carpetas

public class App {
    public static void main(String[] args) throws SQLException {

        try (Scanner sc = new Scanner(System.in)) {

            // NUEVO: control de acceso. Si no consigue entrar, el programa termina
            String usuario = iniciarSesion(sc);

            if (usuario == null) {
                return;
            }

            // Mostrar los datos de la asociación al iniciar la aplicación
            mostrarDatosAsociacion();

            // Menú principal
            int opcion;

            do {

                System.out.println("\n\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Gestión de juegos");
                System.out.println("2. Gestión de socias");
                System.out.println("3. Gestión de partidas");
                System.out.println("4. Exportar datos XML y JSON");
                System.out.println("5. Metadatos de la BBDD");
                System.out.println("0. Salir");
                System.out.print("\nElige una opción: ");

                opcion = leerInt(sc);

                switch (opcion) {
                    case 1:
                        menuJuegos(sc);
                        break;
                    case 2:
                        menuSocias(sc);
                        break;
                    case 3:
                        menuPartidas(sc);
                        break;
                    case 4:
                        menuExportaciones(sc);
                        break;
                    case 5:
                        System.out.println("\n");
                        MetadatosBD.mostrarMetadatos();
                        MetadatosBD.exportarMetadatos();
                        break;
                    case 0:
                        System.out.println("¡Hasta pronto!");
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

            } while (opcion != 0);

            // NUEVO: se apunta la salida en el registro de sesiones
            RegistroSesiones.registrar(usuario, "SALIDA");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Mostrar los datos principales de la asociación
    private static void mostrarDatosAsociacion() {

        AsociacionDao dao = new AsociacionDaoImpl();

        Asociacion asociacion = dao.getAsociacion(1);

        if (asociacion != null) {
            System.out.println("Conexión correcta.\n");

            System.out.println("\n==================================");
            System.out.println("   " + asociacion.getNombre());
            System.out.println("==================================");
            System.out.println("Razón social: " + asociacion.getRazonSocial());
            System.out.println("Fecha de apertura: " + asociacion.getFechaApertura());
            System.out.println("Dirección: " + asociacion.getDireccion());
            System.out.println("Teléfono: " + asociacion.getTelefono());
            System.out.println("Email: " + asociacion.getEmail());
            System.out.println("CIF: " + asociacion.getCif());
        } else {
            System.out.println("No hay datos de la asociación.");
        }
    }

    // NUEVO: pide usuario y contraseña. Devuelve el usuario si entra, o null si no
    private static String iniciarSesion(Scanner sc) {
        try {
            // Si todavía no existe ningún usuario, se crea el primero
            if (!GestorUsuarios.hayUsuarios()) {
                System.out.println("\n===== PRIMER ACCESO: CREA UN USUARIO =====");

                System.out.println("Usuario: ");
                String usuarioNuevo = sc.nextLine().trim();

                System.out.println("Contraseña (mínimo 4 caracteres): ");
                String contrasenaNueva = sc.nextLine();

                System.out.println("Repite la contraseña: ");
                String repetida = sc.nextLine();

                if (usuarioNuevo.isEmpty() || contrasenaNueva.length() < 4) {
                    System.out.println("Usuario vacío o contraseña demasiado corta.");
                    return null;
                }

                if (!contrasenaNueva.equals(repetida)) {
                    System.out.println("Las contraseñas no coinciden.");
                    return null;
                }

                GestorUsuarios.registrarUsuario(usuarioNuevo, contrasenaNueva);
                RegistroSesiones.registrar(usuarioNuevo, "USUARIO CREADO");
                RegistroSesiones.registrar(usuarioNuevo, "ENTRADA");
                return usuarioNuevo;
            }

            // Si ya hay usuarios, tiene 3 intentos para identificarse
            for (int intento = 1; intento <= 3; intento++) {
                System.out.println("\n===== INICIAR SESIÓN =====");

                System.out.println("Usuario: ");
                String usuario = sc.nextLine().trim();

                System.out.println("Contraseña: ");
                String contrasena = sc.nextLine();

                if (GestorUsuarios.comprobar(usuario, contrasena)) {
                    RegistroSesiones.registrar(usuario, "ENTRADA");
                    return usuario;
                }

                RegistroSesiones.registrar(usuario, "ACCESO DENEGADO");
                System.out.println("Usuario o contraseña incorrectos. Intentos restantes: " + (3 - intento));
            }

            System.out.println("Demasiados intentos fallidos.");

        } catch (IOException e) {
            System.out.println("Error al leer o guardar las credenciales: " + e.getMessage());
        }

        return null;
    }

    // Menú de juegos
    private static void menuJuegos(Scanner sc) {

        JuegoDao dao = new JuegoDaoImpl();
        SociaDao sociaDao = new SociaDaoImpl();
        AsociacionDao asociacionDao = new AsociacionDaoImpl();

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE JUEGOS =====");
            System.out.println("1. Listar juegos");
            System.out.println("2. Dar de alta un juego");
            System.out.println("3. Modificar un juego");
            System.out.println("4. Dar de baja un juego");
            System.out.println("5. Buscar juegos");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                opcion = leerInt(sc);

                switch (opcion) {
                    case 1: {
                        List<Juego> juegos = dao.listar();

                        System.out.println("Número total de juegos: " + juegos.size());

                        if (juegos.isEmpty()) {
                            System.out.println("No hay juegos en la base de datos.");
                        } else {
                            for (Juego j : juegos) {
                                System.out.println("\nId: " + j.getId());
                                System.out.println("Nombre: " + j.getNombre());
                                System.out.println("Autor 1: " + j.getAutor1());
                                // Si devuelve "null" de la base de datos, mostrará un texto fijo
                                System.out.println("Autor 2: " +
                                        (j.getAutor2() != null ? j.getAutor2() : "(No tiene segundo autor)"));
                                System.out.println("Descripción: " + j.getDescripcion());
                                System.out.println("Jugadores: " + j.getMinJugadores() + " - " + j.getMaxJugadores());
                                System.out.println("Duración: " + j.getDuracion() + " minutos");
                                System.out.println("Temática: " + j.getTematica());

                                if (j.isCompetitivo()) {
                                    System.out.println("Tipo: Competitivo");
                                } else {
                                    System.out.println("Tipo: Cooperativo");
                                }

                                System.out.println("Ubicación: " + j.getUbicacion());

                                String estado = j.getEstado();
                                // Obtener el estado con la primera letra en mayúscula
                                estado = estado.substring(0, 1).toUpperCase() +
                                        estado.substring(1).toLowerCase();

                                System.out.println("Estado: " + estado);

                                // TODO : CORREGIR ~ Si tiene como propietaria a una socia en vez de una asociación --> No la muestra
                                if (j.getAsociacionPropietaria() != null) {
                                    System.out.println("Propietaria: Asociación llamada '" +
                                            j.getAsociacionPropietaria().getNombre() + "'");
                                } else if (j.getSociaPropietaria() != null) {
                                    System.out.println("Propietaria: Socia con DNI '" +
                                            j.getSociaPropietaria().getDocumento() + "'");
                                }

                                System.out.println("Fecha de alta: " + j.getFechaAlta());
                                System.out.println("----------------------------");
                            }
                        }
                        break;
                    }

                    case 2: {
                        Juego nuevo = new Juego();

                        String nombre = leerTexto(sc, "Nombre del juego: ");
                        nuevo.setNombre(nombre);

                        String autor1 = leerTexto(sc, "Primer autor del juego: ");
                        nuevo.setAutor1(autor1);

                        System.out.println("Segundo autor del juego (deja vacío si no hay): ");
                        String autor2 = sc.nextLine().trim();
                        if (autor2.isEmpty()) {
                            nuevo.setAutor2(null);
                        } else {
                            nuevo.setAutor2(autor2);
                        }

                        String descripcion = leerTexto(sc, "Descripción del juego: ");
                        nuevo.setDescripcion(descripcion);

                        int[] jugadores = leerJugadores(sc);
                        nuevo.setMinJugadores(jugadores[0]);
                        nuevo.setMaxJugadores(jugadores[1]);

                        nuevo.setDuracion(leerPositivo(sc, "Duración del juego (minutos): "));

                        String tematica = leerTexto(sc, "Temática del juego: ");
                        nuevo.setTematica(tematica);

                        nuevo.setCompetitivo(leerCompetitivo(sc));

                        String ubicacion = leerUbicacion(sc);
                        nuevo.setUbicacion(ubicacion);

                        // Se le asignará automáticamente el estado "disponible" y la fecha actual
                        nuevo.setEstado("disponible");
                        nuevo.setFechaAlta(LocalDate.now());

                        int propietaria = leerNumeroEnRango(sc,
                                "Propietaria del juego: 1 = Asociación | 2 = Socia: ",
                                1, 2);

                        if (propietaria == 1) {
                            // Como solo va a existir 1 asociación en la BD, tendrá que ser la existente
                            Asociacion asociacion = asociacionDao.getAsociacion(1);

                            if (asociacion == null) {
                                System.out.println("No existe la asociación en la base de datos.");
                                break;
                            }

                            nuevo.setAsociacionPropietaria(asociacion);
                        } else if (propietaria == 2) {
                            Socia socia;

                            do {
                                String documento = leerDocumento(sc);
                                socia = sociaDao.buscarDocumento(documento);

                                if (socia == null) {
                                    System.out.println("No existe ninguna socia con ese documento.");
                                }

                            } while (socia == null);

                            nuevo.setSociaPropietaria(socia);
                        } else {
                            System.out.println("Opción no válida. No se ha dado de alta el juego.");
                            break;
                        }

                        dao.insertar(nuevo);
                        System.out.println("Juego dado de alta correctamente.");
                        break;
                    }

                    case 3: {
                        int idModificar = leerPositivo(sc, "Id del juego a modificar: ");

                        Juego juego = dao.buscarPorId(idModificar);

                        if (juego == null) {
                            System.out.println("No existe ningún juego con ese id.");
                            break;
                        }

                        System.out.println("Escribe los nuevos datos del juego");

                        // TODO : Poner lo de que ya muestre los datos y tal, pero q esta vez los que aparezcan se puedan modificar tal cual; no sobreescribir
                        juego.setNombre(leerTexto(sc, "Nombre: "));

                        juego.setAutor1(leerTexto(sc, "Primer autor: "));

                        System.out.println("Segundo autor (deja vacío si no hay): ");
                        String autor2Nuevo = sc.nextLine().trim();
                        if (autor2Nuevo.isEmpty()) {
                            juego.setAutor2(null);
                        } else {
                            juego.setAutor2(autor2Nuevo);
                        }

                        juego.setDescripcion(leerTexto(sc, "Descripción: "));

                        int[] jugadores = leerJugadores(sc);
                        juego.setMinJugadores(jugadores[0]);
                        juego.setMaxJugadores(jugadores[1]);

                        juego.setDuracion(leerPositivo(sc, "Duración del juego (minutos): "));

                        System.out.println("Temática: ");
                        juego.setTematica(leerTexto(sc, ""));

                        juego.setCompetitivo(leerCompetitivo(sc));

                        String ubicacion = leerUbicacion(sc);
                        juego.setUbicacion(ubicacion);

                        String estado = leerEstado(sc);
                        juego.setEstado(estado);

                        dao.actualizar(juego);
                        System.out.println("Juego modificado correctamente.");
                        break;
                    }

                    case 4: {
                        int idBaja = leerPositivo(sc, "Id del juego a eliminar: ");

                        Juego aBorrar = dao.buscarPorId(idBaja);

                        if (aBorrar == null) {
                            System.out.println("No existe ningún juego con ese id.");
                            break;
                        }

                        System.out.println("Vas a eliminar el juego: " + aBorrar.getNombre());
                        String respuesta = leerTexto(sc, "¿Seguro? (s/n): ");

                        if (respuesta.equalsIgnoreCase("s")) {
                            try {
                                dao.eliminar(idBaja);
                                System.out.println("Juego eliminado correctamente.");
                            } catch (SQLException e) {
                                // TODO : Mejor hacer una búsqueda de FKs y si tiene relaciones con otras tablas, pues q no se pueda borrar (antes de intentarlo)
                                System.out.println("No se pudo eliminar el juego. " +
                                        "Es posible que tenga partidas asociadas.");
                                System.out.println("Detalle: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                        break;
                    }

                    case 5:
                        menuBusquedaJuegos(sc);
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
                opcion = -1;
            }
        }
        while (opcion != 0);
    }

    // Menú de socias
    private static void menuSocias(Scanner sc) {

        SociaDao dao = new SociaDaoImpl();

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE SOCIAS =====");
            System.out.println("1. Listar socias");
            System.out.println("2. Consultar juegos y partidas de una socia");
            System.out.println("3. Dar de alta una socia");
            System.out.println("4. Modificar una socia");
            System.out.println("5. Dar de baja una socia");
            System.out.println("6. Buscar socias");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");


            try {
                opcion = leerInt(sc);

                switch (opcion) {
                    case 1: {
                        List<Socia> socias = dao.listar();

                        System.out.println("Número total de socias: " + socias.size());

                        if (socias.isEmpty()) {
                            System.out.println("No hay socias en la base de datos.");
                        } else {
                            for (Socia s : socias) {
                                System.out.println("\nDocumento: " + s.getDocumento());
                                System.out.println("Nombre: " + s.getNombre());
                                System.out.println("Apellidos: " + s.getApellido1() + " " +
                                        (s.getApellido2() != null ? s.getApellido2() : "(No tiene segundo apellido)"));
                                System.out.println("Teléfono: " + s.getTelefono());
                                System.out.println("Edad: " + s.getEdad());
                                System.out.println("Email: " + s.getEmail());
                                System.out.println("----------------------------");
                            }
                        }
                        break;
                    }

                    case 2: {
                        String documentoConsulta = leerDocumento(sc);

                        Socia consulta = dao.buscarDocumento(documentoConsulta);

                        if (consulta == null) {
                            System.out.println("No existe ninguna socia con ese documento.");
                            break;
                        }

                        System.out.println("Socia: " + consulta.getNombre() + " " + consulta.getApellido1());

                        System.out.println("--- Juegos de la socia ---");
                        boolean tieneJuegos = false;
                        JuegoDao juegoDao = new JuegoDaoImpl();
                        List<Juego> todosLosJuegos = juegoDao.listar();

                        if (!todosLosJuegos.isEmpty()) {
                            for (Juego j : todosLosJuegos) {
                                if (j.getSociaPropietaria() != null) {
                                    if (j.getSociaPropietaria().getDocumento().equals(documentoConsulta)) {
                                        System.out.println(j.getId() + " - " + j.getNombre() + " (" + j.getEstado() + ")");
                                        tieneJuegos = true;
                                    }
                                }
                            }
                        }

                        if (!tieneJuegos) {
                            System.out.println("No tiene juegos registrados.");
                        }

                        System.out.println("--- Partidas de la socia ---");
                        boolean tienePartidas = false;
                        PartidaDao partidaDao = new PartidaDaoImpl();
                        List<Partida> todasLasPartidas = partidaDao.listar();

                        if (todasLasPartidas.isEmpty()) {
                            System.out.println("No hay partidas registradas en la base de datos.");
                        } else {
                            for (Partida p : todasLasPartidas) {
                                for (Socia jugadora : p.getListaJugadorasSocias()) {
                                    if (jugadora.getDocumento().equals(documentoConsulta)) {
                                        System.out.println("Partida " + p.getId() + " - " +
                                                p.getFecha() + " a las " + p.getHora());
                                        tienePartidas = true;
                                    }
                                }
                            }
                        }

                        if (!tienePartidas) {
                            System.out.println("No está inscrita en ninguna partida.");
                        }
                        break;
                    }

                    case 3: {
                        Socia nueva = new Socia();

                        String documento;

                        Socia existente;

                        do {
                            documento = leerDocumento(sc);
                            existente = dao.buscarDocumento(documento);

                            if (existente != null) {
                                System.out.println("Ya existe una socia con ese documento.");
                            }

                        } while (existente != null);

                        nueva.setDocumento(documento);

                        nueva.setNombre(leerTexto(sc, "Nombre: "));

                        nueva.setApellido1(leerTexto(sc, "Primer apellido: "));

                        System.out.println("Segundo apellido (deja vacío si no tiene): ");
                        String apellido2 = sc.nextLine().trim();
                        if (apellido2.isEmpty()) {
                            nueva.setApellido2(null);
                        } else {
                            nueva.setApellido2(apellido2);
                        }

                        nueva.setTelefono(leerTelefono(sc));

                        nueva.setEdad(leerPositivo(sc, "Edad: "));

                        nueva.setEmail(leerEmail(sc));

                        dao.insertar(nueva);
                        System.out.println("Socia dada de alta correctamente.");
                        break;
                    }

                    case 4: {
                        String documentoModificar = leerDocumento(sc);

                        Socia socia = dao.buscarDocumento(documentoModificar);

                        if (socia == null) {
                            System.out.println("No existe ninguna socia con ese documento.");
                            break;
                        }

                        System.out.println("Escribe los nuevos datos de la socia");

                        socia.setNombre(leerTexto(sc, "Nombre: "));

                        socia.setApellido1(leerTexto(sc, "Primer apellido: "));

                        System.out.println("Segundo apellido (deja vacío si no tiene): ");
                        String apellido2Nuevo = sc.nextLine().trim();
                        if (apellido2Nuevo.isEmpty()) {
                            socia.setApellido2(null);
                        } else {
                            socia.setApellido2(apellido2Nuevo);
                        }

                        socia.setTelefono(leerTelefono(sc));

                        socia.setEdad(leerPositivo(sc, "Edad: "));

                        socia.setEmail(leerEmail(sc));

                        dao.actualizar(socia);
                        System.out.println("Socia modificada correctamente.");
                        break;
                    }

                    case 5: {
                        String documentoBaja = leerDocumento(sc);

                        Socia aBorrar = dao.buscarDocumento(documentoBaja);

                        if (aBorrar == null) {
                            System.out.println("No existe ninguna socia con ese documento.");
                            break;
                        }

                        System.out.println("Vas a eliminar a: " + aBorrar.getNombre() + " " + aBorrar.getApellido1());
                        String respuesta = leerTexto(sc, "¿Seguro? (s/n): ");

                        if (respuesta.equalsIgnoreCase("s")) {
                            dao.eliminar(documentoBaja);
                            System.out.println("Socia eliminada correctamente.");
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                        break;
                    }

                    case 6:
                        menuBusquedaSocias(sc);
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
                opcion = -1;
            }
        }
        while (opcion != 0);
    }

    // Menú de partidas
    private static void menuPartidas(Scanner sc) {

        PartidaDao dao = new PartidaDaoImpl();
        JuegoDao juegoDao = new JuegoDaoImpl();

        int opcion;

        do {

            System.out.println("\n\n===== GESTIÓN DE PARTIDAS =====");
            System.out.println("1. Listar partidas");
            System.out.println("2. Dar de alta una partida");
            System.out.println("3. Modificar una partida");
            System.out.println("4. Dar de baja una partida");
            System.out.println("5. Buscar partidas");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            try {
                opcion = leerInt(sc);

                switch (opcion) {
                    case 1: {
                        List<Partida> partidas = dao.listar();

                        System.out.println("Número total de partidas: " + partidas.size());

                        if (partidas.isEmpty()) {
                            System.out.println("No hay partidas en la base de datos.");
                        } else {
                            for (Partida p : partidas) {
                                System.out.println("\nId: " + p.getId());

                                // TODO : No muestra el ID del juego
                                if (p.getJuego() != null) {
                                    System.out.println("Id del juego: " + p.getJuego().getId());
                                }
                                System.out.println("Fecha: " + p.getFecha());
                                System.out.println("Hora: " + p.getHora());
                                System.out.println("Jugadores deseados: " + p.getJugadoresDeseados());
                                System.out.println("Plazas libres: " + p.getPlazasLibres());
                                System.out.println("----------------------------");
                            }
                        }
                        break;
                    }

                    case 2: {
                        int idJuego = leerPositivo(sc, "Id del juego de la partida: ");

                        Juego juego = juegoDao.buscarPorId(idJuego);

                        if (juego == null) {
                            System.out.println("No existe ningún juego con ese id.");
                            break;
                        }

                        Partida nueva = new Partida();
                        nueva.setJuego(juego);

                        nueva.setFecha(leerFechaFutura(sc, "Fecha de la partida (aaaa-mm-dd): "));

                        nueva.setHora(leerHora(sc, "Hora de la partida (hh:mm): "));

                        int deseados = leerNumeroEnRango(sc,
                                "Jugadores deseados (entre " + juego.getMinJugadores() +
                                        " y " + juego.getMaxJugadores() + "): ",
                                juego.getMinJugadores(),
                                juego.getMaxJugadores());

                        nueva.setJugadoresDeseados(deseados);
                        nueva.setPlazasLibres(deseados);

                        int idNueva = dao.insertar(nueva);
                        System.out.println("Partida dada de alta con id " + idNueva + ".");
                        break;
                    }

                    case 3: {
                        int idModificar = leerPositivo(sc, "Id de la partida a modificar: ");

                        Partida partida = dao.buscarporId(idModificar);

                        if (partida == null) {
                            System.out.println("No existe ninguna partida con ese id.");
                            break;
                        }

                        System.out.println("Escribe los nuevos datos de la partida");

                        partida.setFecha(leerFechaFutura(sc, "Fecha (aaaa-mm-dd): "));

                        partida.setHora(leerHora(sc, "Hora (hh:mm): "));

                        Juego juegoPartida = juegoDao.buscarPorId(partida.getJuego().getId());

                        if (juegoPartida == null) {
                            System.out.println("No se encuentra el juego de la partida.");
                            break;
                        }

                        int deseadosNuevos = leerNumeroEnRango(sc,
                                "Jugadores deseados (entre " + juegoPartida.getMinJugadores() + " y " +
                                        juegoPartida.getMaxJugadores() + "): ",
                                juegoPartida.getMinJugadores(),
                                juegoPartida.getMaxJugadores());

                        // TODO : Revisar esto, para comprobar otros posibles límites relacionados con las jugadoras ya inscritas
                        int diferencia = deseadosNuevos - partida.getJugadoresDeseados();
                        int plazasNuevas = partida.getPlazasLibres() + diferencia;

                        if (plazasNuevas < 0) {
                            plazasNuevas = 0;
                        }

                        partida.setJugadoresDeseados(deseadosNuevos);
                        partida.setPlazasLibres(plazasNuevas);

                        dao.actualizar(partida);
                        System.out.println("Partida modificada correctamente.");
                        break;
                    }

                    case 4: {
                        int idBaja = leerPositivo(sc, "Id de la partida a dar de baja: ");

                        Partida aBorrar = dao.buscarporId(idBaja);

                        if (aBorrar == null) {
                            System.out.println("No existe ninguna partida con ese id.");
                            break;
                        }

                        System.out.println("Vas a eliminar la partida " + aBorrar.getId() + " del " +
                                aBorrar.getFecha() + " a las " + aBorrar.getHora());
                        String respuesta = leerTexto(sc, "¿Seguro? (s/n): ");

                        if (respuesta.equalsIgnoreCase("s")) {
                            try {
                                dao.eliminar(idBaja);
                                System.out.println("Partida eliminada correctamente.");
                            } catch (SQLException e) {
                                System.out.println("No se pudo eliminar la partida. " +
                                        "Es posible que tenga jugadoras inscritas.");
                                System.out.println("Detalle: " + e.getMessage());
                            }
                        } else {
                            System.out.println("Operación cancelada.");
                        }
                        break;
                    }

                    case 5:
                        menuBusquedaPartidas(sc);
                        break;

                    case 0:
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }
            } catch (SQLException e) {
                System.out.println("Error en la base de datos: " + e.getMessage());
                opcion = -1;
            }
        }
        while (opcion != 0);
    }

    // Submenú de búsqueda de juegos
    private static void menuBusquedaJuegos(Scanner sc) {

        JuegoDao dao = new JuegoDaoImpl();
        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE JUEGOS =====");
            System.out.println("1. Buscar por nombre");
            System.out.println("2. Buscar por temática o categoría");
            System.out.println("3. Buscar por número de jugadores");
            System.out.println("0. Volver al menú de juegos");
            System.out.print("\nElige una opción: ");

            try {
                opcion = leerInt(sc);

                switch (opcion) {

                    case 1: {
                        String nombre = leerTexto(sc, "Introduce el nombre del juego: ");

                        List<Juego> resultados = dao.buscarPorNombre(nombre);
                        mostrarResultadosJuegos(resultados);
                        break;
                    }

                    case 2: {
                        String tematica = leerTexto(sc, "Introduce la temática del juego: ");

                        List<Juego> resultados = dao.buscarPorTematica(tematica);
                        mostrarResultadosJuegos(resultados);
                        break;
                    }

                    case 3: {
                        int numero = leerPositivo(sc, "Introduce el número de jugadores: ");

                        List<Juego> resultados = dao.buscarPorNumeroJugadores(numero);
                        mostrarResultadosJuegos(resultados);
                        break;
                    }

                    case 0:
                        break;

                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (SQLException e) {
                System.out.println("Error al buscar juegos: " + e.getMessage());
                opcion = -1;
            }

        } while (opcion != 0);
    }

    // Mostrar los resultados de una búsqueda
    private static void mostrarResultadosJuegos(List<Juego> juegos) {

        System.out.println("\nResultados encontrados: " + juegos.size());

        if (juegos.isEmpty()) {
            System.out.println("No se han encontrado juegos.");
            return;
        }

        for (Juego j : juegos) {

            System.out.println("\nId: " + j.getId());
            System.out.println("Nombre: " + j.getNombre());
            System.out.println("Autor 1: " + j.getAutor1());
            System.out.println("Autor 2: " + (j.getAutor2() != null ? j.getAutor2() : "(No tiene segundo autor)"));
            System.out.println("Descripción: " + j.getDescripcion());
            System.out.println("Jugadores: " + j.getMinJugadores() + " - " + j.getMaxJugadores());
            System.out.println("Duración: " + j.getDuracion() + " minutos");
            System.out.println("Temática: " + j.getTematica());

            if (j.isCompetitivo()) {
                System.out.println("Tipo: Competitivo");
            } else {
                System.out.println("Tipo: Cooperativo");
            }

            System.out.println("Ubicación: " + j.getUbicacion());
            System.out.println("Estado: " + j.getEstado());

            if (j.getAsociacionPropietaria() != null) {
                System.out.println("Propietaria: Asociación llamada '"
                        + j.getAsociacionPropietaria().getNombre() + "'");

            } else if (j.getSociaPropietaria() != null) {
                System.out.println("Propietaria: Socia con DNI '"
                        + j.getSociaPropietaria().getDocumento() + "'");
            }

            System.out.println("Fecha de alta: " + j.getFechaAlta());
            System.out.println("----------------------------");
        }
    }

    // Submenú de búsqueda de socias
    private static void menuBusquedaSocias(Scanner sc) {

        SociaDao dao = new SociaDaoImpl();
        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE SOCIAS =====");
            System.out.println("1. Buscar por nombre");
            System.out.println("2. Buscar por primer apellido");
            System.out.println("3. Buscar por segundo apellido");
            System.out.println("0. Volver al menú de socias");
            System.out.print("\nElige una opción: ");

            try {
                opcion = leerInt(sc);

                switch (opcion) {
                    case 1: {
                        String nombre = leerTexto(sc, "Introduce el nombre de la socia: ");

                        List<Socia> resultados = dao.buscarPorNombre(nombre);
                        mostrarResultadosSocias(resultados);
                        break;
                    }
                    case 2: {
                        String apellido = leerTexto(sc, "Introduce el primer apellido de la socia: ");

                        List<Socia> resultados = dao.buscarPorPrimerApellido(apellido);
                        mostrarResultadosSocias(resultados);
                        break;
                    }
                    case 3: {
                        String apellido = leerTexto(sc, "Introduce el segundo apellido de la socia: ");

                        List<Socia> resultados = dao.buscarPorSegundoApellido(apellido);
                        mostrarResultadosSocias(resultados);
                        break;
                    }
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (SQLException e) {
                System.out.println("Error al buscar socias: " + e.getMessage());
                opcion = -1;
            }

        } while (opcion != 0);
    }

    private static void mostrarResultadosSocias(List<Socia> socias) {

        System.out.println("\nResultados encontrados: " + socias.size());

        if (socias.isEmpty()) {
            System.out.println("No se han encontrado socias.");
            return;
        }

        for (Socia s : socias) {
            System.out.println("\nDocumento: " + s.getDocumento());
            System.out.println("Nombre: " + s.getNombre());
            System.out.println("Primer apellido: " + s.getApellido1());
            System.out.println("Segundo apellido: " +
                    (s.getApellido2() != null ? s.getApellido2() : "(No tiene segundo apellido)"));
            System.out.println("Teléfono: " + s.getTelefono());
            System.out.println("Edad: " + s.getEdad());
            System.out.println("Email: " + s.getEmail());
            System.out.println("----------------------------");
        }
    }

    // Submenú de búsqueda de partidas
    private static void menuBusquedaPartidas(Scanner sc) {

        PartidaDao dao = new PartidaDaoImpl();
        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE PARTIDAS =====");
            System.out.println("1. Buscar por juego");
            System.out.println("2. Buscar por fecha");
            System.out.println("0. Volver al menú de partidas");
            System.out.print("\nElige una opción: ");

            try {
                opcion = leerInt(sc);

                switch (opcion) {
                    case 1: {
                        String nombreJuego = leerTexto(sc, "Introduce el nombre del juego: ");

                        List<Partida> resultados = dao.buscarPorJuego(nombreJuego);
                        mostrarResultadosPartidas(resultados);
                        break;
                    }
                    case 2: {
                        LocalDate fecha = leerFecha(sc, "Introduce la fecha (AAAA-MM-DD): ");

                        List<Partida> resultados = dao.buscarPorFecha(fecha);
                        mostrarResultadosPartidas(resultados);
                        break;
                    }
                    case 0:
                        break;
                    default:
                        System.out.println("Opción no válida.");
                }

            } catch (SQLException e) {
                System.out.println("Error al buscar partidas: " + e.getMessage());
                opcion = -1;
            }

        } while (opcion != 0);
    }

    private static void mostrarResultadosPartidas(List<Partida> partidas) {

        System.out.println("\nResultados encontrados: " + partidas.size());

        if (partidas.isEmpty()) {
            System.out.println("No se han encontrado partidas.");
            return;
        }

        for (Partida p : partidas) {
            System.out.println("\nId: " + p.getId());

            if (p.getJuego() != null) {
                System.out.println("Juego: " + p.getJuego().getNombre());
            }

            System.out.println("Fecha: " + p.getFecha());
            System.out.println("Hora: " + p.getHora());
            System.out.println("Jugadores deseados: " + p.getJugadoresDeseados());
            System.out.println("Plazas libres: " + p.getPlazasLibres());
            System.out.println("----------------------------");
        }
    }

    // Menú de exportaciones
    private static void menuExportaciones(Scanner sc) throws Exception {

        int opcion;

        do {

            System.out.println("\n===== EXPORTAR DATOS =====");
            System.out.println("1. Exportar juegos a XML");
            System.out.println("2. Exportar juegos a JSON");
            System.out.println("3. Exportar socias a XML");
            System.out.println("4. Exportar socias a JSON");
            System.out.println("5. Exportar partidas a XML");
            System.out.println("6. Exportar partidas a JSON");
            System.out.println("7. Exportar todo a XML");
            System.out.println("8. Exportar todo a JSON");
            System.out.println("9. Exportar todo a XML y JSON");
            System.out.println("0. Volver al menú principal");
            System.out.print("\nElige una opción: ");

            opcion = leerInt(sc);

            switch (opcion) {
                case 1:
                    ExportarDatos.exportarJuegosXML();
                    break;
                case 2:
                    ExportarDatos.exportarJuegosJSON();
                    break;
                case 3:
                    ExportarDatos.exportarSociasXML();
                    break;
                case 4:
                    ExportarDatos.exportarSociasJSON();
                    break;
                case 5:
                    ExportarDatos.exportarPartidasXML();
                    break;
                case 6:
                    ExportarDatos.exportarPartidasJSON();
                    break;
                case 7:
                    ExportarDatos.exportarJuegosXML();
                    ExportarDatos.exportarSociasXML();
                    ExportarDatos.exportarPartidasXML();
                    break;
                case 8:
                    ExportarDatos.exportarJuegosJSON();
                    ExportarDatos.exportarSociasJSON();
                    ExportarDatos.exportarPartidasJSON();
                    break;
                case 9:
                    ExportarDatos.exportarJuegosXML();
                    ExportarDatos.exportarSociasXML();
                    ExportarDatos.exportarPartidasXML();
                    ExportarDatos.exportarJuegosJSON();
                    ExportarDatos.exportarSociasJSON();
                    ExportarDatos.exportarPartidasJSON();
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
                    break;
            }

            if (opcion >= 1 && opcion <= 9) {
                System.out.println("Exportación finalizada.\n");
            }
        }
        while (opcion != 0);
    }

    /* ----- Funciones auxiliares ----- */

    // Comprobar que el dato es numérico
    public static int leerInt(Scanner sc) {
        while (!sc.hasNextInt()) {
            System.out.println("Error: debes introducir un número.");
            sc.nextLine();
            System.out.print("Vuelve a intentarlo: ");
        }

        int numero = sc.nextInt();
        sc.nextLine();

        return numero;
    }

    // Leer un número entero mayor que 0
    public static int leerPositivo(Scanner sc, String mensaje) {
        int numero;

        do {
            System.out.print(mensaje);
            numero = leerInt(sc);

            if (numero <= 0) {
                System.out.println("Error: el número debe ser mayor que 0.");
            }

        } while (numero <= 0);

        return numero;
    }

    // Comprobar que se ha proporcionado un texto
    public static String leerTexto(Scanner sc, String mensaje) {

        String texto;

        do {
            System.out.print(mensaje);
            texto = sc.nextLine();

            if (texto.trim().isEmpty()) {
                System.out.println("Error: el campo no puede estar vacío.");
            }

        } while (texto.trim().isEmpty());

        return texto;
    }

    // Leer una fecha válida
    public static LocalDate leerFecha(Scanner sc, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return LocalDate.parse(sc.nextLine());

            } catch (DateTimeParseException e) {
                System.out.println("Error: introduce una fecha válida (AAAA-MM-DD).");
            }
        }
    }

    // Leer una fecha válida y que no sea pasada
    public static LocalDate leerFechaFutura(Scanner sc, String mensaje) {
        while (true) {
            LocalDate fecha = leerFecha(sc, mensaje);

            if (fecha.isBefore(LocalDate.now())) {
                System.out.println("Error: la fecha no puede ser anterior a hoy.");
            } else {
                return fecha;
            }
        }
    }

    // Leer una hora válida
    public static LocalTime leerHora(Scanner sc, String mensaje) {

        while (true) {
            try {
                System.out.print(mensaje);
                return LocalTime.parse(sc.nextLine());

            } catch (DateTimeParseException e) {
                System.out.println("Error: introduce una hora válida (HH:mm).");
            }
        }
    }

    // Leer el número mínimo y máximo de jugadores
    public static int[] leerJugadores(Scanner sc) {
        int min;
        int max;

        do {
            min = leerPositivo(sc, "Número mínimo de jugadores: ");
            max = leerPositivo(sc, "Número máximo de jugadores: ");

            if (max < min) {
                System.out.println("El máximo no puede ser menor que el mínimo.");
            }

        } while (max < min);

        return new int[]{min, max};
    }

    // Leer una opción que solo puede ser 0 o 1
    public static boolean leerCompetitivo(Scanner sc) {
        int opcion;

        do {
            System.out.println("Competitivo = 1 | Cooperativo = 0");
            System.out.print("Escoge un número: ");
            opcion = leerInt(sc);

            if (opcion != 0 && opcion != 1) {
                System.out.println("Opción incorrecta. Introduce 0 o 1.");
            }

        } while (opcion != 0 && opcion != 1);

        return opcion == 1;
    }

    // Leer un número entero dentro de un rango
    public static int leerNumeroEnRango(Scanner sc, String mensaje, int min, int max) {
        int numero;

        do {
            System.out.print(mensaje);
            numero = leerInt(sc);

            if (numero < min || numero > max) {
                System.out.println("Error: introduce un número entre " + min + " y " + max + ".");
            }

        } while (numero < min || numero > max);

        return numero;
    }

    // Leer una ubicación válida para el juego
    public static String leerUbicacion(Scanner sc) {
        int ubicacion;

        do {
            System.out.println("Estantería 1 (>12 años)");
            System.out.println("Estantería 2 (5-11 años)");
            System.out.println("Estantería 3 (Infantil)");
            System.out.print("Escoge la estantería: ");

            ubicacion = leerInt(sc);

            if (ubicacion < 1 || ubicacion > 3) {
                System.out.println("Error: escoge una estantería entre 1 y 3.");
            }

        } while (ubicacion < 1 || ubicacion > 3);

        if (ubicacion == 1) {
            return "Estantería 1 (>12 años)";
        } else if (ubicacion == 2) {
            return "Estantería 2 (5-11 años)";
        } else {
            return "Estantería 3 (Infantil)";
        }
    }

    // Leer un estado válido para el juego
    public static String leerEstado(Scanner sc) {
        int estado;

        do {
            System.out.println("1. Disponible");
            System.out.println("2. Prestado");
            System.out.println("3. En reparación");
            System.out.print("Escoge el estado: ");

            estado = leerInt(sc);

            if (estado < 1 || estado > 3) {
                System.out.println("Error: escoge un estado entre 1 y 3.");
            }

        } while (estado < 1 || estado > 3);

        if (estado == 1) {
            return "disponible";
        } else if (estado == 2) {
            return "prestado";
        } else {
            return "en reparación";
        }
    }

    public static String leerDocumento(Scanner sc) {

        while (true) {

            System.out.print("Documento (DNI/NIE/Pasaporte): ");
            String documento = sc.nextLine().trim().toUpperCase();

            // DNI: 8 números y una letra
            boolean formatoDni = documento.matches("\\d{8}[A-Z]");

            // NIE: X, Y o Z + 7 números + una letra
            boolean formatoNie = documento.matches("[XYZ]\\d{7}[A-Z]");

            // Pasaporte: formato genérico de 6 a 12 caracteres alfanuméricos
            boolean formatoPasaporte = documento.matches("[A-Z0-9]{6,12}") && !formatoDni && !formatoNie;

            if (formatoDni || formatoNie || formatoPasaporte) {
                return documento;
            }

            System.out.println("Error: documento incorrecto. Introduce un DNI, NIE o pasaporte válido.");
        }
    }

    public static String leerEmail(Scanner sc) {

        while (true) {

            System.out.print("Email: ");
            String email = sc.nextLine();

            // Debe ser equivalente a una cantidad mayor que 1 de caracteres alfanuméricos, un "@" exactamente, y otra serie de caracteres alfanuméricos
            if (email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
                return email;
            }

            System.out.println("Error: email incorrecto.");
        }
    }

    public static String leerTelefono(Scanner sc) {

        while (true) {

            System.out.print("Teclea el teléfono: ");
            String telefono = sc.nextLine();

            // Debe ser equivalente a 9 números
            if (telefono.matches("\\d{9}")) {
                return telefono;
            }

            System.out.println("Error: el teléfono debe tener 9 números.");
        }
    }
}