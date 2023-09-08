package com.company.youse.repositrories.yousepay;

import com.company.youse.models.yousepay.B2CRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface B2CRequestRepository extends JpaRepository<B2CRequest, Long> {
    Optional<B2CRequest> findByCorrelationId(String correlationId);
    Optional<B2CRequest> findByOriginatorConversationId(String originatorConversationId);

}
