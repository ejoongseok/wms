package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.LPNFixture;
import leejoongseok.wms.common.fixture.LocationFixture;
import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.location.domain.LocationLPN;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class EfficiencyLocationLPNSorterTest {

    @Test
    @DisplayName("집품하기 효율적인 로케이션 LPN 동선으로 정렬한다." +
            "1. 유통기한이 가장 짧은 순서 (선입선출)" +
            "2. 재고수량이 많은 순서 (그래야 하나의 로케이션 LPN으로 집품이 가능하니까)" +
            "3. 로케이션 바코드 명으로 정렬 (보통 로케이션 바코드는 A -> Z 순서로 정렬되어 있음)")
    void sort() {
        final LocalDateTime today = LocalDateTime.now();
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(1L, "A0001", 3, today.plusDays(4)),
                createLocationLPN(2L, "A0002", 2, today.plusDays(4)),
                createLocationLPN(3L, "D0001", 5, today.plusDays(3)),
                createLocationLPN(4L, "C0001", 2, today.plusDays(1)),
                createLocationLPN(5L, "B0001", 1, today.plusDays(3))
        );

        final List<LocationLPN> sortedLocationLPNList = EfficiencyLocationLPNSorter.sort(locationLPNList);
        assertThat(sortedLocationLPNList.get(0).getLocationBarcode()).isEqualTo("C0001");
        assertThat(sortedLocationLPNList.get(1).getLocationBarcode()).isEqualTo("D0001");
        assertThat(sortedLocationLPNList.get(2).getLocationBarcode()).isEqualTo("B0001");
        assertThat(sortedLocationLPNList.get(3).getLocationBarcode()).isEqualTo("A0001");
        assertThat(sortedLocationLPNList.get(4).getLocationBarcode()).isEqualTo("A0002");

    }

    private LocationLPN createLocationLPN(
            final long locationLPNId,
            final String locationBarcode,
            final int inventoryQuantity,
            final LocalDateTime expirationAt) {
        return LocationLPNFixture.aLocationLPN()
                .withId(locationLPNId)
                .withLPN(LPNFixture.aLPN()
                        .withExpirationAt(expirationAt)
                        .build())
                .withLocation(LocationFixture.aLocation()
                        .withLocationBarcode(locationBarcode)
                        .build())
                .withInventoryQuantity(inventoryQuantity)
                .build();

    }
}