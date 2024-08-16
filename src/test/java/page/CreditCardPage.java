package page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;

public class CreditCardPage {

    private SelenideElement heading = $$(".heading").find(Condition.exactText("Кредит по данным карты"));
    private ElementsCollection form = $$(".input__inner");
    private SelenideElement fNumber = form.findBy(Condition.text("Номер карты")).$(".input__control");
    private SelenideElement fMonth = form.findBy(Condition.text("Месяц")).$(".input__control");
    private SelenideElement fYear = form.findBy(Condition.text("Год")).$(".input__control");
    private SelenideElement fOwner = form.findBy(Condition.text("Владелец")).$(".input__control");
    private SelenideElement fCVC = form.findBy(Condition.text("CVC/CVV")).$(".input__control");
    private SelenideElement btnNext = $$("button").find(Condition.exactText("Продолжить"));
    private SelenideElement mesSuccess = $(".notification_status_ok");
    private SelenideElement mesError = $(".notification_status_error");

    public CreditCardPage() {
        heading.shouldBe(Condition.visible);
    }

    public void inputNumber(String Number) {
        fNumber.setValue(Number);
    }

    public void inputMonth(String Month) {
        fMonth.setValue(Month);
    }

    public void inputYear(String Year) {
        fYear.setValue(Year);
    }

    public void inputOwner(String owner) {
        fOwner.setValue(owner);
    }

    public void inputCvv(String Cvv) {
        fCVC.setValue(Cvv);
    }

    public void clickNext() {
        btnNext.click();
    }


    // "Успешно! Операция одобрена Банком"
    public void isSuccess(String mesText) {
        mesSuccess.shouldBe(Condition.visible, Duration.ofSeconds(20)).shouldBe(Condition.text(mesText));
    }

    // "Ошибка! Банк отказал в проведении операции"
    public void isError(String mesText) {
        mesError.shouldBe(Condition.visible,  Duration.ofSeconds(20)).shouldBe(Condition.text(mesText));
    }

    public void isSuccessHidden() {
        mesSuccess.shouldBe(Condition.hidden,  Duration.ofSeconds(10));
    }

    public void isErrorHidden() {
        mesError.shouldBe(Condition.hidden,  Duration.ofSeconds(10));
    }

    // "Номер карты"
    // "Неверный формат"
    public void isInputError(String numberText, String errText) {
        form.findBy(Condition.text(numberText)).$(".input__sub").shouldHave(Condition.exactText(errText)).shouldBe(Condition.visible);
    }

    // "Месяц"
    // "Неверно указан срок действия карты"

    // "Год"
    // "Истёк срок действия карты"

    // "CVC/CVV"
    // "Неверный формат"

    // "Владелец"
    // "Поле обязательно для заполнения"

    // "Месяц"
    // "Неверный формат"

    // "Год"
    // "Неверный формат"
}
