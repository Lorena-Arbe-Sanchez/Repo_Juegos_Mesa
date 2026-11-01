package org.egibide.dao;

import org.egibide.idao.InscripcionDao;

import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.models.Juego;
import org.egibide.models.Partida;
import org.egibide.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PartidaDaoImpl implements PartidaDao {

    private JuegoDao juegoDao = new JuegoDaoImpl();

    private InscripcionDao inscripcionDao = new InscripcionDaoImpl();

    @Override
    public List<Partida> listar() throws SQLException {

        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT * FROM partida";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Partida partida = new Partida();
                partida.setId(rs.getInt("id"));
                partida.setFecha(rs.getDate("fecha").toLocalDate());
                partida.setHora(rs.getTime("hora").toLocalTime());
                partida.setJugadoresDeseados(rs.getInt("jugadores_deseados"));
                partida.setPlazasLibres(rs.getInt("plazas_libres"));

                // Carga el juego de la partida a partir de su id
                partida.setJuego(juegoDao.buscarPorId(rs.getInt("juego_id")));
                // Carga las socias e invitadas de la partida con su id
                partida.setListaJugadorasSocias(inscripcionDao.listarSocias(partida.getId()));
                partida.setListaJugadorasInvitadas(inscripcionDao.listarInvitadas(partida.getId()));

                // Las jugadoras se cargarán cuando tengamos el DAO de inscripciones
                lista.add(partida);
            }
        }
        return lista;
    }

    @Override
    public int insertar(Partida partida) throws SQLException {

        String sql = "INSERT INTO partida (juego_id, fecha, hora, jugadores_deseados, plazas_libres) VALUES (?, ?, ?, ?, ?)";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, partida.getJuego().getId());
            ps.setDate(2, java.sql.Date.valueOf(partida.getFecha()));
            ps.setTime(3, java.sql.Time.valueOf(partida.getHora()));
            ps.setInt(4, partida.getJugadoresDeseados());
            ps.setInt(5, partida.getPlazasLibres());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()) {
                if (rs.next()) {
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el id de la partida creada.");
    }

    @Override
    public Partida buscarporId(int id) throws SQLException {

        String sql = "SELECT * FROM partida WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Partida partida = new Partida();
                    partida.setId(rs.getInt("id"));
                    partida.setFecha(rs.getDate("fecha").toLocalDate());
                    partida.setHora(rs.getTime("hora").toLocalTime());
                    partida.setJugadoresDeseados(rs.getInt("jugadores_deseados"));
                    partida.setPlazasLibres(rs.getInt("plazas_libres"));

                    // Carga el juego de la partida a partir de su id
                    partida.setJuego(juegoDao.buscarPorId(rs.getInt("juego_id")));

                    return partida;
                }
            }
        }
        return null;
    }

    // Borra la partida y, antes, sus inscripciones
    @Override
    public void eliminar(int id) throws SQLException {
        String sqlSocias = "DELETE FROM partida_socia WHERE partida_id = ?";
        String sqlInvitadas = "DELETE FROM partida_invitada WHERE partida_id = ?";
        String sqlPartida = "DELETE FROM partida WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try {
            puente.setAutoCommit(false);

            try (PreparedStatement ps = puente.prepareStatement(sqlSocias)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = puente.prepareStatement(sqlInvitadas)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            try (PreparedStatement ps = puente.prepareStatement(sqlPartida)) {
                ps.setInt(1, id);
                ps.executeUpdate();
            }

            puente.commit();

        } catch (SQLException e) {
            puente.rollback();
            throw e;
        } finally {
            puente.setAutoCommit(true);
        }
    }

    @Override
    public void actualizar(Partida partida) throws SQLException {
        String sql = "UPDATE partida SET juego_id = ?, fecha = ?, hora = ?, jugadores_deseados = ?, plazas_libres = ? WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, partida.getJuego().getId());
            ps.setDate(2, java.sql.Date.valueOf(partida.getFecha()));
            ps.setTime(3, java.sql.Time.valueOf(partida.getHora()));
            ps.setInt(4, partida.getJugadoresDeseados());
            ps.setInt(5, partida.getPlazasLibres());
            ps.setInt(6, partida.getId());
            ps.executeUpdate();
        }
    }


    @Override
    public List<Partida> buscarPorJuego(String nombreJuego) throws SQLException {
        String sql = "SELECT p.* FROM partida p " +
                "INNER JOIN juego j ON p.juego_id = j.id " +
                "WHERE LOWER(j.nombre) LIKE LOWER(?)";

        return buscarPartidas(sql, "%" + nombreJuego + "%");
    }

    @Override
    public List<Partida> buscarPorFecha(LocalDate fecha) throws SQLException {
        String sql = "SELECT * FROM partida WHERE fecha = ?";

        List<Partida> lista = new ArrayList<>();
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setDate(1, Date.valueOf(fecha));

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPartida(rs));
                }
            }
        }

        return lista;
    }

    // Method común para realizar las búsquedas
    private List<Partida> buscarPartidas(String sql, String valor) throws SQLException {
        List<Partida> lista = new ArrayList<>();
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, valor);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    lista.add(mapearPartida(rs));
                }
            }
        }

        return lista;
    }

    // Convierte los datos de la consulta en un objeto Partida
    private Partida mapearPartida(ResultSet rs) throws SQLException {
        Partida partida = new Partida();

        partida.setId(rs.getInt("id"));
        partida.setFecha(rs.getDate("fecha").toLocalDate());
        partida.setHora(rs.getTime("hora").toLocalTime());
        partida.setJugadoresDeseados(rs.getInt("jugadores_deseados"));
        partida.setPlazasLibres(rs.getInt("plazas_libres"));

        // Recuperar el juego asociado a la partida
        JuegoDao juegoDao = new JuegoDaoImpl();
        Juego juego = juegoDao.buscarPorId(rs.getInt("juego_id"));
        partida.setJuego(juego);

        return partida;
    }
}