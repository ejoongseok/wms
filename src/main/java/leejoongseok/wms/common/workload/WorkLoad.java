package leejoongseok.wms.common.workload;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.util.Assert;

import java.time.LocalDateTime;

/**
 * 작업량 측정용 엔티티
 * 집품과 포장을 할때 작업량을 측정하기 위해 사용한다.
 * 집품/포장이 한번 완료될때마다 WorkLoad가 하나씩 생성된다.
 * 시간별로 다양한 통계를 낼 수 있을것으로 예상.
 */
@Entity
@Table(name = "workload")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class)
public class WorkLoad {
    @Column(name = "worked_at", nullable = false, updatable = false)
    @Comment("작업 일시")
    private final LocalDateTime workedAt = LocalDateTime.now();
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "workload_type", nullable = false)
    @Comment("작업량 측정 유형 ex)포장")
    @Enumerated(EnumType.STRING)
    private WorkloadType workloadType;
    @Column(name = "worker_id", nullable = false, updatable = false)
    @Comment("작업자 ID")
    @CreatedBy
    private Long workerId;

    public WorkLoad(final WorkloadType workloadType) {
        Assert.notNull(workloadType, "작업량을 측정할 작업 유형은 필수입니다.");
        this.workloadType = workloadType;
    }
}
