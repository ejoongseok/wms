package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static leejoongseok.wms.common.fixture.LPNFixture.aLPN;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocation;
import static leejoongseok.wms.common.fixture.LocationLPNFixture.aLocationLPN;
import static org.assertj.core.api.Assertions.assertThat;

class PickingCreatorTest {

    private static LPN createLPN() {
        return aLPN()
                .withExpirationAt(LocalDateTime.now().plusDays(1L))
                .withItemId(1L)
                .build();
    }

    private static Location createLocation(final String locationBarcode) {
        return aLocation()
                .withLocationBarcode(locationBarcode)
                .build();
    }

    @Test
    @DisplayName("재고가 집품해야할 수량보다 충분하면 출고상품의 집품목록을 생성한다.")
    void createPickings() {
        final List<LocationLPN> locationLPNList = List.of(
                aLocationLPN()
                        .withId(3L)
                        .withLPN(createLPN())
                        .withLocation(createLocation("locationBarcode-3"))
                        .withInventoryQuantity(3)
                        .withItemId(1L)
                        .build(),
                aLocationLPN()
                        .withId(2L)
                        .withLPN(createLPN())
                        .withLocation(createLocation("locationBarcode-2"))
                        .withInventoryQuantity(2)
                        .withItemId(1L)
                        .build(),
                aLocationLPN()
                        .withId(1L)
                        .withLPN(createLPN())
                        .withLocation(createLocation("locationBarcode-1"))
                        .withInventoryQuantity(1)
                        .withItemId(1L)
                        .build()
        );

        final List<Picking> pickings = PickingCreator.createPickings(1L, 5, locationLPNList);
        assertThat(pickings).hasSize(2);
        assertThat(pickings.get(0).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-3");
        assertThat(pickings.get(1).getLocationLPN().getLocationBarcode()).isEqualTo("locationBarcode-2");
    }

}