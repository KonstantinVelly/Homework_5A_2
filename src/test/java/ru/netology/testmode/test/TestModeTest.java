package ru.netology.testmode.test;

import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static ru.netology.testmode.data.DataGenerator.Registration.getRegisteredUser;
import static ru.netology.testmode.data.DataGenerator.Registration.getUser;
import static ru.netology.testmode.data.DataGenerator.getRandomLogin;
import static ru.netology.testmode.data.DataGenerator.getRandomPassword;

class TestModeTest {

   // private WebDriver driver;

    @BeforeEach
    void setup() {

        open("http://localhost:9999");

       // ChromeOptions options = new ChromeOptions();
       // options.addArguments("--disable-dev-shm-usage");
       // options.addArguments("--no-sandbox");
        // options.addArguments("--headless");
        //driver = new ChromeDriver(options);
    }

    @Test
    @DisplayName("Should successfully login with active registered user")
    void IfRegisteredAndActiveUser() {
        var registeredUser = getRegisteredUser("active");
        $("[data-test-id='login'] input").setValue(registeredUser.getLogin());
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[id='root']").shouldHave(Condition.text("Личный кабинет"));

    }

    @Test
    @DisplayName("Should get error message if login with not registered user")
    void IfNotRegisteredUser() {
        var notRegisteredUser = getUser("active");
        $("[data-test-id='login'] input").setValue(notRegisteredUser.getLogin());
        $("[data-test-id='password'] input").setValue(notRegisteredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with blocked registered user")
    void IfBlockedUser() {
        var blockedUser = getRegisteredUser("blocked");
        $("[data-test-id='login'] input").setValue(blockedUser.getLogin());
        $("[data-test-id='password'] input").setValue(blockedUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Пользователь заблокирован"));

    }

    @Test
    @DisplayName("If login is wrong - show a message")
    void IfWrongLogin() {
        var registeredUser = getRegisteredUser("active");
        var wrongLogin = getRandomLogin();
        $("[data-test-id='login'] input").setValue((wrongLogin));
        $("[data-test-id='password'] input").setValue(registeredUser.getPassword());
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }

    @Test
    @DisplayName("Should get error message if login with wrong password")
    void IfWrongPassword() {
        var registeredUser = getRegisteredUser("active");
        var wrongPassword = getRandomPassword();
        $("[data-test-id='login'] input").setValue((registeredUser.getLogin()));
        $("[data-test-id='password'] input").setValue(wrongPassword);
        $("[data-test-id='action-login']").click();
        $("[class='notification__content']").shouldHave(Condition.text("Ошибка! Неверно указан логин или пароль"));

    }
}
