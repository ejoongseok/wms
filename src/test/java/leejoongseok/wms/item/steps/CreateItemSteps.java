package leejoongseok.wms.item.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.item.domain.Category;
import leejoongseok.wms.item.domain.TemperatureZone;
import leejoongseok.wms.item.feature.CreateItem;
import org.springframework.http.HttpStatus;

public class CreateItemSteps {
    public static class Request {
        private String itemName = "itemName";
        private String itemBarcode = "itemBarcode";
        private String description = "description";
        private String brandName = "brandName";
        private String makerName = "maker";
        private Integer widthInMillimeters = 10;
        private Integer lengthInMillimeters = 10;
        private Integer heightInMillimeters = 10;
        private Integer weightInGrams = 10;
        private TemperatureZone temperatureZone = TemperatureZone.ROOM_TEMPERATURE;
        private Category category = Category.ELECTRONICS;

        public Request itemName(final String itemName) {
            this.itemName = itemName;
            return this;
        }

        public Request itemBarcode(final String itemBarcode) {
            this.itemBarcode = itemBarcode;
            return this;
        }

        public Request description(final String description) {
            this.description = description;
            return this;
        }

        public Request brandName(final String brandName) {
            this.brandName = brandName;
            return this;
        }

        public Request makerName(final String makerName) {
            this.makerName = makerName;
            return this;
        }

        public Request widthInMillimeters(final Integer widthInMillimeters) {
            this.widthInMillimeters = widthInMillimeters;
            return this;
        }

        public Request lengthInMillimeters(final Integer lengthInMillimeters) {
            this.lengthInMillimeters = lengthInMillimeters;
            return this;
        }

        public Request heightInMillimeters(final Integer heightInMillimeters) {
            this.heightInMillimeters = heightInMillimeters;
            return this;
        }

        public Request weightInGrams(final Integer weightInGrams) {
            this.weightInGrams = weightInGrams;
            return this;
        }

        public Request temperatureZone(final TemperatureZone temperatureZone) {
            this.temperatureZone = temperatureZone;
            return this;
        }

        public Request category(final Category category) {
            this.category = category;
            return this;
        }

        public Scenario request() {
            final CreateItem.Request request = new CreateItem.Request(
                    itemName,
                    itemBarcode,
                    description,
                    brandName,
                    makerName,
                    widthInMillimeters,
                    lengthInMillimeters,
                    heightInMillimeters,
                    weightInGrams,
                    temperatureZone,
                    category
            );
            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/items")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            return new Scenario();
        }
    }
}
