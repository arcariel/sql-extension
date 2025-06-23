package com.company.sql.db.store.runtime;

import com.company.sql.db.store.runtime.config.PersistenceConfig;
import io.quarkus.hibernate.orm.PersistenceUnit;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Default;
import jakarta.enterprise.inject.Instance;
import jakarta.enterprise.inject.Produces;
import jakarta.enterprise.inject.spi.InjectionPoint;
import jakarta.inject.Inject;
import jakarta.inject.Named;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import org.hibernate.reactive.mutiny.Mutiny;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

@ApplicationScoped
public class ReactiveExecutorServiceProducer {

    private static final String DEFAULT_PERSISTENCE_UNIT_NAME = "default";

    @Inject
    Instance<Mutiny.SessionFactory> sessionFactories;

    private final Map<String, ReactiveStoreProcedureService> services = new ConcurrentHashMap<>();

    @PostConstruct
    void init() {
        Set<String> units = sessionFactories.stream()
                .map(sf -> sf.getClass().getAnnotation(PersistenceUnit.class))
                .filter(Objects::nonNull)
                .map(PersistenceUnit::value)
                .collect(Collectors.toSet());

        if (units.isEmpty()) {
            initializeDefault();
        } else {
            units.forEach(this::initializePersistenceUnit);
        }
    }

    private void initializeDefault() {
        try {
            Mutiny.SessionFactory sf = sessionFactories.get();
            services.put(DEFAULT_PERSISTENCE_UNIT_NAME, new ReactiveStoreProcedureService(sf));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create default service", e);
        }
    }

    private void initializePersistenceUnit(String unitName) {
        try {
            Mutiny.SessionFactory sf = sessionFactories
                    .select(new PersistenceUnit.PersistenceUnitLiteral(unitName))
                    .get();
            services.put(unitName, new ReactiveStoreProcedureService(sf));
        } catch (Exception e) {
            throw new IllegalStateException("Failed to create service for: " + unitName, e);
        }
    }

    @Produces
    @Named("reactiveServices")
    public Map<String, ReactiveStoreProcedureService> produceServices() {
        return Collections.unmodifiableMap(services);
    }

    @Produces
    @PersistenceUnit("")
    public ReactiveStoreProcedureService resolveService(InjectionPoint ip) {
        PersistenceUnit puAnnotation = ip.getAnnotated().getAnnotation(PersistenceUnit.class);
        String unitName = (puAnnotation != null && !puAnnotation.value().isEmpty())
                ? puAnnotation.value()
                : DEFAULT_PERSISTENCE_UNIT_NAME;

        ReactiveStoreProcedureService service = services.get(unitName);
        if (service == null) {
            throw new IllegalStateException("No service fond for persistence unit: " + unitName);
        }
        return service;
    }

    @Produces
    @Default
    public ReactiveStoreProcedureService defaultService() {
        return services.get(DEFAULT_PERSISTENCE_UNIT_NAME);
    }
}
