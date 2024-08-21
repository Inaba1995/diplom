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
    void shouldConfirmPaymentWithValidDataCardApproved()  {
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
    void shouldConfirmCreditRequestWithValidDataCardApproved() {
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



    @Test
    @DisplayName("3 позитивный сценарий. Попытка оплаты тура по верно заполненной и недопустимой карте *4442, которую не одобрит банк.")
    @SneakyThrows
    void shouldConfirmPaymentWithValidDataCardDeclined(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberDeclined());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isSuccess("Ошибка!\n" +
                " Банк отказал в проведении операции.");
        debitCardPage.isErrorHidden();
        assertEquals("DECLINED", getRecordFromPayment().getStatus());
        assertNotEquals("", getRecordFromOrder().getId());
    }


    @Test
    @DisplayName("4 позитивный сценарий. Попытка оплаты тура в кредит по верно заполненной и недопустимой карте *4442, которую не одобрит банк.")
    @SneakyThrows
    void shouldConfirmCreditRequestWithValidDataCardDeclined(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberDeclined());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isSuccess("Ошибка!\n" +
                " Банк отказал в проведении операции.");
        creditCardPage.isErrorHidden();
        assertEquals("DECLINED", getRecordFromPayment().getStatus());
        assertNotEquals("", getRecordFromOrder().getId());
    }



