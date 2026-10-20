package org.egibide.idao;

import org.egibide.models.Asociacion;

import java.util.List;

// AsociacionDao: define las operaciones para gestionar asociaciones.
public interface AsociacionDao {

    int add(Asociacion asociacion);

    void delete(int id);

    Asociacion getAsociacion(int id);

    List<Asociacion> getAsociaciones();

    boolean update(Asociacion asociacion);
}
