package leejoongseok.wms.inbound;

import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class InboundRepository {
    Map<Long, Inbound> inbounds = new HashMap<>();

    public void save(final Inbound inbound) {
        inbound.assignId(nextId());
        inbounds.put(inbound.getId(), inbound);
    }

    private Long nextId() {
        return Long.valueOf(inbounds.size() + 1);
    }
}
