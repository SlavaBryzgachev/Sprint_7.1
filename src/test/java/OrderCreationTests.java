import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import api.client.OrderCreate;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;
@RunWith(Parameterized.class)
public class OrderCreationTests {
    private final String[] color;
    public OrderCreationTests(String[] color) {
        this.color = color;
    }
    @Parameterized.Parameters(name = "Тестовые данные: {0} {1}")
    public static Object[][] data(){
        return new Object[][]{
                {new String[]{"GRAY", "BLACK"}},
                {new String[]{"GRAY"}},
                {new String[]{"BLACK"}},
                {new String[]{}}
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
    }
    @Test
    @DisplayName("Создание заказа")
    @Description("Заказ успешно создается, код ответа 201")
    public void OrderCreateTesting() {
        OrderCreate orderCreate = new OrderCreate("Олег","Олегов","Усачева, 14","Кропотнинская","7 800 355 35 35","5","2020-06-06","Saske, come back to Konoha");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(orderCreate)
                .when()
                .post("/api/v1/orders")
.then().assertThat().statusCode(201)
.and().assertThat().body("track", notNullValue());
    }
}




