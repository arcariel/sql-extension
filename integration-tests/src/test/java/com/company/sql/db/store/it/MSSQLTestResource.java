package com.company.sql.db.store.it;

import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;
import org.testcontainers.containers.MSSQLServerContainer;
import java.util.Map;

public class MSSQLTestResource implements QuarkusTestResourceLifecycleManager {

    static final MSSQLServerContainer<?> DB = new MSSQLServerContainer<>("mcr.microsoft.com/mssql/server:2019-latest")
            .acceptLicense()
            .withPassword("YourStrong@Passw0rd");

    @Override
    public Map<String, String> start() {
        DB.start();
        return Map.of(
                "quarkus.datasource.reactive.url",
                "vertx-reactive:sqlserver://" + DB.getHost() + ":" + DB.getFirstMappedPort(),
                "quarkus.datasource.cuentas.reactive.url",
                "vertx-reactive:sqlserver://" + DB.getHost() + ":" + DB.getFirstMappedPort()
        );
    }

    @Override
    public void stop() {
        DB.stop();
    }
}