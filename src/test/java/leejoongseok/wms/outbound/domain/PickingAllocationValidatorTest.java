package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static leejoongseok.wms.common.fixture.ItemFixture.aDefaultItem;
import static leejoongseok.wms.common.fixture.LPNFixture.aLPN;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithNoLocationLPNList;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithStow;
import static leejoongseok.wms.common.fixture.LocationLPNFixture.aLocationLPN;
import static leejoongseok.wms.common.fixture.OutboundFixture.aOutboundWithPickingReadyStatus;
import static leejoongseok.wms.common.fixture.OutboundItemFixture.aOutboundItem;
import static leejoongseok.wms.common.fixture.PackagingMaterialFixture.aPackagingMaterial;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingAllocationValidatorTest {

    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인.")
    void validate() {
        final Outbound outbound = aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(aPackagingMaterial().build())
                .withToteLocation(aLocationWithNoLocationLPNList()
                        .withStorageType(StorageType.TOTE)
                        .build())
                .withOutboundItems(List.of(
                        aOutboundItem()
                                .withOutboundQuantity(1)
                                .withItem(aDefaultItem()
                                        .build())
                                .build()))
                .build();
        final LocationLPN locationLPN = aLocationLPN()
                .withLocation(aLocationWithStow().build())
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().plusDays(1))
                        .build())
                .withInventoryQuantity(1)
                .withItemId(1L)
                .build();

        PickingAllocationValidator.validate(outbound, List.of(locationLPN));
    }


    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인. - 유통기한 만료")
    void validate_expired() {
        final Outbound outbound = aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(aPackagingMaterial().build())
                .withToteLocation(aLocationWithNoLocationLPNList()
                        .withStorageType(StorageType.TOTE)
                        .build())
                .withOutboundItems(List.of(
                        aOutboundItem()
                                .withOutboundQuantity(1)
                                .withItem(aDefaultItem()
                                        .build())
                                .build()))
                .build();
        final LocationLPN locationLPN = aLocationLPN()
                .withLocation(aLocationWithStow().build())
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().minusDays(1))
                        .build())
                .withInventoryQuantity(1)
                .withItemId(1L)
                .build();

        assertThatThrownBy(() -> {
            PickingAllocationValidator.validate(outbound, List.of(locationLPN));
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.");
    }

    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인. - 재고 부족")
    void validate_inventory() {
        final Outbound outbound = aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(aPackagingMaterial().build())
                .withToteLocation(aLocationWithNoLocationLPNList()
                        .withStorageType(StorageType.TOTE)
                        .build())
                .withOutboundItems(List.of(
                        aOutboundItem()
                                .withOutboundQuantity(2)
                                .withItem(aDefaultItem()
                                        .withId(1L)
                                        .build())
                                .build()))
                .build();
        final LocationLPN locationLPN = aLocationLPN()
                .withLocation(aLocationWithStow().build())
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().minusDays(1))
                        .build())
                .withInventoryQuantity(1)
                .withItemId(1L)
                .build();


        assertThatThrownBy(() -> {
            PickingAllocationValidator.validate(outbound, List.of(locationLPN));
        }).isInstanceOf(NotEnoughInventoryException.class)
                .hasMessageContaining("집품할 상품의 재고가 부족합니다.");
    }
}