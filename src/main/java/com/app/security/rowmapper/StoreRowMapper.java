package com.app.security.rowmapper;

import com.app.security.model.Store;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StoreRowMapper implements RowMapper<Store> {

    @Override
    public Store mapRow(ResultSet resultSet, int i) throws SQLException {
        Store store = new Store();
        store.setStore_id(resultSet.getString("store_id"));
        store.setEnterprise_id(resultSet.getString("enterprise_id"));
        store.setName(resultSet.getString("name"));
        store.setActive(resultSet.getBoolean("is_active"));
        store.setCreatedAt(resultSet.getTimestamp("created_at"));
        store.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return store;
    }
}
