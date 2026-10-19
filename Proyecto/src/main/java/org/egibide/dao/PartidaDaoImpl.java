package org.egibide.dao;

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

    @Override
    public List<Partida> listar() throws SQLException {
        List<Partida> lista = new ArrayList<>();
        String sql = "SELECT * FROM partidas";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        /*try {

        }*/

        return lista;
    }

    //Devuelve el id(public int), es util si necesitamos saber su id justo despues de crearlo
    @Override
    public int insertar(Partida partida) throws SQLException {

        String sql = "INSERT INTO partidas (id, juego_id, fecha, hora, jugadores_deseados, plazas_libres) VALUES (?, ?, ?, ?, ?)";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql, PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setInt(1, partida.getJuego().getId());
            ps.setDate(2, java.sql.Date.valueOf(partida.getFecha()));
            ps.setTime(3, java.sql.Time.valueOf(partida.getHora()));
            ps.setInt(4, partida.getJugadoresDeseados());
            ps.setInt(5, partida.getPlazasLibres());
            ps.executeUpdate();

            try (ResultSet rs = ps.getGeneratedKeys()){
                if (rs.next()){
                    return rs.getInt(1);
                }
            }
        }
        throw new SQLException("No se pudo obtener el id de la partida creada.");
    }

    @Override
    public Partida buscarporId(int id) throws SQLException {
        String sql = "SELECT * FROM partidas WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)){
            ps.setInt(1,id);

            try (ResultSet rs = ps.executeQuery()){
                if (rs.next()){
                    Partida partida = new Partida();
                    partida.setId(rs.getInt("id"));
                    partida.setFecha(rs.getDate("fecha").toLocalDate());
                    partida.setHora(rs.getTime("hora").toLocalTime());
                    partida.setJugadoresDeseados(rs.getInt("jugadores_deseados"));
                    partida.setPlazasLibres(rs.getInt("plazas_libres"));
                    return partida;
                }
            }
        }
        return null;
    }


    //ambos solo hacen la accion, no devuelven nada mas:
    @Override
    public void eliminar(int id) throws SQLException {
        String sql = "DELETE FROM partidas WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)){
            ps.setInt(1,id);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Partida partida) throws SQLException {
        String sql = "UPDATE partidas SET juego_id = ?, fecha = ?, jugadores_deseados = ?, plazas_libres = ? WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)){
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

/*Si la partida no existe, ninguno de los dos da error:
el UPDATE o el DELETE simplemente no encuentran ninguna fila y no hacen nada.
Si quieres detectarlo, executeUpdate() devuelve cuántas filas ha tocado (int filas = ps.executeUpdate();,
y si es 0, no existía).
*/


/*
Si la partida tiene jugadoras inscritas en otra tabla con clave foránea,
el DELETE fallará con una SQLException. Es el caso en el que el enunciado habla de respetar la integridad referencial.
Las opciones son borrar antes las inscripciones, configurar ON DELETE CASCADE en la tabla,
o hacer una baja lógica: añadir una columna eliminado y que eliminar sea un UPDATE partidas SET eliminado = true WHERE id = ?.
Eso lo decidís en el grupo y lo justificáis en la memoria.
 */
