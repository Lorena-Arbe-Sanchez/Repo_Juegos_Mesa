package org.egibide.repositories;

import org.egibide.dao.AsociacionDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.idao.AsociacionDao;
import org.egibide.idao.PartidaDao;
import org.egibide.irepositories.JuegoRepository;

public class JuegoRepositoryImpl implements JuegoRepository {

//    private JuegoDao juegoDao = new JuegoDaoImpl();
    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
//    private SociaDao sociaDao = new SociaDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();

    //
}
