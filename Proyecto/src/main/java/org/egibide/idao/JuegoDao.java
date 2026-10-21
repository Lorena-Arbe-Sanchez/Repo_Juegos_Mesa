package org.egibide.idao;

import org.egibide.models.Juego;

import java.sql.SQLException;
import java.util.List;

public interface JuegoDao {

    List<Juego> listar() throws SQLException;
    int insertar(Juego juego) throws SQLException;
    Juego buscarPorId(int id) throws SQLException;
    void actualizar(Juego juego) throws SQLException;
    void eliminar(int id) throws SQLException;
}