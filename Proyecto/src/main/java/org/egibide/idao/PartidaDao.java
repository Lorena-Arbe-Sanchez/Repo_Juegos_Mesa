package org.egibide.idao;

import org.egibide.models.Juego;
import org.egibide.models.Partida;

import java.sql.SQLException;
import java.util.List;

public interface PartidaDao {

    List<Partida> listar() throws SQLException;
    void insertar(Partida partida) throws SQLException;
    Partida buscarporId(int id) throws SQLException;
    void eliminar(int id) throws SQLException;
    void actualizar(Partida partida) throws SQLException;
}
