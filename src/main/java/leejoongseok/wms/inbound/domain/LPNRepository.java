package leejoongseok.wms.inbound.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LPNRepository extends JpaRepository<LPN, Long> {
    /**
     * LPN 바코드로 LPN을 조회합니다.
     */
    @Query("SELECT lpn FROM LPN lpn WHERE lpn.lpnBarcode = :lpnBarcode")
    Optional<LPN> findByLPNBarcode(String lpnBarcode);

    @Query("SELECT lpn FROM LPN lpn WHERE lpn.itemId in :itemIds")
    List<LPN> listOf(List<Long> itemIds);
}
