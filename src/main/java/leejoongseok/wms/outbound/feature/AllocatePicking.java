package leejoongseok.wms.outbound.feature;

public class AllocatePicking {
    public void request(final Request request) {

    }

    public record Request(Long outboundId) {
    }
}
