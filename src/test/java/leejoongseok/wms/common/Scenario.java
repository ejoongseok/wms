package leejoongseok.wms.common;

import leejoongseok.wms.inbound.steps.ConfirmInspectedInboundSteps;
import leejoongseok.wms.inbound.steps.CreateInboundSteps;
import leejoongseok.wms.inbound.steps.CreateLPNSteps;
import leejoongseok.wms.inbound.steps.RejectInboundSteps;
import leejoongseok.wms.item.steps.CreateItemSteps;
import leejoongseok.wms.location.steps.AddManualInventoryToLocationLPNSteps;
import leejoongseok.wms.location.steps.AdjustInventorySteps;
import leejoongseok.wms.location.steps.AssignLPNToLocationSteps;
import leejoongseok.wms.location.steps.CreateLocationSteps;
import leejoongseok.wms.location.steps.MoveToTargetChildLocationSteps;
import leejoongseok.wms.location.steps.TransferInventorySteps;
import leejoongseok.wms.outbound.steps.AllocatePickingSteps;
import leejoongseok.wms.outbound.steps.AssignPackingSteps;
import leejoongseok.wms.outbound.steps.AssignPickingToteSteps;
import leejoongseok.wms.outbound.steps.CompletePackingSteps;
import leejoongseok.wms.outbound.steps.CompletePickingSteps;
import leejoongseok.wms.outbound.steps.CreateOutboundSteps;
import leejoongseok.wms.outbound.steps.CreatePackagingMaterialSteps;
import leejoongseok.wms.outbound.steps.FailInspectionSteps;
import leejoongseok.wms.outbound.steps.IssueWaybillSteps;
import leejoongseok.wms.outbound.steps.ManualToPickSteps;
import leejoongseok.wms.outbound.steps.PassInspectionSteps;
import leejoongseok.wms.outbound.steps.ResetOutboundSteps;
import leejoongseok.wms.outbound.steps.ScanToPickSteps;
import leejoongseok.wms.outbound.steps.SplitToOutboundSteps;
import leejoongseok.wms.outbound.steps.StopOutboundSteps;
import leejoongseok.wms.user.steps.CreateUserSteps;

public class Scenario {
    /**
     * 상품을 생성한다.
     */
    public static CreateItemSteps.Request createItem() {
        return new CreateItemSteps.Request();
    }

    /**
     * 입고를 생성한다.
     */
    public static CreateInboundSteps.Request createInbound() {
        return new CreateInboundSteps.Request();
    }

    /**
     * 입고를 확정한다.
     */
    public static ConfirmInspectedInboundSteps.Request confirmInspectedInbound() {
        return new ConfirmInspectedInboundSteps.Request();
    }

    /**
     * 입고를 거절한다.
     */
    public static RejectInboundSteps.Request rejectInbound() {
        return new RejectInboundSteps.Request();
    }

    /**
     * 입고상품의 LPN을 생성한다.
     */
    public static CreateLPNSteps.Request createLPN() {
        return new CreateLPNSteps.Request();
    }

    /**
     * 로케이션을 생성한다.
     */
    public static CreateLocationSteps.Request createLocation() {
        return new CreateLocationSteps.Request();
    }

    /**
     * LPN을 로케이션에 할당(적재)한다.
     */
    public static AssignLPNToLocationSteps.Request assignLPNToLocation() {
        return new AssignLPNToLocationSteps.Request();
    }

    /**
     * 로케이션에 LPN의 수량을 수동 입력한다.
     */
    public static AddManualInventoryToLocationLPNSteps.Request addManualInventoryToLocationLPN() {
        return new AddManualInventoryToLocationLPNSteps.Request();
    }

    /**
     * 포장자재를 생성한다.
     */
    public static CreatePackagingMaterialSteps.Request createPackagingMaterial() {
        return new CreatePackagingMaterialSteps.Request();
    }

    /**
     * 출고를 생성한다.
     */
    public static CreateOutboundSteps.Request createOutbound() {
        return new CreateOutboundSteps.Request();
    }

    /**
     * 출고를 분할한다.
     * 포장자재를 추천받지 못한경우 출고를 분할해서 포장자재를 할당한다.
     */
    public static SplitToOutboundSteps.Request splitToOutbound() {
        return new SplitToOutboundSteps.Request();
    }

    /**
     * 출고를 집품할 토트를 할당한다.
     */
    public static AssignPickingToteSteps.Request assignPickingTote() {
        return new AssignPickingToteSteps.Request();
    }

    /**
     * 출고 상품에 집품정보를 할당한다.
     */
    public static AllocatePickingSteps.Request allocatePicking() {
        return new AllocatePickingSteps.Request();
    }

    /**
     * 출고 상품을 집품한다.
     */
    public static ScanToPickSteps.Request scanToPick() {
        return new ScanToPickSteps.Request();
    }

    /**
     * 출고 상품을 집품할때 스캔하는 방식이 아니라 수량을 직접입력해서 집품한다.
     * 집품해야할 상품의 수량이 많은경우 사용한다.
     */
    public static ManualToPickSteps.Request manualToPick() {
        return new ManualToPickSteps.Request();
    }

    /**
     * 출고의 집품을 완료한다.
     *
     * @return
     */
    public static CompletePickingSteps.Request completePicking() {
        return new CompletePickingSteps.Request();
    }

    /**
     * 출고에 대한 송장을 발행한다.
     */
    public static IssueWaybillSteps.Request issueWaybill() {
        return new IssueWaybillSteps.Request();
    }

    /**
     * 출고에 대한 포장정보를 등록한다.
     */
    public static AssignPackingSteps.Request assignPacking() {
        return new AssignPackingSteps.Request();
    }

    /**
     * 출고에 대한 포장을 완료한다.
     */
    public static CompletePackingSteps.Request completePacking() {
        return new CompletePackingSteps.Request();
    }

    /**
     * 로케이션을 다른 로케이션으로 이동한다.
     */
    public static MoveToTargetChildLocationSteps.Request moveToTargetChildLocation() {
        return new MoveToTargetChildLocationSteps.Request();
    }

    /**
     * 로케이션에 있는 LPN의 재고를 이동한다.
     */
    public static TransferInventorySteps.Request transferInventory() {
        return new TransferInventorySteps.Request();
    }

    /**
     * 집품한 출고상품의 검수를 완료한다.
     */
    public static PassInspectionSteps.Request passInspection() {
        return new PassInspectionSteps.Request();
    }

    /**
     * 집품한 출고상품의 검수를 불합격 처리한다.
     */
    public static FailInspectionSteps.Request failInspection() {
        return new FailInspectionSteps.Request();
    }

    public static StopOutboundSteps.Request stopOutbound() {
        return new StopOutboundSteps.Request();
    }

    public static ResetOutboundSteps.Request resetOutbound() {
        return new ResetOutboundSteps.Request();
    }

    public static AdjustInventorySteps.Request adjustInventory() {
        return new AdjustInventorySteps.Request();
    }

    public static CreateUserSteps.Request createUser() {
        return new CreateUserSteps.Request();
    }
}
