package leejoongseok.wms.common;

import leejoongseok.wms.inbound.steps.ConfirmInspectedInboundSteps;
import leejoongseok.wms.inbound.steps.CreateInboundSteps;
import leejoongseok.wms.inbound.steps.CreateLPNSteps;
import leejoongseok.wms.inbound.steps.RejectInboundSteps;
import leejoongseok.wms.item.steps.CreateItemSteps;
import leejoongseok.wms.location.steps.AddManualInventoryToLocationLPNSteps;
import leejoongseok.wms.location.steps.AssignLPNToLocationSteps;
import leejoongseok.wms.location.steps.CreateLocationSteps;
import leejoongseok.wms.outbound.steps.AssignPickingToteSteps;
import leejoongseok.wms.outbound.steps.CreateOutboundSteps;
import leejoongseok.wms.outbound.steps.CreatePackagingMaterialSteps;
import leejoongseok.wms.outbound.steps.SplitToOutboundSteps;

public class Scenario {
    public CreateItemSteps.Request createItem() {
        return new CreateItemSteps.Request();
    }

    public CreateInboundSteps.Request createInbound() {
        return new CreateInboundSteps.Request();
    }

    public ConfirmInspectedInboundSteps.Request confirmInspectedInbound() {
        return new ConfirmInspectedInboundSteps.Request();
    }

    public RejectInboundSteps.Request rejectInbound() {
        return new RejectInboundSteps.Request();
    }

    public CreateLPNSteps.Request createLPN() {
        return new CreateLPNSteps.Request();
    }

    public CreateLocationSteps.Request createLocation() {
        return new CreateLocationSteps.Request();
    }

    public AssignLPNToLocationSteps.Request assignLPNToLocation() {
        return new AssignLPNToLocationSteps.Request();
    }

    public AddManualInventoryToLocationLPNSteps.Request addManualInventoryToLocationLPN() {
        return new AddManualInventoryToLocationLPNSteps.Request();
    }

    public CreatePackagingMaterialSteps.Request createPackagingMaterial() {
        return new CreatePackagingMaterialSteps.Request();
    }

    public CreateOutboundSteps.Request createOutbound() {
        return new CreateOutboundSteps.Request();
    }

    public SplitToOutboundSteps.Request splitToOutbound() {
        return new SplitToOutboundSteps.Request();
    }

    public AssignPickingToteSteps.Request assignPickingTote() {
        return new AssignPickingToteSteps.Request();
    }
}
