package ru.netology.web;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Configuration.holdBrowserOpen;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.*;


public class RegistrationTest {
    public static String getLocalDate(int days) {
        return LocalDate.now().plusDays(days).format(DateTimeFormatter.ofPattern("dd.MM.yyyy", new Locale("ru")));
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999/");
        holdBrowserOpen = true;
    }

    @Test
    void shouldBeValidCity() {
        String meetingDate = getLocalDate(13);
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
       // $("[placeholder='Город']").setValue("Санкт-Петербург");
        $("[data-test-id=date] [value]").doubleClick().sendKeys(meetingDate);
        //$("[data-test-id='date'][placeholder='Дата встречи']").sendKeys(meetingDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        // $x("//*[@data-test-id='phone']//input").click();
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//*[@data-test-id='notification']").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").
                shouldHave(Condition.text("Встреча успешно забронирована на " + meetingDate), Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNotValidCity() {
        String planningDate = getLocalDate(5);
        $x("//*[@data-test-id='city']//input").val("Павловск");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иванов Иван");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityEnglish() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("Moscow");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityEmpty() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Поле обязательно для заполнения")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCityWithSpecSymbols() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("Санкт_Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Доставка в выбранный город недоступна")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestDoubleFirstName() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов-Петров");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $x("//*[@data-test-id='notification']").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").shouldHave(
                Condition.text("Встреча успешно забронирована на " + planningDate),
                Duration.ofSeconds(15)
        );
    }

    @Test
    void shouldTestNameWithNum() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("14567 1564");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Допустимы только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameEnglish() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Ivan Ivanov");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Допустимы только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNameWithSpecSymbols() {
        String planningDate = getLocalDate(6);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов!");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Допустимы только русские буквы, пробелы и дефисы")).should(visible);
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithoutPlus() {
        String planningDate = getLocalDate(90);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("89998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithOneNumber() {
        String planningDate = getLocalDate(90);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+7999888776");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneWithSpecSymbols() {
        String planningDate = getLocalDate(90);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+7(999)-888-77 66");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Телефон указан неверно. Должно быть 11 цифр")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPhoneEmpty() {
        String planningDate = getLocalDate(90);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Поле обязательно для заполнения")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestNextDayMeeting() {
        String planningDate = getLocalDate(1);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Заказ на выбранную дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestPlus0days() {
        String planningDate = getLocalDate(0);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Заказ на выбранную дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestMinus5Days() {
        String planningDate = getLocalDate(-5);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Заказ на выбранную дату невозможен")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestFebruaryDays() {
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys("30.02.2023");
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $(withText("Неверно введена дата")).should(visible, Duration.ofSeconds(5));
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestUncheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").doubleClick();
        $x("//*[@class='button__text']").click();
        $(".input_invalid[data-test-id='agreement']").should(exist);
        $x("//*[@data-test-id='notification']").shouldNot(visible, Duration.ofSeconds(15));
    }

    @Test
    void shouldTestCheckedCheckedBox() {
        String planningDate = getLocalDate(4);
        $x("//*[@data-test-id='city']//input").val("Санкт-Петербург");
        $x("//*[@data-test-id='date']//input").doubleClick().sendKeys(planningDate);
        $x("//*[@data-test-id='name']//input").val("Иван Иванов");
        $x("//*[@data-test-id='phone']//input").val("+79998887766");
        $x("//*[@data-test-id='agreement']").click();
        $x("//*[@class='button__text']").click();
        $("[data-test-id='agreement'].checkbox_checked").should(exist);
        $x("//*[@data-test-id='notification']").should(visible, Duration.ofSeconds(15));
        $x("//*[@class='notification__content']").shouldHave(
                Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15));
    }
}