package leejoongseok.wms.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Comment;
import org.springframework.util.Assert;

@Entity
@Table(name = "users")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User {
    @Id
    @GeneratedValue
    private Long id;

    @Column(name = "name", nullable = false)
    @Comment("사용자 이름")
    private String name;

    public User(final String name) {
        Assert.hasText(name, "사용자 이름은 필수입니다.");
        this.name = name;
    }
}
