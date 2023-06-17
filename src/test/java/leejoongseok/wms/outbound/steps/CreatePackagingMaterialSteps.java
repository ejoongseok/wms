package leejoongseok.wms.outbound.steps;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.outbound.domain.PackagingType;
import leejoongseok.wms.outbound.feature.CreatePackagingMaterial;
import org.springframework.http.HttpStatus;

public class CreatePackagingMaterialSteps {
    public static class Request {
        private Integer innerWidthMillimeter = 1;
        private Integer innerHeightMillimeter = 1;
        private Integer innerLengthMillimeter = 1;
        private Integer outerWidthMillimeter = 1;
        private Integer outerHeightMillimeter = 1;
        private Integer outerLengthMillimeter = 1;
        private Integer weightInGrams = 1;
        private PackagingType packagingType = PackagingType.BOX;
        private Integer thickness = 1;
        private String name = "name";
        private String code = "code";
        private Integer maxWeightGram = 1;
        private String description = "description";

        public Request innerWidthMillimeter(final Integer innerWidthMillimeter) {
            this.innerWidthMillimeter = innerWidthMillimeter;
            return this;
        }

        public Request innerHeightMillimeter(final Integer innerHeightMillimeter) {
            this.innerHeightMillimeter = innerHeightMillimeter;
            return this;
        }

        public Request innerLengthMillimeter(final Integer innerLengthMillimeter) {
            this.innerLengthMillimeter = innerLengthMillimeter;
            return this;
        }

        public Request outerWidthMillimeter(final Integer outerWidthMillimeter) {
            this.outerWidthMillimeter = outerWidthMillimeter;
            return this;
        }

        public Request outerHeightMillimeter(final Integer outerHeightMillimeter) {
            this.outerHeightMillimeter = outerHeightMillimeter;
            return this;
        }

        public Request outerLengthMillimeter(final Integer outerLengthMillimeter) {
            this.outerLengthMillimeter = outerLengthMillimeter;
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

        public Request maxWeightGram(final Integer maxWeightGram) {
            this.maxWeightGram = maxWeightGram;
            return this;
        }

        public Request description(final String description) {
            this.description = description;
            return this;
        }

        public Scenario request() {
            final CreatePackagingMaterial.Request request = new CreatePackagingMaterial.Request(
                    innerWidthMillimeter,
                    innerHeightMillimeter,
                    innerLengthMillimeter,
                    outerWidthMillimeter,
                    outerHeightMillimeter,
                    outerLengthMillimeter,
                    weightInGrams,
                    packagingType,
                    thickness,
                    name,
                    code,
                    maxWeightGram,
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
