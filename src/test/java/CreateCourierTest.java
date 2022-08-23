import api.client.CourierClient;
import api.client.CourierGenerator;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.hamcrest.CoreMatchers;
import org.junit.Before;
import org.junit.Test;
import api.client.CreateCourier;
import api.client.LoginCourier;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.*;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.junit.Assert.*;
public class CreateCourierTest {
    private CreateCourier createCourier;
    private  CourierClient courierClient;
    private LoginCourier loginCourier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        createCourier = CourierGenerator.getDefault();
        courierClient = new CourierClient();
        loginCourier = CourierGenerator.loginCourierAfterCreated();
    }
    @Test
    @DisplayName("Успешное создание нового курьера")
    @Description("Курьера можно создать, код ответа 201")
    public void courierCanBeCreated() {
        ValidatableResponse responseCreate = courierClient.create(createCourier);
        int statusCode = responseCreate.extract().statusCode();
        assertEquals(SC_CREATED,statusCode);
        boolean isCourierCreated = responseCreate.extract().path("ok");
        assertTrue(isCourierCreated);

        ValidatableResponse responseLogin = courierClient.login(loginCourier);
        int statusCodeLogin = responseLogin.extract().statusCode();
        assertEquals(SC_OK, statusCodeLogin);
        int id = responseLogin.extract().path("id");
        assertThat(id, CoreMatchers.notNullValue());

        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Создание двух одинаковых курьеров")
    @Description("Ошибка, код ответа 409")
    public void cannotCreateTwoIdenticalCouriers() {
        ValidatableResponse responseCreate = courierClient.create(createCourier);
        int statusCode = responseCreate.extract().statusCode();
        assertEquals(SC_CREATED,statusCode);
        boolean isCourierCreated = responseCreate.extract().path("ok");
        assertTrue(isCourierCreated);

        ValidatableResponse responseCreateSecondTime = courierClient.create(createCourier);
        int statusCode2 = responseCreateSecondTime.extract().statusCode();
        assertEquals(SC_CONFLICT,statusCode2);

        ValidatableResponse responseLogin = courierClient.login(loginCourier);
        int statusCodeLogin = responseLogin.extract().statusCode();
        assertEquals(SC_OK, statusCodeLogin);
        int id = responseLogin.extract().path("id");
        assertThat(id, CoreMatchers.notNullValue());

        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("При создании курьера не все обязательные поля заполнены")
    @Description("Ошибка, код ответа 400")
    public void passNotAllRequiredFields() {
        CreateCourier courier = new CreateCourier("Oleg111", "", "");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(courier)
                .when()
                .post("/api/v1/courier")
                .then().assertThat().statusCode(400);
    }
    @Test
    @DisplayName("При создании курьера возвращается правильный ответ")
    @Description("Когда курьер создан в теле ответа приходит верное сообщение")
    public void queryReturnsTheCorrectAnswer() {
        ValidatableResponse responseCreate = courierClient.create(createCourier);
        int statusCodeLogin = responseCreate.extract().statusCode();
        assertEquals(SC_CREATED, statusCodeLogin);
        responseCreate.body("ok",equalTo(true));

        ValidatableResponse responseLogin = courierClient.login(loginCourier);
        int statusCodeLogin2 = responseLogin.extract().statusCode();
        assertEquals(SC_OK, statusCodeLogin2);
        int id = responseLogin.extract().path("id");
        assertThat(id, CoreMatchers.notNullValue());

        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().statusCode(200);
    }
    @Test
    @DisplayName("Создание 2 курьеров с одинаковым логином")
    @Description("Ошибка, код ответа 409")
    public void sameLogin() {
        ValidatableResponse responseCreate = courierClient.create(createCourier);
        int statusCode = responseCreate.extract().statusCode();
        assertEquals(SC_CREATED,statusCode);
        boolean isCourierCreated = responseCreate.extract().path("ok");
        assertTrue(isCourierCreated);

        ValidatableResponse responseCreate2 = courierClient.create(createCourier);
        int statusCode2 = responseCreate2.extract().statusCode();
        assertEquals(SC_CONFLICT,statusCode2);
        responseCreate2.body("message", equalTo("Этот логин уже используется. Попробуйте другой."));

        ValidatableResponse responseLogin = courierClient.login(loginCourier);
        int statusCodeLogin = responseLogin.extract().statusCode();
        assertEquals(SC_OK, statusCodeLogin);
        int id = responseLogin.extract().path("id");
        assertThat(id, CoreMatchers.notNullValue());

        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{id}", id)
                .then().assertThat().statusCode(200);
    }
}
