package org.bahmni.module.feedintegration.atomfeed.client;

import org.ict4h.atomfeed.jdbc.JdbcConnectionProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.SQLException;

@Component
public class BasicJdbcConnectionProvider implements JdbcConnectionProvider {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Override
    public Connection getConnection() throws SQLException {
        return jdbcTemplate.getDataSource().getConnection();
    }
}
