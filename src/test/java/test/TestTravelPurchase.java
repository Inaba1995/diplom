package test;

import com.codeborne.selenide.logevents.SelenideLogger;
import lombok.SneakyThrows;
import lombok.val;
import org.junit.jupiter.api.*;
import io.qameta.allure.selenide.AllureSelenide;
import page.StartPage;
import data.*;

import java.sql.SQLException;

import static data.DbHelper.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class TestTravelPurchase {

    @AfterEach
    @DisplayName("Очистка БД перед всеми тестами")
    void clearDB() throws SQLException {
        deleteAll();
    }

    @BeforeAll
    static void setupAll() {
        SelenideLogger.addListener("allure", new AllureSelenide());
    }

    @AfterAll
    static void tearDownAll() {
        SelenideLogger.removeListener("allure");
    }

    @Test
    @DisplayName("1 позитивный сценарий. Оплата по дебетовой карте *4441, валидные данные. " +
            "Операция одобрена, в бд статус APPROVED, создаются записи в payment_entity и order_entity")
    @SneakyThrows
    void shouldConfirmPaymentWithValidDataCardOne()  {
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isSuccess("Успешно\n" +
                "Операция одобрена Банком.");
        debitCardPage.isErrorHidden();
        assertEquals("APPROVED", getRecordFromPayment().getStatus());
        assertNotEquals("", getRecordFromOrder().getId());
    }

    @Test
    @DisplayName("2 позитивный сценарий. Оплата по кредитной карте *4441, валидные данные. " +
            "Операция одобрена, в бд статус APPROVED, создаются записи в credit_request_entity и order_entity")
    @SneakyThrows
    void shouldConfirmCreditRequestWithValidDataCardOne() {
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isSuccess("Успешно\n" +
                "Операция одобрена Банком.");
        creditCardPage.isErrorHidden();
        assertEquals("APPROVED", getRecordFromCredit().getStatus());
        assertNotEquals("", getRecordFromOrder().getId());
    }
}
