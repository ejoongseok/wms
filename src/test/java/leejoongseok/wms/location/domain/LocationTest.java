package leejoongseok.wms.location.domain;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.exception.LocationLPNNotFoundException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static leejoongseok.wms.common.fixture.LPNFixture.aLPN;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocation;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithCell;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithMove;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithNoLocationLPNList;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithStow;
import static leejoongseok.wms.common.fixture.LocationFixture.aLocationWithTote;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LocationTest {

    @Test
    @DisplayName("로케이션에 LPN을 등록한다.")
    void assignLPN() {
        final Location location = aLocationWithNoLocationLPNList()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();

        location.assignLPN(lpn);

        final int locationLPNListSize = 1;
        final int expectedInventoryQuantity = 1;
        assertLocationLPN(location, "lpnBarcode", lpn, locationLPNListSize, expectedInventoryQuantity);
    }

    private void assertLocationLPN(
            final Location location,
            final String lpnBarcode,
            final LPN lpn,
            final int locationLPNListSize,
            final int expectedInventoryQuantity) {
        final LocationLPN locationLPN = location.testingGetLocationLPN(lpnBarcode);
        assertThat(locationLPN).isNotNull();
        assertThat(locationLPN.getLpn()).isEqualTo(lpn);
        assertThat(location.getLocationLPNList()).hasSize(locationLPNListSize);
        assertThat(locationLPN.getInventoryQuantity()).isEqualTo(expectedInventoryQuantity);
    }

    @Test
    @DisplayName("로케이션에 같은 LPN을 두번 등록할 경우 LocationLPN은 하나만 생성되고 재고수량이 2가 된다.")
    void duplicate_assignLPN() {
        final Location location = aLocationWithNoLocationLPNList()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();

        location.assignLPN(lpn);
        location.assignLPN(lpn);

        final int locationLPNListSize = 1;
        final int expectedInventoryQuantity = 2;
        assertLocationLPN(location, "lpnBarcode", lpn, locationLPNListSize, expectedInventoryQuantity);
    }

    @Test
    @DisplayName("직접 입력한 재고 수량을 로케이션 LPN에 추가한다.")
    void addManualInventoryToLocationLPN() {
        final Location location = aLocationWithNoLocationLPNList()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();
        location.assignLPN(lpn);
        final int inventoryQuantity = 10;

        location.addManualInventoryToLocationLPN(lpn, inventoryQuantity);

        final int expectedInventoryQuantity = 11;
        assertLocationLPN(location, "lpnBarcode", lpn, 1, expectedInventoryQuantity);
    }

    @Test
    @DisplayName("[실패] 직접 입력한 재고 수량을 로케이션 LPN에 추가한다. - locationLPN이 없는 경우")
    void fail_not_found_location_lpn_addManualInventoryToLocationLPN() {
        final Location location = aLocation()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();
        final int inventoryQuantity = 10;

        assertThatThrownBy(() -> {
            location.addManualInventoryToLocationLPN(lpn, inventoryQuantity);
        }).isInstanceOf(LocationLPNNotFoundException.class)
                .hasMessageContaining("LocationLPN을 찾을 수 없습니다.");
    }

    @Test
    @DisplayName("[실패] 직접 입력한 재고 수량을 로케이션 LPN에 추가한다. - 추가하려는 재고 수량이 0보다 작거나 같은경우")
    void fail_invalid_inventory_quantity_addManualInventoryToLocationLPN() {
        final Location location = aLocation()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();
        location.assignLPN(lpn);
        final int inventoryQuantity = 0;

        assertThatThrownBy(() -> {
            location.addManualInventoryToLocationLPN(lpn, inventoryQuantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가할 재고 수량은 1이상이어야 합니다.");
    }

    @Test
    @DisplayName("로케이션이 토트인지 확인한다.")
    void isTote() {
        final Location tote = aLocationWithTote()
                .build();

        final boolean isTote = tote.isTote();

        assertThat(isTote).isTrue();
    }

    @Test
    @DisplayName("로케이션이 토트인지 확인한다. - 토트가 아닌 경우 isTote는 false")
    void isCell() {
        final Location cell = aLocationWithCell()
                .build();

        final boolean isTote = cell.isTote();

        assertThat(isTote).isFalse();
    }

    @Test
    @DisplayName("로케이션에 LPN이 있는지 확인한다.")
    void hasLocationLPN() {
        final Location location = aLocation()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();
        location.assignLPN(lpn);

        final boolean hasLocationLPN = location.hasLocationLPN();

        assertThat(hasLocationLPN).isTrue();
    }

    @Test
    @DisplayName("로케이션에 LPN이 있는지 확인한다. - LPN이 없는 경우 false")
    void hasLocationLPN_fals() {
        final Location location = aLocationWithNoLocationLPNList()
                .build();

        final boolean hasLocationLPN = location.hasLocationLPN();

        assertThat(hasLocationLPN).isFalse();
    }

    @Test
    @DisplayName("로케이션의 용도가 진열인지 확인한다.")
    void isStow() {
        final Location location = aLocationWithStow()
                .build();

        final boolean isStow = location.isStow();

        assertThat(isStow).isTrue();
    }


    @Test
    @DisplayName("로케이션의 용도가 진열인지 확인한다. - 진열이 아닌 경우 false")
    void isStow_false() {
        final Location location = aLocationWithMove()
                .build();

        final boolean isStow = location.isStow();

        assertThat(isStow).isFalse();
    }

    @Test
    @DisplayName("하위 로케이션을 추가한다.")
    void addChildLocation() {
        final Location parentLocation = aLocationWithTote()
                .build();
        final Location childLocation = aLocationWithCell()
                .build();

        parentLocation.addChildLocation(childLocation);

        assertThat(parentLocation.getChildLocations()).hasSize(1);
        assertThat(parentLocation.getChildLocations().get(0)).isEqualTo(childLocation);
        assertThat(childLocation.getParentLocation()).isEqualTo(parentLocation);
    }

    @Test
    @DisplayName("하위 로케이션을 추가한다. - 하위 로케이션이 이미 추가된 경우")
    void addChildLocation_already_exists() {
        final Location parentLocation = aLocationWithTote()
                .build();
        final Location childLocation = aLocationWithCell()
                .build();

        parentLocation.addChildLocation(childLocation);
        assertThatThrownBy(() -> {
            parentLocation.addChildLocation(childLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("이미 등록된 하위 로케이션입니다.");
    }

    @Test
    @DisplayName("하위 로케이션을 추가한다. - 하위 로케이션의 크기가 부모 로케이션의 크기보다 큰 경우")
    void addChildLocation_over_size() {
        final Location parentLocation = aLocationWithCell()
                .build();
        final Location childLocation = aLocationWithTote()
                .build();

        assertThatThrownBy(() -> {
            parentLocation.addChildLocation(childLocation);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("현재 로케이션의 하위 로케이션에 등록할 수 없습니다. " +
                        "현재 로케이션 보관 타입: CELL, 하위로 추가하려는 로케이션 보관 타입: TOTE");
    }

    @Test
    @DisplayName("입력한 수량만큼 LocationLPN의 재고를 추가한다.")
    void increaseInventory() {
        final Location fromLocation = aLocation()
                .build();
        final Location toLocation = aLocation()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();
        fromLocation.assignLPN(lpn);
        final int inventoryQuantity = 10;
        fromLocation.addManualInventoryToLocationLPN(lpn, inventoryQuantity);
        final int transferQuantity = 5;
        final LocationLPN transferLocationLPN = fromLocation.testingGetLocationLPN("lpnBarcode");

        toLocation.increaseInventory(transferLocationLPN, transferQuantity);

        assertThat(toLocation.testingGetLocationLPN("lpnBarcode").getInventoryQuantity()).isEqualTo(5);
    }

    @Test
    @DisplayName("입력한 수량만큼 LocationLPN의 재고를 추가한다. - 추가하려는 재고가 0이하")
    void increaseInventory_zero_quantity() {
        final Location fromLocation = aLocation()
                .build();
        final Location toLocation = aLocation()
                .build();
        final LPN lpn = aLPN()
                .withLPNBarcode("lpnBarcode")
                .build();
        fromLocation.assignLPN(lpn);
        final int inventoryQuantity = 10;
        fromLocation.addManualInventoryToLocationLPN(lpn, inventoryQuantity);
        final int transferQuantity = 0;
        final LocationLPN transferLocationLPN = fromLocation.testingGetLocationLPN("lpnBarcode");

        assertThatThrownBy(() -> {
            toLocation.increaseInventory(transferLocationLPN, transferQuantity);
        }).isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("추가할 재고 수량은 1이상이어야 합니다.");
    }
}