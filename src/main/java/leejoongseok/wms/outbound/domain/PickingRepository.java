package leejoongseok.wms.outbound.domain;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PickingRepository extends JpaRepository<Picking, Long> {
}
