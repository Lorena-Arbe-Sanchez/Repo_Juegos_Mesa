package org.egibide.dao;

import org.egibide.idao.SociaDao;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class SociaDaoImpl implements SociaDao {

    @Override
    public List<Socia> listar() throws SQLException {
        List<Socia> lista = new ArrayList<>();
        String sql = "SELECT * FROM socia";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                Socia socia = new Socia();
                socia.setDocumento(rs.getString("documento"));
                socia.setNombre(rs.getString("nombre"));
                socia.setApellido1(rs.getString("apellido1"));
                socia.setApellido2(rs.getString("apellido2"));
                socia.setTelefono(rs.getString("telefono"));
                socia.setEdad(rs.getInt("edad"));
                socia.setEmail(rs.getString("email"));
                lista.add(socia);
            }
        }
        return lista;
    }

    @Override
    public void insertar(Socia socia) throws SQLException {
        String sql = "INSERT INTO socia (documento, nombre, apellido1, apellido2, telefono, edad, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Socia buscarDocumento(String documento) {
        return null;
    }

    @Override
    public void eliminar(String documento) {

    }

    @Override
    public void actualizar(Socia socia) {

    }
}
