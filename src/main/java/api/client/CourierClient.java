package api.client;
import io.restassured.response.ValidatableResponse;
import static io.restassured.RestAssured.given;
public class CourierClient {
    public ValidatableResponse create(CreateCourier createCourier) {
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(createCourier)
                .when()
                .post("/api/v1/courier")
                .then();
    }
    public ValidatableResponse login(LoginCourier loginCourier){
        return given()
                .header("Content-type", "application/json")
                .and()
                .body(loginCourier)
                .when()
                .post("/api/v1/courier/login")
                .then();
    }
}








