package org.egibide.repositories;

import org.egibide.dao.InvitadaDaoImpl;
import org.egibide.dao.JuegoDaoImpl;
import org.egibide.dao.PartidaDaoImpl;
import org.egibide.dao.SociaDaoImpl;
import org.egibide.idao.InvitadaDao;
import org.egibide.idao.JuegoDao;
import org.egibide.idao.PartidaDao;
import org.egibide.idao.SociaDao;
import org.egibide.irepositories.PartidaRepository;

public class PartidaRepositoryImpl implements PartidaRepository {

    private PartidaDao partidaDao = new PartidaDaoImpl();
//    private JuegoDao juegoDao = new JuegoDaoImpl();
//    private SociaDao sociaDao = new SociaDaoImpl();
//    private InvitadaDao invitadaDao = new InvitadaDaoImpl();

    //
}
