package leejoongseok.wms.location.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface LocationLPNRepository extends JpaRepository<LocationLPN, Long> {
    @Query("select l from LocationLPN l join fetch l.lpn where l.itemId = :itemId")
    List<LocationLPN> findByItemIdAndFetchJoinLPN(Long itemId);
}
