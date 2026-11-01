package org.egibide.dao;

import org.egibide.idao.AsociacionDao;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.SociaDao;
import org.egibide.models.Asociacion;
import org.egibide.models.Juego;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class JuegoDaoImpl implements JuegoDao {

    @Override
    public List<Juego> listar() throws SQLException {

        AsociacionDao asociacionDao = new AsociacionDaoImpl();

        List<Juego> lista = new ArrayList<>();
        String sql = "SELECT * FROM juego";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Juego juego = new Juego();
                juego.setId(rs.getInt("id"));
                juego.setNombre(rs.getString("nombre"));
                juego.setAutor1(rs.getString("autor1"));
                juego.setAutor2(rs.getString("autor2"));
                juego.setDescripcion(rs.getString("descripcion"));
                juego.setMinJugadores(rs.getInt("min_jugadores"));
                juego.setMaxJugadores(rs.getInt("max_jugadores"));
                juego.setDuracion(rs.getInt("duracion"));
                juego.setTematica(rs.getString("tematica"));
                juego.setCompetitivo(rs.getBoolean("competitivo"));
                juego.setUbicacion(rs.getString("ubicacion"));
                juego.setEstado(rs.getString("estado"));

                // Comprobar quién es la propietaria del juego
                int idAsociacion = rs.getInt("asociacion_id");

                if (!rs.wasNull()) {
                    // El juego pertenece a la asociación
                    Asociacion asociacion = asociacionDao.getAsociacion(idAsociacion);
                    juego.setAsociacionPropietaria(asociacion);

                } else {
                    // El juego pertenece a una socia
                    String documentoSocia = rs.getString("socia_documento");

                    if (documentoSocia != null) {
                        SociaDao sociaDao = new SociaDaoImpl();
                        Socia socia = sociaDao.buscarDocumento(documentoSocia);
                        juego.setSociaPropietaria(socia);
                    }
                }

                juego.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());

                lista.add(juego);
            }
        }
        return lista;
    }

    @Override
    public int insertar(Juego juego) throws SQLException {

        String sql = "INSERT INTO juego (nombre, autor1, autor2, descripcion, min_jugadores, max_jugadores, duracion, tematica, competitivo, ubicacion, estado, asociacion_id, socia_documento, fecha_alta) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, juego.getNombre());
            ps.setString(2, juego.getAutor1());
            ps.setString(3, juego.getAutor2());
            ps.setString(4, juego.getDescripcion());
            ps.setInt(5, juego.getMinJugadores());
            ps.setInt(6, juego.getMaxJugadores());
            ps.setInt(7, juego.getDuracion());
            ps.setString(8, juego.getTematica());
            ps.setBoolean(9, juego.isCompetitivo());
            ps.setString(10, juego.getUbicacion());
            ps.setString(11, juego.getEstado());

            // Solo una de las dos propietarias estará rellena, la otra se guarda como NULL
            if (juego.getAsociacionPropietaria() != null) {
                ps.setInt(12, juego.getAsociacionPropietaria().getId());
            } else {
                ps.setNull(12, java.sql.Types.INTEGER);
            }

            if (juego.getSociaPropietaria() != null) {
                ps.setString(13, juego.getSociaPropietaria().getDocumento());
            } else {
                ps.setNull(13, java.sql.Types.VARCHAR);
            }

            ps.setDate(14, java.sql.Date.valueOf(juego.getFechaAlta()));
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el id del juego creado.");
    }

    @Override
    public Juego buscarPorId(int id) throws SQLException {

        String sql = "SELECT * FROM juego WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Juego juego = new Juego();
                    juego.setId(rs.getInt("id"));
                    juego.setNombre(rs.getString("nombre"));
                    juego.setAutor1(rs.getString("autor1"));
                    juego.setAutor2(rs.getString("autor2"));
                    juego.setDescripcion(rs.getString("descripcion"));
                    juego.setMinJugadores(rs.getInt("min_jugadores"));
                    juego.setMaxJugadores(rs.getInt("max_jugadores"));
                    juego.setDuracion(rs.getInt("duracion"));
                    juego.setTematica(rs.getString("tematica"));
                    juego.setCompetitivo(rs.getBoolean("competitivo"));
                    juego.setUbicacion(rs.getString("ubicacion"));
                    juego.setEstado(rs.getString("estado"));

                    // Obtener la asociación propietaria, si existe.
                    int idAsociacion = rs.getInt("asociacion_id");

                    if (!rs.wasNull()) {
                        AsociacionDao asociacionDao = new AsociacionDaoImpl();
                        juego.setAsociacionPropietaria(asociacionDao.getAsociacion(idAsociacion));
                    }

                    // Obtener la socia propietaria, si existe.
                    String documentoSocia = rs.getString("socia_documento");

                    if (documentoSocia != null) {
                        SociaDao sociaDao = new SociaDaoImpl();
                        juego.setSociaPropietaria(sociaDao.buscarDocumento(documentoSocia));
                    }

                    juego.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());
                    return juego;
                }
            }
        }
        return null;
    }

    @Override
    public void eliminar(int id) throws SQLException {

        String sql = "DELETE FROM juego WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Juego juego) throws SQLException {

        String sql = "UPDATE juego SET nombre = ?, autor1 = ?, autor2 = ?, descripcion = ?, min_jugadores = ?, max_jugadores = ?, duracion = ?, tematica = ?, competitivo = ?, ubicacion = ?, estado = ?, asociacion_id = ?, socia_documento = ? WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, juego.getNombre());
            ps.setString(2, juego.getAutor1());
            ps.setString(3, juego.getAutor2());
            ps.setString(4, juego.getDescripcion());
            ps.setInt(5, juego.getMinJugadores());
            ps.setInt(6, juego.getMaxJugadores());
            ps.setInt(7, juego.getDuracion());
            ps.setString(8, juego.getTematica());
            ps.setBoolean(9, juego.isCompetitivo());
            ps.setString(10, juego.getUbicacion());
            ps.setString(11, juego.getEstado());

            if (juego.getAsociacionPropietaria() != null) {
                ps.setInt(12, juego.getAsociacionPropietaria().getId());
            } else {
                ps.setNull(12, java.sql.Types.INTEGER);
            }

            if (juego.getSociaPropietaria() != null) {
                ps.setString(13, juego.getSociaPropietaria().getDocumento());
            } else {
                ps.setNull(13, java.sql.Types.VARCHAR);
            }

            ps.setInt(14, juego.getId());
            ps.executeUpdate();
        }
    }

    // Buscar juegos por nombre (coincidencia parcial)
    @Override
    public List<Juego> buscarPorNombre(String nombre) throws SQLException {

        String sql = "SELECT * FROM juego WHERE LOWER(nombre) LIKE LOWER(?)";

        return buscarJuegos(sql, "%" + nombre + "%");
    }

    // Buscar juegos por temática (coincidencia parcial)
    @Override
    public List<Juego> buscarPorTematica(String tematica) throws SQLException {

        String sql = "SELECT * FROM juego WHERE LOWER(tematica) LIKE LOWER(?)";

        return buscarJuegos(sql, "%" + tematica + "%");
    }

    // Buscar juegos que permitan jugar a ese número de personas
    @Override
    public List<Juego> buscarPorNumeroJugadores(int numero) throws SQLException {

        String sql = "SELECT * FROM juego WHERE min_jugadores <= ? AND max_jugadores >= ?";

        List<Juego> lista = new ArrayList<>();
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {

            ps.setInt(1, numero);
            ps.setInt(2, numero);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearJuego(rs));
                }
            }
        }

        return lista;
    }

    // Method común para las búsquedas por texto
    private List<Juego> buscarJuegos(String sql, String valor) throws SQLException {

        List<Juego> lista = new ArrayList<>();
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {

            ps.setString(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearJuego(rs));
                }
            }
        }

        return lista;
    }

    // Convertir un registro de la BD en un objeto Juego
    private Juego mapearJuego(ResultSet rs) throws SQLException {

        Juego juego = new Juego();

        juego.setId(rs.getInt("id"));
        juego.setNombre(rs.getString("nombre"));
        juego.setAutor1(rs.getString("autor1"));
        juego.setAutor2(rs.getString("autor2"));
        juego.setDescripcion(rs.getString("descripcion"));
        juego.setMinJugadores(rs.getInt("min_jugadores"));
        juego.setMaxJugadores(rs.getInt("max_jugadores"));
        juego.setDuracion(rs.getInt("duracion"));
        juego.setTematica(rs.getString("tematica"));
        juego.setCompetitivo(rs.getBoolean("competitivo"));
        juego.setUbicacion(rs.getString("ubicacion"));
        juego.setEstado(rs.getString("estado"));

        // Recuperar la propietaria
        int idAsociacion = rs.getInt("asociacion_id");

        if (!rs.wasNull()) {
            AsociacionDao asociacionDao = new AsociacionDaoImpl();
            Asociacion asociacion = asociacionDao.getAsociacion(idAsociacion);
            juego.setAsociacionPropietaria(asociacion);

        } else {
            String documentoSocia = rs.getString("socia_documento");

            if (documentoSocia != null) {
                SociaDao sociaDao = new SociaDaoImpl();
                Socia socia = sociaDao.buscarDocumento(documentoSocia);
                juego.setSociaPropietaria(socia);
            }
        }

        juego.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());

        return juego;
    }
}