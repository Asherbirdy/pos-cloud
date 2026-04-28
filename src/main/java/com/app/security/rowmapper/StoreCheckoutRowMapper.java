package com.app.security.rowmapper;

import com.app.security.enums.OrderStatus;
import com.app.security.model.StoreCheckout;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StoreCheckoutRowMapper implements RowMapper<StoreCheckout> {

    @Override
    public StoreCheckout mapRow(ResultSet resultSet, int i) throws SQLException {
        StoreCheckout checkout = new StoreCheckout();
        checkout.setStoreCheckoutId(resultSet.getString("store_checkout_id"));
        checkout.setStoreId(resultSet.getString("store_id"));
        checkout.setStoreShiftId(resultSet.getString("store_shift_id"));
        checkout.setMemberId(resultSet.getString("member_id"));
        checkout.setSettlePrice(resultSet.getBigDecimal("settle_price"));
        checkout.setOrderStatus(OrderStatus.valueOf(resultSet.getString("order_status")));
        checkout.setCheckoutAt(resultSet.getTimestamp("checkout_at"));
        checkout.setCreatedAt(resultSet.getTimestamp("created_at"));
        checkout.setUpdatedAt(resultSet.getTimestamp("updated_at"));
        return checkout;
    }
}
