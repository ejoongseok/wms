package leejoongseok.wms.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    /**
     * 로케이션 바코드로 로케이션을 조회한다.
     */
    @Query("select l from Location l where l.locationBarcode = :locationBarcode")
    Optional<Location> findByLocationBarcode(String locationBarcode);
}
