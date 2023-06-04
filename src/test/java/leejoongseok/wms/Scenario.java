package leejoongseok.wms;

import leejoongseok.wms.inbound.steps.AssignLPNSteps;
import leejoongseok.wms.inbound.steps.ConfirmInspectedInboundSteps;
import leejoongseok.wms.inbound.steps.CreateInboundSteps;
import leejoongseok.wms.inbound.steps.RejectInboundSteps;
import leejoongseok.wms.item.steps.CreateItemSteps;

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

    public AssignLPNSteps.Request assignLPN() {
        return new AssignLPNSteps.Request();
    }
}
