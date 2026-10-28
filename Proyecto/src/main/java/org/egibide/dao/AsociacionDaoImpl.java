package org.egibide.dao;

import org.egibide.idao.AsociacionDao;
import org.egibide.models.Asociacion;
import org.egibide.utils.DatabaseConnection;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

// AsociacionDaoImpl: ejecuta las consultas SQL contra MariaDB.
public class AsociacionDaoImpl implements AsociacionDao {

    @Override
    public int add(Asociacion asociacion) {

        String query = "INSERT INTO asociacion " +
                "(nombre, razon_social, fecha_apertura, direccion, telefono, email, cif) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try {
            PreparedStatement ps = DatabaseConnection.getInstance()
                    .getConnection()
                    .prepareStatement(query, Statement.RETURN_GENERATED_KEYS);

            ps.setString(1, asociacion.getNombre());
            ps.setString(2, asociacion.getRazonSocial());
            ps.setDate(3, asociacion.getFechaApertura() == null ? null :
                    Date.valueOf(asociacion.getFechaApertura()));
            ps.setString(4, asociacion.getDireccion());
            ps.setString(5, asociacion.getTelefono());
            ps.setString(6, asociacion.getEmail());
            ps.setString(7, asociacion.getCif());

            ps.executeUpdate();

            ResultSet rs = ps.getGeneratedKeys();

            if (rs.next()) {
                return rs.getInt(1);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

    @Override
    public void delete(int id) {

        String query = "DELETE FROM asociacion WHERE id = ?";

        try {
            PreparedStatement ps = DatabaseConnection.getInstance()
                    .getConnection()
                    .prepareStatement(query);

            ps.setInt(1, id);
            ps.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public Asociacion getAsociacion(int id) {

        String query = "SELECT * FROM asociacion WHERE id = ?";
        Asociacion asociacion = null;

        try {
            PreparedStatement ps = DatabaseConnection.getInstance()
                    .getConnection()
                    .prepareStatement(query);

            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                asociacion = convertirAsociacion(rs);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return asociacion;
    }

    @Override
    public List<Asociacion> getAsociaciones() {

        String query = "SELECT * FROM asociacion";
        List<Asociacion> listaAsociaciones = new ArrayList<>();

        try {
            PreparedStatement ps = DatabaseConnection.getInstance()
                    .getConnection()
                    .prepareStatement(query);

            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                listaAsociaciones.add(convertirAsociacion(rs));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return listaAsociaciones;
    }

    @Override
    public boolean update(Asociacion asociacion) {

        String query = "UPDATE asociacion SET nombre = ?, razon_social = ?, " +
                "fecha_apertura = ?, direccion = ?, telefono = ?, email = ?, cif = ? " +
                "WHERE id = ?";

        try {
            PreparedStatement ps = DatabaseConnection.getInstance()
                    .getConnection()
                    .prepareStatement(query);

            ps.setString(1, asociacion.getNombre());
            ps.setString(2, asociacion.getRazonSocial());
            ps.setDate(3, asociacion.getFechaApertura() == null ? null :
                    Date.valueOf(asociacion.getFechaApertura()));
            ps.setString(4, asociacion.getDireccion());
            ps.setString(5, asociacion.getTelefono());
            ps.setString(6, asociacion.getEmail());
            ps.setString(7, asociacion.getCif());
            ps.setInt(8, asociacion.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    // Convertir los datos de una fila en un objeto "Asociacion"
    private Asociacion convertirAsociacion(ResultSet rs) throws SQLException {

        Date fecha = rs.getDate("fecha_apertura");

        return new Asociacion(
                rs.getInt("id"),
                rs.getString("nombre"),
                rs.getString("razon_social"),
                fecha == null ? null : fecha.toLocalDate(),
                rs.getString("direccion"),
                rs.getString("telefono"),
                rs.getString("email"),
                rs.getString("cif")
        );
    }
}