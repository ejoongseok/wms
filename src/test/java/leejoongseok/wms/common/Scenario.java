package leejoongseok.wms.common;

import leejoongseok.wms.inbound.steps.ConfirmInspectedInboundSteps;
import leejoongseok.wms.inbound.steps.CreateInboundSteps;
import leejoongseok.wms.inbound.steps.CreateLPNSteps;
import leejoongseok.wms.inbound.steps.RejectInboundSteps;
import leejoongseok.wms.item.steps.CreateItemSteps;
import leejoongseok.wms.location.steps.AddManualInventoryToLocationLPNSteps;
import leejoongseok.wms.location.steps.AssignLPNToLocationSteps;
import leejoongseok.wms.location.steps.CreateLocationSteps;
import leejoongseok.wms.outbound.steps.AllocatePickingSteps;
import leejoongseok.wms.outbound.steps.AssignPackingSteps;
import leejoongseok.wms.outbound.steps.AssignPickingToteSteps;
import leejoongseok.wms.outbound.steps.CompletePickingSteps;
import leejoongseok.wms.outbound.steps.CreateOutboundSteps;
import leejoongseok.wms.outbound.steps.CreatePackagingMaterialSteps;
import leejoongseok.wms.outbound.steps.IssueWaybillSteps;
import leejoongseok.wms.outbound.steps.ManualToPickSteps;
import leejoongseok.wms.outbound.steps.ScanToPickSteps;
import leejoongseok.wms.outbound.steps.SplitToOutboundSteps;

public class Scenario {
    /**
     * 상품을 생성한다.
     */
    public CreateItemSteps.Request createItem() {
        return new CreateItemSteps.Request();
    }

    /**
     * 입고를 생성한다.
     */
    public CreateInboundSteps.Request createInbound() {
        return new CreateInboundSteps.Request();
    }

    /**
     * 입고를 확정한다.
     */
    public ConfirmInspectedInboundSteps.Request confirmInspectedInbound() {
        return new ConfirmInspectedInboundSteps.Request();
    }

    /**
     * 입고를 거절한다.
     */
    public RejectInboundSteps.Request rejectInbound() {
        return new RejectInboundSteps.Request();
    }

    /**
     * 입고상품의 LPN을 생성한다.
     */
    public CreateLPNSteps.Request createLPN() {
        return new CreateLPNSteps.Request();
    }

    /**
     * 로케이션을 생성한다.
     */
    public CreateLocationSteps.Request createLocation() {
        return new CreateLocationSteps.Request();
    }

    /**
     * LPN을 로케이션에 할당(적재)한다.
     */
    public AssignLPNToLocationSteps.Request assignLPNToLocation() {
        return new AssignLPNToLocationSteps.Request();
    }

    /**
     * 로케이션에 LPN의 수량을 수동 입력한다.
     */
    public AddManualInventoryToLocationLPNSteps.Request addManualInventoryToLocationLPN() {
        return new AddManualInventoryToLocationLPNSteps.Request();
    }

    /**
     * 포장자재를 생성한다.
     */
    public CreatePackagingMaterialSteps.Request createPackagingMaterial() {
        return new CreatePackagingMaterialSteps.Request();
    }

    /**
     * 출고를 생성한다.
     */
    public CreateOutboundSteps.Request createOutbound() {
        return new CreateOutboundSteps.Request();
    }

    /**
     * 출고를 분할한다.
     * 포장자재를 추천받지 못한경우 출고를 분할해서 포장자재를 할당한다.
     */
    public SplitToOutboundSteps.Request splitToOutbound() {
        return new SplitToOutboundSteps.Request();
    }

    /**
     * 출고를 집품할 토트를 할당한다.
     */
    public AssignPickingToteSteps.Request assignPickingTote() {
        return new AssignPickingToteSteps.Request();
    }

    /**
     * 출고 상품에 집품정보를 할당한다.
     */
    public AllocatePickingSteps.Request allocatePicking() {
        return new AllocatePickingSteps.Request();
    }

    /**
     * 출고 상품을 집품한다.
     */
    public ScanToPickSteps.Request scanToPick() {
        return new ScanToPickSteps.Request();
    }

    /**
     * 출고 상품을 집품할때 스캔하는 방식이 아니라 수량을 직접입력해서 집품한다.
     * 집품해야할 상품의 수량이 많은경우 사용한다.
     */
    public ManualToPickSteps.Request manualToPick() {
        return new ManualToPickSteps.Request();
    }

    /**
     * 출고의 집품을 완료한다.
     *
     * @return
     */
    public CompletePickingSteps.Request completePicking() {
        return new CompletePickingSteps.Request();
    }

    /**
     * 출고에 대한 송장을 발행한다.
     */
    public IssueWaybillSteps.Request issueWaybill() {
        return new IssueWaybillSteps.Request();
    }

    /**
     * 출고에 대한 포장정보를 등록한다.
     */
    public AssignPackingSteps.Request assignPacking() {
        return new AssignPackingSteps.Request();
    }
}
