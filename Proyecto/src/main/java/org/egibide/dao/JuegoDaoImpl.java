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

    // Devuelve el id (public int), es útil si necesitamos saber su id justo después de crearlo
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

                    int idAsociacion = rs.getInt("asociacion_id");
                    if (!rs.wasNull()) {
                        Asociacion asociacion = new Asociacion();
                        asociacion.setId(idAsociacion);
                        juego.setAsociacionPropietaria(asociacion);
                    }

                    String documentoSocia = rs.getString("socia_documento");
                    if (documentoSocia != null) {
                        Socia socia = new Socia();
                        socia.setDocumento(documentoSocia);
                        juego.setSociaPropietaria(socia);
                    }

                    juego.setFechaAlta(rs.getDate("fecha_alta").toLocalDate());
                    return juego;
                }
            }
        }
        return null;
    }

    // ambos solo hacen la acción, no devuelven nada más:
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
}