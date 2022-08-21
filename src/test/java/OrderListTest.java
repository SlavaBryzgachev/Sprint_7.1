import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
public class OrderListTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    @DisplayName("Получение списка заказов")
    @Description("Проверка, что список приходит не пустой")
    public void orderListBackTest(){
        given()
                .header("Content-type", "application/json")
                .and()
                .when()
                .get("/api/v1/orders")
                .then().assertThat().body("orders.id",notNullValue());
    }
}

