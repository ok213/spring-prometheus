package com.example.springprometheus.config;

import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Tags;
import io.micrometer.core.instrument.binder.MeterBinder;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Objects;

public class DataSourceStatusProbe implements MeterBinder {

    private final String name;
    private final String description;
    private final Iterable<Tag> tags;
    private static final String SELECT_1 = "SELECT 1;";
    private static final int QUERY_TIMEOUT = 1;
    private static final double UP = 1.0;
    private static final double DOWN = 0.0;
    private final DataSource dataSource;

    public DataSourceStatusProbe(final DataSource dataSource) {
        Objects.requireNonNull(dataSource, "DATASOURCE CANNOT BE NULL!");
        this.dataSource = dataSource;
        this.name = "DATA_SOURCE";
        this.description = "===> DATASOURCE STATUS <===";
        this.tags = tags(dataSource);
    }

    private boolean status() {
        try(Connection connection = dataSource.getConnection()) {
            PreparedStatement statement = connection.prepareStatement(SELECT_1);
            statement.setQueryTimeout(QUERY_TIMEOUT);
            statement.executeQuery();
            return true;
        } catch (SQLException ignored) {
            return false;
        }
    }

    @Override
    public void bindTo(final MeterRegistry meterRegistry) {
        Gauge.builder(name, this, value -> value.status() ? UP : DOWN)
                .description(description)
                .tags(tags)
                .baseUnit("STATUS")
                .register(meterRegistry);
    }

    protected static Iterable<Tag> tags(DataSource dataSource) {
        Objects.requireNonNull(dataSource, "DATASOURCE CANNOT BE NULL!");
        try {
            return Tags.of(Tag.of("url", dataSource.getConnection().getMetaData().getURL()));
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
