package org.egibide.dao;

import org.egibide.idao.InscripcionDao;
import org.egibide.models.Invitada;
import org.egibide.models.Socia;
import org.egibide.utils.DatabaseConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class InscripcionDaoImpl implements InscripcionDao {

    // El JOIN une dos tablas: de partida_socia saca qué socias están en la partida
    // y de socia coge sus datos
    @Override
    public List<Socia> listarSocias(int partidaId) throws SQLException {
        List<Socia> lista = new ArrayList<>();
        String sql = "SELECT s.* FROM socia s JOIN partida_socia ps ON s.documento = ps.socia_documento WHERE ps.partida_id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, partidaId);

            try (ResultSet rs = ps.executeQuery()) {
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
        }
        return lista;
    }

    // Igual que el anterior, pero con las invitadas
    @Override
    public List<Invitada> listarInvitadas(int partidaId) throws SQLException {
        List<Invitada> lista = new ArrayList<>();
        String sql = "SELECT i.* FROM invitada i JOIN partida_invitada pinv ON i.documento = pinv.invitada_documento WHERE pinv.partida_id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, partidaId);

            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    Invitada invitada = new Invitada();
                    invitada.setDocumento(rs.getString("documento"));
                    invitada.setNombre(rs.getString("nombre"));
                    invitada.setApellido1(rs.getString("apellido1"));
                    invitada.setApellido2(rs.getString("apellido2"));
                    invitada.setTelefono(rs.getString("telefono"));
                    invitada.setEdad(rs.getInt("edad"));
                    invitada.setEmail(rs.getString("email"));
                    lista.add(invitada);
                }
            }
        }
        return lista;
    }

    // Devuelve true si ya existe esa fila en partida_socia
    @Override
    public boolean sociaApuntada(int partidaId, String documento) throws SQLException {
        String sql = "SELECT * FROM partida_socia WHERE partida_id = ? AND socia_documento = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            ps.setString(2, documento);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    // Devuelve true si ya existe esa fila en partida_invitada
    @Override
    public boolean invitadaApuntada(int partidaId, String documento) throws SQLException {
        String sql = "SELECT * FROM partida_invitada WHERE partida_id = ? AND invitada_documento = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setInt(1, partidaId);
            ps.setString(2, documento);

            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        }
    }

    @Override
    public Invitada buscarInvitada(String documento) throws SQLException {
        String sql = "SELECT * FROM invitada WHERE documento = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
            ps.setString(1, documento);

            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    Invitada invitada = new Invitada();
                    invitada.setDocumento(rs.getString("documento"));
                    invitada.setNombre(rs.getString("nombre"));
                    invitada.setApellido1(rs.getString("apellido1"));
                    invitada.setApellido2(rs.getString("apellido2"));
                    invitada.setTelefono(rs.getString("telefono"));
                    invitada.setEdad(rs.getInt("edad"));
                    invitada.setEmail(rs.getString("email"));
                    return invitada;
                }
            }
        }
        return null;
    }

    @Override
    public void crearInvitada(Invitada invitada) throws SQLException {
        String sql = "INSERT INTO invitada (documento, nombre, apellido1, apellido2, telefono, edad, email) VALUES (?, ?, ?, ?, ?, ?, ?)";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sql)) {
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

    // Dos pasos: guarda la inscripción y resta una plaza libre a la partida
    @Override
    public void apuntarSocia(int partidaId, String documento) throws SQLException {
        String sqlInscripcion = "INSERT INTO partida_socia (partida_id, socia_documento) VALUES (?, ?)";
        String sqlPlazas = "UPDATE partida SET plazas_libres = plazas_libres - 1 WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sqlInscripcion)) {
            ps.setInt(1, partidaId);
            ps.setString(2, documento);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = puente.prepareStatement(sqlPlazas)) {
            ps.setInt(1, partidaId);
            ps.executeUpdate();
        }
    }

    @Override
    public void apuntarInvitada(int partidaId, String documento) throws SQLException {
        String sqlInscripcion = "INSERT INTO partida_invitada (partida_id, invitada_documento) VALUES (?, ?)";
        String sqlPlazas = "UPDATE partida SET plazas_libres = plazas_libres - 1 WHERE id = ?";
        Connection puente = DatabaseConnection.getInstance().getConnection();

        try (PreparedStatement ps = puente.prepareStatement(sqlInscripcion)) {
            ps.setInt(1, partidaId);
            ps.setString(2, documento);
            ps.executeUpdate();
        }

        try (PreparedStatement ps = puente.prepareStatement(sqlPlazas)) {
            ps.setInt(1, partidaId);
            ps.executeUpdate();
        }
    }
}