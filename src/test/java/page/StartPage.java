package page;


import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$$;
import static com.codeborne.selenide.Selenide.open;

public class StartPage {

        private SelenideElement btnDebitCard = $$("button").find(Condition.exactText("Купить"));
        private SelenideElement btnCreditCard = $$("button").find(Condition.exactText("Купить в кредит"));

        public StartPage() {
            open(System.getProperty("test.suturl"));
        }

        public DebitCardPage openDebitCardPage() {
            btnDebitCard.click();
            return new DebitCardPage();
        }

        public CreditCardPage openCreditPage() {
            btnCreditCard.click();
            return new CreditCardPage();
        }
}
