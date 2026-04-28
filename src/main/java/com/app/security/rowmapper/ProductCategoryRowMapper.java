package com.app.security.rowmapper;

import com.app.security.model.ProductCategory;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class ProductCategoryRowMapper implements RowMapper<ProductCategory> {

    @Override
    public ProductCategory mapRow(ResultSet resultSet, int i) throws SQLException {
        ProductCategory productCategory = new ProductCategory();
        productCategory.setProduct_category_id(resultSet.getString("product_category_id"));
        productCategory.setName(resultSet.getString("name"));
        productCategory.setStore_id(resultSet.getString("store_id"));
        productCategory.setCreatedAt(resultSet.getTimestamp("created_at"));
        productCategory.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return productCategory;
    }
}
