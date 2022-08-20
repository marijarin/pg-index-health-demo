/*
 * Copyright (c) 2019-2022. Ivan Vakhrushev and others.
 * https://github.com/mfvanek/pg-index-health-demo
 *
 * Licensed under the Apache License 2.0
 */

package io.github.mfvanek.pg.index.health.demo.support;

import io.github.mfvanek.pg.model.MemoryUnit;
import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.lang3.tuple.Pair;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.utility.DockerImageName;

import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;
import javax.annotation.Nonnull;
import javax.sql.DataSource;

final class PostgreSqlContainerWrapper {

    private final PostgreSQLContainer<?> container;
    private final DataSource dataSource;

    PostgreSqlContainerWrapper(@Nonnull final List<Pair<String, String>> additionalParameters) {
        this.container = new PostgreSQLContainer<>(DockerImageName.parse("postgres").withTag("13.7"))
                .withSharedMemorySize(MemoryUnit.MB.convertToBytes(512))
                .withTmpFs(Collections.singletonMap("/var/lib/postgresql/data", "rw"))
                .withCommand(prepareCommandParts(additionalParameters));
        this.container.start();
        this.dataSource = buildDataSource();
    }

    @Nonnull
    private static String[] prepareCommandParts(@Nonnull final List<Pair<String, String>> additionalParameters) {
        return additionalParameters.stream()
                .flatMap(kv -> Stream.of("-c", kv.getKey() + "=" + kv.getValue()))
                .toArray(String[]::new);
    }

    @Nonnull
    public DataSource getDataSource() {
        return dataSource;
    }

    public int getPort() {
        return container.getFirstMappedPort();
    }

    @Nonnull
    public String getUrl() {
        return container.getJdbcUrl();
    }

    @Nonnull
    public String getUsername() {
        return container.getUsername();
    }

    @Nonnull
    public String getPassword() {
        return container.getPassword();
    }

    @Nonnull
    private DataSource buildDataSource() {
        final BasicDataSource basicDataSource = new BasicDataSource();
        basicDataSource.setUrl(container.getJdbcUrl());
        basicDataSource.setUsername(container.getUsername());
        basicDataSource.setPassword(container.getPassword());
        basicDataSource.setDriverClassName(container.getDriverClassName());
        return basicDataSource;
    }
}