@Test
@DisplayName("1 негативный сценарий. Попытка оплаты тура по верно заполненной несуществующей карте")
@SneakyThrows
void shouldWarningPaymentWithNonexistentCard(){
    val startPage = new StartPage();
    val debitCardPage = startPage.openDebitCardPage();
    debitCardPage.inputNumber(DataHelper.getCardNumberWrong());
    debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
    debitCardPage.inputYear(DataHelper.getYearValid());
    debitCardPage.inputOwner(DataHelper.getOwnerValid());
    debitCardPage.inputCvv(DataHelper.getCVVValid());
    debitCardPage.clickNext();
    debitCardPage.isError("Ошибка!\n" +
            " Банк отказал в проведении операции.");
    debitCardPage.isSuccessHidden();
    isPaymentNull() ;
    isCreditRequestNull();
}


    @Test
    @DisplayName("2 негативный сценарий. Попытка оплаты тура по верно заполненной несуществующей карте в кредит")
    @SneakyThrows
    void shouldWarningCreditRequestWithNonexistentCard(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberWrong());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isError("Ошибка!\n" +
                " Банк отказал в проведении операции.");
        creditCardPage.isSuccessHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("3 негативный сценарий. Попытка оплаты тура по неверно заполненной несуществующей карте.")
    @SneakyThrows
    void shouldWarningPaymentWithInvalidDataCard(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberWrong());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Номер карты", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("4 негативный сценарий. Попытка оплаты тура по неверно заполненной несуществующей карте в кредит.")
    @SneakyThrows
    void shouldWarningCreditRequestWithInvalidDataCard(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberWrong());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Номер карты", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("5 негативный сценарий. Попытка оплаты тура с пустым полем \"Номер карты\".")
    @SneakyThrows
    void shouldWarningPaymentWithEmptyDataCard(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber("");
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Номер карты", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("6 негативный сценарий. Попытка оплаты тура в кредит с пустым полем \"Номер карты\".")
    @SneakyThrows
    void shouldWarningCreditRequestWithEmptyDataCard(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber("");
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Номер карты", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("7 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441,\n" +
            "которую одобрит банк, но с неверно заполненным полем \"Владелец\"")
    @SneakyThrows
    void shouldWarningPaymentWithInvalidDataOwner(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerInvalid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Владелец", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("8 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            "которую одобрит банк, но с неверно заполненным полем \"Владелец\"")
    @SneakyThrows
    void shouldWarningCreditRequestWithInvalidDataOwner(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerInvalid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Владелец", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("9 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n" +
            "которую одобрит банк, но с пустым полем \"Владелец\"")
    @SneakyThrows
    void shouldWarningPaymentWithEmptyDataOwner(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner("");
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Владелец", "Поле обязательно для заполнения");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("10 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с пустым полем \"Владелец\" ")
    @SneakyThrows
    void shouldWarningCreditRequestWithEmptyDataOwner(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner("");
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Владелец", "Поле обязательно для заполнения");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("11 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк,  но с неверно заполненным полем \"Месяц\" - указан месяц более чем 12")
    @SneakyThrows
    void shouldWarningPaymentWithInvalidDataMonth(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonth2DigitsInvalid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Месяц", "Неверно указан срок действия карты");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("12 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с неверно заполненным полем \"Месяц\" - указан месяц более чем 12")
    @SneakyThrows
    void shouldWarningCreditRequestWithInvalidDataMonth(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonth2DigitsInvalid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Месяц", "Неверно указан срок действия карты");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("13 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк,  но с неверно заполненным полем \"Месяц\" - указан нулевой месяц")
    @SneakyThrows
    void shouldWarningPaymentWithZeroDataMonth(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthNullInvalid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Месяц", "Неверно указан срок действия карты");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("14 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с неверно заполненным полем \"Месяц\" - указан нулевой месяц")
    @SneakyThrows
    void shouldWarningCreditRequestWithZeroDataMonth(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthNullInvalid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Месяц", "Неверно указан срок действия карты");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("15 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк,  но с неверно заполненным полем \"Месяц\" - указана одна цифра")
    @SneakyThrows
    void shouldWarningPaymentWithOneDigitDataMonth(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonth1DigitInvalid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Месяц", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("16 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с неверно заполненным полем \"Месяц\" - указана одна цифра")
    @SneakyThrows
    void shouldWarningCreditRequestWithOneDigitDataMonth(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonth1DigitInvalid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Месяц", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("17 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с пустым полем \"Месяц\"")
    @SneakyThrows
    void shouldWarningPaymentWithEmptyDataMonth(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth("");
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Месяц", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("18 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            "которую одобрит банк, но с пустым полем \"Месяц\"")
    @SneakyThrows
    void shouldWarningCreditRequestWithEmptyDataMonth(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth("");
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Месяц", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("19 негативный сценарий.  Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n"+
           "которую одобрит банк, но с истёкшим срок действия - указан год ранее текущего")
    @SneakyThrows
    void shouldWarningPaymentWithExpiredDataYear(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearInvalid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Год", "Истёк срок действия карты");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("20 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с истёкшим срок действия - указан год ранее текущего")
    @SneakyThrows
    void shouldWarningCreditRequestWithExpiredDataYear(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearInvalid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Год", "Истёк срок действия карты");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("21 негативный сценарий.  Попытка оплаты тура по верно заполненной и допустимой карте *4441, \n"+
            "которую одобрит банк, но с пустым полем \"Год\"")
    @SneakyThrows
    void shouldWarningPaymentWithEmptyDataYear(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear("");
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Год", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("22 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с пустым полем \"Год\"")
    @SneakyThrows
    void shouldWarningCreditRequestWithEmptyDataYear(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear("");
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Год", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("23 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441,\n" +
            "  которую одобрит банк, но с истёкшим срок действия - указан месяц ранее текущего и текущий год")
    @SneakyThrows
    void shouldWarningPaymentWithExpiredDataMonth(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonth2DigitsExpiredInvalid());
        debitCardPage.inputYear(DataHelper.getYearCurrentValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVValid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("Месяц", "Неверно указан срок действия карты");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("24 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк,  но с истёкшим срок действия - указан месяц ранее текущего и текущий год")
    @SneakyThrows
    void shouldWarningCreditRequestWithExpiredDataMonth(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonth2DigitsExpiredInvalid());
        creditCardPage.inputYear(DataHelper.getYearCurrentValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVValid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("Месяц", "Неверно указан срок действия карты");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("25 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441,\n" +
            " которую одобрит банк но с неверно заполненным полем \"CVC/CVC\" - указано две цифры")
    @SneakyThrows
    void shouldWarningPaymentWithTwoDigitsDataCVV(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVV2DigitsInvalid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("CVC/CVV", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("26 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с неверно заполненным полем \"CVC/CVC\" - указано две цифры")
    @SneakyThrows
    void shouldWarningCreditRequestWithTwoDigitsDataCVV(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVV2DigitsInvalid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("CVC/CVV", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("27 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441,\n" +
            " которую одобрит банк но с неверно заполненным полем \"CVC/CVC\" - указана одна цифра")
    @SneakyThrows
    void shouldWarningPaymentWithOneDigitDataCVV(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv(DataHelper.getCVVVDigitInvalid());
        debitCardPage.clickNext();
        debitCardPage.isInputError("CVC/CVV", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("28 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк, но с неверно заполненным полем \"CVC/CVC\" - указана одна цифра")
    @SneakyThrows
    void shouldWarningCreditRequestWithOneDigitDataCVV(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVV2DigitsInvalid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("CVC/CVV", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }


    @Test
    @DisplayName("29 негативный сценарий. Попытка оплаты тура по верно заполненной и допустимой карте *4441,\n" +
            " которую одобрит банк но с пустым полем \"CVC/CVV\"")
    @SneakyThrows
    void shouldWarningPaymentWithEmptyDataCVV(){
        val startPage = new StartPage();
        val debitCardPage = startPage.openDebitCardPage();
        debitCardPage.inputNumber(DataHelper.getCardNumberApproved());
        debitCardPage.inputMonth(DataHelper.getMonthRandomValid());
        debitCardPage.inputYear(DataHelper.getYearValid());
        debitCardPage.inputOwner(DataHelper.getOwnerValid());
        debitCardPage.inputCvv("");
        debitCardPage.clickNext();
        debitCardPage.isInputError("CVC/CVV", "Неверный формат");
        debitCardPage.isSuccessHidden();
        debitCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }

    @Test
    @DisplayName("30 негативный сценарий. Попытка оплаты тура в кредит по верно заполненной и допустимой карте *4441, \n" +
            " которую одобрит банк но с пустым полем \"CVC/CVC\"")
    @SneakyThrows
    void shouldWarningCreditRequestWithEmptyDataCVV(){
        val startPage = new StartPage();
        val creditCardPage = startPage.openCreditPage();
        creditCardPage.inputNumber(DataHelper.getCardNumberApproved());
        creditCardPage.inputMonth(DataHelper.getMonthRandomValid());
        creditCardPage.inputYear(DataHelper.getYearValid());
        creditCardPage.inputOwner(DataHelper.getOwnerValid());
        creditCardPage.inputCvv(DataHelper.getCVVV2DigitsInvalid());
        creditCardPage.clickNext();
        creditCardPage.isInputError("CVC/CVV", "Неверный формат");
        creditCardPage.isSuccessHidden();
        creditCardPage.isErrorHidden();
        isPaymentNull() ;
        isCreditRequestNull();
    }
}
