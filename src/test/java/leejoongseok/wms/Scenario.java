package leejoongseok.wms;

import leejoongseok.wms.inbound.CreateInboundSteps;
import leejoongseok.wms.item.steps.CreateItemSteps;

public class Scenario {
    public CreateItemSteps.Request createItem() {
        return new CreateItemSteps.Request();
    }
    public CreateInboundSteps.Request createInbound() {
        return new CreateInboundSteps.Request();
    }
}
