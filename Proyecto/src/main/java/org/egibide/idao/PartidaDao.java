package org.egibide.idao;

import org.egibide.models.Partida;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public interface PartidaDao {

    List<Partida> listar() throws SQLException;

    //cambio de void a int
    int insertar(Partida partida) throws SQLException;

    Partida buscarporId(int id) throws SQLException;

    void eliminar(int id) throws SQLException;

    void actualizar(Partida partida) throws SQLException;

    List<Partida> buscarPorJuego(String nombreJuego) throws SQLException;

    List<Partida> buscarPorFecha(LocalDate fecha) throws SQLException;
}