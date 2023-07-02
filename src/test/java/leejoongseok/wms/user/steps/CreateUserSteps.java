package leejoongseok.wms.user.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.user.featrue.CreateUser;
import org.springframework.http.HttpStatus;

public class CreateUserSteps {
    public static class Request {
        private String name = "오집품";

        public Request name(final String name) {
            this.name = name;
            return this;
        }

        public Scenario request() {
            final CreateUser.Request request = new CreateUser.Request(
                    name
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/users")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            return new Scenario();
        }
    }
}
