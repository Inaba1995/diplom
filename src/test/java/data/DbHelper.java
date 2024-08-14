package data;

import data.entity.*;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertNull;

public class DbHelper {

    private static String url = System.getProperty("test.dburl");
    private static String user = System.getProperty("test.dblogin");
    private static String password = System.getProperty("test.dbpassword");


    public static Payment getRecordFromPayment() throws SQLException {
        val codeSQL = "SELECT * FROM payment_entity ORDER BY created DESC";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, codeSQL, new BeanHandler<>(data.entity.Payment.class));
        }
    }

    public static CreditRequest getRecordFromCredit() throws SQLException {
        val codeSQL = "SELECT * FROM credit_request_entity ORDER BY created DESC";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, codeSQL, new BeanHandler<>(data.entity.CreditRequest.class));
        }
    }

    public static Order getRecordFromOrder() throws SQLException {
        val codeSQL = "SELECT * FROM order_entity ORDER BY created DESC";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, codeSQL, new BeanHandler<>(data.entity.Order.class));
        }
    }

    public static void isPaymentNull() throws SQLException {
        val codeSQL = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val payment = runner.query(conn, codeSQL , new BeanHandler<>(data.entity.Payment.class));
            assertNull(payment);
        }
    }

    public static void isCreditRequestNull() throws SQLException {
        val codeSQL = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditRequest = runner.query(conn, codeSQL , new BeanHandler<>(data.entity.Payment.class));
            assertNull(creditRequest);
        }
    }

    public static void isOrderNull() throws SQLException {
        val codeSQL = "SELECT * FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val order = runner.query(conn, codeSQL , new BeanHandler<>(data.entity.Payment.class));
            assertNull(order);
        }
    }

    public static void deleteAll() throws SQLException{
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, "DELETE FROM payment_entity");
            runner.update(conn, "DELETE FROM credit_request_entity");
            runner.update(conn, "DELETE FROM order_entity");
        }
    }

}
