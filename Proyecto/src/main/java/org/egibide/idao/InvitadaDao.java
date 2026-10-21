package org.egibide.idao;

import org.egibide.models.Invitada;

import java.sql.SQLException;
import java.util.List;

public interface InvitadaDao {

    List<Invitada> listar() throws SQLException;
    int insertar(Invitada invitada) throws SQLException;
    Invitada buscarPorId(int id) throws SQLException;
    void actualizar(Invitada invitada) throws SQLException;
    void eliminar(int id) throws SQLException;
}