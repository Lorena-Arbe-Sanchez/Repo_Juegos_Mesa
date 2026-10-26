package org.egibide.idao;
import org.egibide.models.Socia;

import java.sql.SQLException;
import java.util.List;


public interface SociaDao {

    List<Socia> listar() throws SQLException;
    void insertar(Socia socia) throws SQLException;
    Socia buscarDocumento(String documento) throws SQLException;
    void eliminar (String documento) throws SQLException;
    void actualizar (Socia socia) throws SQLException;
}
