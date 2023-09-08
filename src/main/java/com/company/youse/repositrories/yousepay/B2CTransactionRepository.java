package com.company.youse.repositrories.yousepay;

import com.company.youse.models.yousepay.B2CTransaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface B2CTransactionRepository extends JpaRepository<B2CTransaction, String> {
    Optional<B2CTransaction> findByTransactionID(String transactionId);

    Page<B2CTransaction> findAllByTenantIdAndCompletedAndCreateTimestampBetween(
            String tenantId, Boolean completed, Long from, Long to, Pageable page);

    Page<B2CTransaction> findAllByCompletedAndCreateTimestampBetween(Boolean completed, Long from, Long to, Pageable page);
}
