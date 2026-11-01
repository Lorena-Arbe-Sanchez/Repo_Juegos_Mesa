package org.egibide.idao;

import org.egibide.models.Invitada;

import java.sql.SQLException;
import java.util.List;

public interface InvitadaDao {

    List<Invitada> listar() throws SQLException;

    void insertar(Invitada invitada) throws SQLException;

    Invitada buscarDocumento(String documento) throws SQLException;

    void actualizar(Invitada invitada) throws SQLException;

    void eliminar(String documento) throws SQLException;
}