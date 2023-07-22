package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import net.datafaker.Faker;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static leejoongseok.wms.common.fixture.LPNFixture.aLPN;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocation;
import static leejoongseok.wms.common.fixture.LocationLPNFixture.aLocationLPN;
import static org.assertj.core.api.Assertions.assertThat;

class LocationLPNFilterForOutboundTest {

    private Faker faker;
    private Location location;

    @BeforeEach
    void setUp() {
        faker = new Faker();
        location = aLocation()
                .withLocationBarcode(faker.idNumber().valid())
                .withStorageType(StorageType.CELL)
                .withUsagePurpose(UsagePurpose.STOW)
                .build();
    }

    @Test
    @DisplayName("출고 가능한 LocationLPN 목록으로 필터링한다.")
    void filter() {
        final LocalDateTime createdDateTime = LocalDateTime.now();
        final LocalDateTime validExpirationAt = createdDateTime.plusDays(1);
        final LocalDateTime invalidExpirationAt = createdDateTime.minusDays(1);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(createLPN(validExpirationAt, 1L, createdDateTime)),
                createLocationLPN(createLPN(invalidExpirationAt, 2L, createdDateTime.minusDays(1))));
        final LocalDateTime filterAt = LocalDateTime.now();

        final List<LocationLPN> avaliableLocationLPNList = LocationLPNFilterForOutbound.filter(locationLPNList, filterAt);

        assertThat(avaliableLocationLPNList).hasSize(1);
    }

    private LocationLPN createLocationLPN(final LPN lpn) {
        return aLocationLPN()
                .withLocation(location)
                .withLPN(lpn)
                .withItemId(lpn.getItemId())
                .build();
    }

    private LPN createLPN(
            final LocalDateTime validExpirationAt,
            final Long itemId,
            final LocalDateTime createdDateTime) {
        return aLPN()
                .withLPNBarcode(faker.idNumber().valid())
                .withExpirationAt(validExpirationAt)
                .withItemId(itemId)
                .withCreatedAt(createdDateTime)
                .withInboundItemId(1L)
                .build();
    }

    @Test
    @DisplayName("출고 가능한 LocationLPN 목록이 없을 경우 빈 목록을 반환한다.")
    void empty_location_lpn_List_for_outbound() {
        final LocalDateTime createdDateTime = LocalDateTime.now();
        final LocalDateTime invalidExpirationAt = createdDateTime.minusDays(1);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(createLPN(invalidExpirationAt, 1L, createdDateTime.minusDays(1))),
                createLocationLPN(createLPN(invalidExpirationAt, 2L, createdDateTime.minusDays(2))));
        final LocalDateTime filterAt = LocalDateTime.now();

        final List<LocationLPN> avaliableLocationLPNList = LocationLPNFilterForOutbound.filter(locationLPNList, filterAt);

        assertThat(avaliableLocationLPNList).isEmpty();
    }
}