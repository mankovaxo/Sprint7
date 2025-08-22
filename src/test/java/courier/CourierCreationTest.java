package courier;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import models.Courier;
import steps.CourierTestSteps;
import utils.Random;

import static org.apache.http.HttpStatus.*;

public class CourierCreationTest {
    private final CourierTestSteps steps = new CourierTestSteps();
    private Courier courier;

    @Test
    @DisplayName("Успешное создание курьера")
    @Description("Проверяет, что курьер может быть успешно создан при корректных данных")
    public void testSuccessfulCourierCreation() {
        courier = Random.createRandomCourier();
        Response response = steps.createCourier(courier);
        steps.verifyCourierCreatedSuccessfully(response);
    }

    @Test
    @DisplayName("Создание дубликата курьера")
    @Description("Проверяет, что нельзя создать двух одинаковых курьеров")
    public void testDuplicateCourierCreation() {
        courier = Random.createRandomCourier();
        steps.createCourier(courier);
        Response duplicateResponse = steps.createCourier(courier);
        steps.verifyCourierCreationError(duplicateResponse, SC_CONFLICT, "Этот логин уже используется. Попробуйте другой.");
    }

    @Test
    @DisplayName("Создание курьера без логина")
    @Description("Проверяет, что нельзя создать курьера без указания логина")
    public void testCourierCreationWithoutLogin() {
        courier = new Courier(null, "validPass", "name");
        Response response = steps.createCourier(courier);
        steps.verifyCourierCreationError(response, SC_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Создание курьера без пароля")
    @Description("Проверяет, что нельзя создать курьера без указания пароля")
    public void testCourierCreationWithoutPassword() {
        courier = new Courier("validLogin", null, "name");
        Response response = steps.createCourier(courier);
        steps.verifyCourierCreationError(response, SC_BAD_REQUEST, "Недостаточно данных для создания учетной записи");
    }
}