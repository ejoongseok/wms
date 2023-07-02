package leejoongseok.wms.outbound.feature;

public class StopOutbound {
    public void request(final Long outboundId, final Request request) {
        throw new UnsupportedOperationException("Unsupported request");
    }

    public record Request(String stoppedReason) {
    }
}
