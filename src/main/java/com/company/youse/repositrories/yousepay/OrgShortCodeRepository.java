package com.company.youse.repositrories.yousepay;


import com.company.youse.enums.ShortCodeType;
import com.company.youse.models.yousepay.OrgShortCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrgShortCodeRepository extends JpaRepository<OrgShortCode,Long> {
    Optional<OrgShortCode> findByShortCodeTypeAndShortCode(ShortCodeType shortCodeType, String  code);
}

