package org.egibide.idao;

import org.egibide.models.Asociacion;

import java.util.List;

public interface AsociacionDao {

    int add(Asociacion asociacion);

    void delete(int id);

    Asociacion getAsociacion(int id);

    List<Asociacion> getAsociaciones();

    boolean update(Asociacion asociacion);
}
