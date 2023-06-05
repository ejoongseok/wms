package leejoongseok.wms.inbound.feature;

import leejoongseok.wms.common.ApiTest;
import leejoongseok.wms.common.Scenario;
import leejoongseok.wms.inbound.domain.LPN;
import leejoongseok.wms.inbound.domain.LPNRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;

class CreateLPNTest extends ApiTest {

    @Autowired
    private LPNRepository lpnRepository;

    @Test
    @DisplayName("입고 아이템의 LPN을 등록한다.")
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

    @Test
    @DisplayName("[실패] 입고 아이템의 LPN을 등록한다. - 동일한 LPN 바코드가 이미 존재하는 경우")
    void fail_duplicate_createLPN() {
        new Scenario()
                .createItem().request()
                .createInbound().request()
                .confirmInspectedInbound().request()
                .createLPN().request()
                .createLPN().request(HttpStatus.BAD_REQUEST.value())
        ;
    }
}
