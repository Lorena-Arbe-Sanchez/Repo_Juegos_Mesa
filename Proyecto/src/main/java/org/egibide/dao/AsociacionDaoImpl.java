package org.egibide.dao;

import org.egibide.idao.AsociacionDao;
import org.egibide.models.Asociacion;

import java.util.ArrayList;
import java.util.List;

public class AsociacionDaoImpl implements AsociacionDao {

    @Override
    public int add(Asociacion asociacion) {
        return 0;
    }

    @Override
    public void delete(int id) {

    }

    @Override
    public Asociacion getAsociacion(int id) {
        return null;
    }

    @Override
    public List<Asociacion> getAsociaciones() {
        return new ArrayList<>();
    }

    @Override
    public boolean update(Asociacion asociacion) {
        return false;
    }
}
