package leejoongseok.wms.inbound.domain;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface InboundRepository extends JpaRepository<Inbound, Long> {
    @Query("select i from Inbound i join fetch i.inboundItems ii where i.id = :id")
    Optional<Inbound> testingFindInboundItemFetJoinByInboundId(long id);
}
