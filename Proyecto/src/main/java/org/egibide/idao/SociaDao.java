package org.egibide.idao;
import org.egibide.models.Socia;

import java.util.List;


public interface SociaDao {

    List<Socia> listar();
    void insertar(Socia socia);
}
