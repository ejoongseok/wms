package leejoongseok.wms.common.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserActionHistoryRepository extends JpaRepository<UserActionHistory, Long> {
}
