package leejoongseok.wms.outbound.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface OutboundRepository extends JpaRepository<Outbound, Long> {

    @Query("select o from Outbound o join fetch o.outboundItems oi where o.id = :outboundId")
    Optional<Outbound> testingFindById(Long outboundId);
}
