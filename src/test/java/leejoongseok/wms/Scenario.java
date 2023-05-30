package leejoongseok.wms;

import leejoongseok.wms.item.steps.CreateItemSteps;

public class Scenario {
    public CreateItemSteps.Request createItem() {
        return new CreateItemSteps.Request();
    }
}
