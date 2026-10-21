package org.egibide;

import org.egibide.dao.JuegoDaoImpl;
import org.egibide.idao.JuegoDao;
import org.egibide.models.Juego;
import org.egibide.utils.DatabaseConnection;
import org.egibide.utils.ExportarDatos;
import org.egibide.utils.MetadatosBD;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.models.Asociacion;
import org.egibide.models.Partida;
import org.egibide.models.Socia;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import java.util.List;
import java.util.Scanner;

// TODO : Hay que implementar lo que aparezca como "Pendiente"

// TODO : No dejar esta clase con demasiadas líneas; hacer funciones y separar código en diferentes ficheros y carpetas

public class App {
    public static void main(String[] args) throws SQLException {

        DatabaseConnection dbc = DatabaseConnection.getInstance();

        try (Scanner sc = new Scanner(System.in)) {

            Connection conexion = dbc.getConnection();

            System.out.println("Conexión correcta.\n");

            // Mostrar los datos de la asociación al iniciar la aplicación
            mostrarDatosAsociacion(conexion);

            // Menú principal
            int opcion;

            do {

                System.out.println("\n===== MENÚ PRINCIPAL =====");
                System.out.println("1. Gestión de juegos");
                System.out.println("2. Gestión de socias");
                System.out.println("3. Gestión de partidas");
                System.out.println("4. Exportar datos XML y JSON");
                System.out.println("5. Metadatos de la BBDD");
                System.out.println("0. Salir");
                System.out.print("\nElige una opción: ");

                // TODO : En todos los "Integer.parseInt" poner mi función de leerInt y añadir más de esas (así habrá más control de errores)
                opcion = Integer.parseInt(sc.nextLine());

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

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
            e.printStackTrace();
        }
    }

    // Mostrar los datos principales de la asociación
    private static void mostrarDatosAsociacion(Connection conexion) throws Exception {

        // Con esta consulta se asegura que solo obtenga datos de 1 asociación (aun así en la BD solo habrá 1 guardada)
        String sql = "SELECT * FROM asociacion LIMIT 1";

        try (PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            if (rs.next()) {
                System.out.println("\n==================================");
                System.out.println("   " + rs.getString("nombre"));
                System.out.println("==================================");
                System.out.println("Razón social: "
                        + rs.getString("razon_social"));
                System.out.println("Fecha de apertura: "
                        + rs.getDate("fecha_apertura"));
                System.out.println("Dirección: "
                        + rs.getString("direccion"));
                System.out.println("Teléfono: "
                        + rs.getString("telefono"));
                System.out.println("Email: "
                        + rs.getString("email"));
                System.out.println("CIF: "
                        + rs.getString("cif"));
            } else {
                System.out.println("No hay datos de la asociación.");
            }
        }
    }

    // Menú de juegos
    private static void menuJuegos(Scanner sc) {

        System.out.println("\n===== GESTIÓN DE JUEGOS =====");
        System.out.println("1. Listar juegos");
        System.out.println("2. Dar de alta un juego");
        System.out.println("3. Modificar un juego");
        System.out.println("4. Dar de baja un juego");
        System.out.println("5. Buscar juegos");
        System.out.println("0. Volver al menú principal");
        System.out.print("\nElige una opción: ");

        JuegoDao dao = new JuegoDaoImpl();
        SociaDao sociaDao = new SociaDaoImpl();

        try {
            int opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1: {
                    List<Juego> juegos = dao.listar();

                    if (juegos.isEmpty()) {
                        // TODO : SALTA AQUÍ AUNQUE HAYA JUEGOS EN LA BD
                        System.out.println("No hay juegos en la base de datos.");
                    } else {
                        for (Juego j : juegos) {
                            System.out.println("Id: " + j.getId());
                            System.out.println("Nombre: " + j.getNombre());
                            System.out.println("Autor 1: " + j.getAutor1());
                            System.out.println("Autor 2: " + j.getAutor2());
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
                                System.out.println("Propietaria: la asociación");
                            } else if (j.getSociaPropietaria() != null) {
                                System.out.println("Propietaria: socia con DNI '" + j.getSociaPropietaria().getDocumento() + "'");
                            }

                            System.out.println("Fecha de alta: " + j.getFechaAlta());
                            System.out.println("----------------------------");
                        }
                    }
                    break;
                }

                case 2: {
                    Juego nuevo = new Juego();

                    System.out.println("Nombre del juego: ");
                    String nombre = sc.nextLine();
                    nuevo.setNombre(nombre);

                    System.out.println("Primer autor del juego: ");
                    String autor1 = sc.nextLine();
                    nuevo.setAutor1(autor1);

                    System.out.println("Segundo autor del juego (deja vacío si no hay): ");
                    String autor2 = sc.nextLine();
                    if (autor2.isEmpty()) {
                        nuevo.setAutor2(null);
                    } else {
                        nuevo.setAutor2(autor2);
                    }

                    System.out.println("Descripción del juego: ");
                    String descripcion = sc.nextLine();
                    nuevo.setDescripcion(descripcion);

                    // TODO : Poner bucle para que en el if de abajo, si da mensaje de error, que se vuelvan a preguntar los números
                    System.out.println("Número mínimo de jugadores: ");
                    int minJugadores = Integer.parseInt(sc.nextLine());

                    System.out.println("Número máximo de jugadores: ");
                    int maxJugadores = Integer.parseInt(sc.nextLine());

                    if (maxJugadores < minJugadores) {
                        System.out.println("El máximo no puede ser menor que el mínimo. No se ha dado de alta el juego.");
                        break;
                    }
                    nuevo.setMinJugadores(minJugadores);
                    nuevo.setMaxJugadores(maxJugadores);

                    System.out.println("Duración del juego (minutos): ");
                    int duracion = Integer.parseInt(sc.nextLine());
                    nuevo.setDuracion(duracion);

                    System.out.println("Temática del juego: ");
                    String tematica = sc.nextLine();
                    nuevo.setTematica(tematica);

                    System.out.println("Competitivo = 1 | Cooperativo = 0");
                    System.out.println("Escoge un número: ");
                    int competitivo = Integer.parseInt(sc.nextLine());
                    if (competitivo == 1) {
                        nuevo.setCompetitivo(true);
                    } else {
                        nuevo.setCompetitivo(false);
                    }

                    System.out.println("Estantería 1 (>12 años)");
                    System.out.println("Estantería 2 (5-11 años)");
                    System.out.println("Estantería 3 (Infantil)");
                    System.out.println("Escoge la estantería: ");
                    int ubicacion = Integer.parseInt(sc.nextLine());

                    if (ubicacion == 1) {
                        nuevo.setUbicacion("Estantería 1 (>12 años)");
                    } else if (ubicacion == 2) {
                        nuevo.setUbicacion("Estantería 2 (5-11 años)");
                    } else if (ubicacion == 3) {
                        nuevo.setUbicacion("Estantería 3 (Infantil)");
                    } else {
                        System.out.println("Estantería no válida. No se ha dado de alta el juego.");
                        break;
                    }

                    nuevo.setEstado("disponible");
                    nuevo.setFechaAlta(LocalDate.now());

                    System.out.println("Propietaria del juego: 1 = Asociación | 2 = Socia");
                    System.out.println("Escoge un número: ");
                    int propietaria = Integer.parseInt(sc.nextLine());

                    if (propietaria == 1) {
                        // TODO : Tiene q ser la asociación existente (solo va a existir en todo momento 1 en la BD). Habrá q hacer una consulta select.
                        Asociacion asociacion = new Asociacion();
                        asociacion.setId(1);
                        nuevo.setAsociacionPropietaria(asociacion);
                    } else if (propietaria == 2) {
                        System.out.println("Documento de la socia propietaria: ");
                        String documento = sc.nextLine();
                        Socia socia = sociaDao.buscarDocumento(documento);

                        if (socia == null) {
                            System.out.println("No existe ninguna socia con ese documento. No se ha dado de alta el juego.");
                            break;
                        }
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
                    System.out.println("Id del juego a modificar: ");
                    int idModificar = Integer.parseInt(sc.nextLine());

                    Juego juego = dao.buscarPorId(idModificar);

                    if (juego == null) {
                        System.out.println("No existe ningún juego con ese id.");
                        break;
                    }

                    System.out.println("Escribe los nuevos datos del juego");

                    // TODO : Poner lo de que ya muestre los datos y tal, pero q esta vez los que aparezcan se puedan modificar tal cual; no sobreescribir
                    System.out.println("Nombre: ");
                    juego.setNombre(sc.nextLine());

                    System.out.println("Primer autor: ");
                    juego.setAutor1(sc.nextLine());

                    System.out.println("Segundo autor (deja vacío si no hay): ");
                    String autor2Nuevo = sc.nextLine();
                    if (autor2Nuevo.isEmpty()) {
                        juego.setAutor2(null);
                    } else {
                        juego.setAutor2(autor2Nuevo);
                    }

                    System.out.println("Descripción: ");
                    juego.setDescripcion(sc.nextLine());

                    System.out.println("Número mínimo de jugadores: ");
                    int minNuevo = Integer.parseInt(sc.nextLine());

                    System.out.println("Número máximo de jugadores: ");
                    int maxNuevo = Integer.parseInt(sc.nextLine());

                    if (maxNuevo < minNuevo) {
                        System.out.println("El máximo no puede ser menor que el mínimo. No se ha modificado el juego.");
                        break;
                    }
                    juego.setMinJugadores(minNuevo);
                    juego.setMaxJugadores(maxNuevo);

                    System.out.println("Duración (minutos): ");
                    juego.setDuracion(Integer.parseInt(sc.nextLine()));

                    System.out.println("Temática: ");
                    juego.setTematica(sc.nextLine());

                    System.out.println("Competitivo = 1 | Cooperativo = 0");
                    System.out.println("Escoge un número: ");
                    int competitivoNuevo = Integer.parseInt(sc.nextLine());
                    if (competitivoNuevo == 1) {
                        juego.setCompetitivo(true);
                    } else {
                        juego.setCompetitivo(false);
                    }

                    System.out.println("Estantería 1 (>12 años)");
                    System.out.println("Estantería 2 (5-11 años)");
                    System.out.println("Estantería 3 (Infantil)");
                    System.out.println("Escoge la estantería: ");
                    int ubicacionNueva = Integer.parseInt(sc.nextLine());

                    if (ubicacionNueva == 1) {
                        juego.setUbicacion("Estantería 1 (>12 años)");
                    } else if (ubicacionNueva == 2) {
                        juego.setUbicacion("Estantería 2 (5-11 años)");
                    } else if (ubicacionNueva == 3) {
                        juego.setUbicacion("Estantería 3 (Infantil)");
                    } else {
                        System.out.println("Estantería no válida. No se ha modificado el juego.");
                        break;
                    }

                    System.out.println("1. Disponible");
                    System.out.println("2. Prestado");
                    System.out.println("3. En reparación");
                    System.out.println("Escoge el estado: ");
                    int estadoNuevo = Integer.parseInt(sc.nextLine());

                    if (estadoNuevo == 1) {
                        juego.setEstado("disponible");
                    } else if (estadoNuevo == 2) {
                        juego.setEstado("prestado");
                    } else if (estadoNuevo == 3) {
                        juego.setEstado("en reparación");
                    } else {
                        System.out.println("Estado no válido. No se ha modificado el juego.");
                        break;
                    }

                    dao.actualizar(juego);
                    System.out.println("Juego modificado correctamente.");
                    break;
                }

                case 4: {
                    System.out.println("Id del juego a eliminar: ");
                    int idBaja = Integer.parseInt(sc.nextLine());

                    Juego aBorrar = dao.buscarPorId(idBaja);

                    if (aBorrar == null) {
                        System.out.println("No existe ningún juego con ese id.");
                        break;
                    }

                    System.out.println("Vas a eliminar el juego: " + aBorrar.getNombre());
                    System.out.println("¿Seguro? (s/n): ");
                    String respuesta = sc.nextLine();

                    if (respuesta.equalsIgnoreCase("s")) {
                        try {
                            dao.eliminar(idBaja);
                            System.out.println("Juego eliminado correctamente.");
                        } catch (SQLException e) {
                            // TODO : Mejor hacer una búsqueda de FKs y si tiene relaciones con otras tablas, pues q no se pueda borrar (antes de intentarlo)
                            System.out.println("No se pudo eliminar el juego. Es posible que tenga partidas asociadas.");
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
        } catch (NumberFormatException e) {
            System.out.println("Debes escribir un número entero válido.");
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // Menú de socias
    private static void menuSocias(Scanner sc) {

        System.out.println("\n===== GESTIÓN DE SOCIAS =====");
        System.out.println("1. Listar socias");
        System.out.println("2. Consultar juegos y partidas de una socia");
        System.out.println("3. Dar de alta una socia");
        System.out.println("4. Modificar una socia");
        System.out.println("5. Dar de baja una socia");
        System.out.println("6. Buscar socias");
        System.out.println("0. Volver al menú principal");
        System.out.print("\nElige una opción: ");

        SociaDao dao = new SociaDaoImpl();

        try {
            int opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1: {
                    // TODO : Antes de mostrar el listado, que muestre el número total de registros q hay en BD

                    List<Socia> socias = dao.listar();

                    if (socias.isEmpty()) {
                        System.out.println("No hay socias en la base de datos.");
                    } else {
                        for (Socia s : socias) {
                            System.out.println("\n");
                            System.out.println("Documento: " + s.getDocumento());
                            System.out.println("Nombre: " + s.getNombre());
                            System.out.println("Apellidos: " + s.getApellido1() + " " + s.getApellido2());
                            System.out.println("Teléfono: " + s.getTelefono());
                            System.out.println("Edad: " + s.getEdad());
                            System.out.println("Email: " + s.getEmail());
                            System.out.println("----------------------------");
                        }
                    }
                    break;
                }

                case 2: {
                    System.out.println("Documento de la socia: ");
                    String documentoConsulta = sc.nextLine();

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

                    for (Juego j : todosLosJuegos) {
                        if (j.getSociaPropietaria() != null) {
                            if (j.getSociaPropietaria().getDocumento().equals(documentoConsulta)) {
                                System.out.println(j.getId() + " - " + j.getNombre() + " (" + j.getEstado() + ")");
                                tieneJuegos = true;
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

                    for (Partida p : todasLasPartidas) {
                        for (Socia jugadora : p.getListaJugadorasSocias()) {
                            if (jugadora.getDocumento().equals(documentoConsulta)) {
                                System.out.println("Partida " + p.getId() + " - " + p.getFecha() + " a las " + p.getHora());
                                tienePartidas = true;
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

                    System.out.println("Documento (DNI/NIE/Pasaporte): ");
                    String documento = sc.nextLine();

                    if (dao.buscarDocumento(documento) != null) {
                        System.out.println("Ya existe una socia con ese documento.");
                        break;
                    }
                    nueva.setDocumento(documento);

                    System.out.println("Nombre: ");
                    nueva.setNombre(sc.nextLine());

                    System.out.println("Primer apellido: ");
                    nueva.setApellido1(sc.nextLine());

                    System.out.println("Segundo apellido (deja vacío si no tiene): ");
                    String apellido2 = sc.nextLine();
                    if (apellido2.isEmpty()) {
                        nueva.setApellido2(null);
                    } else {
                        nueva.setApellido2(apellido2);
                    }

                    System.out.println("Teléfono: ");
                    nueva.setTelefono(sc.nextLine());

                    System.out.println("Edad: ");
                    nueva.setEdad(Integer.parseInt(sc.nextLine()));

                    System.out.println("Email: ");
                    nueva.setEmail(sc.nextLine());

                    dao.insertar(nueva);
                    System.out.println("Socia dada de alta correctamente.");
                    break;
                }

                case 4: {
                    System.out.println("Documento de la socia a modificar: ");
                    String documentoModificar = sc.nextLine();

                    Socia socia = dao.buscarDocumento(documentoModificar);

                    if (socia == null) {
                        System.out.println("No existe ninguna socia con ese documento.");
                        break;
                    }

                    System.out.println("Escribe los nuevos datos de la socia");

                    System.out.println("Nombre: ");
                    socia.setNombre(sc.nextLine());

                    System.out.println("Primer apellido: ");
                    socia.setApellido1(sc.nextLine());

                    System.out.println("Segundo apellido (deja vacío si no tiene): ");
                    String apellido2Nuevo = sc.nextLine();
                    if (apellido2Nuevo.isEmpty()) {
                        socia.setApellido2(null);
                    } else {
                        socia.setApellido2(apellido2Nuevo);
                    }

                    System.out.println("Teléfono: ");
                    socia.setTelefono(sc.nextLine());

                    System.out.println("Edad: ");
                    socia.setEdad(Integer.parseInt(sc.nextLine()));

                    System.out.println("Email: ");
                    socia.setEmail(sc.nextLine());

                    dao.actualizar(socia);
                    System.out.println("Socia modificada correctamente.");
                    break;
                }

                case 5: {
                    System.out.println("Documento de la socia a dar de baja: ");
                    String documentoBaja = sc.nextLine();

                    Socia aBorrar = dao.buscarDocumento(documentoBaja);

                    if (aBorrar == null) {
                        System.out.println("No existe ninguna socia con ese documento.");
                        break;
                    }

                    System.out.println("Vas a eliminar a: " + aBorrar.getNombre() + " " + aBorrar.getApellido1());
                    System.out.println("¿Seguro? (s/n): ");
                    String respuesta = sc.nextLine();

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
        } catch (NumberFormatException e) {
            System.out.println("Debes escribir un número entero válido.");
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // Menú de partidas
    private static void menuPartidas(Scanner sc) {

        System.out.println("\n===== GESTIÓN DE PARTIDAS =====");
        System.out.println("1. Listar partidas");
        System.out.println("2. Dar de alta una partida");
        System.out.println("3. Modificar una partida");
        System.out.println("4. Dar de baja una partida");
        System.out.println("5. Buscar partidas");
        System.out.println("0. Volver al menú principal");
        System.out.print("\nElige una opción: ");

        PartidaDao dao = new PartidaDaoImpl();
        JuegoDao juegoDao = new JuegoDaoImpl();

        try {
            int opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1: {
                    List<Partida> partidas = dao.listar();

                    if (partidas.isEmpty()) {
                        System.out.println("No hay partidas en la base de datos.");
                    } else {
                        for (Partida p : partidas) {
                            System.out.println("Id: " + p.getId());
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
                    System.out.println("Id del juego de la partida: ");
                    int idJuego = Integer.parseInt(sc.nextLine());

                    Juego juego = juegoDao.buscarPorId(idJuego);

                    if (juego == null) {
                        System.out.println("No existe ningún juego con ese id.");
                        break;
                    }

                    Partida nueva = new Partida();
                    nueva.setJuego(juego);

                    System.out.println("Fecha de la partida (aaaa-mm-dd): ");
                    String textoFecha = sc.nextLine();
                    nueva.setFecha(LocalDate.parse(textoFecha));

                    System.out.println("Hora de la partida (hh:mm): ");
                    String textoHora = sc.nextLine();
                    nueva.setHora(LocalTime.parse(textoHora));

                    System.out.println("Jugadores deseados (entre " + juego.getMinJugadores() + " y " + juego.getMaxJugadores() + "): ");
                    int deseados = Integer.parseInt(sc.nextLine());

                    if (deseados < juego.getMinJugadores() || deseados > juego.getMaxJugadores()) {
                        System.out.println("Ese número está fuera del rango del juego. No se ha dado de alta la partida.");
                        break;
                    }

                    nueva.setJugadoresDeseados(deseados);
                    nueva.setPlazasLibres(deseados);

                    int idNueva = dao.insertar(nueva);
                    System.out.println("Partida dada de alta con id " + idNueva + ".");
                    break;
                }

                case 3: {
                    System.out.println("Id de la partida a modificar: ");
                    int idModificar = Integer.parseInt(sc.nextLine());

                    Partida partida = dao.buscarporId(idModificar);

                    if (partida == null) {
                        System.out.println("No existe ninguna partida con ese id.");
                        break;
                    }

                    System.out.println("Escribe los nuevos datos de la partida");

                    System.out.println("Fecha (aaaa-mm-dd): ");
                    String fechaNueva = sc.nextLine();
                    partida.setFecha(LocalDate.parse(fechaNueva));

                    System.out.println("Hora (hh:mm): ");
                    String horaNueva = sc.nextLine();
                    partida.setHora(LocalTime.parse(horaNueva));

                    Juego juegoPartida = juegoDao.buscarPorId(partida.getJuego().getId());

                    if (juegoPartida == null) {
                        System.out.println("No se encuentra el juego de la partida.");
                        break;
                    }

                    System.out.println("Jugadores deseados (entre " + juegoPartida.getMinJugadores() + " y " + juegoPartida.getMaxJugadores() + "): ");
                    int deseadosNuevos = Integer.parseInt(sc.nextLine());

                    if (deseadosNuevos < juegoPartida.getMinJugadores() || deseadosNuevos > juegoPartida.getMaxJugadores()) {
                        System.out.println("Ese número está fuera del rango del juego. No se ha modificado la partida.");
                        break;
                    }

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
                    System.out.println("Id de la partida a dar de baja: ");
                    int idBaja = Integer.parseInt(sc.nextLine());

                    Partida aBorrar = dao.buscarporId(idBaja);

                    if (aBorrar == null) {
                        System.out.println("No existe ninguna partida con ese id.");
                        break;
                    }

                    System.out.println("Vas a eliminar la partida " + aBorrar.getId() + " del " + aBorrar.getFecha() + " a las " + aBorrar.getHora());
                    System.out.println("¿Seguro? (s/n): ");
                    String respuesta = sc.nextLine();

                    if (respuesta.equalsIgnoreCase("s")) {
                        try {
                            dao.eliminar(idBaja);
                            System.out.println("Partida eliminada correctamente.");
                        } catch (SQLException e) {
                            System.out.println("No se pudo eliminar la partida. Es posible que tenga jugadoras inscritas.");
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
        } catch (NumberFormatException e) {
            System.out.println("Debes escribir un número entero válido.");
        } catch (DateTimeParseException e) {
            System.out.println("Fecha u hora con formato incorrecto. Ejemplos: 2026-11-15 y 18:30");
        } catch (SQLException e) {
            System.out.println("Error en la base de datos: " + e.getMessage());
        }
    }

    // Submenú de búsqueda de juegos
    private static void menuBusquedaJuegos(Scanner sc) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE JUEGOS =====");
            System.out.println("1. Buscar por nombre");
            System.out.println("2. Buscar por temática o categoría");
            System.out.println("3. Buscar por número de jugadores");
            System.out.println("0. Volver al menú de juegos");
            System.out.print("\nElige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println("Pendiente: buscar juegos por nombre.");
                    break;
                case 2:
                    System.out.println("Pendiente: buscar juegos por temática.");
                    break;
                case 3:
                    System.out.println("Pendiente: buscar juegos por número de jugadores.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Submenú de búsqueda de socias
    private static void menuBusquedaSocias(Scanner sc) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE SOCIAS =====");
            System.out.println("1. Buscar por nombre");
            System.out.println("2. Buscar por primer apellido");
            System.out.println("3. Buscar por segundo apellido");
            System.out.println("0. Volver al menú de socias");
            System.out.print("\nElige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println("Pendiente: buscar socias por nombre.");
                    break;
                case 2:
                    System.out.println("Pendiente: buscar socias por primer apellido.");
                    break;
                case 3:
                    System.out.println("Pendiente: buscar socias por segundo apellido.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Submenú de búsqueda de partidas
    private static void menuBusquedaPartidas(Scanner sc) {

        int opcion;

        do {
            System.out.println("\n===== BÚSQUEDA DE PARTIDAS =====");
            System.out.println("1. Buscar por juego");
            System.out.println("2. Buscar por fecha");
            System.out.println("0. Volver al menú de partidas");
            System.out.print("\nElige una opción: ");

            opcion = Integer.parseInt(sc.nextLine());

            switch (opcion) {
                case 1:
                    System.out.println("Pendiente: buscar partidas por juego.");
                    break;
                case 2:
                    System.out.println("Pendiente: buscar partidas por fecha.");
                    break;
                case 0:
                    break;
                default:
                    System.out.println("Opción no válida.");
            }

        } while (opcion != 0);
    }

    // Menú de exportaciones
    private static void menuExportaciones(Scanner sc) throws Exception {

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

        int opcion = Integer.parseInt(sc.nextLine());

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
                return;
        }

        if (opcion != 0) {
            System.out.println("Exportación finalizada.\n");
        }
    }
}