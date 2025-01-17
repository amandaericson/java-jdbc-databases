package com.pluralsight.order.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.pluralsight.order.dto.OrderDto;
import com.pluralsight.order.dto.ParamsDto;
import com.pluralsight.order.util.Database;
import com.pluralsight.order.util.ExceptionHandler;

/**
 * DAO to get an order
 */
public class GetOrderDao {
    private String query = "SELECT * FROM orders o WHERE o.order_id = ?";
    private Database database;

    /**
     * Constructor
     * @param database Database object
     */
    public GetOrderDao(Database database) {
        this.database = database;
    }

    /**
     * Gets an order by its ID
     * @param paramsDto Object with the parameters for the operation
     * @return Object with the main information of an order
     */
    public OrderDto getOrderById(ParamsDto paramsDto) {
        OrderDto orderDto = null;

        try (Connection con = database.getConnection();
             PreparedStatement ps = createPreparedStatement(con, paramsDto.getOrderId());
             ResultSet rs = createResultSet(ps)
        ) {
            orderDto = new OrderDto();
            while(rs.next()){
                orderDto.setOrderId(rs.getLong("ORDER_ID"));
                orderDto.setCustomerId(rs.getLong("ORDER_CUSTOMER_ID"));
                orderDto.setDate(rs.getDate("ORDER_DATE"));
                orderDto.setStatus(rs.getString("ORDER_STATUS"));
            }
        } catch (SQLException ex) {
            ExceptionHandler.handleException(ex);
        }

        return orderDto;
    }

    /**
     * Creates a PreparedStatement object to get an order
     * @param con Connnection object
     * @param orderId Order ID to set on the PreparedStatement
     * @return A PreparedStatement object
     * @throws SQLException In case of an error
     */
    private PreparedStatement createPreparedStatement(Connection con, long orderId) throws SQLException {

        PreparedStatement preparedStatement = con.prepareStatement(query);
        preparedStatement.setLong(1, orderId);
        
        return preparedStatement;
    }

    /**
     * Creates a ResultSet object to get the results of the query
     * @param ps PreparedStatement object to create the query
     * @return A ResultSet object
     * @throws SQLException In case of an error
     */
    private ResultSet createResultSet(PreparedStatement ps) throws SQLException {
        ResultSet resultSet = ps.executeQuery();

        return resultSet;
    }
}
