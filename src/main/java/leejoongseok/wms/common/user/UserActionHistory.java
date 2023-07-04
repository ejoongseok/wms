package leejoongseok.wms.common.user;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;

import java.time.Duration;
import java.time.LocalDateTime;

@Entity
@Table(name = "user_action_history")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserActionHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "feature_name", insertable = true, updatable = false, nullable = false)
    @Comment("기능 이름")
    private String featureName;
    @Column(name = "request_uri", length = 1000, insertable = true, updatable = false, nullable = false)
    @Comment("요청 URI")
    private String requestURI;
    @Lob
    @Column(name = "query_parameters", insertable = true, updatable = false, nullable = true)
    @Comment("쿼리 파라미터")
    private String queryParameters;
    @Lob
    @Column(name = "request_body", insertable = true, updatable = false, nullable = true)
    @Comment("requestBody")
    private String requestBody;
    @Lob
    @Column(name = "request_headers", insertable = true, updatable = false, nullable = false)
    @Comment("요청 헤더")
    private String headers;
    @Column(name = "user_id", insertable = true, updatable = false, nullable = false)
    @Comment("USER ID")
    private Long userId;
    @Column(name = "start_time", insertable = true, updatable = false, nullable = false)
    @Comment("요청 일시")
    private LocalDateTime startTime;
    @Column(name = "end_time", insertable = true, updatable = false, nullable = false)
    @Comment("요청 일시")
    private LocalDateTime endTime;
    @Column(name = "process_time_in_seconds", insertable = true, updatable = false, nullable = false)
    @Comment("요청 일시")
    private Long processTimeInSeconds;

    public UserActionHistory(
            final String featureName,
            final String requestURI,
            final String queryParameters,
            final String requestBody,
            final String headers,
            final Long userId,
            final LocalDateTime startTime) {
        this.featureName = featureName;
        this.requestURI = requestURI;
        this.queryParameters = queryParameters;
        this.requestBody = requestBody;
        this.headers = headers;
        this.userId = userId;
        this.startTime = startTime;
    }

    public void registerProcessTime() {
        endTime = LocalDateTime.now();
        final Duration between = Duration.between(startTime, endTime);
        processTimeInSeconds = between.getSeconds();
    }
}
