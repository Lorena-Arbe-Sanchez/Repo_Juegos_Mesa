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

    // El documento lo escribe la persona, así que no hay id generado que devolver (void)
    @Override
    public void insertar(Socia socia) throws SQLException {
        String sql = "INSERT INTO socia (documento, nombre, apellido1, apellido2, telefono, edad, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, socia.getDocumento());
            ps.setString(2, socia.getNombre());
            ps.setString(3, socia.getApellido1());
            ps.setString(4, socia.getApellido2());
            ps.setString(5, socia.getTelefono());
            ps.setInt(6, socia.getEdad());
            ps.setString(7, socia.getEmail());
            ps.executeUpdate();
        }
    }

    @Override
    public Socia buscarDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM socia WHERE documento = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Socia socia = new Socia();
                    socia.setDocumento(rs.getString("documento"));
                    socia.setNombre(rs.getString("nombre"));
                    socia.setApellido1(rs.getString("apellido1"));
                    socia.setApellido2(rs.getString("apellido2"));
                    socia.setTelefono(rs.getString("telefono"));
                    socia.setEdad(rs.getInt("edad"));
                    socia.setEmail(rs.getString("email"));
                    return socia;
                }
            }
        }
        return null;
    }

    // ambos solo hacen la acción, no devuelven nada más:
    @Override
    public void eliminar(String documento) throws SQLException {
        String sql = "DELETE FROM socia WHERE documento = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, documento);
            ps.executeUpdate();
        }
    }

    @Override
    public void actualizar(Socia socia) throws SQLException {
        String sql = "UPDATE socia SET nombre = ?, apellido1 = ?, apellido2 = ?, telefono = ?, edad = ?, email = ? WHERE documento = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, socia.getNombre());
            ps.setString(2, socia.getApellido1());
            ps.setString(3, socia.getApellido2());
            ps.setString(4, socia.getTelefono());
            ps.setInt(5, socia.getEdad());
            ps.setString(6, socia.getEmail());
            ps.setString(7, socia.getDocumento());
            ps.executeUpdate();
        }
    }
}