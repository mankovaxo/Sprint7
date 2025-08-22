package order;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import io.restassured.response.Response;
import steps.OrderTestSteps;

import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DisplayName("Тесты для работы с заказами")
class OrderListTest {
    private final OrderTestSteps steps = new OrderTestSteps();

    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка успешного получения списка заказов")
    void getOrdersListReturnsValidResponse() {
        Response response = steps.getOrdersList();
        response.then().statusCode(SC_OK);
        assertThat("Список заказов должен существовать",
                response.jsonPath().getList("orders"), is(not(empty())));
    }
}