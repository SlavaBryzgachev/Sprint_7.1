import api.client.CourierClient;
import api.client.CourierGenerator;;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.ValidatableResponse;
import org.junit.Before;
import org.junit.Test;
import api.client.LoginCourier;
import static io.restassured.RestAssured.given;
import static org.apache.http.HttpStatus.SC_OK;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.junit.Assert.assertEquals;
public class LoginCourierTest {
    private CourierClient courierClient;
    private LoginCourier loginCourier;
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru/";
        courierClient = new CourierClient();
        loginCourier = CourierGenerator.loginCourierForTest();
    }

    @Test
    @DisplayName("Успешная авторизация курьера")
    @Description("Курьер может авторизоваться, код ответа 200")
    public void courierCanLogIn(){
        ValidatableResponse responseLogin = courierClient.login(loginCourier);
        int statusCodeLogin = responseLogin.extract().statusCode();
        assertEquals(SC_OK, statusCodeLogin);
    }
    @Test
    @DisplayName("При авторизации курьера, одно из обязательных полей не заполнено")
    @Description("Сообщение об ошибке , код ответа 400")
    public void passNotAllRequiredFields(){
        LoginCourier loginCourier = new LoginCourier("Sasha123", "");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().body( "code", equalTo(400))
                .body("message",equalTo("Недостаточно данных для входа"));
    }
    @Test
    @DisplayName("При авторизации курьера введен не верный логин или пароль")
    @Description("Сообщение об ошибке, код ответа 404")
    public void wrongLoginOrPassword(){
        LoginCourier loginCourier = new LoginCourier("Sasha123", "1234");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().body( "code",equalTo(404))
                .body("message", equalTo( "Учетная запись не найдена"));

    }
    @Test
    @DisplayName("Авторизация под несуществующим курьером")
    @Description("Код ответа 404")
    public void nonExistentUser(){
        LoginCourier loginCourier = new LoginCourier("Egor123", "1234");
        given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourier)
                .when()
                .post("/api/v1/courier/login")
                .then().assertThat().statusCode(404);
    }
    @Test
    @DisplayName("При успешной авторизации курьера, возвращается id")
    @Description("Возвращается id курьера, код ответа 200")
    public void successfulRequestReturnsId(){
        ValidatableResponse responseLogin = courierClient.login(loginCourier);
        int statusCodeLogin = responseLogin.extract().statusCode();
        assertEquals(SC_OK, statusCodeLogin);;
        int id = responseLogin.extract().path("id");
        assertThat(id, notNullValue());
    }
}
