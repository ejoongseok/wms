package leejoongseok.wms.inbound;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class CreateInbound {
    public void request(final Request request) {

    }

    public record Request(
            LocalDateTime orderRequestAt,
            LocalDateTime estimatedArrivalAt,
            BigDecimal totalAmount) {
        Inbound toEntity() {
            return new Inbound();
        }
    }
}
