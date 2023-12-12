package com.jpa.data01.domain.base;

import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass @Getter
@EntityListeners(AuditingEntityListener.class)
public abstract class Period {
    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime modifiedDate;

//    @PrePersist
//    public void onPrePersist(){
//        this.createdDate = LocalDateTime.now();
//        this.modifiedDate = LocalDateTime.now();
//    }
//
//    @PreUpdate
//    public void onPreUpdate(){
//        this.modifiedDate = LocalDateTime.now();
//    }
}
