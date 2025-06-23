package com.company.sql.db.store.deployment;

import com.company.sql.db.store.runtime.ReactiveExecutorServiceProducer;
import com.company.sql.db.store.runtime.ReactiveStoreProcedureService;
import com.company.sql.db.store.runtime.config.SessionFactoryConfig;
import io.quarkus.arc.deployment.AdditionalBeanBuildItem;
import io.quarkus.deployment.annotations.BuildProducer;
import io.quarkus.deployment.annotations.BuildStep;
import io.quarkus.deployment.builditem.FeatureBuildItem;

class SqlDbStoreProcessor {

    private static final String FEATURE = "sql-db-store";

    @BuildStep
    FeatureBuildItem feature() {
        return new FeatureBuildItem(FEATURE);
    }

    @BuildStep
    void registerService(BuildProducer<AdditionalBeanBuildItem> additionalBeans) {
        additionalBeans.produce(AdditionalBeanBuildItem.builder()
                        .addBeanClasses(
                                ReactiveStoreProcedureService.class,
                                ReactiveExecutorServiceProducer.class
                        ).setUnremovable()
                .build());
    }
}
