package data;

import com.github.javafaker.Faker;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;
import java.util.Random;

public class DataHelper {

    public static String getCardNumberApproved() {
        return "4444 4444 4444 4441";
    }
    public static String getCardNumberDeclined() {
        return "4444 4444 4444 4442";
    }
    public static String getCardNumberWrong() {
        return "4444 4444 4444 4443";
    }

    public static String getOwnerValid() {
        Faker faker = new Faker();
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getOwnerInvalid() {
        Faker faker = new Faker(new Locale("ru"));
        return faker.name().lastName() + " " + faker.name().firstName();
    }

    public static String getMonthRandomValid() {
        Random random = new Random();
        int month = 1 + random.nextInt(12);
        return String.format("%02d", month);
    }

    public static String getMonth2DigitsInvalid() {
        Random random = new Random();
        Integer month = 13 + random.nextInt(87);
        return month.toString();
    }

    public static String getMonth1DigitInvalid() {
        Random random = new Random();
        Integer month = random.nextInt(10);
        return month.toString();
    }

    public static String getMonthNullInvalid() {
        return "00";
    }

    public static String getYearValid(){
        DateFormat df = new SimpleDateFormat("yy");
        String yearStr = df.format(Calendar.getInstance().getTime());
        Integer year = Integer.parseInt(yearStr) + 1;
        return year.toString();
    }

    public static String getYearInvalid(){
        DateFormat df = new SimpleDateFormat("yy");
        String yearStr = df.format(Calendar.getInstance().getTime());
        Integer year = Integer.parseInt(yearStr) - 1;
        return year.toString();
    }

    public static String getCVVValid() {
        Random random = new Random();
        Integer cvv = 100 + random.nextInt(900);
        return cvv.toString();
    }

    public static String getCVVV2DigitsInvalid() {
        Random random = new Random();
        Integer cvv = 10 + random.nextInt(90);
        return cvv.toString();
    }

    public static String getCVVVDigitInvalid() {
        Random random = new Random();
        Integer cvv = random.nextInt(10);
        return cvv.toString();
    }
}
