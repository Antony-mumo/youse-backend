package com.company.youse.platform.db;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

@Getter
@Setter
public abstract class Auditable<T> {

    @CreatedDate
    private long createTimestamp;

    @LastModifiedDate
    private long modifiedTimestamp;

    private String createdBy;

    private String updatedBy;

}
