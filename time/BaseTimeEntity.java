package com.example.project.time;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
// JPA Entity 클래스들이 TimeEntity를 상속할 경우 이 클래스의 변수인 createdDate와 modifiedDate도 Column으로 인식하게한다.
@MappedSuperclass
// TimeEntity 클래스에 Auditing 기능을 포함시킨다.
@EntityListeners(AuditingEntityListener.class)
public abstract class BaseTimeEntity {
    // Entity가 생성되면 , createdDate에 저장 시간이 자동으로 저장된다.
    @CreatedDate
    private LocalDateTime createdDate;

    // 조회한 Entity의 값을 변경할 때, 시간이 자동으로 저장된다.
    @LastModifiedDate
    private LocalDateTime modifiedDate;

}
