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

class CreateLPNTest extends ApiTest {

    @Autowired
    private LPNRepository lpnRepository;

    @Test
    @DisplayName("입고 아이템의 LPN을 생성한다.")
    void createLPN() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
        ;

        final Optional<LPN> lpn = lpnRepository.findById(1L);

        assertThat(lpn).isPresent();
    }
}
