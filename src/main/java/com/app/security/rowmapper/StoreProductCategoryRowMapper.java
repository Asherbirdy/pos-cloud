package com.app.security.rowmapper;

import com.app.security.model.StoreProductCategory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StoreProductCategoryRowMapper implements RowMapper<StoreProductCategory> {

    @Override
    public StoreProductCategory mapRow(ResultSet resultSet, int i) throws SQLException {
        StoreProductCategory storeProductCategory = new StoreProductCategory();
        storeProductCategory.setProductCategoryId(resultSet.getString("product_category_id"));
        storeProductCategory.setName(resultSet.getString("name"));
        storeProductCategory.setStoreId(resultSet.getString("store_id"));
        storeProductCategory.setCreatedAt(resultSet.getTimestamp("created_at"));
        storeProductCategory.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return storeProductCategory;
    }
}
