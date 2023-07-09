package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.location.domain.LocationLPN;
import org.instancio.Instancio;
import org.instancio.Select;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocationLPNValidatorForOutboundTest {


    @Test
    @DisplayName("출고 대상 LocationLPN 목록이 출고 수량을 충족하는지 검증한다. [예외발생]")
    void validate_exception() {
        assertValidate(
                createInsufficientInventoryQuantityLocationLPNList(),
                2,
                IllegalArgumentException.class,
                "출고 가능한 재고가 부족합니다. 출고 가능한 재고 수량: 1 출고 요청 수량: 2");

        assertValidate(
                List.of(Instancio.create(LocationLPN.class)),
                0,
                IllegalArgumentException.class,
                "출고 요청 수량이 0보다 작거나 같습니다.");

        assertValidate(
                List.of(),
                1,
                IllegalArgumentException.class,
                "출고 가능한지 검증할 로케이션 LPN 목록이 비어있습니다.");
        assertValidate(
                null,
                1,
                IllegalArgumentException.class,
                "출고 가능한지 검증할 로케이션 LPN 목록이 비어있습니다.");
    }

    private void assertValidate(
            final List<LocationLPN> locationLPNList,
            final int orderQuantity,
            final Class<?> exceptionClass,
            final String errorMessage) {

        assertThatThrownBy(() -> {
            LocationLPNValidatorForOutbound.validate(locationLPNList, orderQuantity);
        }).isInstanceOf(exceptionClass)
                .hasMessageContaining(errorMessage);
    }

    private List<LocationLPN> createInsufficientInventoryQuantityLocationLPNList() {
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withInventoryQuantity(1)
                .build();
        return List.of(locationLPN);
    }

    @Test
    @DisplayName("출고 대상 LocationLPN 목록이 출고 수량을 충족하는지 검증한다. [통과]")
    void sufficient_inventory_location_lpn_list_for_outbound() {
        final List<LocationLPN> locationLPNList = createSufficientInventoryQuantityLocationLPNList();
        final int orderQuantity = 2;

        LocationLPNValidatorForOutbound.validate(locationLPNList, orderQuantity);
    }

    private List<LocationLPN> createSufficientInventoryQuantityLocationLPNList() {
        final LocationLPN locationLPN = Instancio.of(LocationLPN.class)
                .supply(Select.field(LocationLPN::getInventoryQuantity), () -> 2)
                .create();
        return List.of(locationLPN);
    }
}