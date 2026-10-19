package org.egibide.repositories;

import org.egibide.dao.JuegoDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.irepositories.SociaRepository;

public class SociaRepositoryImpl implements SociaRepository {

//    private SociaDao sociaDao = new SociaDaoImpl();
    private PartidaDao partidaDao = new PartidaDaoImpl();
//    private JuegoDao juegoDao = new JuegoDaoImpl();

    //
}
