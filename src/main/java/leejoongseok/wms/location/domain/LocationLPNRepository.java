package leejoongseok.wms.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface LocationLPNRepository extends JpaRepository<LocationLPN, Long> {
    /**
     * 상품 ID로 LocationLPN 목록을 조회하는데 LPN도 참조해서 조회한다.
     */
    @Query("select l from LocationLPN l join fetch l.lpn where l.itemId = :itemId")
    List<LocationLPN> findByItemIdAndFetchJoinLPN(Long itemId);

    @Query("select l from LocationLPN l join fetch l.lpn where l.lpn.id in :lpnIds")
    List<LocationLPN> findByLPNIdsAndFetchJoinLPNAndLocation(List<Long> lpnIds);

    @Query("select l from LocationLPN  l join fetch l.lpn join fetch l.location where l.location.locationBarcode = :locationBarcode and l.lpn.lpnBarcode = :lpnBarcode")
    Optional<LocationLPN> findByLocationBarcodeAndLPNBarcode(String locationBarcode, String lpnBarcode);

    @Query("select l from LocationLPN l join fetch l.lpn join fetch l.location where l.location.locationBarcode = :locationBarcode")
    Optional<LocationLPN> testingFindByLocationBarcode(String locationBarcode);
}
