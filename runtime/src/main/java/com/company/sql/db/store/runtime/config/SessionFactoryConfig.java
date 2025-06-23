package com.company.sql.db.store.runtime.config;

//import io.quarkus.agroal.runtime.DataSources;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.Config;
import org.eclipse.microprofile.config.ConfigProvider;
import org.hibernate.reactive.mutiny.Mutiny;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

//@ApplicationScoped
public class SessionFactoryConfig {

    //@ConfigProperty(name = "quarkus.datasource.names")
    //Set<String> datasourceNames;

    //@Inject
    //DataSources dataSources;

    //private final Map<String, Mutiny.SessionFactory> sessionFactories = new HashMap<>();

    /*@Produces
    @ApplicationScoped
    public Map<String, Mutiny.SessionFactory> sessionFactories(DataSources dataSources) throws SQLException {
        Config config = ConfigProvider.getConfig();
        Map<String, Mutiny.SessionFactory> factories = new HashMap<>();

        Set<String> datasourceNames = getConfigDatasourceNames(config);

        datasourceNames.forEach(name -> {
            try {
                factories.put(name, dataSources.getDataSource(name).unwrap(Mutiny.SessionFactory.class));
            } catch (SQLException e) {
                throw new RuntimeException("Failed to create SessionFactory for datasource : " + name, e);
            }
        });
        return factories;
    }

    private Set<String> getConfigDatasourceNames(Config config) {
        Set<String> datasource = new HashSet<>();
        config.getPropertyNames().forEach(property -> {
            if (property.startsWith("quarkus.datasource.") && property.endsWith(".reactive.url")) {
                String[] parts = property.split("\\.");
                datasource.add(parts.length > 2 ? parts[2] : "default");
            }
        });
        return datasource;
    }*/
}
