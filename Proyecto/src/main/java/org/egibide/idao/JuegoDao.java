package org.egibide.idao;

import org.egibide.models.Juego;

import java.util.List;

public interface JuegoDao {

    List<Juego> listar();
    void insertar (Juego juego);
    Juego buscaPorId(int id);

}
