package com.company.sql.db.store.it;

import com.company.sql.db.store.runtime.ReactiveStoreProcedureService;
//import io.quarkus.hibernate.orm.PersistenceUnit;
import io.quarkus.hibernate.orm.PersistenceUnit;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.vertx.RunOnVertxContext;
import jakarta.enterprise.inject.Default;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.junit.jupiter.api.Test;
import java.util.HashMap;
import java.util.Map;
import io.quarkus.test.vertx.UniAsserter;

@QuarkusTest
class SqlDbStoreResourceIT {

    @Inject
    @PersistenceUnit("cuentas")
    @Default
    ReactiveStoreProcedureService service;

    @Test
    @RunOnVertxContext
    void testExecuteStoreProcedure(UniAsserter asserter) {
        Map<String, Object> params = new HashMap<>();
        params.put("param1", 123);
        asserter.execute(() ->
                service.executeMutationQuery("my_procedure", params)
                        .onItem().invoke(result -> System.out.println("Procedure executed: " + result))
        );
    }
}
