package leejoongseok.wms.outbound.domain;

import com.google.common.annotations.VisibleForTesting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OutboundRepository extends JpaRepository<Outbound, Long> {

    @VisibleForTesting
    @Query("select o from Outbound o join fetch o.outboundItems oi where o.id = :outboundId")
    Optional<Outbound> testingFindById(Long outboundId);
}
