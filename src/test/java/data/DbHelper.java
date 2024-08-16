package data;

import data.entity.*;
import lombok.SneakyThrows;
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


    @SneakyThrows
    public static Payment getRecordFromPayment()  {
        val codeSQL = "SELECT * FROM payment_entity ORDER BY created DESC";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, codeSQL, new BeanHandler<>(data.entity.Payment.class));
        }
    }

    @SneakyThrows
    public static CreditRequest getRecordFromCredit() {
        val codeSQL = "SELECT * FROM credit_request_entity ORDER BY created DESC";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, codeSQL, new BeanHandler<>(data.entity.CreditRequest.class));
        }
    }

    @SneakyThrows
    public static Order getRecordFromOrder()  {
        val codeSQL = "SELECT * FROM order_entity ORDER BY created DESC";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            return runner.query(conn, codeSQL, new BeanHandler<>(data.entity.Order.class));
        }
    }

    @SneakyThrows
    public static void isPaymentNull() {
        val codeSQL = "SELECT * FROM payment_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val payment = runner.query(conn, codeSQL , new BeanHandler<>(data.entity.Payment.class));
            assertNull(payment);
        }
    }

    @SneakyThrows
    public static void isCreditRequestNull() {
        val codeSQL = "SELECT * FROM credit_request_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val creditRequest = runner.query(conn, codeSQL , new BeanHandler<>(data.entity.Payment.class));
            assertNull(creditRequest);
        }
    }

    @SneakyThrows
    public static void isOrderNull() {
        val codeSQL = "SELECT * FROM order_entity";
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            val order = runner.query(conn, codeSQL , new BeanHandler<>(data.entity.Payment.class));
            assertNull(order);
        }
    }

    @SneakyThrows
    public static void deleteAll() {
        val runner = new QueryRunner();
        try (val conn = DriverManager.getConnection(url, user, password)) {
            runner.update(conn, "DELETE FROM payment_entity");
            runner.update(conn, "DELETE FROM credit_request_entity");
            runner.update(conn, "DELETE FROM order_entity");
        }
    }

}
