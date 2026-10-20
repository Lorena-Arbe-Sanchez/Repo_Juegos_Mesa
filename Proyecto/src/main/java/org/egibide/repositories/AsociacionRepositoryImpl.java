package org.egibide.repositories;

import org.egibide.dao.AsociacionDaoImpl;
import org.egibide.dao.JuegoDaoImpl;
import org.egibide.idao.AsociacionDao;
import org.egibide.idao.JuegoDao;
import org.egibide.irepositories.AsociacionRepository;
import org.egibide.models.Asociacion;

public class AsociacionRepositoryImpl implements AsociacionRepository {

    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
//    private JuegoDao juegoDao = new JuegoDaoImpl();

    @Override
    public Asociacion getAsociacion(int asociacion_id) {

        //return asociacionDao.getAsociacion(asociacion_id);
        return null;
    }

    //
}
