package org.egibide.idao;

import org.egibide.models.Invitada;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;

public interface InscripcionDao {

    // Quiénes están apuntadas a una partida
    List<Socia> listarSocias(int partidaId) throws SQLException;
    List<Invitada> listarInvitadas(int partidaId) throws SQLException;

    // Comprueban si una persona ya está apuntada a la partida
    boolean sociaApuntada(int partidaId, String documento) throws SQLException;
    boolean invitadaApuntada(int partidaId, String documento) throws SQLException;

    // Las invitadas hay que registrarlas antes de poder apuntarlas
    Invitada buscarInvitada(String documento) throws SQLException;
    void crearInvitada(Invitada invitada) throws SQLException;

    // Apuntan a la persona y restan una plaza libre a la partida
    void apuntarSocia(int partidaId, String documento) throws SQLException;
    void apuntarInvitada(int partidaId, String documento) throws SQLException;
}