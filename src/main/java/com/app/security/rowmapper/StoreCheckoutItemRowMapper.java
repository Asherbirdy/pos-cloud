package com.app.security.rowmapper;

import com.app.security.model.StoreCheckoutItem;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StoreCheckoutItemRowMapper implements RowMapper<StoreCheckoutItem> {

    @Override
    public StoreCheckoutItem mapRow(ResultSet resultSet, int i) throws SQLException {
        StoreCheckoutItem item = new StoreCheckoutItem();
        item.setStoreCheckoutItemId(resultSet.getString("store_checkout_item_id"));
        item.setStoreCheckoutId(resultSet.getString("store_checkout_id"));
        item.setStoreProductItemId(resultSet.getString("store_product_item_id"));
        item.setQuantity(resultSet.getInt("quantity"));
        item.setUnitPrice(resultSet.getBigDecimal("unit_price"));
        item.setSubtotal(resultSet.getBigDecimal("subtotal"));
        item.setCreatedAt(resultSet.getTimestamp("created_at"));
        return item;
    }
}
