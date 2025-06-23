package com.company.sql.db.store.runtime;

import com.company.sql.db.store.runtime.util.QueryBuilders;
//import io.quarkus.hibernate.reactive.panache.common.WithTransaction;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import org.hibernate.reactive.common.ResultSetMapping;
import org.hibernate.reactive.mutiny.Mutiny;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@ApplicationScoped
//@WithTransaction
public class ReactiveStoreProcedureService {

    Mutiny.SessionFactory sessionFactory;

    public ReactiveStoreProcedureService(Mutiny.SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    public <R> Uni<List<R>> executeReadOnlyQuery(String procedureName,
                                                 Map<String, Object> parameters,
                                                 Class<R> clazz, String mappingName) {
        //Mutiny.SessionFactory sessionFactory = sessionFactories.get(datasource);
        /*if (sessionFactory == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Datasource '" + datasource + "' no configurado"));
        }*/
        //return execute(sessionFactory, procedureName, parameters, false, clazz, mappingName);
        return execute(procedureName, parameters, false, clazz, mappingName);
    }

    public <R> Uni<List<R>> executeMutationQuery(//String datasource,
                                                 String procedureName, Map<String, Object> parameters) {
        /*Mutiny.SessionFactory sessionFactory = sessionFactories.get(datasource);
        if (sessionFactory == null) {
            return Uni.createFrom().failure(new IllegalArgumentException("Datasource '" + datasource + "' no configurado"));
        }*/
        //return execute(sessionFactory, procedureName, parameters, true, null, null);
        return execute(procedureName, parameters, true, null, null);
    }

    private <R> Uni<List<R>> execute(//Mutiny.SessionFactory sessionFactory,
                                     String spName,
                                     Map<String, Object> params,
                                     boolean mutationQuery,
                                     Class<R> clazz,
                                     String mappingName) {
        return this.sessionFactory.withTransaction((session, transaction) ->
            mutationQuery
                ? executeMutationQuery(spName, params, session)
                : executeReadOnlyQuery(spName, params, clazz, mappingName, session)
            );
    }

    private static <R> Uni<List<R>> executeMutationQuery(String spName,
                                                         Map<String, Object> params,
                                                         Mutiny.Session session) {
        Mutiny.Query<R> query = session.createNativeQuery(
                QueryBuilders.createMsSqlQuery(spName, Optional.of(params)));
        params.forEach(query::setParameter);
        query.executeUpdate();
        return query.getResultList().onFailure().recoverWithNull();
    }

    private static <R> Uni<List<R>> executeReadOnlyQuery(String spName,
                                                         Map<String, Object> params,
                                                         Class<R> clazz,
                                                         String mappingName,
                                                         Mutiny.Session session) {
        ResultSetMapping<R> resultSetMapping = session.getResultSetMapping(clazz, mappingName);
        Mutiny.SelectionQuery<R> query = session.createNativeQuery(
                QueryBuilders.createMsSqlQuery(spName, Optional.of(params)),
                resultSetMapping);
        params.forEach(query::setParameter);
        return  query.getResultList();
    }
}
