package leejoongseok.wms.inbound.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LPNRepository extends JpaRepository<LPN, Long> {
    @Query("SELECT lpn FROM LPN lpn WHERE lpn.lpnBarcode = :lpnBarcode")
    Optional<LPN> findByLPNBarcode(String lpnBarcode);
}
