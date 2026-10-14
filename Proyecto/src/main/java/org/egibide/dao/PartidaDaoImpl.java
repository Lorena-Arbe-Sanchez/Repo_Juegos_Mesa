package org.egibide.dao;

import org.egibide.idao.PartidaDao;
import org.egibide.models.Partida;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PartidaDaoImpl implements PartidaDao {

    @Override
    public List<Partida> listar() throws SQLException {
        List<Partida> lista = new ArrayList<>();
        return lista;
    }

    @Override
    public void insertar(Partida partida) throws SQLException {

    }

    @Override
    public Partida buscarporId(int id) throws SQLException {
        return null;
    }

    @Override
    public void eliminar(int id) throws SQLException {

    }

    @Override
    public void actualizar(Partida partida) throws SQLException {

    }
}
