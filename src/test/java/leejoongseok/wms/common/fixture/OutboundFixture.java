package leejoongseok.wms.common.fixture;

import leejoongseok.wms.location.domain.Location;
import leejoongseok.wms.outbound.domain.CushioningMaterial;
import leejoongseok.wms.outbound.domain.Outbound;
import leejoongseok.wms.outbound.domain.OutboundItem;
import leejoongseok.wms.outbound.domain.OutboundStatus;
import leejoongseok.wms.outbound.domain.PackagingMaterial;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.util.List;

public class OutboundFixture {

    private InstancioApi<Outbound> outboundInstance = Instancio.of(Outbound.class);

    public static OutboundFixture aOutbound() {
        return new OutboundFixture();
    }

    public static OutboundFixture aOutboundWithNoRecommendedPackagingMaterial() {
        return new OutboundFixture().ignoreRecommendedPackagingMaterial();
    }

    public static OutboundFixture aOutboundWithPickingReadyStatus() {
        return aOutbound()
                .withOutboundStatus(OutboundStatus.PICKING_READY);
    }

    public static OutboundFixture aOutboundWithNoCushionAndNoOutboundItemAndNoToteLocation() {
        return aOutbound()
                .withCushioningMaterial(CushioningMaterial.NONE)
                .withCushioningMaterialQuantity(0)
                .ignoreOutboundItems()
                .ignoreToteLocation();
    }

    public static OutboundFixture aOutboundWithNoCushion() {
        return aOutbound()
                .withCushioningMaterial(CushioningMaterial.NONE)
                .withCushioningMaterialQuantity(0);
    }

    public static OutboundFixture aOutboundWithReady() {
        return aOutbound()
                .withOutboundStatus(OutboundStatus.READY);
    }

    public static OutboundFixture aOutboundWithPickingInProgress() {
        return aOutbound()
                .withOutboundStatus(OutboundStatus.PICKING_IN_PROGRESS);
    }

    public static OutboundFixture aOutboundWithNoTrackingNumber() {
        return aOutbound()
                .ignoreTrackingNumber();
    }

    public static OutboundFixture withPackingInProgress() {
        return aOutbound()
                .withOutboundStatus(OutboundStatus.PACKING_IN_PROGRESS);
    }

    public static OutboundFixture withCompletedPicking() {
        return aOutbound()
                .withOutboundStatus(OutboundStatus.PICKING_COMPLETED);
    }

    public static OutboundFixture aOutboundWithStopped() {
        return aOutbound()
                .withOutboundStatus(OutboundStatus.STOPPED);
    }


    public OutboundFixture ignoreTrackingNumber() {
        outboundInstance.ignore(Select.field(Outbound::getTrackingNumber));
        return this;
    }

    public OutboundFixture withId(final Long id) {
        outboundInstance.supply(Select.field(Outbound::getId), () -> id);
        return this;
    }

    public OutboundFixture withCushioningMaterial(final CushioningMaterial cushioningMaterial) {
        outboundInstance.supply(Select.field(Outbound::getCushioningMaterial), () -> cushioningMaterial);
        return this;
    }

    public OutboundFixture withCushioningMaterialQuantity(final Integer cushioningMaterialQuantity) {
        outboundInstance.supply(Select.field(Outbound::getCushioningMaterialQuantity), () -> cushioningMaterialQuantity);
        return this;
    }

    public OutboundFixture withOutboundItems(final List<OutboundItem> outboundItems) {
        outboundInstance.supply(Select.field(Outbound::getOutboundItems), () -> outboundItems);
        return this;
    }

    public OutboundFixture withOutboundStatus(final OutboundStatus outboundStatus) {
        outboundInstance.supply(Select.field(Outbound::getOutboundStatus), () -> outboundStatus);
        return this;
    }

    public OutboundFixture withRecommendedPackagingMaterial(final PackagingMaterial packagingMaterial) {
        outboundInstance.supply(Select.field(Outbound::getRecommendedPackagingMaterial), () -> packagingMaterial);
        return this;
    }

    public OutboundFixture withToteLocation(final Location toteLocation) {
        outboundInstance.supply(Select.field(Outbound::getToteLocation), () -> toteLocation);
        return this;
    }

    public OutboundFixture ignoreRecommendedPackagingMaterial() {
        outboundInstance.ignore(Select.field(Outbound::getRecommendedPackagingMaterial));
        return this;
    }

    public OutboundFixture ignoreOutboundItems() {
        outboundInstance.ignore(Select.field(Outbound::getOutboundItems));
        return this;
    }

    public OutboundFixture ignoreToteLocation() {
        outboundInstance.ignore(Select.field(Outbound::getToteLocation));
        return this;
    }

    public Outbound build() {
        outboundInstance = null == outboundInstance ? Instancio.of(Outbound.class) : outboundInstance;
        return outboundInstance.create();
    }
}
