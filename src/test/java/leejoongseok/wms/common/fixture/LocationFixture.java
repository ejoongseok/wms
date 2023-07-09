package leejoongseok.wms.common.fixture;

import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.location.domain.StorageType;
import leejoongseok.wms.location.domain.UsagePurpose;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.util.List;

public class LocationFixture {

    private InstancioApi<Location> locationInstance = Instancio.of(Location.class);

    public static LocationFixture aLocation() {
        return new LocationFixture();
    }

    public static LocationFixture aLocationWithNoLocationLPNList() {
        return aLocation()
                .ignoreLocationLPNList();
    }

    public static LocationFixture aLocationWithStow() {
        return aLocation()
                .withUsagePurpose(UsagePurpose.STOW);
    }

    public static LocationFixture aLocationWithMove() {
        return aLocation()
                .withUsagePurpose(UsagePurpose.MOVE);
    }

    public static LocationFixture aLocationWithCell() {
        return aLocation()
                .withStorageType(StorageType.CELL);
    }

    public static LocationFixture aLocationWithTote() {
        return aLocation()
                .withStorageType(StorageType.TOTE);
    }

    public LocationFixture withUsagePurpose(final UsagePurpose usagePurpose) {
        locationInstance.supply(Select.field(Location::getUsagePurpose), () -> usagePurpose);
        return this;
    }

    public LocationFixture withStorageType(final StorageType storageType) {
        locationInstance.supply(Select.field(Location::getStorageType), () -> storageType);
        return this;
    }

    public LocationFixture withLocationBarcode(final String locationBarcode) {
        locationInstance.supply(Select.field(Location::getLocationBarcode), () -> locationBarcode);
        return this;
    }

    public LocationFixture withLocationLPNList(final List<LocationLPN> locationLPNList) {
        locationInstance.supply(Select.field(Location::getLocationLPNList), () -> locationLPNList);
        return this;
    }

    private LocationFixture ignoreLocationLPNList() {
        locationInstance.ignore(Select.field(Location::getLocationLPNList));
        return this;
    }

    public Location build() {
        locationInstance = null == locationInstance ? Instancio.of(Location.class) : locationInstance;
        return locationInstance.create();
    }
}
