package org.egibide.dao;

import org.egibide.idao.InvitadaDao;
import org.egibide.models.Invitada;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InvitadaDaoImpl implements InvitadaDao {

    @Override
    public List<Invitada> listar() throws SQLException {
        List<Invitada> invitadas = new ArrayList<>();
        String sql = "SELECT * FROM invitada";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {
                invitadas.add(mapearInvitada(rs));
            }
        }

        return invitadas;
    }

    @Override
    public void insertar(Invitada invitada) throws SQLException {
        String sql = "INSERT INTO invitada (documento, nombre, apellido1, apellido2, telefono, edad, email) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, invitada.getDocumento());
            ps.setString(2, invitada.getNombre());
            ps.setString(3, invitada.getApellido1());
            ps.setString(4, invitada.getApellido2());
            ps.setString(5, invitada.getTelefono());
            ps.setInt(6, invitada.getEdad());
            ps.setString(7, invitada.getEmail());

            ps.executeUpdate();
        }
    }

    @Override
    public Invitada buscarDocumento(String documento) throws SQLException {
        String sql = "SELECT * FROM invitada WHERE documento = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    return mapearInvitada(rs);
                }
            }
        }

        return null;
    }

    @Override
    public void actualizar(Invitada invitada) throws SQLException {
        String sql = "UPDATE invitada SET nombre = ?, apellido1 = ?, apellido2 = ?, " +
                "telefono = ?, edad = ?, email = ? WHERE documento = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, invitada.getNombre());
            ps.setString(2, invitada.getApellido1());
            ps.setString(3, invitada.getApellido2());
            ps.setString(4, invitada.getTelefono());
            ps.setInt(5, invitada.getEdad());
            ps.setString(6, invitada.getEmail());
            ps.setString(7, invitada.getDocumento());

            ps.executeUpdate();
        }
    }

    @Override
    public void eliminar(String documento) throws SQLException {
        String sql = "DELETE FROM invitada WHERE documento = ?";

        try (Connection conexion = DatabaseConnection.getInstance().getConnection();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, documento);
            ps.executeUpdate();
        }
    }

    /**
     * Convierte una fila del ResultSet en un objeto Invitada.
     *
     * @param rs resultado de la consulta SQL
     * @return objeto Invitada con los datos recuperados
     * @throws SQLException si ocurre un error al acceder a los datos
     */
    private Invitada mapearInvitada(ResultSet rs) throws SQLException {
        return new Invitada(
                rs.getString("documento"),
                rs.getString("nombre"),
                rs.getString("apellido1"),
                rs.getString("apellido2"),
                rs.getString("telefono"),
                rs.getInt("edad"),
                rs.getString("email")
        );
    }
}
