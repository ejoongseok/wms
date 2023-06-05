package leejoongseok.wms.location.domain;

import leejoongseok.wms.inbound.domain.LPN;
import lombok.Getter;
import org.springframework.util.Assert;

public class LocationLPN {
    private final Location location;
    @Getter
    private final LPN lpn;
    @Getter
    private Integer inventoryQuantity = 1;

    public LocationLPN(final Location location, final LPN lpn) {
        Assert.notNull(location, "로케이션은 필수입니다.");
        Assert.notNull(lpn, "LPN은 필수입니다.");
        this.location = location;
        this.lpn = lpn;
    }

    public void increaseInventoryQuantity() {
        inventoryQuantity++;
    }
}
