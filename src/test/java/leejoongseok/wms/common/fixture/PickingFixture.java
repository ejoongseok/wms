package leejoongseok.wms.common.fixture;

import leejoongseok.wms.location.domain.LocationLPN;
import leejoongseok.wms.outbound.domain.Picking;
import leejoongseok.wms.outbound.domain.PickingStatus;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

public class PickingFixture {

    private InstancioApi<Picking> pickingInstance = Instancio.of(Picking.class);

    public static PickingFixture aPicking() {
        return new PickingFixture();
    }

    public static PickingFixture aPickingWithReadyPickingNoPickedQuantity() {
        return aPicking()
                .withPickingStatus(PickingStatus.READY)
                .ignorePickedQuantity();
    }

    public static PickingFixture aPickingWithInProgressPickingNoPickedQuantity() {
        return aPicking()
                .withPickingStatus(PickingStatus.IN_PROGRESS)
                .ignorePickedQuantity();
    }

    public static PickingFixture aPickingWithCompletedPickingNoPickedQuantity() {
        return aPicking()
                .withPickingStatus(PickingStatus.COMPLETED)
                .ignorePickedQuantity();
    }

    public PickingFixture withQuantityRequiredForPick(final Integer quantityRequiredForPick) {
        pickingInstance.supply(Select.field(Picking::getQuantityRequiredForPick), () -> quantityRequiredForPick);
        return this;
    }

    public PickingFixture withPickingStatus(final PickingStatus pickingStatus) {
        pickingInstance.supply(Select.field(Picking::getStatus), () -> pickingStatus);
        return this;
    }

    public PickingFixture withLocationLPN(final LocationLPN locationLPN) {
        pickingInstance.supply(Select.field(Picking::getLocationLPN), () -> locationLPN);
        return this;
    }

    public PickingFixture withPickedQuantity(final Integer pickedQuantity) {
        pickingInstance.supply(Select.field(Picking::getPickedQuantity), () -> pickedQuantity);
        return this;
    }

    public PickingFixture ignorePickedQuantity() {
        pickingInstance.ignore(Select.field(Picking::getPickedQuantity));
        return this;
    }

    public Picking build() {
        pickingInstance = null == pickingInstance ? Instancio.of(Picking.class) : pickingInstance;
        return pickingInstance.create();
    }
}
