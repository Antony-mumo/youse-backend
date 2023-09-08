package com.company.youse.models;

import com.company.youse.platform.db.Auditable;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;

@Getter
@Setter
//@Audited <- hibernate envs should be installed first: very important unless we write custom listener
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class PO extends Auditable<String> {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

}
