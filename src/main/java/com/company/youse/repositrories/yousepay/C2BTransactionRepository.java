package com.company.youse.repositrories.yousepay;

import com.company.youse.models.yousepay.C2BTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface C2BTransactionRepository extends JpaRepository<C2BTransaction,Long> {
    Optional<C2BTransaction> findByTransactionId(String transactionId);
}
