package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.PackagingType;
import leejoongseok.wms.outbound.feature.CreatePackagingMaterial;
import org.springframework.http.HttpStatus;

public class CreatePackagingMaterialSteps {
    public static class Request {
        private Integer innerWidthInMillimeters = 1;
        private Integer innerHeightInMillimeters = 1;
        private Integer innerLengthInMillimeters = 1;
        private Integer outerWidthInMillimeters = 1;
        private Integer outerHeightInMillimeters = 1;
        private Integer outerLengthInMillimeters = 1;
        private Integer weightInGrams = 1;
        private PackagingType packagingType = PackagingType.BOX;
        private Integer thickness = 1;
        private String name = "name";
        private String code = "code";
        private Integer maxWeightInGrams = 1;
        private String description = "description";

        public Request innerWidthInMillimeters(final Integer innerWidthInMillimeters) {
            this.innerWidthInMillimeters = innerWidthInMillimeters;
            return this;
        }

        public Request innerHeightInMillimeters(final Integer innerHeightInMillimeters) {
            this.innerHeightInMillimeters = innerHeightInMillimeters;
            return this;
        }

        public Request innerLengthInMillimeters(final Integer innerLengthInMillimeters) {
            this.innerLengthInMillimeters = innerLengthInMillimeters;
            return this;
        }

        public Request outerWidthInMillimeters(final Integer outerWidthInMillimeters) {
            this.outerWidthInMillimeters = outerWidthInMillimeters;
            return this;
        }

        public Request outerHeightInMillimeters(final Integer outerHeightInMillimeters) {
            this.outerHeightInMillimeters = outerHeightInMillimeters;
            return this;
        }

        public Request outerLengthInMillimeters(final Integer outerLengthInMillimeters) {
            this.outerLengthInMillimeters = outerLengthInMillimeters;
            return this;
        }

        public Request weightInGrams(final Integer weightInGrams) {
            this.weightInGrams = weightInGrams;
            return this;
        }

        public Request packagingType(final PackagingType packagingType) {
            this.packagingType = packagingType;
            return this;
        }

        public Request thickness(final Integer thickness) {
            this.thickness = thickness;
            return this;
        }

        public Request name(final String name) {
            this.name = name;
            return this;
        }

        public Request code(final String code) {
            this.code = code;
            return this;
        }

        public Request maxWeightInGrams(final Integer maxWeightInGrams) {
            this.maxWeightInGrams = maxWeightInGrams;
            return this;
        }

        public Request description(final String description) {
            this.description = description;
            return this;
        }

        public Scenario request() {
            final CreatePackagingMaterial.Request request = new CreatePackagingMaterial.Request(
                    innerWidthInMillimeters,
                    innerHeightInMillimeters,
                    innerLengthInMillimeters,
                    outerWidthInMillimeters,
                    outerHeightInMillimeters,
                    outerLengthInMillimeters,
                    weightInGrams,
                    packagingType,
                    thickness,
                    name,
                    code,
                    maxWeightInGrams,
                    description
            );

            RestAssured.given().log().all()
                    .contentType(ContentType.JSON)
                    .body(request)
                    .when()
                    .post("/packaging-materials")
                    .then()
                    .log().all()
                    .statusCode(HttpStatus.CREATED.value());
            return new Scenario();
        }
    }
}
