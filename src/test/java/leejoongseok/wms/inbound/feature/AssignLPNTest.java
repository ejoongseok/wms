package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.ApiTest;
import leejoongseok.wms.Scenario;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class AssignLPNTest extends ApiTest {

    @Autowired
    private LPNRepository lpnRepository;

    @Test
    @DisplayName("입고 아이템의 LPN을 등록한다.")
    void assignLPN() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .assignLPN().request()
        ;

        final Optional<LPN> lpn = lpnRepository.findById(1L);

        assertThat(lpn).isPresent();
    }
}
