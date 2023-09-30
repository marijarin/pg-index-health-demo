/*
 * Copyright (c) 2019-2023. Ivan Vakhrushev and others.
 * https://github.com/mfvanek/pg-index-health-demo
 *
 * Licensed under the Apache License 2.0
 */

package io.github.mfvanek.pg.index.health.demo;

import io.github.mfvanek.pg.index.health.demo.utils.Consts;
import io.github.mfvanek.pg.index.health.demo.utils.StatisticsCollector;
import io.github.mfvanek.pg.testing.PostgreSqlContainerWrapper;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class StatisticsDemoApp {

    @SneakyThrows
    public static void main(final String[] args) {
        try (PostgreSqlContainerWrapper postgres = PostgreSqlContainerWrapper.withVersion(Consts.PG_VERSION)) {
            StatisticsCollector.resetStatistics(postgres.getDataSource(), postgres.getUrl());
        }
    }
}
