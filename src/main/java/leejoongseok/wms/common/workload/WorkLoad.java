package leejoongseok.wms.common.workload;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

@Entity
@Table(name = "workload")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class WorkLoad {
    @Column(name = "worked_at", nullable = false, updatable = false)
    @Comment("작업량 측정 시각")
    private final LocalDateTime workedAt = LocalDateTime.now();
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "workload_type", nullable = false)
    @Comment("작업량 측정 유형")
    @Enumerated(EnumType.STRING)
    private WorkloadType workloadType;
    @Column(name = "user_id", nullable = false, updatable = false)
    @Comment("작업자 ID")
    private final Long userId = 1L;

    public WorkLoad(final WorkloadType workloadType) {
        Assert.notNull(workloadType, "작업량을 측정할 작업 유형은 필수입니다.");
        this.workloadType = workloadType;
    }
}
