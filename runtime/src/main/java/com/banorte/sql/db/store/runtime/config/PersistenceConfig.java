package com.company.sql.db.store.runtime.config;

import io.quarkus.runtime.annotations.ConfigPhase;
import io.quarkus.runtime.annotations.ConfigRoot;
import io.smallrye.config.ConfigMapping;

import java.util.List;

@ConfigRoot(phase = ConfigPhase.RUN_TIME)
@ConfigMapping(prefix = "com.company.persistence")
public interface PersistenceConfig {

    /**
     * Lista de unidades de persistencia a registrar dinamicamente.
     */
    List<String> persistenceUnits();
}
