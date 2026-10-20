package org.egibide.irepositories;

import org.egibide.models.Asociacion;

// AsociacionRepository: define qué datos completos queremos recuperar.
public interface AsociacionRepository {

    Asociacion getAsociacion(int asociacion_id);
}
