package com.app.security.rowmapper;

import com.app.security.model.StoreProductItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StoreProductItemRowMapper implements RowMapper<StoreProductItem> {

    @Override
    public StoreProductItem mapRow(ResultSet resultSet, int i) throws SQLException {
        StoreProductItem item = new StoreProductItem();
        item.setStoreProductItemId(resultSet.getString("store_product_item_id"));
        item.setStoreProductCategoryId(resultSet.getString("store_product_category_id"));
        item.setName(resultSet.getString("name"));
        item.setCurrentPrice(resultSet.getBigDecimal("current_price"));
        item.setCreatedAt(resultSet.getTimestamp("created_at"));
        item.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return item;
    }
}
