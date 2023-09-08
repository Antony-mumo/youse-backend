package com.company.youse.repositrories.yousepay;

import com.company.youse.models.yousepay.STKPush;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface STKPushRepository extends JpaRepository<STKPush, Long> {
    Optional<STKPush> findByCheckoutRequestId(String checkoutRequestId);
}
