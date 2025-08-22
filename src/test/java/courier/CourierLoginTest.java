package courier;

import io.qameta.allure.Description;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import models.Courier;
import steps.CourierTestSteps;
import utils.Random;

import static org.apache.http.HttpStatus.*;

class CourierLoginTest {
    private final CourierTestSteps steps = new CourierTestSteps();
    private Courier courier;
    private String courierId;

    @BeforeEach
    void setUp() {
        courier = Random.createRandomCourier();
        steps.createCourier(courier);
    }

    @AfterEach
    void tearDown() {
        if (courierId != null) {
            steps.deleteCourier(courierId);
        }
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Проверяет, что курьер может авторизоваться с корректными данными")
    void testSuccessfulCourierLogin() {
        Response response = steps.loginCourier(courier);
        response.then().statusCode(SC_OK);
        courierId = response.jsonPath().getString("id");
    }

    @Test
    @DisplayName("Авторизация с неверным паролем")
    @Description("Проверяет, что система возвращает ошибку при неверном пароле")
    void testLoginWithWrongPassword() {
        Courier wrongPasswordCourier = new Courier(courier.getLogin(), "wrong_password", null);
        Response response = steps.loginCourier(wrongPasswordCourier);
        steps.verifyCourierCreationError(response, SC_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Авторизация с неверным логином")
    @Description("Проверяет, что система возвращает ошибку при неверном логине")
    void testLoginWithWrongLogin() {
        Courier wrongLoginCourier = new Courier("nonexistent_login", courier.getPassword(), null);
        Response response = steps.loginCourier(wrongLoginCourier);
        steps.verifyCourierCreationError(response, SC_NOT_FOUND, "Учетная запись не найдена");
    }

    @Test
    @DisplayName("Авторизация без пароля")
    @Description("Проверяет, что система возвращает ошибку при отсутствии пароля")
    void testLoginWithoutPassword() {
        Courier noPasswordCourier = new Courier(courier.getLogin(), "", null);
        Response response = steps.loginCourier(noPasswordCourier);
        steps.verifyCourierCreationError(response, SC_BAD_REQUEST, "Недостаточно данных для входа");
    }

    @Test
    @DisplayName("Авторизация без логина")
    @Description("Проверяет, что система возвращает ошибку при отсутствии логина")
    void testLoginWithoutLogin() {
        Courier noLoginCourier = new Courier("", courier.getPassword(), null);
        Response response = steps.loginCourier(noLoginCourier);
        steps.verifyCourierCreationError(response, SC_BAD_REQUEST, "Недостаточно данных для входа");
    }
}