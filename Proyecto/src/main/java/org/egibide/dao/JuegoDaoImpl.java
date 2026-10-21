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
        return 0;
    }

    @Override
    public Juego buscarPorId(int id) throws SQLException {
        return null;
    }

    @Override
    public void actualizar(Juego juego) throws SQLException {

    }

    @Override
    public void eliminar(int id) throws SQLException {

    }
}
