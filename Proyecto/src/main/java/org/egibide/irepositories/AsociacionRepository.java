package org.egibide.irepositories;

import org.egibide.models.Asociacion;

// AsociacionRepository: define qué datos completos queremos recuperar.
public interface AsociacionRepository {

    Asociacion getAsociacion(int asociacion_id);

    // TODO : Mirar qué hay que poner aquí y en el "AsociacionRepositoryImpl"
}
