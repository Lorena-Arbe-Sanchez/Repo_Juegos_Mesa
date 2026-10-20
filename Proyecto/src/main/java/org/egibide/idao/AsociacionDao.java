package org.egibide.idao;

import org.egibide.models.Asociacion;

import java.sql.SQLException;

public interface AsociacionDao {

    Asociacion obtener() throws SQLException;
    void actualizar(Asociacion asociacion) throws SQLException;
}