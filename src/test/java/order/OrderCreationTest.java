package order;

import io.qameta.allure.Description;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import io.restassured.response.Response;
import models.Order;
import steps.OrderTestSteps;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.params.provider.Arguments.arguments;

class OrderCreationTest {
    private final OrderTestSteps steps = new OrderTestSteps();
    private Response orderResponse;

    @ParameterizedTest(name = "{1}")
    @MethodSource("colorDataProvider")
    @DisplayName("Создание заказа с разными цветами самоката")
    @Description("Проверяет возможность создания заказа с различными вариантами выбора цвета")
    void testOrderCreationWithDifferentColors(List<String> colors, String testName) {
        Order order = new Order(
                "Иван", "Иванов", "ул. Ленина, 1", "1",
                "+79998887766", 5, "2024-12-31", "Комментарий", colors
        );
        orderResponse = steps.createOrder(order);
        steps.verifyOrderTrackExists(orderResponse);
    }

    static Stream<Arguments> colorDataProvider() {
        return Stream.of(
                arguments(List.of("BLACK"), "Один цвет: BLACK"),
                arguments(List.of("GREY"), "Один цвет: GREY"),
                arguments(List.of("BLACK", "GREY"), "Два цвета: BLACK и GREY"),
                arguments(List.of(), "Без указания цвета")
        );
    }
}