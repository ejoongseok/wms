package leejoongseok.wms.location.domain;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {
    /**
     * 로케이션 바코드로 로케이션을 조회한다.
     */
    @Query("select l from Location l where l.locationBarcode = :locationBarcode")
    Optional<Location> findByLocationBarcode(String locationBarcode);

    @VisibleForTesting
    @Query("select l from Location l left join fetch l.parentLocation left join fetch l.childLocations where l.locationBarcode = :locationBarcode")
    Optional<Location> testingFindByLocationBarcode(String locationBarcode);

    @Query("select l from Location l left join l.locationLPNList where l.locationBarcode = :locationBarcode")
    Optional<Location> findByLocationBarcodeAndFetchJoinLocationLPNList(String locationBarcode);
}
