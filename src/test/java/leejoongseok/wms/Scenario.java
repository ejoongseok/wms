package leejoongseok.wms;

import leejoongseok.wms.inbound.steps.ConfirmInspectedInboundSteps;
import leejoongseok.wms.inbound.steps.CreateInboundSteps;
import leejoongseok.wms.inbound.steps.CreateLPNSteps;
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

    public CreateLPNSteps.Request createLPN() {
        return new CreateLPNSteps.Request();
    }
}
