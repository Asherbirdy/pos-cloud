package com.app.security.rowmapper;

import com.app.security.enums.ShiftStatus;
import com.app.security.model.StoreShift;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class StoreShiftRowMapper implements RowMapper<StoreShift> {

    @Override
    public StoreShift mapRow(ResultSet resultSet, int i) throws SQLException {
        StoreShift shift = new StoreShift();
        shift.setStoreShiftId(resultSet.getString("store_shift_id"));
        shift.setStoreId(resultSet.getString("store_id"));
        shift.setMemberId(resultSet.getString("member_id"));

        Date date = resultSet.getDate("date");
        shift.setDate(date == null ? null : date.toLocalDate());

        shift.setStatus(ShiftStatus.valueOf(resultSet.getString("status")));
        shift.setOpenTime(resultSet.getTimestamp("open_time"));
        shift.setCloseTime(resultSet.getTimestamp("close_time"));
        shift.setCreatedAt(resultSet.getTimestamp("created_at"));
        shift.setUpdatedAt(resultSet.getTimestamp("updated_at"));

        return shift;
    }
}
