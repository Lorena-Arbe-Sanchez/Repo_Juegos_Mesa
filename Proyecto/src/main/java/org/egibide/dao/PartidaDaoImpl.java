package org.egibide.dao;

import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.models.Partida;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartidaDaoImpl implements PartidaDao {

    // NUEVO: se usa para cargar el juego completo de cada partida
    private JuegoDao juegoDao = new JuegoDaoImpl();

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

                // NUEVO: carga el juego de la partida a partir de su id
                partida.setJuego(juegoDao.buscarPorId(rs.getInt("juego_id")));

                // Las jugadoras se cargarán cuando tengamos el DAO de inscripciones
                lista.add(partida);
            }
        }
        return lista;
    }

    // Devuelve el id (public int), es útil si necesitamos saber su id justo después de crearlo
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

                    // NUEVO: carga el juego de la partida a partir de su id
                    partida.setJuego(juegoDao.buscarPorId(rs.getInt("juego_id")));

                    return partida;
                }
            }
        }
        return null;
    }

    // ambos solo hacen la acción, no devuelven nada más:
    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM partida WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Partida partida) throws SQLException {
        // MODIFICADO: añadido "hora = ?" (antes faltaba y fallaba al ejecutar)
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
}