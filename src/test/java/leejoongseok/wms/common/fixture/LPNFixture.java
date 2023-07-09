package leejoongseok.wms.common.fixture;

import leejoongseok.wms.inbound.domain.LPN;
import org.instancio.Instancio;
import org.instancio.InstancioApi;
import org.instancio.Select;

import java.time.LocalDateTime;

public class LPNFixture {

    private InstancioApi<LPN> lpnInstance = Instancio.of(LPN.class);

    public static LPNFixture aLPN() {
        return new LPNFixture();
    }

    public LPNFixture withExpirationAt(final LocalDateTime expirationAt) {
        lpnInstance.supply(Select.field(LPN::getExpirationAt), () -> expirationAt);
        return this;
    }

    public LPNFixture withLPNBarcode(final String lpnBarcode) {
        lpnInstance.supply(Select.field(LPN::getLpnBarcode), () -> lpnBarcode);
        return this;
    }

    public LPNFixture withItemId(final Long itemId) {
        lpnInstance.supply(Select.field(LPN::getItemId), () -> itemId);
        return this;
    }

    public LPNFixture withInboundItemId(final Long inboundItemId) {
        lpnInstance.supply(Select.field(LPN::getInboundItemId), () -> inboundItemId);
        return this;
    }

    public LPNFixture withCreatedAt(final LocalDateTime createdAt) {
        lpnInstance.supply(Select.field(LPN::getCreatedAt), () -> createdAt);
        return this;
    }

    public LPN build() {
        lpnInstance = null == lpnInstance ? Instancio.of(LPN.class) : lpnInstance;
        return lpnInstance.create();
    }
}
