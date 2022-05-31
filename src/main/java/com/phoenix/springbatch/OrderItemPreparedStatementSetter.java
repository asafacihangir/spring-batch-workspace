package com.phoenix.springbatch;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;

public class OrderItemPreparedStatementSetter implements ItemPreparedStatementSetter<Order> {

  @Override
  public void setValues(Order item, PreparedStatement ps) throws SQLException {
    ps.setLong(1, item.getOrderId());
    ps.setString(2, item.getFirstName());
    ps.setString(3, item.getLastName());
    ps.setString(4,  item.getEmail());
    ps.setString(5,item.getItemId());
    ps.setString(6, item.getItemName());
    ps.setBigDecimal(7, item.getCost());
    ps.setDate(8, new Date(item.getShipDate().getTime()));

  }

}
