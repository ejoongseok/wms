package leejoongseok.wms.location.feature;

import leejoongseok.wms.location.domain.Location;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LocationRepository extends JpaRepository<Location, Long> {
}
