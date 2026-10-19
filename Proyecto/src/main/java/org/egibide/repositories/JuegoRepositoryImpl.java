package org.egibide.repositories;

import org.egibide.dao.AsociacionDaoImpl;
import org.egibide.dao.JuegoDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.AsociacionDao;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.irepositories.JuegoRepository;

public class JuegoRepositoryImpl implements JuegoRepository {

//    private JuegoDao juegoDao = new JuegoDaoImpl();
    private AsociacionDao asociacionDao = new AsociacionDaoImpl();
//    private SociaDao sociaDao = new SociaDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();

    //
}
