package leejoongseok.wms.location.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;

import static leejoongseok.wms.common.fixture.LPNFixture.aLPN;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithMove;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithStow;
import static leejoongseok.wms.common.fixture.LocationLPNFixture.aLocationLPN;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocationLPNTest {

    private LocationLPN locationLPN;

    @BeforeEach
    void setUp() {
        locationLPN = new LocationLPN();
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 1씩 증가시킨다.")
    void incrementInventoryQuantity() {
        final Integer inventoryQuantity = locationLPN.getInventoryQuantity();

        locationLPN.incrementInventoryQuantity();

        assertThat(inventoryQuantity).isEqualTo(1);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(2);
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 입력한 숫자만큼 증가시킨다.")
    void addManualInventoryQuantity() {
        final Integer inventoryQuantity = locationLPN.getInventoryQuantity();

        locationLPN.addManualInventoryQuantity(10);

        assertThat(inventoryQuantity).isEqualTo(1);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(11);
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 입력한 숫자만큼 증가시킨다. [실패 - 증가시키려는 재고 수량이 0 이하]")
    void fail_invalid_quantity_addManualInventoryQuantity() {

        assertThatThrownBy(() -> {
            locationLPN.addManualInventoryQuantity(0);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가할 재고 수량은 1이상이어야 합니다.");
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다. [해당일자는 유통기한 전임.]")
    void isFreshBy() {
        final LocationLPN locationLPN = aLocationLPN()
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now())
                        .build())
                .build();
        final LocalDateTime thisDateTime = LocalDateTime.now().minusDays(1);

        final boolean isFresh = locationLPN.isFreshLPNBy(thisDateTime);

        assertThat(isFresh).isTrue();
    }

    @Test
    @DisplayName("LPN의 유통기한이 입력한 날짜보다 남았는지 확인한다. [해당 일자는 유통기한을 지남.]")
    void isExpired() {
        final LocationLPN locationLPN = aLocationLPN()
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now())
                        .build())
                .build();
        final LocalDateTime thisDateTime = LocalDateTime.now().plusDays(1);

        final boolean isFresh = locationLPN.isFreshLPNBy(thisDateTime);

        assertThat(isFresh).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량이 존재하는지 확인한다.")
    void hasInventory() {

        final boolean hasInventory = locationLPN.hasInventory();

        assertThat(hasInventory).isTrue();
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(1);
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량이 존재하는지 확인한다. - 비어있음")
    void hasInventory_empty() {
        final LocationLPN locationLPN = aLocationLPN()
                .withInventoryQuantity(0)
                .build();

        final boolean hasInventory = locationLPN.hasInventory();

        assertThat(hasInventory).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다.")
    void isPickingAllocatable() {
        final LocationLPN locationLPN = aLocationLPN()
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().plusDays(1))
                        .build())
                .withLocation(aLocationWithStow().build())
                .withInventoryQuantity(1)
                .build();

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isTrue();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다. - 유통기한 지남")
    void isPickingAllocatable_expiredLPN() {
        final LocationLPN locationLPN = aLocationLPN()
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().minusDays(1))
                        .build())
                .withLocation(aLocationWithStow().build())
                .withInventoryQuantity(1)
                .build();

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다. - 사용 용도가 진열이 아님")
    void isPickingAllocatable_invalidUsagePurpose() {
        final LocationLPN locationLPN = aLocationLPN()
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().plusDays(1))
                        .build())
                .withLocation(aLocationWithMove().build())
                .withInventoryQuantity(1)
                .build();

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isFalse();
    }

    @Test
    @DisplayName("로케이션 LPN이 집품 가능한지 확인한다. - 재고 수량이 0")
    void isPickingAllocatable_inventoryQuantity() {
        final LocationLPN locationLPN = aLocationLPN()
                .withLPN(aLPN()
                        .withExpirationAt(LocalDateTime.now().plusDays(1))
                        .build())
                .withLocation(aLocationWithMove().build())
                .withInventoryQuantity(0)
                .build();

        final boolean isPickingAllocatable = locationLPN.isPickingAllocatable(LocalDateTime.now());

        assertThat(isPickingAllocatable).isFalse();
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 재고를 차감한다.")
    void deductInventory() {
        final LocationLPN locationLPN = aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final int quantityRequiredForPick = 5;

        locationLPN.deductInventory(quantityRequiredForPick);

        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("집품에 필요한 수량만큼 재고를 차감한다. - 차감할 수량이 재고 수량보다 많음")
    void deductInventory_over_pick_quantity() {
        final LocationLPN locationLPN = aLocationLPN()
                .withInventoryQuantity(10)
                .build();
        final int quantityRequiredForPick = 50;

        assertThatThrownBy(() -> {
            locationLPN.deductInventory(quantityRequiredForPick);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("차감할 재고 수량이 재고 수량보다 많습니다. 재고 수량: 10, 차감할 재고 수량: 50");
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 조정한다.")
    void adjustQuantity() {
        final Integer inventoryQuantity = locationLPN.getInventoryQuantity();
        final int adjustQuantity = 10;

        locationLPN.adjustQuantity(adjustQuantity);

        assertThat(inventoryQuantity).isEqualTo(1);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(adjustQuantity);
    }

    @Test
    @DisplayName("로케이션 LPN의 재고 수량을 조정한다. - 변경할 수량이 0보다 작은 경우 예외 발생")
    void adjustQuantity_invalid_quantity() {
        final int adjustQuantity = -1;

        assertThatThrownBy(() -> {
            locationLPN.adjustQuantity(adjustQuantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("조정할 재고 수량은 0이상이어야 합니다.");
    }
}