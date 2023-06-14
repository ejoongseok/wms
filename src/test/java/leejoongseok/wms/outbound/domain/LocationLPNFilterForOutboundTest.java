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

import static org.assertj.core.api.Assertions.assertThat;

class LocationLPNFilterForOutboundTest {
    private LocationLPNFilterForOutbound filterForOutbound;

    @BeforeEach
    void setUp() {
        filterForOutbound = new LocationLPNFilterForOutbound();
    }

    @Test
    @DisplayName("출고 가능한 LocationLPN 목록으로 필터링한다.")
    void filter() {
        final LocalDateTime createdDateTime = LocalDateTime.now();
        final LocalDateTime validExpirationAt = createdDateTime.plusDays(1);
        final LocalDateTime invalidExpirationAt = createdDateTime.minusDays(1);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(1L, validExpirationAt, createdDateTime),
                createLocationLPN(2L, invalidExpirationAt, createdDateTime.minusDays(1)));
        final LocalDateTime filterAt = LocalDateTime.now();

        final List<LocationLPN> avaliableLocationLPNList = filterForOutbound.filter(locationLPNList, filterAt);

        assertThat(avaliableLocationLPNList).hasSize(1);
    }

    private LocationLPN createLocationLPN(
            final long itemId,
            final LocalDateTime expirationAt,
            final LocalDateTime toDay) {
        final Faker faker = new Faker();
        final String locationBarcode = faker.idNumber().valid();
        final String lpnBarcode = faker.idNumber().valid();

        return new LocationLPN(
                new Location(
                        locationBarcode,
                        StorageType.CELL,
                        UsagePurpose.STOW
                ),
                new LPN(
                        lpnBarcode,
                        itemId,
                        expirationAt,
                        1L,
                        toDay
                ),
                itemId);
    }

    @Test
    @DisplayName("출고 가능한 LocationLPN 목록이 없을 경우 빈 목록을 반환한다.")
    void empty_location_lpn_List_for_outbound() {
        final LocalDateTime createdDateTime = LocalDateTime.now();
        final LocalDateTime invalidExpirationAt = createdDateTime.minusDays(1);
        final List<LocationLPN> locationLPNList = List.of(
                createLocationLPN(1L, invalidExpirationAt, createdDateTime.minusDays(1)),
                createLocationLPN(2L, invalidExpirationAt, createdDateTime.minusDays(2)));
        final LocalDateTime filterAt = LocalDateTime.now();

        final List<LocationLPN> avaliableLocationLPNList = filterForOutbound.filter(locationLPNList, filterAt);

        assertThat(avaliableLocationLPNList).isEmpty();
    }
}