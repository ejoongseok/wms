package leejoongseok.wms.outbound.domain;

import leejoongseok.wms.common.fixture.ItemFixture;
import leejoongseok.wms.common.fixture.LPNFixture;
import leejoongseok.wms.common.fixture.LocationFixture;
import leejoongseok.wms.common.fixture.LocationLPNFixture;
import leejoongseok.wms.common.fixture.OutboundFixture;
import leejoongseok.wms.common.fixture.OutboundItemFixture;
import leejoongseok.wms.common.fixture.PackagingMaterialFixture;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.outbound.exception.NotEnoughInventoryException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PickingAllocationValidatorTest {

    @Test
    @DisplayName("출고하려는 주문이 집품 가능한지 확인.")
    void validate() {
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(PackagingMaterialFixture.aPackagingMaterial().build())
                .withToteLocation(LocationFixture.aLocationWithNoLocationLPNList()
                        .withStorageType(StorageType.TOTE)
                        .build())
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItem()
                                .withOutboundQuantity(1)
                                .withItem(ItemFixture.aDefaultItem()
                                        .build())
                                .build()))
                .build();
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withLocation(LocationFixture.aLocationWithStow().build())
                .withLPN(LPNFixture.aLPN()
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
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(PackagingMaterialFixture.aPackagingMaterial().build())
                .withToteLocation(LocationFixture.aLocationWithNoLocationLPNList()
                        .withStorageType(StorageType.TOTE)
                        .build())
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItem()
                                .withOutboundQuantity(1)
                                .withItem(ItemFixture.aDefaultItem()
                                        .build())
                                .build()))
                .build();
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withLocation(LocationFixture.aLocationWithStow().build())
                .withLPN(LPNFixture.aLPN()
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
        final Outbound outbound = OutboundFixture.aOutboundWithPickingReadyStatus()
                .withRecommendedPackagingMaterial(PackagingMaterialFixture.aPackagingMaterial().build())
                .withToteLocation(LocationFixture.aLocationWithNoLocationLPNList()
                        .withStorageType(StorageType.TOTE)
                        .build())
                .withOutboundItems(List.of(
                        OutboundItemFixture.aOutboundItem()
                                .withOutboundQuantity(2)
                                .withItem(ItemFixture.aDefaultItem()
                                        .withId(1L)
                                        .build())
                                .build()))
                .build();
        final LocationLPN locationLPN = LocationLPNFixture.aLocationLPN()
                .withLocation(LocationFixture.aLocationWithStow().build())
                .withLPN(LPNFixture.aLPN()
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