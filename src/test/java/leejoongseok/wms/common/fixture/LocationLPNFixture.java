package leejoongseok.wms.common.fixture;

import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class LocationLPNFixture {

    private InstancioApi<LocationLPN> locationLPNInstance = Instancio.of(LocationLPN.class);

    public static LocationLPNFixture aLocationLPN() {
        return new LocationLPNFixture();
    }

    public LocationLPNFixture withId(final Long id) {
        locationLPNInstance.supply(Select.field(LocationLPN::getId), () -> id);
        return this;
    }

    public LocationLPNFixture withLPN(final LPN lpn) {
        locationLPNInstance.supply(Select.field(LocationLPN::getLpn), () -> lpn);
        return this;
    }

    public LocationLPNFixture withInventoryQuantity(final Integer inventoryQuantity) {
        locationLPNInstance.supply(Select.field(LocationLPN::getInventoryQuantity), () -> inventoryQuantity);
        return this;
    }

    public LocationLPNFixture withLocation(final Location location) {
        locationLPNInstance.supply(Select.field(LocationLPN::getLocation), () -> location);
        return this;
    }

    public LocationLPNFixture withItemId(final Long itemId) {
        locationLPNInstance.supply(Select.field(LocationLPN::getItemId), () -> itemId);
        return this;
    }

    public LocationLPN build() {
        locationLPNInstance = null == locationLPNInstance ? Instancio.of(LocationLPN.class) : locationLPNInstance;
        return locationLPNInstance.create();
    }
}
