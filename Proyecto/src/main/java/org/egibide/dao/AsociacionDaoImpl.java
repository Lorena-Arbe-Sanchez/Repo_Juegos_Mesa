package org.egibide.dao;

import org.egibide.idao.AsociacionDao;
import org.egibide.models.Asociacion;

import java.sql.ResultSet;
import java.sql.SQLException;

public class AsociacionDaoImpl implements AsociacionDao {
    private Asociacion mapAsociacion (ResultSet rs) throws SQLException {

        return null;
    }

    @Override
    public int add(Asociacion asociacion) {
        return 0;
    }
}
