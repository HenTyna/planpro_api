package com.planprostructure.planpro.domain;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
@Getter
@Setter
public abstract class UpdatableEntity extends CreatableEntity {

    @CreationTimestamp
    @Column(name = "change_at", updatable = false)
    private LocalDateTime changeDateTime;

}
